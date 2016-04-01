package fi.otavanopisto.pyramus.rest.model;

public class CourseEducationSubtype {

  public CourseEducationSubtype() {
  }

  public CourseEducationSubtype(Long id, Long educationSubtypeId, Boolean archived) {
    this();
    this.id = id;
    this.educationSubtypeId = educationSubtypeId;
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

  public Long getEducationSubtypeId() {
    return educationSubtypeId;
  }

  public void setEducationSubtypeId(Long educationSubtypeId) {
    this.educationSubtypeId = educationSubtypeId;
  }

  private Long id;
  private Long educationSubtypeId;
  private Boolean archived;
}
