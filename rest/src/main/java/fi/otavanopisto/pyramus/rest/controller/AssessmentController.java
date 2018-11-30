package fi.otavanopisto.pyramus.rest.controller;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.dao.grading.CourseAssessmentDAO;
import fi.otavanopisto.pyramus.dao.grading.CourseAssessmentRequestDAO;
import fi.otavanopisto.pyramus.dao.grading.CreditLinkDAO;
import fi.otavanopisto.pyramus.dao.grading.TransferCreditDAO;
import fi.otavanopisto.pyramus.domainmodel.base.CourseOptionality;
import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessmentRequest;
import fi.otavanopisto.pyramus.domainmodel.grading.CreditLink;
import fi.otavanopisto.pyramus.domainmodel.grading.CreditType;
import fi.otavanopisto.pyramus.domainmodel.grading.Grade;
import fi.otavanopisto.pyramus.domainmodel.grading.TransferCredit;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;

@Stateless
@Dependent
public class AssessmentController {

  @Inject
  private CourseAssessmentDAO courseAssessmentDAO;
  
  @Inject
  private CourseAssessmentRequestDAO courseAssessmentRequestDAO;
  
  @Inject
  private CreditLinkDAO creditLinkDAO;
  
  @Inject
  private TransferCreditDAO transferCreditDAO;
  
  public CourseAssessment createCourseAssessment(CourseStudent courseStudent, StaffMember assessingUser, Grade grade, Date date, String verbalAssessment){
    // Create course assessment (reusing archived, if any)...
    CourseAssessment courseAssessment = courseAssessmentDAO.findByCourseStudent(courseStudent);
    if (courseAssessment != null) {
      if (Boolean.TRUE.equals(courseAssessment.getArchived()))
        courseAssessmentDAO.unarchive(courseAssessment);
      courseAssessment = courseAssessmentDAO.update(courseAssessment, assessingUser, grade, date, verbalAssessment);
    }
    else {
      courseAssessment = courseAssessmentDAO.create(courseStudent, assessingUser, grade, date, verbalAssessment);
    }
    // ...and mark respective course assessment requests as handled
    List<CourseAssessmentRequest> courseAssessmentRequests = courseAssessmentRequestDAO.listByCourseStudent(courseStudent);
    for (CourseAssessmentRequest courseAssessmentRequest : courseAssessmentRequests) {
      courseAssessmentRequestDAO.updateHandled(courseAssessmentRequest, Boolean.TRUE);
    }
    return courseAssessment;
  }
  
  public CourseAssessment updateCourseAssessment(CourseAssessment courseAssessment, StaffMember assessingUser, Grade grade, Date assessmentDate, String verbalAssessment){
    // Update course assessment...
    courseAssessment = courseAssessmentDAO.update(courseAssessment, assessingUser, grade, assessmentDate, verbalAssessment);
    // ...and mark respective course assessment requests as handled
    List<CourseAssessmentRequest> courseAssessmentRequests = courseAssessmentRequestDAO.listByCourseStudent(courseAssessment.getCourseStudent());
    for (CourseAssessmentRequest courseAssessmentRequest : courseAssessmentRequests) {
      courseAssessmentRequestDAO.updateHandled(courseAssessmentRequest, Boolean.TRUE);
    }
    return courseAssessment;
  }
  
  public CourseAssessment findCourseAssessmentById(Long id){
    return courseAssessmentDAO.findById(id);
  }

  public CourseAssessment findCourseAssessmentByCourseStudentAndArchived(CourseStudent courseStudent, Boolean archived) {
    return courseAssessmentDAO.findByCourseStudentAndArchived(courseStudent, archived);
  }
  
  public List<CourseAssessment> listByStudent(Student student){
    return courseAssessmentDAO.listByStudent(student);
  }

  public List<CreditLink> listLinkedCreditsByStudent(Student student) {
    return creditLinkDAO.listByStudentAndType(student, CreditType.CourseAssessment);
  }

  public List<CourseAssessment> listByCourseAndStudent(Course course, Student student){
    return courseAssessmentDAO.listByStudentAndCourse(student, course);
  }
  
  public void deleteCourseAssessment(CourseAssessment courseAssessment) {
    courseAssessmentDAO.archive(courseAssessment);
  }
  
  public CourseAssessmentRequest createCourseAssessmentRequest(CourseStudent courseStudent, Date created, String requestText) {
    return courseAssessmentRequestDAO.create(courseStudent, created, requestText);
  }
  
  public CourseAssessmentRequest updateCourseAssessmentRequest(CourseAssessmentRequest courseAssessmentRequest, Date created, String requestText, Boolean archived, Boolean handled) {
    return courseAssessmentRequestDAO.update(courseAssessmentRequest, created, requestText, archived, handled);
  }
  
  public CourseAssessmentRequest findCourseAssessmentRequestById(Long id){
    return courseAssessmentRequestDAO.findById(id);
  }

  public CourseAssessmentRequest findCourseAssessmentRequestByCourseStudent(CourseStudent courseStudent) {
    // TODO Return latest request (as implemented) or enforce one assesssment request per course student?  
    CourseAssessmentRequest assessmentRequest = null;
    List<CourseAssessmentRequest> courseAssessmentRequests = courseAssessmentRequestDAO.listByCourseStudent(courseStudent);
    for (CourseAssessmentRequest courseAssessmentRequest : courseAssessmentRequests) {
      if (assessmentRequest == null || courseAssessmentRequest.getCreated().after(assessmentRequest.getCreated())) {
        assessmentRequest = courseAssessmentRequest;
      }
    }
    return assessmentRequest;
  }
  
  public List<CourseAssessmentRequest> listCourseAssessmentRequestsByCourse(Course course) {
    return courseAssessmentRequestDAO.listByCourse(course);
  }

  public List<CourseAssessmentRequest> listCourseAssessmentRequestsByCourseAndHandled(Course course, Boolean handled) {
    return courseAssessmentRequestDAO.listByCourseAndHandled(course, handled);
  }
  
  public List<CourseAssessmentRequest> listCourseAssessmentRequestsByStudent(Student student) {
    return courseAssessmentRequestDAO.listByStudent(student);
  }
  
  public List<CourseAssessmentRequest> listCourseAssessmentRequestsByCourseAndStudent(Course course, Student student) {
    return courseAssessmentRequestDAO.listByCourseAndStudent(course, student);
  }
  
  public Long countCourseAssessments(Student student, Date timeIntervalStartDate, Date timeIntervalEndDate, Boolean passingGrade) {
    return courseAssessmentDAO.countCourseAssessments(student, timeIntervalStartDate, timeIntervalEndDate, passingGrade);
  }
  
  public void deleteCourseAssessmentRequest(CourseAssessmentRequest courseAssessmentRequest) {
    courseAssessmentRequestDAO.delete(courseAssessmentRequest);
  }
  
  /**
   * Lists student's transfer credits by student, subject, curriculum and optionality.
   * 
   * Method exludes archived transfer credits
   * 
   * @param student student
   * @param subject subject
   * @param curriculum curriculum if null, curriculum is ignored
   * @param optionality optionality if null, optionality is ignored
   * @return list of student's transfer credits
   */
  public List<TransferCredit> listTransferCreditsByStudentAndSubjectAndCurriculumAndOptionality(Student student, Subject subject, Curriculum curriculum, CourseOptionality courseOptionality) {
    return transferCreditDAO.listByStudentAndSubjectAndCurriculumAndOptionality(student, subject, curriculum, courseOptionality);
  }

}
