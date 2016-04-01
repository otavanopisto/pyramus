package fi.otavanopisto.pyramus.persistence.search;

public enum SearchTimeFilterMode {
  UNDEFINED (0),
  INCLUSIVE (1),
  EXCLUSIVE (2);
  
  private SearchTimeFilterMode(int mode) {
    this.value = mode;
  }
  
  public static SearchTimeFilterMode getMode(Integer value) {
    if (value == null)
      return UNDEFINED;
    
    for (SearchTimeFilterMode mode : values()) {
      if (mode.getValue() == value.intValue())
        return mode;
    }  
    
    return UNDEFINED;
  }
  
  public int getValue() {
    return value;
  }
  
  int value;
}