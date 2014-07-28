package fi.pyramus.dao.base;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.base.ContactInfo;
import fi.pyramus.domainmodel.base.ContactType;
import fi.pyramus.domainmodel.base.Email;

@Stateless
public class EmailDAO extends PyramusEntityDAO<Email> {

  public Email create(ContactInfo contactInfo, ContactType contactType, Boolean defaultAddress, String address) {
    EntityManager entityManager = getEntityManager();

    Email email = new Email();
    email.setContactInfo(contactInfo);
    email.setContactType(contactType);
    email.setDefaultAddress(defaultAddress);
    email.setAddress(address);
    entityManager.persist(email);

    contactInfo.addEmail(email);
    entityManager.persist(contactInfo);

    return email;
  }

  public Email update(Email email, ContactType contactType, Boolean defaultAddress, String address) {
    EntityManager entityManager = getEntityManager();

    email.setContactType(contactType);
    email.setDefaultAddress(defaultAddress);
    email.setAddress(address);
    entityManager.persist(email);

    return email;
  }

  @Override
  public void delete(Email email) {
    if (email.getContactInfo() != null) {
      email.getContactInfo().removeEmail(email);
    }
    super.delete(email);
  }


}
