package fi.otavanopisto.pyramus.rest.model.muikku;

public class StudentGroupMembersPayload {

  public String getGroupIdentifier() {
    return groupIdentifier;
  }

  public void setGroupIdentifier(String groupIdentifier) {
    this.groupIdentifier = groupIdentifier;
  }

  public String[] getUserIdentifiers() {
    return userIdentifiers;
  }

  public void setUserIdentifiers(String[] userIdentifiers) {
    this.userIdentifiers = userIdentifiers;
  }

  private String groupIdentifier;
  private String[] userIdentifiers;

}
