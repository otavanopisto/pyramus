package fi.otavanopisto.pyramus.domainmodel.grading;

import java.util.Date;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TableGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;

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
  private String verbalAssessment;
  
  @ManyToOne  
  private StaffMember assessor;
  
  @NotNull
  @Column (nullable = false)
  private Boolean archived;

  @Enumerated (EnumType.STRING)
  @Column (insertable = true, updatable = false, nullable = false)
  private CreditType creditType;

  @Version
  @Column(nullable = false)
  private Long version;
}
