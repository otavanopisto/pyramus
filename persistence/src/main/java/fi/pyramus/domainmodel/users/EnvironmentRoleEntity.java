package fi.pyramus.domainmodel.users;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name="id")
public class EnvironmentRoleEntity extends RoleEntity {

  @Override
  public UserRoleType getType() {
    return UserRoleType.ENVIRONMENT;
  }

}
