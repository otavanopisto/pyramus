package fi.pyramus.json.settings;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.MunicipalityDAO;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

/**
 * The controller responsible of archiving a municipality. 
 */
public class ArchiveMunicipalityJSONRequestController extends JSONRequestController {

  /**
   * Processes the request to archive a municipality.
   * 
   * @param jsonRequestContext The JSON request context
   */
  public void process(JSONRequestContext jsonRequestContext) {
    MunicipalityDAO municipalityDAO = DAOFactory.getInstance().getMunicipalityDAO();
    Long muncipalityId = jsonRequestContext.getLong("municipalityId");
    municipalityDAO.archive(municipalityDAO.findById(muncipalityId));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
