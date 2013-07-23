package fi.pyramus.rest.tranquil.modules;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.modules.ModuleComponent.class, entityType = TranquilModelType.COMPACT)
public class ModuleComponentEntity implements fi.tranquil.TranquilModelEntity {

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

  public Long getModule_id() {
    return module_id;
  }

  public void setModule_id(Long module_id) {
    this.module_id = module_id;
  }

  private Long id;

  private String name;

  private String description;

  private Boolean archived;

  private Long version;

  private Long length_id;

  private Long module_id;

  public final static String[] properties = {"id","name","description","archived","version","length","module"};
}
