package fi.pyramus.webhooks;

import fi.pyramus.webhooks.data.WebhookStudentData;

public class WebhookStudentUpdatePayload extends WebhookPayload<WebhookStudentData> {

  public WebhookStudentUpdatePayload(Long studentId) {
    super(WebhookType.STUDENT_UPDATE, new WebhookStudentData(studentId));
  }
  
}
