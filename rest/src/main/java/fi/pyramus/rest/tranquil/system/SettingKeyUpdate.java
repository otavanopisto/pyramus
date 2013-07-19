package fi.pyramus.rest.tranquil.system;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.system.SettingKey.class, entityType = TranquilModelType.UPDATE)
public class SettingKeyUpdate extends SettingKeyComplete {

  public final static String[] properties = {};
}
