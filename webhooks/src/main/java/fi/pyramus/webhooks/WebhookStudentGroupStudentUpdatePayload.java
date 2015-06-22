package fi.pyramus.webhooks;

import fi.pyramus.webhooks.data.WebhookStudentGroupStudentData;

public class WebhookStudentGroupStudentUpdatePayload extends WebhookPayload<WebhookStudentGroupStudentData> {

  public WebhookStudentGroupStudentUpdatePayload(Long studentGroupUserId, Long studentGroupId, Long studentId) {
    super(WebhookType.STUDENTGROUP_STUDENT_UPDATE, new WebhookStudentGroupStudentData(studentGroupUserId, studentGroupId, studentId));
  }
  
}
