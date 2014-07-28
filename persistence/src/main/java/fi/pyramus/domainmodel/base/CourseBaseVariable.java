package fi.pyramus.domainmodel.base;

import java.lang.Long;
import javax.persistence.*;
import org.hibernate.validator.constraints.NotEmpty;

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
