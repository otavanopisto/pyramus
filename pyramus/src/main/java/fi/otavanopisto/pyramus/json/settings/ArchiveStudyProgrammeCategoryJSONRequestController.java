package fi.otavanopisto.pyramus.json.settings;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.StudyProgrammeCategoryDAO;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgrammeCategory;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

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
