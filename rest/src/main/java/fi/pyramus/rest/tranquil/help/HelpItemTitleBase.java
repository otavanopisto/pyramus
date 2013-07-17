package fi.pyramus.rest.tranquil.help;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.help.HelpItemTitle.class, entityType = TranquilModelType.BASE)
public class HelpItemTitleBase implements fi.tranquil.TranquilModelEntity {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public java.util.Locale getLocale() {
    return locale;
  }

  public void setLocale(java.util.Locale locale) {
    this.locale = locale;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
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

  private java.util.Locale locale;

  private String title;

  private java.util.Date created;

  private java.util.Date lastModified;

  public final static String[] properties = {"id","locale","title","created","lastModified"};
}
