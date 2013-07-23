package fi.pyramus.rest.tranquil.projects;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.projects.ProjectModule.class, entityType = TranquilModelType.UPDATE)
public class ProjectModuleUpdate extends ProjectModuleComplete {

  public void setModule(ModuleCompact module) {
    super.setModule(module);
  }

  public ModuleCompact getModule() {
    return (ModuleCompact)super.getModule();
  }

  public void setProject(ProjectCompact project) {
    super.setProject(project);
  }

  public ProjectCompact getProject() {
    return (ProjectCompact)super.getProject();
  }

  public final static String[] properties = {"module","project"};
}
