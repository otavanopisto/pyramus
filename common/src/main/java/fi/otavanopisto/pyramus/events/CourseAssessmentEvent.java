package fi.otavanopisto.pyramus.events;

public class CourseAssessmentEvent {

  public CourseAssessmentEvent(Long studentId, Long courseAssessmentId) {
    this.studentId = studentId;
    this.courseAssessmentId = courseAssessmentId;
  }

  public Long getCourseAssessmentId() {
    return courseAssessmentId;
  }

  public Long getStudentId() {
    return studentId;
  }

  private final Long courseAssessmentId;
  private final Long studentId;
}
