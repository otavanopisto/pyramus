package fi.otavanopisto.pyramus.domainmodel.base;

import java.time.LocalDateTime;

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

  public LocalDateTime getAllowStudyDiscussionsModified() {
    return allowStudyDiscussionsModified;
  }

  public void setAllowStudyDiscussionsModified(LocalDateTime allowStudyDiscussionsModified) {
    this.allowStudyDiscussionsModified = allowStudyDiscussionsModified;
  }

  @Column
  private boolean allowStudyDiscussions;

  @Column
  private LocalDateTime allowStudyDiscussionsModified;
}
