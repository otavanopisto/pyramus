package fi.otavanopisto.pyramus.binary.students;

import java.io.IOException;

import jakarta.servlet.ServletOutputStream;

import fi.internetix.smvc.controllers.BinaryRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.students.StudentImageDAO;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentImage;
import fi.otavanopisto.pyramus.framework.BinaryRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

/** A binary request controller responsible for serving images of students.
 * 
 */
public class ViewStudentImage extends BinaryRequestController {

  /** Processes a binary request.
   * The request should contain the following parameters:
   * <dl>
   *   <dt><code>studentId</code></dt>
   *   <dd>The ID of the student.</dd>
   * </dl>
   * 
   * @param binaryRequestContext The context of the binary request.
   */
  public void process(BinaryRequestContext binaryRequestContext) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    StudentImageDAO imageDAO = DAOFactory.getInstance().getStudentImageDAO();
    
    Long studentId = binaryRequestContext.getLong("studentId");
    
    Student student = studentDAO.findById(studentId);
    StudentImage studentImage = imageDAO.findByStudent(student);
    
    if (studentImage != null) {
      binaryRequestContext.getResponse().setContentType(studentImage.getContentType());
  
      try {
        ServletOutputStream outputStream = binaryRequestContext.getResponse().getOutputStream();
        
        outputStream.write(studentImage.getData());
        
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }
  
  /** Returns the user roles allowed to access this controller.
   * 
   * @return The user roles allowed to access this controller.
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.USER, UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }
}
