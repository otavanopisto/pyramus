package fi.otavanopisto.pyramus.rest.model.report;

public enum PerusopetusCreditState {

  // OK
  ACCEPTED,
  // Re-evaluation of a course/module
  RAISED,
  // Re-evaluation of a course/module within the same calendar year
  RAISED_SAMEYEAR,
  // Re-evaluation of a course/module, where there has been passing evaluations before the current credit
  RAISED_PASSING,
  // Re-evaluation of a course/module, where there has been passing evaluations before the current credit (within same year)
  RAISED_SAMEYEAR_PASSING,
  
  // Rejected due to education type mismatch
  REJECTED_EDUCATIONTYPE,
  // Rejected due to mismatch or absence of course length
  REJECTED_COURSELENGTH,
  // Rejected due to invalid grade
  REJECTED_GRADE,
  // Rejected, student's curriculum doesn't match credit's curriculum
  REJECTED_MISMATCHING_CURRICULUM,
  // Rejected, student has no curriculum
  REJECTED_MISSING_STUDENT_CURRICULUM;
  
  
  public boolean isAcceptedState() {
    return this == ACCEPTED || this == RAISED || this == RAISED_SAMEYEAR;
  }
  
  public boolean isRejectedState() {
    return !this.isAcceptedState();
  }
  
}
