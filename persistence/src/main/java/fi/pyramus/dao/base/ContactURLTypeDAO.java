package fi.pyramus.dao.base;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.base.ContactURLType;

@Stateless
public class ContactURLTypeDAO extends PyramusEntityDAO<ContactURLType> {

  public ContactURLType create(String name) {
    EntityManager entityManager = getEntityManager();
    ContactURLType contactURLType = new ContactURLType();
    contactURLType.setName(name);
    entityManager.persist(contactURLType);
    return contactURLType;
  }

  public ContactURLType update(ContactURLType contactURLType, String name) {
    EntityManager entityManager = getEntityManager();
    contactURLType.setName(name);
    entityManager.persist(contactURLType);
    return contactURLType;
  }

}
