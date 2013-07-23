package fi.pyramus.rest.tranquil.courses;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.courses.CourseUser.class, entityType = TranquilModelType.COMPLETE)
public class CourseUserComplete extends CourseUserBase {

  public TranquilModelEntity getUser() {
    return user;
  }

  public void setUser(TranquilModelEntity user) {
    this.user = user;
  }

  public TranquilModelEntity getCourse() {
    return course;
  }

  public void setCourse(TranquilModelEntity course) {
    this.course = course;
  }

  public TranquilModelEntity getRole() {
    return role;
  }

  public void setRole(TranquilModelEntity role) {
    this.role = role;
  }

  private TranquilModelEntity user;

  private TranquilModelEntity course;

  private TranquilModelEntity role;

  public final static String[] properties = {"user","course","role"};
}
