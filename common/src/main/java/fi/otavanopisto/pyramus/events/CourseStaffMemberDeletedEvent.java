package fi.otavanopisto.pyramus.events;

public class CourseStaffMemberDeletedEvent {

  public CourseStaffMemberDeletedEvent(Long courseStaffMemberId, Long courseId, Long staffMemberId) {
    super();
    this.courseStaffMemberId = courseStaffMemberId;
    this.courseId = courseId;
    this.staffMemberId = staffMemberId;
  }

  public Long getCourseId() {
    return courseId;
  }

  public Long getCourseStaffMemberId() {
    return courseStaffMemberId;
  }

  public Long getStaffMemberId() {
    return staffMemberId;
  }

  private Long courseStaffMemberId;
  private Long courseId;
  private Long staffMemberId;
}
