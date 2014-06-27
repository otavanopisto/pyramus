package fi.pyramus.security.impl;

import fi.muikku.security.AbstractPermissionCollection;

public class AbstractPyramusPermissionCollection extends AbstractPermissionCollection {
  
  public static final String EVERYONE = "Everyone";

  public static final String ADMIN = "Admin";
  public static final String MANAGER = "Manager";
  public static final String TEACHER = "Teacher";
  public static final String STUDENT = "Student";
    
  protected String[] getDefaultRoles(Class<?> collectionClass, String permission) throws NoSuchFieldException {
    DefaultPermissionRoles annotation = collectionClass.getField(permission).getAnnotation(DefaultPermissionRoles.class);

    if (annotation != null)
      return annotation.value();
    else
      return null;
  }
  
}
