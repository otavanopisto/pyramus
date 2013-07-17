package fi.pyramus.rest.tranquil.users;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.users.UserVariable.class, entityType = TranquilModelType.COMPLETE)
public class UserVariableComplete extends UserVariableBase {

  public TranquilModelEntity getUser() {
    return user;
  }

  public void setUser(TranquilModelEntity user) {
    this.user = user;
  }

  public TranquilModelEntity getKey() {
    return key;
  }

  public void setKey(TranquilModelEntity key) {
    this.key = key;
  }

  private TranquilModelEntity user;

  private TranquilModelEntity key;

  public final static String[] properties = {"user","key"};
}
