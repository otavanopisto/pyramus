package fi.otavanopisto.pyramus.security.impl;

import fi.otavanopisto.security.PermissionCollection;
import fi.otavanopisto.security.PermissionFeature;

public interface PyramusPermissionCollection extends PermissionCollection {

  /**
   * Return default roles of supplied permission
   * 
   * @param permission
   * @return
   * @throws NoSuchFieldException when permission is not part of this collection
   */
  String[] getDefaultRoles(String permission) throws NoSuchFieldException;

  PermissionFeature[] listPermissionFeatures(String permission) throws NoSuchFieldException, SecurityException;
  
}
