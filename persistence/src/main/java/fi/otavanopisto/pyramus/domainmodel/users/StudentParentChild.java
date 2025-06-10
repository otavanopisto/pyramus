package fi.otavanopisto.pyramus.domainmodel.users;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import fi.otavanopisto.pyramus.domainmodel.students.Student;

@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(columnNames = { "studentParent", "student"})
    }
)
public class StudentParentChild {
  
  public Long getId() {
    return this.id;
  }

  public StudentParent getStudentParent() {
    return studentParent;
  }

  public void setStudentParent(StudentParent studentParent) {
    this.studentParent = studentParent;
  }

  public Student getStudent() {
    return student;
  }

  public void setStudent(Student student) {
    this.student = student;
  }

  @Id 
  @GeneratedValue(strategy = GenerationType.IDENTITY)  
  private Long id;

  @ManyToOne (optional = false)
  @JoinColumn(name = "studentParent")
  private StudentParent studentParent;

  @ManyToOne (optional = false)
  @JoinColumn(name = "student")
  private Student student;

}
