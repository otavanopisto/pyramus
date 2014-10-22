package fi.pyramus.webhooks;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

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
public class WebhookListeners {

  @Inject
  private WebhookController webhookController;
  
  public void onCourseCreated(@Observes CourseCreatedEvent event) {
    webhookController.sendWebHookNotifications(new WebhookCourseCreatePayload(event.getCourseId()));
  }

  public void onCourseArchived(@Observes CourseArchivedEvent event) {
    webhookController.sendWebHookNotifications(new WebhookCourseArchivePayload(event.getCourseId()));
  }
  
  public void onStudentCreated(@Observes StudentCreatedEvent event) {
    webhookController.sendWebHookNotifications(new WebhookStudentCreatePayload(event.getStudentId()));
  }

  public void onStudentArchived(@Observes StudentArchivedEvent event) {
    webhookController.sendWebHookNotifications(new WebhookStudentArchivePayload(event.getStudentId()));
  }
  
  public void onCourseStaffMemberCreated(@Observes CourseStaffMemberCreatedEvent event) {
    webhookController.sendWebHookNotifications(new WebhookCourseStaffMemberCreatePayload(event.getCourseStaffMemberId(), event.getCourseId(), event.getStaffMemberId()));
  }
  
  public void onCourseStaffMemberDeleted(@Observes CourseStaffMemberDeletedEvent event) {
    webhookController.sendWebHookNotifications(new WebhookCourseStaffMemberDeletePayload(event.getCourseStaffMemberId(), event.getCourseId(), event.getStaffMemberId()));
  }
  
  public void onCourseStudentCreated(@Observes CourseStudentCreatedEvent event) {
    webhookController.sendWebHookNotifications(new WebhookCourseStudentCreatePayload(event.getCourseStudentId(), event.getCourseId(), event.getStudentId()));
  }
  
  public void onCourseStudentArchived(@Observes CourseStudentArchivedEvent event) {
    webhookController.sendWebHookNotifications(new WebhookCourseStudentArchivePayload(event.getCourseStudentId(), event.getCourseId(), event.getStudentId()));
  }
  
  public void onStaffMemberCreated(@Observes StaffMemberCreatedEvent event) {
    webhookController.sendWebHookNotifications(new WebhookStaffMemberCreatePayload(event.getStaffMemberId()));
  }
  
  public void onStaffMemberDeleted(@Observes StaffMemberDeletedEvent event) {
    webhookController.sendWebHookNotifications(new WebhookStaffMemberDeletePayload(event.getStaffMemberId()));
  }
  
}
