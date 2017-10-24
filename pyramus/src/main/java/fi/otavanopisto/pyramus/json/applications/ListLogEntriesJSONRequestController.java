package fi.otavanopisto.pyramus.json.applications;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationDAO;
import fi.otavanopisto.pyramus.dao.application.ApplicationLogDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationLog;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class ListLogEntriesJSONRequestController extends JSONRequestController {

  private static final Logger logger = Logger.getLogger(ListLogEntriesJSONRequestController.class.getName());

  public void process(JSONRequestContext requestContext) {
    try {
      ApplicationDAO applicationDAO = DAOFactory.getInstance().getApplicationDAO();
      ApplicationLogDAO applicationLogDAO = DAOFactory.getInstance().getApplicationLogDAO();
      String applicationId = requestContext.getRequest().getParameter("applicationId");
      Application application = applicationDAO.findByApplicationId(applicationId);
      if (application == null) {
        logger.log(Level.WARNING, "Unable to load log entries due to missing application");
        requestContext.getResponse().sendError(HttpServletResponse.SC_BAD_REQUEST);
        return;
      }
      List<Map<String, Object>> results = new ArrayList<>();
      List<ApplicationLog> logEntries = applicationLogDAO.listByApplication(application);
      for (ApplicationLog logEntry : logEntries) {
        Map<String, Object> logEntryInfo = new HashMap<>();
        logEntryInfo.put("id", logEntry.getId());
        logEntryInfo.put("type", logEntry.getType());
        logEntryInfo.put("text", logEntry.getText());
        logEntryInfo.put("user", logEntry.getUser().getFullName());
        logEntryInfo.put("date", logEntry.getDate().getTime());
        logEntryInfo.put("owner", requestContext.getLoggedUserId() != null &&
            logEntry.getUser() != null &&
            logEntry.getUser().getId() != null &&
            requestContext.getLoggedUserId().equals(logEntry.getUser().getId()));
        results.add(logEntryInfo);
      }
      requestContext.addResponseParameter("logEntries", results);
    }
    catch (Exception e) {
      logger.log(Level.SEVERE, "Error loading log entries", e);
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER };
  }

}
