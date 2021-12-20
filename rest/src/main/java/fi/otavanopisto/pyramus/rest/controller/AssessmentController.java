package fi.otavanopisto.pyramus.rest.controller;

import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.dao.grading.CourseAssessmentDAO;
import fi.otavanopisto.pyramus.dao.grading.CourseAssessmentRequestDAO;
import fi.otavanopisto.pyramus.dao.grading.CreditLinkDAO;
import fi.otavanopisto.pyramus.dao.grading.TransferCreditDAO;
import fi.otavanopisto.pyramus.domainmodel.base.CourseEducationSubtype;
import fi.otavanopisto.pyramus.domainmodel.base.CourseEducationType;
import fi.otavanopisto.pyramus.domainmodel.base.CourseOptionality;
import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;
import fi.otavanopisto.pyramus.domainmodel.base.EducationSubtype;
import fi.otavanopisto.pyramus.domainmodel.base.EducationType;
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
    CourseAssessment courseAssessment = courseAssessmentDAO.create(courseStudent, assessingUser, grade, date, verbalAssessment);
    // Mark respective course assessment requests as handled
    List<CourseAssessmentRequest> courseAssessmentRequests = courseAssessmentRequestDAO.listByCourseStudentAndHandledAndArchived(courseStudent, Boolean.FALSE, Boolean.FALSE);
    for (CourseAssessmentRequest courseAssessmentRequest : courseAssessmentRequests) {
      courseAssessmentRequestDAO.updateHandled(courseAssessmentRequest, Boolean.TRUE);
    }
    return courseAssessment;
  }
  
  public CourseAssessment updateCourseAssessment(CourseAssessment courseAssessment, StaffMember assessingUser, Grade grade, Date assessmentDate, String verbalAssessment){
    // Update course assessment...
    courseAssessment = courseAssessmentDAO.update(courseAssessment, assessingUser, grade, assessmentDate, verbalAssessment);
    // ...and mark respective course assessment requests as handled
    List<CourseAssessmentRequest> courseAssessmentRequests = courseAssessmentRequestDAO.listByCourseStudentAndHandledAndArchived(
        courseAssessment.getCourseStudent(), Boolean.FALSE, Boolean.FALSE);
    for (CourseAssessmentRequest courseAssessmentRequest : courseAssessmentRequests) {
      courseAssessmentRequestDAO.updateHandled(courseAssessmentRequest, Boolean.TRUE);
    }
    return courseAssessment;
  }
  
  public CourseAssessment findCourseAssessmentById(Long id){
    return courseAssessmentDAO.findById(id);
  }

  public CourseAssessment findLatestCourseAssessmentByCourseStudentAndArchived(CourseStudent courseStudent, Boolean archived) {
    return courseAssessmentDAO.findLatestByCourseStudentAndArchived(courseStudent, archived);
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

  public boolean isRaisedGrade(CourseAssessment courseAssessment) {
    Course course = courseAssessment.getCourseStudent().getCourse();
    Student student = courseAssessment.getStudent();
    List<CourseAssessment> assessments = listByCourseAndStudent(course, student);
    assessments.sort(Comparator.comparing(CourseAssessment::getDate));
    return assessments.size() > 1 && !Objects.equals(courseAssessment.getId(), assessments.get(0).getId());
  }
  
  
  public void archiveCourseAssessment(CourseAssessment courseAssessment) {
    courseAssessmentDAO.archive(courseAssessment);
  }
  
  public void deleteCourseAssessment(CourseAssessment courseAssessment) {
    courseAssessmentDAO.delete(courseAssessment);
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

  public CourseAssessmentRequest findLatestCourseAssessmentRequestByCourseStudent(CourseStudent courseStudent) {
    return courseAssessmentRequestDAO.findLatestByCourseStudent(courseStudent);
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
  
  /**
   * Lists student's transfer credits by student, subject, curriculum and optionality.
   * 
   * Method exludes archived transfer credits
   * 
   * @param student student
   * @param curriculum curriculum if null, curriculum is ignored
   * @param optionality optionality if null, optionality is ignored
   * @return list of student's transfer credits
   */
  public List<TransferCredit> listTransferCreditsByStudentAndCurriculumAndOptionality(Student student, Curriculum curriculum, CourseOptionality courseOptionality) {
    return transferCreditDAO.listByStudentAndCurriculumAndOptionality(student, curriculum, courseOptionality);
  }

  /**
   * Counts how many courses with passing grade student has with given subject, education type, education subtype and curriculum.
   * 
   * if educationType, educationSubtype or curriculum is defined as null, all courses are accepted
   * 
   * @param student student entity
   * @param subject subject entity
   * @param educationType education type entity
   * @param educationSubtype education subtype entity
   * @param curriculum curriculum entity
   * @return student passing grade count in matching courses
   */
  public int getAcceptedCourseCount(Student student, Subject subject, EducationType educationType, EducationSubtype educationSubtype, Curriculum curriculum) {
    Set<CourseAssessment> acceptedCourses = getAcceptedCourseAssessments(student, subject, educationType, educationSubtype, curriculum);
    Set<CourseAssessment> acceptedLinkedCourses = getAcceptedLinkedCourseAssessments(student, subject, educationType, educationSubtype, curriculum);
    
    Set<String> courseCodes = new HashSet<>();
    
    acceptedCourses.stream()
      .filter(courseAssessment -> courseAssessment.getGrade().getPassingGrade())
      .forEach(courseAssessment -> courseCodes.add(courseCode(courseAssessment.getCourseStudent().getCourse())));
      
    acceptedLinkedCourses.stream()
      .filter(courseAssessment -> courseAssessment.getGrade().getPassingGrade())
      .forEach(courseAssessment -> courseCodes.add(courseCode(courseAssessment.getCourseStudent().getCourse())));
    
    return courseCodes.size();
  }

  /**
   * Counts how many transfer credits with passing grade student has with given curriculum, subject and mandatority
   * 
   * if curriculum or subject is defined as null filters are ignored. 
   * If transferCreditOnlyMandatory is defined as true, only transfer credits with CourseOptionality MANDATORY are accepted
   * 
   * @param student student entity
   * @param subject subject entity
   * @param transferCreditOnlyMandatory 
   * @param curriculum curriculum entity
   * @return student passing grade count in matching transfer credits
   */
  public int getAcceptedTransferCreditCount(Student student, Subject subject, Boolean transferCreditOnlyMandatory, Curriculum curriculum) {
    CourseOptionality transferCreditOptionality = transferCreditOnlyMandatory ? CourseOptionality.MANDATORY : null;

    List<TransferCredit> transferCredits;
    if (subject == null) {
      transferCredits = listTransferCreditsByStudentAndCurriculumAndOptionality(student, curriculum, transferCreditOptionality);
    } else {
      transferCredits = listTransferCreditsByStudentAndSubjectAndCurriculumAndOptionality(student, subject, curriculum, transferCreditOptionality);
    }
    
    Set<String> courseCodes = new HashSet<>();
    
    transferCredits.stream()
      .filter(transferCredit -> transferCredit.getGrade().getPassingGrade())
      .forEach(transferCredit -> courseCodes.add(courseCode(transferCredit)));
    
    // Add course codes from linked credits 
    List<CreditLink> creditLinks = creditLinkDAO.listLinkedTransferCredits(student, subject, curriculum);
    
    creditLinks.stream()
      .map(creditLink -> ((TransferCredit) creditLink.getCredit()))
      .filter(transferCredit -> transferCreditOptionality != null ? transferCredit.getOptionality() == transferCreditOptionality : true)
      .filter(transferCredit -> transferCredit.getGrade().getPassingGrade())
      .forEach(transferCredit -> courseCodes.add(courseCode(transferCredit)));
    

    return courseCodes.size();
  }
  
  /**
   * Returns set of student courses matching subject, educationType, educationSubtype and curriculum.
   * 
   * if educationType, educationSubtype or curriculum is defined as null, filters are ignored
   * 
   * @param student student entity
   * @param subject subject entity
   * @param educationType education type entity
   * @param educationSubtype education subtype entity
   * @param curriculum curriculum entity
   * @return set of matched courses
   */
  private Set<CourseAssessment> getAcceptedCourseAssessments(Student student, Subject subject, EducationType educationType, EducationSubtype educationSubtype, Curriculum curriculum) {
    List<CourseAssessment> courseAssessments = courseAssessmentDAO.listByStudentAndSubjectAndCurriculum(student, subject, curriculum);
    
    return courseAssessments.stream()
      .filter(courseAssessment -> filterCourseStudentByEducationType(educationType, educationSubtype, courseAssessment.getCourseStudent()))
      .distinct()
      .collect(Collectors.toSet());
  }

  private Set<CourseAssessment> getAcceptedLinkedCourseAssessments(Student student, Subject subject, EducationType educationType, EducationSubtype educationSubtype, Curriculum curriculum) {
    List<CreditLink> creditLinks = creditLinkDAO.listLinkedCourseAssessments(student, subject, curriculum);
    
    return creditLinks.stream()
      .map(creditLink -> ((CourseAssessment) creditLink.getCredit()))
      .filter(courseAssessment -> filterCourseStudentByEducationType(educationType, educationSubtype, courseAssessment.getCourseStudent()))
      .distinct()
      .collect(Collectors.toSet());
  }
  
  /**
   * Returns whether course student course has appropriate educationType and educationSubtype
   * 
   * if educationType is null, all education types are accepted, if educationSubtype is null all subtypes are accepted
   * 
   * @param educationType education type or null for all education types
   * @param educationSubtype education subtype or null for all education subtypes
   * @param courseStudent course student
   * @return whether course student course has appropriate educationType and educationSubtype
   */
  public boolean filterCourseStudentByEducationType(EducationType educationType, EducationSubtype educationSubtype, CourseStudent courseStudent) {
    Course course = courseStudent.getCourse();

     if (educationType != null) {
       CourseEducationType courseEducationType = course.getCourseEducationTypes().stream()
         .filter(filterEducationType -> {
           return filterEducationType.getEducationType().getId().equals(educationType.getId());
         })
         .findFirst()
         .orElse(null);
       
       if (courseEducationType == null) {
         return false;
       }
       
       if (educationSubtype != null) {
         Set<Long> courseEducationSubtypes = courseEducationType.getCourseEducationSubtypes().stream().map(CourseEducationSubtype::getEducationSubtype).map(EducationSubtype::getId).collect(Collectors.toSet());
         return courseEducationSubtypes.contains(educationSubtype.getId());
       }
     }

     return true;
  }

  private String courseCode(Course course) {
    StringBuilder str = new StringBuilder();
    if (course.getSubject() != null) {
      str.append(course.getSubject().getCode());
      
      if (course.getCourseNumber() != null) {
        str.append(course.getCourseNumber());
      }
    }
    return str.length() > 0 ? str.toString() : null;
  }

  private String courseCode(TransferCredit transferCredit) {
    StringBuilder str = new StringBuilder();
    if (transferCredit.getSubject() != null) {
      str.append(transferCredit.getSubject().getCode());
      
      if (transferCredit.getCourseNumber() != null) {
        str.append(transferCredit.getCourseNumber());
      }
    }
    return str.length() > 0 ? str.toString() : null;
  }

}
