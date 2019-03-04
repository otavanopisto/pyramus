package fi.otavanopisto.pyramus.applications;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationAttachmentDAO;
import fi.otavanopisto.pyramus.dao.application.ApplicationDAO;
import fi.otavanopisto.pyramus.dao.application.ApplicationLogDAO;
import fi.otavanopisto.pyramus.dao.application.ApplicationNotificationDAO;
import fi.otavanopisto.pyramus.dao.application.ApplicationSignaturesDAO;
import fi.otavanopisto.pyramus.dao.base.AddressDAO;
import fi.otavanopisto.pyramus.dao.base.ContactTypeDAO;
import fi.otavanopisto.pyramus.dao.base.EmailDAO;
import fi.otavanopisto.pyramus.dao.base.LanguageDAO;
import fi.otavanopisto.pyramus.dao.base.MunicipalityDAO;
import fi.otavanopisto.pyramus.dao.base.NationalityDAO;
import fi.otavanopisto.pyramus.dao.base.PersonDAO;
import fi.otavanopisto.pyramus.dao.base.PhoneNumberDAO;
import fi.otavanopisto.pyramus.dao.base.SchoolDAO;
import fi.otavanopisto.pyramus.dao.base.StudyProgrammeDAO;
import fi.otavanopisto.pyramus.dao.file.StudentFileDAO;
import fi.otavanopisto.pyramus.dao.students.StudentActivityTypeDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.students.StudentExaminationTypeDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.dao.users.UserDAO;
import fi.otavanopisto.pyramus.dao.users.UserIdentificationDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationAttachment;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationLog;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationLogType;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationNotification;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationSignatures;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationState;
import fi.otavanopisto.pyramus.domainmodel.base.ContactType;
import fi.otavanopisto.pyramus.domainmodel.base.Email;
import fi.otavanopisto.pyramus.domainmodel.base.Language;
import fi.otavanopisto.pyramus.domainmodel.base.Municipality;
import fi.otavanopisto.pyramus.domainmodel.base.Nationality;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.base.School;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme;
import fi.otavanopisto.pyramus.domainmodel.students.Sex;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentActivityType;
import fi.otavanopisto.pyramus.domainmodel.students.StudentExaminationType;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.domainmodel.users.UserIdentification;
import fi.otavanopisto.pyramus.framework.PyramusFileUtils;
import fi.otavanopisto.pyramus.framework.SettingUtils;
import fi.otavanopisto.pyramus.mailer.Mailer;
import fi.otavanopisto.pyramus.plugin.auth.AuthenticationProviderVault;
import fi.otavanopisto.pyramus.plugin.auth.InternalAuthenticationProvider;
import net.sf.json.JSONObject;

public class ApplicationUtils {

  private static final Logger logger = Logger.getLogger(ApplicationUtils.class.getName());

  public static String applicationStateUiValue(ApplicationState applicationState) {
    if (applicationState != null) {
      switch (applicationState) {
      case PENDING:
        return "Jätetty";
      case PROCESSING:
        return "Käsittelyssä";
      case WAITING_STAFF_SIGNATURE:
        return "Odottaa virallista hyväksyntää";
      case STAFF_SIGNED:
        return "Hyväksyntä allekirjoitettu";
      case APPROVED_BY_SCHOOL:
        return "Hyväksytty";
      case APPROVED_BY_APPLICANT:
        return "Opiskelupaikka vastaanotettu";
      case TRANSFERRED_AS_STUDENT:
        return "Siirretty opiskelijaksi";
      case REGISTERED_AS_STUDENT:
        return "Ilmoittautunut aineopiskelijaksi";
      case REGISTRATION_CHECKED:
        return "Tiedot tarkistettu";
      case REJECTED:
        return "Hylätty";
       default:
         return null;
      }
    }
    return null;
  }

  public static String applicationLineUiValue(String value) {
    if (value != null) {
      switch (value) {
      case "aineopiskelu":
        return "Aineopiskelu";
      case "nettilukio":
        return "Nettilukio";
      case "nettipk":
        return "Nettiperuskoulu";
      case "aikuislukio":
        return "Aikuislukio";
      case "bandilinja":
        return "Bändilinja";
      case "kasvatustieteet":
        return "Kasvatustieteen linja";
      case "laakislinja":
        return "Lääkislinja";
      case "mk":
        return "Maahanmuuttajakoulutukset";
      default:
        return null;
      }
    }
    return null;
  }
  
