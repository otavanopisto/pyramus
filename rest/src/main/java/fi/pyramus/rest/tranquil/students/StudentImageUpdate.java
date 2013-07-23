package fi.pyramus.rest.tranquil.students;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.students.StudentImage.class, entityType = TranquilModelType.UPDATE)
public class StudentImageUpdate extends StudentImageComplete {

  public void setStudent(StudentCompact student) {
    super.setStudent(student);
  }

  public StudentCompact getStudent() {
    return (StudentCompact)super.getStudent();
  }

  public final static String[] properties = {"student"};
}
