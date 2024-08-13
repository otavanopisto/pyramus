package fi.otavanopisto.pyramus.rest.model.matriculation;

import java.time.OffsetDateTime;

import fi.otavanopisto.pyramus.matriculation.MatriculationExamEnrollmentChangeLogType;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamEnrollmentState;
import fi.otavanopisto.pyramus.rest.model.UserRoleClass;

public class MatriculationExamEnrollmentChangeLog {

  public Long getId() {
    return id;
  }
  
  public void setId(Long id) {
    this.id = id;
  }

  public Long getEnrollmentId() {
    return enrollmentId;
  }

  public void setEnrollmentId(Long enrollmentId) {
    this.enrollmentId = enrollmentId;
  }

  public Long getModifierId() {
    return modifierId;
  }

  public void setModifierId(Long modifierId) {
    this.modifierId = modifierId;
  }

  public OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(OffsetDateTime timestamp) {
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

  public UserRoleClass getModifierRoleClass() {
    return modifierRoleClass;
  }

  public void setModifierRoleClass(UserRoleClass modifierRoleClass) {
    this.modifierRoleClass = modifierRoleClass;
  }

  private Long id;
  private Long enrollmentId;
  private Long modifierId;
  private UserRoleClass modifierRoleClass;
  private OffsetDateTime timestamp;
  private MatriculationExamEnrollmentChangeLogType changeType;
  private MatriculationExamEnrollmentState newState;
}
