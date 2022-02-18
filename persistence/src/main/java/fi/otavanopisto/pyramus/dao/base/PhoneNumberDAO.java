package fi.otavanopisto.pyramus.dao.base;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.dao.users.UserDAO;
import fi.otavanopisto.pyramus.domainmodel.base.ContactInfo;
import fi.otavanopisto.pyramus.domainmodel.base.ContactType;
import fi.otavanopisto.pyramus.domainmodel.base.PhoneNumber;
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
      auditCreate(user.getPerson().getId(), user.getId(), phoneNumber, "number", true);
    }

    return phoneNumber;
  }

  public PhoneNumber update(PhoneNumber phoneNumber, ContactType contactType, Boolean defaultNumber, String number) {
    EntityManager entityManager = getEntityManager();

    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    User user = userDAO.findByContactInfo(phoneNumber.getContactInfo());
    if (user != null) {
      auditUpdate(user.getPerson().getId(), user.getId(), phoneNumber, "number", number, true);
    }

    phoneNumber.setContactType(contactType);
    phoneNumber.setDefaultNumber(defaultNumber);
    phoneNumber.setNumber(number);
    entityManager.persist(phoneNumber);

    return phoneNumber;
  }

  @Override
  public void delete(PhoneNumber phoneNumber) {
    if (phoneNumber.getContactInfo() != null) {

      UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
      User user = userDAO.findByContactInfo(phoneNumber.getContactInfo());
      if (user != null) {
        auditDelete(user.getPerson().getId(), user.getId(), phoneNumber, "number", true);
      }
      
      phoneNumber.getContactInfo().removePhoneNumber(phoneNumber);
    }
    super.delete(phoneNumber);
  }

}
