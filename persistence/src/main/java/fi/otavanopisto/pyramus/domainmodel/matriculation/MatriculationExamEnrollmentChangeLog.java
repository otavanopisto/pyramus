package fi.otavanopisto.pyramus.domainmodel.matriculation;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;

import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamEnrollmentChangeLogType;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamEnrollmentState;

@Entity
public class MatriculationExamEnrollmentChangeLog {

  public Long getId() {
    return id;
  }
  
  public MatriculationExamEnrollment getEnrollment() {
    return enrollment;
  }

  public void setEnrollment(MatriculationExamEnrollment enrollment) {
    this.enrollment = enrollment;
  }

  public User getModifier() {
    return modifier;
  }

  public void setModifier(User modifier) {
    this.modifier = modifier;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }

  public MatriculationExamEnrollmentChangeLogType getChangeType() {
    return changeType;
  }

  public void setChangeType(MatriculationExamEnrollmentChangeLogType changeType) {
    this.changeType = changeType;
  }

  public MatriculationExamEnrollmentState getNewState() {
    return newState;
  }

  public void setNewState(MatriculationExamEnrollmentState newState) {
    this.newState = newState;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
 
  @ManyToOne
  private MatriculationExamEnrollment enrollment;
  
  @ManyToOne
  private User modifier;
  
  @NotNull
  @Column(nullable = false)
  @Temporal(value = TemporalType.TIMESTAMP)
  private Date timestamp;
  
  @Lob
  private String message;

  @NotNull
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private MatriculationExamEnrollmentChangeLogType changeType;
  
  @Enumerated(EnumType.STRING)
  private MatriculationExamEnrollmentState newState;

}