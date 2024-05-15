package fi.otavanopisto.pyramus.webhooks;

import fi.otavanopisto.pyramus.webhooks.data.WebhookStudentParentData;

public class WebhookStudentParentUpdatePayload extends WebhookPayload<WebhookStudentParentData> {

  public WebhookStudentParentUpdatePayload(Long studentParentId) {
    super(WebhookType.STUDENTPARENT_UPDATE, new WebhookStudentParentData(studentParentId));
  }
  
}
