package fi.pyramus.rest.tranquil.system;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.system.Setting.class, entityType = TranquilModelType.BASE)
public class SettingBase implements fi.tranquil.TranquilModelEntity {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  private Long id;

  private String value;

  public final static String[] properties = {"id","value"};
}
