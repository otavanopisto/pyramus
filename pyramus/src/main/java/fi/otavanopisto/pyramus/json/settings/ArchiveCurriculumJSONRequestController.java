package fi.otavanopisto.pyramus.json.settings;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.StatusCode;
import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.CurriculumDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class ArchiveCurriculumJSONRequestController extends JSONRequestController {
  
  public void process(JSONRequestContext requestContext) {
    CurriculumDAO curriculumDAO = DAOFactory.getInstance().getCurriculumDAO();
    Long curriculumId = requestContext.getLong("curriculumId");

    Curriculum curriculum = curriculumId != null ? curriculumDAO.findById(curriculumId) : null;
    
    if (curriculum != null)
      curriculumDAO.archive(curriculum);
    else
      throw new SmvcRuntimeException(StatusCode.UNDEFINED, "Given curriculum was not found.");
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
