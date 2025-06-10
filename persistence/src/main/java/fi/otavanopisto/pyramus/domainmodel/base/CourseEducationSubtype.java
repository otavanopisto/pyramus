package fi.otavanopisto.pyramus.domainmodel.base;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.TableGenerator;
import jakarta.persistence.Version;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;


/**
 * Representation of an education subtype associated with a course base.
 */
@Entity
public class CourseEducationSubtype {

  /**
   * Default constructor.
   */
  protected CourseEducationSubtype() {
  };

  /**
   * Constructor specifying the education subtype of this object.
   * 
   * @param educationSubtype The education subtype of this object
   */
  public CourseEducationSubtype(EducationSubtype educationSubtype) {
    this.educationSubtype = educationSubtype;
  };

  /**
   * Returns the unique identifier of this object.
   * 
   * @return The unique identifier of this object
   */
  public Long getId() {
    return id;
  }

  /**
   * Returns the course education type of this object.
   * 
   * @return The course education type of this object
   */
  public CourseEducationType getCourseEducationType() {
    return courseEducationType;
  }

  /**
   * Sets the module education type of this object.
   * 
   * @param courseEducationType The module education type of this object
   */
  protected void setCourseEducationType(CourseEducationType courseEducationType) {
    this.courseEducationType = courseEducationType;
  }

  /**
   * Returns the education subtype of this object.
   * 
   * @return The education subtype of this object
   */
  public EducationSubtype getEducationSubtype() {
    return educationSubtype;
  }

  /**
   * Sets the education subtype of this object.
   * 
   * @param educationSubtype The education subtype of this object
   */
  protected void setEducationSubtype(EducationSubtype educationSubtype) {
    this.educationSubtype = educationSubtype;
  }

  @SuppressWarnings("unused")
  private void setVersion(Long version) {
    this.version = version;
  }

  public Long getVersion() {
    return version;
  }

  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="CourseEducationSubtype")  
  @TableGenerator(name="CourseEducationSubtype", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "courseEducationType")
  private CourseEducationType courseEducationType;

  @ManyToOne
  @JoinColumn(name = "educationSubtype", updatable = false)
  @IndexedEmbedded(includeEmbeddedObjectId = true)
  private EducationSubtype educationSubtype;

  @Version
  @Column(nullable = false)
  private Long version;
}
