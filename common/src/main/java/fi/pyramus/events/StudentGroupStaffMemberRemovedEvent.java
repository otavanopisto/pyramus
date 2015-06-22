package fi.pyramus.events;

public class StudentGroupStaffMemberRemovedEvent {

  public StudentGroupStaffMemberRemovedEvent(Long studentGroupUserId, Long studentGroupId, Long staffMemberId) {
    super();
    this.studentGroupUserId = studentGroupUserId;
    this.studentGroupId = studentGroupId;
    this.staffMemberId = staffMemberId;
  }

  public Long getStudentGroupUserId() {
    return studentGroupUserId;
  }

  public Long getStudentGroupId() {
    return studentGroupId;
  }

  public Long getStaffMemberId() {
    return staffMemberId;
  }

  private final Long studentGroupUserId;
  private final Long studentGroupId;
  private final Long staffMemberId;
}
