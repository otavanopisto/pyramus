package fi.otavanopisto.pyramus.json.studentparents;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.AddressDAO;
import fi.otavanopisto.pyramus.dao.base.ContactTypeDAO;
import fi.otavanopisto.pyramus.dao.base.EmailDAO;
import fi.otavanopisto.pyramus.dao.base.OrganizationDAO;
import fi.otavanopisto.pyramus.dao.base.PhoneNumberDAO;
import fi.otavanopisto.pyramus.dao.users.InternalAuthDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.dao.users.StudentParentDAO;
import fi.otavanopisto.pyramus.dao.users.UserIdentificationDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Address;
import fi.otavanopisto.pyramus.domainmodel.base.ContactType;
import fi.otavanopisto.pyramus.domainmodel.base.Email;
import fi.otavanopisto.pyramus.domainmodel.base.Organization;
import fi.otavanopisto.pyramus.domainmodel.base.PhoneNumber;
import fi.otavanopisto.pyramus.domainmodel.users.InternalAuth;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.StudentParent;
import fi.otavanopisto.pyramus.domainmodel.users.UserIdentification;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.PyramusStatusCode;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.framework.UserUtils;
import fi.otavanopisto.pyramus.plugin.auth.AuthenticationProviderVault;
import fi.otavanopisto.pyramus.plugin.auth.InternalAuthenticationProvider;

/**
 * The controller responsible of editing an existing Student Parent. 
 * 
 * @see fi.otavanopisto.pyramus.views.studentparents.EditStudentParentViewController
 */
public class EditStudentParentJSONRequestController extends JSONRequestController {

