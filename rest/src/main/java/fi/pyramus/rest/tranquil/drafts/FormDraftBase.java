package fi.pyramus.rest.tranquil.drafts;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.drafts.FormDraft.class, entityType = TranquilModelType.BASE)
public class FormDraftBase implements fi.tranquil.TranquilModelEntity {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public java.util.Date getCreated() {
    return created;
  }

  public void setCreated(java.util.Date created) {
    this.created = created;
  }

  public java.util.Date getModified() {
    return modified;
  }

  public void setModified(java.util.Date modified) {
    this.modified = modified;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  private Long id;

  private String url;

  private java.util.Date created;

  private java.util.Date modified;

  private String data;

  private Long version;

  public final static String[] properties = {"id","url","created","modified","data","version"};
}
