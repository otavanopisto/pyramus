package fi.pyramus.webhooks;

import fi.pyramus.webhooks.data.WebhookStudentGroupStudentData;

public class WebhookStudentGroupStudentCreatePayload extends WebhookPayload<WebhookStudentGroupStudentData> {

  public WebhookStudentGroupStudentCreatePayload(Long studentGroupUserId, Long studentGroupId, Long studentId) {
    super(WebhookType.STUDENTGROUP_STUDENT_CREATE, new WebhookStudentGroupStudentData(studentGroupUserId, studentGroupId, studentId));
  }
  
}
