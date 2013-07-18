package fi.pyramus.rest.tranquil.students;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.students.AbstractStudent.class, entityType = TranquilModelType.COMPLETE)
public class AbstractStudentComplete extends AbstractStudentBase {

  public java.util.List<TranquilModelEntity> getStudents() {
    return students;
  }

  public void setStudents(java.util.List<TranquilModelEntity> students) {
    this.students = students;
  }

  private java.util.List<TranquilModelEntity> students;

  public final static String[] properties = {"students"};
}
