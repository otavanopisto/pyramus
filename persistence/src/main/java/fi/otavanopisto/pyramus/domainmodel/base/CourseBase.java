package fi.otavanopisto.pyramus.domainmodel.base;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.hibernate.HibernateException;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.SortableField;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.NotEmpty;

import fi.otavanopisto.pyramus.domainmodel.users.User;

/**
 * Base entity for both modules and courses, modeling the fields that are mutual to both.  
 */
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class CourseBase implements ArchivableEntity {

  /**
   * Returns the identifier of this entity.
   * 
   * @return The identifier of this entity
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
   * Returns the description of this entity.
   * 
   * @return The description of this entity
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the description of this entity.
   * 
   * @param description The description of this entity
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Returns the course education types of this entity.
   * 
   * @return The course education types of this entity
   */
  public List<CourseEducationType> getCourseEducationTypes() {
    return courseEducationTypes;
  }

  /**
   * Sets the course education types of this entity.
   * 
   * @param courseEducationTypes The course education types of this entity
   */
  @SuppressWarnings("unused")
  private void setCourseEducationTypes(List<CourseEducationType> courseEducationTypes) {
    this.courseEducationTypes = courseEducationTypes;
  }
  
  /**
   * Adds a course education type to this entity.
   * 
   * @param courseEducationType The course education type to be added to this entity
   */
  public void addCourseEducationType(CourseEducationType courseEducationType) {
    if (courseEducationType.getCourseBase() != null && courseEducationType.getCourseBase() != this) {
      throw new HibernateException("Course base mismatch");
    }
    courseEducationType.setCourseBase(this);
    this.courseEducationTypes.add(courseEducationType);
  }
  
  /**
   * Removes a course education type from this entity.
   * 
   * @param courseEducationType The course education type to be removed from this entity
   */
  public void removeCourseEducationType(CourseEducationType courseEducationType) {
    courseEducationType.setCourseBase(null);
    this.courseEducationTypes.remove(courseEducationType);
  }
  
  /**
   * Returns whether this course base contains the given education type.
   * 
   * @param educationType The education type to be tested
   * 
   * @return <code>true</code> if the given education type exists in this course base, otherwise <code>false</code>
   */
  public boolean contains(EducationType educationType) {
    return getCourseEducationTypeByEducationTypeId(educationType.getId()) != null;
  }
  
  /**
   * Returns the course education type corresponding to the given education type identifier. If such
   * instance cannot be found, returns <code>null</code>.
   *  
   * @param educationTypeId Education type identifier
   * 
   * @return Course education type corresponding to the given education type identifier, or <code>null</code> if not found
   */
  public CourseEducationType getCourseEducationTypeByEducationTypeId(Long educationTypeId) {
    for (CourseEducationType courseEducationType : courseEducationTypes) { 
      if (courseEducationType.getEducationType().getId().equals(educationTypeId)) {
        return courseEducationType;
      }
    }
    return null;
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
  
  public List<CourseBaseVariable> getVariables() {
    return variables;
  }
  
  public void setVariables(List<CourseBaseVariable> variables) {
    this.variables = variables;
  }
  
  @Transient
  @Field (analyze = Analyze.NO, store = Store.NO)
  @SortableField
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

  public void setMaxParticipantCount(Long maxParticipantCount) {
    this.maxParticipantCount = maxParticipantCount;
  }

  public Long getMaxParticipantCount() {
    return maxParticipantCount;
  }

  public Set<Curriculum> getCurriculums() {
    return curriculums;
  }
  
  public void setCurriculums(Set<Curriculum> curriculums) {
    this.curriculums = curriculums;
  }
  
  public void addCourseModule(CourseModule courseModule) {
    if (courseModule.getCourse() != this) {
      courseModule.getCourse().removeCourseModule(courseModule);
      courseModule.setCourse(this);
    }
    courseModules.add(courseModule);
  }
  
  public boolean removeCourseModule(CourseModule courseModule) {
    if (courseModules.contains(courseModule)) {
      courseModule.setCourse(null);
      courseModules.remove(courseModule);
      return true;
    }
    
    return false;
  }

  public List<CourseModule> getCourseModules() {
    return courseModules;
  }

  @SuppressWarnings("unused")
  private void setCourseModules(List<CourseModule> courseModules) {
    this.courseModules = courseModules;
  }

  @Id
  @DocumentId
  @GeneratedValue(strategy=GenerationType.TABLE, generator="CourseBase")  
  @TableGenerator(name="CourseBase", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;
  
  @NotNull
  @Column (nullable = false)
  @NotEmpty
  @Field
  private String name;
  
  @ManyToOne 
  @JoinColumn(name="creator")
  private User creator;
  
  @NotNull
  @Column (updatable=false, nullable=false)
  @Temporal (value=TemporalType.TIMESTAMP)
  private Date created;
  
  @ManyToOne  
  @JoinColumn(name="lastModifier")
  private User lastModifier;
  
  @NotNull
  @Column (nullable=false)
  @Temporal (value=TemporalType.TIMESTAMP)
  private Date lastModified;
  
  @Lob
  @Basic (fetch = FetchType.LAZY)
  @Column
  @Field
  private String description;
  
  @OneToMany (cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn (name="courseBase")
  @IndexedEmbedded(includeEmbeddedObjectId = true)
  private List<CourseEducationType> courseEducationTypes = new Vector<>();

  @NotNull
  @Column(nullable = false)
  @Field
  private Boolean archived = Boolean.FALSE;
  
  @OneToMany (cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn (name="courseBase")
  private List<CourseBaseVariable> variables = new ArrayList<>();

  @Column
  private Long maxParticipantCount;

  @Version
  @Column(nullable = false)
  private Long version;
  
  @ManyToMany (fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable (name = "__CourseBaseCurriculums", joinColumns = @JoinColumn(name = "courseBase"), inverseJoinColumns = @JoinColumn(name = "curriculum"))
  @IndexedEmbedded(includeEmbeddedObjectId = true) 
  private Set<Curriculum> curriculums = new HashSet<>();

  @OneToMany
  @JoinColumn (name = "course", updatable = false, insertable = false)
  @IndexedEmbedded(includeEmbeddedObjectId = true)
  private List<CourseModule> courseModules = new Vector<>();
}