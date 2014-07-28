package fi.pyramus.dao.base;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.base.ContactInfo;
import fi.pyramus.domainmodel.base.ContactURL;
import fi.pyramus.domainmodel.base.ContactURLType;

@Stateless
public class ContactURLDAO extends PyramusEntityDAO<ContactURL> {

  public ContactURL create(ContactInfo contactInfo, ContactURLType contactURLType, String url) {
    EntityManager entityManager = getEntityManager();

    ContactURL contactURL = new ContactURL();
    contactURL.setContactInfo(contactInfo);
    contactURL.setURL(url);
    contactURL.setContactURLType(contactURLType);
    
    entityManager.persist(contactURL);

    contactInfo.addContactURL(contactURL);
    entityManager.persist(contactInfo);

    return contactURL;
  }

  public ContactURL update(ContactURL contactURL, ContactURLType contactURLType, String url) {
    EntityManager entityManager = getEntityManager();

    contactURL.setContactURLType(contactURLType);
    contactURL.setURL(url);
    entityManager.persist(contactURL);

    return contactURL;
  }

  @Override
  public void delete(ContactURL contactURL) {
    if (contactURL.getContactInfo() != null) {
      contactURL.getContactInfo().removeContactURL(contactURL);
    }
    super.delete(contactURL);
  }

}
