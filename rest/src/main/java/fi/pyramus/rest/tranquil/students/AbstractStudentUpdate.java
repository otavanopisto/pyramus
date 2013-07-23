package fi.pyramus.rest.tranquil.students;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.students.AbstractStudent.class, entityType = TranquilModelType.UPDATE)
public class AbstractStudentUpdate extends AbstractStudentComplete {

  public void setStudents(java.util.List<StudentCompact> students) {
    super.setStudents(students);
  }

  public java.util.List<StudentCompact> getStudents() {
    return (java.util.List<StudentCompact>)super.getStudents();
  }

  public final static String[] properties = {"students"};
}
