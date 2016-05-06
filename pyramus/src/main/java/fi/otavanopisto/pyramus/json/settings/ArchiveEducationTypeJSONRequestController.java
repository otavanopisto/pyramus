package fi.otavanopisto.pyramus.json.settings;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.EducationTypeDAO;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

/**
 * The controller responsible of archiving an education type. 
 */
public class ArchiveEducationTypeJSONRequestController extends JSONRequestController {

  /**
   * Processes the request to archive an education type.
   * 
   * @param jsonRequestContext The JSON request context
   */
  public void process(JSONRequestContext jsonRequestContext) {
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();
    Long educationTypeId = NumberUtils.createLong(jsonRequestContext.getRequest().getParameter("educationTypeId"));
    educationTypeDAO.archive(educationTypeDAO.findById(educationTypeId));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
