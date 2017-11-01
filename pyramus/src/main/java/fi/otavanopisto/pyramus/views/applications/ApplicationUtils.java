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
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationLogType;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationNotification;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationState;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.util.Mailer;

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
    case "mk":
      return "Maahanmuuttajakoulutukset";
    default:
      return null;
    }
  }
  
  public static void sendNotifications(Application application, HttpServletRequest request, StaffMember staffMember, boolean newApplication) {
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
          staffMember.getPrimaryEmail().getAddress(),
          emails,
          mailSubject,
          mailContent);
    }
    
    // Log entry
    
    if (!newApplication) {
      ApplicationLogDAO applicationLogDAO = DAOFactory.getInstance().getApplicationLogDAO();
      applicationLogDAO.create(
        application,
        ApplicationLogType.HTML,
        String.format("Hakemus on siirtynyt tilaan <b>%s</b>", ApplicationUtils.applicationStateUiValue(application.getState())),
        staffMember);
    }
  }

  public static String constructSSN(String birthday, String ssnEnd) {
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
  

}
