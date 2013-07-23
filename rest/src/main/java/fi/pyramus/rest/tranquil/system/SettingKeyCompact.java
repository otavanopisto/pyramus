package fi.pyramus.rest.tranquil.system;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.system.SettingKey.class, entityType = TranquilModelType.COMPACT)
public class SettingKeyCompact extends SettingKeyBase {

  public final static String[] properties = {};
}
