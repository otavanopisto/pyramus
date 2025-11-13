package fi.otavanopisto.pyramus.dao.base;

import javax.ejb.Stateless;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.ContactType;
import fi.otavanopisto.pyramus.domainmodel.base.UserAdditionalContactInfo;

@Stateless
public class UserAdditionalContactInfoDAO extends PyramusEntityDAO<UserAdditionalContactInfo> {

  public UserAdditionalContactInfo create(ContactType contactType, boolean allowStudyDiscussions) {
    UserAdditionalContactInfo contactInfo = new UserAdditionalContactInfo();
    
    contactInfo.setContactType(contactType);
    contactInfo.setAllowStudyDiscussions(allowStudyDiscussions);
    
    return persist(contactInfo);
  }
  
  public UserAdditionalContactInfo update(UserAdditionalContactInfo contactInfo, ContactType contactType, boolean allowStudyDiscussions) {
    contactInfo.setContactType(contactType);
    contactInfo.setAllowStudyDiscussions(allowStudyDiscussions);
    return persist(contactInfo);
  }

}
