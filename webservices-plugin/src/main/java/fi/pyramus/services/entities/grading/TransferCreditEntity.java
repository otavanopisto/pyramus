package fi.pyramus.services.entities.grading;

import java.util.Date;

import fi.pyramus.services.entities.users.UserEntity;

public class TransferCreditEntity extends CreditEntity {
  
  public TransferCreditEntity() {
    super();
  }

  public TransferCreditEntity(Long id, Long studentId, Date date, GradeEntity grade, String verbalAssessment, UserEntity assessingUser, boolean archived,
      String courseName, Integer courseNumber, Double length, Long lengthUnitId, Long schoolId, Long subjectId, String optionality) {

    super(id, studentId, date, grade, verbalAssessment, assessingUser, archived);
    this.courseName = courseName;
    this.courseNumber = courseNumber;
    this.length = length;
    this.lengthUnitId = lengthUnitId;
    this.schoolId = schoolId;
    this.subjectId = subjectId;
    this.optionality = optionality;
  }

  public String getCourseName() {
    return courseName;
  }

  public void setCourseName(String courseName) {
    this.courseName = courseName;
  }

  public Integer getCourseNumber() {
    return courseNumber;
  }

  public void setCourseNumber(Integer courseNumber) {
    this.courseNumber = courseNumber;
  }

  public Double getLength() {
    return length;
  }

  public void setLength(Double length) {
    this.length = length;
  }

  public Long getLengthUnitId() {
    return lengthUnitId;
  }

  public void setLengthUnitId(Long lengthUnitId) {
    this.lengthUnitId = lengthUnitId;
  }

  public Long getSchoolId() {
    return schoolId;
  }

  public void setSchoolId(Long schoolId) {
    this.schoolId = schoolId;
  }

  public Long getSubjectId() {
    return subjectId;
  }

  public void setSubjectId(Long subjectId) {
    this.subjectId = subjectId;
  }

  public String getOptionality() {
    return optionality;
  }

  public void setOptionality(String optionality) {
    this.optionality = optionality;
  }

  private String courseName;
  private Integer courseNumber;
  private Double length;
  private Long lengthUnitId;
  private Long schoolId;
  private Long subjectId;
  private String optionality;
}
