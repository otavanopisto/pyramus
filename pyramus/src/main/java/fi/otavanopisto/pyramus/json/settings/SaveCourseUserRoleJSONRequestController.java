package fi.otavanopisto.pyramus.json.settings;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.courses.CourseStaffMemberRoleDAO;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStaffMemberRole;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class SaveCourseUserRoleJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext jsonRequestContext) {
	CourseStaffMemberRoleDAO roleDAO = DAOFactory.getInstance().getCourseStaffMemberRoleDAO();

    int rowCount = NumberUtils.createInteger(jsonRequestContext.getRequest().getParameter("courseUserRolesTable.rowCount")).intValue();
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "courseUserRolesTable." + i;
      Long courseUserRoleId = jsonRequestContext.getLong(colPrefix + ".courseUserRoleId");
      String name = jsonRequestContext.getString(colPrefix + ".name");
      
      boolean modified = new Integer(1).equals(jsonRequestContext.getInteger(colPrefix + ".modified"));
      if (courseUserRoleId == -1) {
    	roleDAO.create(name);
      } else if (modified) {
    	CourseStaffMemberRole role = roleDAO.findById(courseUserRoleId);
    	roleDAO.updateName(role, name);
      }
    }
    jsonRequestContext.setRedirectURL(jsonRequestContext.getReferer(true));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
