package fi.pyramus.rest.tranquil.students;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.students.StudentGroupStudent.class, entityType = TranquilModelType.COMPLETE)
public class StudentGroupStudentComplete extends StudentGroupStudentBase {

  public TranquilModelEntity getStudentGroup() {
    return studentGroup;
  }

  public void setStudentGroup(TranquilModelEntity studentGroup) {
    this.studentGroup = studentGroup;
  }

  public TranquilModelEntity getStudent() {
    return student;
  }

  public void setStudent(TranquilModelEntity student) {
    this.student = student;
  }

  private TranquilModelEntity studentGroup;

  private TranquilModelEntity student;

  public final static String[] properties = {"studentGroup","student"};
}
