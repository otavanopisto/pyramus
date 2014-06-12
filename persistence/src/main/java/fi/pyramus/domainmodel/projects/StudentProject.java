package fi.pyramus.domainmodel.projects;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceException;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.IndexColumn;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FullTextFilterDef;
import org.hibernate.search.annotations.FullTextFilterDefs;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.NotEmpty;

import fi.pyramus.domainmodel.base.ArchivableEntity;
import fi.pyramus.domainmodel.base.CourseOptionality;
import fi.pyramus.domainmodel.base.EducationalLength;
import fi.pyramus.domainmodel.base.Tag;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.persistence.search.filters.ArchivedEntityFilterFactory;

@Entity
@Indexed
@FullTextFilterDefs (
  @FullTextFilterDef (
     name="ArchivedStudentProject",
     impl=ArchivedEntityFilterFactory.class
  )
)
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
  
  @Transient
  @Field (analyze = Analyze.NO, store = Store.YES)
  public String getNameSortable() {
    return name;
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
  @Field
  private String name;

  @Lob
  @Basic (fetch = FetchType.LAZY)
  @Column
  @Field
  private String description;

  @OneToOne (cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn (name = "optionalStudies")
  private EducationalLength optionalStudiesLength = new EducationalLength();
  
  @ManyToOne
  @JoinColumn (name = "student")
  @IndexedEmbedded
  private Student student;

  @OneToMany (cascade = CascadeType.ALL, orphanRemoval = true)
  @IndexColumn (name = "indexColumn")
  @JoinColumn (name="studentProject")
  @IndexedEmbedded 
  private List<StudentProjectModule> studentProjectModules = new Vector<StudentProjectModule>();

  @ManyToOne 
  @JoinColumn(name="creator")
  @IndexedEmbedded (depth = 1)
  private User creator;
  
  @Column (updatable=false, nullable=false)
  @Temporal (value=TemporalType.TIMESTAMP)
  private Date created;
  
  @ManyToOne  
  @JoinColumn(name="lastModifier")
  private User lastModifier;
  
  @Column (nullable=false)
  @Temporal (value=TemporalType.TIMESTAMP)
  private Date lastModified;

  @NotNull
  @Column(nullable = false)
  @Field
  private Boolean archived = Boolean.FALSE;

  @ManyToMany (fetch = FetchType.LAZY)
  @JoinTable (name="__StudentProjectTags", joinColumns=@JoinColumn(name="studentProject"), inverseJoinColumns=@JoinColumn(name="tag"))
  @IndexedEmbedded 
  private Set<Tag> tags = new HashSet<Tag>();
  
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