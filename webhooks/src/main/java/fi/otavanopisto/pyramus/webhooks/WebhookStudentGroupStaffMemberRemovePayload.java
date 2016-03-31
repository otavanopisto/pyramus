package fi.otavanopisto.pyramus.webhooks;

import fi.otavanopisto.pyramus.webhooks.data.WebhookStudentGroupStaffMemberData;

public class WebhookStudentGroupStaffMemberRemovePayload extends WebhookPayload<WebhookStudentGroupStaffMemberData> {

  public WebhookStudentGroupStaffMemberRemovePayload(Long studentGroupUserId, Long studentGroupId, Long staffMemberId) {
    super(WebhookType.STUDENTGROUP_STAFFMEMBER_REMOVE, new WebhookStudentGroupStaffMemberData(studentGroupUserId, studentGroupId, staffMemberId));
  }
  
}
