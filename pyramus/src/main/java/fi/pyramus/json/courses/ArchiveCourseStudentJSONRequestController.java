package fi.pyramus.json.courses;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.courses.CourseStudentDAO;
import fi.pyramus.domainmodel.courses.CourseStudent;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

/** A JSON request controller responsible for archiving course students.
 *
 */
public class ArchiveCourseStudentJSONRequestController extends JSONRequestController {
  
  /** Processes a JSON request.
   * The request should contain the following parameters:
   * <dl>
   *   <dt><code>courseStudentId</code></dt>
   *   <dd>The ID of the course student to archive.</dd>
   * </dl>
   * 
   * @param binaryRequestContext The context of the binary request.
   */
  public void process(JSONRequestContext requestContext) {
    CourseStudentDAO courseStudentDAO = DAOFactory.getInstance().getCourseStudentDAO();
    Long courseStudentId = requestContext.getLong("courseStudentId"); 
    CourseStudent courseStudent = courseStudentDAO.findById(courseStudentId);
    courseStudentDAO.archive(courseStudent);
  }

  /** Returns the user roles allowed to access this controller.
   * 
   * @return The user roles allowed to access this controller.
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
