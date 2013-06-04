package fi.pyramus.domainmodel.base;

import java.util.List;
import java.util.Vector;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;
import javax.persistence.Version;

import org.hibernate.search.annotations.IndexedEmbedded;

/**
 * Representation of an education type associated with a course.
 * 
 * @author Pasi Kukkonen
 */
@Entity
public class CourseEducationType {

  /**
   * Default constructor.
   */
  protected CourseEducationType() {
  }

  /**
   * Constructor specifying the education type.
   * 
   * @param educationType The education type
   */
  public CourseEducationType(EducationType educationType) {
    setEducationType(educationType);
  }

  /**
   * Returns the unique identifier of this object.
   * 
   * @return The unique identifier of this object
   */
  public Long getId() {
    return id;
  }

  public CourseBase getCourseBase() {
    return courseBase;
  }

  protected void setCourseBase(CourseBase courseBase) {
    this.courseBase = courseBase;
  }

  /**
   * Returns the education type of this object.
   * 
   * @return The education type of this object
   */
  public EducationType getEducationType() {
    return educationType;
  }

  /**
   * Sets the education type of this object.
   * 
   * @param educationType The education type of this object
   */
  protected void setEducationType(EducationType educationType) {
    this.educationType = educationType;
  }

  /**
   * Returns the subtypes of this object.
   * 
   * @return The subtypes of this object
   */
  public List<CourseEducationSubtype> getCourseEducationSubtypes() {
    return courseEducationSubtypes;
  }

  /**
   * Sets the subtypes of this object.
   * 
   * @param courseEducationSubtypes The subtypes of this object
   */
  @SuppressWarnings("unused")
  private void setCourseEducationSubtypes(List<CourseEducationSubtype> courseEducationSubtypes) {
    this.courseEducationSubtypes = courseEducationSubtypes;
  }

  /**
   * Adds the given subtype to this object.
   * 
   * @param courseEducationSubtype The subtype to be added to this object
   * 
   * @throws RuntimeException Thrown if the education type of the given subtype differs from the education type of this object
   */
  public void addSubtype(CourseEducationSubtype courseEducationSubtype) {
    if (!getEducationType().equals(courseEducationSubtype.getEducationSubtype().getEducationType())) {
      throw new RuntimeException("Education type mismatch");
    }
    if (courseEducationSubtype.getCourseEducationType() != null) {
      courseEducationSubtype.getCourseEducationType().getCourseEducationSubtypes().remove(courseEducationSubtype);
    }
    courseEducationSubtype.setCourseEducationType(this);
    this.courseEducationSubtypes.add(courseEducationSubtype);
  }

  /**
   * Removes the given subtype from this object.
   * 
   * @param courseEducationSubtype The subtype to be removed from this object
   */
  public void removeSubtype(CourseEducationSubtype courseEducationSubtype) {
    courseEducationSubtype.setCourseEducationType(null);
    this.courseEducationSubtypes.remove(courseEducationSubtype);
  }
  
  /**
   * Returns whether this education type contains the given subtype.
   * 
   * @param educationSubtype The education subtype to be tested
   * 
   * @return <code>true</code> if the given subtype exists in this education type, otherwise <code>false</code>
   */
  public boolean contains(EducationSubtype educationSubtype) {
    for (CourseEducationSubtype courseEducationSubtype : courseEducationSubtypes) {
      Long educationSubtypeId = courseEducationSubtype.getEducationSubtype().getId();
      if (educationSubtypeId != null && educationSubtypeId.equals(educationSubtype.getId())) {
        return true;
      }
    }
    return false;
  }

  @SuppressWarnings("unused")
  private void setVersion(Long version) {
    this.version = version;
  }

  public Long getVersion() {
    return version;
  }

  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="CourseEducationType")  
  @TableGenerator(name="CourseEducationType", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "courseBase")
  private CourseBase courseBase;

  @ManyToOne
  @JoinColumn(name = "educationType", updatable = false)
  @IndexedEmbedded
  private EducationType educationType;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "courseEducationType")
  @IndexedEmbedded
  private List<CourseEducationSubtype> courseEducationSubtypes = new Vector<CourseEducationSubtype>();

  @Version
  @Column(nullable = false)
  private Long version;
}
