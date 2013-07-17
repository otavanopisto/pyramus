package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.base.ContactURL.class, entityType = TranquilModelType.BASE)
public class ContactURLBase implements fi.tranquil.TranquilModelEntity {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getURL() {
    return uRL;
  }

  public void setURL(String uRL) {
    this.uRL = uRL;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  private Long id;

  private String uRL;

  private Long version;

  public final static String[] properties = {"id","uRL","version"};
}
