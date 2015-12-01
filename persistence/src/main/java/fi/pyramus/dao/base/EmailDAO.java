package fi.pyramus.dao.base;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.base.ContactInfo;
import fi.pyramus.domainmodel.base.ContactType;
import fi.pyramus.domainmodel.base.Email;
import fi.pyramus.domainmodel.base.Email_;

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
  
  public Email findByContactInfoAndDefaultAddress(ContactInfo contactInfo, Boolean defaultAddress) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Email> criteria = criteriaBuilder.createQuery(Email.class);
    Root<Email> root = criteria.from(Email.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.and(
        criteriaBuilder.equal(root.get(Email_.contactInfo), contactInfo),
        criteriaBuilder.equal(root.get(Email_.defaultAddress), defaultAddress)
      )
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

  public Email findByAddress(String emailAddress) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Email> criteria = criteriaBuilder.createQuery(Email.class);
    Root<Email> root = criteria.from(Email.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(Email_.address), emailAddress)
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
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
