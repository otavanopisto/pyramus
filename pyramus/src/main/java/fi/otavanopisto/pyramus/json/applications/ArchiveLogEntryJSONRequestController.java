package fi.otavanopisto.pyramus.json.applications;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationDAO;
import fi.otavanopisto.pyramus.dao.application.ApplicationLogDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationLog;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class ArchiveLogEntryJSONRequestController extends JSONRequestController {

  private static final Logger logger = Logger.getLogger(ArchiveLogEntryJSONRequestController.class.getName());

  public void process(JSONRequestContext requestContext) {
    try {
      StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
      StaffMember staffMember = requestContext.getLoggedUserId() == null ? null : staffMemberDAO.findById(requestContext.getLoggedUserId());
      ApplicationDAO applicationDAO = DAOFactory.getInstance().getApplicationDAO();
      ApplicationLogDAO applicationLogDAO = DAOFactory.getInstance().getApplicationLogDAO();
      ApplicationLog applicationLog = null;
      Long id = requestContext.getLong("id");
      if (id != null) {
        applicationLog = applicationLogDAO.findById(id);
      }
      if (applicationLog == null) {
        logger.log(Level.WARNING, "Refusing log entry archive due to entry not found");
        requestContext.getResponse().sendError(HttpServletResponse.SC_BAD_REQUEST);
        return;
      }
      Application application = applicationLog.getApplication();
      applicationLogDAO.archive(applicationLog);
      applicationDAO.updateLastModified(application, staffMember);
    }
    catch (Exception e) {
      logger.log(Level.SEVERE, "Error archiving log entry", e);
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER };
  }

}
