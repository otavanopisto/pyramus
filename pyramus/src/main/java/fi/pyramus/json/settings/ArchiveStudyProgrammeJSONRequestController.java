package fi.pyramus.json.settings;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.StudyProgrammeDAO;
import fi.pyramus.domainmodel.base.StudyProgramme;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

public class ArchiveStudyProgrammeJSONRequestController extends JSONRequestController {
  
  public void process(JSONRequestContext requestContext) {
    StudyProgrammeDAO studyProgrammeDAO = DAOFactory.getInstance().getStudyProgrammeDAO();

    Long studyProgrammeId = NumberUtils.createLong(requestContext.getRequest().getParameter("studyProgrammeId"));
    StudyProgramme studyProgramme = studyProgrammeDAO.findById(studyProgrammeId);
    studyProgrammeDAO.archive(studyProgramme);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
