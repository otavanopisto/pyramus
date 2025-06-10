package fi.otavanopisto.pyramus.plugin.simple.domainmodel.users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.TableGenerator;
import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.NotEmpty;

@Entity
/** Entity object for SimplePlugin authorization provider. */
public class SimpleAuth {

  /** Returns the ID of the entity.
   * 
   * @return The ID of the entity.
   */
  public Long getId() {
    return id;
  }
  
  /** Returns the username of the entity.
   * 
   * @return the username of the entity.
   */
  public String getUsername() {
    return username;
  }
  
  /** Sets the username of the entity.
   * 
   * @param username The new username.
   */
  public void setUsername(String username) {
    this.username = username;
  }
  
  /** Returns the password of the entity.
   * 
   * @return the password of the entity.
   */
  public String getPassword() {
    return password;
  }
  
  /** Sets the password of the entity.
   * 
   * @param password The new password.
   */
  public void setPassword(String password) {
    this.password = password;
  }
  
  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="SimpleAuth")  
  @TableGenerator(name="SimpleAuth", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;

  @Column (nullable = false, unique = true)
  @NotNull
  @NotEmpty
  private String username;
  
  @Column (nullable = false)
  @NotNull
  @NotEmpty
  private String password;
}
