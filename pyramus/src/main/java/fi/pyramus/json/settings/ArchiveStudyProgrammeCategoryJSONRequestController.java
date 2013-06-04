package fi.pyramus.json.settings;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.StudyProgrammeCategoryDAO;
import fi.pyramus.domainmodel.base.StudyProgrammeCategory;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

public class ArchiveStudyProgrammeCategoryJSONRequestController extends JSONRequestController {
  
  public void process(JSONRequestContext requestContext) {
    StudyProgrammeCategoryDAO studyProgrammeCategoryDAO = DAOFactory.getInstance().getStudyProgrammeCategoryDAO();
    
    Long studyProgrammeCategoryId = requestContext.getLong("studyProgrammeCategory");
    StudyProgrammeCategory studyProgrammeCategory = studyProgrammeCategoryDAO.findById(studyProgrammeCategoryId);
    studyProgrammeCategoryDAO.archive(studyProgrammeCategory);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
