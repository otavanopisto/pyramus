package fi.otavanopisto.pyramus.events;

public class StaffMemberUpdatedEvent {

  public StaffMemberUpdatedEvent(Long staffMemberId) {
    super();
    this.staffMemberId = staffMemberId;
  }

  public Long getStaffMemberId() {
    return staffMemberId;
  }

  private Long staffMemberId;
}
