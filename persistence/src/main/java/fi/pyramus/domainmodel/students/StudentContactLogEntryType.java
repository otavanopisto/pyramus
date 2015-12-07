package fi.pyramus.domainmodel.students;

/**
 * Enumeration for StudentContactLogEntry, which defines 
 * what kind of entry it is.
 * 
 * @author antti.viljakainen
 */
public enum StudentContactLogEntryType {
  OTHER (0),
  LETTER (1),
  EMAIL (2),
  PHONE (3),
  CHATLOG (4),
  SKYPE (5),
  FACE2FACE (6),
  ABSENCE (7);
  
  /**
   * Creates new enumeration instance
   * 
   * @param value enumeration value 
   */
  private StudentContactLogEntryType(int value) {
    this.value = value;
  }
  
  /**
   * Returns value of this instance.
   * 
   * @return value
   */
  public int getValue() {
    return value;
  }
  
  /**
   * Returns Enumeration object for given int value.
   * @param value
   * @return
   */
  public static StudentContactLogEntryType getType(int value) {
    for (StudentContactLogEntryType types : values()) {
      if (types.getValue() == value) {
        return types;
      }
    }
    return null;
  }
  
  /**
   * Internal value
   */
  private int value;
}
