package fi.otavanopisto.pyramus.applications;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.xhtmlrenderer.pdf.ITextRenderer;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationAttachmentDAO;
import fi.otavanopisto.pyramus.dao.application.ApplicationDAO;
import fi.otavanopisto.pyramus.dao.application.ApplicationLogDAO;
import fi.otavanopisto.pyramus.dao.application.ApplicationNotificationDAO;
import fi.otavanopisto.pyramus.dao.application.ApplicationSignaturesDAO;
import fi.otavanopisto.pyramus.dao.base.AddressDAO;
import fi.otavanopisto.pyramus.dao.base.ContactTypeDAO;
import fi.otavanopisto.pyramus.dao.base.CurriculumDAO;
import fi.otavanopisto.pyramus.dao.base.EmailDAO;
import fi.otavanopisto.pyramus.dao.base.LanguageDAO;
import fi.otavanopisto.pyramus.dao.base.MunicipalityDAO;
import fi.otavanopisto.pyramus.dao.base.NationalityDAO;
import fi.otavanopisto.pyramus.dao.base.PersonDAO;
import fi.otavanopisto.pyramus.dao.base.PhoneNumberDAO;
import fi.otavanopisto.pyramus.dao.base.SchoolDAO;
import fi.otavanopisto.pyramus.dao.base.SchoolVariableDAO;
import fi.otavanopisto.pyramus.dao.base.StudyProgrammeDAO;
import fi.otavanopisto.pyramus.dao.file.StudentFileDAO;
import fi.otavanopisto.pyramus.dao.students.StudentActivityTypeDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.students.StudentExaminationTypeDAO;
import fi.otavanopisto.pyramus.dao.students.StudentGroupStudentDAO;
import fi.otavanopisto.pyramus.dao.students.StudentStudyPeriodDAO;
import fi.otavanopisto.pyramus.dao.system.SettingDAO;
import fi.otavanopisto.pyramus.dao.system.SettingKeyDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.dao.users.StudentParentRegistrationDAO;
import fi.otavanopisto.pyramus.dao.users.UserDAO;
import fi.otavanopisto.pyramus.dao.users.UserIdentificationDAO;
import fi.otavanopisto.pyramus.dao.users.UserVariableDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationAttachment;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationLog;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationLogType;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationNotification;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationSignatures;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationState;
import fi.otavanopisto.pyramus.domainmodel.base.ContactType;
import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;
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
import fi.otavanopisto.pyramus.domainmodel.students.StudentStudyPeriodType;
import fi.otavanopisto.pyramus.domainmodel.system.Setting;
import fi.otavanopisto.pyramus.domainmodel.system.SettingKey;
import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.StudentParentRegistration;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.domainmodel.users.UserIdentification;
import fi.otavanopisto.pyramus.framework.PyramusFileUtils;
import fi.otavanopisto.pyramus.framework.SettingUtils;
import fi.otavanopisto.pyramus.framework.StaffMemberProperties;
import fi.otavanopisto.pyramus.mailer.Mailer;
import fi.otavanopisto.pyramus.plugin.auth.AuthenticationProviderVault;
import fi.otavanopisto.pyramus.plugin.auth.InternalAuthenticationProvider;
import net.sf.json.JSONObject;

public class ApplicationUtils {

  private static final Logger logger = Logger.getLogger(ApplicationUtils.class.getName());

  private static final String SETTINGKEY_SIGNERID = "applications.defaultSignerId";
  public static final String SETTINGKEY_STUDENTPARENTREGISTRATIONENABLED = "applications.studentParentRegistrationEnabled";
  
  private static final String LINE_AINEOPISKELU = "aineopiskelu";
  private static final String LINE_AINEOPISKELU_PK = "aineopiskelupk";
  private static final String LINE_NETTILUKIO = "nettilukio";
  private static final String LINE_NETTIPK = "nettipk";
  private static final String LINE_AIKUISLUKIO = "aikuislukio";
  private static final String LINE_MK = "mk";
  
  public static boolean hasLineAccess(StaffMember staffMember, String line) {
    if (staffMember.hasRole(Role.ADMINISTRATOR)) {
      return true;
    }
    if (StringUtils.equals(line, LINE_AINEOPISKELU) && "1".equals(staffMember.getProperties().get(StaffMemberProperties.APPLICATIONS_AINEOPISKELU.getKey()))) {
      return true;
    }
    if (StringUtils.equals(line, LINE_AINEOPISKELU_PK) && "1".equals(staffMember.getProperties().get(StaffMemberProperties.APPLICATIONS_AINEOPISKELU_PK.getKey()))) {
      return true;
    }
    if (StringUtils.equals(line, LINE_NETTILUKIO) && "1".equals(staffMember.getProperties().get(StaffMemberProperties.APPLICATIONS_NETTILUKIO.getKey()))) {
      return true;
    }
    if (StringUtils.equals(line, LINE_NETTIPK) && "1".equals(staffMember.getProperties().get(StaffMemberProperties.APPLICATIONS_NETTIPERUSKOULU.getKey()))) {
      return true;
    }
    if (StringUtils.equals(line, LINE_AIKUISLUKIO) && "1".equals(staffMember.getProperties().get(StaffMemberProperties.APPLICATIONS_AIKUISLUKIO.getKey()))) {
      return true;
    }
    if (StringUtils.equals(line, LINE_MK) && "1".equals(staffMember.getProperties().get(StaffMemberProperties.APPLICATIONS_AIKUISTENPERUSOPETUS.getKey()))) {
      return true;
    }
    return false;
  }
  
