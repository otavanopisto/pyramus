package fi.pyramus.webhooks;

import fi.pyramus.webhooks.data.WebhookStudentData;

public class WebhookStudentCreatePayload extends WebhookPayload<WebhookStudentData> {

  public WebhookStudentCreatePayload(Long studentId) {
    super(WebhookType.STUDENT_CREATE, new WebhookStudentData(studentId));
  }
  
}
