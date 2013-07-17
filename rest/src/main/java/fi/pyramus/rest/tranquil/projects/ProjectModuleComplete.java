package fi.pyramus.rest.tranquil.projects;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.projects.ProjectModule.class, entityType = TranquilModelType.COMPLETE)
public class ProjectModuleComplete extends ProjectModuleBase {

  public TranquilModelEntity getModule() {
    return module;
  }

  public void setModule(TranquilModelEntity module) {
    this.module = module;
  }

  public TranquilModelEntity getProject() {
    return project;
  }

  public void setProject(TranquilModelEntity project) {
    this.project = project;
  }

  private TranquilModelEntity module;

  private TranquilModelEntity project;

  public final static String[] properties = {"module","project"};
}
