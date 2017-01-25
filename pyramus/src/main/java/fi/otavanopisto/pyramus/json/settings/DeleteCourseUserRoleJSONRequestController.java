package fi.otavanopisto.pyramus.json.settings;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.courses.CourseStaffMemberRoleDAO;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStaffMemberRole;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class DeleteCourseUserRoleJSONRequestController extends JSONRequestController {
  
  public void process(JSONRequestContext requestContext) {
	CourseStaffMemberRoleDAO roleDAO = DAOFactory.getInstance().getCourseStaffMemberRoleDAO();
    Long courseUserRoleId = requestContext.getLong("courseUserRole");
    CourseStaffMemberRole role = roleDAO.findById(courseUserRoleId);
    roleDAO.delete(role);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
