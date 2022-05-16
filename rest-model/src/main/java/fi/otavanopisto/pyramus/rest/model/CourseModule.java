package fi.otavanopisto.pyramus.rest.model;

public class CourseModule {

  public CourseModule() {
  }

  public CourseModule(Long id, Subject subject, Integer courseNumber, CourseLength courseLength) {
    this();
    this.id = id;
    this.subject = subject;
    this.courseNumber = courseNumber;
    this.courseLength = courseLength;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Subject getSubject() {
    return subject;
  }

  public void setSubject(Subject subject) {
    this.subject = subject;
  }

  public Integer getCourseNumber() {
    return courseNumber;
  }

  public void setCourseNumber(Integer courseNumber) {
    this.courseNumber = courseNumber;
  }

  public CourseLength getCourseLength() {
    return courseLength;
  }

  public void setCourseLength(CourseLength courseLength) {
    this.courseLength = courseLength;
  }

  private Long id;
  private Subject subject;
  private Integer courseNumber;
  private CourseLength courseLength;
}
