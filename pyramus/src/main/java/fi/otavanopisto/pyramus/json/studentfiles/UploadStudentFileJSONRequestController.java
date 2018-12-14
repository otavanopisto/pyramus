package fi.otavanopisto.pyramus.json.studentfiles;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.fileupload.FileItem;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.file.FileTypeDAO;
import fi.otavanopisto.pyramus.dao.file.StudentFileDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.file.FileType;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.PyramusFileUtils;
import fi.otavanopisto.pyramus.framework.UserRole;

/**
 * The controller responsible of modifying an existing student group.
 * 
 * @see fi.otavanopisto.pyramus.views.students.EditStudentGroupViewController
 */
public class UploadStudentFileJSONRequestController extends JSONRequestController {

  private static final Logger logger = Logger.getLogger(UploadStudentFileJSONRequestController.class.getName());

  /**
   * Processes the request to edit a student group.
   * 
   * @param requestContext
   *          The JSON request context
   */
  public void process(JSONRequestContext requestContext) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffMemberDAO();
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
      String fileId = null;
      byte[] data = fileItem.get();
      
      if (PyramusFileUtils.isFileSystemStorageEnabled()) {
        try {
          fileId = PyramusFileUtils.generateFileId();
          PyramusFileUtils.storeFile(student, fileId, data);
          data = null;
        }
        catch (IOException e) {
          fileId = null;
          logger.log(Level.SEVERE, "Store user file to file system failed", e);
        }
      }
      
      studentFileDAO.create(student, name, fileItem.getName(), fileId, fileType, fileItem.getContentType(), data, loggedUser);
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
