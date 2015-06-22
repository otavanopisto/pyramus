package fi.pyramus.webhooks.data;

public class WebhookStudentGroupStaffMemberData {

  public WebhookStudentGroupStaffMemberData() {
    super();
  }

  public WebhookStudentGroupStaffMemberData(Long studentGroupUserId, Long studentGroupId, Long staffMemberId) {
    super();
    this.setStudentGroupUserId(studentGroupUserId);
    this.setStudentGroupId(studentGroupId);
    this.setStaffMemberId(staffMemberId);
  }

  public Long getStudentGroupUserId() {
    return studentGroupUserId;
  }

  public void setStudentGroupUserId(Long studentGroupUserId) {
    this.studentGroupUserId = studentGroupUserId;
  }

  public Long getStudentGroupId() {
    return studentGroupId;
  }

  public void setStudentGroupId(Long studentGroupId) {
    this.studentGroupId = studentGroupId;
  }

  public Long getStaffMemberId() {
    return staffMemberId;
  }

  public void setStaffMemberId(Long staffMemberId) {
    this.staffMemberId = staffMemberId;
  }

  private Long studentGroupUserId;
  private Long studentGroupId;
  private Long staffMemberId;
}
