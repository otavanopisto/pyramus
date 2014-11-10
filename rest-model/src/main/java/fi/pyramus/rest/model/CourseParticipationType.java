package fi.pyramus.rest.model;

public class CourseParticipationType {

  public CourseParticipationType() {
  }

  public CourseParticipationType(Long id, String name, Boolean archived) {
    this();
    this.id = id;
    this.name = name;
    this.archived = archived;
  }

  public CourseParticipationType(String name, Boolean archived) {
    this(null, name, archived);
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

  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  private Long id;
  private String name;
  private Boolean archived;
}
