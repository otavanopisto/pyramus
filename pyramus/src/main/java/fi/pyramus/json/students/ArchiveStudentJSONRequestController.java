package fi.pyramus.json.students;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.students.StudentDAO;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

public class ArchiveStudentJSONRequestController extends JSONRequestController {
  
  public void process(JSONRequestContext requestContext) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    Long studentId = requestContext.getLong("student");
    Student student = studentDAO.findById(studentId);
    studentDAO.archive(student);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
