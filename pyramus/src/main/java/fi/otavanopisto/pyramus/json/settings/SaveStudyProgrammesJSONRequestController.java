package fi.otavanopisto.pyramus.json.settings;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.OrganizationDAO;
import fi.otavanopisto.pyramus.dao.base.StudyProgrammeCategoryDAO;
import fi.otavanopisto.pyramus.dao.base.StudyProgrammeDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Organization;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgrammeCategory;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.PyramusStatusCode;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.framework.UserUtils;

public class SaveStudyProgrammesJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext jsonRequestContext) {
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    StudyProgrammeDAO studyProgrammeDAO = DAOFactory.getInstance().getStudyProgrammeDAO();
    StudyProgrammeCategoryDAO studyProgrammeCategoryDAO = DAOFactory.getInstance().getStudyProgrammeCategoryDAO();
    OrganizationDAO organizationDAO = DAOFactory.getInstance().getOrganizationDAO();

    StaffMember loggedUser = staffMemberDAO.findById(jsonRequestContext.getLoggedUserId());
    
    int rowCount = NumberUtils.createInteger(jsonRequestContext.getRequest().getParameter("studyProgrammesTable.rowCount")).intValue();
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "studyProgrammesTable." + i;
      boolean modified = jsonRequestContext.getInteger(colPrefix + ".modified") == 1;
      if (modified) {
        Long studyProgrammeId = jsonRequestContext.getLong(colPrefix + ".studyProgrammeId");
        String name = jsonRequestContext.getString(colPrefix + ".name");
        String code = jsonRequestContext.getString(colPrefix + ".code");
        String officialEducationType = jsonRequestContext.getString(colPrefix + ".officialEducationType");
        Long categoryId = jsonRequestContext.getLong(colPrefix + ".category");
        Long organizationId = jsonRequestContext.getLong(colPrefix + ".organization");
        boolean hasEvaluationFees = StringUtils.equals("1", jsonRequestContext.getString(colPrefix + ".hasEvaluationFees"));
        
        StudyProgrammeCategory category = null;
        Organization organization = null;
        
        if (categoryId != null) {
          category = studyProgrammeCategoryDAO.findById(categoryId);
        }
        
        if (organizationId != null) {
          organization = organizationDAO.findById(organizationId);
        }
  
        if (!UserUtils.canAccessOrganization(loggedUser, organization)) {
          throw new SmvcRuntimeException(PyramusStatusCode.UNAUTHORIZED, "No permission to assign organization to study programme.");
        }
        
        if (studyProgrammeId == -1) {
          studyProgrammeDAO.create(organization, name, category, code, officialEducationType, hasEvaluationFees); 
        }
        else {
          StudyProgramme studyProgramme = studyProgrammeDAO.findById(studyProgrammeId);
          
          if (!UserUtils.canAccessOrganization(loggedUser, studyProgramme.getOrganization())) {
            throw new SmvcRuntimeException(PyramusStatusCode.UNAUTHORIZED, "Can not access study programme from another organization.");
          }
          
          studyProgrammeDAO.update(studyProgramme, organization, name, category, code, officialEducationType, hasEvaluationFees);
        }
      }
    }
    jsonRequestContext.setRedirectURL(jsonRequestContext.getReferer(true));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
