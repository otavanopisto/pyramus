package fi.otavanopisto.pyramus.json.applications;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationDAO;
import fi.otavanopisto.pyramus.dao.application.ApplicationSignaturesDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationSignatureState;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationSignatures;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationState;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.views.applications.ApplicationUtils;
import net.sf.json.JSONObject;

public class UpdateApplicationStateJSONRequestController extends JSONRequestController {

  private static final Logger logger = Logger.getLogger(UpdateApplicationStateJSONRequestController.class.getName());

  public void process(JSONRequestContext requestContext) {
    try {
      StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
      StaffMember staffMember = staffMemberDAO.findById(requestContext.getLoggedUserId());
      if (staffMember == null) {
        requestContext.getResponse().sendError(HttpServletResponse.SC_BAD_REQUEST);
        return;
      }
      Long id = requestContext.getLong("id");
      if (id == null) {
        requestContext.getResponse().sendError(HttpServletResponse.SC_BAD_REQUEST);
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
        requestContext.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
        return;
      }
      
      // Only do anything if the application state actually changes
      
      if (application.getState() != applicationState) {
        
        // Separate logic for when the application has been approved
        
        if (applicationState == ApplicationState.APPROVED_BY_SCHOOL) {
          
          // Gather required dynamic data from the application form
          
          JSONObject formData = JSONObject.fromObject(application.getFormData());
          String line = ApplicationUtils.applicationLineUiValue(application.getLine());
          String applicantName = String.format("%s %s", getFormValue(formData, "field-first-names"), getFormValue(formData, "field-last-name"));
          String ssn = ApplicationUtils.constructSSN(getFormValue(formData, "field-birthday"), getFormValue(formData, "field-ssn-end"));
          String address = String.format("%s, %s %s, %s",
              getFormValue(formData, "field-street-address"),
              getFormValue(formData, "field-zip-code"),
              getFormValue(formData, "field-city"),
              getFormValue(formData, "field-country"));
          String municipality = ApplicationUtils.municipalityUiValue(getFormValue(formData, "field-municipality"));
          String nationality = ApplicationUtils.nationalityUiValue(getFormValue(formData, "field-nationality"));
          String phone = getFormValue(formData, "field-phone");
          String email = getFormValue(formData, "field-email");

          // Make sure we have application signatures and school approval
          
          ApplicationSignaturesDAO applicationSignaturesDAO = DAOFactory.getInstance().getApplicationSignaturesDAO();
          ApplicationSignatures applicationSignatures = applicationSignaturesDAO.findByApplication(application);
          if (applicationSignatures == null || applicationSignatures.getStaffDocumentState() != ApplicationSignatureState.SIGNED) {
            logger.log(Level.WARNING, String.format("Application %s not signed by staff", application.getApplicationId()));
            requestContext.getResponse().sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
          }

          OnnistuuClient onnistuuClient = OnnistuuClient.getInstance();
          
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
          
          // Create and attach PDF to Onnistuu document (if not done before)

          if (applicationSignatures.getApplicantDocumentState() == ApplicationSignatureState.DOCUMENT_CREATED) {
            byte[] pdf = onnistuuClient.generateApplicantSignatureDocument(requestContext, line, applicantName, ssn, address, municipality, nationality, phone, email);
            onnistuuClient.addPdf(documentId, pdf);
            applicationSignatures = applicationSignaturesDAO.updateApplicantDocument(applicationSignatures, documentId, null, null,
                ApplicationSignatureState.PDF_UPLOADED);
          }

          // Create invitation (if not done before)

          if (applicationSignatures.getApplicantDocumentState() == ApplicationSignatureState.PDF_UPLOADED) {
            OnnistuuClient.Invitation invitation = onnistuuClient.createInvitation(documentId, email);
            applicationSignatures = applicationSignaturesDAO.updateApplicantDocument(applicationSignatures, documentId, invitation.getUuid(),
                invitation.getPassphrase(), ApplicationSignatureState.INVITATION_CREATED);
          }
        } // end of application has been approved logic
        
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
        
        ApplicationUtils.sendNotifications(application, requestContext.getRequest(), staffMember, false, null);
      }

      // Response parameters
      
      requestContext.addResponseParameter("id", application.getId());
      requestContext.addResponseParameter("state", application.getState());
      requestContext.addResponseParameter("stateUi", ApplicationUtils.applicationStateUiValue(application.getState()));
      requestContext.addResponseParameter("applicantEditable", application.getApplicantEditable());
      requestContext.addResponseParameter("handler", application.getHandler() == null ? null : application.getHandler().getFullName());
      requestContext.addResponseParameter("handlerId", application.getHandler() == null ? null : application.getHandler().getId());
      requestContext.addResponseParameter("lastModified", application.getLastModified().getTime());
    }
    catch (Exception e) {
      logger.log(Level.SEVERE, "Error updating application state", e);
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER };
  }

  private String getFormValue(JSONObject object, String key) {
    return object.has(key) ? object.getString(key) : null;
  }

}
