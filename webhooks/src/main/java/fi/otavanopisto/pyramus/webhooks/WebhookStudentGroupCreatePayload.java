package fi.otavanopisto.pyramus.webhooks;

import fi.otavanopisto.pyramus.webhooks.data.WebhookStudentGroupData;

public class WebhookStudentGroupCreatePayload extends WebhookPayload<WebhookStudentGroupData> {

  public WebhookStudentGroupCreatePayload(Long studentGroupId) {
    super(WebhookType.STUDENTGROUP_CREATE, new WebhookStudentGroupData(studentGroupId));
  }
  
}
