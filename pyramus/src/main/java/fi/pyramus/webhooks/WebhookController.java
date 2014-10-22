package fi.pyramus.webhooks;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fi.pyramus.webhooks.Webhook;

@Stateless
@Dependent
public class WebhookController {

  @Inject
  private Logger logger;

  public void sendWebhookNotifications(List<Webhook> webhooks, WebhookPayload<?> payload) {
    if (!webhooks.isEmpty()) {
      ObjectMapper objectMapper = new ObjectMapper();
      try {
        String data = objectMapper.writeValueAsString(payload);

        for (Webhook webhook : webhooks) {
          try {
            HttpClient client = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost(webhook.getUrl());
            try {
              StringEntity dataEntity = new StringEntity(data);
              try {
                httpPost.addHeader("X-Pyramus-Signature", webhook.getSignature());
                httpPost.setEntity(dataEntity);
                client.execute(httpPost);
              } finally {
                EntityUtils.consume(dataEntity);
              }
            } finally {
              httpPost.releaseConnection();
            }
          } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to send webhook notification to " + webhook.getUrl(), e);
          }
        }
      } catch (JsonProcessingException e) {
        logger.log(Level.SEVERE, "Failed to send webhook notifications", e);
      }
    }
  }

}
