package fi.otavanopisto.pyramus.rest.model;

public class CourseEnrolmentType {

  public CourseEnrolmentType() {
  }

  public CourseEnrolmentType(Long id, String name) {
    this();
    this.id = id;
    this.name = name;
  }

  public CourseEnrolmentType(String name) {
    this(null, name);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  private Long id;
  private String name;
}
