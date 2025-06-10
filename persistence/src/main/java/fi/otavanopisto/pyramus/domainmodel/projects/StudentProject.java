package fi.otavanopisto.pyramus.domainmodel.projects;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.automaticindexing.ReindexOnUpdate;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.DocumentId;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexingDependency;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;

import fi.otavanopisto.pyramus.domainmodel.base.ArchivableEntity;
import fi.otavanopisto.pyramus.domainmodel.base.CourseOptionality;
import fi.otavanopisto.pyramus.domainmodel.base.EducationalLength;
import fi.otavanopisto.pyramus.domainmodel.base.Tag;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TableGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Indexed
public class StudentProject implements ArchivableEntity {

  /**
   * Returns the unique identifier of this object.
   * 
   * @return The unique identifier of this object
   */
  public Long getId() {
    return id;
  }

  /**
   * Returns the name of this entity.
   * 
   * @return The name of this entity
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of this entity.
   * 
   * @param name The name of this entity
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the creator, and therefore the owner, of this entity.
   * 
   * @return The creator of this entity
   */
  public User getCreator() {
    return creator;
  }

  /**
   * Sets the creator of this entity.
   * 
   * @param creator The creator of this entity
   */
  public void setCreator(User creator) {
    this.creator = creator;
  }
  
  /**
   * Returns the creation time of this entity.
   * 
   * @return The creation time of this entity
   */
  public Date getCreated() {
    return created;
  }

  /**
   * Sets the creation time of this entity.
   * 
   * @param created The creation time of this entity
   */
  public void setCreated(Date created) {
    this.created = created;
  }
  
  /**
   * Returns the last modifier of this entity.
   * 
   * @return The last modifier of this entity
   */
  public User getLastModifier() {
    return lastModifier;
  }
  
  /**
   * Sets the last modifier of this entity.
   * 
   * @param lastModifier The last modifier of this entity
   */
  public void setLastModifier(User lastModifier) {
    this.lastModifier = lastModifier;
  }

  /**
   * Returns the last modification time of this entity.
   * 
   * @return The last modification time of this entity
   */
  public Date getLastModified() {
    return lastModified;
  }

  /**
   * Sets the last modification time of this entity.
   * 
   * @param created The last modification time of this entity
   */
  public void setLastModified(Date lastModified) {
    this.lastModified = lastModified;
  }

  /**
   * Sets the archived flag of this object.
   * 
   * @param archived The archived flag of this object
   */
  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  /**
   * Returns the archived flag of this object.
   * 
   * @return The archived flag of this object
   */
  public Boolean getArchived() {
    return archived;
  }

  /**
   * Sets the project description.
   * 
   * @param description The project description
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Returns the project description.
   * 
   * @return The project description
   */
  public String getDescription() {
    return description;
  }

  @SuppressWarnings("unused")
  private void setOptionalStudiesLength(EducationalLength optionalStudiesLength) {
    this.optionalStudiesLength = optionalStudiesLength;
  }

  public EducationalLength getOptionalStudiesLength() {
    return optionalStudiesLength;
  }

  public void setStudent(Student student) {
    this.student = student;
  }

  public Student getStudent() {
    return student;
  }

  public List<StudentProjectModule> getStudentProjectModules() {
    return studentProjectModules;
  }

  @SuppressWarnings("unused")
  private void setStudentProjectModules(List<StudentProjectModule> studentProjectModules) {
    this.studentProjectModules = studentProjectModules;
  }
  
  @Transient
  public void addStudentProjectModule(StudentProjectModule studentProjectModule) {
    if (!studentProjectModules.contains(studentProjectModule)) {
      if (studentProjectModule.getStudentProject() != null) {
        studentProjectModule.getStudentProject().removeStudentProjectModule(studentProjectModule);
      }
      studentProjectModule.setStudentProject(this);
      studentProjectModules.add(studentProjectModule);
    } else {
      throw new PersistenceException("StudentProject already contains this module");
    }
  }

  @Transient
  public void removeStudentProjectModule(StudentProjectModule studentProjectModule) {
    if (studentProjectModules.contains(studentProjectModule)) {
      studentProjectModule.setStudentProject(null);
      studentProjectModules.remove(studentProjectModule);
    } else {
      throw new PersistenceException("StudentProject does not contain this module");
    }
  }

