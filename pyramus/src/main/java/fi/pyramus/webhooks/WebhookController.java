package fi.pyramus.webhooks;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Stateful
@Dependent
public class WebhookController {

  @Inject
  private Logger logger;
  
  @PostConstruct
  public void init() {
    executorService = Executors.newSingleThreadExecutor();
  }

  public void sendWebhookNotifications(List<Webhook> webhooks, WebhookPayload<?> payload) {
    if (!webhooks.isEmpty()) {
      ObjectMapper objectMapper = new ObjectMapper();
      try {
        String data = objectMapper.writeValueAsString(payload);

        for (Webhook webhook : webhooks) {
          FutureTask<Boolean> futureTask = new FutureTask<>(new NotificationCallable(webhook.getUrl(), webhook.getSignature(), data));
          executorService.execute(futureTask);
        }
      } catch (JsonProcessingException e) {
        logger.log(Level.SEVERE, "Failed to send webhook notifications", e);
      }
    }
  }
  
  private ExecutorService executorService;

  private class NotificationCallable implements Callable<Boolean> {
    
    public NotificationCallable(String url, String signature, String data) {
      this.url = url;
      this.signature = signature;
      this.data = data;
    }
    
    @Override
    public Boolean call() throws Exception {
      try {
        HttpClient client = new DefaultHttpClient();
  
        HttpPost httpPost = new HttpPost(url);
        try {
          StringEntity dataEntity = new StringEntity(data);
          try {
            httpPost.addHeader("X-Pyramus-Signature", signature);
            httpPost.setEntity(dataEntity);
            client.execute(httpPost);
            return true;
          } finally {
            EntityUtils.consume(dataEntity);
          }
        } finally {
          httpPost.releaseConnection();
        }
      } catch (IOException e) {
        logger.log(Level.SEVERE, "Failed to send webhook notification to " + url, e);
      }
      
      return false;
    }
   
    private String url;
    private String signature;
    private String data;
  }
}
