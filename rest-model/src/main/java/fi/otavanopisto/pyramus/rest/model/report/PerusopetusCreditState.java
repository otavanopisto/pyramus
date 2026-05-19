package fi.otavanopisto.pyramus.rest.model.report;

public enum PerusopetusCreditState {

  // OK
  ACCEPTED,
  // Re-evaluation of a course/module
  RAISED,
  // Re-evaluation of a course/module, where there has been passing evaluations before the current credit
  RAISED_PASSING,
  
  // Rejected due to education type mismatch
  REJECTED_EDUCATIONTYPE,
  // Rejected due to mismatch or absence of course length
  REJECTED_COURSELENGTH,
  // Rejected due to invalid grade
  REJECTED_GRADE
}
