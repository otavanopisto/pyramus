package fi.pyramus.rest.tranquil.students;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.students.StudentGroupUser.class, entityType = TranquilModelType.COMPACT)
public class StudentGroupUserCompact extends StudentGroupUserBase {

  public Long getStudentGroup_id() {
    return studentGroup_id;
  }

  public void setStudentGroup_id(Long studentGroup_id) {
    this.studentGroup_id = studentGroup_id;
  }

  public Long getUser_id() {
    return user_id;
  }

  public void setUser_id(Long user_id) {
    this.user_id = user_id;
  }

  private Long studentGroup_id;

  private Long user_id;

  public final static String[] properties = {"studentGroup","user"};
}
