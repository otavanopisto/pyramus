package fi.otavanopisto.pyramus.rest.model;

public enum MatriculationExamStudentStatus {
  
  // Student not eligible to enroll for exam
  NOT_ELIGIBLE,
  // Student eligible to enroll for exam
  ELIGIBLE,
  // Student has submitted the form (implies they were eligible at the time)
  SUBMITTED,
  // Enrollment has been approved (implies the form was also submitted before)
  APPROVED,
  // Enrollment has been rejected (implies the form was also submitted before)
  REJECTED
  
}
