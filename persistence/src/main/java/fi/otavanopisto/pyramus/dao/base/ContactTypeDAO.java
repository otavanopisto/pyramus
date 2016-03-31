package fi.otavanopisto.pyramus.dao.base;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.ContactType;
import fi.pyramus.domainmodel.base.ContactType_;

@Stateless
public class ContactTypeDAO extends PyramusEntityDAO<ContactType> {

  public ContactType create(String name, Boolean nonUnique) {
    EntityManager entityManager = getEntityManager();
    ContactType contactType = new ContactType();
    contactType.setName(name);
    contactType.setNonUnique(nonUnique);
    entityManager.persist(contactType);
    return contactType;
  }

  public List<ContactType> listByName(String name) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<ContactType> criteria = criteriaBuilder.createQuery(ContactType.class);
    Root<ContactType> root = criteria.from(ContactType.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(ContactType_.name), name)
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public ContactType update(ContactType contactType, String name, Boolean nonUnique) {
    EntityManager entityManager = getEntityManager();
    contactType.setName(name);
    contactType.setNonUnique(nonUnique);
    entityManager.persist(contactType);
    return contactType;
  }

}
