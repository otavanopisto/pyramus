package fi.pyramus.rest.tranquil.help;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.help.HelpItemTitle.class, entityType = TranquilModelType.COMPACT)
public class HelpItemTitleEntity implements fi.tranquil.TranquilModelEntity {

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

  public Long getItem_id() {
    return item_id;
  }

  public void setItem_id(Long item_id) {
    this.item_id = item_id;
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

  private java.util.Locale locale;

  private String title;

  private java.util.Date created;

  private java.util.Date lastModified;

  private Long item_id;

  private Long creator_id;

  private Long lastModifier_id;

  public final static String[] properties = {"id","locale","title","created","lastModified","item","creator","lastModifier"};
}
