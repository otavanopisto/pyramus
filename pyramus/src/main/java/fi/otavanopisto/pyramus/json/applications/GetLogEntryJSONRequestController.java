package fi.otavanopisto.pyramus.json.applications;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationLogDAO;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationLog;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class GetLogEntryJSONRequestController extends JSONRequestController {

  private static final Logger logger = Logger.getLogger(GetLogEntryJSONRequestController.class.getName());

  public void process(JSONRequestContext requestContext) {
    try {
      Long id = requestContext.getLong("id");
      if (id == null) {
        requestContext.getResponse().sendError(HttpServletResponse.SC_BAD_REQUEST);
        return;
      }
      ApplicationLogDAO applicationLogDAO = DAOFactory.getInstance().getApplicationLogDAO();
      ApplicationLog applicationLog = applicationLogDAO.findById(id);
      if (applicationLog == null) {
        requestContext.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
        return;
      }
      requestContext.addResponseParameter("id", applicationLog.getId());
      requestContext.addResponseParameter("type", applicationLog.getType());
      requestContext.addResponseParameter("text", applicationLog.getText());
      if (applicationLog.getUser() != null) {
        requestContext.addResponseParameter("user", applicationLog.getUser().getFullName());
      }
      requestContext.addResponseParameter("date", applicationLog.getDate().getTime());
    }
    catch (Exception e) {
      logger.log(Level.SEVERE, "Error retrieving log entry", e);
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER };
  }

}
