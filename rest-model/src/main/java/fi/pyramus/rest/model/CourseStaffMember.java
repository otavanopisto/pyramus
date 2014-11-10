package fi.pyramus.rest.model;

public class CourseStaffMember {

  public CourseStaffMember() {
    super();
  }

  public CourseStaffMember(Long id, Long courseId, Long staffMemberId, Long roleId) {
    super();
    this.id = id;
    this.courseId = courseId;
    this.staffMemberId = staffMemberId;
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

  public Long getRoleId() {
    return roleId;
  }

  public void setRoleId(Long roleId) {
    this.roleId = roleId;
  }

  public Long getStaffMemberId() {
    return staffMemberId;
  }

  public void setStaffMemberId(Long staffMemberId) {
    this.staffMemberId = staffMemberId;
  }

  private Long id;
  private Long courseId;
  private Long staffMemberId;
  private Long roleId;
}
