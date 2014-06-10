package fi.pyramus.json.settings;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.file.FileTypeDAO;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

/**
 * The controller responsible of archiving a municipality. 
 */
public class ArchiveFileTypeJSONRequestController extends JSONRequestController {

  /**
   * Processes the request to archive a municipality.
   * 
   * @param jsonRequestContext The JSON request context
   */
  public void process(JSONRequestContext jsonRequestContext) {
    FileTypeDAO fileTypeDAO = DAOFactory.getInstance().getFileTypeDAO();
    Long fileTypeId = jsonRequestContext.getLong("fileTypeId");
    fileTypeDAO.archive(fileTypeDAO.findById(fileTypeId));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
