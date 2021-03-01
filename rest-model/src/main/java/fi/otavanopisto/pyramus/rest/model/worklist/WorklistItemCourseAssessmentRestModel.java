package fi.otavanopisto.pyramus.rest.model.worklist;

/**
 * Course assessment information for worklist items associated with one.
 */
public class WorklistItemCourseAssessmentRestModel {

  public String getCourseName() {
    return courseName;
  }

  public void setCourseName(String courseName) {
    this.courseName = courseName;
  }

  public String getStudentName() {
    return studentName;
  }

  public void setStudentName(String studentName) {
    this.studentName = studentName;
  }

  public Boolean getRaisedGrade() {
    return raisedGrade;
  }

  public void setRaisedGrade(Boolean raisedGrade) {
    this.raisedGrade = raisedGrade;
  }

  private String courseName;
  private String studentName;
  private Boolean raisedGrade;

}
