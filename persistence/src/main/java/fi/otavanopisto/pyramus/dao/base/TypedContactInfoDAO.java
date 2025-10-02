package fi.otavanopisto.pyramus.dao.base;

import javax.ejb.Stateless;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.ContactType;
import fi.otavanopisto.pyramus.domainmodel.base.TypedContactInfo;

@Stateless
public class TypedContactInfoDAO extends PyramusEntityDAO<TypedContactInfo> {

  public TypedContactInfo create(ContactType contactType) {
    TypedContactInfo typedContactInfo = new TypedContactInfo();
    
    typedContactInfo.setContactType(contactType);
    
    return persist(typedContactInfo);
  }
  
  public TypedContactInfo update(TypedContactInfo typedContactInfo, ContactType contactType) {
    typedContactInfo.setContactType(contactType);
    return persist(typedContactInfo);
  }

}
