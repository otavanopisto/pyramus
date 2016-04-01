package fi.otavanopisto.pyramus.webhooks;

import fi.otavanopisto.pyramus.webhooks.data.WebhookStudentData;

public class WebhookStudentArchivePayload extends WebhookPayload<WebhookStudentData> {

  public WebhookStudentArchivePayload(Long studentId) {
    super(WebhookType.STUDENT_ARCHIVE, new WebhookStudentData(studentId));
  }
  
}
