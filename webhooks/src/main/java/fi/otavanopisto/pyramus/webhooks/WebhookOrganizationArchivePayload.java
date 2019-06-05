package fi.otavanopisto.pyramus.webhooks;

import fi.otavanopisto.pyramus.webhooks.data.WebhookOrganizationData;

public class WebhookOrganizationArchivePayload extends WebhookPayload<WebhookOrganizationData> {

  public WebhookOrganizationArchivePayload(Long id) {
    super(WebhookType.ORGANIZATION_ARCHIVE, new WebhookOrganizationData(id, null));
  }
  
}
