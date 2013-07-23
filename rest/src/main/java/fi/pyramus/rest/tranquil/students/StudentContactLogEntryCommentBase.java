package fi.pyramus.rest.tranquil.students;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.students.StudentContactLogEntryComment.class, entityType = TranquilModelType.BASE)
public class StudentContactLogEntryCommentBase implements fi.tranquil.TranquilModelEntity {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public java.util.Date getCommentDate() {
    return commentDate;
  }

  public void setCommentDate(java.util.Date commentDate) {
    this.commentDate = commentDate;
  }

  public String getCreatorName() {
    return creatorName;
  }

  public void setCreatorName(String creatorName) {
    this.creatorName = creatorName;
  }

  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  private Long id;

  private String text;

  private java.util.Date commentDate;

  private String creatorName;

  private Boolean archived;

  private Long version;

  public final static String[] properties = {"id","text","commentDate","creatorName","archived","version"};
}
