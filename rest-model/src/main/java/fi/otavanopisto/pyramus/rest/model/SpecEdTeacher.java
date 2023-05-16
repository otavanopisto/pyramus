package fi.otavanopisto.pyramus.rest.model;

public class SpecEdTeacher {
  
  public SpecEdTeacher() {
    super();
  }

  public SpecEdTeacher(Long id, boolean isGuidanceCouncelor) {
    this.id = id;
    this.isGuidanceCouncelor = isGuidanceCouncelor;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public boolean isGuidanceCouncelor() {
    return isGuidanceCouncelor;
  }

  public void setGuidanceCouncelor(boolean isGuidanceCouncelor) {
    this.isGuidanceCouncelor = isGuidanceCouncelor;
  }

  private Long id;
  private boolean isGuidanceCouncelor;

}
