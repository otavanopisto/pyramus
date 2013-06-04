package fi.pyramus.views.students;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.students.StudentDAO;
import fi.pyramus.dao.students.StudentImageDAO;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.domainmodel.users.Role;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;

/**
 * The controller responsible of the Edit Student Image Dialog view of the application.
 */
public class EditStudentImageDialogViewController extends PyramusViewController {

  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    StudentImageDAO imageDAO = DAOFactory.getInstance().getStudentImageDAO();

    Long studentId = pageRequestContext.getLong("studentId");
    
    Student student = studentDAO.findById(studentId);
    Boolean hasImage = imageDAO.findStudentHasImage(student);
    
    pageRequestContext.getRequest().setAttribute("student", student);
    pageRequestContext.getRequest().setAttribute("studentHasImage", hasImage);
    
    pageRequestContext.setIncludeJSP("/templates/students/editstudentimagedialog.jsp");
  }

  /**
   * Returns the roles allowed to access this page. Editing student groups is available for users with
   * {@link Role#MANAGER} or {@link Role#ADMINISTRATOR} privileges.
   * 
   * @return The roles allowed to access this page
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
