package fi.pyramus.json.courses;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.courses.CourseComponentResourceDAO;
import fi.pyramus.domainmodel.courses.CourseComponentResource;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

/**
 * The controller responsible of deleting an existing course component resource. 
 */
public class DeleteCourseComponentResourceJSONRequestController extends JSONRequestController {
  
  /**
   * Processes the request to delete a course.
   * The request should contain the following parameters:
   * <dl>
   *   <dt><code>courseComponentResourceId</code></dt>
   *   <dd>The ID of the course component resource.</dd>
   * </dl>
   */
  public void process(JSONRequestContext requestContext) {
    CourseComponentResourceDAO componentDAO = DAOFactory.getInstance().getCourseComponentResourceDAO();
    Long componentResourceId = requestContext.getLong("courseComponentResourceId");    
    CourseComponentResource componentResource = componentDAO.findById(componentResourceId);
    componentDAO.delete(componentResource);
  }

  /** Returns the user roles allowed to access this controller.
   * 
   * @return The user roles allowed to access this controller.
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }
  
}
