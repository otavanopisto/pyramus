package fi.pyramus.webhooks;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.inject.Inject;

import org.apache.commons.codec.digest.DigestUtils;

import fi.pyramus.events.CourseArchivedEvent;
import fi.pyramus.events.CourseCreatedEvent;
import fi.pyramus.events.CourseStaffMemberCreatedEvent;
import fi.pyramus.events.CourseStaffMemberDeletedEvent;
import fi.pyramus.events.CourseStaffMemberUpdatedEvent;
import fi.pyramus.events.CourseStudentArchivedEvent;
import fi.pyramus.events.CourseStudentCreatedEvent;
import fi.pyramus.events.CourseStudentUpdatedEvent;
import fi.pyramus.events.CourseUpdatedEvent;
import fi.pyramus.events.StaffMemberCreatedEvent;
import fi.pyramus.events.StaffMemberDeletedEvent;
import fi.pyramus.events.StaffMemberUpdatedEvent;
import fi.pyramus.events.StudentArchivedEvent;
import fi.pyramus.events.StudentCreatedEvent;
import fi.pyramus.events.StudentUpdatedEvent;

@ApplicationScoped 
@Stateful
public class Webhooks {

  @Inject
  private WebhookController webhookController;

  @Inject
  private WebhookSessionData sessionData;
  
  @PostConstruct
  public void init() {
    webhooks = new ArrayList<>();
  }
  
  public void addWebhook(String url, String secret) {
    String signature = DigestUtils.md5Hex(secret);
    webhooks.add(new fi.pyramus.webhooks.Webhook(url, signature));
  }
  
  /* Courses */
  
  public void onCourseCreated(@Observes(during=TransactionPhase.AFTER_SUCCESS) CourseCreatedEvent event) {
    webhookController.sendWebhookNotifications(webhooks, new WebhookCourseCreatePayload(event.getCourseId()));
  }
  
  public synchronized void onCourseUpdatedBeforeCompletion(@Observes(during=TransactionPhase.BEFORE_COMPLETION) CourseUpdatedEvent event) {
    sessionData.addUpdatedCourseId(event.getCourseId());
  }

  public synchronized void onCourseUpdatedAfterSuccess(@Observes(during=TransactionPhase.AFTER_SUCCESS) CourseUpdatedEvent event) {
    List<Long> updatedCourseIds = sessionData.retrieveUpdatedCourseIds();
    
    for (Long updatedCourseId : updatedCourseIds) {
      webhookController.sendWebhookNotifications(webhooks, new WebhookCourseUpdatePayload(updatedCourseId));
    }
  }

  public void onCourseArchived(@Observes(during=TransactionPhase.AFTER_SUCCESS) CourseArchivedEvent event) {
    webhookController.sendWebhookNotifications(webhooks, new WebhookCourseArchivePayload(event.getCourseId()));
  }
  
  /* Student */
  
  public void onStudentCreated(@Observes(during=TransactionPhase.AFTER_SUCCESS) StudentCreatedEvent event) {
    webhookController.sendWebhookNotifications(webhooks, new WebhookStudentCreatePayload(event.getStudentId()));
  }
  
  public synchronized void onStudentUpdatedBeforeCompletion(@Observes(during=TransactionPhase.BEFORE_COMPLETION) StudentUpdatedEvent event) {
    sessionData.addUpdatedStudentId(event.getStudentId());
  }

  public synchronized void onStudentUpdatedAfterSuccess(@Observes(during=TransactionPhase.AFTER_SUCCESS) StudentUpdatedEvent event) {
    List<Long> updatedStudentIds = sessionData.retrieveUpdatedStudentIds();
    
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
    sessionData.addUpdatedCourseStaffMember(event.getCourseStaffMemberId(), event.getCourseId(), event.getStaffMemberId());
  }

