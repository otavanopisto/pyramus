package fi.otavanopisto.pyramus.json.users;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.internetix.smvc.controllers.RequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.AddressDAO;
import fi.otavanopisto.pyramus.dao.base.ContactTypeDAO;
import fi.otavanopisto.pyramus.dao.base.EmailDAO;
import fi.otavanopisto.pyramus.dao.base.OrganizationDAO;
import fi.otavanopisto.pyramus.dao.base.PersonDAO;
import fi.otavanopisto.pyramus.dao.base.PhoneNumberDAO;
import fi.otavanopisto.pyramus.dao.base.StudyProgrammeDAO;
import fi.otavanopisto.pyramus.dao.base.TagDAO;
import fi.otavanopisto.pyramus.dao.users.InternalAuthDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.dao.users.UserIdentificationDAO;
import fi.otavanopisto.pyramus.dao.users.UserVariableDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Address;
import fi.otavanopisto.pyramus.domainmodel.base.ContactType;
import fi.otavanopisto.pyramus.domainmodel.base.Email;
import fi.otavanopisto.pyramus.domainmodel.base.Organization;
import fi.otavanopisto.pyramus.domainmodel.base.PhoneNumber;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme;
import fi.otavanopisto.pyramus.domainmodel.base.Tag;
import fi.otavanopisto.pyramus.domainmodel.users.InternalAuth;
import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.UserIdentification;
import fi.otavanopisto.pyramus.framework.JSONRequestController2;
import fi.otavanopisto.pyramus.framework.PyramusRequestControllerAccess;
import fi.otavanopisto.pyramus.framework.PyramusStatusCode;
import fi.otavanopisto.pyramus.framework.StaffMemberProperties;
import fi.otavanopisto.pyramus.framework.UserUtils;
import fi.otavanopisto.pyramus.plugin.auth.AuthenticationProviderVault;
import fi.otavanopisto.pyramus.plugin.auth.InternalAuthenticationProvider;

/**
 * The controller responsible of editing an existing Pyramus user. 
 * 
 * @see fi.otavanopisto.pyramus.views.users.EditUserViewController
 */
public class EditUserJSONRequestController extends JSONRequestController2 {

  public EditUserJSONRequestController() {
    super(PyramusRequestControllerAccess.REQUIRELOGIN);
  }

  @Override
  protected boolean checkAccess(RequestContext requestContext) {
    if (!requestContext.isLoggedIn()) {
      return false;
    }

    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();

    Long loggedUserId = requestContext.getLoggedUserId();
    StaffMember staffMember = staffMemberDAO.findById(loggedUserId);
    Long userId = requestContext.getLong("userId");

    return staffMember.hasRole(Role.ADMINISTRATOR) || Objects.equals(loggedUserId, userId);
  }

