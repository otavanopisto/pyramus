package fi.pyramus.rest.tranquil.help;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.help.HelpItem.class, entityType = TranquilModelType.COMPACT)
public class HelpItemEntity implements fi.tranquil.TranquilModelEntity {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public Integer getIndexColumn() {
    return indexColumn;
  }

  public void setIndexColumn(Integer indexColumn) {
    this.indexColumn = indexColumn;
  }

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

  public java.util.List<Long> getTitles_ids() {
    return titles_ids;
  }

  public void setTitles_ids(java.util.List<Long> titles_ids) {
    this.titles_ids = titles_ids;
  }

  public java.util.List<Long> getTags_ids() {
    return tags_ids;
  }

  public void setTags_ids(java.util.List<Long> tags_ids) {
    this.tags_ids = tags_ids;
  }

  private Long id;

  private java.util.Date created;

  private java.util.Date lastModified;

  private Integer indexColumn;

  private Long parent_id;

  private Long creator_id;

  private Long lastModifier_id;

  private java.util.List<Long> titles_ids;

  private java.util.List<Long> tags_ids;

  public final static String[] properties = {"id","created","lastModified","indexColumn","parent","creator","lastModifier","titles","tags"};
}
