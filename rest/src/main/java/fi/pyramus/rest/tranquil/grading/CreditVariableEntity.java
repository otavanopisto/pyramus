package fi.pyramus.rest.tranquil.grading;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.grading.CreditVariable.class, entityType = TranquilModelType.COMPACT)
public class CreditVariableEntity implements fi.tranquil.TranquilModelEntity {

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

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public Long getCredit_id() {
    return credit_id;
  }

  public void setCredit_id(Long credit_id) {
    this.credit_id = credit_id;
  }

  public Long getKey_id() {
    return key_id;
  }

  public void setKey_id(Long key_id) {
    this.key_id = key_id;
  }

  private Long id;

  private String value;

  private Long version;

  private Long credit_id;

  private Long key_id;

  public final static String[] properties = {"id","value","version","credit","key"};
}
