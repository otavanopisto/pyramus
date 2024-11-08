package fi.otavanopisto.pyramus.koski;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.TransactionScoped;

import org.apache.commons.collections.CollectionUtils;

import fi.otavanopisto.pyramus.dao.courses.CourseDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseStudentDAO;
import fi.otavanopisto.pyramus.dao.grading.CourseAssessmentDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.events.CourseArchivedEvent;
import fi.otavanopisto.pyramus.events.CourseAssessmentEvent;
import fi.otavanopisto.pyramus.events.CourseStudentArchivedEvent;
import fi.otavanopisto.pyramus.events.StudentArchivedEvent;
import fi.otavanopisto.pyramus.events.StudentSubjectGradeEvent;
import fi.otavanopisto.pyramus.events.StudentUpdatedEvent;
import fi.otavanopisto.pyramus.events.TransferCreditEvent;

@TransactionScoped
public class KoskiEventListeners implements Serializable {

  private static final long serialVersionUID = -8220045004219884113L;

  @Inject
  private Logger logger;
  
  @Inject
  private KoskiController koskiController;
  
  @Inject
  private CourseDAO courseDAO;

  @Inject
  private CourseAssessmentDAO courseAssessmentDAO;

  @Inject
  private CourseStudentDAO courseStudentDAO;
  
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
  
  public void onStudentSubjectGradeEvent(@Observes StudentSubjectGradeEvent event) {
    studentChanged(event.getStudentId());
  }
  
  /**
   * Marks student for Koski update if the archived CourseStudent has any
   * unarchived CourseAssessments for the course. Archived CourseAssessments
   * should be handled via onCourseAssessmentEvent.
   * 
   * @param event the event
   */
  public void onCourseStudentArchivedEvent(@Observes CourseStudentArchivedEvent event) {
    CourseStudent courseStudent = courseStudentDAO.findById(event.getCourseStudentId());
    List<CourseAssessment> courseAssessments = courseStudent != null ? courseAssessmentDAO.listByCourseStudentAndArchived(courseStudent, false) : null;
    
    if (courseStudent != null && CollectionUtils.isNotEmpty(courseAssessments)) {
      studentChanged(event.getStudentId());
    }
  }

  /**
   * Marks all students for Koski update that have any CourseAssessments
   * for the archived Course.
   * 
   * @param event the event
   */
  public void onCourseArchivedEvent(@Observes CourseArchivedEvent event) {
    Course course = event.getCourseId() != null ? courseDAO.findById(event.getCourseId()) : null;
    if (course != null) {
      /*
       * List all assessments and do the logic separately - otherwise some other
       * archivable entity might drop the assessment from the list.
       * 
       * Add the student to the update queue when
       * - courseAssessment is unarchived
       * - courseStudent is unarchived
       * 
       * Otherwise, the update should have been done by the other Observer methods
       * already.
       */
      List<CourseAssessment> courseAssessments = courseAssessmentDAO.listByCourseIncludeArchived(course);
      
      courseAssessments.stream()
        .filter(courseAssessment -> Boolean.FALSE.equals(courseAssessment.getArchived()))
        .filter(courseAssessment -> Boolean.FALSE.equals(courseAssessment.getCourseStudent().getArchived()))
        .map(courseAssessment -> courseAssessment.getStudent().getId())
        .forEach(studentId -> studentChanged(studentId));
    }
    else {
      logger.severe(String.format("Course was not found with id %d", event.getCourseId()));
    }
  }
  
  private void studentChanged(Long studentId) {
    Student student = studentDAO.findById(studentId);
    if (student != null) {
      koskiController.markForUpdate(student);
    }
    else {
      logger.severe(String.format("Student was not found with id %d", studentId));
    }
  }

}
