package fi.otavanopisto.pyramus.webhooks;

import fi.otavanopisto.pyramus.webhooks.data.WebhookStudentParentData;

public class WebhookStudentParentCreatePayload extends WebhookPayload<WebhookStudentParentData> {

  public WebhookStudentParentCreatePayload(Long studentParentId) {
    super(WebhookType.STUDENTPARENT_CREATE, new WebhookStudentParentData(studentParentId));
  }
  
}
