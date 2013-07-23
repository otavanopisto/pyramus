package fi.pyramus.rest.tranquil.help;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.help.HelpPageContent.class, entityType = TranquilModelType.COMPACT)
public class HelpPageContentEntity implements fi.tranquil.TranquilModelEntity {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public java.util.Locale getLocale() {
    return locale;
  }

  public void setLocale(java.util.Locale locale) {
    this.locale = locale;
  }

  public java.util.Date getCreated() {
    return created;
  }

  public void setCreated(java.util.Date created) {
    this.created = created;
  }

  public java.util.Date getLastModified() {
    return lastModified;
  }

  public void setLastModified(java.util.Date lastModified) {
    this.lastModified = lastModified;
  }

  public Long getPage_id() {
    return page_id;
  }

  public void setPage_id(Long page_id) {
    this.page_id = page_id;
  }

  public Long getCreator_id() {
    return creator_id;
  }

  public void setCreator_id(Long creator_id) {
    this.creator_id = creator_id;
  }

  public Long getLastModifier_id() {
    return lastModifier_id;
  }

  public void setLastModifier_id(Long lastModifier_id) {
    this.lastModifier_id = lastModifier_id;
  }

  private Long id;

  private String content;

  private java.util.Locale locale;

  private java.util.Date created;

  private java.util.Date lastModified;

  private Long page_id;

  private Long creator_id;

  private Long lastModifier_id;

  public final static String[] properties = {"id","content","locale","created","lastModified","page","creator","lastModifier"};
}
