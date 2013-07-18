package fi.pyramus.rest.tranquil.students;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.students.StudentGroup.class, entityType = TranquilModelType.COMPLETE)
public class StudentGroupComplete extends StudentGroupBase {

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

  public java.util.List<TranquilModelEntity> getStudents() {
    return students;
  }

  public void setStudents(java.util.List<TranquilModelEntity> students) {
    this.students = students;
  }

  public java.util.List<TranquilModelEntity> getUsers() {
    return users;
  }

  public void setUsers(java.util.List<TranquilModelEntity> users) {
    this.users = users;
  }

  public java.util.List<TranquilModelEntity> getTags() {
    return tags;
  }

  public void setTags(java.util.List<TranquilModelEntity> tags) {
    this.tags = tags;
  }

  private TranquilModelEntity creator;

  private TranquilModelEntity lastModifier;

  private java.util.List<TranquilModelEntity> students;

  private java.util.List<TranquilModelEntity> users;

  private java.util.List<TranquilModelEntity> tags;

  public final static String[] properties = {"creator","lastModifier","students","users","tags"};
}
