package fi.pyramus.webhooks;

import fi.pyramus.webhooks.data.WebhookStaffMemberData;

public class WebhookStaffMemberCreatePayload extends WebhookPayload<WebhookStaffMemberData> {

  public WebhookStaffMemberCreatePayload(Long staffMemberId) {
    super(WebhookType.STAFF_MEMBER_CREATE, new WebhookStaffMemberData(staffMemberId));
  }
  
}
