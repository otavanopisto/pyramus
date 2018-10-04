package fi.otavanopisto.pyramus.rest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import fi.otavanopisto.pyramus.applications.ApplicationUtils;
import fi.otavanopisto.pyramus.applications.DuplicatePersonException;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationAttachmentDAO;
import fi.otavanopisto.pyramus.dao.application.ApplicationDAO;
import fi.otavanopisto.pyramus.dao.application.ApplicationLogDAO;
import fi.otavanopisto.pyramus.dao.base.LanguageDAO;
import fi.otavanopisto.pyramus.dao.base.MunicipalityDAO;
import fi.otavanopisto.pyramus.dao.base.NationalityDAO;
import fi.otavanopisto.pyramus.dao.base.PersonDAO;
import fi.otavanopisto.pyramus.dao.base.SchoolDAO;
import fi.otavanopisto.pyramus.dao.system.SettingDAO;
import fi.otavanopisto.pyramus.dao.system.SettingKeyDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationAttachment;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationLogType;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationState;
import fi.otavanopisto.pyramus.domainmodel.base.Language;
import fi.otavanopisto.pyramus.domainmodel.base.Municipality;
import fi.otavanopisto.pyramus.domainmodel.base.Nationality;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.base.School;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.system.Setting;
import fi.otavanopisto.pyramus.domainmodel.system.SettingKey;
import fi.otavanopisto.pyramus.mailer.Mailer;
import fi.otavanopisto.pyramus.rest.annotation.Unsecure;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

@Path("/applications")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateful
@RequestScoped
public class ApplicationRESTService extends AbstractRESTService {

  private static final Logger logger = Logger.getLogger(ApplicationRESTService.class.getName());

  @Context
  private UriInfo uri;

  @Inject
  private HttpServletRequest httpRequest;
  
  @Inject
  private SchoolDAO schoolDAO;

  @Inject
  private MunicipalityDAO municipalityDAO;

  @Inject
  private NationalityDAO nationalityDAO;

  @Inject
  private LanguageDAO languageDAO;