  public static boolean isValidLine(String line) {
    return applicationLineUiValue(line) != null;
  }

  public static String municipalityUiValue(String value) {
    if (StringUtils.isBlank(value)) {
      return null;
    }
    else if (value.equals("none")) {
      return "Ei kotikuntaa Suomessa";
    }
    Long municipalityId = Long.valueOf(value);
    MunicipalityDAO municipalityDAO = DAOFactory.getInstance().getMunicipalityDAO();
    Municipality municipality = municipalityDAO.findById(municipalityId);
    return municipality == null ? null : municipality.getName();
  }
  
  public static Municipality resolveMunicipality(String value) {
    if (StringUtils.isBlank(value) || StringUtils.equals(value,  "none")) {
      return null;
    }
    MunicipalityDAO municipalityDAO = DAOFactory.getInstance().getMunicipalityDAO();
    return municipalityDAO.findById(Long.valueOf(value));
  }

  public static String nationalityUiValue(String value) {
    if (StringUtils.isBlank(value)) {
      return null;
    }
    Long nationalityId = Long.valueOf(value);
    NationalityDAO nationalityDAO = DAOFactory.getInstance().getNationalityDAO();
    Nationality nationality = nationalityDAO.findById(nationalityId);
    return nationality == null ? null : nationality.getName();
  }
  
  public static String previousStudiesUiValue(String value) {
    if (value != null) {
      switch (value) {
      case "perus":
        return "Peruskoulu";
      case "kansa":
        return "Kansakoulu";
      case "lukio":
        return "Lukio (keskeytynyt)";
      case "ammatillinen":
        return "Ammatillinen 2. aste";
      case "korkea":
        return "Korkeakoulu";
      case "muu":
        return "Muu";
      default:
        return null;
      }
    }
    return null;
  }

  public static Nationality resolveNationality(String value) {
    if (StringUtils.isBlank(value)) {
      return null;
    }
    NationalityDAO nationalityDAO = DAOFactory.getInstance().getNationalityDAO();
    return nationalityDAO.findById(Long.valueOf(value));
  }

  public static String languageUiValue(String value) {
    if (StringUtils.isBlank(value)) {
      return null;
    }
    Long languageId = Long.valueOf(value);
    LanguageDAO languageDAO = DAOFactory.getInstance().getLanguageDAO();
    Language language = languageDAO.findById(languageId);
    return language == null ? null : language.getName();
  }

  public static Language resolveLanguage(String value) {
    if (StringUtils.isBlank(value)) {
      return null;
    }
    LanguageDAO languageDAO = DAOFactory.getInstance().getLanguageDAO();
    return languageDAO.findById(Long.valueOf(value));
  }

  public static School resolveSchool(String value) {
    if (StringUtils.isBlank(value) || StringUtils.equals(value, "muu")) {
      return null;
    }
    SchoolDAO schoolDAO = DAOFactory.getInstance().getSchoolDAO();
    return schoolDAO.findById(Long.valueOf(value));
  }
  
  public static String genderUiValue(String value) {
    switch (value) {
    case "mies":
      return "Mies";
    case "nainen":
      return "Nainen";
    case "muu":
      return "Muu";
    default:
      return null;
    }
  }
  
  public static Sex resolveGender(String genderValue, String ssnEnd) {
    if (!StringUtils.isBlank(ssnEnd) && !StringUtils.equalsIgnoreCase(ssnEnd, "XXXX") && ssnEnd.length() == 4) {
      char c = ssnEnd.charAt(2);
      return c == '1' || c == '3' || c == '5' || c == '7' || c == '9' ? Sex.MALE : Sex.FEMALE;
    }
    return "nainen".equals(genderValue) ? Sex.FEMALE : Sex.MALE;
  }
  
