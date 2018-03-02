package fi.otavanopisto.pyramus.security.impl;

import fi.otavanopisto.pyramus.domainmodel.security.Permission;
import fi.otavanopisto.security.ContextReference;
import fi.otavanopisto.security.User;

public interface PermissionResolver {

  /**
   * Return true, if this PermissionResolver can handle given permission
   * 
   * @return
   */
  boolean handlesPermission(Permission permission);

  /**
   * Return true, if user has permission to resource. Note that this method must handle possible public (everyone) access also.
   *  
   * @param user
   * @param resource
   * @param permission
   * @return
   */
  boolean hasPermission(Permission permission, ContextReference contextReference, User user);
  
  /**
   * Return true, if public access to resource is allowed with permission. Used for unauthenticated users.
   * 
   * @param resource
   * @param permission
   * @return
   */
  boolean hasEveryonePermission(Permission permission, ContextReference contextReference);
}
