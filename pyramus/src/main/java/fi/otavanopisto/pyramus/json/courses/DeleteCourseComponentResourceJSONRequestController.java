package fi.otavanopisto.pyramus.json.courses;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.courses.CourseComponentResourceDAO;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseComponentResource;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

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
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }
  
}
