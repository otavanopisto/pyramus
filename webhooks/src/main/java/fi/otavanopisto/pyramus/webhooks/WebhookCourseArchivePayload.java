package fi.otavanopisto.pyramus.webhooks;

import fi.otavanopisto.pyramus.webhooks.data.WebhookCourseData;

public class WebhookCourseArchivePayload extends WebhookPayload<WebhookCourseData> {

  public WebhookCourseArchivePayload(Long courseId) {
    super(WebhookType.COURSE_ARCHIVE, new WebhookCourseData(courseId));
  }
  
}
