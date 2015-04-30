package fi.pyramus.rest.controller;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.pyramus.dao.grading.CourseAssessmentDAO;
import fi.pyramus.domainmodel.courses.Course;
import fi.pyramus.domainmodel.courses.CourseStudent;
import fi.pyramus.domainmodel.grading.CourseAssessment;
import fi.pyramus.domainmodel.grading.Grade;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.domainmodel.users.StaffMember;

@Stateless
@Dependent
public class AssessmentController {

  @Inject
  private CourseAssessmentDAO courseAssessmentDAO;
  
  public CourseAssessment createCourseCourseAssessment(CourseStudent courseStudent, StaffMember assessingUser, Grade grade, Date date, String verbalAssessment){
    CourseAssessment courseAssessment = courseAssessmentDAO.create(courseStudent, assessingUser, grade, date, verbalAssessment);
    return courseAssessment;
  }
  
  public CourseAssessment updateCourseAssessment(CourseAssessment courseAssessment, StaffMember assessingUser, Grade grade, Date assessmentDate, String verbalAssessment){
    return courseAssessmentDAO.update(courseAssessment, assessingUser, grade, assessmentDate, verbalAssessment);
  }
  
  public CourseAssessment findCourseAssessmentById(Long id){
    return courseAssessmentDAO.findById(id);
  }
  
  public List<CourseAssessment> listByCourseAndStudent(Course course, Student student){
    return courseAssessmentDAO.listByStudentAndCourse(student, course);
  }
  
  public void deleteCourseAssessment(CourseAssessment courseAssessment){
    courseAssessmentDAO.delete(courseAssessment);
  }
  
  
}
