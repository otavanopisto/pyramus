package fi.otavanopisto.pyramus.webhooks;

import fi.otavanopisto.pyramus.webhooks.data.WebhookStudentData;

public class WebhookStudentUpdatePayload extends WebhookPayload<WebhookStudentData> {

  public WebhookStudentUpdatePayload(Long studentId) {
    super(WebhookType.STUDENT_UPDATE, new WebhookStudentData(studentId));
  }
  
}
