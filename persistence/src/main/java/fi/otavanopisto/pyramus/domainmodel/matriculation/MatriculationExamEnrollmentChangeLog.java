package fi.otavanopisto.pyramus.domainmodel.matriculation;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

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

  @NotNull
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private MatriculationExamEnrollmentChangeLogType changeType;
  
  @Enumerated(EnumType.STRING)
  private MatriculationExamEnrollmentState newState;

}