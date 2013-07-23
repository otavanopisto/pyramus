package fi.pyramus.rest.tranquil.resources;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.resources.WorkResource.class, entityType = TranquilModelType.UPDATE)
public class WorkResourceUpdate extends WorkResourceComplete {

  public void setCategory(ResourceCategoryCompact category) {
    super.setCategory(category);
  }

  public ResourceCategoryCompact getCategory() {
    return (ResourceCategoryCompact)super.getCategory();
  }

  public void setTags(java.util.List<TagCompact> tags) {
    super.setTags(tags);
  }

  public java.util.List<TagCompact> getTags() {
    return (java.util.List<TagCompact>)super.getTags();
  }

  public final static String[] properties = {"category","tags"};
}
