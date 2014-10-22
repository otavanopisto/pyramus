package fi.pyramus.webhooks;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fi.pyramus.dao.webhooks.WebhookDAO;
import fi.pyramus.domainmodel.webhooks.Webhook;

@Stateless
@Dependent
public class WebhookController {
  
  @Inject
  private Logger logger;
  
  @Inject
  private WebhookDAO webhookDAO;

  public void sendWebhookNotifications(WebhookPayload<?> payload) {
    List<Webhook> webhooks = webhookDAO.listAll();
    if (!webhooks.isEmpty()) {
      ObjectMapper objectMapper = new ObjectMapper(); 
      try {
        String data = objectMapper.writeValueAsString(payload);
        
        for (Webhook webhook : webhooks) {
          try {
            String signature = DigestUtils.md5Hex(webhook.getSecret());
            
            HttpClient client = new DefaultHttpClient();
            
            HttpPost httpPost = new HttpPost(webhook.getUrl());
            try {
              StringEntity dataEntity = new StringEntity(data);
              try {
                httpPost.addHeader("X-Pyramus-Signature", signature);
                httpPost.setEntity(dataEntity);
                client.execute(httpPost);
              } finally {
                EntityUtils.consume(dataEntity);
              }
            } finally {
              httpPost.releaseConnection();
            }
          } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to send webhook notification to " + webhook.getSecret(), e);
          }
        }
      } catch (JsonProcessingException e) {
        logger.log(Level.SEVERE, "Failed to send webhook notifications", e);
      }
    }
  }
  
}
