package fi.pyramus.services;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.persistence.EnumType;
import javax.xml.ws.BindingType;

import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.ContactTypeDAO;
import fi.pyramus.dao.base.EmailDAO;
import fi.pyramus.dao.base.PersonDAO;
import fi.pyramus.dao.users.StaffMemberDAO;
import fi.pyramus.dao.users.UserIdentificationDAO;
import fi.pyramus.dao.users.UserVariableDAO;
import fi.pyramus.domainmodel.base.ContactType;
import fi.pyramus.domainmodel.base.Email;
import fi.pyramus.domainmodel.base.Person;
import fi.pyramus.domainmodel.users.Role;
import fi.pyramus.domainmodel.users.StaffMember;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.domainmodel.users.UserIdentification;
import fi.pyramus.framework.UserUtils;
import fi.pyramus.services.entities.EntityFactoryVault;
import fi.pyramus.services.entities.users.UserEntity;

@Stateless
@WebService
@BindingType(javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@RolesAllowed("WebServices")
public class UsersService extends PyramusService {

  public UserEntity[] listUsers() {
    StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffMemberDAO();
    return (UserEntity[]) EntityFactoryVault.buildFromDomainObjects(userDAO.listAll());
  }

  public UserEntity[] listUsersByUserVariable(@WebParam(name = "key") String key, @WebParam(name = "value") String value) {
    StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffMemberDAO();
    return (UserEntity[]) EntityFactoryVault.buildFromDomainObjects(userDAO.listByUserVariable(key, value));
  }

  public UserEntity createUser(@WebParam(name = "firstName") String firstName, @WebParam(name = "lastName") String lastName,
      @WebParam(name = "externalId") String externalId, @WebParam(name = "authProvider") String authProvider, @WebParam(name = "role") String role) {
    StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffMemberDAO();
    PersonDAO personDAO = DAOFactory.getInstance().getPersonDAO();
    UserIdentificationDAO userIdentificationDAO = DAOFactory.getInstance().getUserIdentificationDAO();
    // TODO: should not create if user exists
    Person person = personDAO.create(null, null, null, null, Boolean.FALSE);
    userIdentificationDAO.create(person, authProvider, externalId);
    Role userRole = EnumType.valueOf(Role.class, role);
    User user = userDAO.create(firstName, lastName, userRole, person);
    personDAO.updateDefaultUser(person, user);
    validateEntity(user);
    return EntityFactoryVault.buildFromDomainObject(user);
  }

  public void updateUser(@WebParam(name = "userId") Long userId, @WebParam(name = "firstName") String firstName, @WebParam(name = "lastName") String lastName,
      @WebParam(name = "role") String role) {
    StaffMemberDAO staffDAO = DAOFactory.getInstance().getStaffMemberDAO();
    StaffMember user = staffDAO.findById(userId);
    Role userRole = EnumType.valueOf(Role.class, role);
    staffDAO.update(user, firstName, lastName, userRole);
    validateEntity(user);
  }

  public UserEntity getUserById(@WebParam(name = "userId") Long userId) {
    StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffMemberDAO();
    return EntityFactoryVault.buildFromDomainObject(userDAO.findById(userId));
  }

  public UserEntity getUserByExternalId(@WebParam(name = "externalId") String externalId, @WebParam(name = "authProvider") String authProvider) {
    UserIdentificationDAO userIdentificationDAO = DAOFactory.getInstance().getUserIdentificationDAO();
    StaffMemberDAO staffDAO = DAOFactory.getInstance().getStaffMemberDAO();
    UserIdentification userIdentification = userIdentificationDAO.findByAuthSourceAndExternalId(authProvider, externalId);
    if(userIdentification != null){
      StaffMember staffMember = staffDAO.findByPerson(userIdentification.getPerson());
      if(staffMember != null){
        return EntityFactoryVault.buildFromDomainObject(staffMember);
      }
    }
    return null;
  }

  public UserEntity getUserByEmail(@WebParam(name = "email") String email) {
    StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffMemberDAO();
    return EntityFactoryVault.buildFromDomainObject(userDAO.findByEmail(email));
  }

  public void addUserEmail(@WebParam(name = "userId") Long userId, @WebParam(name = "address") String address) {
    StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffMemberDAO();
    EmailDAO emailDAO = DAOFactory.getInstance().getEmailDAO();
    ContactTypeDAO contactTypeDAO = DAOFactory.getInstance().getContactTypeDAO();
    StaffMember user = userDAO.findById(userId);
    
    // TODO contact type, default address
    ContactType contactType = contactTypeDAO.findById(new Long(1));

    if (!UserUtils.isAllowedEmail(address, contactType, user.getPerson().getId()))
      throw new RuntimeException("Email address is in use");

    Email email = emailDAO.create(user.getContactInfo(), contactType, Boolean.TRUE, address);
    validateEntity(email);
  }

  public void removeUserEmail(@WebParam(name = "userId") Long userId, @WebParam(name = "address") String address) {
    StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffMemberDAO();
    EmailDAO emailDAO = DAOFactory.getInstance().getEmailDAO();
    fi.pyramus.domainmodel.users.User user = userDAO.findById(userId);
    for (Email email : user.getContactInfo().getEmails()) {
      if (email.getAddress().equals(address)) {
        emailDAO.delete(email);
        break;
      }
    }
  }

  public void updateUserEmail(@WebParam(name = "userId") Long userId, @WebParam(name = "fromAddress") String fromAddress,
      @WebParam(name = "toAddress") String toAddress) {
    StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffMemberDAO();
    EmailDAO emailDAO = DAOFactory.getInstance().getEmailDAO();
    fi.pyramus.domainmodel.users.User user = userDAO.findById(userId);
    for (Email email : user.getContactInfo().getEmails()) {
      if (email.getAddress().equals(fromAddress)) {
        email = emailDAO.update(email, email.getContactType(), email.getDefaultAddress(), toAddress);
        validateEntity(email);
        break;
      }
    }
  }

  public String getUserVariable(@WebParam(name = "userId") Long userId, @WebParam(name = "key") String key) {
    StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffMemberDAO();
    UserVariableDAO userVariableDAO = DAOFactory.getInstance().getUserVariableDAO();
    return userVariableDAO.findByUserAndKey(userDAO.findById(userId), key);
  }

  public void setUserVariable(@WebParam(name = "userId") Long userId, @WebParam(name = "key") String key, @WebParam(name = "value") String value) {
    StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffMemberDAO();
    UserVariableDAO userVariableDAO = DAOFactory.getInstance().getUserVariableDAO();
    userVariableDAO.setUserVariable(userDAO.findById(userId), key, value);
  }

}
