package fi.pyramus.rest.model;

public class CourseEducationType {

  public CourseEducationType() {
  }

  public CourseEducationType(Long id, Long educationTypeId, Boolean archived) {
    this();
    this.id = id;
    this.educationTypeId = educationTypeId;
    this.archived = archived;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  public Long getEducationTypeId() {
    return educationTypeId;
  }

  public void setEducationTypeId(Long educationTypeId) {
    this.educationTypeId = educationTypeId;
  }

  private Long id;
  private Long educationTypeId;
  private Boolean archived;
}
