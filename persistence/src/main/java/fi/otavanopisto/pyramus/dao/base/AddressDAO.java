package fi.otavanopisto.pyramus.dao.base;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.dao.users.UserDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Address;
import fi.otavanopisto.pyramus.domainmodel.base.ContactInfo;
import fi.otavanopisto.pyramus.domainmodel.base.ContactType;
import fi.otavanopisto.pyramus.domainmodel.users.User;

@Stateless
public class AddressDAO extends PyramusEntityDAO<Address> {

  public Address create(ContactInfo contactInfo, ContactType contactType, String name, String streetAddress, String postalCode, String city,
      String country, Boolean defaultAddress) {
    EntityManager entityManager = getEntityManager();

    Address address = new Address();
    address.setContactInfo(contactInfo);
    address.setContactType(contactType);
    address.setName(name);
    address.setStreetAddress(streetAddress);
    address.setPostalCode(postalCode);
    address.setCity(city);
    address.setCountry(country);
    address.setDefaultAddress(defaultAddress);
    entityManager.persist(address);

    contactInfo.addAddress(address);
    entityManager.persist(contactInfo);

    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    User user = userDAO.findByContactInfo(address.getContactInfo());
    if (user != null) {
      auditCreate(user.getPersonId(), user.getId(), address, "name", true);
      auditCreate(user.getPersonId(), user.getId(), address, "streetAddress", true);
      auditCreate(user.getPersonId(), user.getId(), address, "postalCode", true);
      auditCreate(user.getPersonId(), user.getId(), address, "city", true);
      auditCreate(user.getPersonId(), user.getId(), address, "country", true);
    }

    return address;
  }

  /**
   * Updates and returns the given address.
   * 
   * @param address
   *          The address to update
   * @param defaultAddress
   *          Default address
   * @param contactType
   *          Contact type
   * @param name
   *          Name
   * @param streetAddress
   *          Street address
   * @param postalCode
   *          Postal code
   * @param city
   *          City
   * @param country
   *          Country
   * 
   * @return The updated address
   */
  public Address update(Address address, Boolean defaultAddress, ContactType contactType, String name, String streetAddress, String postalCode,
      String city, String country) {
    EntityManager entityManager = getEntityManager();

    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    User user = userDAO.findByContactInfo(address.getContactInfo());
    if (user != null) {
      auditUpdate(user.getPersonId(), user.getId(), address, "name", name, true);
      auditUpdate(user.getPersonId(), user.getId(), address, "streetAddress", streetAddress, true);
      auditUpdate(user.getPersonId(), user.getId(), address, "postalCode", postalCode, true);
      auditUpdate(user.getPersonId(), user.getId(), address, "city", city, true);
      auditUpdate(user.getPersonId(), user.getId(), address, "country", country, true);
    }

    address.setDefaultAddress(defaultAddress);
    address.setContactType(contactType);
    address.setName(name);
    address.setStreetAddress(streetAddress);
    address.setPostalCode(postalCode);
    address.setCity(city);
    address.setCountry(country);

    entityManager.persist(address);

    return address;
  }

  @Override
  public void delete(Address address) {
    if (address.getContactInfo() != null) {

      UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
      User user = userDAO.findByContactInfo(address.getContactInfo());
      if (user != null) {
        auditDelete(user.getPersonId(), user.getId(), address, "name", true);
        auditDelete(user.getPersonId(), user.getId(), address, "streetAddress", true);
        auditDelete(user.getPersonId(), user.getId(), address, "postalCode", true);
        auditDelete(user.getPersonId(), user.getId(), address, "city", true);
        auditDelete(user.getPersonId(), user.getId(), address, "country", true);
      }
      
      address.getContactInfo().removeAddress(address);
    }
    super.delete(address);
  }

}
