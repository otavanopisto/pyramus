package fi.pyramus.rest.tranquil.help;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.help.HelpPage.class, entityType = TranquilModelType.COMPLETE)
public class HelpPageComplete extends HelpPageBase {

  public TranquilModelEntity getParent() {
    return parent;
  }

  public void setParent(TranquilModelEntity parent) {
    this.parent = parent;
  }

  public TranquilModelEntity getCreator() {
    return creator;
  }

  public void setCreator(TranquilModelEntity creator) {
    this.creator = creator;
  }

  public TranquilModelEntity getLastModifier() {
    return lastModifier;
  }

  public void setLastModifier(TranquilModelEntity lastModifier) {
    this.lastModifier = lastModifier;
  }

  public TranquilModelEntity getTitleByLocale() {
    return titleByLocale;
  }

  public void setTitleByLocale(TranquilModelEntity titleByLocale) {
    this.titleByLocale = titleByLocale;
  }

  public TranquilModelEntity getContentByLocale() {
    return contentByLocale;
  }

  public void setContentByLocale(TranquilModelEntity contentByLocale) {
    this.contentByLocale = contentByLocale;
  }

  public java.util.List<TranquilModelEntity> getTitles() {
    return titles;
  }

  public void setTitles(java.util.List<TranquilModelEntity> titles) {
    this.titles = titles;
  }

  public java.util.List<TranquilModelEntity> getContents() {
    return contents;
  }

  public void setContents(java.util.List<TranquilModelEntity> contents) {
    this.contents = contents;
  }

  private TranquilModelEntity parent;

  private TranquilModelEntity creator;

  private TranquilModelEntity lastModifier;

  private TranquilModelEntity titleByLocale;

  private TranquilModelEntity contentByLocale;

  private java.util.List<TranquilModelEntity> titles;

  private java.util.List<TranquilModelEntity> contents;

  public final static String[] properties = {"parent","creator","lastModifier","titleByLocale","contentByLocale","titles","contents"};
}
