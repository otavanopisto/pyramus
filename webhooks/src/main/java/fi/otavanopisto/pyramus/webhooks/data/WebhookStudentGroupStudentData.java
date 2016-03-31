package fi.otavanopisto.pyramus.webhooks.data;

public class WebhookStudentGroupStudentData {

  public WebhookStudentGroupStudentData() {
    super();
  }

  public WebhookStudentGroupStudentData(Long studentGroupUserId, Long studentGroupId, Long studentId) {
    super();
    this.setStudentGroupUserId(studentGroupUserId);
    this.setStudentGroupId(studentGroupId);
    this.setStudentId(studentId);
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

  public Long getStudentId() {
    return studentId;
  }

  public void setStudentId(Long studentId) {
    this.studentId = studentId;
  }

  private Long studentGroupUserId;
  private Long studentGroupId;
  private Long studentId;
}
