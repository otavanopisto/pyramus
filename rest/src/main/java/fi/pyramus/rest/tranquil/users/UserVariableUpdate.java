package fi.pyramus.rest.tranquil.users;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.users.UserVariable.class, entityType = TranquilModelType.UPDATE)
public class UserVariableUpdate extends UserVariableComplete {

  public void setUser(UserCompact user) {
    super.setUser(user);
  }

  public UserCompact getUser() {
    return (UserCompact)super.getUser();
  }

  public void setKey(UserVariableKeyCompact key) {
    super.setKey(key);
  }

  public UserVariableKeyCompact getKey() {
    return (UserVariableKeyCompact)super.getKey();
  }

  public final static String[] properties = {"user","key"};
}
