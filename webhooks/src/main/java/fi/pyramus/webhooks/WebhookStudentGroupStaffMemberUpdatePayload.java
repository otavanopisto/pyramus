package fi.pyramus.webhooks;

import fi.pyramus.webhooks.data.WebhookStudentGroupStaffMemberData;

public class WebhookStudentGroupStaffMemberUpdatePayload extends WebhookPayload<WebhookStudentGroupStaffMemberData> {

  public WebhookStudentGroupStaffMemberUpdatePayload(Long studentGroupUserId, Long studentGroupId, Long staffMemberId) {
    super(WebhookType.STUDENTGROUP_STAFFMEMBER_UPDATE, new WebhookStudentGroupStaffMemberData(studentGroupUserId, studentGroupId, staffMemberId));
  }
  
}
