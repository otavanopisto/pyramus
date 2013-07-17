package fi.pyramus.rest.tranquil.modules;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.modules.Module.class, entityType = TranquilModelType.COMPLETE)
public class ModuleComplete extends ModuleBase {

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

  public TranquilModelEntity getSubject() {
    return subject;
  }

  public void setSubject(TranquilModelEntity subject) {
    this.subject = subject;
  }

  public TranquilModelEntity getCourseEducationTypeByEducationTypeId() {
    return courseEducationTypeByEducationTypeId;
  }

  public void setCourseEducationTypeByEducationTypeId(TranquilModelEntity courseEducationTypeByEducationTypeId) {
    this.courseEducationTypeByEducationTypeId = courseEducationTypeByEducationTypeId;
  }

  public TranquilModelEntity getCourseLength() {
    return courseLength;
  }

  public void setCourseLength(TranquilModelEntity courseLength) {
    this.courseLength = courseLength;
  }

  public java.util.List<TranquilModelEntity> getCourseEducationTypes() {
    return courseEducationTypes;
  }

  public void setCourseEducationTypes(java.util.List<TranquilModelEntity> courseEducationTypes) {
    this.courseEducationTypes = courseEducationTypes;
  }

  public java.util.List<TranquilModelEntity> getVariables() {
    return variables;
  }

  public void setVariables(java.util.List<TranquilModelEntity> variables) {
    this.variables = variables;
  }

  public java.util.List<TranquilModelEntity> getModuleComponents() {
    return moduleComponents;
  }

  public void setModuleComponents(java.util.List<TranquilModelEntity> moduleComponents) {
    this.moduleComponents = moduleComponents;
  }

  private TranquilModelEntity creator;

  private TranquilModelEntity lastModifier;

  private TranquilModelEntity subject;

  private TranquilModelEntity courseEducationTypeByEducationTypeId;

  private TranquilModelEntity courseLength;

  private java.util.List<TranquilModelEntity> courseEducationTypes;

  private java.util.List<TranquilModelEntity> variables;

  private java.util.List<TranquilModelEntity> moduleComponents;

  public final static String[] properties = {"creator","lastModifier","subject","courseEducationTypeByEducationTypeId","courseLength","courseEducationTypes","variables","moduleComponents"};
}
