package fi.pyramus.events;

public class StaffMemberCreatedEvent {

  public StaffMemberCreatedEvent(Long staffMemberId) {
    super();
    this.staffMemberId = staffMemberId;
  }

  public Long getStaffMemberId() {
    return staffMemberId;
  }

  private Long staffMemberId;
}
