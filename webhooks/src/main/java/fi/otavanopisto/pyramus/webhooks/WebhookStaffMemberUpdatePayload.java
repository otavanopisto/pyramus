package fi.otavanopisto.pyramus.webhooks;

import fi.otavanopisto.pyramus.webhooks.data.WebhookStaffMemberData;

public class WebhookStaffMemberUpdatePayload extends WebhookPayload<WebhookStaffMemberData> {

  public WebhookStaffMemberUpdatePayload(Long staffMemberId) {
    super(WebhookType.STAFF_MEMBER_UPDATE, new WebhookStaffMemberData(staffMemberId));
  }
  
}
