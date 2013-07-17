package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.base.PhoneNumber.class, entityType = TranquilModelType.BASE)
public class PhoneNumberBase implements fi.tranquil.TranquilModelEntity {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Boolean getDefaultNumber() {
    return defaultNumber;
  }

  public void setDefaultNumber(Boolean defaultNumber) {
    this.defaultNumber = defaultNumber;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  private Long id;

  private Boolean defaultNumber;

  private String number;

  private Long version;

  public final static String[] properties = {"id","defaultNumber","number","version"};
}
