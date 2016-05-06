package fi.otavanopisto.pyramus.json.settings;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.MunicipalityDAO;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

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
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
