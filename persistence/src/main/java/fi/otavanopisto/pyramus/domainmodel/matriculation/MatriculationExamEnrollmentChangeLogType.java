package fi.otavanopisto.pyramus.domainmodel.matriculation;

public enum MatriculationExamEnrollmentChangeLogType {
  // Enrollment was created
  ENROLLMENT_CREATED,
  // Enrollment was updated
  ENROLLMENT_UPDATED,
  // Enrollment's state was changed f.ex. study guider approves the enrollment.
  // When using this option, fill the newState field for the log entry.
  STATE_CHANGED
}
