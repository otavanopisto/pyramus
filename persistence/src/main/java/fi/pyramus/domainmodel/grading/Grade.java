package fi.pyramus.domainmodel.grading;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import fi.pyramus.domainmodel.base.ArchivableEntity;

@Entity
public class Grade implements ArchivableEntity {

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }
  
  public void setDescription(String description) {
    this.description = description;
  }
  
  public Boolean getPassingGrade() {
    return passingGrade;
  }

  public void setPassingGrade(Boolean passingGrade) {
    this.passingGrade = passingGrade;
  }

  public String getQualification() {
    return qualification;
  }

  public void setQualification(String qualification) {
    this.qualification = qualification;
  }

  public Double getGPA() {
    return GPA;
  }

  public void setGPA(Double GPA) {
    this.GPA = GPA;
  }
  
  public GradingScale getGradingScale() {
    return gradingScale;
  }
  
  public void setGradingScale(GradingScale gradingScale) {
    this.gradingScale = gradingScale;
  }

  public Boolean getArchived() {
    return archived;
  }
  
  public void setArchived(Boolean archived) {
    this.archived = archived;
  }
  
  @SuppressWarnings("unused")
  private void setVersion(Long version) {
    this.version = version;
  }

  public Long getVersion() {
    return version;
  }

  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="Grade")  
  @TableGenerator(name="Grade", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;

  @NotNull
  @Column(nullable = false)
  @NotEmpty
  private String name;
  
  private String description;
  
  @ManyToOne
  @JoinColumn(name = "gradingScale")
  private GradingScale gradingScale;

  @NotNull
  @Column(nullable = false)
  private Boolean passingGrade;
  
  @NotNull
  @Column (nullable = false)
  private Boolean archived = Boolean.FALSE;

  private String qualification;
  private Double GPA;

  @Version
  @Column(nullable = false)
  private Long version;
}