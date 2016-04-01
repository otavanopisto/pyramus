package fi.otavanopisto.pyramus.json.students;

import org.apache.commons.fileupload.FileItem;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.students.StudentImageDAO;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentImage;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

/**
 * The controller responsible of modifying an existing student group.
 * 
 * @see fi.otavanopisto.pyramus.views.students.EditStudentGroupViewController
 */
public class EditStudentImageJSONRequestController extends JSONRequestController {

  /**
   * Processes the request to edit a student group.
   * 
   * @param requestContext
   *          The JSON request context
   */
  public void process(JSONRequestContext requestContext) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    StudentImageDAO imageDAO = DAOFactory.getInstance().getStudentImageDAO();

    String deleteImage = requestContext.getString("deleteImage");
    Long studentId = requestContext.getLong("studentId");
    FileItem studentImage = requestContext.getFile("studentImage");

    Student student = studentDAO.findById(studentId);
    StudentImage oldImage = imageDAO.findByStudent(student);
    
    if (deleteImage == null) {
      if (studentImage != null) {
        String contentType = studentImage.getContentType();
        byte[] data = studentImage.get();
        
        if (oldImage != null) {
          imageDAO.update(oldImage, contentType, data);
        } else {
          imageDAO.create(student, contentType, data);
        }
      }
    } else {
      imageDAO.delete(oldImage);
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
