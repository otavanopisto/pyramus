package fi.otavanopisto.pyramus.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import fi.otavanopisto.pyramus.domainmodel.base.Address;
import fi.otavanopisto.pyramus.domainmodel.base.ContactInfo;
import fi.otavanopisto.pyramus.domainmodel.base.Email;
import fi.otavanopisto.pyramus.domainmodel.base.PhoneNumber;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit;
import fi.otavanopisto.pyramus.rest.controller.UserController;
import fi.otavanopisto.pyramus.rest.controller.permissions.UserPermissions;
import fi.otavanopisto.pyramus.rest.model.UserContact;

@Path("/contacts")
@Produces("application/json")
@Consumes("application/json")
@Stateful
@RequestScoped
public class UserContactRESTService extends AbstractRESTService {

  @Inject
  private UserController userController;

  @Path("/users/{USERID:[0-9]*}/contacts")
  @GET
  @RESTPermit(UserPermissions.LIST_STAFFMEMBER_EMAILS)
  public Response listUserContacts(@PathParam("USERID") Long userId) {

    User user = userController.findUserById(userId);

    if (user == null) {
      return Response.status(Status.BAD_REQUEST).build();
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

      // Email
      if (emails.size()-1 >= i) {
        Email email = emails.get(i);

        if (email != null) {
          userContact.setEmail(email.getAddress());
          userContact.setContactType(email.getContactType().getName());
        }
      }
      // Address & name
      
      if (addresses.size()-1 >= i) {
        Address address = addresses.get(i);

        if (address != null) {
          String addressString = address.getStreetAddress() + ", " + address.getPostalCode() + " " + address.getCity()
              + ", " + address.getCountry();
          userContact.setAddress(addressString);
          userContact.setName(address.getName());

          if (userContact.getContactType() == null) {
            userContact.setContactType(address.getContactType().getName());
          }
        }
      }
      // Phone number

      if (phonenumbers.size()-1 >= i) {
        PhoneNumber phoneNumber = phonenumbers.get(i);

        if (phoneNumber != null) {
          userContact.setPhoneNumber(phoneNumber.getNumber());

          if (userContact.getContactType() == null) {
            userContact.setContactType(phoneNumber.getContactType().getName());
          }
        }
      }

      userContactList.add(userContact);
    }

    return Response.ok(userContactList).build();
  }
}