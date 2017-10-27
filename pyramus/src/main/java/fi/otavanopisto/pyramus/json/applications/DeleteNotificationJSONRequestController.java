package fi.otavanopisto.pyramus.json.applications;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationNotificationDAO;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationNotification;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class DeleteNotificationJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext requestContext) {
    ApplicationNotificationDAO applicationNotificationDAO = DAOFactory.getInstance().getApplicationNotificationDAO();
    ApplicationNotification applicationNotification = null;
    Long id = requestContext.getLong("id");
    if (id != null) {
      applicationNotification = applicationNotificationDAO.findById(id);
    }
    if (applicationNotification != null) {
      applicationNotificationDAO.setUsers(applicationNotification, null);
      applicationNotificationDAO.delete(applicationNotification);
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER };
  }

}
