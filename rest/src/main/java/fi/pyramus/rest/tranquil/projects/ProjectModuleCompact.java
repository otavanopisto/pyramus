package fi.pyramus.rest.tranquil.projects;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.projects.ProjectModule.class, entityType = TranquilModelType.COMPACT)
public class ProjectModuleCompact extends ProjectModuleBase {

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

  private Long module_id;

  private Long project_id;

  public final static String[] properties = {"module","project"};
}
