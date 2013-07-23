package fi.pyramus.rest.tranquil.plugins;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.plugins.Plugin.class, entityType = TranquilModelType.UPDATE)
public class PluginUpdate extends PluginComplete {

  public final static String[] properties = {};
}
