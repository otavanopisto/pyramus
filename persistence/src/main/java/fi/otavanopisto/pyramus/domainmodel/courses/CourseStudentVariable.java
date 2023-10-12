package fi.otavanopisto.pyramus.domainmodel.courses;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Version;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Entity implementation class for Entity: UserVariable
 *
 */
@Entity
public class CourseStudentVariable {

  public CourseStudentVariable() {
    super();
  }
  
  public Long getId() {
    return this.id;
  }
  
  public CourseStudentVariableKey getKey() {
    return key;
  }
  
  public void setKey(CourseStudentVariableKey key) {
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

  public CourseStudent getCourseStudent() {
    return courseStudent;
  }

  public void setCourseStudent(CourseStudent courseStudent) {
    this.courseStudent = courseStudent;
  }

  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="CourseStudentVariable")  
  @TableGenerator(name="CourseStudentVariable", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;
  
  @ManyToOne
  @JoinColumn(name = "courseStudent")
  private CourseStudent courseStudent;
  
  @ManyToOne
  @JoinColumn(name = "variableKey")
  private CourseStudentVariableKey key;
  
  @NotEmpty
  @Column (nullable = false)
  @NotNull
  @Lob
  private String value;

  @Version
  @Column(nullable = false)
  private Long version;
}
