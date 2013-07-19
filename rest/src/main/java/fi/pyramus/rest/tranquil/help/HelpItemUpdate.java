package fi.pyramus.rest.tranquil.help;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.help.HelpItem.class, entityType = TranquilModelType.UPDATE)
public class HelpItemUpdate extends HelpItemComplete {

  public void setParent(HelpFolderCompact parent) {
    super.setParent(parent);
  }

  public HelpFolderCompact getParent() {
    return (HelpFolderCompact)super.getParent();
  }

  public void setCreator(UserCompact creator) {
    super.setCreator(creator);
  }

  public UserCompact getCreator() {
    return (UserCompact)super.getCreator();
  }

  public void setLastModifier(UserCompact lastModifier) {
    super.setLastModifier(lastModifier);
  }

  public UserCompact getLastModifier() {
    return (UserCompact)super.getLastModifier();
  }

  public void setTitles(java.util.List<HelpItemTitleCompact> titles) {
    super.setTitles(titles);
  }

  public java.util.List<HelpItemTitleCompact> getTitles() {
    return (java.util.List<HelpItemTitleCompact>)super.getTitles();
  }

  public void setTags(java.util.List<TagCompact> tags) {
    super.setTags(tags);
  }

  public java.util.List<TagCompact> getTags() {
    return (java.util.List<TagCompact>)super.getTags();
  }

  public final static String[] properties = {"parent","creator","lastModifier","titles","tags"};
}
