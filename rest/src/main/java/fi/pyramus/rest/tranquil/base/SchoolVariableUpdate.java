package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.base.SchoolVariable.class, entityType = TranquilModelType.UPDATE)
public class SchoolVariableUpdate extends SchoolVariableComplete {

  public void setSchool(SchoolCompact school) {
    super.setSchool(school);
  }

  public SchoolCompact getSchool() {
    return (SchoolCompact)super.getSchool();
  }

  public void setKey(SchoolVariableKeyCompact key) {
    super.setKey(key);
  }

  public SchoolVariableKeyCompact getKey() {
    return (SchoolVariableKeyCompact)super.getKey();
  }

  public final static String[] properties = {"school","key"};
}
