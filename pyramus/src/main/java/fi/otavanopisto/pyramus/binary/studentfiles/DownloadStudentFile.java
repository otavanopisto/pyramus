package fi.otavanopisto.pyramus.binary.studentfiles;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import fi.internetix.smvc.controllers.BinaryRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.file.StudentFileDAO;
import fi.otavanopisto.pyramus.domainmodel.file.StudentFile;
import fi.otavanopisto.pyramus.framework.BinaryRequestController;
import fi.otavanopisto.pyramus.framework.PyramusFileUtils;
import fi.otavanopisto.pyramus.framework.UserRole;

/** A binary request controller responsible for serving files
 * attached to students.
 *
 */
public class DownloadStudentFile extends BinaryRequestController {

  private static final Logger logger = Logger.getLogger(DownloadStudentFile.class.getName());

  /** Processes a binary request.
   * The request should contain the following parameters:
   * <dl>
   *   <dt><code>fileId</code></dt>
   *   <dd>The ID of the student file.</dd>
   * </dl>
   * 
   * @param binaryRequestContext The context of the binary request.
   */
  public void process(BinaryRequestContext binaryRequestContext) {
    StudentFileDAO studentFileDAO = DAOFactory.getInstance().getStudentFileDAO();
    
    Long fileId = binaryRequestContext.getLong("fileId");
    
    StudentFile studentFile = studentFileDAO.findById(fileId);
    
    if (studentFile != null) {
      binaryRequestContext.setFileName(null);
      
      try {
        binaryRequestContext.setResponseContent(PyramusFileUtils.getFileData(studentFile), studentFile.getContentType());
      }
      catch (IOException e) {
        logger.log(Level.SEVERE, String.format("Exception retrieving user file %d", studentFile.getId()), e); 
      }
    }
  }
  
  /** Returns the user roles allowed to access this controller.
   * 
   * @return The user roles allowed to access this controller.
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }
}
