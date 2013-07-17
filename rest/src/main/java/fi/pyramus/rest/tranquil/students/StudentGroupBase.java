package fi.pyramus.rest.tranquil.students;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.students.StudentGroup.class, entityType = TranquilModelType.BASE)
public class StudentGroupBase implements fi.tranquil.TranquilModelEntity {

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

  public java.util.Set<fi.pyramus.domainmodel.students.StudentGroupStudent> getStudents() {
    return students;
  }

  public void setStudents(java.util.Set<fi.pyramus.domainmodel.students.StudentGroupStudent> students) {
    this.students = students;
  }

  public java.util.Set<fi.pyramus.domainmodel.students.StudentGroupUser> getUsers() {
    return users;
  }

  public void setUsers(java.util.Set<fi.pyramus.domainmodel.students.StudentGroupUser> users) {
    this.users = users;
  }

  public java.util.Date getBeginDate() {
    return beginDate;
  }

  public void setBeginDate(java.util.Date beginDate) {
    this.beginDate = beginDate;
  }

  public java.util.Set<fi.pyramus.domainmodel.base.Tag> getTags() {
    return tags;
  }

  public void setTags(java.util.Set<fi.pyramus.domainmodel.base.Tag> tags) {
    this.tags = tags;
  }

  public String getNameSortable() {
    return nameSortable;
  }

  public void setNameSortable(String nameSortable) {
    this.nameSortable = nameSortable;
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

  private Long id;

  private String name;

  private String description;

  private Boolean archived;

  private java.util.Set<fi.pyramus.domainmodel.students.StudentGroupStudent> students;

  private java.util.Set<fi.pyramus.domainmodel.students.StudentGroupUser> users;

  private java.util.Date beginDate;

  private java.util.Set<fi.pyramus.domainmodel.base.Tag> tags;

  private String nameSortable;

  private java.util.Date created;

  private java.util.Date lastModified;

  private Long version;

  public final static String[] properties = {"id","name","description","archived","students","users","beginDate","tags","nameSortable","created","lastModified","version"};
}
