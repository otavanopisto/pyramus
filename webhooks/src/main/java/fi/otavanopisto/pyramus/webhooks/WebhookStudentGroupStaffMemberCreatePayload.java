package fi.otavanopisto.pyramus.webhooks;

import fi.otavanopisto.pyramus.webhooks.data.WebhookStudentGroupStaffMemberData;

public class WebhookStudentGroupStaffMemberCreatePayload extends WebhookPayload<WebhookStudentGroupStaffMemberData> {

  public WebhookStudentGroupStaffMemberCreatePayload(Long studentGroupUserId, Long studentGroupId, Long staffMemberId) {
    super(WebhookType.STUDENTGROUP_STAFFMEMBER_CREATE, new WebhookStudentGroupStaffMemberData(studentGroupUserId, studentGroupId, staffMemberId));
  }
  
}
