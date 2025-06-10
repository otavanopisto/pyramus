package fi.otavanopisto.pyramus.domainmodel.courses;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.TableGenerator;

import fi.otavanopisto.pyramus.domainmodel.base.CourseBase;

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
  
  @Lob
  private String description;
}
