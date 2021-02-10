package fi.otavanopisto.pyramus.rest.model.muikku;

public class StudentGroupPayload {

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Boolean getIsGuidanceGroup() {
    return isGuidanceGroup;
  }

  public void setIsGuidanceGroup(Boolean isGuidanceGroup) {
    this.isGuidanceGroup = isGuidanceGroup;
  }

  private String identifier;
  private String name;
  private Boolean isGuidanceGroup;
  // TODO If needed: keywords, begin date, description

}
