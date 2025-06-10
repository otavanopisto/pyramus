package fi.otavanopisto.pyramus.domainmodel.matriculation;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.TableGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Version;

import fi.otavanopisto.pyramus.matriculation.MatriculationExamTerm;

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

  @Version
  @Column(nullable = false)
  private Long version;
}