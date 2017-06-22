package fi.otavanopisto.pyramus.json.settings;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

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
import fi.otavanopisto.pyramus.domainmodel.base.BillingDetails;
import fi.otavanopisto.pyramus.domainmodel.base.ContactType;
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
public class CreateSchoolJSONRequestController extends JSONRequestController {

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

    String schoolCode = requestContext.getString("code");
    String schoolName = requestContext.getString("name");
    String tagsText = requestContext.getString("tags");
    
    Long schoolFieldId = requestContext.getLong("schoolFieldId");
    SchoolField schoolField = null;
    if (schoolFieldId != null && schoolFieldId.intValue() >= 0)
      schoolField = schoolFieldDAO.findById(schoolFieldId);

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
    
    BillingDetails billingDetails = billingDetailsDAO.create(
        billingPersonName, billingCompanyName, billingStreetAddress1, billingStreetAddress2, 
        billingPostalCode, billingCity, billingRegion, billingCountry, billingPhoneNumber, 
        billingEmailAddress, billingElectronicBillingAddress, billingElectronicBillingOperator,
        billingCompanyIdentifier, billingReferenceNumber, billingNotes);
    
    School school = schoolDAO.create(schoolCode, schoolName, schoolField, billingDetails);
    
    // Tags
    
    schoolDAO.updateTags(school, tagEntities);

    // Addresses
    
    int addressCount = requestContext.getInteger("addressTable.rowCount");
    for (int i = 0; i < addressCount; i++) {
      String colPrefix = "addressTable." + i;
      Boolean defaultAddress = requestContext.getBoolean(colPrefix + ".defaultAddress");
      ContactType contactType = contactTypeDAO.findById(requestContext.getLong(colPrefix + ".contactTypeId"));
      String name = requestContext.getString(colPrefix + ".name");
      String street = requestContext.getString(colPrefix + ".street");
      String postal = requestContext.getString(colPrefix + ".postal");
      String city = requestContext.getString(colPrefix + ".city");
      String country = requestContext.getString(colPrefix + ".country");
      boolean hasAddress = name != null || street != null || postal != null || city != null || country != null;
      if (hasAddress) {
        addressDAO.create(school.getContactInfo(), contactType, name, street, postal, city, country, defaultAddress);
      }
    }
    
    // Email addresses

    int emailCount = requestContext.getInteger("emailTable.rowCount");
    for (int i = 0; i < emailCount; i++) {
      String colPrefix = "emailTable." + i;
      Boolean defaultAddress = requestContext.getBoolean(colPrefix + ".defaultAddress");
      ContactType contactType = contactTypeDAO.findById(requestContext.getLong(colPrefix + ".contactTypeId"));
      String email = requestContext.getString(colPrefix + ".email");
      
      // Trim the email address
      email = email != null ? email.trim() : null;

      if (email != null) {
        emailDAO.create(school.getContactInfo(), contactType, defaultAddress, email);
      }
    }
    
    // Phone numbers

    int phoneCount = requestContext.getInteger("phoneTable.rowCount");
    for (int i = 0; i < phoneCount; i++) {
      String colPrefix = "phoneTable." + i;
      Boolean defaultNumber = requestContext.getBoolean(colPrefix + ".defaultNumber");
      ContactType contactType = contactTypeDAO.findById(requestContext.getLong(colPrefix + ".contactTypeId"));
      String number = requestContext.getString(colPrefix + ".phone");
      if (number != null) {
        phoneNumberDAO.create(school.getContactInfo(), contactType, defaultNumber, number);
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
    
    String redirectURL = requestContext.getRequest().getContextPath() + "/settings/editschool.page?school=" + school.getId();
    String refererAnchor = requestContext.getRefererAnchor();
    
    if (!StringUtils.isBlank(refererAnchor))
      redirectURL += "#" + refererAnchor;

    requestContext.setRedirectURL(redirectURL);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
