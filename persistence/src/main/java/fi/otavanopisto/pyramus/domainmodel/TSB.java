package fi.otavanopisto.pyramus.domainmodel;

/**
 * Tri-State Boolean
 */
public enum TSB {

  TRUE,
  FALSE,
  IGNORE;
  
  /**
   * Returns TSB TRUE/FALSE from primitive boolean.
   * 
   * @param b primitive boolean value
   * @return TSB TRUE/FALSE representing the b value
   */
  public static TSB from(boolean b) {
    return b ? TRUE : FALSE;
  }
  
  /**
   * Returns TSB from Boolean. If b is null, returns IGNORE, 
   * otherwise returns TSB presenting the boolean value.
   * 
   * @param b Boolean
   * @return TRUE/FALSE if b is not null, IGNORE otherwise
   */
  public static TSB from(Boolean b) {
    return b != null ? from(b.booleanValue()) : IGNORE;
  }
  
  /**
   * Returns TSB from String s, value is
   * TRUE when s equals "true" (case insensitive)
   * FALSE when s equals "false" (case insensitive)
   * IGNORE otherwise
   * 
   * @param s String
   * @return TRUE/FALSE if b is not null, IGNORE otherwise
   */
  public static TSB from(String s) {
    if ("true".equalsIgnoreCase(s)) {
      return TRUE;
    }
    
    if ("false".equalsIgnoreCase(s)) {
      return FALSE;
    }
    
    return IGNORE;
  }
  
  /**
   * Returns true if this enum represents a boolean value
   * 
   * @return true if this enum represents a boolean value
   */
  public boolean isBoolean() {
    return this.equals(TRUE) || this.equals(FALSE);
  }
  
  /**
   * Returns Boolean value of this enum or null if this enum is IGNORE.
   * 
   * @return Boolean value of this enum or null if this enum is IGNORE 
   */
  public Boolean booleanValue() {
    return this.equals(TRUE) ? Boolean.TRUE : this.equals(FALSE) ? Boolean.FALSE : null;
  }
}
