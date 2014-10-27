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
import fi.pyramus.events.CourseStudentArchivedEvent;
import fi.pyramus.events.CourseStudentCreatedEvent;
import fi.pyramus.events.StaffMemberCreatedEvent;
import fi.pyramus.events.StaffMemberDeletedEvent;
import fi.pyramus.events.StudentArchivedEvent;
import fi.pyramus.events.StudentCreatedEvent;

@ApplicationScoped 
@Stateful
public class Webhooks {

  @Inject
  private WebhookController webhookController;

  @PostConstruct
  public void init() {
    webhooks = new ArrayList<>();
  }
  
  public void addWebhook(String url, String secret) {
    String signature = DigestUtils.md5Hex(secret);
    webhooks.add(new fi.pyramus.webhooks.Webhook(url, signature));
  }
  
  public void onCourseCreated(@Observes(during=TransactionPhase.AFTER_SUCCESS) CourseCreatedEvent event) {
    webhookController.sendWebhookNotifications(webhooks, new WebhookCourseCreatePayload(event.getCourseId()));
  }

  public void onCourseArchived(@Observes(during=TransactionPhase.AFTER_SUCCESS) CourseArchivedEvent event) {
    webhookController.sendWebhookNotifications(webhooks, new WebhookCourseArchivePayload(event.getCourseId()));
  }
  
  public void onStudentCreated(@Observes(during=TransactionPhase.AFTER_SUCCESS) StudentCreatedEvent event) {
    webhookController.sendWebhookNotifications(webhooks, new WebhookStudentCreatePayload(event.getStudentId()));
  }

  public void onStudentArchived(@Observes(during=TransactionPhase.AFTER_SUCCESS) StudentArchivedEvent event) {
    webhookController.sendWebhookNotifications(webhooks, new WebhookStudentArchivePayload(event.getStudentId()));
  }
  
  public void onCourseStaffMemberCreated(@Observes(during=TransactionPhase.AFTER_SUCCESS) CourseStaffMemberCreatedEvent event) {
    webhookController.sendWebhookNotifications(webhooks, new WebhookCourseStaffMemberCreatePayload(event.getCourseStaffMemberId(), event.getCourseId(), event.getStaffMemberId()));
  }
  
  public void onCourseStaffMemberDeleted(@Observes(during=TransactionPhase.AFTER_SUCCESS) CourseStaffMemberDeletedEvent event) {
    webhookController.sendWebhookNotifications(webhooks, new WebhookCourseStaffMemberDeletePayload(event.getCourseStaffMemberId(), event.getCourseId(), event.getStaffMemberId()));
  }
  
  public void onCourseStudentCreated(@Observes(during=TransactionPhase.AFTER_SUCCESS) CourseStudentCreatedEvent event) {
    webhookController.sendWebhookNotifications(webhooks, new WebhookCourseStudentCreatePayload(event.getCourseStudentId(), event.getCourseId(), event.getStudentId()));
  }
  
  public void onCourseStudentArchived(@Observes(during=TransactionPhase.AFTER_SUCCESS) CourseStudentArchivedEvent event) {
    webhookController.sendWebhookNotifications(webhooks, new WebhookCourseStudentArchivePayload(event.getCourseStudentId(), event.getCourseId(), event.getStudentId()));
  }
  
  public void onStaffMemberCreated(@Observes(during=TransactionPhase.AFTER_SUCCESS) StaffMemberCreatedEvent event) {
    webhookController.sendWebhookNotifications(webhooks, new WebhookStaffMemberCreatePayload(event.getStaffMemberId()));
  }
  
  public void onStaffMemberDeleted(@Observes(during=TransactionPhase.AFTER_SUCCESS) StaffMemberDeletedEvent event) {
    webhookController.sendWebhookNotifications(webhooks, new WebhookStaffMemberDeletePayload(event.getStaffMemberId()));
  }
 
  private List<fi.pyramus.webhooks.Webhook> webhooks;
}
