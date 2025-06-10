package fi.otavanopisto.pyramus.domainmodel.base;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;

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
  @GeneratedValue(strategy = GenerationType.IDENTITY)  
  private Long id;

  @ManyToOne // Don't mark this as lazy as the type cannot be determined 
             // and you end up with Narrowing proxy warnings
  @JoinColumn(name="course", nullable = false)
  @NotNull
  private CourseBase course;
  
  @ManyToOne  
  @JoinColumn(name="subject")
  @IndexedEmbedded(includeEmbeddedObjectId = true)
  private Subject subject;
  
  @Column
  private Integer courseNumber;
  
  @OneToOne (cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn (name = "courseLength")
  @IndexedEmbedded(includeEmbeddedObjectId = true)
  private EducationalLength courseLength;
  
}