package fi.pyramus.security.impl;

public class PermissionScope {
  public static final String ENVIRONMENT = "ENVIRONMENT";

  /**
   * Owner of an entity who is a student. Ownership ends when studies are over.
   */
  public static final String STUDENT_OWNER = "STUDENT_OWNER";

  /**
   * Owner of an person entity (e.g. Student or User of same person entity)
   */
  public static final String PERSON_OWNER = "PERSON_OWNER";
}
