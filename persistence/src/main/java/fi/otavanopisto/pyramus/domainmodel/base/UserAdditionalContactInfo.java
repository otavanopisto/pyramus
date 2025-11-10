package fi.otavanopisto.pyramus.domainmodel.base;

import javax.persistence.Entity;

@Entity
public class UserAdditionalContactInfo extends TypedContactInfo {
  
  public boolean isAllowStudyDiscussions() {
    return allowStudyDiscussions;
  }

  public void setAllowStudyDiscussions(boolean allowStudyDiscussions) {
    this.allowStudyDiscussions = allowStudyDiscussions;
  }

  private boolean allowStudyDiscussions;
}
