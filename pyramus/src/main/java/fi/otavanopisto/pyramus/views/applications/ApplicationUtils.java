package fi.otavanopisto.pyramus.views.applications;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationLogDAO;
import fi.otavanopisto.pyramus.dao.application.ApplicationNotificationDAO;
import fi.otavanopisto.pyramus.dao.base.LanguageDAO;
import fi.otavanopisto.pyramus.dao.base.MunicipalityDAO;
import fi.otavanopisto.pyramus.dao.base.NationalityDAO;
import fi.otavanopisto.pyramus.dao.base.SchoolDAO;
import fi.otavanopisto.pyramus.dao.base.StudyProgrammeDAO;
import fi.otavanopisto.pyramus.dao.students.StudentActivityTypeDAO;
import fi.otavanopisto.pyramus.dao.students.StudentExaminationTypeDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationLogType;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationNotification;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationState;
import fi.otavanopisto.pyramus.domainmodel.base.Language;
import fi.otavanopisto.pyramus.domainmodel.base.Municipality;
import fi.otavanopisto.pyramus.domainmodel.base.Nationality;
import fi.otavanopisto.pyramus.domainmodel.base.School;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme;
import fi.otavanopisto.pyramus.domainmodel.students.Sex;
import fi.otavanopisto.pyramus.domainmodel.students.StudentActivityType;
import fi.otavanopisto.pyramus.domainmodel.students.StudentExaminationType;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.util.Mailer;
import net.sf.json.JSONObject;

public class ApplicationUtils {

  public static String applicationStateUiValue(ApplicationState applicationState) {
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
      return "Rekisteröitynyt aineopiskelijaksi";
    case REJECTED:
      return "Hylätty";
    }
    return null;
  }

  public static String applicationLineUiValue(String value) {
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
    if (!StringUtils.isBlank(ssnEnd) && ssnEnd.length() == 4) {
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
    case "ammatillinen-korkea":
      return studentExaminationTypeDAO.findById(3L); // Ammattikorkeakoulututkinto
    case "kaksoistutkinto":
      return studentExaminationTypeDAO.findById(4L); // Kaksoistutkinto
    case "yo-tutkinto":
      return studentExaminationTypeDAO.findById(5L); // YO-tutkinto / lukion oppimäärä
    case "oppisopimus":
      return studentExaminationTypeDAO.findById(6L); // Oppisopimuskoulutus
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
      return null;
    case "laakislinja":
      return studyProgrammeDAO.findById(31L); // Lääketieteen opintoihin valmentava koulutus
    case "mk":
      switch (foreignLine) {
      case "apa":
        return studyProgrammeDAO.findById(29L); // Aikuisten perusopetuksen alkuvaiheen opetus
      case "pk":
        return studyProgrammeDAO.findById(15L); // Monikulttuurinen peruskoululinja
      case "luva":
        return studyProgrammeDAO.findById(19L); // Lukioon valmistava peruskoululinja maahanmuuttajille
      default:
        return null;
      }
    default:
      return null;
    }
  }
  
  public static void sendNotifications(Application application, HttpServletRequest request, StaffMember staffMember, boolean newApplication, String notificationPostfix) {
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
        mailSubject = String.format("Uusi hakemus linjalle %s", ApplicationUtils.applicationLineUiValue(application.getLine()));
        mailContent = String.format(
          "<p>Hakija <b>%s %s</b> (%s) on jättänyt hakemuksen linjalle <b>%s</b>.</p>" +
          "<p>Pääset hakemustietoihin <b><a href=\"%s\">tästä linkistä</a></b>.</p>",
          application.getFirstName(),
          application.getLastName(),
          application.getEmail(),
          ApplicationUtils.applicationLineUiValue(application.getLine()),
          viewUrl);
      }
      else {
        mailSubject = "Hakemuksen tila on muuttunut";
        mailContent = String.format(
          "<p>Hakijan <b>%s %s</b> (%s) hakemus linjalle <b>%s</b> on siirtynyt tilaan <b>%s</b>.</p>" +
          "<p>Pääset hakemustietoihin <b><a href=\"%s\">tästä linkistä</a></b>.</p>",
          application.getFirstName(),
          application.getLastName(),
          application.getEmail(),
          ApplicationUtils.applicationLineUiValue(application.getLine()),
          ApplicationUtils.applicationStateUiValue(application.getState()),
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
    
    if (!newApplication) {
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
    if (StringUtils.isBlank(birthday) || StringUtils.isBlank(ssnEnd)) {
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
  
  private static String getFormValue(JSONObject object, String key) {
    return object.has(key) ? object.getString(key) : null;
  }

}
