package fi.otavanopisto.pyramus.dao.base;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.dao.users.UserDAO;
import fi.otavanopisto.pyramus.domainmodel.base.ContactInfo;
import fi.otavanopisto.pyramus.domainmodel.base.ContactType;
import fi.otavanopisto.pyramus.domainmodel.base.Email;
import fi.otavanopisto.pyramus.domainmodel.base.Email_;
import fi.otavanopisto.pyramus.domainmodel.users.User;

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

    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    User user = userDAO.findByContactInfo(email.getContactInfo());
    if (user != null) {
      auditCreate(user.getPersonId(), user.getId(), email, "address", true);
    }

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

  public List<Email> listByContactInfo(ContactInfo contactInfo) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Email> criteria = criteriaBuilder.createQuery(Email.class);
    Root<Email> root = criteria.from(Email.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.equal(root.get(Email_.contactInfo), contactInfo)
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public List<Email> listByAddressLowercase(String emailAddress) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Email> criteria = criteriaBuilder.createQuery(Email.class);
    Root<Email> root = criteria.from(Email.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.equal(
        criteriaBuilder.lower(root.get(Email_.address)), emailAddress.toLowerCase())
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public Email update(Email email, ContactType contactType, Boolean defaultAddress, String address) {
    EntityManager entityManager = getEntityManager();

    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    User user = userDAO.findByContactInfo(email.getContactInfo());
    if (user != null) {
      auditUpdate(user.getPersonId(), user.getId(), email, "address", address, true);
    }

    email.setContactType(contactType);
    email.setDefaultAddress(defaultAddress);
    email.setAddress(address);
    entityManager.persist(email);

    return email;
  }

  @Override
  public void delete(Email email) {
    if (email.getContactInfo() != null) {

      UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
      User user = userDAO.findByContactInfo(email.getContactInfo());
      if (user != null) {
        auditDelete(user.getPersonId(), user.getId(), email, "address", true);
      }
      
      email.getContactInfo().removeEmail(email);
    }
    super.delete(email);
  }

}
