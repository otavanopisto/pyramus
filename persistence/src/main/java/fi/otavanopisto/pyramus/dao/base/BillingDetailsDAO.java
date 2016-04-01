package fi.otavanopisto.pyramus.dao.base;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.BillingDetails;

@Stateless
public class BillingDetailsDAO extends PyramusEntityDAO<BillingDetails> {

  public BillingDetails create(String personName, String companyName, String streetAddress1, String streetAddress2,
      String postalCode, String city, String region, String country, String phoneNumber, String emailAddress, String bic, String iban,
      String companyIdentifier, String referenceNumber) {
    EntityManager entityManager = getEntityManager();

    BillingDetails billingDetails = new BillingDetails();
    billingDetails.setPersonName(personName);
    billingDetails.setCompanyName(companyName);
    billingDetails.setStreetAddress1(streetAddress1);
    billingDetails.setStreetAddress2(streetAddress2);
    billingDetails.setPostalCode(postalCode);
    billingDetails.setCity(city);
    billingDetails.setRegion(region);
    billingDetails.setCountry(country);
    billingDetails.setPhoneNumber(phoneNumber);
    billingDetails.setEmailAddress(emailAddress);
    billingDetails.setBic(bic);
    billingDetails.setIban(iban);
    billingDetails.setCompanyIdentifier(companyIdentifier);
    billingDetails.setReferenceNumber(referenceNumber);
    entityManager.persist(billingDetails);

    return billingDetails;
  }

  public BillingDetails update(BillingDetails billingDetails, String personName, String companyName, String streetAddress1, String streetAddress2,
      String postalCode, String city, String region, String country, String phoneNumber, String emailAddress, String bic, String iban, String companyIdentifier,
      String referenceNumber) {

    EntityManager entityManager = getEntityManager();

    billingDetails.setPersonName(personName);
    billingDetails.setCompanyName(companyName);
    billingDetails.setStreetAddress1(streetAddress1);
    billingDetails.setStreetAddress2(streetAddress2);
    billingDetails.setPostalCode(postalCode);
    billingDetails.setCity(city);
    billingDetails.setRegion(region);
    billingDetails.setCountry(country);
    billingDetails.setPhoneNumber(phoneNumber);
    billingDetails.setEmailAddress(emailAddress);
    billingDetails.setBic(bic);
    billingDetails.setIban(iban);
    billingDetails.setCompanyIdentifier(companyIdentifier);
    billingDetails.setReferenceNumber(referenceNumber);

    entityManager.persist(billingDetails);

    return billingDetails;
  }

  @Override
  public void delete(BillingDetails billingDetails) {
    super.delete(billingDetails);
  }

}
