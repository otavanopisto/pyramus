package fi.otavanopisto.pyramus.views.studentfiles;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.file.StudentFileDAO;
import fi.otavanopisto.pyramus.domainmodel.file.StudentFile;
import fi.otavanopisto.pyramus.framework.PyramusFileUtils;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class MoveStudentFilesViewController extends PyramusViewController {

  private static final Logger logger = Logger.getLogger(MoveStudentFilesViewController.class.getName());

  public void process(PageRequestContext pageRequestContext) {
    int count = pageRequestContext.getInteger("count");
    StudentFileDAO studentFileDAO = DAOFactory.getInstance().getStudentFileDAO();
    if (count > 0) {
      int bytes = 0;
      int totalBytes = 0;
      int totalFiles = 0;
      Long currentEntityId = PyramusFileUtils.getLastMovedEntityId();
      List<Long> entityIds = studentFileDAO.listIdsByLargerAndLimit(currentEntityId, count);
      for (Long entityId : entityIds) {
        try {
          StudentFile studentFile = studentFileDAO.findById(entityId); 
          if (studentFile != null) {
            bytes = PyramusFileUtils.relocateToFileSystem(studentFile);
            if (bytes > 0) {
              totalBytes += bytes;
              totalFiles++;
            }
            currentEntityId = entityId;
          }
          PyramusFileUtils.setLastMovedEntityId(currentEntityId);
        }
        catch (IOException e) {
          logger.log(Level.SEVERE, String.format("Failed to relocate StudentFile %d", currentEntityId), e);
        }
      }
      logger.info(String.format("Moved %d files (%d bytes) with latest entity at %d", totalFiles, totalBytes, currentEntityId));
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR };
  }

}
