package fi.otavanopisto.pyramus.webhooks;

import fi.otavanopisto.pyramus.webhooks.data.WebhookStudentGroupStaffMemberData;

public class WebhookStudentGroupStaffMemberUpdatePayload extends WebhookPayload<WebhookStudentGroupStaffMemberData> {

  public WebhookStudentGroupStaffMemberUpdatePayload(Long studentGroupUserId, Long studentGroupId, Long staffMemberId) {
    super(WebhookType.STUDENTGROUP_STAFFMEMBER_UPDATE, new WebhookStudentGroupStaffMemberData(studentGroupUserId, studentGroupId, staffMemberId));
  }
  
}
