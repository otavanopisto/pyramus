package fi.pyramus.dao.webhooks;

import javax.ejb.Stateless;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.webhooks.Webhook;

@Stateless
public class WebhookDAO extends PyramusEntityDAO<Webhook> {
  
  public Webhook create(String url) {
    Webhook webhook = new Webhook();
    webhook.setUrl(url);
    return persist(webhook);
  }
  
  public Webhook create(String url, String secret) {
    Webhook webhook = new Webhook();
    webhook.setUrl(url);
    webhook.setSecret(secret);
    return persist(webhook);
  }
  
  public Webhook updateUrl(Webhook webhook, String url){
    webhook.setUrl(url);
    return persist(webhook);
  }

}
