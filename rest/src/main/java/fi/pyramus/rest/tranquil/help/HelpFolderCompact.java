package fi.pyramus.rest.tranquil.help;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.help.HelpFolder.class, entityType = TranquilModelType.COMPACT)
public class HelpFolderCompact extends HelpFolderBase {

  public Long getParent_id() {
    return parent_id;
  }

  public void setParent_id(Long parent_id) {
    this.parent_id = parent_id;
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

  public Long getTitleByLocale_id() {
    return titleByLocale_id;
  }

  public void setTitleByLocale_id(Long titleByLocale_id) {
    this.titleByLocale_id = titleByLocale_id;
  }

  public java.util.List<Long> getTitles_ids() {
    return titles_ids;
  }

  public void setTitles_ids(java.util.List<Long> titles_ids) {
    this.titles_ids = titles_ids;
  }

  public java.util.List<Long> getChildren_ids() {
    return children_ids;
  }

  public void setChildren_ids(java.util.List<Long> children_ids) {
    this.children_ids = children_ids;
  }

  private Long parent_id;

  private Long creator_id;

  private Long lastModifier_id;

  private Long titleByLocale_id;

  private java.util.List<Long> titles_ids;

  private java.util.List<Long> children_ids;

  public final static String[] properties = {"parent","creator","lastModifier","titleByLocale","titles","children"};
}
