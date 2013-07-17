package fi.pyramus.rest.tranquil.plugins;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.plugins.PluginRepository.class, entityType = TranquilModelType.COMPLETE)
public class PluginRepositoryComplete extends PluginRepositoryBase {

  public final static String[] properties = {};
}
