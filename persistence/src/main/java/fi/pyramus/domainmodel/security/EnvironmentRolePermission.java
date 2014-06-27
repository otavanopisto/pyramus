package fi.pyramus.domainmodel.security;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name="id")
public class EnvironmentRolePermission extends RolePermission {

}
