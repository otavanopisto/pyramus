package fi.pyramus.json.users;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.I18N.Messages;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.AddressDAO;
import fi.pyramus.dao.base.ContactTypeDAO;
import fi.pyramus.dao.base.EmailDAO;
import fi.pyramus.dao.base.PersonDAO;
import fi.pyramus.dao.base.PhoneNumberDAO;
import fi.pyramus.dao.base.TagDAO;
import fi.pyramus.dao.users.StaffMemberDAO;
import fi.pyramus.dao.users.UserIdentificationDAO;
import fi.pyramus.domainmodel.base.ContactType;
import fi.pyramus.domainmodel.base.Person;
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
 * The controller responsible of creating a new Pyramus user. 
 * 
 * @see fi.pyramus.views.users.CreateUserViewController
 */
public class CreateUserJSONRequestController extends JSONRequestController {

  /**
   * Processes the request to create a new user. Simply gathers the fields submitted from the
   * web page and adds the user to the database.
   * 
   * @param requestContext The JSON request context
   */
  public void process(JSONRequestContext requestContext) {
    StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffMemberDAO();
    AddressDAO addressDAO = DAOFactory.getInstance().getAddressDAO();
    EmailDAO emailDAO = DAOFactory.getInstance().getEmailDAO();
    PhoneNumberDAO phoneNumberDAO = DAOFactory.getInstance().getPhoneNumberDAO();
    TagDAO tagDAO = DAOFactory.getInstance().getTagDAO();
    ContactTypeDAO contactTypeDAO = DAOFactory.getInstance().getContactTypeDAO();
    PersonDAO personDAO = DAOFactory.getInstance().getPersonDAO();
    UserIdentificationDAO userIdentificationDAO = DAOFactory.getInstance().getUserIdentificationDAO();

    Long personId = requestContext.getLong("personId");
    
    int emailCount2 = requestContext.getInteger("emailTable.rowCount");
    for (int i = 0; i < emailCount2; i++) {
      String colPrefix = "emailTable." + i;
      String email = requestContext.getString(colPrefix + ".email");
      ContactType contactType = contactTypeDAO.findById(requestContext.getLong(colPrefix + ".contactTypeId"));
      if (email != null) {
        if (!UserUtils.isAllowedEmail(email, contactType, personId)) {
          throw new RuntimeException(Messages.getInstance().getText(requestContext.getRequest().getLocale(), "generic.errors.emailInUse"));
        }
      }
    }
    
    // Fields from the web page

    String firstName = requestContext.getString("firstName");
    String lastName = requestContext.getString("lastName");
    String title = requestContext.getString("title");
    Role role = Role.getRole(requestContext.getInteger("role"));
    String tagsText = requestContext.getString("tags");
    String authProvider = requestContext.getString("authProvider");
    String username = requestContext.getString("username");
    String password = requestContext.getString("password1");
    String password2 = requestContext.getString("password2");
    
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
    
    // Authentication
    
    String externalId = "-1";

    AuthenticationProvider authenticationProvider = AuthenticationProviderVault.getInstance().getAuthenticationProvider(authProvider);
    if (authenticationProvider instanceof InternalAuthenticationProvider) {
      InternalAuthenticationProvider internalAuthenticationProvider = (InternalAuthenticationProvider) authenticationProvider;
      
      boolean usernameBlank = StringUtils.isBlank(username);
      boolean passwordBlank = StringUtils.isBlank(password);
      
      if (!usernameBlank||!passwordBlank) {
        if (!passwordBlank) {
          if (!password.equals(password2))
            throw new SmvcRuntimeException(PyramusStatusCode.PASSWORD_MISMATCH, "Passwords don't match");
        }

        externalId = internalAuthenticationProvider.createCredentials(username, password);
      }
    } 
    
    // User

    Person person = personId != null ? personDAO.findById(personId) : personDAO.create(null, null, null, null, Boolean.FALSE);
    StaffMember user = userDAO.create(firstName, lastName, role, person, false);
    if (title != null)
      userDAO.updateTitle(user, title);
    
    if(person.getDefaultUser() == null){
      personDAO.updateDefaultUser(person, user);
    }
    
    // UserIdentification
    
    /**
     * Create userIdentification only for internal authentication providers, external providers will create the identification during first login
     */
    if(authenticationProvider instanceof InternalAuthenticationProvider){
      List<UserIdentification> userIdentifications = userIdentificationDAO.listByPerson(person);
      if (userIdentifications == null || userIdentifications.size() == 0) {
        userIdentificationDAO.create(person, authProvider, externalId);
      } else if (userIdentifications.size() > 1) {
        //TODO: Currently only 1 UserIdentification for person is supported, this may change in the future.
        throw new SmvcRuntimeException(PyramusStatusCode.MULTIPLE_IDENTIFICATIONS_FOR_PERSON, "Multiple UserIdentifications found for person");
      } else {
        UserIdentification userIdentification = userIdentificationDAO.updateAuthSource(userIdentifications.get(0), authProvider);
        userIdentificationDAO.updateExternalId(userIdentification, externalId);
      } 
    }
    
    // Tags
    userDAO.updateTags(user, tagEntities);
    
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
        addressDAO.create(user.getContactInfo(), contactType, name, street, postal, city, country, defaultAddress);
      }
    }
    
    // Email addresses

    int emailCount = requestContext.getInteger("emailTable.rowCount");
    for (int i = 0; i < emailCount; i++) {
      String colPrefix = "emailTable." + i;
      Boolean defaultAddress = requestContext.getBoolean(colPrefix + ".defaultAddress");
      ContactType contactType = contactTypeDAO.findById(requestContext.getLong(colPrefix + ".contactTypeId"));
      String email = requestContext.getString(colPrefix + ".email");
      if (email != null) {
        emailDAO.create(user.getContactInfo(), contactType, defaultAddress, email);
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
        phoneNumberDAO.create(user.getContactInfo(), contactType, defaultNumber, number);
      }
    }
    
    // Redirect to the Edit User view

    requestContext.setRedirectURL(requestContext.getRequest().getContextPath() + "/users/edituser.page?userId="
        + user.getId());
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
