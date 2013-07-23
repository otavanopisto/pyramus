package fi.pyramus.rest.tranquil.resources;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.resources.WorkResource.class, entityType = TranquilModelType.COMPACT)
public class WorkResourceEntity implements fi.tranquil.TranquilModelEntity {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public fi.pyramus.persistence.usertypes.MonetaryAmount getHourlyCost() {
    return hourlyCost;
  }

  public void setHourlyCost(fi.pyramus.persistence.usertypes.MonetaryAmount hourlyCost) {
    this.hourlyCost = hourlyCost;
  }

  public fi.pyramus.persistence.usertypes.MonetaryAmount getCostPerUse() {
    return costPerUse;
  }

  public void setCostPerUse(fi.pyramus.persistence.usertypes.MonetaryAmount costPerUse) {
    this.costPerUse = costPerUse;
  }

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

  private Long id;

  private String name;

  private Boolean archived;

  private Long version;

  private fi.pyramus.persistence.usertypes.MonetaryAmount hourlyCost;

  private fi.pyramus.persistence.usertypes.MonetaryAmount costPerUse;

  private Long category_id;

  private java.util.List<Long> tags_ids;

  public final static String[] properties = {"id","name","archived","version","hourlyCost","costPerUse","category","tags"};
}
