package fi.otavanopisto.pyramus.json.applications;

import java.util.logging.Logger;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.applications.ApplicationUtils;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationState;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class ArchiveApplicationJSONRequestController extends JSONRequestController {

  private static final Logger logger = Logger.getLogger(ArchiveApplicationJSONRequestController.class.getName());

  public void process(JSONRequestContext requestContext) {
    ApplicationDAO applicationDAO = DAOFactory.getInstance().getApplicationDAO();
    Long id = requestContext.getLong("id");
    if (id != null) {
      Application application = applicationDAO.findById(id);
      if (application != null) {
        if (application.getState() == ApplicationState.TRANSFERRED_AS_STUDENT) {
          logger.warning(String.format("User %s attempt to delete application %s (%d) revoked due to applicant already transferred as student",
              requestContext.getLoggedUserId() == null ? "?" : requestContext.getLoggedUserId().toString(),
              application.getApplicationId(), application.getId()));
          return;
        }
        ApplicationUtils.deleteApplication(requestContext.getLoggedUserId(), application, "archive application");
      }
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER };
  }

}
