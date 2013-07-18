package fi.pyramus.rest.tranquil.help;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.help.HelpItem.class, entityType = TranquilModelType.COMPLETE)
public class HelpItemComplete extends HelpItemBase {

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

  public java.util.List<TranquilModelEntity> getTitles() {
    return titles;
  }

  public void setTitles(java.util.List<TranquilModelEntity> titles) {
    this.titles = titles;
  }

  public java.util.List<TranquilModelEntity> getTags() {
    return tags;
  }

  public void setTags(java.util.List<TranquilModelEntity> tags) {
    this.tags = tags;
  }

  private TranquilModelEntity parent;

  private TranquilModelEntity creator;

  private TranquilModelEntity lastModifier;

  private java.util.List<TranquilModelEntity> titles;

  private java.util.List<TranquilModelEntity> tags;

  public final static String[] properties = {"parent","creator","lastModifier","titles","tags"};
}
