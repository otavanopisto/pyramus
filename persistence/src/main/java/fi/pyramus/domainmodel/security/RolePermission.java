package fi.pyramus.domainmodel.security;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

import fi.pyramus.domainmodel.users.RoleEntity;

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

  public RoleEntity getRole() {
		return role;
	}
  
  public void setRole(RoleEntity role) {
		this.role = role;
	}

  public Permission getPermission() {
    return permission;
  }

  public void setPermission(Permission permission) {
    this.permission = permission;
  }

  @Id
  @GeneratedValue (strategy = GenerationType.IDENTITY)
  private Long id;
  
  @ManyToOne
  private Permission permission;
  
  @ManyToOne
  private RoleEntity role;
}
