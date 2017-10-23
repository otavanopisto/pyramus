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
  
  private Set<Long> studentIds = new HashSet<>();
  
  @PreDestroy
  private void preDestroy() {
    if (transactionRegistry.getTransactionStatus() == Status.STATUS_COMMITTED) {
      for (Long studentId : studentIds) {
        koskiUpdater.updateStudent(studentId);
      }
    } else {
      if (CollectionUtils.isNotEmpty(studentIds)) {
        String list = studentIds.stream().map(studentId -> studentId != null ? studentId.toString() : "null").collect(Collectors.joining(", "));
        
        logger.log(Level.WARNING, String.format("Koski was not updated for students %s as transaction was closed with status %d.", list, transactionRegistry.getTransactionStatus()));
      }
    }
  }

  public void onStudentUpdated(@Observes StudentUpdatedEvent event) {
    Long studentId = event.getStudentId();
    studentIds.add(studentId);
  }

  public void onTransferCreditEvent(@Observes TransferCreditEvent event) {
    Long studentId = event.getStudentId();
    studentIds.add(studentId);
  }
  
  public void onCourseAssessmentEvent(@Observes CourseAssessmentEvent event) {
    Long studentId = event.getStudentId();
    studentIds.add(studentId);
  }
  
//  @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
//  public synchronized void onStudentUpdatedAfterSuccess(@Observes(during=TransactionPhase.AFTER_SUCCESS) StudentUpdatedEvent event) {
//    Long studentId = event.getStudentId();
//    Student student = studentDAO.findById(studentId);
//    client.updateStudent(student);
//  }
//  
//  @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
//  public synchronized void onCourseAssessmentEvent(@Observes(during=TransactionPhase.AFTER_SUCCESS) CourseAssessmentEvent event) {
//    Long studentId = event.getStudentId();
//    Student student = studentDAO.findById(studentId);
//    client.updateStudent(student);
//  }
//  
//  @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
//  public synchronized void onTransferCreditEvent(@Observes(during=TransactionPhase.AFTER_SUCCESS) TransferCreditEvent event) {
//    Long studentId = event.getStudentId();
//    Student student = studentDAO.findById(studentId);
//    client.updateStudent(student);
//  }
  
}
