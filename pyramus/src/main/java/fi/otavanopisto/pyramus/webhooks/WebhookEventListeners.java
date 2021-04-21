package fi.otavanopisto.pyramus.webhooks;

import java.util.List;

import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.inject.Inject;

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
import fi.otavanopisto.pyramus.events.OrganizationArchivedEvent;
import fi.otavanopisto.pyramus.events.OrganizationCreatedEvent;
import fi.otavanopisto.pyramus.events.OrganizationUpdatedEvent;
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
import fi.otavanopisto.pyramus.events.StudyProgrammeArchivedEvent;
import fi.otavanopisto.pyramus.events.StudyProgrammeCreatedEvent;
import fi.otavanopisto.pyramus.events.StudyProgrammeUpdatedEvent;

public class WebhookEventListeners {

  @Inject
  private WebhookDatas datas;
  
  @Inject
  private WebhookPayloadQueue webhookPayloadQueue;
  
  @Inject
  private Webhooks webhooks;
  
  /* Courses */
  
  public void onCourseCreated(@Observes(during=TransactionPhase.AFTER_SUCCESS) CourseCreatedEvent event) {
    webhooks.sendWebhookNotification(new WebhookCourseCreatePayload(event.getCourseId()));
  }
  
  public synchronized void onCourseUpdatedBeforeCompletion(@Observes(during=TransactionPhase.BEFORE_COMPLETION) CourseUpdatedEvent event) {
    datas.addUpdatedCourseId(event.getCourseId());
  }

  public synchronized void onCourseUpdatedAfterSuccess(@Observes(during=TransactionPhase.AFTER_SUCCESS) CourseUpdatedEvent event) {
    List<Long> updatedCourseIds = datas.retrieveUpdatedCourseIds();
    
    for (Long updatedCourseId : updatedCourseIds) {
      webhooks.sendWebhookNotification(new WebhookCourseUpdatePayload(updatedCourseId));
    }
  }

  public void onCourseArchived(@Observes(during=TransactionPhase.AFTER_SUCCESS) CourseArchivedEvent event) {
    webhooks.sendWebhookNotification(new WebhookCourseArchivePayload(event.getCourseId()));
  }
  
  /* Person */

  public void onPersonCreated(@Observes(during=TransactionPhase.AFTER_SUCCESS) PersonCreatedEvent event) {
    webhooks.sendWebhookNotification(new WebhookPersonCreatePayload(event.getPersonId()));
  }

  public void onPersonUpdated(@Observes(during=TransactionPhase.AFTER_SUCCESS) PersonUpdatedEvent event) {
    webhooks.sendWebhookNotification(new WebhookPersonUpdatePayload(event.getPersonId()));
  }

  public void onPersonArchived(@Observes(during=TransactionPhase.AFTER_SUCCESS) PersonArchivedEvent event) {
    webhooks.sendWebhookNotification(new WebhookPersonArchivePayload(event.getPersonId()));
  }

  /* Student */
  
  public void onStudentCreated(@Observes(during=TransactionPhase.AFTER_SUCCESS) StudentCreatedEvent event) {
    webhooks.sendWebhookNotification(new WebhookStudentCreatePayload(event.getStudentId()));
  }
  
  public synchronized void onStudentUpdatedBeforeCompletion(@Observes(during=TransactionPhase.BEFORE_COMPLETION) StudentUpdatedEvent event) {
    datas.addUpdatedStudentId(event.getStudentId());
  }

  public synchronized void onStudentUpdatedAfterSuccess(@Observes(during=TransactionPhase.AFTER_SUCCESS) StudentUpdatedEvent event) {
    List<Long> updatedStudentIds = datas.retrieveUpdatedStudentIds();
    
    for (Long updatedStudentId : updatedStudentIds) {
      webhooks.sendWebhookNotification(new WebhookStudentUpdatePayload(updatedStudentId));
    }
  }

  public void onStudentArchived(@Observes(during=TransactionPhase.AFTER_SUCCESS) StudentArchivedEvent event) {
    webhooks.sendWebhookNotification(new WebhookStudentArchivePayload(event.getStudentId()));
  }
  
  /* Organization */

