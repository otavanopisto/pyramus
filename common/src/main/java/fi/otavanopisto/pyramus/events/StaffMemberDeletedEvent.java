package fi.otavanopisto.pyramus.events;

public class StaffMemberDeletedEvent {

  public StaffMemberDeletedEvent(Long staffMemberId) {
    super();
    this.staffMemberId = staffMemberId;
  }

  public Long getStaffMemberId() {
    return staffMemberId;
  }

  private Long staffMemberId;
}
