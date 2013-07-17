package fi.pyramus.rest.tranquil.plugins;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.plugins.Plugin.class, entityType = TranquilModelType.COMPLETE)
public class PluginComplete extends PluginBase {

  public final static String[] properties = {};
}
