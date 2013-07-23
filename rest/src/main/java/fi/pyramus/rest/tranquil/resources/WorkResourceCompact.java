package fi.pyramus.rest.tranquil.resources;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.resources.WorkResource.class, entityType = TranquilModelType.COMPACT)
public class WorkResourceCompact extends WorkResourceBase {

  public Long getCategory_id() {
    return category_id;
  }

  public void setCategory_id(Long category_id) {
    this.category_id = category_id;
  }

  public java.util.List<Long> getTags_ids() {
    return tags_ids;
  }

  public void setTags_ids(java.util.List<Long> tags_ids) {
    this.tags_ids = tags_ids;
  }

  private Long category_id;

  private java.util.List<Long> tags_ids;

  public final static String[] properties = {"category","tags"};
}