  public static StudentExaminationType resolveStudentExaminationType(String examinationType) {
    StudentExaminationTypeDAO studentExaminationTypeDAO = DAOFactory.getInstance().getStudentExaminationTypeDAO();
    switch (examinationType) {
    case "muu":
      return studentExaminationTypeDAO.findById(1L); // Muu tutkinto
    case "ammatillinen-perus":
      return studentExaminationTypeDAO.findById(2L); // Ammatillinen perustutkinto
    case "korkeakoulu":
      return studentExaminationTypeDAO.findById(3L); // Korkeakoulututkinto
    case "kaksoistutkinto":
      return studentExaminationTypeDAO.findById(4L); // Kaksoistutkinto
    case "yo-tutkinto":
      return studentExaminationTypeDAO.findById(5L); // YO-tutkinto / lukion oppimäärä
    case "oppisopimus":
      return studentExaminationTypeDAO.findById(6L); // Oppisopimuskoulutus
    case "peruskoulu":
      return studentExaminationTypeDAO.findById(7L); // Peruskoulun oppimäärä
    case "ylempi-kk":
      return studentExaminationTypeDAO.findById(8L); // Ylempi korkeakoulututkinto
    default:
      return null;
    }
  }
  
  public static StudentActivityType resolveStudentActivityType(String activityType) {
    StudentActivityTypeDAO studentActivityTypeDAO = DAOFactory.getInstance().getStudentActivityTypeDAO();
    switch (activityType) {
    case "tyollinen":
      return studentActivityTypeDAO.findById(1L); // Työllinen
    case "tyoton":
      return studentActivityTypeDAO.findById(2L); // Työtön
    case "opiskelija":
      return studentActivityTypeDAO.findById(3L); // Opiskelija
    case "elakelainen":
      return studentActivityTypeDAO.findById(4L); // Eläkeläinen
    case "muu":
      return studentActivityTypeDAO.findById(5L); // Muu
    default:
      return null;
    }
  }
  
  public static StudyProgramme resolveStudyProgramme(String line, String foreignLine) {
    StudyProgrammeDAO studyProgrammeDAO = DAOFactory.getInstance().getStudyProgrammeDAO();
    switch (line) {
    case "aineopiskelu":
      return studyProgrammeDAO.findById(13L); // Internetix/lukio
    case "nettilukio":
      return studyProgrammeDAO.findById(6L); // Nettilukio
    case "nettipk":
      return studyProgrammeDAO.findById(7L); // Nettiperuskoulu
    case "aikuislukio":
      return studyProgrammeDAO.findById(1L); // Aikuislukio
    case "bandilinja":
      return studyProgrammeDAO.findById(8L); // Bändilinja/vapaa
    case "kasvatustieteet":
      return null; // TODO Does not yet have a study programme in Pyramus 
    case "laakislinja":
      return studyProgrammeDAO.findById(31L); // Lääketieteen opintoihin valmentava koulutus
    case "mk":
      switch (foreignLine) {
      case "apa":
        return studyProgrammeDAO.findById(29L); // Aikuisten perusopetuksen alkuvaiheen opetus
      case "luku":
        return studyProgrammeDAO.findById(33L); // Aikuisten perusopetuksen lukutaitovaihe
      case "pk":
        return studyProgrammeDAO.findById(11L); // Peruskoululinja
      case "luva":
        return studyProgrammeDAO.findById(27L); // LUVA
      case "lisaopetus":
        return studyProgrammeDAO.findById(15L); // Monikulttuurinen peruskoululinja
      default:
        return null;
      }
    default:
      return null;
    }
  }
  
