package fi.otavanopisto.pyramus.domainmodel.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Version;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
public class StudyProgrammeCategory implements ArchivableEntity {

  public Long getId() {
    return id;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  public Boolean getArchived() {
    return archived;
  }

  @SuppressWarnings("unused")
  private void setVersion(Long version) {
    this.version = version;
  }

  public Long getVersion() {
    return version;
  }

  public void setEducationType(EducationType educationType) {
    this.educationType = educationType;
  }

  public EducationType getEducationType() {
    return educationType;
  }

  @Id 
  @GeneratedValue(strategy=GenerationType.TABLE, generator="StudyProgrammeCategory")  
  @TableGenerator(name="StudyProgrammeCategory", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;
  
  @NotNull
  @Column (nullable = false)
  @NotEmpty
  private String name;
  
  @NotNull
  @Column(nullable = false)
  private Boolean archived = Boolean.FALSE;

  @ManyToOne
  @JoinColumn(name="educationType")
  private EducationType educationType;
  
  @Version
  @Column(nullable = false)
  private Long version;
}
