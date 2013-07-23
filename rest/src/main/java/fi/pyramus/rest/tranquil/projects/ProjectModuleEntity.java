package fi.pyramus.rest.tranquil.projects;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.projects.ProjectModule.class, entityType = TranquilModelType.COMPACT)
public class ProjectModuleEntity implements fi.tranquil.TranquilModelEntity {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public fi.pyramus.domainmodel.projects.ProjectModuleOptionality getOptionality() {
    return optionality;
  }

  public void setOptionality(fi.pyramus.domainmodel.projects.ProjectModuleOptionality optionality) {
    this.optionality = optionality;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public Long getModule_id() {
    return module_id;
  }

  public void setModule_id(Long module_id) {
    this.module_id = module_id;
  }

  public Long getProject_id() {
    return project_id;
  }

  public void setProject_id(Long project_id) {
    this.project_id = project_id;
  }

  private Long id;

  private fi.pyramus.domainmodel.projects.ProjectModuleOptionality optionality;

  private Long version;

  private Long module_id;

  private Long project_id;

  public final static String[] properties = {"id","optionality","version","module","project"};
}
