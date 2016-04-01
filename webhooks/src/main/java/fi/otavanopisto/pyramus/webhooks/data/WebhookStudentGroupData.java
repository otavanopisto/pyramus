package fi.otavanopisto.pyramus.webhooks.data;

public class WebhookStudentGroupData {

  public WebhookStudentGroupData() {
    super();
  }

  public WebhookStudentGroupData(Long studentGroupId) {
    super();
    this.studentGroupId = studentGroupId;
  }

  public Long getStudentGroupId() {
    return studentGroupId;
  }

  public void setStudentGroupId(Long studentGroupId) {
    this.studentGroupId = studentGroupId;
  }

  private Long studentGroupId;
}
