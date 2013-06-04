package fi.pyramus.services.entities.courses;

public class CourseDescriptionEntity {
  
  public CourseDescriptionEntity() {
  }

  public CourseDescriptionEntity(Long id, Long courseBaseId, CourseDescriptionCategoryEntity category, String description) {
    this.id = id;
    this.courseBaseId = courseBaseId;
    this.category = category;
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

  public CourseDescriptionCategoryEntity getCategory() {
    return category;
  }

  public void setCategory(CourseDescriptionCategoryEntity category) {
    this.category = category;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  private Long id;
  private Long courseBaseId;
  private CourseDescriptionCategoryEntity category;
  private String description;
}
