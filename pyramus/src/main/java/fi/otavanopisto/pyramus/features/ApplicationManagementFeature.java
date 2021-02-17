package fi.otavanopisto.pyramus.features;

import fi.internetix.smvc.Feature;
import fi.internetix.smvc.controllers.RequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationNotificationDAO;
import fi.otavanopisto.pyramus.dao.users.UserDAO;
import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.domainmodel.users.User;

public class ApplicationManagementFeature implements FeatureResolver {

  public Feature resolve(RequestContext requestContext) {
    boolean hasFeature = false;
    Long userId = requestContext.getLoggedUserId();
    if (userId != null) {
      UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
      User user = userDAO.findById(userId);
      // Allow access to administrators...
      hasFeature = user != null && user.getRole() == Role.ADMINISTRATOR;
      if (!hasFeature) {
        // ...and people who have been set to receive application notifications
        ApplicationNotificationDAO applicationNotificationDAO = DAOFactory.getInstance().getApplicationNotificationDAO();
        hasFeature = applicationNotificationDAO.countByUser(user) > 0;
      }
    }
    return hasFeature ? Feature.APPLICATION_MANAGEMENT : null;
  }

}
