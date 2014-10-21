package fi.pyramus.events;

public class CourseCreatedEvent {

  public CourseCreatedEvent(Long courseId) {
    this.courseId = courseId;
  }
  
  public Long getCourseId() {
    return courseId;
  }
  
  private Long courseId;
}
