package fi.pyramus.events;

public class CourseUpdatedEvent {

  public CourseUpdatedEvent(Long courseId) {
    this.courseId = courseId;
  }
  
  public Long getCourseId() {
    return courseId;
  }
  
  private Long courseId;
}