  public void onOrganizationCreated(@Observes(during=TransactionPhase.AFTER_SUCCESS) OrganizationCreatedEvent event) {
    webhooks.sendWebhookNotification(new WebhookOrganizationCreatePayload(event.getId(), event.getName()));
  }

  public void onOrganizationUpdated(@Observes(during=TransactionPhase.AFTER_SUCCESS) OrganizationUpdatedEvent event) {
    webhooks.sendWebhookNotification(new WebhookOrganizationUpdatePayload(event.getId(), event.getName()));
  }

  public void onOrganizationArchived(@Observes(during=TransactionPhase.AFTER_SUCCESS) OrganizationArchivedEvent event) {
    webhooks.sendWebhookNotification(new WebhookOrganizationArchivePayload(event.getId()));
  }
  
  /* Course Staff Member */
  
  public void onCourseStaffMemberCreated(@Observes(during=TransactionPhase.AFTER_SUCCESS) CourseStaffMemberCreatedEvent event) {
    webhooks.sendWebhookNotification(new WebhookCourseStaffMemberCreatePayload(event.getCourseStaffMemberId(), event.getCourseId(), event.getStaffMemberId()));
  }
  
  public synchronized void onCourseStaffMemberUpdatedBeforeCompletion(@Observes(during=TransactionPhase.BEFORE_COMPLETION) CourseStaffMemberUpdatedEvent event) {
    datas.addUpdatedCourseStaffMember(event.getCourseStaffMemberId(), event.getCourseId(), event.getStaffMemberId());
  }

  public synchronized void onCourseStaffMemberUpdatedAfterSuccess(@Observes(during=TransactionPhase.AFTER_SUCCESS) CourseStaffMemberUpdatedEvent event) {
    List<Long> updatedCourseStaffMemberIds = datas.retrieveUpdatedCourseStaffMemberIds();
    
    for (Long updatedCourseStaffMemberId : updatedCourseStaffMemberIds) {
      Long courseId = datas.getCourseStaffMemberCourseId(updatedCourseStaffMemberId);
      Long staffMemberId = datas.getCourseStaffMemberStaffMemberId(updatedCourseStaffMemberId);
      webhooks.sendWebhookNotification(new WebhookCourseStaffMemberUpdatePayload(updatedCourseStaffMemberId, courseId, staffMemberId));
    }
    
    datas.clearUpdatedCourseStaffMemberIds();
  }
  
  public void onCourseStaffMemberDeleted(@Observes(during=TransactionPhase.AFTER_SUCCESS) CourseStaffMemberDeletedEvent event) {
    webhooks.sendWebhookNotification(new WebhookCourseStaffMemberDeletePayload(event.getCourseStaffMemberId(), event.getCourseId(), event.getStaffMemberId()));
  }
  
  /* Course Student */
  
  public void onCourseStudentCreated(@Observes(during=TransactionPhase.AFTER_SUCCESS) CourseStudentCreatedEvent event) {
    webhooks.sendWebhookNotification(new WebhookCourseStudentCreatePayload(event.getCourseStudentId(), event.getCourseId(), event.getStudentId()));
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
      webhooks.sendWebhookNotification(new WebhookCourseStudentUpdatePayload(updatedCourseStudentId, courseId, studentId));
    }
    
