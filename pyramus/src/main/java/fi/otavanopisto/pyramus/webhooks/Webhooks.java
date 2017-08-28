package fi.otavanopisto.pyramus.webhooks;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Stateful;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.inject.Inject;
import javax.transaction.TransactionSynchronizationRegistry;

import org.apache.commons.codec.digest.DigestUtils;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent;
import fi.otavanopisto.pyramus.events.CourseArchivedEvent;
import fi.otavanopisto.pyramus.events.CourseCreatedEvent;
import fi.otavanopisto.pyramus.events.CourseStaffMemberCreatedEvent;
import fi.otavanopisto.pyramus.events.CourseStaffMemberDeletedEvent;
import fi.otavanopisto.pyramus.events.CourseStaffMemberUpdatedEvent;
import fi.otavanopisto.pyramus.events.CourseStudentArchivedEvent;
import fi.otavanopisto.pyramus.events.CourseStudentCreatedEvent;
import fi.otavanopisto.pyramus.events.CourseStudentUpdatedEvent;
import fi.otavanopisto.pyramus.events.CourseUpdatedEvent;
import fi.otavanopisto.pyramus.events.PersonArchivedEvent;
import fi.otavanopisto.pyramus.events.PersonCreatedEvent;
import fi.otavanopisto.pyramus.events.PersonUpdatedEvent;
import fi.otavanopisto.pyramus.events.StaffMemberCreatedEvent;
import fi.otavanopisto.pyramus.events.StaffMemberDeletedEvent;
import fi.otavanopisto.pyramus.events.StaffMemberUpdatedEvent;
import fi.otavanopisto.pyramus.events.StudentArchivedEvent;
import fi.otavanopisto.pyramus.events.StudentCreatedEvent;
import fi.otavanopisto.pyramus.events.StudentGroupArchivedEvent;
import fi.otavanopisto.pyramus.events.StudentGroupCreatedEvent;
import fi.otavanopisto.pyramus.events.StudentGroupStaffMemberCreatedEvent;
import fi.otavanopisto.pyramus.events.StudentGroupStaffMemberRemovedEvent;
import fi.otavanopisto.pyramus.events.StudentGroupStaffMemberUpdatedEvent;
import fi.otavanopisto.pyramus.events.StudentGroupStudentCreatedEvent;
import fi.otavanopisto.pyramus.events.StudentGroupStudentRemovedEvent;
import fi.otavanopisto.pyramus.events.StudentGroupStudentUpdatedEvent;
import fi.otavanopisto.pyramus.events.StudentGroupUpdatedEvent;
import fi.otavanopisto.pyramus.events.StudentUpdatedEvent;
import fi.otavanopisto.pyramus.webhooks.WebhookCourseArchivePayload;
import fi.otavanopisto.pyramus.webhooks.WebhookCourseCreatePayload;
import fi.otavanopisto.pyramus.webhooks.WebhookCourseStaffMemberCreatePayload;
import fi.otavanopisto.pyramus.webhooks.WebhookCourseStaffMemberDeletePayload;
import fi.otavanopisto.pyramus.webhooks.WebhookCourseStaffMemberUpdatePayload;
import fi.otavanopisto.pyramus.webhooks.WebhookCourseStudentArchivePayload;
import fi.otavanopisto.pyramus.webhooks.WebhookCourseStudentCreatePayload;
import fi.otavanopisto.pyramus.webhooks.WebhookCourseStudentUpdatePayload;
import fi.otavanopisto.pyramus.webhooks.WebhookCourseUpdatePayload;
import fi.otavanopisto.pyramus.webhooks.WebhookPersonArchivePayload;
import fi.otavanopisto.pyramus.webhooks.WebhookPersonCreatePayload;
import fi.otavanopisto.pyramus.webhooks.WebhookPersonUpdatePayload;
import fi.otavanopisto.pyramus.webhooks.WebhookStaffMemberCreatePayload;
import fi.otavanopisto.pyramus.webhooks.WebhookStaffMemberDeletePayload;
import fi.otavanopisto.pyramus.webhooks.WebhookStaffMemberUpdatePayload;
import fi.otavanopisto.pyramus.webhooks.WebhookStudentArchivePayload;
import fi.otavanopisto.pyramus.webhooks.WebhookStudentCreatePayload;
import fi.otavanopisto.pyramus.webhooks.WebhookStudentGroupArchivePayload;
import fi.otavanopisto.pyramus.webhooks.WebhookStudentGroupCreatePayload;
import fi.otavanopisto.pyramus.webhooks.WebhookStudentGroupStaffMemberCreatePayload;
import fi.otavanopisto.pyramus.webhooks.WebhookStudentGroupStaffMemberRemovePayload;
import fi.otavanopisto.pyramus.webhooks.WebhookStudentGroupStaffMemberUpdatePayload;
import fi.otavanopisto.pyramus.webhooks.WebhookStudentGroupStudentCreatePayload;
import fi.otavanopisto.pyramus.webhooks.WebhookStudentGroupStudentRemovePayload;
import fi.otavanopisto.pyramus.webhooks.WebhookStudentGroupStudentUpdatePayload;
import fi.otavanopisto.pyramus.webhooks.WebhookStudentGroupUpdatePayload;
import fi.otavanopisto.pyramus.webhooks.WebhookStudentUpdatePayload;