  public static void sendNotifications(Application application, HttpServletRequest request, StaffMember staffMember, boolean newApplication, String notificationPostfix, boolean doLogEntry) {
    ApplicationNotificationDAO applicationNotificationDAO = DAOFactory.getInstance().getApplicationNotificationDAO();
    List<ApplicationNotification> notifications = applicationNotificationDAO.listByNullOrLineAndState(
        application.getLine(), application.getState());
    Set<String> emails = new HashSet<>();
    for (ApplicationNotification notification : notifications) {
      Set<User> users = notification.getUsers();
      for (User user : users) {
        if (user.getPrimaryEmail() != null) {
          emails.add(user.getPrimaryEmail().getAddress());
        }
      }
    }
    if (!emails.isEmpty()) {
      StringBuilder viewUrl = new StringBuilder();
      viewUrl.append(request.getScheme());
      viewUrl.append("://");
      viewUrl.append(request.getServerName());
      viewUrl.append(":");
      viewUrl.append(request.getServerPort());
      viewUrl.append("/applications/view.page?application=");
      viewUrl.append(application.getId());
      
      String mailSubject = null;
      String mailContent = null;
      if (newApplication) {
        mailSubject = String.format("Uusi hakemus linjalle %s [%s %s]",
            applicationLineUiValue(application.getLine()),
            application.getFirstName(),
            application.getLastName());
        mailContent = String.format(
          "<p>Hakija <b>%s %s</b> (%s) on jättänyt hakemuksen linjalle <b>%s</b>.</p>" +
          "<p>Pääset hakemustietoihin <b><a href=\"%s\">tästä linkistä</a></b>.</p>",
          application.getFirstName(),
          application.getLastName(),
          application.getEmail(),
          applicationLineUiValue(application.getLine()),
          viewUrl);
      }
      else {
        mailSubject = String.format("Hakemuksen tila on muuttunut [%s %s]",
            application.getFirstName(),
            application.getLastName());
        mailContent = String.format(
          "<p>Hakijan <b>%s %s</b> (%s) hakemus linjalle <b>%s</b> on siirtynyt tilaan <b>%s</b>.</p>" +
          "<p>Pääset hakemustietoihin <b><a href=\"%s\">tästä linkistä</a></b>.</p>",
          application.getFirstName(),
          application.getLastName(),
          application.getEmail(),
          applicationLineUiValue(application.getLine()),
          applicationStateUiValue(application.getState()),
          viewUrl);
      }

      Mailer.sendMail(Mailer.JNDI_APPLICATION,
          Mailer.HTML,
          staffMember == null ? null : staffMember.getPrimaryEmail().getAddress(),
          emails,
          mailSubject,
          mailContent);
    }
    
    // Log entry
    
    if (doLogEntry) {
      String notification = String.format("Hakemus on siirtynyt tilaan <b>%s</b>", ApplicationUtils.applicationStateUiValue(application.getState()));
      if (notificationPostfix != null) {
        notification = String.format("%s<br/>%s", notification, notificationPostfix);
      }
      ApplicationLogDAO applicationLogDAO = DAOFactory.getInstance().getApplicationLogDAO();
      applicationLogDAO.create(
          application,
          ApplicationLogType.HTML,
          notification,
          staffMember);
    }
  }

  public static String extractSSN(Application application) {
    if (application == null) {
      return null;
    }
    JSONObject formData = JSONObject.fromObject(application.getFormData());
    return constructSSN(getFormValue(formData, "field-birthday"), getFormValue(formData, "field-ssn-end"));
  }

  public static String constructSSN(String birthday, String ssnEnd) {
    if (StringUtils.isBlank(birthday) || StringUtils.isBlank(ssnEnd) || StringUtils.equalsIgnoreCase(ssnEnd, "XXXX")) {
      return null;
    }
    try {
      StringBuffer ssn = new StringBuffer();
      Date d = new SimpleDateFormat("d.M.yyyy").parse(birthday);
      ssn.append(new SimpleDateFormat("ddMMyy").format(d));
      Calendar calendar = new GregorianCalendar();
      calendar.setTime(d);
      ssn.append(calendar.get(Calendar.YEAR) >= 2000 ? 'A' : '-');
      ssn.append(StringUtils.upperCase(ssnEnd));
      return ssn.toString();
    }
    catch (ParseException e) {
      return null;
    }
  }

  public static Date getLatest(Date...dates) {
    Date result = null;
    for (Date date : dates) {
      if (result == null || date.after(result)) {
        result = date;
      }
    }
    return result;
  }

  public static Student createPyramusStudent(Application application, StaffMember staffMember) throws DuplicatePersonException {
    Person person = resolvePerson(application);
    return createPyramusStudent(application, person, staffMember);
  }
  
