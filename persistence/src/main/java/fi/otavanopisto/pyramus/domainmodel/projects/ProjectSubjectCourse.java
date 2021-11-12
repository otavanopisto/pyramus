package fi.otavanopisto.pyramus.domainmodel.projects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.IndexedEmbedded;

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
  @Field
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
