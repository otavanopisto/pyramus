package fi.pyramus.dao.courses;

import javax.ejb.Stateless;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.courses.CourseStaffMemberRole;

@Stateless
public class CourseStaffMemberRoleDAO extends PyramusEntityDAO<CourseStaffMemberRole> {

  public CourseStaffMemberRole create(String name) {
    CourseStaffMemberRole courseStaffMemberRole = new CourseStaffMemberRole();
    courseStaffMemberRole.setName(name);
    return persist(courseStaffMemberRole);
  }

  public CourseStaffMemberRole updateName(CourseStaffMemberRole courseStaffMemberRole, String name) {
    courseStaffMemberRole.setName(name);
    return persist(courseStaffMemberRole);
  }

}
