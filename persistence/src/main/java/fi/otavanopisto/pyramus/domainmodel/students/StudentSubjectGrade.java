package fi.otavanopisto.pyramus.domainmodel.students;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.grading.Grade;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;

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

  public String getExplanation() {
    return explanation;
  }

  public void setExplanation(String explanation) {
    this.explanation = explanation;
  }

  public StaffMember getIssuer() {
    return issuer;
  }

  public void setIssuer(StaffMember issuer) {
    this.issuer = issuer;
  }

  public Date getGradeDate() {
    return gradeDate;
  }

  public void setGradeDate(Date gradeDate) {
    this.gradeDate = gradeDate;
  }

  public StaffMember getGradeApprover() {
    return gradeApprover;
  }

  public void setGradeApprover(StaffMember gradeApprover) {
    this.gradeApprover = gradeApprover;
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
  @JoinColumn (name = "issuer")
  private StaffMember issuer;
  
  @ManyToOne (optional = false)
  @JoinColumn (name = "grade")
  private Grade grade;
  
  @Temporal (value=TemporalType.DATE)
  private Date gradeDate;
  
  @ManyToOne
  @JoinColumn(name="gradeApprover")
  private StaffMember gradeApprover;

  @Lob
  @Basic(fetch = FetchType.LAZY)
  @Column
  private String explanation;
}
