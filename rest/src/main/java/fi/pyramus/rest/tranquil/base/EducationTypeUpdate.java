package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.base.EducationType.class, entityType = TranquilModelType.UPDATE)
public class EducationTypeUpdate extends EducationTypeComplete {

  public void setSubtypes(java.util.List<EducationSubtypeCompact> subtypes) {
    super.setSubtypes(subtypes);
  }

  public java.util.List<EducationSubtypeCompact> getSubtypes() {
    return (java.util.List<EducationSubtypeCompact>)super.getSubtypes();
  }

  public final static String[] properties = {"subtypes"};
}
