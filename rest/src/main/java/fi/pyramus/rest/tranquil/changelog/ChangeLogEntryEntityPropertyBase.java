package fi.pyramus.rest.tranquil.changelog;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.changelog.ChangeLogEntryEntityProperty.class, entityType = TranquilModelType.BASE)
public class ChangeLogEntryEntityPropertyBase implements fi.tranquil.TranquilModelEntity {

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

  private Long id;

  private String name;

  public final static String[] properties = {"id","name"};
}
