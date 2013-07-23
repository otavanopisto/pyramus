package fi.pyramus.rest.tranquil.resources;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.resources.WorkResource.class, entityType = TranquilModelType.COMPLETE)
public class WorkResourceComplete extends WorkResourceBase {

  public TranquilModelEntity getCategory() {
    return category;
  }

  public void setCategory(TranquilModelEntity category) {
    this.category = category;
  }

  public java.util.List<TranquilModelEntity> getTags() {
    return tags;
  }

  public void setTags(java.util.List<TranquilModelEntity> tags) {
    this.tags = tags;
  }

  private TranquilModelEntity category;

  private java.util.List<TranquilModelEntity> tags;

  public final static String[] properties = {"category","tags"};
}