  public static void deleteApplication(Application application) {
    logger.info(String.format("Removing application %d of %s %s (%s) to line %s created at %tF)",
        application.getId(),
        application.getFirstName(),
        application.getLastName(),
        application.getEmail(),
        application.getLine(),
        application.getCreated()));
    // Delete signatures
    ApplicationSignaturesDAO applicationSignaturesDAO = DAOFactory.getInstance().getApplicationSignaturesDAO();
    List<ApplicationSignatures> applicationSignatures = applicationSignaturesDAO.listByApplication(application);
    for (ApplicationSignatures applicationSignature : applicationSignatures) {
      applicationSignaturesDAO.delete(applicationSignature);
    }
    // Delete log entries
    ApplicationLogDAO applicationLogDAO = DAOFactory.getInstance().getApplicationLogDAO();
    List<ApplicationLog> applicationLogs = applicationLogDAO.listByApplication(application);
    for (ApplicationLog applicationLog : applicationLogs) {
      applicationLogDAO.delete(applicationLog);
    }
    // Delete attachments (database)
    ApplicationAttachmentDAO applicationAttachmentDAO = DAOFactory.getInstance().getApplicationAttachmentDAO();
    List<ApplicationAttachment> applicationAttachments = applicationAttachmentDAO.listByApplicationId(application.getApplicationId());
    for (ApplicationAttachment applicationAttachment : applicationAttachments) {
      applicationAttachmentDAO.delete(applicationAttachment);
    }
    // Delete attachments (file system)
    String attachmentsFolder = SettingUtils.getSettingValue("applications.storagePath");
    if (!StringUtils.isEmpty(attachmentsFolder)) {
      File attachmentFolder = Paths.get(attachmentsFolder, application.getApplicationId()).toFile();
      if (attachmentFolder.exists()) {
        try {
          FileUtils.deleteDirectory(attachmentFolder);
        }
        catch (IOException e) {
          logger.log(Level.WARNING, String.format("Failed to remove application attachment folder %s", attachmentFolder), e);
        }
      }
    }
    // Delete application
    ApplicationDAO applicationDAO = DAOFactory.getInstance().getApplicationDAO();
    applicationDAO.delete(application);
  }

