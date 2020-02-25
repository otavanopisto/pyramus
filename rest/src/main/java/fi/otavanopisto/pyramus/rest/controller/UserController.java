package fi.otavanopisto.pyramus.rest.controller;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.dao.base.AddressDAO;
import fi.otavanopisto.pyramus.dao.base.EmailDAO;
import fi.otavanopisto.pyramus.dao.base.PhoneNumberDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.dao.users.UserDAO;
import fi.otavanopisto.pyramus.dao.users.UserVariableDAO;
import fi.otavanopisto.pyramus.dao.users.UserVariableKeyDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Address;
import fi.otavanopisto.pyramus.domainmodel.base.ContactType;
import fi.otavanopisto.pyramus.domainmodel.base.Email;
import fi.otavanopisto.pyramus.domainmodel.base.Organization;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.base.PhoneNumber;
import fi.otavanopisto.pyramus.domainmodel.base.VariableType;
import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.domainmodel.users.UserVariable;
import fi.otavanopisto.pyramus.domainmodel.users.UserVariableKey;
import fi.otavanopisto.pyramus.framework.UserEmailInUseException;
import fi.otavanopisto.pyramus.framework.UserUtils;

@Dependent
@Stateless
public class UserController {

  @Inject
  private Logger logger;
  
  @Inject
  private UserDAO userDAO;

  @Inject
  private EmailDAO emailDAO;
  
  @Inject
  private StudentDAO studentDAO;

  @Inject
  private StaffMemberDAO staffMemberDAO;
  
  @Inject
  private UserVariableDAO userVariableDAO;

  @Inject
  private UserVariableKeyDAO userVariableKeyDAO;
  
  @Inject
  private AddressDAO addressDAO;

  @Inject
  private PhoneNumberDAO phoneNumberDAO;
  
  /* Users */
  
  public User findUserById(Long defaultUserId) {
    return userDAO.findById(defaultUserId);
  }
  
  // TODO: StaffMemberController?

  /* StaffMember */

  public StaffMember createStaffMember(Organization organization, String firstName, String lastName, Role role, Person person) {
    return staffMemberDAO.create(organization, firstName, lastName, role, person, false);
  }

  public StaffMember updateStaffMember(StaffMember staffMember, Organization organization, String firstName, String lastName, Role role) {
    return staffMemberDAO.update(staffMember, organization, firstName, lastName, role);
  }
  
  public void archiveStaffMember(StaffMember staffMember) {
    staffMemberDAO.archive(staffMember);
  }
  
  public void deleteStaffMember(StaffMember staffMember) {
    staffMemberDAO.delete(staffMember);
  }

  public StaffMember findStaffMemberById(Long userId) {
    return staffMemberDAO.findById(userId);
  }

  public Email addUserEmail(User user, ContactType contactType, String address, Boolean defaultAddress) {
    // Trim the email address
    address = StringUtils.trim(address);

    if (StringUtils.isBlank(address)) {
      throw new IllegalArgumentException("Email cannot be blank.");
    }
    
    Email email = emailDAO.create(user.getContactInfo(), contactType, defaultAddress, address);
    if (user.getRole() == Role.STUDENT) {
      studentDAO.fireUpdate(user.getId());
    }
    else {
      staffMemberDAO.fireUpdate(user.getId());
    }
    return email;
  }

  public StaffMember findStaffMemberByEmail(String email) {
    return staffMemberDAO.findByUniqueEmail(email);
  }

  public List<Email> listStaffMemberEmails(StaffMember staffMember) {
    return emailDAO.listByContactInfo(staffMember.getContactInfo());
  }
  
  public Email updateStaffMemberEmail(StaffMember staffMember, Email email, ContactType contactType, String address, Boolean defaultAddress) throws UserEmailInUseException {
    // Trim the email address
    address = StringUtils.trim(address);

    if (StringUtils.isBlank(address)) {
      throw new IllegalArgumentException("Email cannot be blank.");
    }
    
    if (!UserUtils.isAllowedEmail(address, contactType, staffMember.getPerson().getId())) {
      throw new UserEmailInUseException();
    }
    
    return emailDAO.update(email, contactType, defaultAddress, address);
  }

  public List<StaffMember> listStaffMembers() {
    return staffMemberDAO.listAll();
  }

  public List<StaffMember> listStaffMembers(Integer firstResult, Integer maxResults) {
    return staffMemberDAO.listAll(firstResult, maxResults);
  }

