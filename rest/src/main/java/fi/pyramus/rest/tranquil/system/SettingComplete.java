package fi.pyramus.rest.tranquil.system;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.system.Setting.class, entityType = TranquilModelType.COMPLETE)
public class SettingComplete extends SettingBase {

  public TranquilModelEntity getKey() {
    return key;
  }

  public void setKey(TranquilModelEntity key) {
    this.key = key;
  }

  private TranquilModelEntity key;

  public final static String[] properties = {"key"};
}
