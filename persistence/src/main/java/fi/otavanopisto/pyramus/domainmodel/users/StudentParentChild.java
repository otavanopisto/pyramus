package fi.otavanopisto.pyramus.domainmodel.users;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

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

  public boolean isContinuedViewPermission() {
    return continuedViewPermission;
  }

  public void setContinuedViewPermission(boolean continuedViewPermission) {
    this.continuedViewPermission = continuedViewPermission;
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

  private boolean continuedViewPermission;
  
}
