package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.base.MagicKey.class, entityType = TranquilModelType.BASE)
public class MagicKeyBase implements fi.tranquil.TranquilModelEntity {

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

  public java.util.Date getCreated() {
    return created;
  }

  public void setCreated(java.util.Date created) {
    this.created = created;
  }

  public fi.pyramus.domainmodel.base.MagicKeyScope getScope() {
    return scope;
  }

  public void setScope(fi.pyramus.domainmodel.base.MagicKeyScope scope) {
    this.scope = scope;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  private Long id;

  private String name;

  private java.util.Date created;

  private fi.pyramus.domainmodel.base.MagicKeyScope scope;

  private Long version;

  public final static String[] properties = {"id","name","created","scope","version"};
}
