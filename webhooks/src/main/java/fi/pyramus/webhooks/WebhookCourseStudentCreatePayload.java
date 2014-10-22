package fi.pyramus.webhooks;

import fi.pyramus.webhooks.data.WebhookCourseStudentData;

public class WebhookCourseStudentCreatePayload extends WebhookPayload<WebhookCourseStudentData> {

  public WebhookCourseStudentCreatePayload(Long courseStudentId, Long courseId, Long studentId) {
    super(WebhookType.COURSE_STUDENT_CREATE, new WebhookCourseStudentData(courseStudentId, courseId, studentId));
  }
  
}
