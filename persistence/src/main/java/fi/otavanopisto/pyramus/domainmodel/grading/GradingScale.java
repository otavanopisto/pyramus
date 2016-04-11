package fi.otavanopisto.pyramus.domainmodel.grading;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.IndexColumn;
import org.hibernate.validator.constraints.NotEmpty;

import fi.otavanopisto.pyramus.domainmodel.base.ArchivableEntity;

@Entity
public class GradingScale implements ArchivableEntity {
  
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

  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  public List<Grade> getGrades() {
    return grades;
  }

  public void setGrades(List<Grade> grades) {
    this.grades = grades;
  }
  
  public void addGrade(Grade grade) {
    if (grade.getGradingScale() != null)
      grade.getGradingScale().removeGrade(grade);
    grade.setGradingScale(this);
    grades.add(grade);
  }
  
  public void removeGrade(Grade grade) {
    grades.remove(grade);
    grade.setGradingScale(null);
  }

  @SuppressWarnings("unused")
  private void setVersion(Long version) {
    this.version = version;
  }

  public Long getVersion() {
    return version;
  }

  @Id 
  @GeneratedValue(strategy=GenerationType.TABLE, generator="GradingScale")  
  @TableGenerator(name="GradingScale", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;
  
  @NotNull
  @Column (nullable = false)
  @NotEmpty
  private String name;
  
  @Lob
  @Basic (fetch = FetchType.LAZY)
  @Column
  private String description;
  
  private Boolean archived = Boolean.FALSE;
  
  @NotNull
  @Column (nullable = false)
  @OneToMany (cascade = CascadeType.ALL, orphanRemoval = true)
  @IndexColumn (name = "indexColumn")
  @JoinColumn (name="gradingScale")
  private List<Grade> grades = new ArrayList<>(); 

  @Version
  @Column(nullable = false)
  private Long version;
}
