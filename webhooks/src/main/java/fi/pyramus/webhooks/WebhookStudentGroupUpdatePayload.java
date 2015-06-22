package fi.pyramus.webhooks;

import fi.pyramus.webhooks.data.WebhookStudentGroupData;

public class WebhookStudentGroupUpdatePayload extends WebhookPayload<WebhookStudentGroupData> {

  public WebhookStudentGroupUpdatePayload(Long studentGroupId) {
    super(WebhookType.STUDENTGROUP_UPDATE, new WebhookStudentGroupData(studentGroupId));
  }
  
}