  public static Student createPyramusStudent(Application application, Person person, StaffMember staffMember) {
    PhoneNumberDAO phoneNumberDAO = DAOFactory.getInstance().getPhoneNumberDAO();
    AddressDAO addressDAO = DAOFactory.getInstance().getAddressDAO();
    EmailDAO emailDAO = DAOFactory.getInstance().getEmailDAO();
    ContactTypeDAO contactTypeDAO = DAOFactory.getInstance().getContactTypeDAO();
    PersonDAO personDAO = DAOFactory.getInstance().getPersonDAO();
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    SchoolDAO schoolDAO = DAOFactory.getInstance().getSchoolDAO();
    ApplicationAttachmentDAO applicationAttachmentDAO = DAOFactory.getInstance().getApplicationAttachmentDAO();
    
    JSONObject formData = JSONObject.fromObject(application.getFormData());
    
    // Create person (if needed)
    
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
    
    // Study time end plus one year (for Internetix students)  
    
    Date studyTimeEnd = null;
    if (StringUtils.equals(getFormValue(formData, "field-line"), "aineopiskelu")) {
      Calendar c = Calendar.getInstance();
      c.add(Calendar.YEAR, 1);
      studyTimeEnd = c.getTime();
    }
    
    // #868: Non-contract school information (for Internetix students, if exists)
    
    String additionalInfo = null;
    if (StringUtils.equals(getFormValue(formData, "field-line"), "aineopiskelu")) {
      String contractSchoolName = getFormValue(formData, "field-internetix-contract-school-name");
      String contractSchoolMunicipality = getFormValue(formData, "field-internetix-contract-school-municipality");
      String contractSchoolContact = getFormValue(formData, "field-internetix-contract-school-contact");
      if (!StringUtils.isAnyEmpty(contractSchoolName, contractSchoolMunicipality, contractSchoolContact)) {
        additionalInfo = String.format("<p><strong>Muun kuin sopimusoppilaitoksen yhteystiedot:</strong><br/>%s (%s)<br/>%s</p>",
            contractSchoolName,
            contractSchoolMunicipality,
            StringUtils.replace(contractSchoolContact, "\n", "<br/>"));
      }
    }
    
    // Create student
    
    Student student = studentDAO.create(
        person,
        getFormValue(formData, "field-first-names"),
        getFormValue(formData, "field-last-name"),
        getFormValue(formData, "field-nickname"),
        additionalInfo,
        studyTimeEnd,
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
    
    String email = StringUtils.lowerCase(StringUtils.trim(getFormValue(formData, "field-email")));
    logger.info(String.format("Attaching primary email %s", email));
    emailDAO.create(student.getContactInfo(), contactType, Boolean.TRUE, email);
    
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
    
    email = StringUtils.lowerCase(StringUtils.trim(getFormValue(formData, "field-underage-email")));
    if (!StringUtils.isBlank(email)) {
      
      // Attach email
      
      logger.info(String.format("Attaching guardian email %s", email));
      contactType = contactTypeDAO.findById(5L); // Yhteyshenkilö (non-unique)
      emailDAO.create(student.getContactInfo(), contactType, Boolean.FALSE, email);

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
    
    // Contract school (Internetix students)
    
    String schoolId = getFormValue(formData, "field-internetix-contract-school");
    if (!NumberUtils.isNumber(schoolId)) {
      String customSchool = getFormValue(formData, "field-internetix-contract-school-name");
      if (!StringUtils.isBlank(customSchool)) {
        List<School> schools = schoolDAO.listByNameLowercase(customSchool);
        School school = schools.isEmpty() ? null : schools.get(0);
        if (school != null) {
          studentDAO.updateSchool(student, school);
        }
        else {
          String notification = "<b>Huom!</b> Opiskelijan ilmoittamaa oppilaitosta ei löydy vielä Pyramuksesta!";
          ApplicationLogDAO applicationLogDAO = DAOFactory.getInstance().getApplicationLogDAO();
          applicationLogDAO.create(
              application,
              ApplicationLogType.HTML,
              notification,
              null);
        }
      }
    }
    
    // Attachments
    
    List<ApplicationAttachment> attachments = applicationAttachmentDAO.listByApplicationId(application.getApplicationId());
    if (!attachments.isEmpty()) {
      String attachmentsFolder = SettingUtils.getSettingValue("applications.storagePath");
      if (StringUtils.isNotEmpty(attachmentsFolder)) {
        StudentFileDAO studentFileDAO = DAOFactory.getInstance().getStudentFileDAO();
        String applicationId = sanitizeFilename(application.getApplicationId());
        for (ApplicationAttachment attachment : attachments) {
          String attachmentFileName = sanitizeFilename(attachment.getName());
          try {
            java.nio.file.Path path = Paths.get(attachmentsFolder, applicationId, attachmentFileName);
            File file = path.toFile();
            if (file.exists()) {
              String fileId = null;
              String contentType = Files.probeContentType(path);
              byte[] data = FileUtils.readFileToByteArray(file);

              if (PyramusFileUtils.isFileSystemStorageEnabled()) {
                try {
                  fileId = PyramusFileUtils.generateFileId();
                  PyramusFileUtils.storeFile(student, fileId, data);
                  data = null;
                }
                catch (IOException e) {
                  fileId = null;
                  logger.log(Level.WARNING, "Store user file to file system failed", e);
                }
              }
              
              studentFileDAO.create(
                  student,
                  StringUtils.isBlank(attachment.getDescription()) ? attachmentFileName : attachment.getDescription(),
                  attachmentFileName,
                  fileId,
                  null, // file type
                  contentType,
                  data,
                  staffMember);
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
  
  public static void mailCredentialsInfo(HttpServletRequest request, Student student,  Application application) {
    try {
      // Application form 
      
      JSONObject formData = JSONObject.fromObject(application.getFormData());
      
      // Determine the need for credentials
      
      List<InternalAuthenticationProvider> providers = AuthenticationProviderVault.getInstance().getInternalAuthenticationProviders();
      InternalAuthenticationProvider provider = providers.size() == 1 ? providers.get(0) : null;
      if (provider == null) {
        throw new InternalError("Unable to resolve InternalAuthenticationProvider");
      }
      UserIdentificationDAO userIdentificationDAO = DAOFactory.getInstance().getUserIdentificationDAO();
      UserIdentification userIdentification = userIdentificationDAO.findByAuthSourceAndPerson(provider.getName(), student.getPerson());
      
      // Choose mail template depending on whether student already has credentials or not
      
      String subject = "Muikku-oppimisympäristön tunnukset";
      String content;
      if (userIdentification == null) {
        StringBuilder createCredentialsUrl = new StringBuilder();
        createCredentialsUrl.append(request.getScheme());
        createCredentialsUrl.append("://");
        createCredentialsUrl.append(request.getServerName());
        createCredentialsUrl.append(":");
        createCredentialsUrl.append(request.getServerPort());
        createCredentialsUrl.append("/applications/createcredentials.page?a=");
        createCredentialsUrl.append(application.getApplicationId());
        createCredentialsUrl.append("&t=");
        createCredentialsUrl.append(application.getCredentialToken());
        
        content = IOUtils.toString(request.getServletContext().getResourceAsStream(
            "/templates/applications/mails/mail-credentials-create.html"), "UTF-8");
        content = String.format(
            content,
            getFormValue(formData, "field-nickname"),
            createCredentialsUrl);
      }
      else {
        content = IOUtils.toString(request.getServletContext().getResourceAsStream(
            "/templates/applications/mails/mail-credentials-exist.html"), "UTF-8");
        content = String.format(
            content,
            getFormValue(formData, "field-nickname"));
      }
        
      // Send mail to applicant (and possible guardian)
      
      if (StringUtils.isBlank(getFormValue(formData, "field-underage-email"))) {
        Mailer.sendMail(
            Mailer.JNDI_APPLICATION,
            Mailer.HTML,
            null,
            application.getEmail(),
            subject,
            content);
      }
      else {
        Mailer.sendMail(
            Mailer.JNDI_APPLICATION,
            Mailer.HTML,
            null,
            application.getEmail(),
            getFormValue(formData, "field-underage-email"),
            subject,
            content);
      }
      
      // Add notification about sent mail
      
      ApplicationLogDAO applicationLogDAO = DAOFactory.getInstance().getApplicationLogDAO();
      applicationLogDAO.create(
        application,
        ApplicationLogType.HTML,
        String.format("<p>%s</p><p><b>%s</b></p>%s", "Hakijalle lähetetty ohjeet Muikku-tunnuksista", subject, content),
        null);
    }
    catch (IOException ioe) {
      logger.log(Level.SEVERE, "Error retrieving mail templates", ioe);
    }
  }

  public static Person resolvePerson(Application application) throws DuplicatePersonException {
    EmailDAO emailDAO = DAOFactory.getInstance().getEmailDAO();
    PersonDAO personDAO = DAOFactory.getInstance().getPersonDAO();
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    JSONObject applicationData = JSONObject.fromObject(application.getFormData());
    
    // Person by social security number
    
    Map<Long, Person> existingPersons = new HashMap<Long, Person>();
    String ssn = constructSSN(getFormValue(applicationData, "field-birthday"), getFormValue(applicationData, "field-ssn-end"));
    if (StringUtils.isNotBlank(ssn)) {

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
    
    String emailAddress = StringUtils.lowerCase(StringUtils.trim(application.getEmail()));
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
      throw new DuplicatePersonException("Käyttäjätiedot täsmäävät useampaan olemassa olevaan käyttäjätiliin");
    }
    else if (existingPersons.isEmpty()) {
      return null;
    }
    else {
      Person person = existingPersons.values().iterator().next();
      if (person.getDefaultUser() != null) {
        StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
        StaffMember staffMember = staffMemberDAO.findById(person.getDefaultUser().getId());
        if (staffMember != null) {
          throw new DuplicatePersonException("Käyttäjätiedot viittaavat henkilökunnan jäseneen");
        }
      }
      return person;
    }
  }

  public static String sourceUiValue(String value) {
    switch (value) {
    case "tuttu":
      return "Ennestään tuttu";
    case "google":
      return "Google";
    case "facebook":
      return "Facebook";
    case "instagram":
      return "Instagram";
    case "sanomalehti":
      return "Sanomalehti";
    case "tienvarsimainos":
      return "Tienvarsimainos";
    case "valotaulumainos":
      return "Valotaulumainos";
    case "elokuva":
      return "Elokuva- tai TV-mainos";
    case "tuttava":
      return "Kuulin kaverilta, tuttavalta, tms.";
    case "opot":
      return "Opot";
    case "messut":
      return "Messut";
    case "te-toimisto":
      return "TE-toimisto";
    case "nuorisotyo":
      return "Nuorisotyö";
    case "muu":
      return "Muu";
    default:
      return null;
    }
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
  private static String sanitizeFilename(String filename) {
    return StringUtils.stripEnd(StringUtils.stripStart(StringUtils.strip(filename, "\\/:*?\"<>|"), "."), ".");
  }
  
  private static String getFormValue(JSONObject object, String key) {
    return object.has(key) ? object.getString(key) : null;
  }

}