@ApplicationScoped 
@Stateful
public class Webhooks {

  @Inject
  private WebhookController webhookController;

  @Inject
  private WebhookDatas datas;
  
  @PostConstruct
  public void init() {
    webhooks = new ArrayList<>();
  }
  
  public void addWebhook(String url, String secret) {
    String signature = DigestUtils.md5Hex(secret);
    webhooks.add(new fi.otavanopisto.pyramus.webhooks.Webhook(url, signature));
  }
  
  /* Courses */
  
  public void onCourseCreated(@Observes(during=TransactionPhase.AFTER_SUCCESS) CourseCreatedEvent event) {
    webhookController.sendWebhookNotifications(webhooks, new WebhookCourseCreatePayload(event.getCourseId()));
  }
  
  public synchronized void onCourseUpdatedBeforeCompletion(@Observes(during=TransactionPhase.BEFORE_COMPLETION) CourseUpdatedEvent event) {
    datas.addUpdatedCourseId(event.getCourseId());
  }

  public synchronized void onCourseUpdatedAfterSuccess(@Observes(during=TransactionPhase.AFTER_SUCCESS) CourseUpdatedEvent event) {
    List<Long> updatedCourseIds = datas.retrieveUpdatedCourseIds();
    
    for (Long updatedCourseId : updatedCourseIds) {
      webhookController.sendWebhookNotifications(webhooks, new WebhookCourseUpdatePayload(updatedCourseId));
    }
  }

  public void onCourseArchived(@Observes(during=TransactionPhase.AFTER_SUCCESS) CourseArchivedEvent event) {
    webhookController.sendWebhookNotifications(webhooks, new WebhookCourseArchivePayload(event.getCourseId()));
  }
  
  /* Person */

  public void onPersonCreated(@Observes(during=TransactionPhase.AFTER_SUCCESS) PersonCreatedEvent event) {
    webhookController.sendWebhookNotifications(webhooks, new WebhookPersonCreatePayload(event.getPersonId()));
  }

  public void onPersonUpdated(@Observes(during=TransactionPhase.AFTER_SUCCESS) PersonUpdatedEvent event) {
    webhookController.sendWebhookNotifications(webhooks, new WebhookPersonUpdatePayload(event.getPersonId()));
  }

  public void onPersonArchived(@Observes(during=TransactionPhase.AFTER_SUCCESS) PersonArchivedEvent event) {
    webhookController.sendWebhookNotifications(webhooks, new WebhookPersonArchivePayload(event.getPersonId()));
  }

