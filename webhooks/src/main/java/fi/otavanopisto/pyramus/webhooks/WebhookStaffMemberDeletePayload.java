package fi.otavanopisto.pyramus.webhooks;

import fi.otavanopisto.pyramus.webhooks.data.WebhookStaffMemberData;

public class WebhookStaffMemberDeletePayload extends WebhookPayload<WebhookStaffMemberData> {

  public WebhookStaffMemberDeletePayload(Long staffMemberId) {
    super(WebhookType.STAFF_MEMBER_DELETE, new WebhookStaffMemberData(staffMemberId));
  }
  
}
