package fi.otavanopisto.pyramus.rest;

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
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.base.PhoneNumber;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit;
import fi.otavanopisto.pyramus.rest.controller.UserController;
import fi.otavanopisto.pyramus.rest.controller.permissions.UserPermissions;
import fi.otavanopisto.pyramus.rest.model.UserContactInfo;

@Path("/users")
@Produces("application/json")
@Consumes("application/json")
@Stateful
@RequestScoped
public class UserRESTService {

  @Inject
  private UserController userController;

  @Produces("text/plain")
  @Path("/users/{ID:[0-9]*}/defaultEmailAddress")
  @GET
  @RESTPermit(UserPermissions.GET_USER_DEFAULT_EMAIL_ADDRESS)
  public Response getUserDefaultEmailAddress(@PathParam("ID") Long id) {
    User user = userController.findUserById(id);
    if (user == null) {
      return Response.status(Status.NOT_FOUND).entity("User not found").build();
    }
    Email email = user.getPrimaryEmail();
    return Response.ok(email.getAddress()).build();
  }

  @Path("/users/{ID:[0-9]*}/contactInfo")
  @GET
  @RESTPermit(UserPermissions.GET_USER_CONTACT_INFO)
  public Response getUserContactInfo(@PathParam("ID") Long id) {
    User user = userController.findUserById(id);
    if (user == null) {
      return Response.status(Status.NOT_FOUND).entity("User not found").build();
    }

    Person person = user.getPerson();
    ContactInfo contactInfo = user.getContactInfo();
    PhoneNumber phoneNumber = contactInfo.getPhoneNumbers().stream().filter(p -> Boolean.TRUE.equals(p.getDefaultNumber())).findFirst().orElse(null);
    Address address = contactInfo.getAddresses().stream().filter(a -> Boolean.TRUE.equals(a.getDefaultAddress())).findFirst().orElse(null);
    Email email = contactInfo.getEmails().stream().filter(e -> Boolean.TRUE.equals(e.getDefaultAddress())).findFirst().orElse(null);
    
    UserContactInfo userContactInfo = new UserContactInfo();
    userContactInfo.setFirstName(user.getFirstName());
    userContactInfo.setLastName(user.getLastName());
    userContactInfo.setDateOfBirth(person.getBirthday() == null ? null : new java.sql.Date(person.getBirthday().getTime()).toLocalDate());
    userContactInfo.setPhoneNumber(phoneNumber == null ? null : phoneNumber.getNumber());
    if (address != null) {
      userContactInfo.setAddressName(address.getName());
      userContactInfo.setStreetAddress(address.getStreetAddress());
      userContactInfo.setZipCode(address.getPostalCode());
      userContactInfo.setCity(address.getCity());
      userContactInfo.setCountry(address.getCountry());
    }
    userContactInfo.setEmail(email == null ? null : email.getAddress());

    return Response.ok(userContactInfo).build();
  }
  
}
