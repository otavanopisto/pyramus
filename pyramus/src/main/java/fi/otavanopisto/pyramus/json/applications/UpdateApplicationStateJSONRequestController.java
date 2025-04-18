package fi.otavanopisto.pyramus.json.applications;

import static fi.otavanopisto.pyramus.applications.ApplicationUtils.getFormValue;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.applications.ApplicationMailErrorHandler;
import fi.otavanopisto.pyramus.applications.ApplicationUtils;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationDAO;
import fi.otavanopisto.pyramus.dao.application.ApplicationLogDAO;
import fi.otavanopisto.pyramus.dao.application.ApplicationSignaturesDAO;
import fi.otavanopisto.pyramus.dao.base.PersonDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationLogType;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationSignatureState;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationSignatures;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationState;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.mailer.MailAttachment;
import fi.otavanopisto.pyramus.mailer.Mailer;
import net.sf.json.JSONObject;

public class UpdateApplicationStateJSONRequestController extends JSONRequestController {

  private static final Logger logger = Logger.getLogger(UpdateApplicationStateJSONRequestController.class.getName());

  public void process(JSONRequestContext requestContext) {
    try {
      StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
      StaffMember staffMember = requestContext.getLoggedUserId() == null ? null : staffMemberDAO.findById(requestContext.getLoggedUserId());
      if (staffMember == null) {
        fail(requestContext, "Et ole kirjautunut sisään");
        return;
      }
      Long id = requestContext.getLong("id");
      if (id == null) {
        fail(requestContext, "Puuttuva hakemustunnus");
        return;
      }
      ApplicationState applicationState = ApplicationState.valueOf(requestContext.getString("state"));
      Boolean lockApplication = requestContext.getBoolean("lockApplication");
      Boolean setHandler = requestContext.getBoolean("setHandler");
      Boolean removeHandler = requestContext.getBoolean("removeHandler");
      
      // Application update
      
      ApplicationDAO applicationDAO = DAOFactory.getInstance().getApplicationDAO();
      Application application = applicationDAO.findById(id);
      if (application == null) {
        fail(requestContext, "Puuttuva hakemus");
        return;
      }
      if (!ApplicationUtils.hasLineAccess(staffMember, application.getLine())) {
        logger.warning(String.format("User %d has no line access to application %d", staffMember.getId(), application.getId()));
        fail(requestContext, "Ei pääsyoikeutta hakemuksen linjalle");
        return;
      }
      
      // Fix for situations where two handlers would simultaneously start to process the same application or application is already handled
      
      if (applicationState == ApplicationState.PROCESSING) {
        switch (application.getState()) {
        case PROCESSING:
          fail(requestContext, "Hakemus on jo otettu käsittelyyn. Lataa sivu uudelleen.");
          return;
        case TRANSFERRED_AS_STUDENT:
        case REGISTERED_AS_STUDENT:
        case REGISTRATION_CHECKED:
          fail(requestContext, "Hakija on jo järjestelmässä. Lataa sivu uudelleen.");
          return;
        default:
          break;
        }
      }
      
      // Only do anything if the application state actually changes
      
      if (application.getState() != applicationState) {
        
        // Separate logic for when the application has been approved
        
        if (applicationState == ApplicationState.APPROVED_BY_SCHOOL) {
          
          // Gather required dynamic data from the application form
          
          JSONObject formData = JSONObject.fromObject(application.getFormData());
          String line = application.getLine();
          String applicantName = String.format("%s %s", getFormValue(formData, "field-first-names"), getFormValue(formData, "field-last-name"));
          String email = StringUtils.lowerCase(StringUtils.trim(getFormValue(formData, "field-email")));
          String nickname = getFormValue(formData, "field-nickname");
          String guardianMail = StringUtils.lowerCase(StringUtils.trim(getFormValue(formData, "field-underage-email")));
          boolean underageApplicant = ApplicationUtils.isUnderage(application);

          // Make sure we have application signatures and school approval
          
          ApplicationSignaturesDAO applicationSignaturesDAO = DAOFactory.getInstance().getApplicationSignaturesDAO();
          ApplicationSignatures applicationSignatures = applicationSignaturesDAO.findByApplication(application);
          if (applicationSignatures == null || applicationSignatures.getStaffDocumentState() != ApplicationSignatureState.SIGNED) {
            logger.log(Level.WARNING, String.format("Application %s not signed by staff", application.getApplicationId()));
            fail(requestContext, "Oppilaitos ei ole vielä allekirjoittanut hyväksymisasiakirjaa");
            return;
          }

          OnnistuuClient onnistuuClient = OnnistuuClient.getInstance();
          
          // 1430: For underage applicants, only create the acceptance PDF
          
          byte[] applicantDocument = null;
          if (ApplicationUtils.isUnderage(application)) {
            applicantDocument = ApplicationUtils.generateApplicantSignatureDocument(
                requestContext.getRequest(),
                application.getId(),
                line,
                applicantName,
                email,
                true);
          }
          else {

            // Create Onnistuu document (if not done before)

            String documentId = null;
            if (applicationSignatures.getApplicantDocumentId() == null) {
              documentId = onnistuuClient.createDocument(String.format("Vastaanotto: %s", applicantName));
              applicationSignatures = applicationSignaturesDAO.updateApplicantDocument(applicationSignatures, documentId, null, null,
                  ApplicationSignatureState.DOCUMENT_CREATED);
              
            }
            else {
              documentId = applicationSignatures.getApplicantDocumentId();
            }
            
            // Create and attach acceptance PDF to Onnistuu document (if not done before)

            if (applicationSignatures.getApplicantDocumentState() == ApplicationSignatureState.DOCUMENT_CREATED) {
              applicantDocument = ApplicationUtils.generateApplicantSignatureDocument(requestContext.getRequest(), application.getId(), line, applicantName, email, false);
              onnistuuClient.addPdf(documentId, applicantDocument);
              applicationSignatures = applicationSignaturesDAO.updateApplicantDocument(applicationSignatures, documentId, null, null,
                  ApplicationSignatureState.PDF_UPLOADED);
            }

            // Create invitation (if not done before)

            if (applicationSignatures.getApplicantDocumentState() == ApplicationSignatureState.PDF_UPLOADED) {
              OnnistuuClient.Invitation invitation = onnistuuClient.createInvitation(documentId, email);
              applicationSignatures = applicationSignaturesDAO.updateApplicantDocument(applicationSignatures, documentId, invitation.getUuid(),
                  invitation.getPassphrase(), ApplicationSignatureState.INVITATION_CREATED);
            }
          }
          
          // Construct accepted mail template
          
          byte[] staffDocument = null;
          try {
            staffDocument = onnistuuClient.getDocument(applicationSignatures.getStaffDocumentId());
            if (applicantDocument == null) {
              applicantDocument = onnistuuClient.getDocument(applicationSignatures.getApplicantDocumentId());
            }
          }
          catch (Exception e) {
            logger.log(Level.SEVERE, String.format("Application %s document fetch failure: %s", application.getApplicationId(), e.getMessage()));
            fail(requestContext, "Liitetiedostojen lataaminen epäonnistui");
            return;
          }
          
          String filename = StringUtils.replaceChars(StringUtils.lowerCase(applicantName), ' ', '-'); 
          List<MailAttachment> attachments = new ArrayList<MailAttachment>();
          attachments.add(new MailAttachment(String.format("%s-oppilaitos.pdf", filename), "application/pdf", staffDocument));
          attachments.add(new MailAttachment(String.format("%s-hakija.pdf", filename), "application/pdf", applicantDocument));
          
          String lineOrganization = ApplicationUtils.isOtaviaLine(application.getLine()) ? "Otavian" : "Otavan Opiston";
          String signerOrganization = ApplicationUtils.isOtaviaLine(application.getLine()) ? "Otavia" : "Otavan Opisto";
          String subject = String.format("Hyväksyminen %s opiskelijaksi", lineOrganization);

          String content = null;
          if (underageApplicant) {
            content = IOUtils.toString(requestContext.getServletContext().getResourceAsStream(
                "/templates/applications/mails/mail-accept-study-place-underage.html"), "UTF-8");
            content = String.format(content,
                nickname,
                lineOrganization,
                ApplicationUtils.applicationLineUiValue(line),
                staffMember.getFullName(),
                signerOrganization);
          }
          else {
            StringBuilder signUpUrl = new StringBuilder();
            signUpUrl.append(requestContext.getRequest().getScheme());
            signUpUrl.append("://");
            signUpUrl.append(requestContext.getRequest().getServerName());
            signUpUrl.append(":");
            signUpUrl.append(requestContext.getRequest().getServerPort());
            signUpUrl.append("/applications/accept.page?application=");
            signUpUrl.append(application.getApplicationId());

            content = IOUtils.toString(requestContext.getServletContext().getResourceAsStream(
                "/templates/applications/mails/mail-accept-study-place.html"), "UTF-8");
            content = String.format(content,
                nickname,
                lineOrganization,
                ApplicationUtils.applicationLineUiValue(line),
                signUpUrl.toString(),
                staffMember.getFullName(),
                signerOrganization);
          }
          
          // Send mail to applicant (and possible guardian)
          
          if (StringUtils.isBlank(guardianMail)) {
            Mailer.sendMail(Mailer.JNDI_APPLICATION, Mailer.HTML, null, email, subject, content, attachments, new ApplicationMailErrorHandler(application));
          }
          else {
            Mailer.sendMail(Mailer.JNDI_APPLICATION, Mailer.HTML, null, email, guardianMail, subject, content, attachments, new ApplicationMailErrorHandler(application));
          }
          
          // Add notification about sent mail
          
          ApplicationLogDAO applicationLogDAO = DAOFactory.getInstance().getApplicationLogDAO();
          applicationLogDAO.create(
            application,
            ApplicationLogType.HTML,
            String.format("<p>%s</p><p><b>%s</b></p>%s", "Hakijalle lähetetty ilmoitus opiskelijaksi hyväksymisestä", subject, content),
            staffMember);

        } // end of application has been approved logic
        else if (applicationState == ApplicationState.TRANSFERRED_AS_STUDENT) {
          
          // Separate logic for transferring the applicant as student
          
          Student student = ApplicationUtils.createPyramusStudent(application, staffMember); // throws exception if multiple persons or is staff
          PersonDAO personDAO = DAOFactory.getInstance().getPersonDAO();
          personDAO.updateDefaultUser(student.getPerson(), student);
          String credentialToken = RandomStringUtils.randomAlphanumeric(32).toLowerCase();
          application = applicationDAO.updateApplicationStudentAndCredentialToken(application, student, credentialToken);
          ApplicationUtils.mailCredentialsInfo(requestContext.getRequest(), student, application);
        }
        else if (applicationState == ApplicationState.REJECTED) {
          if (application.getState() == ApplicationState.REGISTERED_AS_STUDENT) {
            Student student = application.getStudent();
            StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
            studentDAO.archive(student);
          }
          // #4226: Applications of rejected Internetix students are removed immediately
          // #1355: No they are not (helpdesk ticket #740155). Like others, they will be removed after a year (via RemoveOldApplicationsScheduler)
          /*
          if (StringUtils.equals("aineopiskelu", application.getLine())) {
            ApplicationUtils.deleteApplication(application);
            requestContext.setRedirectURL(requestContext.getRequest().getContextPath() + "/applications/browse.page");
            return;
          }
          */
        }
        else if (applicationState == ApplicationState.PROCESSING) {
          // #1216: If a signed application is returned to Processing state, remove the
          // previous signatures so that the proper processing order can once again be followed
          ApplicationSignaturesDAO applicationSignaturesDAO = DAOFactory.getInstance().getApplicationSignaturesDAO();
          ApplicationSignatures applicationSignatures = applicationSignaturesDAO.findByApplication(application);
          if (applicationSignatures != null) {
            applicationSignaturesDAO.delete(applicationSignatures);
          }
        }
        
        // Update the actual application state
        
        application = applicationDAO.updateApplicationState(application, applicationState, staffMember);
        if (Boolean.TRUE.equals(lockApplication) && application.getApplicantEditable()) {
          application = applicationDAO.updateApplicantEditable(application, Boolean.FALSE, staffMember);
        }
        if (Boolean.TRUE.equals(setHandler)) {
          application = applicationDAO.updateApplicationHandler(application, staffMember);
        }
        if (Boolean.TRUE.equals(removeHandler)) {
          application = applicationDAO.updateApplicationHandler(application, null);
        }
        
        // Email notifications and log entries related to state change
        
        ApplicationUtils.sendNotifications(application, requestContext.getRequest(), staffMember, false, null, true);
      }

      // Response parameters
      
      requestContext.addResponseParameter("status", "OK");
      requestContext.addResponseParameter("id", application.getId());
      requestContext.addResponseParameter("state", application.getState());
      requestContext.addResponseParameter("stateUi", ApplicationUtils.applicationStateUiValue(application.getState()));
      requestContext.addResponseParameter("applicantEditable", application.getApplicantEditable());
      requestContext.addResponseParameter("handler", application.getHandler() == null ? null : application.getHandler().getFullName());
      requestContext.addResponseParameter("handlerId", application.getHandler() == null ? null : application.getHandler().getId());
      requestContext.addResponseParameter("lastModified", application.getLastModified().getTime());
    }
    catch (Exception e) {
      requestContext.addResponseParameter("status", "FAIL");
      requestContext.addResponseParameter("reason", e.getMessage());
      logger.log(Level.SEVERE, "Error updating application state", e);
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER };
  }

  private void fail(JSONRequestContext requestContext, String reason) {
    requestContext.addResponseParameter("status", "FAIL");
    requestContext.addResponseParameter("reason", reason);
  }

}
