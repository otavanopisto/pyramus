package fi.otavanopisto.pyramus.webhooks;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.inject.Inject;
import javax.transaction.TransactionScoped;
import javax.transaction.TransactionSynchronizationRegistry;

import fi.otavanopisto.pyramus.events.StudentGroupArchivedEvent;
import fi.otavanopisto.pyramus.events.StudentGroupCreatedEvent;
import fi.otavanopisto.pyramus.events.StudentGroupStaffMemberCreatedEvent;
import fi.otavanopisto.pyramus.events.StudentGroupStaffMemberRemovedEvent;
import fi.otavanopisto.pyramus.events.StudentGroupStaffMemberUpdatedEvent;
import fi.otavanopisto.pyramus.events.StudentGroupStudentCreatedEvent;
import fi.otavanopisto.pyramus.events.StudentGroupStudentRemovedEvent;
import fi.otavanopisto.pyramus.events.StudentGroupStudentUpdatedEvent;
import fi.otavanopisto.pyramus.events.StudentGroupUpdatedEvent;

@TransactionScoped
public class TransactionalWebhooks implements Serializable {

  private static final long serialVersionUID = 7862963427955238387L;

  @Resource
  private TransactionSynchronizationRegistry transactionRegistry;
  
  @Inject
  private Webhooks webhooks;
  
  private String getTransactionKey() {
    Object transactionKey = transactionRegistry.getTransactionKey();
    return transactionKey != null ? transactionKey.toString() : null;
  }
  
  private void addQueue(WebhookPayload<?> item) {
    payloadQueue.add(item);
    System.out.println("TWHAdd: " + this.toString() + " -- size: " + payloadQueue.size());
    System.out.println("Transaction key: " + getTransactionKey());
  }
  
  @PreDestroy
  public void unload() {
    flushQueue();
  }
  
  private void flushQueue() {
    System.out.println("TWHFlush: " + this.toString() + " -- size: " + payloadQueue.size());
    System.out.println("Transaction key: " + getTransactionKey());
    while (!payloadQueue.isEmpty()) {
      WebhookPayload<?> payload = payloadQueue.poll();
      System.out.println("payload: " + payload.getType().toString());
      webhooks.sendWebhookNotification(payload);
    }
  }

  public void onStudentGroupCreated(@Observes(during=TransactionPhase.IN_PROGRESS) StudentGroupCreatedEvent event) {
    addQueue(new WebhookStudentGroupCreatePayload(event.getStudentGroupId()));
  }

  public void onStudentGroupCreatedFlush(@Observes(during=TransactionPhase.AFTER_SUCCESS) StudentGroupCreatedEvent event) {
    flushQueue();
  }
  
  public void onStudentGroupUpdated(@Observes(during=TransactionPhase.AFTER_SUCCESS) StudentGroupUpdatedEvent event) {
    webhooks.sendWebhookNotification(new WebhookStudentGroupUpdatePayload(event.getStudentGroupId()));
  }

  public void onStudentGroupArchived(@Observes(during=TransactionPhase.AFTER_SUCCESS) StudentGroupArchivedEvent event) {
    webhooks.sendWebhookNotification(new WebhookStudentGroupArchivePayload(event.getStudentGroupId()));
  }

  public void onStudentGroupStaffMemberCreated(@Observes(during=TransactionPhase.IN_PROGRESS) StudentGroupStaffMemberCreatedEvent event) {
    addQueue(new WebhookStudentGroupStaffMemberCreatePayload(event.getStudentGroupUserId(), event.getStudentGroupId(), event.getStaffMemberId()));
  }

  public void onStudentGroupStaffMemberCreatedFlush(@Observes(during=TransactionPhase.AFTER_SUCCESS) StudentGroupStaffMemberCreatedEvent event) {
    flushQueue();
  }
  
  public void onStudentGroupStaffMemberUpdated(@Observes(during=TransactionPhase.AFTER_SUCCESS) StudentGroupStaffMemberUpdatedEvent event) {
    webhooks.sendWebhookNotification(new WebhookStudentGroupStaffMemberUpdatePayload(
        event.getStudentGroupUserId(), event.getStudentGroupId(), event.getStaffMemberId()));
  }

  public void onStudentGroupStaffMemberRemoved(@Observes(during=TransactionPhase.AFTER_SUCCESS) StudentGroupStaffMemberRemovedEvent event) {
    webhooks.sendWebhookNotification(new WebhookStudentGroupStaffMemberRemovePayload(
        event.getStudentGroupUserId(), event.getStudentGroupId(), event.getStaffMemberId()));
  }

  public void onStudentGroupStudentCreated(@Observes(during=TransactionPhase.IN_PROGRESS) StudentGroupStudentCreatedEvent event) {
    addQueue(new WebhookStudentGroupStudentCreatePayload(event.getStudentGroupUserId(), event.getStudentGroupId(), event.getStudentId()));
  }

  public void onStudentGroupStudentCreatedAfter(@Observes(during=TransactionPhase.AFTER_SUCCESS) StudentGroupStudentCreatedEvent event) {
    flushQueue();
  }
  
  public void onStudentGroupStudentUpdated(@Observes(during=TransactionPhase.AFTER_SUCCESS) StudentGroupStudentUpdatedEvent event) {
    webhooks.sendWebhookNotification(new WebhookStudentGroupStudentUpdatePayload(
        event.getStudentGroupUserId(), event.getStudentGroupId(), event.getStudentId()));
  }

  public void onStudentGroupStudentRemoved(@Observes(during=TransactionPhase.AFTER_SUCCESS) StudentGroupStudentRemovedEvent event) {
    webhooks.sendWebhookNotification(new WebhookStudentGroupStudentRemovePayload(
        event.getStudentGroupUserId(), event.getStudentGroupId(), event.getStudentId()));
  }

  private Queue<WebhookPayload<?>> payloadQueue = new LinkedList<WebhookPayload<?>>();
}
