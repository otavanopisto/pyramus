package fi.otavanopisto.pyramus.domainmodel.users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.TableGenerator;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * Entity that stores user credentials. 
 */
@Entity
public class InternalAuth {
  
  /**
   * Returns the identifier of this entity.
   * 
   * @return The identifier of this entity
   */
  public Long getId() {
    return id;
  }
  
  /**
   * Returns the password of this entity.
   * 
   * @return The password of this entity
   */
  public String getPassword() {
    return password;
  }
  
  /**
   * Sets the password of this entity.
   * 
   * @param password The password of this entity
   */
  public void setPassword(String password) {
    this.password = password;
  }
  
  /**
   * Returns the username of this entity.
   * 
   * @return The username of this entity
   */
  public String getUsername() {
    return username;
  }
  
  /**
   * Sets the username of this entity.
   * 
   * @param password The username of this entity
   */
  public void setUsername(String username) {
    this.username = username;
  }
  
  @SuppressWarnings("unused")
  private void setVersion(Long version) {
    this.version = version;
  }

  public Long getVersion() {
    return version;
  }

  @Id 
  @GeneratedValue(strategy=GenerationType.TABLE, generator="InternalAuth")  
  @TableGenerator(name="InternalAuth", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;
  
  @Column (nullable = false, unique = true)
  @NotEmpty 
  private String username;
  
  @NotNull
  @Column (nullable = false)
  @NotEmpty
  private String password;
  
  @Version
  @Column(nullable = false)
  private Long version;
}