  /**
   * Processes the request to edit an user. Simply gathers the fields submitted from the
   * web page and updates the database.
   * 
   * @param jsonRequestContext The JSON request context
   */
  public void process(JSONRequestContext requestContext) {
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    
    UserVariableDAO userVariableDAO = DAOFactory.getInstance().getUserVariableDAO();
    AddressDAO addressDAO = DAOFactory.getInstance().getAddressDAO();
    EmailDAO emailDAO = DAOFactory.getInstance().getEmailDAO();
    PhoneNumberDAO phoneNumberDAO = DAOFactory.getInstance().getPhoneNumberDAO();
    TagDAO tagDAO = DAOFactory.getInstance().getTagDAO();
    ContactTypeDAO contactTypeDAO = DAOFactory.getInstance().getContactTypeDAO();
    UserIdentificationDAO userIdentificationDAO = DAOFactory.getInstance().getUserIdentificationDAO();
    OrganizationDAO organizationDAO = DAOFactory.getInstance().getOrganizationDAO();
    StudyProgrammeDAO studyProgrammeDAO = DAOFactory.getInstance().getStudyProgrammeDAO();

    Long loggedUserId = requestContext.getLoggedUserId();
    final StaffMember loggedUser = staffMemberDAO.findById(loggedUserId);
    
    Long userId = requestContext.getLong("userId");

    StaffMember staffMember = staffMemberDAO.findById(userId);

    if (staffMember.getOrganization() != null) {
      // Check that the editing user has access to the organization
      if (!UserUtils.canAccessOrganization(loggedUser, staffMember.getOrganization())) {
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
    String title = requestContext.getString("title");
    
    String username = requestContext.getString("username");
    String password = requestContext.getString("password1");
    String password2 = requestContext.getString("password2");
    String tagsText = requestContext.getString("tags");
    
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
        if (userIdentification != null && !staffMember.getPerson().getId().equals(userIdentification.getPerson().getId())) {
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
  
        if (!UserUtils.isAllowedEmail(email, contactType, staffMember.getPerson().getId())) {
          throw new RuntimeException(Messages.getInstance().getText(requestContext.getRequest().getLocale(), "generic.errors.emailInUse"));
        }
      }
    }
    
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
    
    staffMemberDAO.update(staffMember, organization, firstName, lastName);

    if (loggedUser.hasRole(Role.ADMINISTRATOR)) {
      Boolean accountActive = requestContext.getBoolean("accountActive");
      staffMemberDAO.updateEnabled(staffMember, accountActive);

      String[] roleSelections = StringUtils.isNotBlank(requestContext.getString("role")) ? requestContext.getString("role").split(",") : new String[0];
      Set<Role> roles = new HashSet<>(roleSelections.length);
      for (String roleSelection : roleSelections) {
        roles.add(Role.valueOf(roleSelection));
      }
      staffMemberDAO.updateRoles(staffMember, roles);
      
      Integer propertyCount = requestContext.getInteger("propertiesTable.rowCount");
      for (int i = 0; i < (propertyCount != null ? propertyCount : 0); i++) {
        String colPrefix = "propertiesTable." + i;
        String propertyKey = requestContext.getString(colPrefix + ".key");
        String propertyValue = requestContext.getString(colPrefix + ".value");
        if (StaffMemberProperties.isProperty(propertyKey)) {
          staffMember.getProperties().put(propertyKey, propertyValue);
        }
      }
    }

    staffMemberDAO.updateTitle(staffMember, title);
    
    // SSN

    String ssn = requestContext.getString("ssn");
    String existingSsn = staffMember.getPerson().getSocialSecurityNumber();
    if (!StringUtils.equals(ssn, existingSsn)) {
      PersonDAO personDAO = DAOFactory.getInstance().getPersonDAO();
      personDAO.updateSocialSecurityNumber(staffMember.getPerson(), ssn);
    }

    // Tags

    staffMemberDAO.updateTags(staffMember, tagEntities);
    
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
        Address address = addressDAO.create(staffMember.getContactInfo(), contactType, name, street, postal, city, country, defaultAddress);
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
    List<Address> addresses = staffMember.getContactInfo().getAddresses();
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
        emailId = emailDAO.create(staffMember.getContactInfo(), contactType, defaultAddress, email).getId();
        existingEmails.add(emailId);
      }
      else if (emailId > 0 && email != null) {
        existingEmails.add(emailId);
        emailDAO.update(emailDAO.findById(emailId), contactType, defaultAddress, email);
      }
    }
    List<Email> emails = staffMember.getContactInfo().getEmails();
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
        phoneId = phoneNumberDAO.create(staffMember.getContactInfo(), contactType, defaultNumber, number).getId();
        existingPhoneNumbers.add(phoneId);
      }
      else if (phoneId > 0 && number != null) {
        phoneNumberDAO.update(phoneNumberDAO.findById(phoneId), contactType, defaultNumber, number);
        existingPhoneNumbers.add(phoneId);
      }
    }
    List<PhoneNumber> phoneNumbers = staffMember.getContactInfo().getPhoneNumbers();
    for (int i = phoneNumbers.size() - 1; i >= 0; i--) {
      PhoneNumber phoneNumber = phoneNumbers.get(i);
      if (!existingPhoneNumbers.contains(phoneNumber.getId())) {
        phoneNumberDAO.delete(phoneNumber);
      }
    }
    
    // Study programmes
    
    Set<StudyProgramme> studyProgrammes = new HashSet<>();
    String studyProgrammeStr = requestContext.getString("studyProgrammes");
    if (!StringUtils.isEmpty(studyProgrammeStr)) {
      List<Long> studyProgrammeIds = Stream.of(studyProgrammeStr
          .split(","))
          .map(Long::parseLong)
          .collect(Collectors.toList());
      for (Long studyProgrammeId : studyProgrammeIds) {
        StudyProgramme studyProgramme = studyProgrammeDAO.findById(studyProgrammeId);
        if (studyProgramme != null) {
          studyProgrammes.add(studyProgramme);
        }
      }
    }
    staffMemberDAO.setStudyProgrammes(staffMember, studyProgrammes);
    
    // Variables

    if (loggedUser.hasRole(Role.ADMINISTRATOR)) {
      Integer variableCount = requestContext.getInteger("variablesTable.rowCount");
      for (int i = 0; i < (variableCount != null ? variableCount : 0); i++) {
        String colPrefix = "variablesTable." + i;
        String variableKey = requestContext.getString(colPrefix + ".key");
        String variableValue = requestContext.getString(colPrefix + ".value");
        userVariableDAO.setUserVariable(staffMember, variableKey, variableValue);
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
          UserIdentification userIdentification = userIdentificationDAO.findByAuthSourceAndPerson(internalAuthenticationProvider.getName(), staffMember.getPerson());
          
          if (internalAuthenticationProvider.canUpdateCredentials()) {
            if (userIdentification == null) {
              String externalId = internalAuthenticationProvider.createCredentials(username, password);
              userIdentificationDAO.create(staffMember.getPerson(), internalAuthenticationProvider.getName(), externalId);
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
    
    if (requestContext.getLoggedUserId().equals(staffMember.getId())) {
      staffMember = staffMemberDAO.findById(staffMember.getId());
      HttpSession session = requestContext.getRequest().getSession(true);
      session.setAttribute("loggedUserName", staffMember.getFullName());
      session.setAttribute("loggedUserRoles", Set.copyOf(staffMember.getRoles()));
    }

    requestContext.setRedirectURL(requestContext.getReferer(true));
  }

}
