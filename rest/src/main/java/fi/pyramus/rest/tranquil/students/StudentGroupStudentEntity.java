package fi.pyramus.rest.tranquil.students;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.students.StudentGroupStudent.class, entityType = TranquilModelType.COMPACT)
public class StudentGroupStudentEntity implements fi.tranquil.TranquilModelEntity {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

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

  private Long id;

  private Long version;

  private Long studentGroup_id;

  private Long student_id;

  public final static String[] properties = {"id","version","studentGroup","student"};
}
