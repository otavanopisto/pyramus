package fi.otavanopisto.pyramus.domainmodel.base;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.hibernate.search.annotations.Indexed;

@Entity
@Indexed
public class UserAdditionalContactInfo extends TypedContactInfo {
  
  public boolean isAllowStudyDiscussions() {
    return allowStudyDiscussions;
  }

  public void setAllowStudyDiscussions(boolean allowStudyDiscussions) {
    this.allowStudyDiscussions = allowStudyDiscussions;
  }

  public Date getAllowStudyDiscussionsModified() {
    return allowStudyDiscussionsModified;
  }

  public void setAllowStudyDiscussionsModified(Date allowStudyDiscussionsModified) {
    this.allowStudyDiscussionsModified = allowStudyDiscussionsModified;
  }

  @Column
  private boolean allowStudyDiscussions;

  @Column
  private Date allowStudyDiscussionsModified;
}
