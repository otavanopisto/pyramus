package fi.otavanopisto.pyramus.webhooks;

import fi.otavanopisto.pyramus.webhooks.data.WebhookCourseData;

public class WebhookCourseUpdatePayload extends WebhookPayload<WebhookCourseData> {

  public WebhookCourseUpdatePayload(Long courseId) {
    super(WebhookType.COURSE_UPDATE, new WebhookCourseData(courseId));
  }
  
}
