package fi.otavanopisto.pyramus.koski;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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

import fi.otavanopisto.pyramus.dao.koski.KoskiPersonLogDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.domainmodel.koski.KoskiPersonLog;
import fi.otavanopisto.pyramus.domainmodel.koski.KoskiPersonState;
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
  private KoskiPersonLogDAO koskiPersonLogDAO;
  
  @Inject
  private StudentDAO studentDAO;
  
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
    clearPersonLog(studentId);
  }

  public void onTransferCreditEvent(@Observes TransferCreditEvent event) {
    Long studentId = event.getStudentId();
    studentIds.add(studentId);
    clearPersonLog(studentId);
  }
  
  public void onCourseAssessmentEvent(@Observes CourseAssessmentEvent event) {
    Long studentId = event.getStudentId();
    studentIds.add(studentId);
    clearPersonLog(studentId);
  }
  
  private void clearPersonLog(Long studentId) {
    try {
      Student student = studentDAO.findById(studentId);
      
      List<KoskiPersonLog> entries = koskiPersonLogDAO.listByPerson(student.getPerson());
      entries.forEach(entry -> koskiPersonLogDAO.delete(entry));
      koskiPersonLogDAO.create(student.getPerson(), KoskiPersonState.PENDING, new Date());
    } catch (Exception ex) {
      logger.log(Level.SEVERE, "Couldn't clear person log.", ex);
    }
  }
  
}
