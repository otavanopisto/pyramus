package fi.pyramus.webhooks;

import fi.pyramus.webhooks.data.WebhookStudentGroupStaffMemberData;

public class WebhookStudentGroupStaffMemberCreatePayload extends WebhookPayload<WebhookStudentGroupStaffMemberData> {

  public WebhookStudentGroupStaffMemberCreatePayload(Long studentGroupUserId, Long studentGroupId, Long staffMemberId) {
    super(WebhookType.STUDENTGROUP_STAFFMEMBER_CREATE, new WebhookStudentGroupStaffMemberData(studentGroupUserId, studentGroupId, staffMemberId));
  }
  
}
