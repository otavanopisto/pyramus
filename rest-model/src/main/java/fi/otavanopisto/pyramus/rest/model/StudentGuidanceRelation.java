package fi.otavanopisto.pyramus.rest.model;

public class StudentGuidanceRelation {

  public boolean isSpecEdTeacher() {
    return isSpecEdTeacher;
  }

  public void setSpecEdTeacher(boolean isSpecEdTeacher) {
    this.isSpecEdTeacher = isSpecEdTeacher;
  }

  public boolean isGuidanceCounselor() {
    return isGuidanceCounselor;
  }

  public void setGuidanceCounselor(boolean isGuidanceCounselor) {
    this.isGuidanceCounselor = isGuidanceCounselor;
  }

  public boolean isCourseTeacher() {
    return isCourseTeacher;
  }

  public void setCourseTeacher(boolean isCourseTeacher) {
    this.isCourseTeacher = isCourseTeacher;
  }

  private boolean isSpecEdTeacher;
  private boolean isGuidanceCounselor;
  private boolean isCourseTeacher;

}
