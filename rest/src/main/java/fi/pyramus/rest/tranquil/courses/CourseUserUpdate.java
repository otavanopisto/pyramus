package fi.pyramus.rest.tranquil.courses;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.courses.CourseUser.class, entityType = TranquilModelType.UPDATE)
public class CourseUserUpdate extends CourseUserComplete {

  public void setUser(UserCompact user) {
    super.setUser(user);
  }

  public UserCompact getUser() {
    return (UserCompact)super.getUser();
  }

  public void setCourse(CourseCompact course) {
    super.setCourse(course);
  }

  public CourseCompact getCourse() {
    return (CourseCompact)super.getCourse();
  }

  public void setRole(CourseUserRoleCompact role) {
    super.setRole(role);
  }

  public CourseUserRoleCompact getRole() {
    return (CourseUserRoleCompact)super.getRole();
  }

  public final static String[] properties = {"user","course","role"};
}
