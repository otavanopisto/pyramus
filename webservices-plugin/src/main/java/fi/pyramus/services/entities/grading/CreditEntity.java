package fi.pyramus.services.entities.grading;

import java.util.Date;

import fi.pyramus.services.entities.users.UserEntity;

public class CreditEntity {
  
  public CreditEntity() {
  }

  public CreditEntity(Long id, Long studentId, Date date, GradeEntity grade, String verbalAssessment, UserEntity assessingUser, Boolean archived) {
    super();
    this.id = id;
    this.studentId = studentId;
    this.date = date;
    this.grade = grade;
    this.verbalAssessment = verbalAssessment;
    this.assessingUser = assessingUser;
    this.archived = archived;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getStudentId() {
    return studentId;
  }

  public void setStudentId(Long studentId) {
    this.studentId = studentId;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public GradeEntity getGrade() {
    return grade;
  }

  public void setGrade(GradeEntity grade) {
    this.grade = grade;
  }

  public String getVerbalAssessment() {
    return verbalAssessment;
  }

  public void setVerbalAssessment(String verbalAssessment) {
    this.verbalAssessment = verbalAssessment;
  }

  public UserEntity getAssessingUser() {
    return assessingUser;
  }

  public void setAssessingUser(UserEntity assessingUser) {
    this.assessingUser = assessingUser;
  }

  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  private Long id;
  private Long studentId;
  private Date date;
  private GradeEntity grade;
  private String verbalAssessment;
  private UserEntity assessingUser;
  private Boolean archived;
}
