package fi.otavanopisto.pyramus.webhooks.data;

public class WebhookStudentParentData {

  public WebhookStudentParentData() {
    super();
  }

  public WebhookStudentParentData(Long studentParentId) {
    super();
    this.setStudentParentId(studentParentId);
  }

  public Long getStudentParentId() {
    return studentParentId;
  }

  public void setStudentParentId(Long studentParentId) {
    this.studentParentId = studentParentId;
  }

  private Long studentParentId;
}
