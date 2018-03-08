package fi.otavanopisto.pyramus.util;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.applications.ApplicationUtils;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.events.ApplicationCreatedEvent;
import fi.otavanopisto.pyramus.events.ApplicationModifiedByApplicantEvent;
import fi.otavanopisto.pyramus.mailer.Mailer;
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
          ApplicationUtils.applicationLineUiValue(application.getLine()),
          viewUrl);
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
      String line = formData.getString("field-line");
      String lineUi = ApplicationUtils.applicationLineUiValue(line);
      String surname = application.getLastName();
      String referenceCode = application.getReferenceCode();
      String applicantMail = application.getEmail();
      String guardianMail = formData.getString("field-underage-email");
      try {
        
        // Confirmation mail subject and content
        
        String subject = "Hakemus opiskelemaan Otavan Opistoon vastaanotettu";
        String content = IOUtils.toString(httpRequest.getServletContext().getResourceAsStream(
            "/templates/applications/mail-confirmation.html"), "UTF-8");
        
        // #577: Contact information depends on the line selected; append suitable footer to mail content
        
        try {
          String contentFooter = IOUtils.toString(httpRequest.getServletContext().getResourceAsStream(
              String.format("/templates/applications/mail-confirmation-footer-%s.html", line)), "UTF-8");
          if (contentFooter != null) {
            content += contentFooter;
          }
        }
        catch (Exception e) {
          logger.log(Level.WARNING, String.format("No mail confirmation footer for line %s", line), e);
        }
        
        // Replace the dynamic parts of the mail content
        
        content = String.format(content, lineUi, surname, referenceCode);
        
        // Send mail to applicant or, for minors, applicant and guardian
        
        if (StringUtils.isEmpty(guardianMail)) {
          Mailer.sendMail(Mailer.JNDI_APPLICATION, Mailer.HTML, null, applicantMail, subject, content);
        }
        else {
          Mailer.sendMail(Mailer.JNDI_APPLICATION, Mailer.HTML, null, applicantMail, guardianMail, subject, content);
        }

        // Handle notification mails and log entries

        ApplicationUtils.sendNotifications(application, httpRequest, null, true, null);
      }
      catch (IOException e) {
        logger.log(Level.SEVERE, "Unable to retrieve confirmation mail template", e);
      }
    }
  }

}
