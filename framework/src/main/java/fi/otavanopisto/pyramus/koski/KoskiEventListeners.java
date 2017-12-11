package fi.otavanopisto.pyramus.koski;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Status;
import javax.transaction.TransactionScoped;
import javax.transaction.TransactionSynchronizationRegistry;

import org.apache.commons.collections.CollectionUtils;

import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.events.CourseAssessmentEvent;
import fi.otavanopisto.pyramus.events.StudentUpdatedEvent;
import fi.otavanopisto.pyramus.events.TransferCreditEvent;

@TransactionScoped
public class KoskiEventListeners implements Serializable {

  private static final long serialVersionUID = -8220045004219884113L;

  @Inject
  private Logger logger;
  
  @Resource
  private TransactionSynchronizationRegistry transactionRegistry;

  @Inject
  private KoskiUpdater koskiUpdater;
  
  @Inject
  private StudentDAO studentDAO;
  
  private Set<Long> personIds = new HashSet<>();
  
  @PreDestroy
  private void preDestroy() {
    System.out.println("sList: " + personIds.stream().map(studentId -> studentId != null ? studentId.toString() : "null").collect(Collectors.joining(", ")));
    if (transactionRegistry.getTransactionStatus() == Status.STATUS_COMMITTED) {
      for (Long personId : personIds) {
        koskiUpdater.updatePerson(personId);
      }
    } else {
      if (CollectionUtils.isNotEmpty(personIds)) {
        String list = personIds.stream().map(studentId -> studentId != null ? studentId.toString() : "null").collect(Collectors.joining(", "));
        
        logger.log(Level.WARNING, String.format("Koski was not updated for students %s as transaction was closed with status %d.", list, transactionRegistry.getTransactionStatus()));
      }
    }
  }

  private void addStudentId(Long studentId) {
    Student student = studentDAO.findById(studentId);
    if (student != null && student.getPerson() != null) {
      personIds.add(student.getPerson().getId());
    } else {
      logger.severe(String.format("Student was not found with id %d", studentId));
    }
  }
  
  public void onStudentUpdated(@Observes StudentUpdatedEvent event) {
//    Long studentId = event.getStudentId();
//    studentIds.add(studentId);
    addStudentId(event.getStudentId());
  }

  public void onTransferCreditEvent(@Observes TransferCreditEvent event) {
//    Long studentId = event.getStudentId();
//    studentIds.add(studentId);
    addStudentId(event.getStudentId());
  }
  
  public void onCourseAssessmentEvent(@Observes CourseAssessmentEvent event) {
//    Long studentId = event.getStudentId();
//    studentIds.add(studentId);
    addStudentId(event.getStudentId());
  }
  
}
