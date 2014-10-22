package fi.pyramus.webhooks;

import fi.pyramus.webhooks.data.WebhookStudentData;

public class WebhookStudentArchivePayload extends WebhookPayload<WebhookStudentData> {

  public WebhookStudentArchivePayload(Long studentId) {
    super(WebhookType.STUDENT_ARCHIVE, new WebhookStudentData(studentId));
  }
  
}
