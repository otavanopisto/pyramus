package fi.pyramus.domainmodel.users;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.NotNull;

@Entity
@PrimaryKeyJoinColumn(name="id")
public class SystemRoleEntity extends RoleEntity {

  @Override
  public UserRoleType getType() {
    return UserRoleType.SYSTEM;
  }

  public SystemRoleType getRoleType() {
    return roleType;
  }

  public void setRoleType(SystemRoleType roleType) {
    this.roleType = roleType;
  }

  @NotNull
  @Column (nullable = false)
  @Enumerated (EnumType.STRING)
  private SystemRoleType roleType;
}
