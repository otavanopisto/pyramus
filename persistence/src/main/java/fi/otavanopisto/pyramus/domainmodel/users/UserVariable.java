package fi.otavanopisto.pyramus.domainmodel.users;

import java.lang.Long;
import javax.persistence.*;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Entity implementation class for Entity: UserVariable
 *
 */
@Entity
public class UserVariable {

	public UserVariable() {
		super();
	}
	
	public Long getId() {
		return this.id;
	}
	
	public User getUser() {
    return user;
  }
	
	public void setUser(User user) {
    this.user = user;
  }
	
	public UserVariableKey getKey() {
    return key;
  }
	
	public void setKey(UserVariableKey key) {
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
  @GeneratedValue(strategy=GenerationType.TABLE, generator="UserVariable")  
  @TableGenerator(name="UserVariable", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
	private Long id;
	
	@ManyToOne
  @JoinColumn(name = "user")
	private User user;
	
	@ManyToOne
  @JoinColumn(name = "variableKey")
  private UserVariableKey key;
	
	@NotEmpty
	private String value;

  @Version
  @Column(nullable = false)
  private Long version;
}
