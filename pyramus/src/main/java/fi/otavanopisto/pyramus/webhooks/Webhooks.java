package fi.otavanopisto.pyramus.webhooks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.codec.digest.DigestUtils;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClientBuilder;
//import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ApplicationScoped 
@Stateful
public class Webhooks {

  @Inject
  private Logger logger;
  
  @PostConstruct
  public void init() {
    webhooks = new ArrayList<>();
    executorService = Executors.newSingleThreadExecutor();
  }
  
  public void addWebhook(String url, String secret) {
    String signature = DigestUtils.md5Hex(secret);
    webhooks.add(new fi.otavanopisto.pyramus.webhooks.Webhook(url, signature));
  }
  
  public void sendWebhookNotification(WebhookPayload<?> payload) {
    if (!webhooks.isEmpty()) {
      ObjectMapper objectMapper = new ObjectMapper();
      try {
        String data = objectMapper.writeValueAsString(payload);

        for (Webhook webhook : webhooks) {
          executorService.execute(() -> notifyWebhook(webhook.getUrl(), webhook.getSignature(), data));
        }
      } catch (JsonProcessingException e) {
        logger.log(Level.SEVERE, "Failed to form webhook notification payload JSON.", e);
      }
    }
  }
  
  private Boolean notifyWebhook(String url, String signature, String data) {
//    try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
//      HttpPost httpPost = new HttpPost(url);
//      try {
//        StringEntity dataEntity = new StringEntity(data, "UTF-8");
//        try {
//          httpPost.addHeader("X-Pyramus-Signature", signature);
//          httpPost.setEntity(dataEntity);
//          client.execute(httpPost);
//          return true;
//        } finally {
//          EntityUtils.consume(dataEntity);
//        }
//      } finally {
//        httpPost.releaseConnection();
//      }
//    } catch (IOException e) {
//      logger.log(Level.SEVERE, "Failed to send webhook notification to " + url, e);
//    }
//    
    return false;
  }

  private ExecutorService executorService;
  private List<fi.otavanopisto.pyramus.webhooks.Webhook> webhooks;
}
