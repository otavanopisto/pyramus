package fi.pyramus.rest.tranquil.students;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.students.StudentGroup.class, entityType = TranquilModelType.UPDATE)
public class StudentGroupUpdate extends StudentGroupComplete {

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

  public void setStudents(java.util.List<StudentGroupStudentCompact> students) {
    super.setStudents(students);
  }

  public java.util.List<StudentGroupStudentCompact> getStudents() {
    return (java.util.List<StudentGroupStudentCompact>)super.getStudents();
  }

  public void setUsers(java.util.List<StudentGroupUserCompact> users) {
    super.setUsers(users);
  }

  public java.util.List<StudentGroupUserCompact> getUsers() {
    return (java.util.List<StudentGroupUserCompact>)super.getUsers();
  }

  public void setTags(java.util.List<TagCompact> tags) {
    super.setTags(tags);
  }

  public java.util.List<TagCompact> getTags() {
    return (java.util.List<TagCompact>)super.getTags();
  }

  public final static String[] properties = {"creator","lastModifier","students","users","tags"};
}
