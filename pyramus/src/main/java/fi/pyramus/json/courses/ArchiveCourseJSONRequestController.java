package fi.pyramus.json.courses;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.courses.CourseDAO;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.domainmodel.courses.Course;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

/** A JSON request controller responsible for archiving courses.
 *
 */
public class ArchiveCourseJSONRequestController extends JSONRequestController {
  
  /** Processes a JSON request.
   * The request should contain the following parameters:
   * <dl>
   *   <dt><code>courseId</code></dt>
   *   <dd>The ID of the course to archive.</dd>
   * </dl>
   * 
   * @param binaryRequestContext The context of the binary request.
   */
  public void process(JSONRequestContext requestContext) {
    CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();
    Long courseId = NumberUtils.createLong(requestContext.getRequest().getParameter("courseId"));
    Course course = courseDAO.findById(courseId);
    courseDAO.archive(course);
  }

  /** Returns the user roles allowed to access this controller.
   * 
   * @return The user roles allowed to access this controller.
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }
  
}
