package fi.otavanopisto.pyramus.rest.model;

public class ProjectModule {

  public ProjectModule() {
    super();
  }

  public ProjectModule(Long id, Long moduleId, ProjectModuleOptionality optionality) {
    this();
    this.id = id;
    this.moduleId = moduleId;
    this.optionality = optionality;
  }
  
  public Long getId() {
    return id;
  }
  
  public void setId(Long id) {
    this.id = id;
  }

  public Long getModuleId() {
    return moduleId;
  }

  public void setModuleId(Long moduleId) {
    this.moduleId = moduleId;
  }

  public ProjectModuleOptionality getOptionality() {
    return optionality;
  }

  public void setOptionality(ProjectModuleOptionality optionality) {
    this.optionality = optionality;
  }

  private Long id;
  private Long moduleId;
  private ProjectModuleOptionality optionality;

}