  /* Student */
  
  public void onStudentCreated(@Observes(during=TransactionPhase.AFTER_SUCCESS) StudentCreatedEvent event) {
    webhookController.sendWebhookNotifications(webhooks, new WebhookStudentCreatePayload(event.getStudentId()));
  }
  
  public synchronized void onStudentUpdatedBeforeCompletion(@Observes(during=TransactionPhase.BEFORE_COMPLETION) StudentUpdatedEvent event) {
    datas.addUpdatedStudentId(event.getStudentId());
  }

  public synchronized void onStudentUpdatedAfterSuccess(@Observes(during=TransactionPhase.AFTER_SUCCESS) StudentUpdatedEvent event) {
    List<Long> updatedStudentIds = datas.retrieveUpdatedStudentIds();
    
    for (Long updatedStudentId : updatedStudentIds) {
      webhookController.sendWebhookNotifications(webhooks, new WebhookStudentUpdatePayload(updatedStudentId));
    }
  }

  public void onStudentArchived(@Observes(during=TransactionPhase.AFTER_SUCCESS) StudentArchivedEvent event) {
    webhookController.sendWebhookNotifications(webhooks, new WebhookStudentArchivePayload(event.getStudentId()));
  }
  
  /* Course Staff Member */
  
  public void onCourseStaffMemberCreated(@Observes(during=TransactionPhase.AFTER_SUCCESS) CourseStaffMemberCreatedEvent event) {
    webhookController.sendWebhookNotifications(webhooks, new WebhookCourseStaffMemberCreatePayload(event.getCourseStaffMemberId(), event.getCourseId(), event.getStaffMemberId()));
  }
  
  public synchronized void onCourseStaffMemberUpdatedBeforeCompletion(@Observes(during=TransactionPhase.BEFORE_COMPLETION) CourseStaffMemberUpdatedEvent event) {
    datas.addUpdatedCourseStaffMember(event.getCourseStaffMemberId(), event.getCourseId(), event.getStaffMemberId());
  }

  public synchronized void onCourseStaffMemberUpdatedAfterSuccess(@Observes(during=TransactionPhase.AFTER_SUCCESS) CourseStaffMemberUpdatedEvent event) {
    List<Long> updatedCourseStaffMemberIds = datas.retrieveUpdatedCourseStaffMemberIds();
    
    for (Long updatedCourseStaffMemberId : updatedCourseStaffMemberIds) {
      Long courseId = datas.getCourseStaffMemberCourseId(updatedCourseStaffMemberId);
      Long staffMemberId = datas.getCourseStaffMemberStaffMemberId(updatedCourseStaffMemberId);
      webhookController.sendWebhookNotifications(webhooks, new WebhookCourseStaffMemberUpdatePayload(updatedCourseStaffMemberId, courseId, staffMemberId));
    }
    
    datas.clearUpdatedCourseStaffMemberIds();
  }
  
  public void onCourseStaffMemberDeleted(@Observes(during=TransactionPhase.AFTER_SUCCESS) CourseStaffMemberDeletedEvent event) {
    webhookController.sendWebhookNotifications(webhooks, new WebhookCourseStaffMemberDeletePayload(event.getCourseStaffMemberId(), event.getCourseId(), event.getStaffMemberId()));
  }
  
  /* Course Student */
  
  public void onCourseStudentCreated(@Observes(during=TransactionPhase.AFTER_SUCCESS) CourseStudentCreatedEvent event) {
    webhookController.sendWebhookNotifications(webhooks, new WebhookCourseStudentCreatePayload(event.getCourseStudentId(), event.getCourseId(), event.getStudentId()));
  }
  
