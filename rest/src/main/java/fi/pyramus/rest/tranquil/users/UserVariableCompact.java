package fi.pyramus.rest.tranquil.users;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.users.UserVariable.class, entityType = TranquilModelType.COMPACT)
public class UserVariableCompact extends UserVariableBase {

  public Long getUser_id() {
    return user_id;
  }

  public void setUser_id(Long user_id) {
    this.user_id = user_id;
  }

  public Long getKey_id() {
    return key_id;
  }

  public void setKey_id(Long key_id) {
    this.key_id = key_id;
  }

  private Long user_id;

  private Long key_id;

  public final static String[] properties = {"user","key"};
}