  public synchronized void onCourseStaffMemberUpdatedAfterSuccess(@Observes(during=TransactionPhase.AFTER_SUCCESS) CourseStaffMemberUpdatedEvent event) {
    List<Long> updatedCourseStaffMemberIds = sessionData.retrieveUpdatedCourseStaffMemberIds();
    
    for (Long updatedCourseStaffMemberId : updatedCourseStaffMemberIds) {
      Long courseId = sessionData.getCourseStaffMemberCourseId(updatedCourseStaffMemberId);
      Long staffMemberId = sessionData.getCourseStaffMemberStaffMemberId(updatedCourseStaffMemberId);
      webhookController.sendWebhookNotifications(webhooks, new WebhookCourseStaffMemberUpdatePayload(updatedCourseStaffMemberId, courseId, staffMemberId));
    }
    
    sessionData.clearUpdatedCourseStaffMemberIds();
  }
  
  public void onCourseStaffMemberDeleted(@Observes(during=TransactionPhase.AFTER_SUCCESS) CourseStaffMemberDeletedEvent event) {
    webhookController.sendWebhookNotifications(webhooks, new WebhookCourseStaffMemberDeletePayload(event.getCourseStaffMemberId(), event.getCourseId(), event.getStaffMemberId()));
  }
  
  /* Course Student */
  
  public void onCourseStudentCreated(@Observes(during=TransactionPhase.AFTER_SUCCESS) CourseStudentCreatedEvent event) {
    webhookController.sendWebhookNotifications(webhooks, new WebhookCourseStudentCreatePayload(event.getCourseStudentId(), event.getCourseId(), event.getStudentId()));
  }
  
  public synchronized void onCourseStudentUpdatedBeforeCompletion(@Observes(during=TransactionPhase.BEFORE_COMPLETION) CourseStudentUpdatedEvent event) {
    sessionData.addUpdatedCourseStudent(event.getCourseStudentId(), event.getCourseId(), event.getStudentId());
  }
  public synchronized void onCourseStudentUpdatedAfterSuccess(@Observes(during=TransactionPhase.AFTER_SUCCESS) CourseStudentUpdatedEvent event) {
    List<Long> updatedCourseStudentIds = sessionData.retrieveUpdatedCourseStudentIds();
    
    for (Long updatedCourseStudentId : updatedCourseStudentIds) {
      Long courseId = sessionData.getCourseStudentCourseId(updatedCourseStudentId);
      Long studentId = sessionData.getCourseStudentStudentId(updatedCourseStudentId);
      webhookController.sendWebhookNotifications(webhooks, new WebhookCourseStudentUpdatePayload(updatedCourseStudentId, courseId, studentId));
    }
    
    sessionData.clearUpdatedCourseStudentIds();
  }
  
  public void onCourseStudentArchived(@Observes(during=TransactionPhase.AFTER_SUCCESS) CourseStudentArchivedEvent event) {
    webhookController.sendWebhookNotifications(webhooks, new WebhookCourseStudentArchivePayload(event.getCourseStudentId(), event.getCourseId(), event.getStudentId()));
  }
  
  /* StaffMember */
  
  public void onStaffMemberCreated(@Observes(during=TransactionPhase.AFTER_SUCCESS) StaffMemberCreatedEvent event) {
    webhookController.sendWebhookNotifications(webhooks, new WebhookStaffMemberCreatePayload(event.getStaffMemberId()));
  }
  
  public synchronized void onStaffMemberUpdatedBeforeCompletion(@Observes(during=TransactionPhase.BEFORE_COMPLETION) StaffMemberUpdatedEvent event) {
    sessionData.addUpdatedStaffMemberId(event.getStaffMemberId());
  }

  public synchronized void onStaffMemberUpdatedAfterSuccess(@Observes(during=TransactionPhase.AFTER_SUCCESS) StaffMemberUpdatedEvent event) {
    List<Long> updatedStaffMemberIds = sessionData.retrieveUpdatedStaffMemberIds();
    
    for (Long updatedStaffMemberId : updatedStaffMemberIds) {
      webhookController.sendWebhookNotifications(webhooks, new WebhookStaffMemberUpdatePayload(updatedStaffMemberId));
    }
  }

  public void onStaffMemberDeleted(@Observes(during=TransactionPhase.AFTER_SUCCESS) StaffMemberDeletedEvent event) {
    webhookController.sendWebhookNotifications(webhooks, new WebhookStaffMemberDeletePayload(event.getStaffMemberId()));
  }
 
  private List<fi.pyramus.webhooks.Webhook> webhooks;

}
