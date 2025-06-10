package fi.otavanopisto.pyramus.domainmodel.security;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.TableGenerator;
import jakarta.validation.constraints.NotNull;

import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;

import fi.otavanopisto.pyramus.domainmodel.users.Role;

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
  @GenericField (projectable = Projectable.NO)
  private Role role;
}
