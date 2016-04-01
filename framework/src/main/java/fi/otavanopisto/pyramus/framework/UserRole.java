package fi.otavanopisto.pyramus.framework;

/**
 * The user privileges of the application.
 */
@Deprecated
public enum UserRole {
  /**
   * Everyone role, including users who haven't yet logged in.
   */
  EVERYONE       (0),
  /**
   * Guest privileges. Basically users who can browse around but aren't allowed to modify anything.
   */
  GUEST          (1),
  /**
   * User privileges. Restricted access around the application (to be decided what it actually means, though).
   */
  USER           (2),
  /**
   * Manager privileges. Allowed to create modules and courses but only modify their own.
   */
  MANAGER        (3),
  /**
   * Administrator privileges. Full control over all application features.
   */
  ADMINISTRATOR  (4),
  
  /**
   * Student privileges.
   */
  STUDENT  (5),
  
  /**
   * Trusted system account
   */
  TRUSTED_SYSTEM (6),
  
  /**
   * Generic teacher (non-course teacher) account
   */
  TEACHER (7),

  /**
   * Study guider account
   */
  STUDY_GUIDER (8);
  
  /**
   * Constructor specifying the role.
   * 
   * @param value The role
   */
  private UserRole(int value) {
    this.value = value;
  }
  
  /**
   * Returns the value of this enumeration as an integer.
   * 
   * @return The value of this enumeration as an integer
   */
  public int getValue() {
    return value;
  }
  
  /**
   * Converts the given integer to a <code>Role</code>. If the given integer
   * doesn't map to any enumeration value, returns <code>Role.EVERYONE</code>.
   * 
   * @param value The integer to be converted into a role
   * 
   * @return The <code>Role</code> corresponding to the given integer, or
   *         <code>Role.EVERYONE</code> if no match is found
   */
  public static UserRole getRole(int value) {
    for (UserRole role : values()) {
        if (role.getValue() == value) {
          return role;
        }
      }
      return EVERYONE;
    }
    
    /**
     * The value of this enumeration.
     */
    private int value;
  }
