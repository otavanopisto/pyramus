package fi.otavanopisto.pyramus.domainmodel.matriculation;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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
  
  public void setId(Long id) {
    this.id = id;
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

  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="MatriculationExam")  
  @TableGenerator(name="MatriculationExam", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false)
  private Date starts;
  
  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false)
  private Date ends;

  @ManyToOne
  private Grade signupGrade;
  
  /* Version required because we persist an entity with a set id, to get rid
   * of duplication
   */
  @Version
  @Column(nullable = false)
  private Long version;
}