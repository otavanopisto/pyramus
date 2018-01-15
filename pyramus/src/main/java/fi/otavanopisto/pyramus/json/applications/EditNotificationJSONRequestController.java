package fi.otavanopisto.pyramus.json.applications;

import java.util.HashSet;
import java.util.Set;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationNotificationDAO;
import fi.otavanopisto.pyramus.dao.users.UserDAO;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationNotification;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationState;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class EditNotificationJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext requestContext) {
    ApplicationNotificationDAO applicationNotificationDAO = DAOFactory.getInstance().getApplicationNotificationDAO();
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();

    Long notificationId = requestContext.getLong("notificationId");
    ApplicationNotification applicationNotification = applicationNotificationDAO.findById(notificationId); 
    
    String line = requestContext.getString("line");
    ApplicationState state = ApplicationState.valueOf(requestContext.getString("state"));
    Set<User> users = new HashSet<>();
    int rowCount = requestContext.getInteger("usersTable.rowCount");
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "usersTable." + i;
      Long userId = requestContext.getLong(colPrefix + ".userId");
      User user = userDAO.findById(userId);
      users.add(user);
    }
    applicationNotification = applicationNotificationDAO.update(applicationNotification, line, state);
    applicationNotificationDAO.setUsers(applicationNotification, users);
    requestContext.setRedirectURL(
        requestContext.getRequest().getContextPath() +
        "/applications/editnotification.page?notification=" +
        applicationNotification.getId());
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER };
  }

}
