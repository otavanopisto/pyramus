package fi.pyramus.domainmodel.security;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Store;

import fi.pyramus.domainmodel.users.Role;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class RolePermission {

  /**
   * Returns internal unique id
   * 
   * @return Internal unique id
   */
  public Long getId() {
    return id;
  }

  public Role getRole() {
		return role;
	}
  
  public void setRole(Role role) {
		this.role = role;
	}

  public Permission getPermission() {
    return permission;
  }

  public void setPermission(Permission permission) {
    this.permission = permission;
  }

  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="RolePermission")  
  @TableGenerator(name="RolePermission", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;
  
  @ManyToOne
  private Permission permission;
  
  @NotNull
  @Column (nullable = false)
  @Enumerated (EnumType.STRING)
  @Field (store = Store.NO)
  private Role role;
}
