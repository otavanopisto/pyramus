package fi.otavanopisto.pyramus.json.applications;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationDAO;
import fi.otavanopisto.pyramus.dao.application.ApplicationLogDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationLog;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationLogType;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;
import net.sf.json.JSONObject;

public class SaveLogEntryJSONRequestController extends JSONRequestController {

  private static final Logger logger = Logger.getLogger(SaveLogEntryJSONRequestController.class.getName());

  public void process(JSONRequestContext requestContext) {
    try {
      StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
      StaffMember staffMember = staffMemberDAO.findById(requestContext.getLoggedUserId());
      if (staffMember == null) {
        logger.log(Level.WARNING, "Refusing log entry due to staff member not found");
        requestContext.getResponse().sendError(HttpServletResponse.SC_BAD_REQUEST);
        return;
      }
      String formDataStr = getFormData(requestContext.getRequest());
      if (formDataStr == null) {
        logger.log(Level.WARNING, "Refusing log entry due to missing form data");
        requestContext.getResponse().sendError(HttpServletResponse.SC_BAD_REQUEST);
        return;
      }

      JSONObject formData = JSONObject.fromObject(formDataStr);
      
      String applicationId = formData.getString("log-form-application-id");
      ApplicationDAO applicationDAO = DAOFactory.getInstance().getApplicationDAO();
      Application application = applicationDAO.findByApplicationId(applicationId);
      if (application == null) {
        logger.log(Level.WARNING, "Refusing log entry due to missing application");
        requestContext.getResponse().sendError(HttpServletResponse.SC_BAD_REQUEST);
        return;
      }
      String logEntry = formData.getString("log-form-text");
      
      ApplicationLogDAO applicationLogDAO = DAOFactory.getInstance().getApplicationLogDAO();
      ApplicationLog applicationLog = applicationLogDAO.create(application, ApplicationLogType.PLAINTEXT, logEntry, staffMember);
      
      requestContext.addResponseParameter("id", applicationLog.getId());
      requestContext.addResponseParameter("type", applicationLog.getType());
      requestContext.addResponseParameter("text", applicationLog.getText());
      requestContext.addResponseParameter("user", staffMember.getFullName());
      requestContext.addResponseParameter("date", applicationLog.getDate().getTime());
      requestContext.addResponseParameter("owner", Boolean.TRUE);
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

}
