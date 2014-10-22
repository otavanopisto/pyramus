package fi.pyramus.webhooks.data;

public class WebhookCourseData {

  public WebhookCourseData() {
    super();
  }

  public WebhookCourseData(Long courseId) {
    super();
    this.courseId = courseId;
  }

  public void setCourseId(Long courseId) {
    this.courseId = courseId;
  }

  public Long getCourseId() {
    return courseId;
  }

  private Long courseId;
}
