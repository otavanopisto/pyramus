package fi.pyramus.webhooks;

import fi.pyramus.webhooks.data.WebhookPersonData;

public class WebhookPersonArchivePayload extends WebhookPayload<WebhookPersonData> {

  public WebhookPersonArchivePayload(Long personId) {
    super(WebhookType.PERSON_ARCHIVE, new WebhookPersonData(personId));
  }
  
}
