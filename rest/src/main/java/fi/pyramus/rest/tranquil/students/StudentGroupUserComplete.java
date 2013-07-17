package fi.pyramus.rest.tranquil.students;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.students.StudentGroupUser.class, entityType = TranquilModelType.COMPLETE)
public class StudentGroupUserComplete extends StudentGroupUserBase {

  public TranquilModelEntity getStudentGroup() {
    return studentGroup;
  }

  public void setStudentGroup(TranquilModelEntity studentGroup) {
    this.studentGroup = studentGroup;
  }

  public TranquilModelEntity getUser() {
    return user;
  }

  public void setUser(TranquilModelEntity user) {
    this.user = user;
  }

  private TranquilModelEntity studentGroup;

  private TranquilModelEntity user;

  public final static String[] properties = {"studentGroup","user"};
}
