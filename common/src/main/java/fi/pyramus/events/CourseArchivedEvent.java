package fi.pyramus.events;

public class CourseArchivedEvent {

  public CourseArchivedEvent(Long courseId) {
    this.courseId = courseId;
  }
  
  public Long getCourseId() {
    return courseId;
  }
  
  private Long courseId;
}
