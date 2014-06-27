package fi.pyramus.security.impl;

import fi.muikku.security.PermissionCollection;

public interface PyramusPermissionCollection extends PermissionCollection {

  /**
   * Return default roles of supplied permission
   * 
   * @param permission
   * @return
   * @throws NoSuchFieldException when permission is not part of this collection
   */
  String[] getDefaultRoles(String permission) throws NoSuchFieldException;
  
}
