package fi.pyramus.json.studentfiles;

import org.apache.commons.fileupload.FileItem;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.file.FileTypeDAO;
import fi.pyramus.dao.file.StudentFileDAO;
import fi.pyramus.dao.students.StudentDAO;
import fi.pyramus.dao.users.UserDAO;
import fi.pyramus.domainmodel.file.FileType;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

/**
 * The controller responsible of modifying an existing student group.
 * 
 * @see fi.pyramus.views.students.EditStudentGroupViewController
 */
public class UploadStudentFileJSONRequestController extends JSONRequestController {

  /**
   * Processes the request to edit a student group.
   * 
   * @param requestContext
   *          The JSON request context
   */
  public void process(JSONRequestContext requestContext) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    StudentFileDAO studentFileDAO = DAOFactory.getInstance().getStudentFileDAO();
    FileTypeDAO fileTypeDAO = DAOFactory.getInstance().getFileTypeDAO();

    Long userId = requestContext.getLoggedUserId();
    User loggedUser = userDAO.findById(userId);
    
    Long studentId = requestContext.getLong("studentId");
    String name = requestContext.getString("fileName");
    Long fileTypeId = requestContext.getLong("fileType");
    FileItem fileItem = requestContext.getFile("file");

    Student student = studentDAO.findById(studentId);
    FileType fileType = fileTypeId != null ? fileTypeDAO.findById(fileTypeId) : null;
    
    if (fileItem != null) {
      byte[] data = fileItem.get();

      studentFileDAO.create(student, name, fileItem.getName(), fileType, fileItem.getContentType(), data, loggedUser);
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
