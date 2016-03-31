package fi.otavanopisto.pyramus.webhooks;

import fi.otavanopisto.pyramus.webhooks.data.WebhookCourseStudentData;

public class WebhookCourseStudentUpdatePayload extends WebhookPayload<WebhookCourseStudentData> {

  public WebhookCourseStudentUpdatePayload(Long courseStudentId, Long courseId, Long studentId) {
    super(WebhookType.COURSE_STUDENT_UPDATE, new WebhookCourseStudentData(courseStudentId, courseId, studentId));
  }
  
}
