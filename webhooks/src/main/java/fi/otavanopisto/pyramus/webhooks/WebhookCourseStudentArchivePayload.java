package fi.otavanopisto.pyramus.webhooks;

import fi.otavanopisto.pyramus.webhooks.data.WebhookCourseStudentData;

public class WebhookCourseStudentArchivePayload extends WebhookPayload<WebhookCourseStudentData> {

  public WebhookCourseStudentArchivePayload(Long courseStudentId, Long courseId, Long studentId) {
    super(WebhookType.COURSE_STUDENT_ARCHIVE, new WebhookCourseStudentData(courseStudentId, courseId, studentId));
  }
  
}
