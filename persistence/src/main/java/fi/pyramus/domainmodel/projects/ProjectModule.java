package fi.pyramus.domainmodel.projects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import fi.pyramus.domainmodel.modules.Module;

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
