package fi.pyramus.webhooks.data;

public class WebhookStudentData {

  public WebhookStudentData() {
    super();
  }

  public WebhookStudentData(Long studentId) {
    super();
    this.studentId = studentId;
  }

  public Long getStudentId() {
    return studentId;
  }

  public void setStudentId(Long studentId) {
    this.studentId = studentId;
  }

  private Long studentId;
}
