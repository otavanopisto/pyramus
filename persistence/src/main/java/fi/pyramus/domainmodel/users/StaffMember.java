package fi.pyramus.domainmodel.users;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Indexed
@Cache (usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@PrimaryKeyJoinColumn(name="id")
public class StaffMember extends User {

  public String getAuthProvider() {
    return authProvider;
  }
  
  public void setAuthProvider(String authProvider) {
    this.authProvider = authProvider;
  }
  
  public String getExternalId() {
    return externalId;
  }
  
  public void setExternalId(String externalId) {
    this.externalId = externalId;
  }
  
  public void setRole(Role role) {
    this.role = role;
  }

  public Role getRole() {
    return role;
  }
  
  public void setTitle(String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }

  @NotNull
  @Column (nullable = false)
  @NotEmpty
  private String externalId;
  
  @NotNull
  @Column (nullable = false)
  @NotEmpty
  private String authProvider;  

  private String title;  
  
  @NotNull
  @Column (nullable = false)
  @Enumerated (EnumType.STRING)
  @Field (store = Store.NO)
  // TODO Some way to disallow Role.EVERYONE
  private Role role;
}
