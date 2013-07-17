package fi.pyramus.rest.tranquil.students;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.students.StudentGroupStudent.class, entityType = TranquilModelType.COMPACT)
public class StudentGroupStudentCompact extends StudentGroupStudentBase {

  public Long getStudentGroup_id() {
    return studentGroup_id;
  }

  public void setStudentGroup_id(Long studentGroup_id) {
    this.studentGroup_id = studentGroup_id;
  }

  public Long getStudent_id() {
    return student_id;
  }

  public void setStudent_id(Long student_id) {
    this.student_id = student_id;
  }

  private Long studentGroup_id;

  private Long student_id;

  public final static String[] properties = {"studentGroup","student"};
}
