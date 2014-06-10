package fi.pyramus.json.studentfiles;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.file.StudentFileDAO;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

/**
 * The controller responsible of archiving a municipality. 
 */
public class ArchiveStudentFileJSONRequestController extends JSONRequestController {

  /**
   * Processes the request to archive a municipality.
   * 
   * @param jsonRequestContext The JSON request context
   */
  public void process(JSONRequestContext jsonRequestContext) {
    StudentFileDAO studentFileDAO = DAOFactory.getInstance().getStudentFileDAO();
    Long fileId = jsonRequestContext.getLong("fileId");
    studentFileDAO.archive(studentFileDAO.findById(fileId));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
