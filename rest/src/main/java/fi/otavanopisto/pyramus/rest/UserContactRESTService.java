package fi.otavanopisto.pyramus.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import fi.otavanopisto.pyramus.dao.base.UserAdditionalContactInfoDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Address;
import fi.otavanopisto.pyramus.domainmodel.base.ContactInfo;
import fi.otavanopisto.pyramus.domainmodel.base.Email;
import fi.otavanopisto.pyramus.domainmodel.base.PhoneNumber;
import fi.otavanopisto.pyramus.domainmodel.base.UserAdditionalContactInfo;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit.Handling;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit.Style;
import fi.otavanopisto.pyramus.rest.controller.UserController;
import fi.otavanopisto.pyramus.rest.controller.permissions.StudentPermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.UserPermissions;
import fi.otavanopisto.pyramus.rest.model.UserContact;
import fi.otavanopisto.pyramus.rest.security.RESTSecurity;

@Path("/contacts")
@Produces("application/json")
@Consumes("application/json")
@Stateful
@RequestScoped
public class UserContactRESTService extends AbstractRESTService {

  @Inject
  private UserController userController;

  @Inject
  private UserAdditionalContactInfoDAO studentAdditionalContactInfoDAO;
  
  @Inject
  private RESTSecurity restSecurity;

  @Path("/users/{USERID:[0-9]*}/contacts")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response listUserContacts(@PathParam("USERID") Long userId) {

    User user = userController.findUserById(userId);

    if (user == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (user instanceof Student) {
      if (!restSecurity.hasPermission(new String[] { StudentPermissions.LIST_STUDENT_CONTACTS, UserPermissions.USER_OWNER, UserPermissions.STUDENT_PARENT }, user, Style.OR)) {
        return Response.status(Status.FORBIDDEN).build();
      }
    }
    else {
      if (!restSecurity.hasPermission(new String[] { UserPermissions.LIST_STAFF_CONTACTS, UserPermissions.USER_OWNER }, user, Style.OR)) {
        return Response.status(Status.FORBIDDEN).build();
      }
    }

    ContactInfo contactInfo = user.getContactInfo();
    List<Address> addresses = contactInfo.getAddresses();
    List<PhoneNumber> phonenumbers = contactInfo.getPhoneNumbers();
    List<Email> emails = contactInfo.getEmails();

    List<UserContact> userContactList = new ArrayList<UserContact>();

    // Find the maximum value for the loop
    int len = addresses.size();
    len = Math.max(len, emails.size());
    len = Math.max(len, phonenumbers.size());

    for (int i = 0; i < len; i++) {
      UserContact userContact = new UserContact();

      // TODO: Refactor later to represent the contact's id when a separate contact table has been created
      userContact.setId(contactInfo.getId());

      // Email
      if (emails.size() - 1 >= i) {
        Email email = emails.get(i);

        if (email != null) {
          userContact.setEmail(email.getAddress());
          userContact.setDefaultContact(email.getDefaultAddress());
        }
      }
      // Address & name

      if (addresses.size() - 1 >= i) {
        Address address = addresses.get(i);

        if (address != null) {
          userContact.setStreetAddress(address.getStreetAddress());
          userContact.setPostalCode(address.getPostalCode());
          userContact.setCity(address.getCity());
          userContact.setCountry(address.getCountry());
          userContact.setName(address.getName());
        }
      }
      // Phone number

      if (phonenumbers.size() - 1 >= i) {
        PhoneNumber phoneNumber = phonenumbers.get(i);

        if (phoneNumber != null) {
          userContact.setPhoneNumber(phoneNumber.getNumber());
        }
      }

      userContactList.add(userContact);
    }

    // If the user is Student, add the additional contact infos
    
    if (user instanceof Student) {
      Student student = (Student) user;
      
      for (UserAdditionalContactInfo additionalContactInfo : student.getAdditionalContactInfos()) {
        userContactList.add(restModel(additionalContactInfo));
      }
    }
    
    return Response.ok(userContactList).build();
  }
  
  @Path("/users/{USERID:[0-9]*}/contacts/{CONTACTINFOID}/allowStudyDiscussions")
  @PUT
  @RESTPermit(handling = Handling.INLINE)
  public Response updateContactInfo(@PathParam("USERID") Long userId, @PathParam("CONTACTINFOID") Long contactInfoId,
      Boolean allowStudyDiscussions) {
    if (allowStudyDiscussions == null) {
      return Response.status(Status.BAD_REQUEST).entity("invalid payload").build();
    }

    User user = userController.findUserById(userId);

    if (user == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (!restSecurity.hasPermission(new String[] { UserPermissions.USER_OWNER }, user)) {
      return Response.status(Status.FORBIDDEN).build();
    }

    if (!(user instanceof Student)) {
      return Response.status(Status.BAD_REQUEST).entity("User is not a student").build();
    }
    
    UserAdditionalContactInfo studentAdditionalContactInfo = studentAdditionalContactInfoDAO.findById(contactInfoId);
    if (studentAdditionalContactInfo == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    Student student = (Student) user;
    if (!student.getAdditionalContactInfos().stream().anyMatch(aci -> contactInfoId.equals(aci.getId()))) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    studentAdditionalContactInfo = studentAdditionalContactInfoDAO.update(studentAdditionalContactInfo, studentAdditionalContactInfo.getContactType(), allowStudyDiscussions);
    
    return Response.ok(restModel(studentAdditionalContactInfo)).build();
  }
  
  private UserContact restModel(UserAdditionalContactInfo additionalContactInfo) {
    UserContact userContact = new UserContact();
    userContact.setId(additionalContactInfo.getId());
    userContact.setContactType(additionalContactInfo.getContactType() != null ? additionalContactInfo.getContactType().getName() : null);
    userContact.setAllowStudyDiscussions(additionalContactInfo.isAllowStudyDiscussions());
    
    Address defaultAddress = additionalContactInfo.getDefaultAddress();
    Email defaultEmail = additionalContactInfo.getDefaultEmail();
    PhoneNumber defaultPhoneNumber = additionalContactInfo.getDefaultPhoneNumber();
    
    if (defaultAddress != null) {
      userContact.setStreetAddress(defaultAddress.getStreetAddress());
      userContact.setPostalCode(defaultAddress.getPostalCode());
      userContact.setCity(defaultAddress.getCity());
      userContact.setCountry(defaultAddress.getCountry());
      userContact.setName(defaultAddress.getName());
    }
    
    if (defaultEmail != null) {
      userContact.setEmail(defaultEmail.getAddress());
    }
    
    if (defaultPhoneNumber != null) {
      userContact.setPhoneNumber(defaultPhoneNumber.getNumber());
    }
    
    return userContact;
  }
}