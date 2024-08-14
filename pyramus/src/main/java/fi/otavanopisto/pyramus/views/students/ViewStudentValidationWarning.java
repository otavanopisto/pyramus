package fi.otavanopisto.pyramus.views.students;

import fi.otavanopisto.pyramus.domainmodel.students.Student;

/**
 * POJO to deliver validation warnings about student information to front end.
 */
public class ViewStudentValidationWarning {

  public enum ViewStudentValidationType {
    // Student's studyEndDate is set before studyStartDate
    STUDYENDDATE_BEFORE_STARTDATE,
    // Some StudyPeriod for the Student is not within the timeframe of studyStartDate to studyEndDate
    STUDYPERIOD_OUTSIDE_STUDYTIME
  }
  
  public enum ViewStudentValidationWarningSeverity {
    // Just a warning
    WARNING,
    // Error, when something is deemed to be in an invalid state
    ERROR
  }
  
  public ViewStudentValidationWarning(Student student, ViewStudentValidationType type, ViewStudentValidationWarningSeverity severity) {
    this.setStudent(student);
    this.type = type;
    this.severity = severity;
  }
  
  public ViewStudentValidationWarningSeverity getSeverity() {
    return severity;
  }

  public void setSeverity(ViewStudentValidationWarningSeverity severity) {
    this.severity = severity;
  }

  public ViewStudentValidationType getType() {
    return type;
  }

  public void setType(ViewStudentValidationType type) {
    this.type = type;
  }

  public Student getStudent() {
    return student;
  }

  public void setStudent(Student student) {
    this.student = student;
  }

  private Student student;
  private ViewStudentValidationType type;
  private ViewStudentValidationWarningSeverity severity;
}
