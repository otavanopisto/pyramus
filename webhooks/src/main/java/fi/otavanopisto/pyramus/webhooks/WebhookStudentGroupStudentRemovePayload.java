package fi.otavanopisto.pyramus.webhooks;

import fi.otavanopisto.pyramus.webhooks.data.WebhookStudentGroupStudentData;

public class WebhookStudentGroupStudentRemovePayload extends WebhookPayload<WebhookStudentGroupStudentData> {

  public WebhookStudentGroupStudentRemovePayload(Long studentGroupUserId, Long studentGroupId, Long studentId) {
    super(WebhookType.STUDENTGROUP_STUDENT_REMOVE, new WebhookStudentGroupStudentData(studentGroupUserId, studentGroupId, studentId));
  }
  
}
