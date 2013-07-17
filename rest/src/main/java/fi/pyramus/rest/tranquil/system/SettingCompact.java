package fi.pyramus.rest.tranquil.system;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.system.Setting.class, entityType = TranquilModelType.COMPACT)
public class SettingCompact extends SettingBase {

  public Long getKey_id() {
    return key_id;
  }

  public void setKey_id(Long key_id) {
    this.key_id = key_id;
  }

  private Long key_id;

  public final static String[] properties = {"key"};
}
