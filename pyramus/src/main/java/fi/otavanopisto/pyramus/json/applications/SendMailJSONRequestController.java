package fi.otavanopisto.pyramus.json.applications;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.applications.ApplicationMailErrorHandler;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationDAO;
import fi.otavanopisto.pyramus.dao.application.ApplicationLogDAO;
import fi.otavanopisto.pyramus.dao.users.EmailSignatureDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationLogType;
import fi.otavanopisto.pyramus.domainmodel.users.EmailSignature;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.mailer.Mailer;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class SendMailJSONRequestController extends JSONRequestController {

  private static final Logger logger = Logger.getLogger(SendMailJSONRequestController.class.getName());

  public void process(JSONRequestContext requestContext) {
    try {
      
      // Sender
      
      StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
      StaffMember staffMember = staffMemberDAO.findById(requestContext.getLoggedUserId());
      if (staffMember == null) {
        requestContext.sendError(HttpServletResponse.SC_BAD_REQUEST, "Käsittelijää ei löydy");
        return;
      }
      
      // Form data

      String formDataStr = getFormData(requestContext.getRequest());
      if (formDataStr == null) {
        requestContext.sendError(HttpServletResponse.SC_BAD_REQUEST, "Hakemusta ei löydy");
        return;
      }
      JSONObject formData = JSONObject.fromObject(formDataStr);
      
      // Application
      
      Long applicationEntityId = formData.getLong("applicationEntityId");
      ApplicationDAO applicationDAO = DAOFactory.getInstance().getApplicationDAO();
      Application application = applicationDAO.findById(applicationEntityId);
      if (application == null) {
        requestContext.sendError(HttpServletResponse.SC_BAD_REQUEST, "Hakemusta ei löydy");
        return;
      }
      
      // Mail content
      
      Set<String> recipients = new HashSet<String>();
      if (formData.has("mail-form-recipient")) {
        JSONArray jsonRecipients = toJSONArray(formData.getString("mail-form-recipient"));
        for (int i = 0; i < jsonRecipients.size(); i++) {
          recipients.add(jsonRecipients.get(i).toString());
        }
      }
      if (recipients.isEmpty()) {
        requestContext.sendError(HttpServletResponse.SC_BAD_REQUEST, "Viestille ei ole valittu vastaanottajia");
        return;
      }
      String subject = formData.getString("mail-form-subject");
      String content = formData.getString("mail-form-content");
      if (StringUtils.isEmpty(subject) || StringUtils.isEmpty(content)) {
        requestContext.sendError(HttpServletResponse.SC_BAD_REQUEST, "Viestistä puuttuu otsikko ja/tai sisältö");
        return;
      }
      
      // #870: Email signature
      
      EmailSignatureDAO emailSignatureDAO = DAOFactory.getInstance().getEmailSignatureDAO();
      EmailSignature emailSignature = emailSignatureDAO.findByUser(staffMember);
      if (emailSignature != null) {
        content += emailSignature.getSignature();
      }
      
      // Send mail
      
      Mailer.sendMail(Mailer.JNDI_APPLICATION,
          Mailer.HTML,
          staffMember.getPrimaryEmail().getAddress(),
          Collections.emptySet(), // to
          Collections.emptySet(), // cc
          recipients, // bcc
          subject,
          content,
          new ApplicationMailErrorHandler(application));
      
      StringBuffer recipientMails = new StringBuffer();
      for (String s : recipients) {
        recipientMails.append(s);
        recipientMails.append("<br/>");
      }
      
      // Application log entry
      
      ApplicationLogDAO applicationLogDAO = DAOFactory.getInstance().getApplicationLogDAO();
      applicationLogDAO.create(application,
          ApplicationLogType.HTML,
          String.format("<p>Lähetetty sähköposti. Vastaanottajat:<br/>%s</p><p>Viesti:</p><p><b>%s</b></p>%s", recipientMails.toString(), subject, content),
          staffMember);
    }
    catch (Exception e) {
      logger.log(Level.SEVERE, "Error saving log entry", e);
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER };
  }
  
  private String getFormData(HttpServletRequest req) throws IOException {
    StringBuilder sb = new StringBuilder();
    BufferedReader reader = req.getReader();
    try {
      String line;
      while ((line = reader.readLine()) != null) {
        sb.append(line).append('\n');
      }
    }
    finally {
      reader.close();
    }
    return sb.toString();
  }
  
  private JSONArray toJSONArray(String value) {
    if (StringUtils.isBlank(value)) {
      return JSONArray.fromObject("[]");
    }
    else if (StringUtils.startsWith(value, "[")) {
      return JSONArray.fromObject(value);
    }
    else {
      return JSONArray.fromObject(String.format("['%s']", value));
    }
  }

}