  public static boolean isInternetixAutoRegistrationPossible(Application application, boolean allowEmptySsn) {
    JSONObject formData = JSONObject.fromObject(application.getFormData());
    // #1487: Jos aineopiskelijaksi hakeva opiskelee sopimusoppilaitoksessa, käsitellään manuaalisesti
    if (ApplicationUtils.isContractSchool(formData)) {
      return false;
    }
    // #1487: Jos hetun loppuosa puuttuu tai on XXXX, käsitellään manuaalisesti
    String ssnSuffix = getSsnSuffix(formData);
    if (StringUtils.isEmpty(ssnSuffix) || StringUtils.equalsIgnoreCase("XXXX", ssnSuffix)) {
      if (!allowEmptySsn) {
        return false;
      }
    }
    // #1487: Jos aineopiskelija on alle 20 (lukio, vain 1.1.2005 jälkeen syntyneet) tai alle 18, käsitellään manuaalisesti
    String line = getFormValue(formData, "field-line");
    if (StringUtils.equals(line, "aineopiskelu")) {
      return !isInternetixUnderage(application);
    }
    else {
      return !isUnderage(application);
    }
  }
  
  private static String getSsnSuffix(JSONObject formData) {
    String ssn = getFormValue(formData, "field-ssn");
    if (!StringUtils.isEmpty(ssn)) {
      return StringUtils.upperCase(StringUtils.substring(ssn, 7, 11));
    }
    else {
      return StringUtils.upperCase(getFormValue(formData, "field-ssn-end"));
    }
  }
  
  public static boolean isInternetixLine(String line) {
    return StringUtils.equals(line, LINE_AINEOPISKELU) || StringUtils.equals(line, LINE_AINEOPISKELU_PK);
  }
  