  public List<StaffMember> listStaffMembersByPerson(Person person) {
    return staffMemberDAO.listByPerson(person);
  }
  
  /* Variables */

  public synchronized User updateUserVariables(User user, Map<String, String> variables) {
    Set<String> newKeys = new HashSet<>(variables.keySet());
    Set<String> oldKeys = new HashSet<>();
    Set<String> updateKeys = new HashSet<>();
    
    List<UserVariable> userVariables = userVariableDAO.listByUser(user);
    
    for (UserVariable variable : userVariables) {
      oldKeys.add(variable.getKey().getVariableKey());
    }

    for (String oldKey : oldKeys) {
      if (!newKeys.contains(oldKey)) {
        UserVariableKey key = findUserVariableKeyByVariableKey(oldKey);
        UserVariable userVariable = findUserVariableByStudentAndKey(user, key);
        deleteUserVariable(userVariable);
      } else {
        updateKeys.add(oldKey);
      }
      
      newKeys.remove(oldKey);
    }
    
    for (String newKey : newKeys) {
      String value = variables.get(newKey);
      UserVariableKey key = findUserVariableKeyByVariableKey(newKey);
      createUserVariable(user, key, value);
    }
    
    for (String updateKey : updateKeys) {
      String value = variables.get(updateKey);
      UserVariableKey key = findUserVariableKeyByVariableKey(updateKey);
      UserVariable userVariable = findUserVariableByStudentAndKey(user, key);
      updateUserVariable(userVariable, value);
    }
    
    return user;
  }
  
  public boolean checkUserVariableKeysExist(Collection<String> variableKeys) {
    if (variableKeys != null) {
      for (String variableKeyName : variableKeys) {
        if (userVariableKeyDAO.findByVariableKey(variableKeyName) == null) {
          logger.warning(String.format("Detected missing uservariable %s", variableKeyName));
          return false;
        }
      }
    }
    
    return true;
  }
  
  public UserVariable createUserVariable(User user, UserVariableKey key, String value) {
    if (key == null) {
      throw new IllegalArgumentException("UserVariableKey cannot be null");
    }
    
    return userVariableDAO.create(user, key, value);
  }

  public UserVariable findUserVariableById(Long id) {
    UserVariable userVariable = userVariableDAO.findById(id);
    return userVariable;
  }

  public UserVariable findUserVariableByStudentAndKey(User user, UserVariableKey key) {
    return userVariableDAO.findByUserAndVariableKey(user, key);
  }

  public void deleteUserVariable(UserVariable variable) {
    userVariableDAO.delete(variable);
  }

  public UserVariable updateUserVariable(UserVariable userVariable, String value) {
    return userVariableDAO.updateValue(userVariable, value);
  }
  
  public UserVariableKey findUserVariableKeyByVariableKey(String variableKey) {
    return userVariableKeyDAO.findByVariableKey(variableKey);
  }

  public List<UserVariable> listUserVariablesByUser(User user) {
    return userVariableDAO.listByUser(user);
  }
  
  /* Variable Keys */

  public UserVariableKey createUserVariableKey(String key, String name, VariableType variableType, Boolean userEditable) {
    return userVariableKeyDAO.create(key, name, variableType, userEditable);
  }

  public List<UserVariableKey> listUserVariableKeys() {
    return userVariableKeyDAO.listAll();
  }

  public UserVariableKey updateUserVariableKey(UserVariableKey userVariableKey, String name, VariableType variableType, Boolean userEditable) {
    userVariableKeyDAO.updateVariableName(userVariableKey, name);
    userVariableKeyDAO.updateVariableType(userVariableKey, variableType);
    userVariableKeyDAO.updateUserEditable(userVariableKey, userEditable);
    return userVariableKey;
  }

  public void deleteUserVariableKey(UserVariableKey userVariableKey) {
    userVariableKeyDAO.delete(userVariableKey);
  }

  /* Address */

  public Address addStaffMemberAddress(StaffMember staffMember, ContactType contactType, Boolean defaultAddress, String name, String streetAddress, String postalCode, String city, String country) {
    return addressDAO.create(staffMember.getContactInfo(), contactType, name ,streetAddress, postalCode, city, country, defaultAddress);
  }
  
  /* PhoneNumber */

  public PhoneNumber addStaffMemberPhoneNumber(StaffMember staffMember, ContactType contactType, String number, Boolean defaultNumber) {
    return phoneNumberDAO.create(staffMember.getContactInfo(), contactType, defaultNumber, number);
  }

}

