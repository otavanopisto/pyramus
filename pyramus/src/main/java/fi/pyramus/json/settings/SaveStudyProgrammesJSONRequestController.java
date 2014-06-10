package fi.pyramus.json.settings;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.StudyProgrammeCategoryDAO;
import fi.pyramus.dao.base.StudyProgrammeDAO;
import fi.pyramus.domainmodel.base.StudyProgramme;
import fi.pyramus.domainmodel.base.StudyProgrammeCategory;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

public class SaveStudyProgrammesJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext jsonRequestContext) {
    StudyProgrammeDAO studyProgrammeDAO = DAOFactory.getInstance().getStudyProgrammeDAO();
    StudyProgrammeCategoryDAO studyProgrammeCategoryDAO = DAOFactory.getInstance().getStudyProgrammeCategoryDAO();

    int rowCount = NumberUtils.createInteger(jsonRequestContext.getRequest().getParameter("studyProgrammesTable.rowCount")).intValue();
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "studyProgrammesTable." + i;
      Long studyProgrammeId = jsonRequestContext.getLong(colPrefix + ".studyProgrammeId");
      String name = jsonRequestContext.getString(colPrefix + ".name");
      String code = jsonRequestContext.getString(colPrefix + ".code");
      Long categoryId = jsonRequestContext.getLong(colPrefix + ".category");
      
      StudyProgrammeCategory category = null;
      
      if (categoryId != null) {
        category = studyProgrammeCategoryDAO.findById(categoryId);
      }
      
      boolean modified = jsonRequestContext.getInteger(colPrefix + ".modified") == 1;
      if (studyProgrammeId == -1) {
        studyProgrammeDAO.create(name, category, code); 
      }
      else if (modified) {
        StudyProgramme studyProgramme = studyProgrammeDAO.findById(studyProgrammeId);
        studyProgrammeDAO.update(studyProgramme, name, category, code);
      }
    }
    jsonRequestContext.setRedirectURL(jsonRequestContext.getReferer(true));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
