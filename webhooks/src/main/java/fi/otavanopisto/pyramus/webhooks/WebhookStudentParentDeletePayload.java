package fi.otavanopisto.pyramus.webhooks;

import fi.otavanopisto.pyramus.webhooks.data.WebhookStudentParentData;

public class WebhookStudentParentDeletePayload extends WebhookPayload<WebhookStudentParentData> {

  public WebhookStudentParentDeletePayload(Long studentParentId) {
    super(WebhookType.STUDENTPARENT_DELETE, new WebhookStudentParentData(studentParentId));
  }
  
}
