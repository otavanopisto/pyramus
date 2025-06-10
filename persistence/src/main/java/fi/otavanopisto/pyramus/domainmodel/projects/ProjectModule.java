package fi.otavanopisto.pyramus.domainmodel.projects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.TableGenerator;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;

import fi.otavanopisto.pyramus.domainmodel.modules.Module;

@Entity
public class ProjectModule {

  /**
   * Returns the unique identifier of this object.
   * 
   * @return The unique identifier of this object
   */
  public Long getId() {
    return id;
  }

  public Module getModule() {
    return module;
  }
  
  public void setModule(Module module) {
    this.module = module;
  }

  public void setProject(Project project) {
    this.project = project;
  }

  public Project getProject() {
    return project;
  }

  public void setOptionality(ProjectModuleOptionality optionality) {
    this.optionality = optionality;
  }

  public ProjectModuleOptionality getOptionality() {
    return optionality;
  }

  @SuppressWarnings("unused")
  private void setVersion(Long version) {
    this.version = version;
  }

  public Long getVersion() {
    return version;
  }

  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="ProjectModule")  
  @TableGenerator(name="ProjectModule", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;
  
  @ManyToOne
  @JoinColumn(name="module")
  private Module module;

  @ManyToOne  
  @JoinColumn(name="project")
  private Project project;

  @NotNull
  @Column (nullable = false)
  @Enumerated (EnumType.STRING)
  private ProjectModuleOptionality optionality;

  @Version
  @Column(nullable = false)
  private Long version;
}