  public synchronized void onCourseStudentUpdatedBeforeCompletion(@Observes(during=TransactionPhase.BEFORE_COMPLETION) CourseStudentUpdatedEvent event) {
    CourseStudent courseStudent = DAOFactory.getInstance().getCourseStudentDAO().findById(event.getCourseStudentId());
    datas.addUpdatedCourseStudent(event.getCourseStudentId(), event.getCourseId(), courseStudent.getStudent().getId());
  }
  
  public synchronized void onCourseStudentUpdatedAfterSuccess(@Observes(during=TransactionPhase.AFTER_SUCCESS) CourseStudentUpdatedEvent event) {
    List<Long> updatedCourseStudentIds = datas.retrieveUpdatedCourseStudentIds();
    
    for (Long updatedCourseStudentId : updatedCourseStudentIds) {
      Long courseId = datas.getCourseStudentCourseId(updatedCourseStudentId);
      Long studentId = datas.getCourseStudentStudentId(updatedCourseStudentId);
      webhookController.sendWebhookNotifications(webhooks, new WebhookCourseStudentUpdatePayload(updatedCourseStudentId, courseId, studentId));
    }
    
    datas.clearUpdatedCourseStudentIds();
  }
  
  public void onCourseStudentArchived(@Observes(during=TransactionPhase.AFTER_SUCCESS) CourseStudentArchivedEvent event) {
    webhookController.sendWebhookNotifications(webhooks, new WebhookCourseStudentArchivePayload(event.getCourseStudentId(), event.getCourseId(), event.getStudentId()));
  }
  
  /* StaffMember */
  
  public void onStaffMemberCreated(@Observes(during=TransactionPhase.AFTER_SUCCESS) StaffMemberCreatedEvent event) {
    webhookController.sendWebhookNotifications(webhooks, new WebhookStaffMemberCreatePayload(event.getStaffMemberId()));
  }
  
  public synchronized void onStaffMemberUpdatedBeforeCompletion(@Observes(during=TransactionPhase.BEFORE_COMPLETION) StaffMemberUpdatedEvent event) {
    datas.addUpdatedStaffMemberId(event.getStaffMemberId());
  }

  public synchronized void onStaffMemberUpdatedAfterSuccess(@Observes(during=TransactionPhase.AFTER_SUCCESS) StaffMemberUpdatedEvent event) {
    List<Long> updatedStaffMemberIds = datas.retrieveUpdatedStaffMemberIds();
    
    for (Long updatedStaffMemberId : updatedStaffMemberIds) {
      webhookController.sendWebhookNotifications(webhooks, new WebhookStaffMemberUpdatePayload(updatedStaffMemberId));
    }
  }

  public void onStaffMemberDeleted(@Observes(during=TransactionPhase.AFTER_SUCCESS) StaffMemberDeletedEvent event) {
    webhookController.sendWebhookNotifications(webhooks, new WebhookStaffMemberDeletePayload(event.getStaffMemberId()));
  }
 
  /* StudentGroup */

  @Resource
  private TransactionSynchronizationRegistry transactionRegistry;
  
  private Queue<WebhookPayload<?>> queue = new ArrayBlockingQueue<>(10);
  private void flushQueue() {
    while (!queue.isEmpty()) {
      WebhookPayload<?> payload = queue.poll();
      System.out.println("payload: " + payload.getType().toString());
      Object key = transactionRegistry.getTransactionKey();
      System.out.println("Transaction key: " + key);
      webhookController.sendWebhookNotifications(webhooks, payload);
    }
  }

  public void onStudentGroupCreatedAfter(@Observes(during=TransactionPhase.AFTER_SUCCESS) StudentGroupCreatedEvent event) {
    flushQueue();
  }
  
  public void onStudentGroupCreated(@Observes(during=TransactionPhase.IN_PROGRESS) StudentGroupCreatedEvent event) {
    queue.add(new WebhookStudentGroupCreatePayload(event.getStudentGroupId()));
//    webhookController.sendWebhookNotifications(webhooks, new WebhookStudentGroupCreatePayload(event.getStudentGroupId()));
  }

