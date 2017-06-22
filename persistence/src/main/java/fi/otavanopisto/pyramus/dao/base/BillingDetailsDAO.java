package fi.otavanopisto.pyramus.dao.base;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.BillingDetails;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent_;
import fi.otavanopisto.pyramus.domainmodel.students.Student;

@Stateless
public class BillingDetailsDAO extends PyramusEntityDAO<BillingDetails> {

  public BillingDetails create(String personName, String companyName, String streetAddress1, String streetAddress2,
      String postalCode, String city, String region, String country, String phoneNumber, String emailAddress, String electronicBillingAddress,
      String electronicBillingOperator, String companyIdentifier, String referenceNumber, String notes) {

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
    billingDetails.setElectronicBillingAddress(electronicBillingAddress);
    billingDetails.setElectronicBillingOperator(electronicBillingOperator);
    billingDetails.setCompanyIdentifier(companyIdentifier);
    billingDetails.setReferenceNumber(referenceNumber);
    billingDetails.setNotes(notes);
    
    return persist(billingDetails);
  }
  
  public List<BillingDetails> listByStudent(Student student) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<BillingDetails> criteria = criteriaBuilder.createQuery(BillingDetails.class);
    Root<CourseStudent> root = criteria.from(CourseStudent.class);
    
    criteria.select(root.get(CourseStudent_.billingDetails));
    criteria.where(criteriaBuilder.equal(root.get(CourseStudent_.student), student));
    criteria.groupBy(root.get(CourseStudent_.billingDetails));
    
    return entityManager.createQuery(criteria).getResultList();
  }
  
  public BillingDetails updatePersonName(BillingDetails billingDetails, String personName) {
    billingDetails.setPersonName(personName);
    return persist(billingDetails);
  }

  public BillingDetails updateCompanyName(BillingDetails billingDetails, String companyName) {
    billingDetails.setCompanyName(companyName);
    return persist(billingDetails);
  }

  public BillingDetails updateStreetAddress1(BillingDetails billingDetails, String streetAddress1) {
    billingDetails.setStreetAddress1(streetAddress1);
    return persist(billingDetails);
  }

  public BillingDetails updateStreetAddress2(BillingDetails billingDetails, String streetAddress2) {
    billingDetails.setStreetAddress2(streetAddress2);
    return persist(billingDetails);
  }

  public BillingDetails updatePostalCode(BillingDetails billingDetails, String postalCode) {
    billingDetails.setPostalCode(postalCode);
    return persist(billingDetails);
  }

  public BillingDetails updateCity(BillingDetails billingDetails, String city) {
    billingDetails.setCity(city);
    return persist(billingDetails);
  }

  public BillingDetails updateRegion(BillingDetails billingDetails, String region) {
    billingDetails.setRegion(region);
    return persist(billingDetails);
  }

  public BillingDetails updateCountry(BillingDetails billingDetails, String country) {
    billingDetails.setCountry(country);
    return persist(billingDetails);
  }

  public BillingDetails updatePhoneNumber(BillingDetails billingDetails, String phoneNumber) {
    billingDetails.setPhoneNumber(phoneNumber);
    return persist(billingDetails);
  }

  public BillingDetails updateEmailAddress(BillingDetails billingDetails, String emailAddress) {
    billingDetails.setEmailAddress(emailAddress);
    return persist(billingDetails);
  }

  public BillingDetails updateCompanyIdentifier(BillingDetails billingDetails, String companyIdentifier) {
    billingDetails.setCompanyIdentifier(companyIdentifier);
    return persist(billingDetails);
  }

  public BillingDetails updateReferenceNumber(BillingDetails billingDetails, String referenceNumber) {
    billingDetails.setReferenceNumber(referenceNumber);
    return persist(billingDetails);
  }

  public BillingDetails updateNotes(BillingDetails billingDetails, String notes) {
    billingDetails.setNotes(notes);
    return persist(billingDetails);
  }

  public BillingDetails updateElectronicBillingAddress(BillingDetails billingDetails, String electronicBillingAddress) {
    billingDetails.setElectronicBillingAddress(electronicBillingAddress);
    return persist(billingDetails);
  }

  public BillingDetails updateElectronicBillingOperator(BillingDetails billingDetails, String electronicBillingOperator) {
    billingDetails.setElectronicBillingOperator(electronicBillingOperator);
    return persist(billingDetails);
  }

  @Override
  public void delete(BillingDetails billingDetails) {
    super.delete(billingDetails);
  }

}
