package fi.pyramus.json.settings;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.courses.CourseDescriptionCategoryDAO;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

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
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
