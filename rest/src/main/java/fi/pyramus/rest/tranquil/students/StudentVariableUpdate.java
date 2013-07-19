package fi.pyramus.rest.tranquil.students;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.students.StudentVariable.class, entityType = TranquilModelType.UPDATE)
public class StudentVariableUpdate extends StudentVariableComplete {

  public void setStudent(StudentCompact student) {
    super.setStudent(student);
  }

  public StudentCompact getStudent() {
    return (StudentCompact)super.getStudent();
  }

  public void setKey(StudentVariableKeyCompact key) {
    super.setKey(key);
  }

  public StudentVariableKeyCompact getKey() {
    return (StudentVariableKeyCompact)super.getKey();
  }

  public final static String[] properties = {"student","key"};
}
