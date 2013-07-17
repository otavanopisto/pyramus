package fi.pyramus.rest.tranquil.projects;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.projects.Project.class, entityType = TranquilModelType.COMPLETE)
public class ProjectComplete extends ProjectBase {

  public TranquilModelEntity getCreator() {
    return creator;
  }

  public void setCreator(TranquilModelEntity creator) {
    this.creator = creator;
  }

  public TranquilModelEntity getLastModifier() {
    return lastModifier;
  }

  public void setLastModifier(TranquilModelEntity lastModifier) {
    this.lastModifier = lastModifier;
  }

  public TranquilModelEntity getOptionalStudiesLength() {
    return optionalStudiesLength;
  }

  public void setOptionalStudiesLength(TranquilModelEntity optionalStudiesLength) {
    this.optionalStudiesLength = optionalStudiesLength;
  }

  public java.util.List<TranquilModelEntity> getProjectModules() {
    return projectModules;
  }

  public void setProjectModules(java.util.List<TranquilModelEntity> projectModules) {
    this.projectModules = projectModules;
  }

  private TranquilModelEntity creator;

  private TranquilModelEntity lastModifier;

  private TranquilModelEntity optionalStudiesLength;

  private java.util.List<TranquilModelEntity> projectModules;

  public final static String[] properties = {"creator","lastModifier","optionalStudiesLength","projectModules"};
}
