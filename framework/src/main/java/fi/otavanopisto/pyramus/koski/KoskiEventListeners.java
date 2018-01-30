package fi.otavanopisto.pyramus.koski;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.TransactionScoped;

import fi.otavanopisto.pyramus.dao.koski.KoskiPersonLogDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.koski.KoskiPersonLog;
import fi.otavanopisto.pyramus.domainmodel.koski.KoskiPersonState;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.events.CourseAssessmentEvent;
import fi.otavanopisto.pyramus.events.StudentArchivedEvent;
import fi.otavanopisto.pyramus.events.StudentUpdatedEvent;
import fi.otavanopisto.pyramus.events.TransferCreditEvent;

@TransactionScoped
public class KoskiEventListeners implements Serializable {

  private static final long serialVersionUID = -8220045004219884113L;

  @Inject
  private Logger logger;
  
  @Inject
  private KoskiSettings settings;
  
  @Inject
  private KoskiPersonLogDAO koskiPersonLogDAO;
  
  @Inject
  private StudentDAO studentDAO;
  
  public void onStudentUpdated(@Observes StudentUpdatedEvent event) {
    studentChanged(event.getStudentId());
  }
  
  public void onStudentArchived(@Observes StudentArchivedEvent event) {
    studentChanged(event.getStudentId());
  }

  public void onTransferCreditEvent(@Observes TransferCreditEvent event) {
    studentChanged(event.getStudentId());
  }
  
  public void onCourseAssessmentEvent(@Observes CourseAssessmentEvent event) {
    studentChanged(event.getStudentId());
  }
  
  private void studentChanged(Long studentId) {
    Student student = studentDAO.findById(studentId);
    if (student != null && student.getPerson() != null) {
      clearPersonLog(student.getPerson());
    } else {
      logger.severe(String.format("Student was not found with id %d", studentId));
    }
  }

  private void clearPersonLog(Person person) {
    try {
      if (settings.hasReportedStudents(person)) {
        List<KoskiPersonLog> entries = koskiPersonLogDAO.listByPerson(person);
        entries.forEach(entry -> koskiPersonLogDAO.delete(entry));
        koskiPersonLogDAO.create(person, KoskiPersonState.PENDING, new Date());
      }
    } catch (Exception ex) {
      logger.log(Level.SEVERE, "Couldn't clear person log.", ex);
    }
  }
  
}
