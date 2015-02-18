package fi.pyramus.json.users;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.I18N.Messages;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.AddressDAO;
import fi.pyramus.dao.base.ContactTypeDAO;
import fi.pyramus.dao.base.EmailDAO;
import fi.pyramus.dao.base.PhoneNumberDAO;
import fi.pyramus.dao.base.TagDAO;
import fi.pyramus.dao.users.StaffMemberDAO;
import fi.pyramus.dao.users.UserIdentificationDAO;
import fi.pyramus.dao.users.UserVariableDAO;
import fi.pyramus.domainmodel.base.Address;
import fi.pyramus.domainmodel.base.ContactType;
import fi.pyramus.domainmodel.base.Email;
import fi.pyramus.domainmodel.base.PhoneNumber;
import fi.pyramus.domainmodel.base.Tag;
import fi.pyramus.domainmodel.users.Role;
import fi.pyramus.domainmodel.users.StaffMember;
import fi.pyramus.domainmodel.users.UserIdentification;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.PyramusStatusCode;
import fi.pyramus.framework.UserRole;
import fi.pyramus.framework.UserUtils;
import fi.pyramus.plugin.auth.AuthenticationProvider;
import fi.pyramus.plugin.auth.AuthenticationProviderVault;
import fi.pyramus.plugin.auth.InternalAuthenticationProvider;

/**
 * The controller responsible of editing an existing Pyramus user. 
 * 
 * @see fi.pyramus.views.users.EditUserViewController
 */
public class EditUserJSONRequestController extends JSONRequestController {