    datas.clearUpdatedCourseStudentIds();
  }
  
  public void onCourseStudentArchived(@Observes(during=TransactionPhase.AFTER_SUCCESS) CourseStudentArchivedEvent event) {
    webhooks.sendWebhookNotification(new WebhookCourseStudentArchivePayload(event.getCourseStudentId(), event.getCourseId(), event.getStudentId()));
  }
  
  /* StaffMember */
  
  public void onStaffMemberCreated(@Observes(during=TransactionPhase.AFTER_SUCCESS) StaffMemberCreatedEvent event) {
    webhooks.sendWebhookNotification(new WebhookStaffMemberCreatePayload(event.getStaffMemberId()));
  }
  
  public synchronized void onStaffMemberUpdatedBeforeCompletion(@Observes(during=TransactionPhase.BEFORE_COMPLETION) StaffMemberUpdatedEvent event) {
    datas.addUpdatedStaffMemberId(event.getStaffMemberId());
  }

  public synchronized void onStaffMemberUpdatedAfterSuccess(@Observes(during=TransactionPhase.AFTER_SUCCESS) StaffMemberUpdatedEvent event) {
    List<Long> updatedStaffMemberIds = datas.retrieveUpdatedStaffMemberIds();
    
    for (Long updatedStaffMemberId : updatedStaffMemberIds) {
      webhooks.sendWebhookNotification(new WebhookStaffMemberUpdatePayload(updatedStaffMemberId));
    }
  }

  public void onStaffMemberDeleted(@Observes(during=TransactionPhase.AFTER_SUCCESS) StaffMemberDeletedEvent event) {
    webhooks.sendWebhookNotification(new WebhookStaffMemberDeletePayload(event.getStaffMemberId()));
  }

  /* StudyProgramme */
  
  public void onStudyProgrammeCreated(@Observes StudyProgrammeCreatedEvent event) {
    webhookPayloadQueue.queuePayload(new WebhookStudyProgrammeCreatePayload(event.getStudyProgrammeId()));
  }

  public void onStudyProgrammeUpdated(@Observes StudyProgrammeUpdatedEvent event) {
    webhookPayloadQueue.queuePayload(new WebhookStudyProgrammeUpdatePayload(event.getStudyProgrammeId()));
  }

  public void onStudyProgrammeArchived(@Observes StudyProgrammeArchivedEvent event) {
    webhookPayloadQueue.queuePayload(new WebhookStudyProgrammeArchivePayload(event.getStudyProgrammeId()));
  }

  /* StudentGroup */
  
  public void onStudentGroupCreated(@Observes StudentGroupCreatedEvent event) {
    webhookPayloadQueue.queuePayload(new WebhookStudentGroupCreatePayload(event.getStudentGroupId()));
  }

  public void onStudentGroupUpdated(@Observes StudentGroupUpdatedEvent event) {
    webhookPayloadQueue.queuePayload(new WebhookStudentGroupUpdatePayload(event.getStudentGroupId()));
  }

  public void onStudentGroupArchived(@Observes StudentGroupArchivedEvent event) {
    webhookPayloadQueue.queuePayload(new WebhookStudentGroupArchivePayload(event.getStudentGroupId()));
  }

  /* StudentGroupStaffMember */

  public void onStudentGroupStaffMemberCreated(@Observes StudentGroupStaffMemberCreatedEvent event) {
    webhookPayloadQueue.queuePayload(new WebhookStudentGroupStaffMemberCreatePayload(event.getStudentGroupUserId(), event.getStudentGroupId(), event.getStaffMemberId()));
  }

  public void onStudentGroupStaffMemberUpdated(@Observes StudentGroupStaffMemberUpdatedEvent event) {
    webhookPayloadQueue.queuePayload(new WebhookStudentGroupStaffMemberUpdatePayload(
        event.getStudentGroupUserId(), event.getStudentGroupId(), event.getStaffMemberId()));
  }

  public void onStudentGroupStaffMemberRemoved(@Observes StudentGroupStaffMemberRemovedEvent event) {
    webhookPayloadQueue.queuePayload(new WebhookStudentGroupStaffMemberRemovePayload(
        event.getStudentGroupUserId(), event.getStudentGroupId(), event.getStaffMemberId()));
  }

  /* StudentGroupStudent */

  public void onStudentGroupStudentCreated(@Observes StudentGroupStudentCreatedEvent event) {
    webhookPayloadQueue.queuePayload(new WebhookStudentGroupStudentCreatePayload(event.getStudentGroupUserId(), event.getStudentGroupId(), event.getStudentId()));
  }

  public void onStudentGroupStudentUpdated(@Observes StudentGroupStudentUpdatedEvent event) {
    webhookPayloadQueue.queuePayload(new WebhookStudentGroupStudentUpdatePayload(
        event.getStudentGroupUserId(), event.getStudentGroupId(), event.getStudentId()));
  }

  public void onStudentGroupStudentRemoved(@Observes StudentGroupStudentRemovedEvent event) {
    webhookPayloadQueue.queuePayload(new WebhookStudentGroupStudentRemovePayload(
        event.getStudentGroupUserId(), event.getStudentGroupId(), event.getStudentId()));
  }

}
