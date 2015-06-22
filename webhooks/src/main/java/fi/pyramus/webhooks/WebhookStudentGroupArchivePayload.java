package fi.pyramus.webhooks;

import fi.pyramus.webhooks.data.WebhookStudentGroupData;

public class WebhookStudentGroupArchivePayload extends WebhookPayload<WebhookStudentGroupData> {

  public WebhookStudentGroupArchivePayload(Long studentGroupId) {
    super(WebhookType.STUDENTGROUP_ARCHIVE, new WebhookStudentGroupData(studentGroupId));
  }
  
}
