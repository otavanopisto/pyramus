package fi.pyramus.json.settings;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.SchoolDAO;
import fi.pyramus.domainmodel.base.School;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

public class ArchiveSchoolJSONRequestController extends JSONRequestController {
  
  public void process(JSONRequestContext requestContext) {
    SchoolDAO schoolDAO = DAOFactory.getInstance().getSchoolDAO();
    Long schoolId = NumberUtils.createLong(requestContext.getRequest().getParameter("schoolId"));
    School school = schoolDAO.findById(schoolId);
    schoolDAO.archive(school);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }
  
}
