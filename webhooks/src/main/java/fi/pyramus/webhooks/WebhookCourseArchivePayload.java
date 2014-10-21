package fi.pyramus.webhooks;

import fi.pyramus.webhooks.data.WebhookCourseData;

public class WebhookCourseArchivePayload extends WebhookPayload<WebhookCourseData> {

  public WebhookCourseArchivePayload(Long courseId) {
    super(WebhookType.COURSE_UPDATE, new WebhookCourseData(courseId));
  }
  
}
