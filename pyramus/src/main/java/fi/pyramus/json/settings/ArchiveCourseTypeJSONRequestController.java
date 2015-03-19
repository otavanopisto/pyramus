package fi.pyramus.json.settings;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.courses.CourseTypeDAO;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

/**
 * The controller responsible of archiving course types 
 */
public class ArchiveCourseTypeJSONRequestController extends JSONRequestController {

  /**
   * Processes the request to archive.
   * 
   * @param jsonRequestContext The JSON request context
   */
  public void process(JSONRequestContext jsonRequestContext) {
    CourseTypeDAO courseTypeDAO = DAOFactory.getInstance().getCourseTypeDAO();
    Long categoryId = jsonRequestContext.getLong("courseTypeId");
    courseTypeDAO.archive(courseTypeDAO.findById(categoryId));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
