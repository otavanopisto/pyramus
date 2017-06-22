package fi.otavanopisto.pyramus.json.settings;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.AddressDAO;
import fi.otavanopisto.pyramus.dao.base.BillingDetailsDAO;
import fi.otavanopisto.pyramus.dao.base.ContactTypeDAO;
import fi.otavanopisto.pyramus.dao.base.EmailDAO;
import fi.otavanopisto.pyramus.dao.base.PhoneNumberDAO;
import fi.otavanopisto.pyramus.dao.base.SchoolDAO;
import fi.otavanopisto.pyramus.dao.base.SchoolFieldDAO;
import fi.otavanopisto.pyramus.dao.base.SchoolVariableDAO;
import fi.otavanopisto.pyramus.dao.base.TagDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Address;
import fi.otavanopisto.pyramus.domainmodel.base.BillingDetails;
import fi.otavanopisto.pyramus.domainmodel.base.ContactType;
import fi.otavanopisto.pyramus.domainmodel.base.Email;
import fi.otavanopisto.pyramus.domainmodel.base.PhoneNumber;
import fi.otavanopisto.pyramus.domainmodel.base.School;
import fi.otavanopisto.pyramus.domainmodel.base.SchoolField;
import fi.otavanopisto.pyramus.domainmodel.base.Tag;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

/**
 * The controller responsible of creating a new school. 
 * 
 * @see fi.otavanopisto.pyramus.views.settings.CreateSchoolViewController
 */
public class EditSchoolJSONRequestController extends JSONRequestController {

