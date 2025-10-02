package fi.otavanopisto.pyramus.domainmodel.students;

import javax.persistence.Entity;

import fi.otavanopisto.pyramus.domainmodel.base.TypedContactInfo;

@Entity
public class StudentAdditionalContactInfo extends TypedContactInfo {
  
  public boolean isAllowStudyDiscussions() {
    return allowStudyDiscussions;
  }

  public void setAllowStudyDiscussions(boolean allowStudyDiscussions) {
    this.allowStudyDiscussions = allowStudyDiscussions;
  }

  private boolean allowStudyDiscussions;
}
