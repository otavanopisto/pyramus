package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.base.SchoolVariable.class, entityType = TranquilModelType.COMPLETE)
public class SchoolVariableComplete extends SchoolVariableBase {

  public TranquilModelEntity getSchool() {
    return school;
  }

  public void setSchool(TranquilModelEntity school) {
    this.school = school;
  }

  public TranquilModelEntity getKey() {
    return key;
  }

  public void setKey(TranquilModelEntity key) {
    this.key = key;
  }

  private TranquilModelEntity school;

  private TranquilModelEntity key;

  public final static String[] properties = {"school","key"};
}
