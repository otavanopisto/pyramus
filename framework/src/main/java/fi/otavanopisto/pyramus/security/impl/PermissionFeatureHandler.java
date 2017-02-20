package fi.otavanopisto.pyramus.security.impl;

import fi.otavanopisto.pyramus.domainmodel.security.Permission;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.security.ContextReference;

public interface PermissionFeatureHandler {

  /**
   * Allows permission feature to explicitly approve given 
   * allowance or change it in any way it needs to.
   * 
   * @param perm
   * @param userEntity
   * @param contextReference
   * @param allowed
   * @return
   */
  boolean hasPermission(Permission perm, User userEntity, ContextReference contextReference, boolean allowed);
}
