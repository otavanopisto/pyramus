package fi.otavanopisto.pyramus.json.applications;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationLogDAO;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationLog;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class ArchiveLogEntryJSONRequestController extends JSONRequestController {

  private static final Logger logger = Logger.getLogger(ArchiveLogEntryJSONRequestController.class.getName());

  public void process(JSONRequestContext requestContext) {
    try {
      ApplicationLogDAO applicationLogDAO = DAOFactory.getInstance().getApplicationLogDAO();
      ApplicationLog applicationLog = null;
      Long id = NumberUtils.toLong(requestContext.getRequest().getParameter("id"));
      if (id != null) {
        applicationLog = applicationLogDAO.findById(id);
      }
      if (applicationLog == null) {
        logger.log(Level.WARNING, "Refusing log entry archive due to entry not found");
        requestContext.getResponse().sendError(HttpServletResponse.SC_BAD_REQUEST);
        return;
      }
      applicationLogDAO.archive(applicationLog);
    }
    catch (Exception e) {
      logger.log(Level.SEVERE, "Error archiving log entry", e);
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER };
  }

}
