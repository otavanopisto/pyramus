package fi.otavanopisto.pyramus.webhooks;

import fi.otavanopisto.pyramus.webhooks.data.WebhookOrganizationData;

public class WebhookOrganizationUpdatePayload extends WebhookPayload<WebhookOrganizationData> {

  public WebhookOrganizationUpdatePayload(Long id, String name) {
    super(WebhookType.ORGANIZATION_UPDATE, new WebhookOrganizationData(id, name));
  }
  
}
