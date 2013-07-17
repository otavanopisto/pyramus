package fi.pyramus.rest.tranquil.projects;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.projects.ProjectModule.class, entityType = TranquilModelType.BASE)
public class ProjectModuleBase implements fi.tranquil.TranquilModelEntity {

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

  private Long id;

  private fi.pyramus.domainmodel.projects.ProjectModuleOptionality optionality;

  private Long version;

  public final static String[] properties = {"id","optionality","version"};
}
