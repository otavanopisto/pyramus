package fi.pyramus.webhooks.data;

public class WebhookStaffMemberData {

  public WebhookStaffMemberData() {
    super();
  }

  public WebhookStaffMemberData(Long staffMemberId) {
    super();
    this.staffMemberId = staffMemberId;
  }

  public Long getStaffMemberId() {
    return staffMemberId;
  }

  public void setStaffMemberId(Long staffMemberId) {
    this.staffMemberId = staffMemberId;
  }

  private Long staffMemberId;
}
