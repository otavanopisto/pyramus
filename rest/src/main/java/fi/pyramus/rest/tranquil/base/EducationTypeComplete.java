package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.base.EducationType.class, entityType = TranquilModelType.COMPLETE)
public class EducationTypeComplete extends EducationTypeBase {

  public TranquilModelEntity getEducationSubtypeById() {
    return educationSubtypeById;
  }

  public void setEducationSubtypeById(TranquilModelEntity educationSubtypeById) {
    this.educationSubtypeById = educationSubtypeById;
  }

  public java.util.List<TranquilModelEntity> getSubtypes() {
    return subtypes;
  }

  public void setSubtypes(java.util.List<TranquilModelEntity> subtypes) {
    this.subtypes = subtypes;
  }

  public java.util.List<TranquilModelEntity> getUnarchivedSubtypes() {
    return unarchivedSubtypes;
  }

  public void setUnarchivedSubtypes(java.util.List<TranquilModelEntity> unarchivedSubtypes) {
    this.unarchivedSubtypes = unarchivedSubtypes;
  }

  public java.util.List<TranquilModelEntity> getArchivedSubtypes() {
    return archivedSubtypes;
  }

  public void setArchivedSubtypes(java.util.List<TranquilModelEntity> archivedSubtypes) {
    this.archivedSubtypes = archivedSubtypes;
  }

  private TranquilModelEntity educationSubtypeById;

  private java.util.List<TranquilModelEntity> subtypes;

  private java.util.List<TranquilModelEntity> unarchivedSubtypes;

  private java.util.List<TranquilModelEntity> archivedSubtypes;

  public final static String[] properties = {"educationSubtypeById","subtypes","unarchivedSubtypes","archivedSubtypes"};
}
