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
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;

import fi.otavanopisto.pyramus.domainmodel.base.Subject;

@Entity
public class ProjectSubjectCourse {

  /**
   * Returns the unique identifier of this object.
   * 
   * @return The unique identifier of this object
   */
  public Long getId() {
    return id;
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

  public Subject getSubject() {
    return subject;
  }

  public void setSubject(Subject subject) {
    this.subject = subject;
  }

  public Integer getCourseNumber() {
    return courseNumber;
  }

  public void setCourseNumber(Integer courseNumber) {
    this.courseNumber = courseNumber;
  }

  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)  
  private Long id;
  
  @ManyToOne  
  @JoinColumn(name="subject")
  @IndexedEmbedded(includeEmbeddedObjectId = true)
  private Subject subject;

  @Column
  @GenericField
  private Integer courseNumber;
  
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
