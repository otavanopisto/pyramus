package fi.otavanopisto.pyramus.webhooks;

import fi.otavanopisto.pyramus.webhooks.data.WebhookPersonData;

public class WebhookPersonCreatePayload extends WebhookPayload<WebhookPersonData> {

  public WebhookPersonCreatePayload(Long personId) {
    super(WebhookType.PERSON_CREATE, new WebhookPersonData(personId));
  }
  
}