  /**
   * Processes the request to edit a student parent. Simply gathers the fields submitted from the
   * web page and updates the database.
   * 
   * @param jsonRequestContext The JSON request context
   */
  public void process(JSONRequestContext requestContext) {
    StudentParentDAO studentParentDAO = DAOFactory.getInstance().getStudentParentDAO();
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    
    AddressDAO addressDAO = DAOFactory.getInstance().getAddressDAO();
    EmailDAO emailDAO = DAOFactory.getInstance().getEmailDAO();
    PhoneNumberDAO phoneNumberDAO = DAOFactory.getInstance().getPhoneNumberDAO();
    ContactTypeDAO contactTypeDAO = DAOFactory.getInstance().getContactTypeDAO();
    UserIdentificationDAO userIdentificationDAO = DAOFactory.getInstance().getUserIdentificationDAO();
    OrganizationDAO organizationDAO = DAOFactory.getInstance().getOrganizationDAO();

    Long loggedUserId = requestContext.getLoggedUserId();
    final StaffMember loggedUser = staffMemberDAO.findById(loggedUserId);
    
    Long userId = requestContext.getLong("userId");

    StudentParent studentParent = studentParentDAO.findById(userId);

    if (studentParent.getOrganization() != null) {
      // Check that the editing user has access to the organization
      if (!UserUtils.canAccessOrganization(loggedUser, studentParent.getOrganization())) {
        throw new RuntimeException("Cannot access users' organization");
      }
    } else {
      // Check that the editing user has generic access when users' organization is null
      if (!UserUtils.canAccessAllOrganizations(loggedUser)) {
        throw new RuntimeException("Cannot access users' organization");
      }
    }

    String firstName = requestContext.getString("firstName");
    String lastName = requestContext.getString("lastName");
    
    String username = requestContext.getString("username");
    String password = requestContext.getString("password1");
    String password2 = requestContext.getString("password2");
    
    Long organizationId = requestContext.getLong("organizationId");
    Organization organization = null;
    
    if (organizationId != null) {
      organization = organizationDAO.findById(organizationId);
    }
    
    if (organization != null) {
      // Check that the editing user has access to the organization
      if (!UserUtils.canAccessOrganization(loggedUser, organization)) {
        throw new RuntimeException("Cannot access organization");
      }
    } else {
      // Check that the editing user can set the organization as null
      if (!UserUtils.canAccessAllOrganizations(loggedUser)) {
        throw new RuntimeException("Cannot access organization");
      }
    }

    // #921: Check username
    if (!StringUtils.isBlank(username)) {
      InternalAuthDAO internalAuthDAO = DAOFactory.getInstance().getInternalAuthDAO();
      InternalAuth internalAuth = internalAuthDAO.findByUsername(username);
      if (internalAuth != null) {
        UserIdentification userIdentification = userIdentificationDAO.findByAuthSourceAndExternalId("internal", internalAuth.getId().toString());
        if (userIdentification != null && !studentParent.getPerson().getId().equals(userIdentification.getPerson().getId())) {
          throw new RuntimeException(Messages.getInstance().getText(requestContext.getRequest().getLocale(), "generic.errors.usernameInUse"));
        }
      }
    }
    
    int emailCount2 = requestContext.getInteger("emailTable.rowCount");
    for (int i = 0; i < emailCount2; i++) {
      String colPrefix = "emailTable." + i;
      String email = StringUtils.trim(requestContext.getString(colPrefix + ".email"));
      if (StringUtils.isNotBlank(email)) {
        ContactType contactType = contactTypeDAO.findById(requestContext.getLong(colPrefix + ".contactTypeId"));
  
        if (!UserUtils.isAllowedEmail(email, contactType, studentParent.getPerson().getId())) {
          throw new RuntimeException(Messages.getInstance().getText(requestContext.getRequest().getLocale(), "generic.errors.emailInUse"));
        }
      }
    }
    
    studentParentDAO.update(studentParent, firstName, lastName);
    
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
        Address address = addressDAO.create(studentParent.getContactInfo(), contactType, name, street, postal, city, country, defaultAddress);
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
    List<Address> addresses = studentParent.getContactInfo().getAddresses();
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
      String email = StringUtils.trim(requestContext.getString(colPrefix + ".email"));

      Long emailId = requestContext.getLong(colPrefix + ".emailId");
      if (emailId == -1 && email != null) {
        emailId = emailDAO.create(studentParent.getContactInfo(), contactType, defaultAddress, email).getId();
        existingEmails.add(emailId);
      }
      else if (emailId > 0 && email != null) {
        existingEmails.add(emailId);
        emailDAO.update(emailDAO.findById(emailId), contactType, defaultAddress, email);
      }
    }
    List<Email> emails = studentParent.getContactInfo().getEmails();
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
        phoneId = phoneNumberDAO.create(studentParent.getContactInfo(), contactType, defaultNumber, number).getId();
        existingPhoneNumbers.add(phoneId);
      }
      else if (phoneId > 0 && number != null) {
        phoneNumberDAO.update(phoneNumberDAO.findById(phoneId), contactType, defaultNumber, number);
        existingPhoneNumbers.add(phoneId);
      }
    }
    List<PhoneNumber> phoneNumbers = studentParent.getContactInfo().getPhoneNumbers();
    for (int i = phoneNumbers.size() - 1; i >= 0; i--) {
      PhoneNumber phoneNumber = phoneNumbers.get(i);
      if (!existingPhoneNumbers.contains(phoneNumber.getId())) {
        phoneNumberDAO.delete(phoneNumber);
      }
    }
    
    boolean usernameBlank = StringUtils.isBlank(username);
    boolean passwordBlank = StringUtils.isBlank(password);
    
    if (!usernameBlank||!passwordBlank) {
      if (!passwordBlank) {
        if (!password.equals(password2))
          throw new SmvcRuntimeException(PyramusStatusCode.PASSWORD_MISMATCH, "Passwords don't match");
      }
      
      // TODO: Support for multiple internal authentication providers
      List<InternalAuthenticationProvider> internalAuthenticationProviders = AuthenticationProviderVault.getInstance().getInternalAuthenticationProviders();
      if (internalAuthenticationProviders.size() == 1) {
        InternalAuthenticationProvider internalAuthenticationProvider = internalAuthenticationProviders.get(0);
        if (internalAuthenticationProvider != null) {
          UserIdentification userIdentification = userIdentificationDAO.findByAuthSourceAndPerson(internalAuthenticationProvider.getName(), studentParent.getPerson());
          
          if (internalAuthenticationProvider.canUpdateCredentials()) {
            if (userIdentification == null) {
              String externalId = internalAuthenticationProvider.createCredentials(username, password);
              userIdentificationDAO.create(studentParent.getPerson(), internalAuthenticationProvider.getName(), externalId);
            } else {
              if ("-1".equals(userIdentification.getExternalId())) {
                String externalId = internalAuthenticationProvider.createCredentials(username, password);
                userIdentificationDAO.updateExternalId(userIdentification, externalId);
              } else {
                if (!StringUtils.isBlank(username))
                  internalAuthenticationProvider.updateUsername(userIdentification.getExternalId(), username);
              
                if (!StringUtils.isBlank(password))
                  internalAuthenticationProvider.updatePassword(userIdentification.getExternalId(), password);
              }
            }
          }
        }
      }
    }
    
    requestContext.setRedirectURL(requestContext.getReferer(true));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
