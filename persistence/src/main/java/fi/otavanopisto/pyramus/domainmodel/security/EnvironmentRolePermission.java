package fi.otavanopisto.pyramus.domainmodel.security;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name="id")
public class EnvironmentRolePermission extends RolePermission {

}
