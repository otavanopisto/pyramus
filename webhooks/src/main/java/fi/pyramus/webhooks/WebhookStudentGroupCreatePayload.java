package fi.pyramus.webhooks;

import fi.pyramus.webhooks.data.WebhookStudentGroupData;

public class WebhookStudentGroupCreatePayload extends WebhookPayload<WebhookStudentGroupData> {

  public WebhookStudentGroupCreatePayload(Long studentGroupId) {
    super(WebhookType.STUDENTGROUP_CREATE, new WebhookStudentGroupData(studentGroupId));
  }
  
}
