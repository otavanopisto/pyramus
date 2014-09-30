package fi.pyramus.rest.model;

public class CourseStaffMember {

  public CourseStaffMember() {
    super();
  }

  public CourseStaffMember(Long id, Long courseId, Long userId, Long roleId) {
    super();
    this.id = id;
    this.courseId = courseId;
    this.userId = userId;
    this.roleId = roleId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getCourseId() {
    return courseId;
  }

  public void setCourseId(Long courseId) {
    this.courseId = courseId;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Long getRoleId() {
    return roleId;
  }

  public void setRoleId(Long roleId) {
    this.roleId = roleId;
  }

  private Long id;
  private Long courseId;
  private Long userId;
  private Long roleId;
}