  /**
   * Processes the request to create a new grading scale.
   * 
   * @param requestContext The JSON request context
   */
  public void process(JSONRequestContext requestContext) {
    SchoolDAO schoolDAO = DAOFactory.getInstance().getSchoolDAO();
    SchoolFieldDAO schoolFieldDAO = DAOFactory.getInstance().getSchoolFieldDAO();
    SchoolVariableDAO schoolVariableDAO = DAOFactory.getInstance().getSchoolVariableDAO();
    AddressDAO addressDAO = DAOFactory.getInstance().getAddressDAO();
    EmailDAO emailDAO = DAOFactory.getInstance().getEmailDAO();
    PhoneNumberDAO phoneNumberDAO = DAOFactory.getInstance().getPhoneNumberDAO();
    TagDAO tagDAO = DAOFactory.getInstance().getTagDAO();
    ContactTypeDAO contactTypeDAO = DAOFactory.getInstance().getContactTypeDAO();
    BillingDetailsDAO billingDetailsDAO = DAOFactory.getInstance().getBillingDetailsDAO();

    Long schoolId = NumberUtils.createLong(requestContext.getRequest().getParameter("schoolId"));
    School school = schoolDAO.findById(schoolId);

    Long schoolFieldId = requestContext.getLong("schoolFieldId");
    SchoolField schoolField = null;
    if ((schoolFieldId != null) && (schoolFieldId.intValue() >= 0))
      schoolField = schoolFieldDAO.findById(schoolFieldId);
    
    String schoolCode = requestContext.getString("code");
    String schoolName = requestContext.getString("name");
    String tagsText = requestContext.getString("tags");
    
    Set<Tag> tagEntities = new HashSet<>();
    if (!StringUtils.isBlank(tagsText)) {
      List<String> tags = Arrays.asList(tagsText.split("[\\ ,]"));
      for (String tag : tags) {
        if (!StringUtils.isBlank(tag)) {
          Tag tagEntity = tagDAO.findByText(tag.trim());
          if (tagEntity == null)
            tagEntity = tagDAO.create(tag);
          tagEntities.add(tagEntity);
        }
      }
    }
    
    schoolDAO.update(school, schoolCode, schoolName, schoolField);

    // BillingDetails
    
    String billingPersonName = requestContext.getString("billingDetailsPersonName");
    String billingCompanyName = requestContext.getString("billingDetailsCompanyName");
    String billingStreetAddress1 = requestContext.getString("billingDetailsStreetAddress1");
    String billingStreetAddress2 = requestContext.getString("billingDetailsStreetAddress2");
    String billingPostalCode = requestContext.getString("billingDetailsPostalCode");
    String billingCity = requestContext.getString("billingDetailsCity");
    String billingRegion = requestContext.getString("billingDetailsRegion");
    String billingCountry = requestContext.getString("billingDetailsCountry");
    String billingPhoneNumber = requestContext.getString("billingDetailsPhoneNumber");
    String billingEmailAddress = requestContext.getString("billingDetailsEmailAddress");
    String billingElectronicBillingAddress = requestContext.getString("billingDetailsElectronicBillingAddress");
    String billingElectronicBillingOperator = requestContext.getString("billingDetailsElectronicBillingOperator");
    String billingCompanyIdentifier = requestContext.getString("billingDetailsCompanyIdentifier");
    String billingReferenceNumber = requestContext.getString("billingDetailsReferenceNumber");
    String billingNotes = requestContext.getString("billingDetailsNotes");

    if (school.getBillingDetails() == null) {
      BillingDetails billingDetails = billingDetailsDAO.create(
          billingPersonName, billingCompanyName, billingStreetAddress1, billingStreetAddress2, 
          billingPostalCode, billingCity, billingRegion, billingCountry, billingPhoneNumber, 
          billingEmailAddress, billingElectronicBillingAddress, billingElectronicBillingOperator, 
          billingCompanyIdentifier, billingReferenceNumber, billingNotes);
      
      schoolDAO.updateBillingDetails(school, billingDetails);
    } else {
      BillingDetails billingDetails = school.getBillingDetails();
      billingDetailsDAO.updatePersonName(billingDetails, billingPersonName);
      billingDetailsDAO.updateCompanyName(billingDetails, billingCompanyName);
      billingDetailsDAO.updateStreetAddress1(billingDetails, billingStreetAddress1);
      billingDetailsDAO.updateStreetAddress2(billingDetails, billingStreetAddress2);
      billingDetailsDAO.updatePostalCode(billingDetails, billingPostalCode);
      billingDetailsDAO.updateCity(billingDetails, billingCity);
      billingDetailsDAO.updateRegion(billingDetails, billingRegion);
      billingDetailsDAO.updateCountry(billingDetails, billingCountry);
      billingDetailsDAO.updatePhoneNumber(billingDetails, billingPhoneNumber);
      billingDetailsDAO.updateEmailAddress(billingDetails, billingEmailAddress);
      billingDetailsDAO.updateElectronicBillingAddress(billingDetails, billingElectronicBillingAddress);
      billingDetailsDAO.updateElectronicBillingOperator(billingDetails, billingElectronicBillingOperator);
      billingDetailsDAO.updateCompanyIdentifier(billingDetails, billingCompanyIdentifier);
      billingDetailsDAO.updateReferenceNumber(billingDetails, billingReferenceNumber);
      billingDetailsDAO.updateNotes(billingDetails, billingNotes);
    }
    
    // Tags

    schoolDAO.updateTags(school, tagEntities);

    // Addresses
    
    Set<Long> existingAddresses = new HashSet<>();
    int addressCount = requestContext.getInteger("addressTable.rowCount");
    for (int i = 0; i < addressCount; i++) {
      String colPrefix = "addressTable." + i;
      Long addressId = requestContext.getLong(colPrefix + ".addressId");
      Boolean defaultAddress = requestContext.getBoolean(colPrefix + ".defaultAddress");
      ContactType contactType = contactTypeDAO.findById(requestContext.getLong(colPrefix + ".contactTypeId"));
      String name = requestContext.getString(colPrefix + ".name");
      String street = requestContext.getString(colPrefix + ".street");
      String postal = requestContext.getString(colPrefix + ".postal");
      String city = requestContext.getString(colPrefix + ".city");
      String country = requestContext.getString(colPrefix + ".country");
      boolean hasAddress = name != null || street != null || postal != null || city != null || country != null;
      if (addressId == -1 && hasAddress) {
        Address address = addressDAO.create(school.getContactInfo(), contactType, name, street, postal, city, country, defaultAddress);
        existingAddresses.add(address.getId());
      }
      else if (addressId > 0) {
        Address address = addressDAO.findById(addressId);
        if (hasAddress) {
          existingAddresses.add(addressId);
          addressDAO.update(address, defaultAddress, contactType, name, street, postal, city, country);
        }
      }
    }
    List<Address> addresses = school.getContactInfo().getAddresses();
    for (int i = addresses.size() - 1; i >= 0; i--) {
      Address address = addresses.get(i);
      if (!existingAddresses.contains(address.getId())) {
        addressDAO.delete(address);
      }
    }

    // E-mail addresses
    
    Set<Long> existingEmails = new HashSet<>();
    int emailCount = requestContext.getInteger("emailTable.rowCount");
    for (int i = 0; i < emailCount; i++) {
      String colPrefix = "emailTable." + i;
      Boolean defaultAddress = requestContext.getBoolean(colPrefix + ".defaultAddress");
      ContactType contactType = contactTypeDAO.findById(requestContext.getLong(colPrefix + ".contactTypeId"));
      String email = requestContext.getString(colPrefix + ".email");
      
      // Trim the email address
      email = email != null ? email.trim() : null;

      Long emailId = requestContext.getLong(colPrefix + ".emailId");
      if (emailId == -1 && email != null) {
        emailId = emailDAO.create(school.getContactInfo(), contactType, defaultAddress, email).getId();
        existingEmails.add(emailId);
      }
      else if (emailId > 0 && email != null) {
        existingEmails.add(emailId);
        emailDAO.update(emailDAO.findById(emailId), contactType, defaultAddress, email);
      }
    }
    List<Email> emails = school.getContactInfo().getEmails();
    for (int i = emails.size() - 1; i >= 0; i--) {
      Email email = emails.get(i);
      if (!existingEmails.contains(email.getId())) {
        emailDAO.delete(email);
      }
    }

    // Phone numbers
    
    Set<Long> existingPhoneNumbers = new HashSet<>();
    int phoneCount = requestContext.getInteger("phoneTable.rowCount");
    for (int i = 0; i < phoneCount; i++) {
      String colPrefix = "phoneTable." + i;
      Boolean defaultNumber = requestContext.getBoolean(colPrefix + ".defaultNumber");
      ContactType contactType = contactTypeDAO.findById(requestContext.getLong(colPrefix + ".contactTypeId"));
      String number = requestContext.getString(colPrefix + ".phone");
      Long phoneId = requestContext.getLong(colPrefix + ".phoneId");
      if (phoneId == -1 && number != null) {
        phoneId = phoneNumberDAO.create(school.getContactInfo(), contactType, defaultNumber, number).getId();
        existingPhoneNumbers.add(phoneId);
      }
      else if (phoneId > 0 && number != null) {
        phoneNumberDAO.update(phoneNumberDAO.findById(phoneId), contactType, defaultNumber, number);
        existingPhoneNumbers.add(phoneId);
      }
    }
    List<PhoneNumber> phoneNumbers = school.getContactInfo().getPhoneNumbers();
    for (int i = phoneNumbers.size() - 1; i >= 0; i--) {
      PhoneNumber phoneNumber = phoneNumbers.get(i);
      if (!existingPhoneNumbers.contains(phoneNumber.getId())) {
        phoneNumberDAO.delete(phoneNumber);
      }
    }
    
    // Variables

    Integer variableCount = requestContext.getInteger("variablesTable.rowCount");
    if (variableCount != null) {
      for (int i = 0; i < variableCount; i++) {
        String colPrefix = "variablesTable." + i;
        String key = requestContext.getRequest().getParameter(colPrefix + ".key");
        String value = requestContext.getRequest().getParameter(colPrefix + ".value");
        schoolVariableDAO.setVariable(school, key, value);
      }
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
