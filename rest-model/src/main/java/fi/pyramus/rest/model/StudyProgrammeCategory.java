package fi.pyramus.rest.model;

public class StudyProgrammeCategory {

  public StudyProgrammeCategory() {
  }

  public StudyProgrammeCategory(Long id, String name, Long educationTypeId, Boolean archived) {
    super();
    this.id = id;
    this.name = name;
    this.educationTypeId = educationTypeId;
    this.archived = archived;
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
  
  public Long getEducationTypeId() {
    return educationTypeId;
  }
  
  public void setEducationTypeId(Long educationTypeId) {
    this.educationTypeId = educationTypeId;
  }
  
  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  private Long id;
  private String name;
  private Long educationTypeId;
  private Boolean archived;
}