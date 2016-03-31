package fi.otavanopisto.pyramus.webhooks;

import fi.otavanopisto.pyramus.webhooks.data.WebhookStudentData;

public class WebhookStudentCreatePayload extends WebhookPayload<WebhookStudentData> {

  public WebhookStudentCreatePayload(Long studentId) {
    super(WebhookType.STUDENT_CREATE, new WebhookStudentData(studentId));
  }
  
}
