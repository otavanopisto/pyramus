package fi.otavanopisto.pyramus.matriculation;

public enum MatriculationExamEnrollmentChangeLogType {
  /**
   * Enrollment was created
   */
  ENROLLMENT_CREATED,
  
  /**
   * Enrollment was updated. Use this ideally only when the fields have changed.
   * The state may or may not change within the same log entry. I.e. if both
   * the fields and state are changed, only add one ENROLLMENT_UPDATED log entry
   * instead of ENROLLMENT_UPDATED and STATE_CHANGED.
   */
  ENROLLMENT_UPDATED,

  /**
   * Enrollment's state was changed f.ex. study guider approves the enrollment.
   * Use this log type when only the state has changed.
   * 
   * When using this option, fill the newState field for the log entry.
   */
  STATE_CHANGED
}
