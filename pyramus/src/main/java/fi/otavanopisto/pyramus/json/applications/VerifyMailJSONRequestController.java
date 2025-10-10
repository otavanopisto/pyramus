package fi.otavanopisto.pyramus.json.applications;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.applications.ApplicationMailErrorHandler;
import fi.otavanopisto.pyramus.applications.ApplicationUtils;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationDAO;
import fi.otavanopisto.pyramus.dao.application.ApplicationEmailVerificationDAO;
import fi.otavanopisto.pyramus.dao.application.ApplicationLogDAO;
import fi.otavanopisto.pyramus.dao.application.ApplicationNotificationDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationEmailVerification;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationLogType;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationNotification;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.mailer.Mailer;
import net.sf.json.JSONObject;

public class VerifyMailJSONRequestController extends JSONRequestController {

  private static final Logger logger = Logger.getLogger(VerifyMailJSONRequestController.class.getName());

  public void process(JSONRequestContext requestContext) {
    
    // Form validation 
    
    String token = requestContext.getString("token");
    String birthday = requestContext.getString("birthday");
    if (StringUtils.isAnyBlank(token, birthday)) {
      requestContext.sendError(HttpServletResponse.SC_BAD_REQUEST, "Puuttuvia tietoja");
      return;
    }
    String applicationIdStr = StringUtils.substringBefore(token, "-");
    if (!NumberUtils.isDigits(applicationIdStr)) {
      requestContext.sendError(HttpServletResponse.SC_BAD_REQUEST, "Vahvistusvirhe");
      return;
    }
    Long applicationId = Long.valueOf(applicationIdStr);
    ApplicationDAO applicationDAO = DAOFactory.getInstance().getApplicationDAO();
    Application application = applicationDAO.findById(applicationId);
    if (application == null) {
      requestContext.sendError(HttpServletResponse.SC_NOT_FOUND, "Hakemusta ei löytynyt");
      return;
    }
    String verificationToken = StringUtils.substringAfter(token, "-");
    ApplicationEmailVerificationDAO verificationDAO = DAOFactory.getInstance().getApplicationEmailVerificationDAO();
    ApplicationEmailVerification verification = verificationDAO.findByApplicationAndToken(application, verificationToken);
    if (verification == null) {
      requestContext.sendError(HttpServletResponse.SC_NOT_FOUND, "Vahvistuspyyntöä ei löytynyt");
      return;
    }
    
    // Birthday validation
    
    String applicationBirthday = ApplicationUtils.extractBirthdayString(application);
    if (!StringUtils.equals(birthday, applicationBirthday)) {
      requestContext.sendError(HttpServletResponse.SC_BAD_REQUEST, "Syöttämäsi syntymäaika ei vastaa hakemuksessa olevaa syntymäaikaa");
      return;
    }
    
    // Email verified
    
    verificationDAO.updateVerified(verification, true);
    
    // Application log entry

    ApplicationLogDAO applicationLogDAO = DAOFactory.getInstance().getApplicationLogDAO();
    applicationLogDAO.create(application,
        ApplicationLogType.HTML,
        String.format("<p>Sähköpostiosoite %s vahvistettu</p>", verification.getEmail()),
        null);
    
    // Send application edit instructions to applicant (also notify staff about a verified email) 
    
    if (StringUtils.equals(application.getEmail(), verification.getEmail())) {

      // Verification mail to staff
      
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
        viewUrl.append(requestContext.getRequest().getScheme());
        viewUrl.append("://");
        viewUrl.append(requestContext.getRequest().getServerName());
        viewUrl.append(":");
        viewUrl.append(requestContext.getRequest().getServerPort());
        viewUrl.append("/applications/view.page?application=");
        viewUrl.append(application.getId());

        String subject = String.format("Sähköposti vahvistettu [%s %s]",
            application.getFirstName(),
            application.getLastName());
        String content = String.format(
            "<p>Hakija <b>%s %s</b> (%s) on vahvistanut sähköpostiosoitteensa.</p>" +
            "<p>Pääset hakemustietoihin <b><a href=\"%s\">tästä linkistä</a></b>.</p>",
            application.getFirstName(),
            application.getLastName(),
            application.getEmail(),
            viewUrl);

        Mailer.sendMail(Mailer.JNDI_APPLICATION,
            Mailer.HTML,
            null,
            emails,
            subject,
            content,
            new ApplicationMailErrorHandler(application));
      }
      
      // Confirmation mail to applicant
      
      JSONObject formData = JSONObject.fromObject(application.getFormData());
      String line = formData.getString("field-line");
      String surname = application.getLastName();
      String referenceCode = application.getReferenceCode();
      
      try {
        String subject = IOUtils.toString(requestContext.getServletContext().getResourceAsStream(
            String.format("/templates/applications/mails/mail-confirmation-%s-subject.txt", line)), "UTF-8");
        String content = IOUtils.toString(requestContext.getServletContext().getResourceAsStream(
            String.format("/templates/applications/mails/mail-confirmation-%s-content.html", line)), "UTF-8");
        if (StringUtils.isBlank(subject) || StringUtils.isBlank(content)) {
          logger.log(Level.SEVERE, String.format("Confirmation mail for line %s not defined", line));
          return;
        }

        if (!ApplicationUtils.isInternetixLine(application.getLine())) {

          // Replace the dynamic parts of the mail content (edit link, surname and reference code)
          // #1487: Internetix confirmation mails do not have any dynamic content

          StringBuilder viewUrl = new StringBuilder();
          viewUrl.append(requestContext.getRequest().getScheme());
          viewUrl.append("://");
          viewUrl.append(requestContext.getRequest().getServerName());
          viewUrl.append(":");
          viewUrl.append(requestContext.getRequest().getServerPort());
          viewUrl.append("/applications/edit.page");

          content = String.format(content, viewUrl, surname, referenceCode);
        }

        Mailer.sendMail(Mailer.JNDI_APPLICATION, Mailer.HTML, null, verification.getEmail(), subject, content, new ApplicationMailErrorHandler(application));

        applicationLogDAO.create(application,
            ApplicationLogType.HTML,
            String.format("<p>Lähetetty sähköpostia</p><p>%s</p><p><b>%s</b></p>%s", verification.getEmail(), subject, content),
            null);
      }
      catch (IOException e) {
        logger.log(Level.SEVERE, "Error sending mails", e);
      }
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.EVERYONE };
  }

}
