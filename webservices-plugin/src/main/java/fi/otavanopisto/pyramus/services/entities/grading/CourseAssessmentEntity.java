package fi.otavanopisto.pyramus.services.entities.grading;

import java.util.Date;

import fi.otavanopisto.pyramus.services.entities.users.UserEntity;

public class CourseAssessmentEntity extends CreditEntity {

  public CourseAssessmentEntity() {
    super();
  }
  
  public CourseAssessmentEntity(Long id, Long studentId, Date date, GradeEntity grade, String verbalAssessment, UserEntity assessingUser, Boolean archived,
      Long courseId, Long courseStudentId) {
    super(id, studentId, date, grade, verbalAssessment, assessingUser, archived);
    this.courseId = courseId;
    this.courseStudentId = courseStudentId;
  }

  public Long getCourseStudentId() {
    return courseStudentId;
  }

  public void setCourseStudentId(Long courseStudentId) {
    this.courseStudentId = courseStudentId;
  }

  public Long getCourseId() {
    return courseId;
  }

  public void setCourseId(Long courseId) {
    this.courseId = courseId;
  }

  private Long courseStudentId;
  private Long courseId;
}