  @Path("/createattachment")
  @POST
  @Unsecure
  @Consumes(MediaType.MULTIPART_FORM_DATA + ";charset=UTF-8")
  public Response createAttachment(MultipartFormDataInput multipart, @HeaderParam("Referer") String referer) {
    
    // Allow calls from within the application only
    
    if (!isApplicationCall(referer)) {
      return Response.status(Status.FORBIDDEN).build();
    }
    
    // Ensure attachment storage has been defined
    
    String attachmentsFolder = getSettingValue("applications.storagePath");
    if (StringUtils.isEmpty(attachmentsFolder)) {
      logger.log(Level.SEVERE, "applications.storagePath not set");
      return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }

    try {
      
      // Ensure file content exists and sanitize both its name and the folder where it will be stored 
      
      byte[] fileData = getFile(multipart, "file");
      
      String name = sanitizeFilename(getString(multipart, "name"));
      String attachmentFolder = sanitizeFilename(getString(multipart, "applicationId"));
      if (fileData == null || fileData.length == 0 || StringUtils.isEmpty(name) || StringUtils.isEmpty(attachmentFolder)) {
        logger.log(Level.SEVERE, "Application attachment preconditions not met");
        return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
      
      // Create attachment folder, if needed
      
      File folder = Paths.get(attachmentsFolder, attachmentFolder).toFile();
      if (!folder.exists()) {
        folder.mkdir();
      }

      // Enforce maximum attachment size
      
      long size = fileData.length + FileUtils.sizeOfDirectory(folder);
      if (size > 10485760) {
        logger.log(Level.WARNING,"Refusing attachment due to total attachment size over 10MB");
        return Response.status(Status.BAD_REQUEST).build();
      }
      
      // Create file, unless a file with the same name already exists
      
      File file = Paths.get(attachmentsFolder, attachmentFolder, name).toFile();
      if (file.exists()) {
        return Response.status(Status.CONFLICT).build();
      }
      
      // Write data to file
      
      FileUtils.writeByteArrayToFile(file, fileData);
      logger.log(Level.INFO, String.format("Stored application attachment %s", file.getAbsolutePath()));
      
      ApplicationAttachmentDAO applicationAttachmentDAO = DAOFactory.getInstance().getApplicationAttachmentDAO();
      applicationAttachmentDAO.create(attachmentFolder, name, fileData.length);
    }
    catch (Exception e) {
      logger.log(Level.SEVERE, "Failed to store application attachment", e);
      return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }
    return Response.noContent().build();
  }

  @Path("/listattachments/{ID}")
  @GET
  @Unsecure
  public Response listAttachments(@PathParam("ID") String applicationId, @HeaderParam("Referer") String referer) {

    // Allow calls from within the application only
    
    if (!isApplicationCall(referer)) {
      return Response.status(Status.FORBIDDEN).build();
    }

    // Ensure attachment storage has been defined
    
    String attachmentsFolder = getSettingValue("applications.storagePath");
    if (StringUtils.isEmpty(attachmentsFolder)) {
      logger.log(Level.SEVERE, "applications.storagePath not set");
      return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }
    
    ApplicationAttachmentDAO applicationAttachmentDAO = DAOFactory.getInstance().getApplicationAttachmentDAO();
    List<ApplicationAttachment> applicationAttachments = applicationAttachmentDAO.listByApplicationId(applicationId);

    List<Map<String, Object>> results = new ArrayList<>();
    for (ApplicationAttachment applicationAttachment : applicationAttachments) {
      Map<String, Object> attachmentInfo = new HashMap<>();
      attachmentInfo.put("id", applicationAttachment.getId());
      attachmentInfo.put("applicationId", applicationAttachment.getApplicationId());
      attachmentInfo.put("name", applicationAttachment.getName());
      attachmentInfo.put("description", applicationAttachment.getDescription());
      attachmentInfo.put("size", applicationAttachment.getSize());
      results.add(attachmentInfo);
    }
    return Response.ok(results).build();
  }

  @Path("/getattachment/{ID}")
  @GET
  @Unsecure
  @Produces("*/*")
  public Response getAttachment(@PathParam("ID") String applicationId, @QueryParam("attachment") String attachment, @HeaderParam("Referer") String referer) {

    // Allow calls from within the application only
    
    if (!isApplicationCall(referer)) {
      return Response.status(Status.FORBIDDEN).build();
    }

    // Ensure attachment storage has been defined
    
    String attachmentsFolder = getSettingValue("applications.storagePath");
    if (StringUtils.isEmpty(attachmentsFolder)) {
      logger.log(Level.SEVERE, "applications.storagePath not set");
      return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }
    
    // Sanitize folder and file names before retrieval

    applicationId = sanitizeFilename(applicationId);
    attachment = sanitizeFilename(attachment);
    if (StringUtils.isEmpty(applicationId) || StringUtils.isEmpty(attachment)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    // Serve file

    try {
      java.nio.file.Path path = Paths.get(attachmentsFolder, applicationId, attachment);
      File file = path.toFile();
      if (file.exists()) {
        String contentType = Files.probeContentType(path);
        byte[] data = FileUtils.readFileToByteArray(file);
        return Response.ok(data)
            .type(contentType)
            .header("Content-Length", data.length)
            .header("Content-Disposition", String.format("inline; filename=\"%s\"", attachment))
            .build();
      }
    }
    catch (Exception e) {
      logger.log(Level.SEVERE, String.format("Exception serving application attachment %s", attachment), e);
      return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }
    return Response.status(Status.NOT_FOUND).build();
  }
  
  @Path("/removeattachment/{ID}")
  @DELETE
  @Unsecure
  public Response removeAttachment(@PathParam("ID") String applicationId, @QueryParam("attachment") String attachment, @HeaderParam("Referer") String referer) {

    // Allow calls from within the application only
    
    if (!isApplicationCall(referer)) {
      return Response.status(Status.FORBIDDEN).build();
    }

    // Ensure attachment storage has been defined
    
    String attachmentsFolder = getSettingValue("applications.storagePath");
    if (StringUtils.isEmpty(attachmentsFolder)) {
      logger.log(Level.SEVERE, "applications.storagePath not set");
      return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }
    
    // Sanitize folder and file names before removal

    applicationId = sanitizeFilename(applicationId);
    attachment = sanitizeFilename(attachment);
    if (StringUtils.isEmpty(applicationId) || StringUtils.isEmpty(attachment)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    // Remove the file
    
    try {
      java.nio.file.Path path = Paths.get(attachmentsFolder, applicationId, attachment);
      File file = path.toFile();
      ApplicationAttachmentDAO applicationAttachmentDAO = DAOFactory.getInstance().getApplicationAttachmentDAO();
      ApplicationAttachment applicationAttachment = applicationAttachmentDAO.findByApplicationIdAndName(applicationId, attachment);
      if (applicationAttachment != null && file.exists() && file.delete()) {
        applicationAttachmentDAO.delete(applicationAttachment);
        logger.log(Level.INFO, String.format("Removed application attachment %s", file.getAbsolutePath()));
        return Response.noContent().build();
      }
    }
    catch (Exception e) {
      logger.log(Level.SEVERE, String.format("Exception serving application attachment %s", attachment), e);
      return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }
    return Response.status(Status.NOT_FOUND).build();
  }

  
  @Path("/getapplicationid")
  @POST
  @Unsecure
  public Response getApplicationId(Object object, @HeaderParam("Referer") String referer) {
    if (!isApplicationCall(referer)) {
      return Response.status(Status.FORBIDDEN).build();
    }
    
    JSONObject formData = JSONObject.fromObject(object);
    try {
      String lastName = formData.getString("field-last-name");
      if (lastName == null) {
        logger.log(Level.WARNING, "Refusing entry to application edit due to missing last name");
        return Response.status(Status.BAD_REQUEST).build();
      }
      String referenceCode = formData.getString("field-reference-code");
      if (referenceCode == null) {
        logger.log(Level.WARNING, "Refusing entry to application edit due to missing reference code");
        return Response.status(Status.BAD_REQUEST).build();
      }
      
      ApplicationDAO applicationDAO = DAOFactory.getInstance().getApplicationDAO();
      Application application = applicationDAO.findByLastNameAndReferenceCode(lastName, referenceCode);
      if (application == null || application.getArchived()) {
        return Response.status(Status.NOT_FOUND).build();
      }
      // TODO Evaluate application state; can the data still be modified by the applicant?

      Map<String, String> response = new HashMap<String, String>();
      response.put("applicationId", application.getApplicationId());

      return Response.ok(response).build();
    }
    catch (JSONException e) {
      logger.log(Level.SEVERE, String.format("Refusing entry to application edit due to malformatted json: %s", e.toString()));
      return Response.status(Status.BAD_REQUEST).build();
      
    }
  }
  
  @Path("/getapplicationdata/{ID}")
  @GET
  @Unsecure
  public Response getApplicationData(@PathParam("ID") String applicationId, @HeaderParam("Referer") String referer) {
    if (!isApplicationCall(referer)) {
      return Response.status(Status.FORBIDDEN).build();
    }
    ApplicationDAO applicationDAO = DAOFactory.getInstance().getApplicationDAO();
    Application application = applicationDAO.findByApplicationId(applicationId);
    if (application == null || application.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    return Response.ok(JSONObject.fromObject(application.getFormData())).build();
  }

  @Path("/saveapplication")
  @POST
  @Unsecure
  public Response createOrUpdateApplication(Object object, @HeaderParam("Referer") String referer) {
    if (!isApplicationCall(referer)) {
      return Response.status(Status.FORBIDDEN).build();
    }

    JSONObject formData = JSONObject.fromObject(object);
    
    // Validate key parts of form data
    
    try {
      String applicationId = formData.getString("field-application-id");
      if (applicationId == null) {
        logger.log(Level.WARNING, "Refusing application due to missing applicationId");
        return Response.status(Status.BAD_REQUEST).build();
      }
      String line = formData.getString("field-line");
      if (line == null) {
        logger.log(Level.WARNING, "Refusing application due to missing line");
        return Response.status(Status.BAD_REQUEST).build();
      }
      String firstName = formData.getString("field-first-names");
      if (firstName == null) {
        logger.log(Level.WARNING, "Refusing application due to missing first name");
        return Response.status(Status.BAD_REQUEST).build();
      }
      String lastName = formData.getString("field-last-name");
      if (lastName == null) {
        logger.log(Level.WARNING, "Refusing application due to missing last name");
        return Response.status(Status.BAD_REQUEST).build();
      }
      String email = formData.getString("field-email");
      if (email == null) {
        logger.log(Level.WARNING, "Refusing application due to missing email");
        return Response.status(Status.BAD_REQUEST).build();
      }
      
      // Store application

      Map<String, String> response = new HashMap<String, String>();
      
      ApplicationDAO applicationDAO = DAOFactory.getInstance().getApplicationDAO();
      
      String referenceCode = generateReferenceCode(lastName);
      Application application = applicationDAO.findByApplicationId(applicationId);

      // #765: Prevent multiple (active) applications with same e-mail
      
      List<Application> existingApplications = applicationDAO.listByEmailAndArchived(email, Boolean.FALSE);
      for (Application existingApplication : existingApplications) {
        if (application != null && existingApplication.getId().equals(application.getId())) {
          continue;
        }
        switch (existingApplication.getState()) {
        case PENDING:
        case PROCESSING:
        case WAITING_STAFF_SIGNATURE:
        case STAFF_SIGNED:
        case APPROVED_BY_SCHOOL:
        case APPROVED_BY_APPLICANT:
          return Response.status(Status.CONFLICT).build();
         default:
           break;
        }
      }
      
      if (application == null) {
        application = applicationDAO.create(
            applicationId,
            line,
            firstName,
            lastName,
            email,
            referenceCode,
            formData.toString(),
            !StringUtils.equals(line, "aineopiskelu"), // applicantEditable (#769: Internetix applicants may not edit submitted data)
            ApplicationState.PENDING);
        logger.log(Level.INFO, String.format("Created new %s application with id %s", line, application.getApplicationId()));
        
        // Automatic registration of new Internetix students
        
        boolean autoRegistration = StringUtils.equals("aineopiskelu", line);
        if (autoRegistration) {
          Person person = null;
          try {
            person = ApplicationUtils.resolvePerson(application);
          }
          catch (DuplicatePersonException dpe) {
            autoRegistration = false;
          }
          autoRegistration = autoRegistration && person == null;
        }
        if (autoRegistration) {
          Student student = ApplicationUtils.createPyramusStudent(application, null, null);
          if (student != null) {
            PersonDAO personDAO = DAOFactory.getInstance().getPersonDAO();
            personDAO.updateDefaultUser(student.getPerson(), student);
            String credentialToken = RandomStringUtils.randomAlphanumeric(32).toLowerCase();
            application = applicationDAO.updateApplicationStudentAndCredentialToken(application, student, credentialToken);
            application = applicationDAO.updateApplicationStateAsApplicant(application, ApplicationState.REGISTERED_AS_STUDENT);
            application = applicationDAO.updateApplicantEditable(application, Boolean.FALSE);
            ApplicationUtils.sendNotifications(application, httpRequest, null, true, null, true);
            ApplicationUtils.mailCredentialsInfo(httpRequest, student, application);
            response.put("autoRegistered", "true");
          }
          else {
            logger.log(Level.SEVERE, "Creating student for application %s failed. Falling back to manual processing");
            newApplicationPostProcessing(application);
          }
        }
        else {
          // If the application doesn't lead to auto-registration, send out the
          // usual confirmation and notification e-mails about a new application
          newApplicationPostProcessing(application);
        }
      }
      else {
        if (!StringUtils.equals(application.getLastName(), lastName)) {
          referenceCode = generateReferenceCode(lastName);
        }
        else {
          referenceCode = application.getReferenceCode();
        }
        boolean lineChanged = !StringUtils.equals(line, application.getLine());
        String oldLine = application.getLine(); 
        application = applicationDAO.update(
            application,
            line,
            firstName,
            lastName,
            email,
            referenceCode,
            formData.toString(),
            application.getState(),
            application.getApplicantEditable(),
            null);
        logger.log(Level.INFO, String.format("Updated %s application with id %s", line, application.getApplicationId()));
        modifiedApplicationPostProcessing(application);
        if (lineChanged) {
          String notification = String.format("Hakija vaihtoi hakemustaan linjalta <b>%s</b> linjalle <b>%s</b>",
              ApplicationUtils.applicationLineUiValue(oldLine), ApplicationUtils.applicationLineUiValue(line));
          ApplicationLogDAO applicationLogDAO = DAOFactory.getInstance().getApplicationLogDAO();
          applicationLogDAO.create(
              application,
              ApplicationLogType.HTML,
              notification,
              null);
          ApplicationUtils.sendNotifications(application, httpRequest, null, true, null, false);
        }
      }

      // Attachments
      
      if (formData.has("attachment-name") && formData.has("attachment-description")) {
        ApplicationAttachmentDAO applicationAttachmentDAO = DAOFactory.getInstance().getApplicationAttachmentDAO();
        if (JSONUtils.isArray(formData.get("attachment-name"))) {
          JSONArray attachmentNames = formData.getJSONArray("attachment-name");
          JSONArray attachmentDescriptions = formData.getJSONArray("attachment-description");
          for (int i = 0; i < attachmentNames.size(); i++) {
            String name = attachmentNames.getString(i);
            String description = attachmentDescriptions.getString(i);
            ApplicationAttachment applicationAttachment = applicationAttachmentDAO.findByApplicationIdAndName(applicationId, name);
            if (applicationAttachment == null) {
              logger.warning(String.format("Attachment %s for application %s not found", name, applicationId));
            }
            else {
              applicationAttachmentDAO.updateDescription(applicationAttachment, description);
            }
          }
        }
        else {
          String name = formData.getString("attachment-name");
          String description = formData.getString("attachment-description");
          ApplicationAttachment applicationAttachment = applicationAttachmentDAO.findByApplicationIdAndName(applicationId, name);
          if (applicationAttachment == null) {
            logger.warning(String.format("Attachment %s for application %s not found", name, applicationId));
          }
          else {
            applicationAttachmentDAO.updateDescription(applicationAttachment, description);
          }
        }
      }
      
      response.put("referenceCode", referenceCode);

      return Response.ok(response).build();
    }
    catch (JSONException e) {
      logger.log(Level.SEVERE, String.format("Refusing application save due to malformatted json: %s", e.toString()));
      return Response.status(Status.BAD_REQUEST).build();
    }
  }
  
  @Path("/municipalities")
  @GET
  @Unsecure
  public Response listMunicipalities() {
    List<Municipality> municipalities = municipalityDAO.listAll();
    municipalities.sort(new Comparator<Municipality>() {
      public int compare(Municipality o1, Municipality o2) {
        return o1.getName().compareTo(o2.getName());
      }
    });

    List<HashMap<String, String>> municipalityList = new ArrayList<HashMap<String, String>>();
    for (Municipality municipality : municipalities) {
      HashMap<String, String> municipalityData = new HashMap<String, String>();
      municipalityData.put("text", municipality.getName());
      municipalityData.put("value", municipality.getId().toString());
      municipalityList.add(municipalityData);
    }

    return Response.ok(municipalityList).build();
  }
  
  @Path("/schools")
  @GET
  @Unsecure
  public Response listSchools() {
    List<School> schools = schoolDAO.listUnarchived();
    String[] schoolNames = schools.stream().map(school -> school.getName()).sorted(String::compareToIgnoreCase).toArray(String[]::new);
    return Response.ok(schoolNames).build();
  }

  @Path("/contractschools")
  @GET
  @Unsecure
  public Response listContractSchools() {
    List<School> contractSchools = schoolDAO.listByVariable("contractSchool", "1");
    contractSchools.sort(new Comparator<School>() {
      public int compare(School o1, School o2) {
        return o1.getName().compareTo(o2.getName());
      }
    });

    // TODO schoolDAO.listByVariable should be the one to strip archived schools
    
    for (int i = contractSchools.size() - 1; i >= 0; i--) {
      if (Boolean.TRUE.equals(contractSchools.get(i).getArchived())) {
        contractSchools.remove(i);
      }
    }

    List<HashMap<String, String>> schoolList = new ArrayList<HashMap<String, String>>();
    for (School school : contractSchools) {
      HashMap<String, String> schoolData = new HashMap<String, String>();
      schoolData.put("text", school.getName());
      schoolData.put("value", school.getId().toString());
      schoolList.add(schoolData);
    }

    return Response.ok(schoolList).build();
  }

  @Path("/nationalities")
  @GET
  @Unsecure
  public Response listNationalities() {
    List<Nationality> nationalities = nationalityDAO.listAll();
    nationalities.sort(new Comparator<Nationality>() {
      public int compare(Nationality o1, Nationality o2) {
        return o1.getName().compareTo(o2.getName());
      }
    });

    List<HashMap<String, String>> nationalityList = new ArrayList<HashMap<String, String>>();

    for (Nationality nationality : nationalities) {
      HashMap<String, String> nationalityData = new HashMap<String, String>();
      nationalityData.put("text", nationality.getName());
      nationalityData.put("value", nationality.getId().toString());
      nationalityList.add(nationalityData);
    }

    return Response.ok(nationalityList).build();
  }

  @Path("/languages")
  @GET
  @Unsecure
  public Response listLanguages() {
    List<Language> languages = languageDAO.listAll();
    languages.sort(new Comparator<Language>() {
      public int compare(Language o1, Language o2) {
        return o1.getName().compareTo(o2.getName());
      }
    });

    List<HashMap<String, String>> languageList = new ArrayList<HashMap<String, String>>();

    for (Language language : languages) {
      HashMap<String, String> languageData = new HashMap<String, String>();
      languageData.put("text", language.getName());
      languageData.put("value", language.getId().toString());
      languageList.add(languageData);
    }

    return Response.ok(languageList).build();
  }

  private boolean isApplicationCall(String referer) {
    try {
      URI refererUri = new URI(referer);
      URI baseUri = uri.getBaseUri();
      return StringUtils.equals(refererUri.getHost(), baseUri.getHost());
    }
    catch (URISyntaxException e) {
      return false;
    }
  }

  private byte[] getFile(MultipartFormDataInput multipart, String field) {
    try {
      Map<String, List<InputPart>> form = multipart.getFormDataMap();
      List<InputPart> inputParts = form.get(field);
      if (inputParts.size() == 0) {
        return null;
      }
      else {
        return IOUtils.toByteArray(inputParts.get(0).getBody(InputStream.class, null));
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private String getString(MultipartFormDataInput multipart, String field) {
    try {
      Map<String, List<InputPart>> form = multipart.getFormDataMap();
      List<InputPart> inputParts = form.get(field);
      return inputParts.size() == 0 ? null : inputParts.get(0).getBodyAsString();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return null;
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
  
  private String generateReferenceCode(String lastName) {
    ApplicationDAO applicationDAO = DAOFactory.getInstance().getApplicationDAO();
    String referenceCode = RandomStringUtils.randomAlphabetic(6).toUpperCase();
    while (applicationDAO.findByLastNameAndReferenceCode(lastName, referenceCode) != null) {
      referenceCode = RandomStringUtils.randomAlphabetic(6).toUpperCase();
    }
    return referenceCode;
  }

  /**
   * Handles post-processing related to a new application. Sends a confirmation e-mail to the
   * applicant, a notification e-mail to application handlers, and adds an application log entry.  
   * 
   * @param application The new application
   */
  private void newApplicationPostProcessing(Application application) {

    // Mail to the applicant

    JSONObject formData = JSONObject.fromObject(application.getFormData());
    String line = formData.getString("field-line");
    String surname = application.getLastName();
    String referenceCode = application.getReferenceCode();
    String applicantMail = application.getEmail();
    String guardianMail = formData.getString("field-underage-email");
    try {
      // #769: Do not mail application edit instructions to Internetix applicants 
      if (!StringUtils.equals(application.getLine(), "aineopiskelu")) {

        // Confirmation mail subject and content
        
        String subject = IOUtils.toString(httpRequest.getServletContext().getResourceAsStream(
            String.format("/templates/applications/mails/mail-confirmation-%s-subject.txt", line)), "UTF-8");
        String content = IOUtils.toString(httpRequest.getServletContext().getResourceAsStream(
            String.format("/templates/applications/mails/mail-confirmation-%s-content.html", line)), "UTF-8");
        
        if (StringUtils.isBlank(subject) || StringUtils.isBlank(content)) {
          logger.log(Level.SEVERE, String.format("Confirmation mail for line %s not defined", line));
          return;
        }

        // Replace the dynamic parts of the mail content (edit link, surname and reference code)

        StringBuilder viewUrl = new StringBuilder();
        viewUrl.append(httpRequest.getScheme());
        viewUrl.append("://");
        viewUrl.append(httpRequest.getServerName());
        viewUrl.append(":");
        viewUrl.append(httpRequest.getServerPort());
        viewUrl.append("/applications/edit.page");

        content = String.format(content, viewUrl, surname, referenceCode);

        // Send mail to applicant or, for minors, applicant and guardian

        if (StringUtils.isEmpty(guardianMail)) {
          Mailer.sendMail(Mailer.JNDI_APPLICATION, Mailer.HTML, null, applicantMail, subject, content);
        }
        else {
          Mailer.sendMail(Mailer.JNDI_APPLICATION, Mailer.HTML, null, applicantMail, guardianMail, subject, content);
        }
      }

      // Handle notification mails and log entries

      ApplicationUtils.sendNotifications(application, httpRequest, null, true, null, true);
    }
    catch (IOException e) {
      logger.log(Level.SEVERE, "Unable to retrieve confirmation mail template", e);
    }
  }
  
  /**
   * Handles post-processing of existing applications modified by the applicant. If the
   * application already has an assigned handler with a valid e-mail address, send them
   * mail about the application having been modified by the applicant.
   */
  private void modifiedApplicationPostProcessing(Application application) {
    if (application.getHandler() != null && application.getHandler().getPrimaryEmail() != null) {

      StringBuilder viewUrl = new StringBuilder();
      viewUrl.append(httpRequest.getScheme());
      viewUrl.append("://");
      viewUrl.append(httpRequest.getServerName());
      viewUrl.append(":");
      viewUrl.append(httpRequest.getServerPort());
      viewUrl.append("/applications/view.page?application=");
      viewUrl.append(application.getId());

      String subject = "Hakija on muokannut hakemustaan";
      String content = String.format(
          "<p>Hakija <b>%s %s</b> (%s) on muokannut hakemustaan linjalle <b>%s</b>.</p>" +
          "<p>Pääset hakemustietoihin <b><a href=\"%s\">tästä linkistä</a></b>.</p>",
          application.getFirstName(),
          application.getLastName(),
          application.getEmail(),
          ApplicationUtils.applicationLineUiValue(application.getLine()),
          viewUrl);
      Mailer.sendMail(Mailer.JNDI_APPLICATION, Mailer.HTML, application.getEmail(),
          application.getHandler().getPrimaryEmail().getAddress(), subject, content);
    }
  }

}
