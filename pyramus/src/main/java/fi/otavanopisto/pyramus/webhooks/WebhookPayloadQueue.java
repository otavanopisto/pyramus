package fi.otavanopisto.pyramus.webhooks;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.transaction.Status;
import jakarta.transaction.TransactionScoped;
import jakarta.transaction.TransactionSynchronizationRegistry;

import java.io.Serializable;

/**
 * Transaction-bound queue for webhook payloads. Payloads are delivered when 
 * transaction is closed and the transaction is in COMMITTED state.
 */
@TransactionScoped
public class WebhookPayloadQueue implements Serializable {

  private static final long serialVersionUID = -3465338468296285254L;

  @Resource
  private TransactionSynchronizationRegistry transactionRegistry;

  @Inject
  private Logger log;
  
  @Inject
  private Webhooks webhooks;
  
  @PreDestroy
  private void preDestroy() {
    try {
      if (transactionRegistry.getTransactionStatus() == Status.STATUS_COMMITTED) {
        flushQueue();
      }
      
      payloadQueue.clear();
    } catch (Exception e) {
      log.log(Level.SEVERE, "Unable to query transaction status", e);
    }
  }
  
  private void flushQueue() {
    while (!payloadQueue.isEmpty()) {
      WebhookPayload<?> payload = payloadQueue.poll();
      webhooks.sendWebhookNotification(payload);
    }
  }
  
  public void queuePayload(WebhookPayload<?> payload) {
    payloadQueue.add(payload);
  }
  
  private Queue<WebhookPayload<?>> payloadQueue = new LinkedList<WebhookPayload<?>>();
}
