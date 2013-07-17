package fi.pyramus.rest.tranquil.resources;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.resources.Resource.class, entityType = TranquilModelType.COMPLETE)
public class ResourceComplete extends ResourceBase {

  public TranquilModelEntity getCategory() {
    return category;
  }

  public void setCategory(TranquilModelEntity category) {
    this.category = category;
  }

  private TranquilModelEntity category;

  public final static String[] properties = {"category"};
}
