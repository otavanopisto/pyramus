package fi.pyramus.domainmodel.courses;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;

import fi.pyramus.domainmodel.base.CourseBase;

@Entity
public class CourseDescription {

  /**
   * Returns the identifier of this entity.
   * 
   * @return The identifier of this entity
   */
  public Long getId() {
    return id;
  }
  
  public void setCourseBase(CourseBase courseBase) {
    this.courseBase = courseBase;
  }

  public CourseBase getCourseBase() {
    return courseBase;
  }

  public void setCategory(CourseDescriptionCategory category) {
    this.category = category;
  }

  public CourseDescriptionCategory getCategory() {
    return category;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="CourseDescription")  
  @TableGenerator(name="CourseDescription", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;
  
  @ManyToOne
  @JoinColumn(name="courseBase")
  private CourseBase courseBase;
  
  @ManyToOne
  @JoinColumn(name="category")
  private CourseDescriptionCategory category;
  
  @Basic (fetch = FetchType.LAZY)
  @Column (length=2147483647)
  private String description;
}
