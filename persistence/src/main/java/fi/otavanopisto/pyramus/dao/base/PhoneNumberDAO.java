package fi.otavanopisto.pyramus.dao.base;

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
import fi.otavanopisto.pyramus.domainmodel.base.PhoneNumber;
import fi.otavanopisto.pyramus.domainmodel.base.PhoneNumber_;
import fi.otavanopisto.pyramus.domainmodel.users.User;

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

    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    User user = userDAO.findByContactInfo(phoneNumber.getContactInfo());
    if (user != null) {
      auditCreate(user.getPersonId(), user.getId(), phoneNumber, PhoneNumber_.number, true);
    }

    return phoneNumber;
  }

  public PhoneNumber update(PhoneNumber phoneNumber, ContactType contactType, Boolean defaultNumber, String number) {
    EntityManager entityManager = getEntityManager();

    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    User user = userDAO.findByContactInfo(phoneNumber.getContactInfo());
    if (user != null) {
      auditUpdate(user.getPersonId(), user.getId(), phoneNumber, PhoneNumber_.number, number, true);
    }

    phoneNumber.setContactType(contactType);
    phoneNumber.setDefaultNumber(defaultNumber);
    phoneNumber.setNumber(number);
    entityManager.persist(phoneNumber);

    return phoneNumber;
  }

  public PhoneNumber findDefaultPhoneNumber(ContactInfo contactInfo) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<PhoneNumber> criteria = criteriaBuilder.createQuery(PhoneNumber.class);
    Root<PhoneNumber> root = criteria.from(PhoneNumber.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.and(
        criteriaBuilder.equal(root.get(PhoneNumber_.contactInfo), contactInfo),
        criteriaBuilder.equal(root.get(PhoneNumber_.defaultNumber), Boolean.TRUE)
      )
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

  @Override
  public void delete(PhoneNumber phoneNumber) {
    if (phoneNumber.getContactInfo() != null) {

      UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
      User user = userDAO.findByContactInfo(phoneNumber.getContactInfo());
      if (user != null) {
        auditDelete(user.getPersonId(), user.getId(), phoneNumber, PhoneNumber_.number, true);
      }
      
      phoneNumber.getContactInfo().removePhoneNumber(phoneNumber);
    }
    super.delete(phoneNumber);
  }

}
