package fi.otavanopisto.pyramus.json.applications;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class ArchiveApplicationJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext requestContext) {
    ApplicationDAO applicationDAO = DAOFactory.getInstance().getApplicationDAO();
    Long id = requestContext.getLong("id");
    if (id != null) {
      Application application = applicationDAO.findById(id);
      if (application != null) {
        applicationDAO.archive(application);
      }
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER };
  }

}
