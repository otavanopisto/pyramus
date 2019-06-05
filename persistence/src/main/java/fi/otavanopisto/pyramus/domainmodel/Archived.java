package fi.otavanopisto.pyramus.domainmodel;

public enum Archived {

  // Entities that are not archived
  UNARCHIVED,
  // Entities that are archived
  ARCHIVED,
  // All entities
  BOTH;
  
  /**
   * Returns true if this enum represents a boolean value
   * 
   * @return true if this enum represents a boolean value
   */
  public boolean isBoolean() {
    return this.equals(ARCHIVED) || this.equals(UNARCHIVED);
  }
  
  /**
   * Returns True for ARCHIVED, false for UNARCHIVED or null if this enum is BOTH.
   * 
   * @return True for ARCHIVED, false for UNARCHIVED or null if this enum is BOTH.
   */
  public Boolean booleanValue() {
    return this.equals(ARCHIVED) ? Boolean.TRUE : this.equals(UNARCHIVED) ? Boolean.FALSE : null;
  }
}
