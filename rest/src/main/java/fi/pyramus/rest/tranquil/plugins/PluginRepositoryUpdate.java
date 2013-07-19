package fi.pyramus.rest.tranquil.plugins;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.plugins.PluginRepository.class, entityType = TranquilModelType.UPDATE)
public class PluginRepositoryUpdate extends PluginRepositoryComplete {

  public final static String[] properties = {};
}
