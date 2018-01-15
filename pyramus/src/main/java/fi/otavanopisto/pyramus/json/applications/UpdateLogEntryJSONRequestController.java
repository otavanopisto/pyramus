package fi.otavanopisto.pyramus.json.applications;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationLogDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationLog;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class UpdateLogEntryJSONRequestController extends JSONRequestController {

  private static final Logger logger = Logger.getLogger(UpdateLogEntryJSONRequestController.class.getName());

  public void process(JSONRequestContext requestContext) {
    try {
      StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
      StaffMember staffMember = staffMemberDAO.findById(requestContext.getLoggedUserId());
      if (staffMember == null) {
        logger.log(Level.WARNING, "Refusing log entry due to staff member not found");
        requestContext.getResponse().sendError(HttpServletResponse.SC_BAD_REQUEST);
        return;
      }
      Long id = requestContext.getLong("id");
      String text = requestContext.getRequest().getParameter("text");
      if (id == null || text == null) {
        requestContext.getResponse().sendError(HttpServletResponse.SC_BAD_REQUEST);
        return;
      }
      ApplicationLogDAO applicationLogDAO = DAOFactory.getInstance().getApplicationLogDAO();
      ApplicationLog applicationLog = applicationLogDAO.findById(id);
      if (applicationLog != null) {
        applicationLog = applicationLogDAO.update(applicationLog, text, staffMember);
        requestContext.getResponse().setStatus(HttpServletResponse.SC_OK);
        requestContext.addResponseParameter("id", applicationLog.getId());
        requestContext.addResponseParameter("type", applicationLog.getType());
        requestContext.addResponseParameter("text", applicationLog.getText());
        requestContext.addResponseParameter("user", staffMember.getFullName());
        requestContext.addResponseParameter("date", applicationLog.getDate().getTime());
      }
      else {
        requestContext.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
      }
    }
    catch (Exception e) {
      logger.log(Level.SEVERE, "Error saving log entry", e);
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER };
  }

}
