package fi.otavanopisto.pyramus.domainmodel.base;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
   * Returns the subject of this entity.
   * 
   * @return The subject of this entity
   */
  public Subject getSubject() {
    return subject;
  }
  
  /**
   * Sets the subject of this entity.
   * 
   * @param subject The subject of this entity
   */
  public void setSubject(Subject subject) {
    this.subject = subject;
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
   * Sets the course length of this course base.
   * 
   * @param courseLength The course length of this course base
   */
  public void setCourseLength(EducationalLength courseLength) {
    this.courseLength = courseLength;
  }

  /**
   * Returns the course length of this course base.
   * 
   * @return The course length of this course base
   */
  public EducationalLength getCourseLength() {
    return courseLength;
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
  
  public void setCourseNumber(Integer courseNumber) {
    this.courseNumber = courseNumber;
  }

  public Integer getCourseNumber() {
    return courseNumber;
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

  public Curriculum getCurriculum() {
    return curriculum;
  }

  public void setCurriculum(Curriculum curriculum) {
    this.curriculum = curriculum;
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
  
  @ManyToOne  
  @JoinColumn(name="subject")
  @IndexedEmbedded
  private Subject subject;

  @Column
  @Field
  private Integer courseNumber;
  
  @OneToOne (cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn (name = "courseLength")
  private EducationalLength courseLength;
  
  @OneToMany (cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn (name="courseBase")
  @IndexedEmbedded
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
  
  @ManyToOne
  private Curriculum curriculum;
}