  public List<StudentProjectSubjectCourse> getStudentProjectSubjectCourses() {
    return studentProjectSubjectCourses;
  }

  @SuppressWarnings("unused")
  private void setStudentProjectSubjectCourses(List<StudentProjectSubjectCourse> studentProjectSubjectCourses) {
    this.studentProjectSubjectCourses = studentProjectSubjectCourses;
  }
  
  public void addStudentProjectSubjectCourse(StudentProjectSubjectCourse studentProjectSubjectCourse) {
    if (studentProjectSubjectCourse.getStudentProject() != null) {
      studentProjectSubjectCourse.getStudentProject().removeStudentProjectSubjectCourse(studentProjectSubjectCourse);
    }
    studentProjectSubjectCourse.setStudentProject(this);
    studentProjectSubjectCourses.add(studentProjectSubjectCourse);
  }
  
  public void removeStudentProjectSubjectCourse(StudentProjectSubjectCourse studentProjectSubjectCourse) {
    studentProjectSubjectCourse.setStudentProject(null);
    studentProjectSubjectCourses.remove(studentProjectSubjectCourse);
  }

  public Set<Tag> getTags() {
    return tags;
  }
  
  public void setTags(Set<Tag> tags) {
    this.tags = tags;
  }
  
  public void addTag(Tag tag) {
    if (!tags.contains(tag)) {
      tags.add(tag);
    } else {
      throw new PersistenceException("Entity already has this tag");
    }
  }
  
  public void removeTag(Tag tag) {
    if (tags.contains(tag)) {
      tags.remove(tag);
    } else {
      throw new PersistenceException("Entity does not have this tag");
    }
  }
  
  @SuppressWarnings("unused")
  private void setVersion(Long version) {
    this.version = version;
  }

  public Long getVersion() {
    return version;
  }

  public void setProject(Project project) {
    this.project = project;
  }

  public Project getProject() {
    return project;
  }

  public void setOptionality(CourseOptionality optionality) {
    this.optionality = optionality;
  }

  public CourseOptionality getOptionality() {
    return optionality;
  }

  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="StudentProject")  
  @TableGenerator(name="StudentProject", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  @DocumentId 
  private Long id;

  @NotNull
  @Column (nullable = false)
  @NotEmpty
  @FullTextField
  @KeywordField(name = "name_sort", projectable = Projectable.NO, sortable = Sortable.YES)
  private String name;

  @Lob
  @Basic (fetch = FetchType.LAZY)
  @FullTextField
  private String description;

  @OneToOne (cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn (name = "optionalStudies")
  private EducationalLength optionalStudiesLength = new EducationalLength();
  
  @ManyToOne
  @JoinColumn (name = "student")
  @IndexedEmbedded
  @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.SHALLOW)
  private Student student;

  @OneToMany (cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderColumn (name = "indexColumn")
  @JoinColumn (name="studentProject")
  @IndexedEmbedded 
  private List<StudentProjectModule> studentProjectModules = new Vector<>();

  @OneToMany (cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn (name="studentProject")
  @IndexedEmbedded 
  @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.SHALLOW)
  private List<StudentProjectSubjectCourse> studentProjectSubjectCourses = new Vector<>();

  @ManyToOne (fetch = FetchType.LAZY)
  @JoinColumn(name="creator")
  @IndexedEmbedded (includeDepth = 1)
  @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.SHALLOW)
  private User creator;
  
  @Column (updatable=false, nullable=false)
  @Temporal (value=TemporalType.TIMESTAMP)
  private Date created;
  
  @ManyToOne (fetch = FetchType.LAZY)
  @JoinColumn(name="lastModifier")
  private User lastModifier;
  
  @Column (nullable=false)
  @Temporal (value=TemporalType.TIMESTAMP)
  private Date lastModified;

  @NotNull
  @Column(nullable = false)
  @GenericField
  private Boolean archived = Boolean.FALSE;

  @ManyToMany (fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable (name="__StudentProjectTags", joinColumns=@JoinColumn(name="studentProject"), inverseJoinColumns=@JoinColumn(name="tag"))
  @IndexedEmbedded 
  @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.SHALLOW)
  private Set<Tag> tags = new HashSet<>();
  
  @ManyToOne
  @JoinColumn(name="project")
  private Project project;

  @Column
  @Enumerated (EnumType.STRING)
  private CourseOptionality optionality;
  
  @Version
  @Column(nullable = false)
  private Long version;
}