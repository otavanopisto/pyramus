package fi.otavanopisto.pyramus.dao.base;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.ContactInfo;

@Stateless
public class ContactInfoDAO extends PyramusEntityDAO<ContactInfo> {

  public ContactInfo update(ContactInfo contactInfo, String additionalInfo) {
    EntityManager entityManager = getEntityManager();
    contactInfo.setAdditionalInfo(additionalInfo);
    entityManager.persist(contactInfo);
    return contactInfo;
  }

}
