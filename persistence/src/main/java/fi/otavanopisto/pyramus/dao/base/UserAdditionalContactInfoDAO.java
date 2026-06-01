package fi.otavanopisto.pyramus.dao.base;

import java.util.Date;

import javax.ejb.Stateless;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.ContactType;
import fi.otavanopisto.pyramus.domainmodel.base.UserAdditionalContactInfo;

@Stateless
public class UserAdditionalContactInfoDAO extends PyramusEntityDAO<UserAdditionalContactInfo> {

  /**
   * Creates an new UserAdditionalContactInfo with default allowStudyDiscussions set to false (modified = null).
   * 
   * If they need to be set in a constructor, make a new constructor.
   * 
   * @param contactType
   * @return
   */
  public UserAdditionalContactInfo create(ContactType contactType) {
    UserAdditionalContactInfo contactInfo = new UserAdditionalContactInfo();
    
    contactInfo.setContactType(contactType);
    contactInfo.setAllowStudyDiscussions(false);
    contactInfo.setAllowStudyDiscussionsModified(null);
    
    return persist(contactInfo);
  }
  
  public UserAdditionalContactInfo update(UserAdditionalContactInfo contactInfo, ContactType contactType) {
    contactInfo.setContactType(contactType);
    return persist(contactInfo);
  }

  public UserAdditionalContactInfo updateAllowStudyDiscussions(UserAdditionalContactInfo contactInfo, boolean allowStudyDiscussions) {
    contactInfo.setAllowStudyDiscussions(allowStudyDiscussions);
    contactInfo.setAllowStudyDiscussionsModified(new Date());
    return persist(contactInfo);
  }

}
