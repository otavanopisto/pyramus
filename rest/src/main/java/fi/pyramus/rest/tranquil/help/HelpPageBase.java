package fi.pyramus.rest.tranquil.help;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.help.HelpPage.class, entityType = TranquilModelType.BASE)
public class HelpPageBase implements fi.tranquil.TranquilModelEntity {

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

  public java.util.Set<fi.pyramus.domainmodel.base.Tag> getTags() {
    return tags;
  }

  public void setTags(java.util.Set<fi.pyramus.domainmodel.base.Tag> tags) {
    this.tags = tags;
  }

  public String getRecursiveIndex() {
    return recursiveIndex;
  }

  public void setRecursiveIndex(String recursiveIndex) {
    this.recursiveIndex = recursiveIndex;
  }

  private Long id;

  private java.util.Date created;

  private java.util.Date lastModified;

  private Integer indexColumn;

  private java.util.Set<fi.pyramus.domainmodel.base.Tag> tags;

  private String recursiveIndex;

  public final static String[] properties = {"id","created","lastModified","indexColumn","tags","recursiveIndex"};
}
