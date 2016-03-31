package fi.otavanopisto.pyramus.dao.base;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.ContactInfo;
import fi.otavanopisto.pyramus.domainmodel.base.ContactType;
import fi.otavanopisto.pyramus.domainmodel.base.PhoneNumber;

@Stateless
public class PhoneNumberDAO extends PyramusEntityDAO<PhoneNumber> {

  public PhoneNumber create(ContactInfo contactInfo, ContactType contactType, Boolean defaultNumber, String number) {
    EntityManager entityManager = getEntityManager();

    PhoneNumber phoneNumber = new PhoneNumber();
    phoneNumber.setContactInfo(contactInfo);
    phoneNumber.setContactType(contactType);
    phoneNumber.setDefaultNumber(defaultNumber);
    phoneNumber.setNumber(number);
    entityManager.persist(phoneNumber);

    contactInfo.addPhoneNumber(phoneNumber);
    entityManager.persist(contactInfo);

    return phoneNumber;
  }

  public PhoneNumber update(PhoneNumber phoneNumber, ContactType contactType, Boolean defaultNumber, String number) {
    EntityManager entityManager = getEntityManager();

    phoneNumber.setContactType(contactType);
    phoneNumber.setDefaultNumber(defaultNumber);
    phoneNumber.setNumber(number);
    entityManager.persist(phoneNumber);

    return phoneNumber;
  }

  @Override
  public void delete(PhoneNumber phoneNumber) {
    if (phoneNumber.getContactInfo() != null) {
      phoneNumber.getContactInfo().removePhoneNumber(phoneNumber);
    }
    super.delete(phoneNumber);
  }

}
