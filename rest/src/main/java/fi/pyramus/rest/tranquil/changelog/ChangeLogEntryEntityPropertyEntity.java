package fi.pyramus.rest.tranquil.changelog;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.changelog.ChangeLogEntryEntityProperty.class, entityType = TranquilModelType.COMPACT)
public class ChangeLogEntryEntityPropertyEntity implements fi.tranquil.TranquilModelEntity {

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

  public Long getEntity_id() {
    return entity_id;
  }

  public void setEntity_id(Long entity_id) {
    this.entity_id = entity_id;
  }

  private Long id;

  private String name;

  private Long entity_id;

  public final static String[] properties = {"id","name","entity"};
}
