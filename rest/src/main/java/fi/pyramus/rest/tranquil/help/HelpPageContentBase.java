package fi.pyramus.rest.tranquil.help;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.help.HelpPageContent.class, entityType = TranquilModelType.BASE)
public class HelpPageContentBase implements fi.tranquil.TranquilModelEntity {

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

  private Long id;

  private String content;

  private java.util.Locale locale;

  private java.util.Date created;

  private java.util.Date lastModified;

  public final static String[] properties = {"id","content","locale","created","lastModified"};
}
