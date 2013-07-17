package fi.pyramus.rest.tranquil.students;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.students.StudentContactLogEntry.class, entityType = TranquilModelType.BASE)
public class StudentContactLogEntryBase implements fi.tranquil.TranquilModelEntity {

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

  public fi.pyramus.domainmodel.students.StudentContactLogEntryType getType() {
    return type;
  }

  public void setType(fi.pyramus.domainmodel.students.StudentContactLogEntryType type) {
    this.type = type;
  }

  public java.util.Date getEntryDate() {
    return entryDate;
  }

  public void setEntryDate(java.util.Date entryDate) {
    this.entryDate = entryDate;
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

  private fi.pyramus.domainmodel.students.StudentContactLogEntryType type;

  private java.util.Date entryDate;

  private String creatorName;

  private Boolean archived;

  private Long version;

  public final static String[] properties = {"id","text","type","entryDate","creatorName","archived","version"};
}
