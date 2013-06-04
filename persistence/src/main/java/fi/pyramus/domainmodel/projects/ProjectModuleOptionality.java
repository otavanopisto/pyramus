package fi.pyramus.domainmodel.projects;

public enum ProjectModuleOptionality {
  MANDATORY       (0),
  OPTIONAL        (1);
  
  private ProjectModuleOptionality(int value) {
    this.value = value;
  }
  
  public int getValue() {
    return value;
  }
  
  public static ProjectModuleOptionality getOptionality(int value) {
    for (ProjectModuleOptionality optionality : values()) {
      if (optionality.getValue() == value) {
        return optionality;
      }
    }
    return MANDATORY;
  }
  
  private int value;
}
