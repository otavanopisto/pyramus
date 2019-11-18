package fi.otavanopisto.pyramus.webhooks;

import fi.otavanopisto.pyramus.webhooks.data.WebhookOrganizationData;

public class WebhookOrganizationCreatePayload extends WebhookPayload<WebhookOrganizationData> {

  public WebhookOrganizationCreatePayload(Long id, String name) {
    super(WebhookType.ORGANIZATION_CREATE, new WebhookOrganizationData(id, name));
  }
  
}