  /**
   * Processes the request to edit an user. Simply gathers the fields submitted from the
   * web page and updates the database.
   * 
   * @param jsonRequestContext The JSON request context
   */
  public void process(JSONRequestContext requestContext) {
    StaffMemberDAO staffDAO = DAOFactory.getInstance().getStaffMemberDAO();
    
    UserVariableDAO userVariableDAO = DAOFactory.getInstance().getUserVariableDAO();
    AddressDAO addressDAO = DAOFactory.getInstance().getAddressDAO();
    EmailDAO emailDAO = DAOFactory.getInstance().getEmailDAO();
    PhoneNumberDAO phoneNumberDAO = DAOFactory.getInstance().getPhoneNumberDAO();
    TagDAO tagDAO = DAOFactory.getInstance().getTagDAO();
    ContactTypeDAO contactTypeDAO = DAOFactory.getInstance().getContactTypeDAO();
    UserIdentificationDAO userIdentificationDAO = DAOFactory.getInstance().getUserIdentificationDAO();

    Long loggedUserId = requestContext.getLoggedUserId();
    StaffMember loggedUser = staffDAO.findById(loggedUserId);
    Role loggedUserRole = loggedUser.getRole();
    
    Long userId = requestContext.getLong("userId");

    StaffMember user = staffDAO.findById(userId);

    List<UserIdentification> userIdentifications = userIdentificationDAO.listByPerson(user.getPerson());
    
    //TODO: Currently only 1 UserIdentification for person is supported, this may change in the future.
    if(userIdentifications.size() > 1) {
      throw new SmvcRuntimeException(PyramusStatusCode.MULTIPLE_IDENTIFICATIONS_FOR_PERSON, "Multiple UserIdentifications found for person");
    }
    
    UserIdentification userIdentification = userIdentifications.isEmpty() ? null : userIdentifications.get(0);
    
    String firstName = requestContext.getString("firstName");
    String lastName = requestContext.getString("lastName");
    String title = requestContext.getString("title");
    Role role = Role.getRole(requestContext.getInteger("role").intValue());
    String username = requestContext.getString("username");
    String password = requestContext.getString("password1");
    String password2 = requestContext.getString("password2");
    String tagsText = requestContext.getString("tags");
    
    int emailCount2 = requestContext.getInteger("emailTable.rowCount");
    for (int i = 0; i < emailCount2; i++) {
      String colPrefix = "emailTable." + i;
      String email = requestContext.getString(colPrefix + ".email");
      ContactType contactType = contactTypeDAO.findById(requestContext.getLong(colPrefix + ".contactTypeId"));

      if (!UserUtils.isAllowedEmail(email, contactType, user.getPerson().getId()))
        throw new RuntimeException(Messages.getInstance().getText(requestContext.getRequest().getLocale(), "generic.errors.emailInUse"));
    }
    
    Set<Tag> tagEntities = new HashSet<Tag>();
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
    
    staffDAO.update(user, firstName, lastName, role);
    staffDAO.updateTitle(user, title);

    // Tags

    staffDAO.updateTags(user, tagEntities);
    
    // Addresses
    
    Set<Long> existingAddresses = new HashSet<Long>();
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
        Address address = addressDAO.create(user.getContactInfo(), contactType, name, street, postal, city, country, defaultAddress);
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
    List<Address> addresses = user.getContactInfo().getAddresses();
    for (int i = addresses.size() - 1; i >= 0; i--) {
      Address address = addresses.get(i);
      if (!existingAddresses.contains(address.getId())) {
        addressDAO.delete(address);
      }
    }

    // E-mail addresses
    
    Set<Long> existingEmails = new HashSet<Long>();
    int emailCount = requestContext.getInteger("emailTable.rowCount");
    for (int i = 0; i < emailCount; i++) {
      String colPrefix = "emailTable." + i;
      Boolean defaultAddress = requestContext.getBoolean(colPrefix + ".defaultAddress");
      ContactType contactType = contactTypeDAO.findById(requestContext.getLong(colPrefix + ".contactTypeId"));
      String email = requestContext.getString(colPrefix + ".email");
      Long emailId = requestContext.getLong(colPrefix + ".emailId");
      if (emailId == -1 && email != null) {
        emailId = emailDAO.create(user.getContactInfo(), contactType, defaultAddress, email).getId();
        existingEmails.add(emailId);
      }
      else if (emailId > 0 && email != null) {
        existingEmails.add(emailId);
        emailDAO.update(emailDAO.findById(emailId), contactType, defaultAddress, email);
      }
    }
    List<Email> emails = user.getContactInfo().getEmails();
    for (int i = emails.size() - 1; i >= 0; i--) {
      Email email = emails.get(i);
      if (!existingEmails.contains(email.getId())) {
        emailDAO.delete(email);
      }
    }

    // Phone numbers
    
    Set<Long> existingPhoneNumbers = new HashSet<Long>();
    int phoneCount = requestContext.getInteger("phoneTable.rowCount");
    for (int i = 0; i < phoneCount; i++) {
      String colPrefix = "phoneTable." + i;
      Boolean defaultNumber = requestContext.getBoolean(colPrefix + ".defaultNumber");
      ContactType contactType = contactTypeDAO.findById(requestContext.getLong(colPrefix + ".contactTypeId"));
      String number = requestContext.getString(colPrefix + ".phone");
      Long phoneId = requestContext.getLong(colPrefix + ".phoneId");
      if (phoneId == -1 && number != null) {
        phoneId = phoneNumberDAO.create(user.getContactInfo(), contactType, defaultNumber, number).getId();
        existingPhoneNumbers.add(phoneId);
      }
      else if (phoneId > 0 && number != null) {
        phoneNumberDAO.update(phoneNumberDAO.findById(phoneId), contactType, defaultNumber, number);
        existingPhoneNumbers.add(phoneId);
      }
    }
    List<PhoneNumber> phoneNumbers = user.getContactInfo().getPhoneNumbers();
    for (int i = phoneNumbers.size() - 1; i >= 0; i--) {
      PhoneNumber phoneNumber = phoneNumbers.get(i);
      if (!existingPhoneNumbers.contains(phoneNumber.getId())) {
        phoneNumberDAO.delete(phoneNumber);
      }
    }

    if (Role.ADMINISTRATOR.equals(loggedUserRole)) {
      if (userIdentification != null) {
        String authProvider = requestContext.getString("authProvider");
        if (!userIdentification.getAuthSource().equals(authProvider)) {
          userIdentificationDAO.updateAuthSource(userIdentification, authProvider);
        }
      }
      
      Integer variableCount = requestContext.getInteger("variablesTable.rowCount");
      for (int i = 0; i < (variableCount != null ? variableCount : 0); i++) {
        String colPrefix = "variablesTable." + i;
        String variableKey = requestContext.getString(colPrefix + ".key");
        String variableValue = requestContext.getString(colPrefix + ".value");
        userVariableDAO.setUserVariable(user, variableKey, variableValue);
      }
    }
    
    boolean usernameBlank = StringUtils.isBlank(username);
    boolean passwordBlank = StringUtils.isBlank(password);
    
    if (!usernameBlank||!passwordBlank) {
      if (!passwordBlank) {
        if (!password.equals(password2))
          throw new SmvcRuntimeException(PyramusStatusCode.PASSWORD_MISMATCH, "Passwords don't match");
      }
      
      if (userIdentification != null) {
        AuthenticationProvider authenticationProvider = AuthenticationProviderVault.getInstance().getAuthenticationProvider(userIdentification.getAuthSource());
        if (authenticationProvider instanceof InternalAuthenticationProvider) {
          InternalAuthenticationProvider internalAuthenticationProvider = (InternalAuthenticationProvider) authenticationProvider;
          if (internalAuthenticationProvider.canUpdateCredentials()) {
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
    
    if (requestContext.getLoggedUserId().equals(user.getId())) {
      user = staffDAO.findById(user.getId());
      HttpSession session = requestContext.getRequest().getSession(true);
      session.setAttribute("loggedUserName", user.getFullName());
      session.setAttribute("loggedUserRole", Role.valueOf(user.getRole().name()));
    }

    requestContext.setRedirectURL(requestContext.getReferer(true));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
