package fi.otavanopisto.pyramus.webhooks.data;

public class WebhookCourseStaffMemberData {

  public WebhookCourseStaffMemberData() {
    super();
  }

  public WebhookCourseStaffMemberData(Long courseStaffMemberId, Long courseId, Long staffMemberId) {
    super();
    this.courseStaffMemberId = courseStaffMemberId;
    this.courseId = courseId;
    this.staffMemberId = staffMemberId;
  }

  public Long getCourseId() {
    return courseId;
  }

  public void setCourseId(Long courseId) {
    this.courseId = courseId;
  }

  public Long getCourseStaffMemberId() {
    return courseStaffMemberId;
  }

  public void setCourseStaffMemberId(Long courseStaffMemberId) {
    this.courseStaffMemberId = courseStaffMemberId;
  }

  public Long getStaffMemberId() {
    return staffMemberId;
  }

  public void setStaffMemberId(Long staffMemberId) {
    this.staffMemberId = staffMemberId;
  }

  private Long courseStaffMemberId;
  private Long courseId;
  private Long staffMemberId;
}
