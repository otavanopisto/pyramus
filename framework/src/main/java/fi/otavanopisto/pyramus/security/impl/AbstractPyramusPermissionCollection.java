package fi.otavanopisto.pyramus.security.impl;

import fi.otavanopisto.security.AbstractPermissionCollection;

public class AbstractPyramusPermissionCollection extends AbstractPermissionCollection {
  
  public static final String EVERYONE = "EVERYONE";

  public static final String ADMINISTRATOR = "ADMINISTRATOR";
  public static final String MANAGER = "MANAGER";
  public static final String USER = "USER";
  public static final String GUEST = "GUEST";
  public static final String TEACHER = "TEACHER";
  public static final String STUDY_GUIDER = "STUDY_GUIDER";
  public static final String STUDY_PROGRAMME_LEADER = "STUDY_PROGRAMME_LEADER";

  public static final String STUDENT = "STUDENT";
  
  public static final String TRUSTED_SYSTEM = "TRUSTED_SYSTEM";

  protected String[] getDefaultRoles(Class<?> collectionClass, String permission) throws NoSuchFieldException {
    DefaultPermissionRoles annotation = collectionClass.getField(permission).getAnnotation(DefaultPermissionRoles.class);

    if (annotation != null)
      return annotation.value();
    else
      return null;
  }
  
  protected CourseRoleArchetype[] getDefaultCourseRoles(Class<?> collectionClass, String permission) throws NoSuchFieldException {
    DefaultCoursePermissionRoles annotation = collectionClass.getField(permission).getAnnotation(DefaultCoursePermissionRoles.class);

    if (annotation != null)
      return annotation.value();
    else
      return null;
  }
}
