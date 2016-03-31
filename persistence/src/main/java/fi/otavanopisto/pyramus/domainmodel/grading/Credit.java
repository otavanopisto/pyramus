package fi.otavanopisto.pyramus.domainmodel.grading;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceException;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import fi.otavanopisto.pyramus.domainmodel.base.ArchivableEntity;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class Credit implements ArchivableEntity {

  public Long getId() {
    return id;
  }
  
  public Date getDate() {
    return date;
  }
  
  public void setDate(Date date) {
    this.date = date;
  }
  
  public Grade getGrade() {
    return grade;
  }
  
  public void setGrade(Grade grade) {
    this.grade = grade;
  }
  
  public String getVerbalAssessment() {
    return verbalAssessment;
  }
  
  public void setVerbalAssessment(String verbalAssessment) {
    this.verbalAssessment = verbalAssessment;
  }
  
  public Boolean getArchived() {
    return archived;
  }
  
  public void setArchived(Boolean archived) {
    this.archived = archived;
  }
  
  public CreditType getCreditType() {
    return creditType;
  }
  
  protected void setCreditType(CreditType creditType) {
    this.creditType = creditType;
  }
  
  @SuppressWarnings("unused")
  private void setVersion(Long version) {
    this.version = version;
  }

  public Long getVersion() {
    return version;
  }

  @Transient
  public Student getStudent() {
    throw new PersistenceException("Credit.getStudent() not implemented");
  }
  
  public StaffMember getAssessor() {
    return assessor;
  }

  public void setAssessor(StaffMember assessor) {
    this.assessor = assessor;
  }

  @Id 
  @GeneratedValue(strategy=GenerationType.TABLE, generator="Credit")  
  @TableGenerator(name="Credit", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;
  
  @Column (nullable=false)
  @Temporal (value=TemporalType.TIMESTAMP)
  private Date date;
  
  @ManyToOne
  @JoinColumn(name="grade")
  private Grade grade;
  
  @Lob
  @Basic (fetch = FetchType.LAZY)
  @Column
  private String verbalAssessment;
  
  @ManyToOne  
  private StaffMember assessor;
  
  @Basic (optional = false)
  private Boolean archived = Boolean.FALSE;

  @Enumerated (EnumType.STRING)
  @Column (insertable = true, updatable = false, nullable = false)
  private CreditType creditType;

  @Version
  @Column(nullable = false)
  private Long version;
}
