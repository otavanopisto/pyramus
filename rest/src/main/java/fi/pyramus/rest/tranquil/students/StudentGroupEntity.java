package fi.pyramus.rest.tranquil.students;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.students.StudentGroup.class, entityType = TranquilModelType.COMPACT)
public class StudentGroupEntity implements fi.tranquil.TranquilModelEntity {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  public java.util.Date getBeginDate() {
    return beginDate;
  }

  public void setBeginDate(java.util.Date beginDate) {
    this.beginDate = beginDate;
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

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
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

  public java.util.List<Long> getStudents_ids() {
    return students_ids;
  }

  public void setStudents_ids(java.util.List<Long> students_ids) {
    this.students_ids = students_ids;
  }

  public java.util.List<Long> getUsers_ids() {
    return users_ids;
  }

  public void setUsers_ids(java.util.List<Long> users_ids) {
    this.users_ids = users_ids;
  }

  public java.util.List<Long> getTags_ids() {
    return tags_ids;
  }

  public void setTags_ids(java.util.List<Long> tags_ids) {
    this.tags_ids = tags_ids;
  }

  private Long id;

  private String name;

  private String description;

  private Boolean archived;

  private java.util.Date beginDate;

  private java.util.Date created;

  private java.util.Date lastModified;

  private Long version;

  private Long creator_id;

  private Long lastModifier_id;

  private java.util.List<Long> students_ids;

  private java.util.List<Long> users_ids;

  private java.util.List<Long> tags_ids;

  public final static String[] properties = {"id","name","description","archived","beginDate","created","lastModified","version","creator","lastModifier","students","users","tags"};
}
