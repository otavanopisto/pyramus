package fi.otavanopisto.pyramus.rest.model;

public class CourseStaffMember {

  public CourseStaffMember() {
    super();
  }

  public CourseStaffMember(Long id, Long courseId, Long staffMemberId, CourseStaffMemberRoleEnum role) {
    super();
    this.id = id;
    this.courseId = courseId;
    this.staffMemberId = staffMemberId;
    this.role = role;
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

  public Long getStaffMemberId() {
    return staffMemberId;
  }

  public void setStaffMemberId(Long staffMemberId) {
    this.staffMemberId = staffMemberId;
  }

  public CourseStaffMemberRoleEnum getRole() {
    return role;
  }

  public void setRole(CourseStaffMemberRoleEnum role) {
    this.role = role;
  }

  private Long id;
  private Long courseId;
  private Long staffMemberId;
  private CourseStaffMemberRoleEnum role;
}
