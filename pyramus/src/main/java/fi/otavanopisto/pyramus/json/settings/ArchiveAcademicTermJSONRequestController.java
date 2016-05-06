package fi.otavanopisto.pyramus.json.settings;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.AcademicTermDAO;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

/**
 * The controller responsible of archiving a grading scale. 
 */
public class ArchiveAcademicTermJSONRequestController extends JSONRequestController {

  /**
   * Processes the request to create a new grading scale.
   * 
   * @param jsonRequestContext The JSON request context
   */
  public void process(JSONRequestContext jsonRequestContext) {
    AcademicTermDAO academicTermDAO = DAOFactory.getInstance().getAcademicTermDAO();

    Long academicTermId = NumberUtils.createLong(jsonRequestContext.getRequest().getParameter("academicTermId"));
    academicTermDAO.archive(academicTermDAO.findById(academicTermId));
  }
  
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
