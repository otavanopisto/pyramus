package fi.otavanopisto.pyramus.domainmodel.courses;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.TableGenerator;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

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
  
  @Basic(optional = false)
  @NotEmpty
  @NotNull
  @Lob
  private String value;

  @Version
  @Column(nullable = false)
  private Long version;
}
