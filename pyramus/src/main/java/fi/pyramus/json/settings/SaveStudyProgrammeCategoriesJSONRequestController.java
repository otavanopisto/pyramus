package fi.pyramus.json.settings;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.EducationTypeDAO;
import fi.pyramus.dao.base.StudyProgrammeCategoryDAO;
import fi.pyramus.domainmodel.base.EducationType;
import fi.pyramus.domainmodel.base.StudyProgrammeCategory;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

public class SaveStudyProgrammeCategoriesJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext jsonRequestContext) {
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();    
    StudyProgrammeCategoryDAO studyProgrammeCategoryDAO = DAOFactory.getInstance().getStudyProgrammeCategoryDAO();

    int rowCount = NumberUtils.createInteger(jsonRequestContext.getRequest().getParameter("studyProgrammeCategoriesTable.rowCount")).intValue();
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "studyProgrammeCategoriesTable." + i;
      Long studyProgrammeCategoryId = jsonRequestContext.getLong(colPrefix + ".studyProgrammeCategoryId");
      String name = jsonRequestContext.getString(colPrefix + ".name");
      Long educationTypeId = jsonRequestContext.getLong(colPrefix + ".educationTypeId");
      EducationType educationType = null;
      if (educationTypeId != null)
        educationType = educationTypeDAO.findById(educationTypeId);
      boolean modified = jsonRequestContext.getInteger(colPrefix + ".modified") == 1;

      if (studyProgrammeCategoryId == -1) {
        studyProgrammeCategoryDAO.create(name, educationType); 
      }
      else if (modified) {
        StudyProgrammeCategory studyProgrammeCategory = studyProgrammeCategoryDAO.findById(studyProgrammeCategoryId);
        studyProgrammeCategoryDAO.update(studyProgrammeCategory, name, educationType);
      }
    }
    jsonRequestContext.setRedirectURL(jsonRequestContext.getReferer(true));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
