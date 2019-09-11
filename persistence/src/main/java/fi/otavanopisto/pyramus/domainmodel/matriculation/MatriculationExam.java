package fi.otavanopisto.pyramus.domainmodel.matriculation;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import fi.otavanopisto.pyramus.domainmodel.grading.Grade;

@Entity
public class MatriculationExam {

  /**
   * Returns the unique identifier of this object.
   * 
   * @return The unique identifier of this object
   */
  public Long getId() {
    return id;
  }
  
  public Date getStarts() {
    return starts;
  }

  public void setStarts(Date starts) {
    this.starts = starts;
  }

  public Date getEnds() {
    return ends;
  }

  public void setEnds(Date ends) {
    this.ends = ends;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public Grade getSignupGrade() {
    return signupGrade;
  }

  public void setSignupGrade(Grade signupGrade) {
    this.signupGrade = signupGrade;
  }

  public Integer getExamYear() {
    return examYear;
  }

  public void setExamYear(Integer examYear) {
    this.examYear = examYear;
  }

  public MatriculationExamTerm getExamTerm() {
    return examTerm;
  }

  public void setExamTerm(MatriculationExamTerm examTerm) {
    this.examTerm = examTerm;
  }

  public boolean isEnrollmentActive() {
    return enrollmentActive;
  }

  public void setEnrollmentActive(boolean enrollmentActive) {
    this.enrollmentActive = enrollmentActive;
  }

  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="MatriculationExam")  
  @TableGenerator(name="MatriculationExam", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;
  
  @Column(nullable = false)
  private boolean enrollmentActive;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false)
  private Date starts;
  
  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false)
  private Date ends;

  @Column
  private Integer examYear;
  
  @Column
  @Enumerated(EnumType.STRING)
  private MatriculationExamTerm examTerm;

  @ManyToOne
  private Grade signupGrade;
  
  @Version
  @Column(nullable = false)
  private Long version;
}