  public void onStudentGroupUpdated(@Observes(during=TransactionPhase.AFTER_SUCCESS) StudentGroupUpdatedEvent event) {
    webhookController.sendWebhookNotifications(webhooks, new WebhookStudentGroupUpdatePayload(event.getStudentGroupId()));
  }

  public void onStudentGroupArchived(@Observes(during=TransactionPhase.AFTER_SUCCESS) StudentGroupArchivedEvent event) {
    webhookController.sendWebhookNotifications(webhooks, new WebhookStudentGroupArchivePayload(event.getStudentGroupId()));
  }

  public void onStudentGroupStaffMemberCreatedAfter(@Observes(during=TransactionPhase.AFTER_SUCCESS) StudentGroupStaffMemberCreatedEvent event) {
    flushQueue();
  }
  
  public void onStudentGroupStaffMemberCreated(@Observes(during=TransactionPhase.IN_PROGRESS) StudentGroupStaffMemberCreatedEvent event) {
    queue.add(new WebhookStudentGroupStaffMemberCreatePayload(
        event.getStudentGroupUserId(), event.getStudentGroupId(), event.getStaffMemberId()));
//    webhookController.sendWebhookNotifications(webhooks, new WebhookStudentGroupStaffMemberCreatePayload(
//        event.getStudentGroupUserId(), event.getStudentGroupId(), event.getStaffMemberId()));
  }

  public void onStudentGroupStaffMemberUpdated(@Observes(during=TransactionPhase.AFTER_SUCCESS) StudentGroupStaffMemberUpdatedEvent event) {
    webhookController.sendWebhookNotifications(webhooks, new WebhookStudentGroupStaffMemberUpdatePayload(
        event.getStudentGroupUserId(), event.getStudentGroupId(), event.getStaffMemberId()));
  }

  public void onStudentGroupStaffMemberRemoved(@Observes(during=TransactionPhase.AFTER_SUCCESS) StudentGroupStaffMemberRemovedEvent event) {
    webhookController.sendWebhookNotifications(webhooks, new WebhookStudentGroupStaffMemberRemovePayload(
        event.getStudentGroupUserId(), event.getStudentGroupId(), event.getStaffMemberId()));
  }

  public void onStudentGroupStudentCreatedAfter(@Observes(during=TransactionPhase.AFTER_SUCCESS) StudentGroupStudentCreatedEvent event) {
    flushQueue();
  }
  
  public void onStudentGroupStudentCreated(@Observes(during=TransactionPhase.IN_PROGRESS) StudentGroupStudentCreatedEvent event) {
    queue.add(new WebhookStudentGroupStudentCreatePayload(
        event.getStudentGroupUserId(), event.getStudentGroupId(), event.getStudentId()));
//    webhookController.sendWebhookNotifications(webhooks, new WebhookStudentGroupStudentCreatePayload(
//        event.getStudentGroupUserId(), event.getStudentGroupId(), event.getStudentId()));
  }

  public void onStudentGroupStudentUpdated(@Observes(during=TransactionPhase.AFTER_SUCCESS) StudentGroupStudentUpdatedEvent event) {
    webhookController.sendWebhookNotifications(webhooks, new WebhookStudentGroupStudentUpdatePayload(
        event.getStudentGroupUserId(), event.getStudentGroupId(), event.getStudentId()));
  }

  public void onStudentGroupStudentRemoved(@Observes(during=TransactionPhase.AFTER_SUCCESS) StudentGroupStudentRemovedEvent event) {
    webhookController.sendWebhookNotifications(webhooks, new WebhookStudentGroupStudentRemovePayload(
        event.getStudentGroupUserId(), event.getStudentGroupId(), event.getStudentId()));
  }

  
  private List<fi.otavanopisto.pyramus.webhooks.Webhook> webhooks;

}
