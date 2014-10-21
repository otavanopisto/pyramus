package fi.pyramus.webhooks;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import fi.pyramus.events.CourseArchivedEvent;
import fi.pyramus.events.CourseCreatedEvent;
import fi.pyramus.events.CourseUpdatedEvent;

@ApplicationScoped
public class WebhookListeners {

  @Inject
  private WebhookController webhookController;
  
  public void onCourseCreated(@Observes CourseCreatedEvent event) {
    webhookController.sendWebHookNotifications(new WebhookCourseCreatePayload(event.getCourseId()));
  }

  public void onCourseUpdated(@Observes CourseUpdatedEvent event) {
    webhookController.sendWebHookNotifications(new WebhookCourseUpdatePayload(event.getCourseId()));
  }

  public void onCourseArchived(@Observes CourseArchivedEvent event) {
    webhookController.sendWebHookNotifications(new WebhookCourseArchivePayload(event.getCourseId()));
  }
  
}
