package fi.otavanopisto.pyramus.webhooks;

import fi.otavanopisto.pyramus.webhooks.data.WebhookStaffMemberData;

public class WebhookStaffMemberCreatePayload extends WebhookPayload<WebhookStaffMemberData> {

  public WebhookStaffMemberCreatePayload(Long staffMemberId) {
    super(WebhookType.STAFF_MEMBER_CREATE, new WebhookStaffMemberData(staffMemberId));
  }
  
}
