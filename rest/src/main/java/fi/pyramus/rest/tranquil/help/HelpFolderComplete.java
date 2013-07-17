package fi.pyramus.rest.tranquil.help;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.help.HelpFolder.class, entityType = TranquilModelType.COMPLETE)
public class HelpFolderComplete extends HelpFolderBase {

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

  public java.util.List<TranquilModelEntity> getTitles() {
    return titles;
  }

  public void setTitles(java.util.List<TranquilModelEntity> titles) {
    this.titles = titles;
  }

  public java.util.List<TranquilModelEntity> getChildren() {
    return children;
  }

  public void setChildren(java.util.List<TranquilModelEntity> children) {
    this.children = children;
  }

  private TranquilModelEntity parent;

  private TranquilModelEntity creator;

  private TranquilModelEntity lastModifier;

  private TranquilModelEntity titleByLocale;

  private java.util.List<TranquilModelEntity> titles;

  private java.util.List<TranquilModelEntity> children;

  public final static String[] properties = {"parent","creator","lastModifier","titleByLocale","titles","children"};
}
