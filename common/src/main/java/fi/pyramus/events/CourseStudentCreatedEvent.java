package fi.pyramus.events;

public class CourseStudentCreatedEvent {

  public CourseStudentCreatedEvent(Long courseStudentId, Long courseId, Long studentId) {
    super();
    this.courseStudentId = courseStudentId;
    this.courseId = courseId;
    this.studentId = studentId;
  }

  public Long getCourseStudentId() {
    return courseStudentId;
  }

  public Long getCourseId() {
    return courseId;
  }

  public Long getStudentId() {
    return studentId;
  }

  private Long courseStudentId;
  private Long courseId;
  private Long studentId;
}
