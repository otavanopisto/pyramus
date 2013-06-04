package fi.pyramus.domainmodel.base;

import java.lang.Long;
import javax.persistence.*;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Entity implementation class for Entity: CourseVariable
 *
 */
@Entity
public class SchoolVariable {

	public SchoolVariable() {
		super();
	}
	
	public Long getId() {
		return this.id;
	}
	
	public School getSchool() {
    return school;
  }
	
	public void setSchool(School school) {
    this.school = school;
  }
	
	public SchoolVariableKey getKey() {
    return key;
  }
	
	public void setKey(SchoolVariableKey key) {
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
  @GeneratedValue(strategy=GenerationType.TABLE, generator="SchoolVariable")  
  @TableGenerator(name="SchoolVariable", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
	private Long id;
	
	@ManyToOne
  @JoinColumn(name = "school")
	private School school;
	
	@ManyToOne
  @JoinColumn(name = "variableKey")
  private SchoolVariableKey key;
	
	@NotEmpty
	private String value;

  @Version
  @Column(nullable = false)
  private Long version;
}
