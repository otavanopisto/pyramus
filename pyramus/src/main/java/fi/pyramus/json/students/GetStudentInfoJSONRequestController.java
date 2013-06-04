package fi.pyramus.json.students;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.students.StudentDAO;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

/**
 * JSON request controller to view student info.
 * 
 * @author antti.viljakainen
 */
public class GetStudentInfoJSONRequestController extends JSONRequestController {
  
  /**
   * Processes JSON request
   * 
   * In parameters
   * - studentId - student id to retrieve information for
   * 
   * Page parameters
   * - student - Map including
   * * id - Student id
   * * firstname - First name
   * * lastname - Last name
   * 
   * @param requestContext JSON request context
   */
  public void process(JSONRequestContext requestContext) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();

    Long studentId = NumberUtils.createLong(requestContext.getRequest().getParameter("studentId"));
    Student student = studentDAO.findById(studentId);
    
    Map<String, Object> studentInfo = new HashMap<String, Object>();
    
    studentInfo.put("id", student.getId());
    studentInfo.put("firstname", student.getFirstName());
    studentInfo.put("lastname", student.getLastName());
    
    requestContext.addResponseParameter("student", studentInfo);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.EVERYONE };
  }

}
