package fi.pyramus.webhooks;

import fi.pyramus.webhooks.data.WebhookStaffMemberData;

public class WebhookStaffMemberDeletePayload extends WebhookPayload<WebhookStaffMemberData> {

  public WebhookStaffMemberDeletePayload(Long staffMemberId) {
    super(WebhookType.STAFF_MEMBER_DELETE, new WebhookStaffMemberData(staffMemberId));
  }
  
}
