package fi.otavanopisto.pyramus.webhooks;

import fi.otavanopisto.pyramus.webhooks.data.WebhookStudentGroupData;

public class WebhookStudentGroupUpdatePayload extends WebhookPayload<WebhookStudentGroupData> {

  public WebhookStudentGroupUpdatePayload(Long studentGroupId) {
    super(WebhookType.STUDENTGROUP_UPDATE, new WebhookStudentGroupData(studentGroupId));
  }
  
}
