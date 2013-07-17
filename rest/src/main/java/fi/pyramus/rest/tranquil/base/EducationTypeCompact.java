package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.base.EducationType.class, entityType = TranquilModelType.COMPACT)
public class EducationTypeCompact extends EducationTypeBase {

  public Long getEducationSubtypeById_id() {
    return educationSubtypeById_id;
  }

  public void setEducationSubtypeById_id(Long educationSubtypeById_id) {
    this.educationSubtypeById_id = educationSubtypeById_id;
  }

  public java.util.List<Long> getSubtypes_ids() {
    return subtypes_ids;
  }

  public void setSubtypes_ids(java.util.List<Long> subtypes_ids) {
    this.subtypes_ids = subtypes_ids;
  }

  public java.util.List<Long> getUnarchivedSubtypes_ids() {
    return unarchivedSubtypes_ids;
  }

  public void setUnarchivedSubtypes_ids(java.util.List<Long> unarchivedSubtypes_ids) {
    this.unarchivedSubtypes_ids = unarchivedSubtypes_ids;
  }

  public java.util.List<Long> getArchivedSubtypes_ids() {
    return archivedSubtypes_ids;
  }

  public void setArchivedSubtypes_ids(java.util.List<Long> archivedSubtypes_ids) {
    this.archivedSubtypes_ids = archivedSubtypes_ids;
  }

  private Long educationSubtypeById_id;

  private java.util.List<Long> subtypes_ids;

  private java.util.List<Long> unarchivedSubtypes_ids;

  private java.util.List<Long> archivedSubtypes_ids;

  public final static String[] properties = {"educationSubtypeById","subtypes","unarchivedSubtypes","archivedSubtypes"};
}
