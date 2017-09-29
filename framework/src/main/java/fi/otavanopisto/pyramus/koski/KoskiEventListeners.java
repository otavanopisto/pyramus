package fi.otavanopisto.pyramus.koski;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.events.CourseAssessmentEvent;
import fi.otavanopisto.pyramus.events.StudentUpdatedEvent;
import fi.otavanopisto.pyramus.events.TransferCreditEvent;

@Stateless
public class KoskiEventListeners {

  @Inject
  private KoskiClient client;
  
  @Inject
  private StudentDAO studentDAO;
  
  @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
  public synchronized void onStudentUpdatedAfterSuccess(@Observes(during=TransactionPhase.AFTER_SUCCESS) StudentUpdatedEvent event) {
    Long studentId = event.getStudentId();
    Student student = studentDAO.findById(studentId);
    client.updateStudent(student);
  }
  
  @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
  public synchronized void onCourseAssessmentEvent(@Observes(during=TransactionPhase.AFTER_SUCCESS) CourseAssessmentEvent event) {
    Long studentId = event.getStudentId();
    Student student = studentDAO.findById(studentId);
    client.updateStudent(student);
  }
  
  @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
  public synchronized void onTransferCreditEvent(@Observes(during=TransactionPhase.AFTER_SUCCESS) TransferCreditEvent event) {
    Long studentId = event.getStudentId();
    Student student = studentDAO.findById(studentId);
    client.updateStudent(student);
  }
  
}
