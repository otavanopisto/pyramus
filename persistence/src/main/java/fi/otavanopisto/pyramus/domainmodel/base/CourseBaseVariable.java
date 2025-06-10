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
import jakarta.validation.constraints.NotEmpty;

/**
 * Entity implementation class for Entity: CourseVariable
 *
 */
@Entity
public class CourseBaseVariable {

  public CourseBaseVariable() {
    super();
  }
  
  public Long getId() {
    return this.id;
  }
  
  public CourseBase getCourseBase() {
    return courseBase;
  }
  
  public void setCourseBase(CourseBase courseBase) {
    this.courseBase = courseBase;
  }
  
  public CourseBaseVariableKey getKey() {
    return key;
  }
  
  public void setKey(CourseBaseVariableKey key) {
    this.key = key;
  }
  
  public String getValue() {
    return value;
  }
  
  public void setValue(String value) {
    this.value = value;
  }

  @SuppressWarnings("unused")
  private void setVersion(Long version) {
    this.version = version;
  }

  public Long getVersion() {
    return version;
  }

  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="CourseBaseVariable")  
  @TableGenerator(name="CourseBaseVariable", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;
  
  // TODO: Shouldn't courseBase + key be unique?
  
  @ManyToOne
  @JoinColumn(name = "courseBase")
  private CourseBase courseBase;
  
  @ManyToOne
  @JoinColumn(name = "variableKey")
  private CourseBaseVariableKey key;
  
  @NotEmpty
  private String value;

  @Version
  @Column(nullable = false)
  private Long version;
}
