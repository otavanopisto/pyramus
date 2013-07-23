package fi.pyramus.rest.tranquil.resources;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.resources.ResourceCategory.class, entityType = TranquilModelType.UPDATE)
public class ResourceCategoryUpdate extends ResourceCategoryComplete {

  public final static String[] properties = {};
}
