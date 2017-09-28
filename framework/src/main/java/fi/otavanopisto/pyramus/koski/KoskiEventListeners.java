package fi.otavanopisto.pyramus.koski;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.events.StudentUpdatedEvent;

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
    
    client.createStudent(student);
  }
  
}
