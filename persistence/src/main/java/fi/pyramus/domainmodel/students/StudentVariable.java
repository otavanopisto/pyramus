package fi.pyramus.domainmodel.students;

import java.lang.Long;
import javax.persistence.*;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Entity implementation class for Entity: UserVariable
 *
 */
@Entity
public class StudentVariable {

	public StudentVariable() {
		super();
	}
	
	public Long getId() {
		return this.id;
	}
	
	public Student getStudent() {
    return student;
  }
	
	public void setStudent(Student student) {
    this.student = student;
  }
	
	public StudentVariableKey getKey() {
    return key;
  }
	
	public void setKey(StudentVariableKey key) {
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
  @GeneratedValue(strategy=GenerationType.TABLE, generator="StudentVariable")  
  @TableGenerator(name="StudentVariable", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
	private Long id;
	
	@ManyToOne
  @JoinColumn(name = "student")
	private Student student;
	
	@ManyToOne
  @JoinColumn(name = "variableKey")
  private StudentVariableKey key;
	
	@NotEmpty
	private String value;

  @Version
  @Column(nullable = false)
  private Long version;
}
