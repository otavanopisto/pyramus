package fi.otavanopisto.pyramus.json.settings;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.courses.CourseDescriptionCategoryDAO;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

/**
 * The controller responsible of archiving. 
 */
public class ArchiveCourseDescriptionCategoryJSONRequestController extends JSONRequestController {

  /**
   * Processes the request to archive.
   * 
   * @param jsonRequestContext The JSON request context
   */
  public void process(JSONRequestContext jsonRequestContext) {
    CourseDescriptionCategoryDAO descriptionCategoryDAO = DAOFactory.getInstance().getCourseDescriptionCategoryDAO();
    Long categoryId = jsonRequestContext.getLong("categoryId");
    descriptionCategoryDAO.archive(descriptionCategoryDAO.findById(categoryId));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
