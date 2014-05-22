package fi.pyramus.dao.base;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.base.ContactType;

@Stateless
public class ContactTypeDAO extends PyramusEntityDAO<ContactType> {

  public ContactType create(String name) {
    EntityManager entityManager = getEntityManager();
    ContactType contactType = new ContactType();
    contactType.setName(name);
    entityManager.persist(contactType);
    return contactType;
  }

  public ContactType update(ContactType contactType, String name) {
    EntityManager entityManager = getEntityManager();
    contactType.setName(name);
    entityManager.persist(contactType);
    return contactType;
  }

}
