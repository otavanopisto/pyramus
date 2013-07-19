package fi.pyramus.rest.tranquil.system;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.system.Setting.class, entityType = TranquilModelType.UPDATE)
public class SettingUpdate extends SettingComplete {

  public void setKey(SettingKeyCompact key) {
    super.setKey(key);
  }

  public SettingKeyCompact getKey() {
    return (SettingKeyCompact)super.getKey();
  }

  public final static String[] properties = {"key"};
}
