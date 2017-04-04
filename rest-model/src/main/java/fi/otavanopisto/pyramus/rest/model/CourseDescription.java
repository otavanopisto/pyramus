package fi.otavanopisto.pyramus.rest.model;

public class CourseDescription {
  
  public CourseDescription() {
  }
  
  public CourseDescription(Long id, Long courseBaseId, Long courseDescriptionCategoryId, String description) {
    super();
    this.id = id;
    this.courseBaseId = courseBaseId;
    this.courseDescriptionCategoryId = courseDescriptionCategoryId;
    this.description = description;
  }
  
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getCourseBaseId() {
    return courseBaseId;
  }

  public void setCourseBaseId(Long courseBaseId) {
    this.courseBaseId = courseBaseId;
  }

  public Long getCourseDescriptionCategoryId() {
    return courseDescriptionCategoryId;
  }

  public void setCourseDescriptionCategoryId(Long courseDescriptionCategoryId) {
    this.courseDescriptionCategoryId = courseDescriptionCategoryId;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  private Long id;
  private Long courseBaseId;
  private Long courseDescriptionCategoryId;
  private String description;
}
