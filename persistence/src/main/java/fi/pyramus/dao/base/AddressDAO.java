package fi.pyramus.dao.base;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.base.Address;
import fi.pyramus.domainmodel.base.ContactInfo;
import fi.pyramus.domainmodel.base.ContactType;

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
      address.getContactInfo().removeAddress(address);
    }
    super.delete(address);
  }

}
