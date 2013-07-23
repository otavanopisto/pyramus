package fi.pyramus.rest.tranquil.students;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.students.StudentGroup.class, entityType = TranquilModelType.COMPACT)
public class StudentGroupCompact extends StudentGroupBase {

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

  private Long creator_id;

  private Long lastModifier_id;

  private java.util.List<Long> students_ids;

  private java.util.List<Long> users_ids;

  private java.util.List<Long> tags_ids;

  public final static String[] properties = {"creator","lastModifier","students","users","tags"};
}
