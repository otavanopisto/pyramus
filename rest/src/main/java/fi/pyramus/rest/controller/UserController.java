package fi.pyramus.rest.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.pyramus.dao.base.AddressDAO;
import fi.pyramus.dao.users.StaffMemberDAO;
import fi.pyramus.dao.users.UserDAO;
import fi.pyramus.dao.users.UserVariableDAO;
import fi.pyramus.dao.users.UserVariableKeyDAO;
import fi.pyramus.domainmodel.base.Address;
import fi.pyramus.domainmodel.base.ContactType;
import fi.pyramus.domainmodel.base.Person;
import fi.pyramus.domainmodel.base.VariableType;
import fi.pyramus.domainmodel.users.Role;
import fi.pyramus.domainmodel.users.StaffMember;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.domainmodel.users.UserVariable;
import fi.pyramus.domainmodel.users.UserVariableKey;

@Dependent
@Stateless
public class UserController {

  @Inject
  private UserDAO userDAO;
  
  @Inject
  private StaffMemberDAO staffMemberDAO;
  
  @Inject
  private UserVariableDAO userVariableDAO;

  @Inject
  private UserVariableKeyDAO userVariableKeyDAO;
  
  @Inject
  private AddressDAO addressDAO;
  
  /* Users */
  
  public User findUserById(Long defaultUserId) {
    return userDAO.findById(defaultUserId);
  }
  
  // TODO: StaffMemberController?

  /* StaffMember */

  public StaffMember createStaffMember(String firstName, String lastName, Role role, Person person) {
    return staffMemberDAO.create(firstName, lastName, role, person, false);
  }
  
  public StaffMember findStaffMemberById(Long userId) {
    return staffMemberDAO.findById(userId);
  }

  public StaffMember findStaffMemberByEmail(String email) {
    return staffMemberDAO.findByUniqueEmail(email);
  }

  public List<StaffMember> listStaffMembers() {
    return staffMemberDAO.listAll();
  }

  public List<StaffMember> listStaffMembers(Integer firstResult, Integer maxResults) {
    return staffMemberDAO.listAll(firstResult, maxResults);
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
  
  public UserVariable createUserVariable(User user, UserVariableKey key, String value) {
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
  
}

