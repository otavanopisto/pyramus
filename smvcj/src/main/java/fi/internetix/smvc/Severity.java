package fi.internetix.smvc;

/**
 * The error levels of the application.
 */
public enum Severity {
  
  OK          (1),
  INFORMATION (2),
  WARNING     (3),
  ERROR       (4),
  CRITICAL    (5);
  
  /**
   * Constructor specifying the error level.
   * 
   * @param value The error level
   */
  private Severity(int value) {
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
  
  /** The value of this enumeration */
  private int value;
}
