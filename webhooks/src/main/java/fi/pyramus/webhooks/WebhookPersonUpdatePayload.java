package fi.pyramus.webhooks;

import fi.pyramus.webhooks.data.WebhookPersonData;

public class WebhookPersonUpdatePayload extends WebhookPayload<WebhookPersonData> {

  public WebhookPersonUpdatePayload(Long personId) {
    super(WebhookType.PERSON_UPDATE, new WebhookPersonData(personId));
  }
  
}
