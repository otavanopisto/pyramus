package fi.otavanopisto.pyramus.webhooks.data;

public class WebhookCourseStudentData {

  public WebhookCourseStudentData() {
    super();
  }

  public WebhookCourseStudentData(Long courseStudentId, Long courseId, Long studentId) {
    super();
    this.courseStudentId = courseStudentId;
    this.courseId = courseId;
    this.studentId = studentId;
  }

  public Long getCourseId() {
    return courseId;
  }

  public void setCourseId(Long courseId) {
    this.courseId = courseId;
  }
  
  public Long getCourseStudentId() {
    return courseStudentId;
  }
  
  public void setCourseStudentId(Long courseStudentId) {
    this.courseStudentId = courseStudentId;
  }
  
  public Long getStudentId() {
    return studentId;
  }
  
  public void setStudentId(Long studentId) {
    this.studentId = studentId;
  }

  private Long courseStudentId;
  private Long courseId;
  private Long studentId;
}
