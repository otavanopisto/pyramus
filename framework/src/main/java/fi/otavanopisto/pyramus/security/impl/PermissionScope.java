package fi.otavanopisto.pyramus.security.impl;

public interface PermissionScope {
  public static final String ENVIRONMENT = "ENVIRONMENT";
  public static final String COURSE = "COURSE";

  /**
   * Owner of an entity who is a student. Ownership ends when studies are over.
   */
  public static final String STUDENT_OWNER = "STUDENT_OWNER";

  /**
   * Owner of an entity who is a user.
   */
  public static final String USER_OWNER = "USER_OWNER";

  /**
   * Owner of an person entity (e.g. Student or User of same person entity)
   */
  public static final String PERSON_OWNER = "PERSON_OWNER";
  
  /**
   * 
   */
  public static final String PERSON_PARENT = "PERSON_PARENT";
  
  /**
   * 
   */
  public static final String STUDENT_PARENT = "STUDENT_PARENT";
}
