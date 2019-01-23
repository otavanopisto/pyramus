package fi.otavanopisto.pyramus.domainmodel.students;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.grading.Grade;

@Entity
public class StudentSubjectGrade {
  
  public Long getId() {
    return id;
  }
  
  public Student getStudent() {
    return student;
  }

  public void setStudent(Student student) {
    this.student = student;
  }

  public Subject getSubject() {
    return subject;
  }

  public void setSubject(Subject subject) {
    this.subject = subject;
  }

  public Grade getGrade() {
    return grade;
  }

  public void setGrade(Grade grade) {
    this.grade = grade;
  }

  @Id
  @GeneratedValue (strategy = GenerationType.IDENTITY)
  private Long id; 
  
  @ManyToOne (optional = false)
  @JoinColumn (name = "student")
  private Student student;
  
  @ManyToOne (optional = false)
  @JoinColumn (name = "subject")
  private Subject subject;

  @ManyToOne (optional = false)
  @JoinColumn (name = "grade")
  private Grade grade;
}
