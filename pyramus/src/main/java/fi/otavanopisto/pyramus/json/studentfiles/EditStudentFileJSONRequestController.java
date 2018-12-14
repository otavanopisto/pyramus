package fi.otavanopisto.pyramus.json.studentfiles;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.fileupload.FileItem;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.file.FileTypeDAO;
import fi.otavanopisto.pyramus.dao.file.StudentFileDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.file.FileType;
import fi.otavanopisto.pyramus.domainmodel.file.StudentFile;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.PyramusFileUtils;
import fi.otavanopisto.pyramus.framework.UserRole;

/**
 * The controller responsible of modifying an existing student group.
 * 
 * @see fi.otavanopisto.pyramus.views.students.EditStudentGroupViewController
 */
public class EditStudentFileJSONRequestController extends JSONRequestController {

  private static final Logger logger = Logger.getLogger(EditStudentFileJSONRequestController.class.getName());

  /**
   * Processes the request to edit a student group.
   * 
   * @param requestContext
   *          The JSON request context
   */
  public void process(JSONRequestContext requestContext) {
    StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffMemberDAO();
    StudentFileDAO studentFileDAO = DAOFactory.getInstance().getStudentFileDAO();
    FileTypeDAO fileTypeDAO = DAOFactory.getInstance().getFileTypeDAO();

    Long userId = requestContext.getLoggedUserId();
    User loggedUser = userDAO.findById(userId);
    
    Long fileId = requestContext.getLong("fileId");
    String name = requestContext.getString("fileName");
    Long fileTypeId = requestContext.getLong("fileType");
    FileItem fileItem = requestContext.getFile("file");

    StudentFile studentFile = studentFileDAO.findById(fileId);
    FileType fileType = fileTypeId != null ? fileTypeDAO.findById(fileTypeId) : null;
    
    studentFileDAO.updateBasicInfo(studentFile, name, studentFile.getFileName(), fileType, loggedUser);

    if (fileItem != null) {
      String fileIdentifier = null;
      byte[] data = fileItem.get();
      if (data != null && data.length > 0) {
        if (PyramusFileUtils.isFileSystemStorageEnabled()) {
          try {
            fileIdentifier = studentFile.getFileId() == null ? PyramusFileUtils.generateFileId() : studentFile.getFileId();
            PyramusFileUtils.storeFile(studentFile.getStudent(), fileIdentifier, data);
            data = null;
          }
          catch (IOException e) {
            logger.log(Level.WARNING, "Store user file to file system failed", e);
          }
        }
        studentFileDAO.updateData(studentFile, fileItem.getContentType(), fileIdentifier, data, loggedUser);
      }
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
