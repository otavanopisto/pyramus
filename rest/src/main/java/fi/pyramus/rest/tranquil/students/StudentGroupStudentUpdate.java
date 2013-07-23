package fi.pyramus.rest.tranquil.students;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.students.StudentGroupStudent.class, entityType = TranquilModelType.UPDATE)
public class StudentGroupStudentUpdate extends StudentGroupStudentComplete {

  public void setStudentGroup(StudentGroupCompact studentGroup) {
    super.setStudentGroup(studentGroup);
  }

  public StudentGroupCompact getStudentGroup() {
    return (StudentGroupCompact)super.getStudentGroup();
  }

  public void setStudent(StudentCompact student) {
    super.setStudent(student);
  }

  public StudentCompact getStudent() {
    return (StudentCompact)super.getStudent();
  }

  public final static String[] properties = {"studentGroup","student"};
}