  public static Set<String> listAccessibleLines(StaffMember staffMember) {
    boolean isAdmin = staffMember.hasRole(Role.ADMINISTRATOR);
    Set<String> lines = new HashSet<>();
    if (isAdmin || "1".equals(staffMember.getProperties().get(StaffMemberProperties.APPLICATIONS_AINEOPISKELU_PK.getKey()))) {
      lines.add(LINE_AINEOPISKELU_PK);
    }
    if (isAdmin || "1".equals(staffMember.getProperties().get(StaffMemberProperties.APPLICATIONS_AINEOPISKELU.getKey()))) {
      lines.add(LINE_AINEOPISKELU);
    }
    if (isAdmin || "1".equals(staffMember.getProperties().get(StaffMemberProperties.APPLICATIONS_NETTILUKIO.getKey()))) {
      lines.add(LINE_NETTILUKIO);
    }
    if (isAdmin || "1".equals(staffMember.getProperties().get(StaffMemberProperties.APPLICATIONS_NETTIPERUSKOULU.getKey()))) {
      lines.add(LINE_NETTIPK);
    }
    if (isAdmin || "1".equals(staffMember.getProperties().get(StaffMemberProperties.APPLICATIONS_AIKUISLUKIO.getKey()))) {
      lines.add(LINE_AIKUISLUKIO);
    }
    if (isAdmin || "1".equals(staffMember.getProperties().get(StaffMemberProperties.APPLICATIONS_AIKUISTENPERUSOPETUS.getKey()))) {
      lines.add(LINE_MK);
    }
    return lines;
  }

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
      case LINE_AINEOPISKELU:
        return "Aineopiskelu/lukio";
      case LINE_AINEOPISKELU_PK:
        return "Aineopiskelu/perusopetus";
      case LINE_NETTILUKIO:
        return "Nettilukio";
      case LINE_NETTIPK:
        return "Nettiperuskoulu";
      case LINE_AIKUISLUKIO:
        return "Aikuislukio";
      case LINE_MK:
        return "Aikuisten perusopetus";
      default:
        return null;
      }
    }
    return null;
  }
  
  public static boolean isOtaviaLine(String line) {
    return !StringUtils.equals(line, "aikuislukio");
  }
  
  public static boolean isValidLine(String line) {
    return applicationLineUiValue(line) != null;
  }
  
  public static boolean isInternetixUnderage(Application application) {
    String dateString = extractBirthdayString(application);
    if (StringUtils.isBlank(dateString)) {
      return false;
    }
    try {
      // #1487: If you're born on or after 1.1.2005, until the end of the year you turn 20...
      LocalDate birthday = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("d.M.yyyy"));
      LocalDate threshold = LocalDate.parse("1.1.2005", DateTimeFormatter.ofPattern("d.M.yyyy"));
      if (birthday.equals(threshold) || birthday.isAfter(threshold)) {
        if (LocalDate.now().getYear() - birthday.getYear() <= 20) {
          return true;
        }
      }
      // ...otherwise under 18
      return isUnderage(application);
    }
    catch (DateTimeParseException e) {
      logger.warning(String.format("Malformatted date %s (%s)", dateString, e.getMessage()));
      return false;
    }
  }

  public static boolean isUnderage(Application application) {
    String dateString = extractBirthdayString(application);
    if (StringUtils.isBlank(dateString)) {
      return false;
    }
    try {
      LocalDate birthday = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("d.M.yyyy"));
      LocalDate threshold = LocalDate.now().minusYears(18);
      return birthday.isAfter(threshold);
    }
    catch (DateTimeParseException e) {
      logger.warning(String.format("Malformatted date %s (%s)", dateString, e.getMessage()));
      return false;
    }
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
  
  public static String previousStudiesInternetixUiValue(String value) {
    if (value != null) {
      switch (value) {
      case "perus":
        return "Peruskoulu (tai vastaava)";
      case "lukio":
        return "Lukion oppimäärä tai 4 vuotta lukio-opintoja";
      case "ei":
        return "En ole suorittanut mitään näistä";
      default:
        return null;
      }
    }
    return null;
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
        return "Ammatillinen 2. aste"; // #1349: Tämä poistunut lomakkeelta
      case "ammatillinen-kesken":
        return "Ammatilliset 2. asteen opinnot (keskeytynyt)";
      case "ammatillinen-valmis":
        return "Ammatilliset 2. asteen opinnot (valmis tutkinto)";
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

  public static School resolveSchool(JSONObject formData) {
    String value = getFormValue(formData, "field-internetix-contract-school");
    if (!StringUtils.isBlank(value)) {
      SchoolDAO schoolDAO = DAOFactory.getInstance().getSchoolDAO();
      if (StringUtils.equals(value, "muu")) {
        String customSchool = getFormValue(formData, "field-internetix-contract-school-name");
        if (!StringUtils.isBlank(customSchool)) {
          List<School> schools = schoolDAO.listByNameLowercaseAndArchived(customSchool, Boolean.FALSE);
          return schools.isEmpty() ? null : schools.get(0);
        }
      }
      else if (NumberUtils.isDigits(value)) {
        return schoolDAO.findById(Long.valueOf(value));
      }
    }
    return null;
  }
  
  public static boolean isContractSchool(JSONObject formData) {
    School school = resolveSchool(formData);
    if (school != null) {
      SchoolVariableDAO schoolVariableDAO = DAOFactory.getInstance().getSchoolVariableDAO();
      String contractSchool = schoolVariableDAO.findValueBySchoolAndKey(school, "contractSchool");
      return StringUtils.equals(contractSchool, "1");
    }
    return false;
  }
  
  public static String genderUiValue(String value) {
    if (value != null) {
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
    return null;
  }
  
  public static Sex resolveGender(String genderValue) {
    return StringUtils.equals("mies",  genderValue) ? Sex.MALE : StringUtils.equals("nainen",  genderValue) ? Sex.FEMALE : Sex.OTHER;
  }
  
  public static StudentExaminationType resolveStudentExaminationType(String examinationType) {
    if (StringUtils.isBlank(examinationType)) {
      return null;
    }
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
      return studentExaminationTypeDAO.findById(8L); // Ylempi korkeakoulututkinto (#1349: poistunut lomakkeelta)
    case "tuva":
      return studentExaminationTypeDAO.findById(9L); // TUVA-koulutus
    default:
      return null;
    }
  }
  
  public static StudentActivityType resolveStudentActivityType(String activityType) {
    if (StringUtils.isBlank(activityType)) {
      return null;
    }
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
  
  public static StudyProgramme resolveStudyProgramme(Application application) {
    JSONObject formData = JSONObject.fromObject(application.getFormData());
    String line = getFormValue(formData, "field-line");
    if (StringUtils.isBlank(line)) {
      return null;
    }
    StudyProgrammeDAO studyProgrammeDAO = DAOFactory.getInstance().getStudyProgrammeDAO();
    switch (line) {
    case LINE_AINEOPISKELU:
      if (!isInternetixAutoRegistrationPossible(application, true)) {
        return studyProgrammeDAO.findById(49L); // Aineopiskelu/lukio (oppivelvolliset)
      }
      return studyProgrammeDAO.findById(13L); // Aineopiskelu/lukio
    case LINE_AINEOPISKELU_PK:
      InternetixStudyProgramme internetixLine = EnumUtils.getEnum(InternetixStudyProgramme.class, getFormValue(formData, "field-internetix_alternativelines"));
      if (internetixLine == InternetixStudyProgramme.OPPIVELVOLLINEN) {
        return studyProgrammeDAO.findById(41L); // Aineopiskelu/perusopetus (oppivelvolliset)
      }
      else if (internetixLine == InternetixStudyProgramme.OPPILAITOS) {
        return studyProgrammeDAO.findById(50L); // Aineopiskelu/perusopetus (oppilaitos ilmoittaa)
      }
      return studyProgrammeDAO.findById(12L); // Aineopiskelu/perusopetus
    case LINE_NETTILUKIO: {
      AlternativeLine nettilukioAlternative = EnumUtils.getEnum(AlternativeLine.class, getFormValue(formData, "field-nettilukio_alternativelines"));
      if (nettilukioAlternative == AlternativeLine.PRIVATE) {
        return studyProgrammeDAO.findById(45L); // Nettilukio/yksityisopiskelu (aineopiskelu)
      }
      else if (nettilukioAlternative == AlternativeLine.YO) {
        return studyProgrammeDAO.findById(39L); // Aineopiskelu/yo-tutkinto
      }
      return studyProgrammeDAO.findById(6L); // Nettilukio
    }
    case LINE_NETTIPK:
      return studyProgrammeDAO.findById(7L); // Nettiperuskoulu
    case LINE_AIKUISLUKIO:
      return studyProgrammeDAO.findById(1L); // Aikuislukio
    case LINE_MK:
      String foreignLine = getFormValue(formData, "field-foreign-line");
      if (StringUtils.isBlank(foreignLine)) {
        return null;
      }
      switch (foreignLine) {
      case "apa":
        return studyProgrammeDAO.findById(29L); // Aikuisten perusopetuksen alkuvaiheen opetus
      case "luku":
        return studyProgrammeDAO.findById(33L); // Aikuisten perusopetuksen lukutaitovaihe
      case "pk":
        return studyProgrammeDAO.findById(11L); // Peruskoululinja
      case "luva":
        return studyProgrammeDAO.findById(27L); // LUVA (#1399: deprecated; backward compatibility only)
      case "lisaopetus":
        return studyProgrammeDAO.findById(15L); // Monikulttuurinen peruskoululinja (#1430: deprecated; backward compatibiity only)
      default:
        return null;
      }
    default:
      return null;
    }
  }
  
  public static String generateReferenceCode(String lastName, String initialReferenceCode) {
    ApplicationDAO applicationDAO = DAOFactory.getInstance().getApplicationDAO();
    String referenceCode = initialReferenceCode == null ? RandomStringUtils.randomAlphabetic(6).toUpperCase() : initialReferenceCode; 
    while (applicationDAO.findByLastNameAndReferenceCode(lastName, referenceCode) != null) {
      referenceCode = RandomStringUtils.randomAlphabetic(6).toUpperCase();
    }
    return referenceCode;
  }
  
  public static byte[] generateStaffSignatureDocument(HttpServletRequest request, String applicant, String line,
      StaffMember signer, boolean underageApplicant) throws Exception {
    try {
      StringBuilder baseUrl = new StringBuilder();
      baseUrl.append(request.getScheme());
      baseUrl.append("://");
      baseUrl.append(request.getServerName());
      baseUrl.append(":");
      baseUrl.append(request.getServerPort());
      
      String documentPath = isOtaviaLine(line) ? "/templates/applications/document-staff-signed-otavia.html" : "/templates/applications/document-staff-signed-otava.html"; 

      // Staff signed document skeleton

      String document = IOUtils.toString(request.getServletContext().getResourceAsStream(documentPath), "UTF-8");

      // Replace document date

      document = StringUtils.replace(document, "[DOCUMENT-DATE]", new SimpleDateFormat("d.M.yyyy").format(new Date()));

      // Replace applicant name

      document = StringUtils.replace(document, "[DOCUMENT-APPLICANT]", applicant);

      // Replace line specific welcome text
      // #1430: Differente template for underage applicants

      String template = underageApplicant
          ? "/templates/applications/document-acceptance-%s-underage.html"
          : "/templates/applications/document-acceptance-%s.html";
      String welcomeText = IOUtils.toString(request.getServletContext().getResourceAsStream(String.format(template, line)), "UTF-8");
      document = StringUtils.replace(document, "[DOCUMENT-TEXT]", welcomeText);

      // Replace primary and (optional) secondary signers

      Long primarySignerId = getPrimarySignerId(line);
      if (primarySignerId == null) {
        primarySignerId = signer.getId();
      }
      if (primarySignerId.equals(signer.getId())) {
        document = StringUtils.replace(document, "[DOCUMENT-PRIMARY-SIGNER]", getSignature(signer, line));
        document = StringUtils.replace(document, "[DOCUMENT-SECONDARY-SIGNER]", "");
      }
      else {
        StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
        StaffMember primarySigner = staffMemberDAO.findById(primarySignerId);
        document = StringUtils.replace(document, "[DOCUMENT-PRIMARY-SIGNER]", getSignature(primarySigner, line));
        document = StringUtils.replace(document, "[DOCUMENT-SECONDARY-SIGNER]",
            "<p>Puolesta</p>" + getSignature(signer, line));
      }

      // Convert to PDF

      ByteArrayOutputStream out = new ByteArrayOutputStream();
      ITextRenderer renderer = new ITextRenderer();
      renderer.setDocumentFromString(document, baseUrl.toString());
      renderer.layout();
      renderer.createPDF(out);
      return out.toByteArray();
    }
    catch (Exception e) {
      logger.log(Level.SEVERE, "Unable to create staff document", e);
      throw e;
    }
  }
  
  public static byte[] generateApplicantSignatureDocument(
      HttpServletRequest request,
      Long applicationId,
      String line,
      String applicantName,
      String email,
      boolean underageApplicant) throws Exception {
    try {
      StringBuilder baseUrl = new StringBuilder();
      baseUrl.append(request.getScheme());
      baseUrl.append("://");
      baseUrl.append(request.getServerName());
      baseUrl.append(":");
      baseUrl.append(request.getServerPort());

      String documentPath = null;
      if (isOtaviaLine(line)) {
        if (underageApplicant) {
          documentPath = StringUtils.equals(line, "nettilukio")
              ? "/templates/applications/document-student-signed-otavia-underage-nettilukio.html"
              : "/templates/applications/document-student-signed-otavia-underage.html";
        }
        else {
          documentPath = "/templates/applications/document-student-signed-otavia.html";
        }
      }
      else {
        documentPath = underageApplicant
            ? "/templates/applications/document-student-signed-otava-underage.html"
            : "/templates/applications/document-student-signed-otava.html";
      }

      // Applicant signed document skeleton

      String document = IOUtils.toString(request.getServletContext().getResourceAsStream(documentPath), "UTF-8");

      // Replace applicant information

      document = StringUtils.replace(document, "[DOCUMENT-APPLICATION-ID]", applicationId.toString());
      document = StringUtils.replace(document, "[DOCUMENT-APPLICANT-LINE]", ApplicationUtils.applicationLineUiValue(line));
      document = StringUtils.replace(document, "[DOCUMENT-APPLICANT-NAME]", applicantName);
      document = StringUtils.replace(document, "[DOCUMENT-APPLICANT-EMAIL]", email);

      // Convert to PDF

      ByteArrayOutputStream out = new ByteArrayOutputStream();
      ITextRenderer renderer = new ITextRenderer();
      renderer.setDocumentFromString(document, baseUrl.toString());
      renderer.layout();
      renderer.createPDF(out);
      return out.toByteArray();
    }
    catch (Exception e) {
      logger.log(Level.SEVERE, "Unable to create applicant document", e);
      throw e;
    }
  }
  
  public static void sendApplicationModifiedMail(Application application, HttpServletRequest httpRequest, String oldSurname) {

    // Mail to the applicant

    JSONObject formData = JSONObject.fromObject(application.getFormData());
    String line = formData.getString("field-line");
    String surname = application.getLastName();
    String referenceCode = application.getReferenceCode();
    String applicantMail = application.getEmail();
    String guardianMail = StringUtils.lowerCase(StringUtils.trim(getFormValue(formData, "field-underage-email")));
    try {

      // #769: Do not mail application edit instructions to Internetix applicants 
      if (!isInternetixLine(application.getLine())) {

        // Modification mail subject and content
        
        String subject = IOUtils.toString(httpRequest.getServletContext().getResourceAsStream(
            String.format("/templates/applications/mails/mail-modified-%s-subject.txt", line)), "UTF-8");
        String content = IOUtils.toString(httpRequest.getServletContext().getResourceAsStream(
            String.format("/templates/applications/mails/mail-modified-%s-content.html", line)), "UTF-8");
        
        if (StringUtils.isBlank(subject) || StringUtils.isBlank(content)) {
          logger.log(Level.SEVERE, String.format("Modification mail for line %s not defined", line));
          return;
        }

        // Replace the dynamic parts of the mail content (old surname, new surname, edit link, surname and reference code)

        StringBuilder viewUrl = new StringBuilder();
        viewUrl.append(httpRequest.getScheme());
        viewUrl.append("://");
        viewUrl.append(httpRequest.getServerName());
        viewUrl.append(":");
        viewUrl.append(httpRequest.getServerPort());
        viewUrl.append("/applications/edit.page");

        content = String.format(content, oldSurname, surname, viewUrl, surname, referenceCode);

        // Send mail to applicant or, for minors, applicant and guardian

        if (StringUtils.isBlank(guardianMail)) {
          Mailer.sendMail(Mailer.JNDI_APPLICATION, Mailer.HTML, null, applicantMail, subject, content);
        }
        else {
          Mailer.sendMail(Mailer.JNDI_APPLICATION, Mailer.HTML, null, applicantMail, guardianMail, subject, content);
        }

        // #879: Add sent modification mail to application log
        
        ApplicationLogDAO applicationLogDAO = DAOFactory.getInstance().getApplicationLogDAO();
        applicationLogDAO.create(application,
            ApplicationLogType.HTML,
            String.format("<p>Hakijalle lähetettiin sähköpostia:</p><p><b>%s</b></p>%s", subject, content),
            null);
      }
    }
    catch (IOException e) {
      logger.log(Level.SEVERE, "Unable to retrieve modification mail template", e);
    }
  }
  
  public static void sendNotifications(Application application, HttpServletRequest request, StaffMember staffMember, boolean newApplication, String notificationPostfix, boolean doLogEntry) {
    ApplicationNotificationDAO applicationNotificationDAO = DAOFactory.getInstance().getApplicationNotificationDAO();
    List<ApplicationNotification> notifications = applicationNotificationDAO.listByNullOrLineAndState(application.getLine(), application.getState());
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
      String notification = String.format("Hakemus on siirtynyt tilaan <b>%s</b>", applicationStateUiValue(application.getState()));
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
  
  public static String extractBirthdayString(Application application) {
    if (application == null) {
      return null;
    }
    JSONObject formData = JSONObject.fromObject(application.getFormData());
    String ssn = StringUtils.upperCase(getFormValue(formData, "field-ssn"));
    if (!StringUtils.isEmpty(ssn)) {
      if (ssn.length() != 11) {
        logger.severe(String.format("Invalid SSN format %s", ssn));
        return null;
      }
      try {
        int d = Integer.parseInt(ssn.substring(0, 2));
        int m = Integer.parseInt(ssn.substring(2, 4));
        int y = Integer.parseInt(ssn.substring(4, 6));
        if ("ABCDEF".indexOf(ssn.charAt(6)) >= 0) {
          y += 2000;
        }
        else {
          y += 1900;
        }
        return String.format("%d.%d.%d", d, m, y);
      }
      catch (NumberFormatException e) {
        logger.severe(String.format("Invalid SSN format %s", ssn));
        return null;
      }
    }
    else {
      return getFormValue(formData, "field-birthday"); 
    }
  }

  public static String extractSSN(Application application) {
    if (application == null) {
      return null;
    }
    JSONObject formData = JSONObject.fromObject(application.getFormData());
    if (!StringUtils.isEmpty(getFormValue(formData, "field-ssn"))) {
      // #1529: SSN is in its own field
      return StringUtils.upperCase(getFormValue(formData, "field-ssn"));
    }
    else {
      // #1529: Backwards compatibility for old way of determining SSN (might result in an incorrect separator character)
      return constructSSN(getFormValue(formData, "field-birthday"), getFormValue(formData, "field-ssn-end"));
    }
  }

  private static String constructSSN(String birthday, String ssnEnd) {
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
    logger.info(String.format("Removing application %d to line %s created at %tF)",
        application.getId(),
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
    if (!StringUtils.isBlank(attachmentsFolder)) {
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
    ApplicationAttachmentDAO applicationAttachmentDAO = DAOFactory.getInstance().getApplicationAttachmentDAO();
    UserVariableDAO userVariableDAO = DAOFactory.getInstance().getUserVariableDAO();
    StudentStudyPeriodDAO studentStudyPeriodDAO = DAOFactory.getInstance().getStudentStudyPeriodDAO();
    StudentParentRegistrationDAO studentParentRegistrationDAO = DAOFactory.getInstance().getStudentParentRegistrationDAO();
    
    JSONObject formData = JSONObject.fromObject(application.getFormData());
    
    // Validate that the compulsory end date is in correct format to avoid all database operations in case it isn't

    if (StringUtils.isNotBlank(getFormValue(formData, "field-nettilukio_compulsory_enddate"))) {
      try {
        new SimpleDateFormat("d.M.yyyy").parse(getFormValue(formData, "field-nettilukio_compulsory_enddate"));
      }
      catch (ParseException e) {
        logger.severe(String.format("Invalid compulsory end date format in application entity %d", application.getId()));
        return null;
      }
    }
    
    // Create person (if needed)
    
    if (person == null) {
      // #1529: Determine birthday from birthday field or SSN field
      String birthdayStr = extractBirthdayString(application);
      try {
        Date birthday = StringUtils.isBlank(birthdayStr) ? null : new SimpleDateFormat("d.M.yyyy").parse(birthdayStr);
        Sex sex = resolveGender(getFormValue(formData, "field-sex"));
        person = personDAO.create(birthday, extractSSN(application), sex, null, Boolean.FALSE);
      }
      catch (ParseException e) {
        logger.severe(String.format("Invalid birthday format in application entity %d", application.getId()));
        return null;
      }
    }
    
    // Determine correct study programme
    
    StudyProgramme studyProgramme = resolveStudyProgramme(application);
    if (studyProgramme == null) {
      logger.severe(String.format("Unable to resolve study programme of application entity %d", application.getId()));
      return null;
    }
    
    // Various fields for Internetix students only
    
    Curriculum curriculum = null;
    Date studyTimeEnd = null;
    String additionalInfo = null;
    String line = getFormValue(formData, "field-line");
    if (isInternetixLine(line)) {
      
      // #1562: Curriculum for Internetix students in high school is always OPS 2021
      
      if (StringUtils.equals(line, LINE_AINEOPISKELU)) {
        CurriculumDAO curriculumDAO = DAOFactory.getInstance().getCurriculumDAO();
        curriculum = curriculumDAO.findById(4L); // OPS 2021
      }
      
      // Study time end plus one year

      Calendar c = Calendar.getInstance();
      c.add(Calendar.YEAR, 1);
      studyTimeEnd = c.getTime();

      // #868: Non-contract school information (if exists)

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
    
    Date studyStartDate = new Date();
    School school = resolveSchool(formData);
    Student student = studentDAO.create(
        person,
        getFormValue(formData, "field-first-names"),
        getFormValue(formData, "field-last-name"),
        getFormValue(formData, "field-nickname"),
        additionalInfo,
        studyTimeEnd,
        resolveStudentActivityType(getFormValue(formData, "field-job")),
        resolveStudentExaminationType(getFormValue(formData, "field-internetix-contract-school-degree")),
        null, // student educational level (entity)
        null, // education (string)
        resolveNationality(getFormValue(formData, "field-nationality")),
        resolveMunicipality(getFormValue(formData, "field-municipality")),
        resolveLanguage(getFormValue(formData, "field-language")),
        school,
        studyProgramme,
        curriculum,
        null, // previous studies (double)
        studyStartDate, // study start date
        null, // study end date
        null, // study end reason
        null, // study end text
        Boolean.FALSE); // archived
    
    // #1480: If school has a student group, add student to it
    
    if (school != null && school.getStudentGroup() != null) {
      StudentGroupStudentDAO studentGroupStudentDAO = DAOFactory.getInstance().getStudentGroupStudentDAO();
      studentGroupStudentDAO.create(school.getStudentGroup(), student, staffMember);
    }

    userVariableDAO.createDefaultValueVariables(student);
    
    // StudyPeriods
    
    String compulsoryStudies = getFormValue(formData, "field-nettilukio_compulsory");
    if (StringUtils.isNotBlank(compulsoryStudies)) {
      if (StringUtils.equals(compulsoryStudies, "compulsory")) {
        studentStudyPeriodDAO.create(student, studyStartDate, null, StudentStudyPeriodType.COMPULSORY_EDUCATION);
      
        String compulsoryEndDateStr = getFormValue(formData, "field-nettilukio_compulsory_enddate");
        if (StringUtils.isNotBlank(compulsoryEndDateStr)) {
          try {
            Date compulsoryEndDate = StringUtils.isBlank(compulsoryEndDateStr) ? null : new SimpleDateFormat("d.M.yyyy").parse(compulsoryEndDateStr);
            studentStudyPeriodDAO.create(student, compulsoryEndDate, null, StudentStudyPeriodType.NON_COMPULSORY_EDUCATION);
          } catch (ParseException e) {
            logger.severe(String.format("Invalid compulsory end date format in application entity %d", application.getId()));
            return null;
          }
        }
      } 
      else if (StringUtils.equals(compulsoryStudies, "non_compulsory")) {
        studentStudyPeriodDAO.create(student, studyStartDate, null, StudentStudyPeriodType.NON_COMPULSORY_EDUCATION);
      }
    }
    
    // #1079: Aineopiskelu; yleissivistävä koulutustausta
    
    if (isInternetixLine(getFormValue(formData, "field-line"))) {
      String internetixStudies = getFormValue(formData, "field-previous-studies-aineopiskelu");
      if (StringUtils.isNotBlank(internetixStudies)) {
        if (StringUtils.equals(internetixStudies, "perus")) {
          student = studentDAO.updateEducation(student, "Yleissivistävä koulutustausta: peruskoulu");
        }
        else if (StringUtils.equals(internetixStudies, "lukio")) {
          student = studentDAO.updateEducation(student, "Yleissivistävä koulutustausta: lukio");
        }
        else if (StringUtils.equals(internetixStudies, "ei")) {
          student = studentDAO.updateEducation(student, "Yleissivistävä koulutustausta: ei mitään");
        }
      }
    }
    
    // Main contact type
    
    ContactType contactType = contactTypeDAO.findById(1L); // Koti (unique)

    // Attach email
    
    String email = StringUtils.lowerCase(StringUtils.trim(getFormValue(formData, "field-email")));
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
    
    // Guardian info for underage applicants
    
    if (isUnderage(application)) {
      
      // Attach email
      
      contactType = contactTypeDAO.findById(5L); // Yhteyshenkilö (non-unique)
      email = StringUtils.lowerCase(StringUtils.trim(getFormValue(formData, "field-underage-email")));
      if (!StringUtils.isBlank(email)) {
        emailDAO.create(student.getContactInfo(), contactType, Boolean.FALSE, email);
      }
      email = StringUtils.lowerCase(StringUtils.trim(getFormValue(formData, "field-underage-email-2")));
      if (!StringUtils.isBlank(email)) {
        emailDAO.create(student.getContactInfo(), contactType, Boolean.FALSE, email);
      }
      email = StringUtils.lowerCase(StringUtils.trim(getFormValue(formData, "field-underage-email-3")));
      if (!StringUtils.isBlank(email)) {
        emailDAO.create(student.getContactInfo(), contactType, Boolean.FALSE, email);
      }

      // Attach address
      
      if (!StringUtils.isBlank(getFormValue(formData, "field-underage-first-name"))) {
        addressDAO.create(
            student.getContactInfo(),
            contactType,
            String.format("%s %s", getFormValue(formData, "field-underage-first-name"), getFormValue(formData, "field-underage-last-name")),
            getFormValue(formData, "field-underage-street-address"),
            getFormValue(formData, "field-underage-zip-code"),
            getFormValue(formData, "field-underage-city"),
            getFormValue(formData, "field-underage-country"),
            Boolean.FALSE);
      }
      if (!StringUtils.isBlank(getFormValue(formData, "field-underage-first-name-2"))) {
        addressDAO.create(
            student.getContactInfo(),
            contactType,
            String.format("%s %s", getFormValue(formData, "field-underage-first-name-2"), getFormValue(formData, "field-underage-last-name-2")),
            getFormValue(formData, "field-underage-street-address-2"),
            getFormValue(formData, "field-underage-zip-code-2"),
            getFormValue(formData, "field-underage-city-2"),
            getFormValue(formData, "field-underage-country-2"),
            Boolean.FALSE);
      }
      if (!StringUtils.isBlank(getFormValue(formData, "field-underage-first-name-3"))) {
        addressDAO.create(
            student.getContactInfo(),
            contactType,
            String.format("%s %s", getFormValue(formData, "field-underage-first-name-3"), getFormValue(formData, "field-underage-last-name-3")),
            getFormValue(formData, "field-underage-street-address-3"),
            getFormValue(formData, "field-underage-zip-code-3"),
            getFormValue(formData, "field-underage-city-3"),
            getFormValue(formData, "field-underage-country-3"),
            Boolean.FALSE);
      }

      // Attach phone
      
      if (!StringUtils.isBlank(getFormValue(formData, "field-underage-phone"))) {
        phoneNumberDAO.create(
            student.getContactInfo(),
            contactType,
            Boolean.FALSE,
            getFormValue(formData, "field-underage-phone"));
      }
      if (!StringUtils.isBlank(getFormValue(formData, "field-underage-phone-2"))) {
        phoneNumberDAO.create(
            student.getContactInfo(),
            contactType,
            Boolean.FALSE,
            getFormValue(formData, "field-underage-phone-2"));
      }
      if (!StringUtils.isBlank(getFormValue(formData, "field-underage-phone-3"))) {
        phoneNumberDAO.create(
            student.getContactInfo(),
            contactType,
            Boolean.FALSE,
            getFormValue(formData, "field-underage-phone-3"));
      }
      
      // Create guardian invitations if the automated registration is enabled
      
      if (isStudentParentRegistrationsEnabled()) {
        // Guardian #1
        email = StringUtils.lowerCase(StringUtils.trim(getFormValue(formData, "field-underage-email")));
        if (StringUtils.isNotBlank(email)) {
          String firstName = getFormValue(formData, "field-underage-first-name");
          String lastName = getFormValue(formData, "field-underage-last-name");
          
          if (StringUtils.isNotBlank(firstName) && StringUtils.isNotBlank(lastName)) {
            String hash = UUID.randomUUID().toString();
            studentParentRegistrationDAO.create(firstName, lastName, email, student, hash);
          }
        }
        
        // Guardian #2
        email = StringUtils.lowerCase(StringUtils.trim(getFormValue(formData, "field-underage-email-2")));
        if (StringUtils.isNotBlank(email)) {
          String firstName = getFormValue(formData, "field-underage-first-name-2");
          String lastName = getFormValue(formData, "field-underage-last-name-2");
          
          if (StringUtils.isNotBlank(firstName) && StringUtils.isNotBlank(lastName)) {
            String hash = UUID.randomUUID().toString();
            studentParentRegistrationDAO.create(firstName, lastName, email, student, hash);
          }
        }
        
        // Guardian #3
        email = StringUtils.lowerCase(StringUtils.trim(getFormValue(formData, "field-underage-email-3")));
        if (StringUtils.isNotBlank(email)) {
          String firstName = getFormValue(formData, "field-underage-first-name-3");
          String lastName = getFormValue(formData, "field-underage-last-name-3");
          
          if (StringUtils.isNotBlank(firstName) && StringUtils.isNotBlank(lastName)) {
            String hash = UUID.randomUUID().toString();
            studentParentRegistrationDAO.create(firstName, lastName, email, student, hash);
          }
        }
      }
      
    }
    
    // Warning if Internetix custom school isn't found
    if (isInternetixLine(getFormValue(formData, "field-line")) && school == null && !StringUtils.isBlank(getFormValue(formData, "field-internetix-contract-school-name"))) {
      String notification = "<b>Huom!</b> Opiskelijan ilmoittamaa oppilaitosta ei löydy vielä Pyramuksesta!";
      ApplicationLogDAO applicationLogDAO = DAOFactory.getInstance().getApplicationLogDAO();
      applicationLogDAO.create(
          application,
          ApplicationLogType.HTML,
          notification,
          null);
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
        StringBuilder createCredentialsUrl = new StringBuilder(getRequestURIRoot(request));
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
      
      String guardianMail = StringUtils.lowerCase(StringUtils.trim(getFormValue(formData, "field-underage-email")));
      
      if (StringUtils.isBlank(guardianMail)) {
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
            guardianMail,
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

      /**
       * Send instructions to guardians how to create their credentials
       */

      StudentParentRegistrationDAO studentParentRegistrationDAO = DAOFactory.getInstance().getStudentParentRegistrationDAO();
      List<StudentParentRegistration> guardians = studentParentRegistrationDAO.listBy(student);

      String guardianEmailContent = IOUtils.toString(request.getServletContext().getResourceAsStream(
          "/templates/applications/mails/mail-credentials-guardian-create.html"), "UTF-8");
      
      for (StudentParentRegistration guardian : guardians) {
        StringBuffer guardianCreateCredentialsLink = new StringBuffer(getRequestURIRoot(request));
        guardianCreateCredentialsLink.append("/parentregister.page?c=");
        guardianCreateCredentialsLink.append(guardian.getHash());
        
        // subject is the same as for above messages
        content = String.format(guardianEmailContent, guardian.getFirstName(), guardianCreateCredentialsLink.toString());
        
        Mailer.sendMail(
            Mailer.JNDI_APPLICATION,
            Mailer.HTML,
            null,
            guardian.getEmail(),
            subject,
            content);

        // Add notification about sent mail

        applicationLogDAO.create(
            application,
            ApplicationLogType.HTML,
            String.format("<p>%s %s %s</p><p><b>%s</b></p>%s", "Huoltajalle", guardian.getEmail(), "lähetetty ohjeet Muikku-tunnuksien luomiseen", subject, content),
            null);
      }
    }
    catch (IOException ioe) {
      logger.log(Level.SEVERE, "Error retrieving mail templates", ioe);
    }
  }

  public static Person resolvePerson(Application application) throws DuplicatePersonException {
    EmailDAO emailDAO = DAOFactory.getInstance().getEmailDAO();
    PersonDAO personDAO = DAOFactory.getInstance().getPersonDAO();
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();

    // Person by social security number
    
    Map<Long, Person> existingPersons = new HashMap<Long, Person>();
    String ssn = extractSSN(application); 
    if (StringUtils.isNotBlank(ssn)) {

      List<Person> persons = personDAO.listBySSNUppercase(ssn);
      for (Person person : persons) {
        existingPersons.put(person.getId(), person);
      }
    }
    
    // Person by email address
    
    String emailAddress = StringUtils.lowerCase(StringUtils.trim(application.getEmail()));
    List<Email> emails = emailDAO.listByAddressLowercase(emailAddress);
    for (Email email : emails) {
      if (email.getContactType() == null || Boolean.FALSE.equals(email.getContactType().getNonUnique())) {
        User user = userDAO.findByContactInfo(email.getContactInfo());
        if (user != null && !Boolean.TRUE.equals(user.getArchived())) {
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
      if (!StringUtils.equals(person.getSocialSecurityNumber(), ssn)) {
        throw new DuplicatePersonException("Hakemuksen ja olemassa olevan käyttäjän henkilötunnus eivät täsmää");
      }
      return person;
    }
  }

  public static String sourceUiValue(String value) {
    if (value != null) {
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
  public static String sanitizeFilename(String filename) {
    filename = StringUtils.trim(filename);
    if (StringUtils.isBlank(filename)) {
      return filename;
    }
    return StringUtils.lowerCase(StringUtils.strip(RegExUtils.removePattern(filename, "[\\\\/:*?\"<>|]"), "."));
  }

  public static String getFormValue(JSONObject object, String key) {
    return object.has(key) ? StringUtils.trim(object.getString(key)) : null;
  }
  
  private static Long getPrimarySignerId(String line) {
    
    Long signerId = null;
    
    // #1333: Primary signer depends on line. Fall back to default signer if line specific one isn't found
    
    SettingKeyDAO settingKeyDAO = DAOFactory.getInstance().getSettingKeyDAO();
    SettingDAO settingDAO = DAOFactory.getInstance().getSettingDAO();
    
    // Try line specific signer first...
    
    SettingKey settingKey = settingKeyDAO.findByName(String.format("%s.%s", SETTINGKEY_SIGNERID, line));
    if (settingKey != null) {
      Setting setting = settingDAO.findByKey(settingKey);
      if (setting != null) {
        signerId = NumberUtils.toLong(setting.getValue());
      }
    }
    
    // ...and fall back to default signer if not found
    
    if (signerId == null) {
      settingKey = settingKeyDAO.findByName(SETTINGKEY_SIGNERID);
      if (settingKey != null) {
        Setting setting = settingDAO.findByKey(settingKey);
        if (setting != null) {
          signerId = NumberUtils.toLong(setting.getValue());
        }
      }
    }

    return signerId;
  }

  private static String getSignature(StaffMember staffMember, String line) {
    StringBuffer sb = new StringBuffer();
    sb.append(String.format("<p>%s</p>", staffMember.getFullName()));
    if (!StringUtils.isBlank(staffMember.getTitle())) {
      sb.append(String.format("<p>%s</p>", StringUtils.capitalize(staffMember.getTitle())));
    }
    if (isOtaviaLine(line)) {
      sb.append("<p>Otavia</p>");
    }
    else {
      sb.append("<p>Otavan Opisto</p>");
    }
    
    return sb.toString();
  }

  public static String getRequestURIRoot(ServletRequest request) {
    StringBuilder rootUri = new StringBuilder();
    rootUri.append(request.getScheme());
    rootUri.append("://");
    rootUri.append(request.getServerName());
    
    boolean includePort = !(
        (StringUtils.equals(request.getScheme(), "http") && request.getServerPort() == 80) ||
        (StringUtils.equals(request.getScheme(), "https") && request.getServerPort() == 443)
    );
    
    if (includePort) {
      rootUri.append(":");
      rootUri.append(request.getServerPort());
    }
    
    return rootUri.toString();
  }
  
  /**
   * Returns true if the StudentParentRegistration invitations 
   * are enabled when approving a student.
   */
  public static boolean isStudentParentRegistrationsEnabled() {
    String enabledSettingValue = SettingUtils.getSettingValue(SETTINGKEY_STUDENTPARENTREGISTRATIONENABLED);
    return StringUtils.isNotBlank(enabledSettingValue) ? Boolean.parseBoolean(enabledSettingValue) : false;
  }
  
}
