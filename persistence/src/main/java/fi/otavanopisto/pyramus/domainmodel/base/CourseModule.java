package fi.otavanopisto.pyramus.domainmodel.base;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.IndexedEmbedded;

@Entity
public class CourseModule {

  /**
   * Returns the identifier of this entity.
   * 
   * @return The identifier of this entity
   */
  public Long getId() {
    return id;
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
  
  public void setCourseNumber(Integer courseNumber) {
    this.courseNumber = courseNumber;
  }
  
  public Integer getCourseNumber() {
    return courseNumber;
  }

  public CourseBase getCourse() {
    return course;
  }

  public void setCourse(CourseBase course) {
    this.course = course;
  }

  @Id
//  @GeneratedValue(strategy=GenerationType.TABLE, generator="CourseBase")  
//  @TableGenerator(name="CourseBase", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;

  @ManyToOne  
  @JoinColumn(name="course")
  @IndexedEmbedded(includeEmbeddedObjectId = true)
  private CourseBase course;
  
  @ManyToOne  
  @JoinColumn(name="subject")
  @IndexedEmbedded(includeEmbeddedObjectId = true)
  private Subject subject;
  
  @Column
  @Field
  private Integer courseNumber;
  
  @OneToOne (cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn (name = "courseLength")
  private EducationalLength courseLength;
  
}