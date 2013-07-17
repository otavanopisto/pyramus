package fi.pyramus.rest.tranquil.resources;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.resources.ResourceCategory.class, entityType = TranquilModelType.COMPACT)
public class ResourceCategoryCompact extends ResourceCategoryBase {

  public final static String[] properties = {};
}
