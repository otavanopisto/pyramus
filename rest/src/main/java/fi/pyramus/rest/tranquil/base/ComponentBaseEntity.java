package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.base.ComponentBase.class, entityType = TranquilModelType.COMPACT)
public class ComponentBaseEntity implements fi.tranquil.TranquilModelEntity {

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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
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

  public Long getLength_id() {
    return length_id;
  }

  public void setLength_id(Long length_id) {
    this.length_id = length_id;
  }

  private Long id;

  private String name;

  private String description;

  private Boolean archived;

  private Long version;

  private Long length_id;

  public final static String[] properties = {"id","name","description","archived","version","length"};
}
