package fi.otavanopisto.pyramus.json.applications;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationAttachmentDAO;
import fi.otavanopisto.pyramus.dao.application.ApplicationDAO;
import fi.otavanopisto.pyramus.dao.application.ApplicationLogDAO;
import fi.otavanopisto.pyramus.dao.application.ApplicationSignaturesDAO;
import fi.otavanopisto.pyramus.dao.base.AddressDAO;
import fi.otavanopisto.pyramus.dao.base.ContactTypeDAO;
import fi.otavanopisto.pyramus.dao.base.EmailDAO;
import fi.otavanopisto.pyramus.dao.base.PersonDAO;
import fi.otavanopisto.pyramus.dao.base.PhoneNumberDAO;
import fi.otavanopisto.pyramus.dao.file.StudentFileDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.system.SettingDAO;
import fi.otavanopisto.pyramus.dao.system.SettingKeyDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.dao.users.UserDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationAttachment;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationLogType;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationSignatureState;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationSignatures;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationState;
import fi.otavanopisto.pyramus.domainmodel.base.ContactType;
import fi.otavanopisto.pyramus.domainmodel.base.Email;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme;
import fi.otavanopisto.pyramus.domainmodel.students.Sex;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.system.Setting;
import fi.otavanopisto.pyramus.domainmodel.system.SettingKey;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.util.Mailer;
import fi.otavanopisto.pyramus.views.applications.ApplicationUtils;
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
          String nickname = getFormValue(formData, "field-nickname");
          String guardianMail = formData.getString("field-underage-email");

          // Make sure we have application signatures and school approval
          
          ApplicationSignaturesDAO applicationSignaturesDAO = DAOFactory.getInstance().getApplicationSignaturesDAO();
          ApplicationSignatures applicationSignatures = applicationSignaturesDAO.findByApplication(application);
          if (applicationSignatures == null || applicationSignatures.getStaffDocumentState() != ApplicationSignatureState.SIGNED) {
            logger.log(Level.WARNING, String.format("Application %s not signed by staff", application.getApplicationId()));
            fail(requestContext, "Oppilaitos ei ole vielä allekirjoittanut hyväksymisasiakirjaa");
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
          
          // Construct accepted mail template
          
          String staffDocUrl = String.format("https://www.onnistuu.fi/api/v1/invitation/%s/%s/files/0",
              applicationSignatures.getStaffInvitationId(),
              applicationSignatures.getStaffInvitationToken());

          StringBuilder signUpUrl = new StringBuilder();
          signUpUrl.append(requestContext.getRequest().getScheme());
          signUpUrl.append("://");
          signUpUrl.append(requestContext.getRequest().getServerName());
          signUpUrl.append(":");
          signUpUrl.append(requestContext.getRequest().getServerPort());
          signUpUrl.append("/applications/accept.page?application=");
          signUpUrl.append(application.getApplicationId());
          
          String subject = "Hyväksyminen Otavan Opiston opiskelijaksi";
          String content = IOUtils.toString(requestContext.getServletContext().getResourceAsStream(
              "/templates/applications/mail-accept-study-place.html"), "UTF-8");
          content = String.format(content,
              nickname,
              line.toLowerCase(),
              staffDocUrl,
              staffDocUrl,
              signUpUrl.toString(),
              signUpUrl.toString(),
              staffMember.getFullName());
          
          // Send mail to applicant (and possible guardian)
          
          if (StringUtils.isBlank(guardianMail)) {
            Mailer.sendMail(Mailer.JNDI_APPLICATION, Mailer.HTML, null, email, subject, content);
          }
          else {
            Mailer.sendMail(Mailer.JNDI_APPLICATION, Mailer.HTML, null, email, guardianMail, subject, content);
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
          
          Student student = createPyramusStudent(application, staffMember);
          application = applicationDAO.updateApplicationStudent(application, student);
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
        
        ApplicationUtils.sendNotifications(application, requestContext.getRequest(), staffMember, false, null);
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
  
  private Student createPyramusStudent(Application application, StaffMember staffMember) throws DuplicatePersonException {
    PhoneNumberDAO phoneNumberDAO = DAOFactory.getInstance().getPhoneNumberDAO();
    AddressDAO addressDAO = DAOFactory.getInstance().getAddressDAO();
    EmailDAO emailDAO = DAOFactory.getInstance().getEmailDAO();
    ContactTypeDAO contactTypeDAO = DAOFactory.getInstance().getContactTypeDAO();
    PersonDAO personDAO = DAOFactory.getInstance().getPersonDAO();
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    ApplicationAttachmentDAO applicationAttachmentDAO = DAOFactory.getInstance().getApplicationAttachmentDAO();
    
    JSONObject formData = JSONObject.fromObject(application.getFormData());
    
    // Create person (if needed)
    
    Person person = resolvePerson(application);
    if (person == null) {
      String birthdayStr = getFormValue(formData, "field-birthday");
      String ssnEnd = getFormValue(formData, "field-ssn-end");
      try {
        Date birthday = StringUtils.isEmpty(birthdayStr) ? null : new SimpleDateFormat("d.M.yyyy").parse(birthdayStr);
        String ssn = StringUtils.isBlank(ssnEnd) ? null : ApplicationUtils.constructSSN(birthdayStr, ssnEnd);
        Sex sex = ApplicationUtils.resolveGender(getFormValue(formData, "field-sex"), ssnEnd);
        person = personDAO.create(birthday, ssn, sex, null, Boolean.FALSE);
      }
      catch (ParseException e) {
        logger.severe(String.format("Invalid birthday format in application entity %d", application.getId()));
        return null;
      }
    }
    
    // Determine correct study programme
    
    StudyProgramme studyProgramme = ApplicationUtils.resolveStudyProgramme(
        getFormValue(formData, "field-line"),
        getFormValue(formData, "field-foreign-line"));
    if (studyProgramme == null) {
      logger.severe(String.format("Unable to resolve study programme of application entity %d", application.getId()));
      return null;
    }
    
    // Create student
    
    Student student = studentDAO.create(
        person,
        getFormValue(formData, "field-first-names"),
        getFormValue(formData, "field-last-name"),
        getFormValue(formData, "field-nickname"),
        null, // additionalInfo,
        null, // studyTimeEnd (TODO can this be resolved?)
        ApplicationUtils.resolveStudentActivityType(getFormValue(formData, "field-job")),
        ApplicationUtils.resolveStudentExaminationType(getFormValue(formData, "field-internetix-contract-school-degree")),
        null, // student educational level (entity)
        null, // education (string)
        ApplicationUtils.resolveNationality(getFormValue(formData, "field-nationality")),
        ApplicationUtils.resolveMunicipality(getFormValue(formData, "field-municipality")),
        ApplicationUtils.resolveLanguage(getFormValue(formData, "field-language")),
        ApplicationUtils.resolveSchool(getFormValue(formData, "field-internetix-contract-school")),
        studyProgramme,
        null, // curriculum (TODO can this be resolved?)
        null, // previous studies (double)
        new Date(), // study start date
        null, // study end date
        null, // study end reason
        null, // study end text
        Boolean.FALSE); // archived
    
    // Main contact type
    
    ContactType contactType = contactTypeDAO.findById(1L); // Koti (unique)

    // Attach email
    
    String email = getFormValue(formData, "field-email");
    emailDAO.create(student.getContactInfo(), contactType, Boolean.TRUE, email.toLowerCase());
    
    // Attach address
    
    addressDAO.create(
        student.getContactInfo(),
        contactType,
        String.format("%s %s", getFormValue(formData, "field-nickname"), getFormValue(formData, "field-last-name")),
        getFormValue(formData, "field-street-address"),
        getFormValue(formData, "field-zip-code"),
        getFormValue(formData, "field-city"),
        getFormValue(formData, "field-country"),
        Boolean.TRUE);

    // Attach phone
    
    phoneNumberDAO.create(
        student.getContactInfo(),
        contactType,
        Boolean.TRUE,
        getFormValue(formData, "field-phone"));
    
    // Guardian info (if email is present, all other fields are required and present, too)
    
    email = getFormValue(formData, "field-underage-email");
    if (!StringUtils.isBlank(email)) {
      
      // Attach email
      
      contactType = contactTypeDAO.findById(5L); // Yhteyshenkilö (non-unique)
      emailDAO.create(student.getContactInfo(), contactType, Boolean.FALSE, email.toLowerCase());

      // Attach address
      
      addressDAO.create(
          student.getContactInfo(),
          contactType,
          String.format("%s %s", getFormValue(formData, "field-underage-first-name"), getFormValue(formData, "field-underage-last-name")),
          getFormValue(formData, "field-underage-street-address"),
          getFormValue(formData, "field-underage-zip-code"),
          getFormValue(formData, "field-underage-city"),
          getFormValue(formData, "field-underage-country"),
          Boolean.FALSE);

      // Attach phone
      
      phoneNumberDAO.create(
          student.getContactInfo(),
          contactType,
          Boolean.FALSE,
          getFormValue(formData, "field-underage-phone"));
    }
    
    // Attachments
    
    List<ApplicationAttachment> attachments = applicationAttachmentDAO.listByApplicationId(application.getApplicationId());
    if (!attachments.isEmpty()) {
      String attachmentsFolder = getSettingValue("applications.storagePath");
      if (StringUtils.isNotEmpty(attachmentsFolder)) {
        StudentFileDAO studentFileDAO = DAOFactory.getInstance().getStudentFileDAO();
        String applicationId = sanitizeFilename(application.getApplicationId());
        for (ApplicationAttachment attachment : attachments) {
          String attachmentFileName = sanitizeFilename(attachment.getName());
          try {
            java.nio.file.Path path = Paths.get(attachmentsFolder, applicationId, attachmentFileName);
            File file = path.toFile();
            if (file.exists()) {
              String contentType = Files.probeContentType(path);
              byte[] data = FileUtils.readFileToByteArray(file);
              studentFileDAO.create(student, attachmentFileName, attachmentFileName, null, contentType, data, staffMember);
            }
          }
          catch (IOException e) {
            logger.log(Level.WARNING, String.format("Exception processing attachment %s of application %d",
                attachment.getName(), application.getId()), e);
          }
        }
      }
    }
    
    return student;
  }
  
  private Person resolvePerson(Application application) throws DuplicatePersonException {
    EmailDAO emailDAO = DAOFactory.getInstance().getEmailDAO();
    PersonDAO personDAO = DAOFactory.getInstance().getPersonDAO();
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    JSONObject applicationData = JSONObject.fromObject(application.getFormData());
    
    // Person by social security number
    
    Map<Long, Person> existingPersons = new HashMap<Long, Person>();
    boolean hasSsn = applicationData.getString("field-ssn-end") != null;
    if (hasSsn) {
      
      // Given SSN
      
      String ssn = hasSsn ? ApplicationUtils.constructSSN(applicationData.getString("field-birthday"), applicationData.getString("field-ssn-end")) : null;
      List<Person> persons = personDAO.listBySSNUppercase(ssn);
      for (Person person : persons) {
        existingPersons.put(person.getId(), person);
      }
      
      // SSN with "wrong" delimiter
      
      char[] ssnChars = ssn.toCharArray();
      ssnChars[6] = ssnChars[6] == 'A' ? '-' : 'A';
      ssn = ssnChars.toString();
      persons = personDAO.listBySSNUppercase(ssn);
      for (Person person : persons) {
        existingPersons.put(person.getId(), person);
      }
    }
    
    // Person by email address
    
    String emailAddress = StringUtils.lowerCase(StringUtils.trim(applicationData.getString("field-email")));
    List<Email> emails = emailDAO.listByAddressLowercase(emailAddress);
    for (Email email : emails) {
      if (email.getContactType() != null && Boolean.FALSE.equals(email.getContactType().getNonUnique())) {
        User user = userDAO.findByContactInfo(email.getContactInfo());
        if (user != null) {
          Person person = user.getPerson();
          if (person != null) {
            existingPersons.put(person.getId(), person);
          }
        }
      }
    }

    if (existingPersons.size() > 1) {
      StringBuilder sb = new StringBuilder();
      for (Person person : existingPersons.values()) {
        if (sb.length() > 0) {
          sb.append(",");
        }
        sb.append(person.getId());
      }
      logger.severe(String.format("Persons with duplicate SSN or unique email: %s", sb));
      throw new DuplicatePersonException();
    }
    else {
      return existingPersons.values().iterator().next();
    }
  }

  private String getFormValue(JSONObject object, String key) {
    return object.has(key) ? object.getString(key) : null;
  }

  private String getSettingValue(String key) {
    SettingKeyDAO settingKeyDAO = DAOFactory.getInstance().getSettingKeyDAO();
    SettingKey settingKey = settingKeyDAO.findByName(key);
    if (settingKey != null) {
      SettingDAO settingDAO = DAOFactory.getInstance().getSettingDAO();
      Setting setting = settingDAO.findByKey(settingKey);
      if (setting != null && setting.getValue() != null) {
        return setting.getValue();
      }
    }
    return null;
  }

  /**
   * Sanitizes the given filename so that it can be safely used as part of a file path. The filename
   * is first stripped of traditional invalid filename characters (\ / : * ? " < > |) and then of all
   * leading and trailing periods.
   * 
   * @param filename Filename to be sanitized
   * 
   * @return Sanitized filename
   */
  private String sanitizeFilename(String filename) {
    return StringUtils.stripEnd(StringUtils.stripStart(StringUtils.strip(filename, "\\/:*?\"<>|"), "."), ".");
  }
  
  private class DuplicatePersonException extends Exception {
    private static final long serialVersionUID = -1840064717234172676L;
  }

}
