package fi.otavanopisto.pyramus.util;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.events.ApplicationCreatedEvent;
import fi.otavanopisto.pyramus.events.ApplicationModifiedByApplicantEvent;
import fi.otavanopisto.pyramus.views.applications.ApplicationUtils;
import net.sf.json.JSONObject;

public class ApplicationMailerUtils {

  @Inject
  private HttpServletRequest httpRequest;

  private static final Logger logger = Logger.getLogger(ApplicationMailerUtils.class.getName());

  public void onApplicationModifiedByApplicant(@Observes ApplicationModifiedByApplicantEvent event) {
    ApplicationDAO applicationDAO = DAOFactory.getInstance().getApplicationDAO();
    Application application = applicationDAO.findById(event.getEntityId());
    if (application != null && application.getHandler() != null && application.getHandler().getPrimaryEmail() != null) {

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
          ApplicationUtils.applicationLineUiValue(application.getLine()), viewUrl);
      Mailer.sendMail(Mailer.JNDI_APPLICATION, Mailer.HTML, application.getEmail(),
          application.getHandler().getPrimaryEmail().getAddress(), subject, content);
    }
  }

  public void onApplicationCreated(@Observes ApplicationCreatedEvent event) {
    ApplicationDAO applicationDAO = DAOFactory.getInstance().getApplicationDAO();
    Application application = applicationDAO.findById(event.getEntityId());
    if (application != null) {

      // Mail to the applicant

      JSONObject formData = JSONObject.fromObject(application.getFormData());
      String line = ApplicationUtils.applicationLineUiValue(formData.getString("field-line"));
      String surname = application.getLastName();
      String referenceCode = application.getReferenceCode();
      String applicantMail = application.getEmail();
      String guardianMail = formData.getString("field-underage-email");
      try {
        String subject = "Hakemus opiskelemaan Otavan Opistoon vastaanotettu";
        String content = IOUtils.toString(
            httpRequest.getServletContext().getResourceAsStream("/templates/applications/mail-confirmation.html"));
        content = String.format(content, line, surname, referenceCode);
        if (StringUtils.isEmpty(guardianMail)) {
          Mailer.sendMail(Mailer.JNDI_APPLICATION, Mailer.HTML, null, applicantMail, subject, content);
        }
        else {
          Mailer.sendMail(Mailer.JNDI_APPLICATION, Mailer.HTML, null, applicantMail, guardianMail, subject, content);
        }

        // Notification mails and log entries

        ApplicationUtils.sendNotifications(application, httpRequest, null, true);
      }
      catch (IOException e) {
        logger.log(Level.SEVERE, "Unable to retrieve confirmation mail template", e);
      }
    }
  }

}
