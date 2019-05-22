package fi.otavanopisto.pyramus.domainmodel;

/**
 * Tri-State Boolean
 */
public enum TSB {

  TRUE,
  FALSE,
  IGNORE;
  
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
