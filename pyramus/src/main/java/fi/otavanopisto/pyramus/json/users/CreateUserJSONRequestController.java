package fi.otavanopisto.pyramus.json.users;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.JSONRequestContext;
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
import fi.otavanopisto.pyramus.domainmodel.base.ContactType;
import fi.otavanopisto.pyramus.domainmodel.base.Organization;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme;
import fi.otavanopisto.pyramus.domainmodel.base.Tag;
import fi.otavanopisto.pyramus.domainmodel.users.InternalAuth;
import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.PyramusStatusCode;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.framework.UserUtils;
import fi.otavanopisto.pyramus.plugin.auth.AuthenticationProviderVault;
import fi.otavanopisto.pyramus.plugin.auth.InternalAuthenticationProvider;

/**
 * The controller responsible of creating a new Pyramus user. 
 * 
 * @see fi.otavanopisto.pyramus.views.users.CreateUserViewController
 */
public class CreateUserJSONRequestController extends JSONRequestController {

  /**
   * Processes the request to create a new user. Simply gathers the fields submitted from the
   * web page and adds the user to the database.
   * 
   * @param requestContext The JSON request context
   */
  public void process(JSONRequestContext requestContext) {
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    AddressDAO addressDAO = DAOFactory.getInstance().getAddressDAO();
    EmailDAO emailDAO = DAOFactory.getInstance().getEmailDAO();
    PhoneNumberDAO phoneNumberDAO = DAOFactory.getInstance().getPhoneNumberDAO();
    TagDAO tagDAO = DAOFactory.getInstance().getTagDAO();
    ContactTypeDAO contactTypeDAO = DAOFactory.getInstance().getContactTypeDAO();
    PersonDAO personDAO = DAOFactory.getInstance().getPersonDAO();
    UserIdentificationDAO userIdentificationDAO = DAOFactory.getInstance().getUserIdentificationDAO();
    OrganizationDAO organizationDAO = DAOFactory.getInstance().getOrganizationDAO();
    StudyProgrammeDAO studyProgrammeDAO = DAOFactory.getInstance().getStudyProgrammeDAO();

    Long personId = requestContext.getLong("personId");
    // If the user is being created under existing person, skip credentials
    boolean createCredentials = personId == null;
    
    int emailCount2 = requestContext.getInteger("emailTable.rowCount");
    for (int i = 0; i < emailCount2; i++) {
      String colPrefix = "emailTable." + i;
      String email = StringUtils.trim(requestContext.getString(colPrefix + ".email"));
      if (StringUtils.isNotBlank(email)) {
        ContactType contactType = contactTypeDAO.findById(requestContext.getLong(colPrefix + ".contactTypeId"));
        
        if (!UserUtils.isAllowedEmail(email, contactType, personId)) {
          throw new RuntimeException(Messages.getInstance().getText(requestContext.getRequest().getLocale(), "generic.errors.emailInUse"));
        }
      }
    }
    
    // Fields from the web page

    String firstName = requestContext.getString("firstName");
    String lastName = requestContext.getString("lastName");
    String title = requestContext.getString("title");

    String[] roleSelections = StringUtils.isNotBlank(requestContext.getString("role")) ? requestContext.getString("role").split(",") : new String[0];
    Set<Role> roles = new HashSet<>(roleSelections.length);
    for (String roleSelection : roleSelections) {
      roles.add(Role.valueOf(roleSelection));
    }

    String tagsText = requestContext.getString("tags");
    String username = requestContext.getString("username");
    String password = requestContext.getString("password1");
    String password2 = requestContext.getString("password2");
    Long organizationId = requestContext.getLong("organizationId");
    Boolean accountActive = requestContext.getBoolean("accountActive");

    User loggedUser = staffMemberDAO.findById(requestContext.getLoggedUserId());
    Organization organization = organizationId != null ? organizationDAO.findById(organizationId) : null;

    if (!UserUtils.canAccessOrganization(loggedUser, organization)) {
      throw new SmvcRuntimeException(PyramusStatusCode.UNAUTHORIZED, "Invalid organization.");
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
    
    // User

    Person person = personId != null ? personDAO.findById(personId) : personDAO.create(null, null, null, null, Boolean.FALSE);
    StaffMember staffMember = staffMemberDAO.create(organization, firstName, lastName, roles, person, false);
    staffMember = staffMemberDAO.updateEnabled(staffMember, accountActive);

    if (StringUtils.isNotBlank(title)) {
      staffMember = staffMemberDAO.updateTitle(staffMember, title);
    }
    
    if (person.getDefaultUser() == null) {
      person = personDAO.updateDefaultUser(person, staffMember);
    }
    
    // Authentication
    
    if (AuthenticationProviderVault.getInstance().hasInternalStrategies() && createCredentials) {
      boolean usernameBlank = StringUtils.isBlank(username);
      boolean passwordBlank = StringUtils.isBlank(password);
      
      // TODO: Support multiple internal authentication sources
      if (!usernameBlank) {

        // #921: Check username
        InternalAuthDAO internalAuthDAO = DAOFactory.getInstance().getInternalAuthDAO();
        InternalAuth internalAuth = internalAuthDAO.findByUsername(username);
        if (internalAuth != null) {
          throw new RuntimeException(Messages.getInstance().getText(requestContext.getRequest().getLocale(), "generic.errors.usernameInUse"));
        }
        
        InternalAuthenticationProvider internalAuthenticationProvider = AuthenticationProviderVault.getInstance().getInternalAuthenticationProviders().get(0);
        
        if (!passwordBlank) {
          if (!password.equals(password2))
            throw new SmvcRuntimeException(PyramusStatusCode.PASSWORD_MISMATCH, "Passwords don't match");
        } else {
          throw new RuntimeException(Messages.getInstance().getText(requestContext.getRequest().getLocale(), "generic.errors.nopassword"));
        }

        String externalId = internalAuthenticationProvider.createCredentials(username, password);
        userIdentificationDAO.create(person, internalAuthenticationProvider.getName(), externalId);
      }
    }
    
    // Tags
    staffMemberDAO.updateTags(staffMember, tagEntities);
    
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
        addressDAO.create(staffMember.getContactInfo(), contactType, name, street, postal, city, country, defaultAddress);
      }
    }
    
    // Email addresses

    int emailCount = requestContext.getInteger("emailTable.rowCount");
    for (int i = 0; i < emailCount; i++) {
      String colPrefix = "emailTable." + i;
      Boolean defaultAddress = requestContext.getBoolean(colPrefix + ".defaultAddress");
      ContactType contactType = contactTypeDAO.findById(requestContext.getLong(colPrefix + ".contactTypeId"));
      String email = StringUtils.trim(requestContext.getString(colPrefix + ".email"));
      
      if (StringUtils.isNotBlank(email)) {
        emailDAO.create(staffMember.getContactInfo(), contactType, defaultAddress, email);
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
        phoneNumberDAO.create(staffMember.getContactInfo(), contactType, defaultNumber, number);
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
    
    // Redirect to the Edit User view

    requestContext.setRedirectURL(requestContext.getRequest().getContextPath() + "/users/edituser.page?userId="
        + staffMember.getId());
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR };
  }

}
