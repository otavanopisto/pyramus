package fi.pyramus.rest.tranquil.students;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.students.StudentGroupUser.class, entityType = TranquilModelType.UPDATE)
public class StudentGroupUserUpdate extends StudentGroupUserComplete {

  public void setStudentGroup(StudentGroupCompact studentGroup) {
    super.setStudentGroup(studentGroup);
  }

  public StudentGroupCompact getStudentGroup() {
    return (StudentGroupCompact)super.getStudentGroup();
  }

  public void setUser(UserCompact user) {
    super.setUser(user);
  }

  public UserCompact getUser() {
    return (UserCompact)super.getUser();
  }

  public final static String[] properties = {"studentGroup","user"};
}
