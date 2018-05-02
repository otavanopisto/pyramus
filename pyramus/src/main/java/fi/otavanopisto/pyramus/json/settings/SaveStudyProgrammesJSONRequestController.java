package fi.otavanopisto.pyramus.json.settings;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.OrganizationDAO;
import fi.otavanopisto.pyramus.dao.base.StudyProgrammeCategoryDAO;
import fi.otavanopisto.pyramus.dao.base.StudyProgrammeDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Organization;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgrammeCategory;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class SaveStudyProgrammesJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext jsonRequestContext) {
    StudyProgrammeDAO studyProgrammeDAO = DAOFactory.getInstance().getStudyProgrammeDAO();
    StudyProgrammeCategoryDAO studyProgrammeCategoryDAO = DAOFactory.getInstance().getStudyProgrammeCategoryDAO();
    OrganizationDAO organizationDAO = DAOFactory.getInstance().getOrganizationDAO();

    int rowCount = NumberUtils.createInteger(jsonRequestContext.getRequest().getParameter("studyProgrammesTable.rowCount")).intValue();
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "studyProgrammesTable." + i;
      Long studyProgrammeId = jsonRequestContext.getLong(colPrefix + ".studyProgrammeId");
      String name = jsonRequestContext.getString(colPrefix + ".name");
      String code = jsonRequestContext.getString(colPrefix + ".code");
      Long categoryId = jsonRequestContext.getLong(colPrefix + ".category");
      Long organizationId = jsonRequestContext.getLong(colPrefix + ".organization");
      
      StudyProgrammeCategory category = null;
      Organization organization = null;
      
      if (categoryId != null) {
        category = studyProgrammeCategoryDAO.findById(categoryId);
      }
      
      if (organizationId != null) {
        organization = organizationDAO.findById(organizationId);
      }
      
      boolean modified = jsonRequestContext.getInteger(colPrefix + ".modified") == 1;
      if (studyProgrammeId == -1) {
        studyProgrammeDAO.create(organization, name, category, code); 
      }
      else if (modified) {
        StudyProgramme studyProgramme = studyProgrammeDAO.findById(studyProgrammeId);
        studyProgrammeDAO.update(studyProgramme, organization, name, category, code);
      }
    }
    jsonRequestContext.setRedirectURL(jsonRequestContext.getReferer(true));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
