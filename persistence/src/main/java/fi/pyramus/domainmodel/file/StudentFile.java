package fi.pyramus.domainmodel.file;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import fi.pyramus.domainmodel.students.Student;

@Entity
@PrimaryKeyJoinColumn(name="id")
public class StudentFile extends File {

  public void setStudent(Student student) {
    this.student = student;
  }

  public Student getStudent() {
    return student;
  }

  @ManyToOne
  @JoinColumn(name="student")
  private Student student;
}
