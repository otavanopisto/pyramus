package fi.pyramus.services.entities.courses;

import java.util.Date;

import fi.pyramus.services.entities.students.StudentEntity;

public class CourseStudentEntity {

  public CourseStudentEntity() {
  }
  
  public CourseStudentEntity(Long id, Date enrolmentTime, StudentEntity student, CourseEntity course, CourseParticipationTypeEntity participationType,
      CourseEnrolmentTypeEntity courseEnrolmentType, Boolean lodging, String optionality, Boolean archived) {
    super();
    this.id = id;
    this.enrolmentTime = enrolmentTime;
    this.student = student;
    this.course = course;
    this.participationType = participationType;
    this.courseEnrolmentType = courseEnrolmentType;
    this.lodging = lodging;
    this.optionality = optionality;
    this.archived = archived;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Date getEnrolmentTime() {
    return enrolmentTime;
  }

  public void setEnrolmentTime(Date enrolmentTime) {
    this.enrolmentTime = enrolmentTime;
  }

  public StudentEntity getStudent() {
    return student;
  }

  public void setStudent(StudentEntity student) {
    this.student = student;
  }

  public CourseEntity getCourse() {
    return course;
  }

  public void setCourse(CourseEntity course) {
    this.course = course;
  }

  public CourseParticipationTypeEntity getParticipationType() {
    return participationType;
  }

  public void setParticipationType(CourseParticipationTypeEntity participationType) {
    this.participationType = participationType;
  }

  public CourseEnrolmentTypeEntity getCourseEnrolmentType() {
    return courseEnrolmentType;
  }

  public void setCourseEnrolmentType(CourseEnrolmentTypeEntity courseEnrolmentType) {
    this.courseEnrolmentType = courseEnrolmentType;
  }

  public Boolean getLodging() {
    return lodging;
  }

  public void setLodging(Boolean lodging) {
    this.lodging = lodging;
  }

  public String getOptionality() {
    return optionality;
  }

  public void setOptionality(String optionality) {
    this.optionality = optionality;
  }

  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  private Long id;
  private Date enrolmentTime;
  private StudentEntity student;
  private CourseEntity course;
  private CourseParticipationTypeEntity participationType;
  private CourseEnrolmentTypeEntity courseEnrolmentType;
  private Boolean lodging;
  private String optionality;
  private Boolean archived;
}
