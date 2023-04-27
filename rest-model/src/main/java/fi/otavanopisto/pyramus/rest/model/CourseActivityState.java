package fi.otavanopisto.pyramus.rest.model;

public enum CourseActivityState {

  ONGOING("unassessed"),
  ASSESSMENT_REQUESTED_NO_GRADE("pending"),
  ASSESSMENT_REQUESTED_PASSING_GRADE("pending_pass"),
  ASSESSMENT_REQUESTED_FAILING_GRADE("pending_fail"),
  GRADED_PASS("pass"),
  GRADED_FAIL("fail"),
  TRANSFERRED("transferred");
  
  CourseActivityState(final String value) {
    this.value = value;
  }
  
  @Override
  public String toString() {
    return value;
  }      
  
  private final String value;
  
}
