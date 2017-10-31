package fi.otavanopisto.pyramus.json.applications;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationDAO;
import fi.otavanopisto.pyramus.dao.application.ApplicationLogDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationLogType;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.util.Mailer;
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
        logger.log(Level.WARNING, "Refusing application mail due to staff member not found");
        requestContext.getResponse().sendError(HttpServletResponse.SC_BAD_REQUEST);
        return;
      }
      
      // Form data

      String formDataStr = getFormData(requestContext.getRequest());
      if (formDataStr == null) {
        logger.log(Level.WARNING, "Refusing application mail due to missing form data");
        requestContext.getResponse().sendError(HttpServletResponse.SC_BAD_REQUEST);
        return;
      }
      JSONObject formData = JSONObject.fromObject(formDataStr);
      
      // Application
      
      Long applicationEntityId = formData.getLong("applicationEntityId");
      ApplicationDAO applicationDAO = DAOFactory.getInstance().getApplicationDAO();
      Application application = applicationDAO.findById(applicationEntityId);
      if (application == null) {
        logger.log(Level.WARNING, "Refusing application mail due to missing application");
        requestContext.getResponse().sendError(HttpServletResponse.SC_BAD_REQUEST);
        return;
      }
      
      // Mail content
      
      Set<String> toRecipients = new HashSet<String>();
      JSONArray recipients = toJSONArray(formData.getString("mail-form-recipient-to"));
      for (int i = 0; i < recipients.size(); i++) {
        toRecipients.add(recipients.get(i).toString());
      }
      Set<String> ccRecipients = new HashSet<String>();
      if (formData.has("mail-form-recipient-cc")) {
        recipients = toJSONArray(formData.getString("mail-form-recipient-cc"));
        for (int i = 0; i < recipients.size(); i++) {
          ccRecipients.add(recipients.get(i).toString());
        }
      }
      if (toRecipients.isEmpty() && ccRecipients.isEmpty()) {
        logger.log(Level.WARNING, "Refusing application mail due to missing recipients");
        requestContext.getResponse().sendError(HttpServletResponse.SC_BAD_REQUEST);
        return;
      }
      else if (toRecipients.isEmpty() && !ccRecipients.isEmpty()) {
        toRecipients.addAll(ccRecipients);
        ccRecipients.clear();
      }
      String subject = formData.getString("mail-form-subject");
      String content = formData.getString("mail-form-content");
      if (StringUtils.isEmpty(subject) || StringUtils.isEmpty(content)) {
        logger.log(Level.WARNING, "Refusing application mail due to missing subject or content");
        requestContext.getResponse().sendError(HttpServletResponse.SC_BAD_REQUEST);
        return;
      }
      
      // Send mail
      
      Mailer.sendMail(Mailer.JNDI_APPLICATION,
          Mailer.HTML,
          staffMember.getPrimaryEmail().getAddress(),
          toRecipients,
          ccRecipients,
          subject,
          content);
      
      // Application log entry
      
      ApplicationLogDAO applicationLogDAO = DAOFactory.getInstance().getApplicationLogDAO();
      applicationLogDAO.create(application,
          ApplicationLogType.HTML,
          String.format("<p>Hakijalle lähetettiin sähköpostia:</p><p><b>%s</b></p>%s", subject, content),
          staffMember);
    }
    catch (Exception e) {
      logger.log(Level.SEVERE, "Error saving log entry", e);
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER };
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
