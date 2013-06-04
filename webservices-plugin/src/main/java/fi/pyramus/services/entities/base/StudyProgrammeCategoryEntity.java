package fi.pyramus.services.entities.base;

public class StudyProgrammeCategoryEntity {

  public StudyProgrammeCategoryEntity() {
  }
  
  public StudyProgrammeCategoryEntity(Long id, String name, Boolean archived, EducationTypeEntity educationType) {
    super();
    this.id = id;
    this.name = name;
    this.archived = archived;
    this.educationType = educationType;
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

  public EducationTypeEntity getEducationType() {
    return educationType;
  }

  public void setEducationType(EducationTypeEntity educationType) {
    this.educationType = educationType;
  }

  private Long id;
  private String name;
  private Boolean archived;
  private EducationTypeEntity educationType;
}
