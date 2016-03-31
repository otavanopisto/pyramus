package fi.otavanopisto.pyramus.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.domainmodel.base.Address;
import fi.otavanopisto.pyramus.domainmodel.base.ContactType;
import fi.otavanopisto.pyramus.domainmodel.base.Email;
import fi.otavanopisto.pyramus.domainmodel.base.PhoneNumber;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit.Handling;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit.Style;
import fi.otavanopisto.pyramus.rest.controller.CommonController;
import fi.otavanopisto.pyramus.rest.controller.UserController;
import fi.otavanopisto.pyramus.rest.controller.permissions.PersonPermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.UserPermissions;
import fi.otavanopisto.pyramus.rest.security.RESTSecurity;

@Path("/staff")
@Produces("application/json")
@Consumes("application/json")
@Stateful
@RequestScoped
public class StaffRESTService extends AbstractRESTService {

  @Inject
  private UserController userController;
  
  @Inject
  private CommonController commonController;
  
  @Inject
  private ObjectFactory objectFactory;

  @Inject
  private RESTSecurity restSecurity;

  @Path("/members")
  @GET
  @RESTPermit (UserPermissions.LIST_STAFFMEMBERS)
  public Response listStaffMembers(@QueryParam ("firstResult") Integer firstResult, @QueryParam ("maxResults") Integer maxResults, @QueryParam ("email") String email) {
    List<StaffMember> staffMembers = null;
    
    if (StringUtils.isNotBlank(email)) {
      staffMembers = new ArrayList<StaffMember>();
      StaffMember staffMember = userController.findStaffMemberByEmail(email);
      if (staffMember != null) {
        staffMembers.add(staffMember);
      }
    } else {
      staffMembers = userController.listStaffMembers(firstResult, maxResults);
    }
    
    return Response.ok(objectFactory.createModel(staffMembers)).build();
  }
  
  @Path("/members/{ID:[0-9]*}")
  @GET
  @RESTPermit (handling = Handling.INLINE)
  //@RESTPermit (UserPermissions.FIND_STAFFMEMBER)
  public Response findStaffMemberById(@PathParam("ID") Long id, @Context Request request) {
    StaffMember staffMember = userController.findStaffMemberById(id);
    
    if ((staffMember == null) || (staffMember.getArchived())) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (!restSecurity.hasPermission(new String[] { UserPermissions.FIND_STAFFMEMBER, UserPermissions.USER_OWNER }, staffMember, Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }

    EntityTag tag = new EntityTag(DigestUtils.md5Hex(String.valueOf(staffMember.getVersion())));
    ResponseBuilder builder = request.evaluatePreconditions(tag);
    if (builder != null) {
      return builder.build();
    }

    CacheControl cacheControl = new CacheControl();
    cacheControl.setMustRevalidate(true);
    
    return Response
        .ok(objectFactory.createModel(staffMember))
        .cacheControl(cacheControl)
        .tag(tag)
        .build();
  }

  @Path("/members/{ID:[0-9]*}/emails")
  @GET
  @RESTPermit (UserPermissions.LIST_STAFFMEMBER_EMAILS)
  public Response listStaffMembersEmails(@PathParam("ID") Long id) {
    StaffMember user = userController.findStaffMemberById(id);
    if (user == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    List<Email> emails = user.getContactInfo().getEmails();
    if (emails.isEmpty()) {
      return Response.noContent().build();
    }
    
    return Response.ok(objectFactory.createModel(emails)).build();
  }
  
  @Path("/members/{STAFFMEMBERID:[0-9]*}/addresses")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response listStaffMemberAddresses(@PathParam("STAFFMEMBERID") Long staffMemberId) {
    StaffMember staffMember = userController.findStaffMemberById(staffMemberId);
    if (staffMember == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (staffMember.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (!restSecurity.hasPermission(new String[] { UserPermissions.LIST_STAFFMEMBERADDRESSES }, staffMember ) && !restSecurity.hasPermission(new String[] { PersonPermissions.PERSON_OWNER }, staffMember.getPerson() )) {
      return Response.status(Status.FORBIDDEN).build();
    }
    
    List<Address> addresses = staffMember.getContactInfo().getAddresses();
    if (addresses.isEmpty()) {
      return Response.noContent().build();
    }

    return Response.ok(objectFactory.createModel(addresses)).build();
  }

  @Path("/members/{STAFFMEMBERID:[0-9]*}/addresses")
  @POST
  @RESTPermit(handling = Handling.INLINE)
  public Response createStaffMemberAddress(@PathParam("STAFFMEMBERID") Long staffMemberId, fi.otavanopisto.pyramus.rest.model.Address address) {
    if (address == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    StaffMember staffMember = userController.findStaffMemberById(staffMemberId);
    if (staffMember == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (staffMember.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (!restSecurity.hasPermission(new String[] { UserPermissions.CREATE_STAFFMEMBERADDRESS }, staffMember ) && !restSecurity.hasPermission(new String[] { PersonPermissions.PERSON_OWNER }, staffMember.getPerson() )) {
      return Response.status(Status.FORBIDDEN).build();
    }

    Long contactTypeId = address.getContactTypeId();
    Boolean defaultAddress = address.getDefaultAddress();
    String name = address.getName();
    String streetAddress = address.getStreetAddress();
    String postalCode = address.getPostalCode();
    String country = address.getCountry();
    String city = address.getCity();

    if ((contactTypeId == null) || (defaultAddress == null)) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    ContactType contactType = commonController.findContactTypeById(contactTypeId);
    if (contactType == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    return Response.ok(
        objectFactory.createModel(userController.addStaffMemberAddress(staffMember, contactType, defaultAddress, name, streetAddress, postalCode, city, country)))
        .build();
  }

  @Path("/members/{STAFFMEMBERID:[0-9]*}/addresses/{ID:[0-9]*}")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response findStaffMemberAddress(@PathParam("STAFFMEMBERID") Long staffMemberId, @PathParam("ID") Long id) {
    StaffMember staffMember = userController.findStaffMemberById(staffMemberId);
    if (staffMember == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (staffMember.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (!restSecurity.hasPermission(new String[] { UserPermissions.FIND_STAFFMEMBERADDRESS }, staffMember ) && !restSecurity.hasPermission(new String[] { PersonPermissions.PERSON_OWNER }, staffMember.getPerson() )) {
      return Response.status(Status.FORBIDDEN).build();
    }

    Address address = commonController.findAddressById(id);
    if (address == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (!address.getContactInfo().getId().equals(staffMember.getContactInfo().getId())) {
      return Response.status(Status.NOT_FOUND).build();
    }

    return Response.ok(objectFactory.createModel(address)).build();
  }

  @Path("/members/{STAFFMEMBERID:[0-9]*}/addresses/{ID:[0-9]*}")
  @DELETE
  @RESTPermit(handling = Handling.INLINE)
  public Response deleteStaffMemberAddress(@PathParam("STAFFMEMBERID") Long staffMemberId, @PathParam("ID") Long id) {
    StaffMember staffMember = userController.findStaffMemberById(staffMemberId);
    if (staffMember == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (staffMember.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (!restSecurity.hasPermission(new String[] { UserPermissions.DELETE_STAFFMEMBERADDRESS }, staffMember ) && !restSecurity.hasPermission(new String[] { PersonPermissions.PERSON_OWNER }, staffMember.getPerson() )) {
      return Response.status(Status.FORBIDDEN).build();
    }
    
    Address address = commonController.findAddressById(id);
    if (address == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (!address.getContactInfo().getId().equals(staffMember.getContactInfo().getId())) {
      return Response.status(Status.NOT_FOUND).build();
    }

    commonController.deleteAddress(address);

    return Response.noContent().build();
  }
  
  @Path("/members/{STAFFMEMBERID:[0-9]*}/phoneNumbers")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response listStaffMemberPhoneNumbers(@PathParam("STAFFMEMBERID") Long staffMemberId) {
    StaffMember staffMember = userController.findStaffMemberById(staffMemberId);
    if (staffMember == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (staffMember.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (!restSecurity.hasPermission(new String[] { UserPermissions.LIST_STAFFMEMBERPHONENUMBERS }, staffMember ) && !restSecurity.hasPermission(new String[] { PersonPermissions.PERSON_OWNER }, staffMember.getPerson() )) {
      return Response.status(Status.FORBIDDEN).build();
    }

    List<PhoneNumber> phoneNumbers = staffMember.getContactInfo().getPhoneNumbers();
    if (phoneNumbers.isEmpty()) {
      return Response.noContent().build();
    }

    return Response.ok(objectFactory.createModel(phoneNumbers)).build();
  }

  @Path("/members/{STAFFMEMBERID:[0-9]*}/phoneNumbers")
  @POST
  @RESTPermit(handling = Handling.INLINE)
  public Response createStaffMemberPhoneNumber(@PathParam("STAFFMEMBERID") Long staffMemberId, fi.otavanopisto.pyramus.rest.model.PhoneNumber phoneNumber) {
    if (phoneNumber == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    StaffMember staffMember = userController.findStaffMemberById(staffMemberId);
    if (staffMember == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (staffMember.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (!restSecurity.hasPermission(new String[] { UserPermissions.CREATE_STAFFMEMBERPHONENUMBER }, staffMember ) && !restSecurity.hasPermission(new String[] { PersonPermissions.PERSON_OWNER }, staffMember.getPerson() )) {
      return Response.status(Status.FORBIDDEN).build();
    }

    Long contactTypeId = phoneNumber.getContactTypeId();
    Boolean defaultNumber = phoneNumber.getDefaultNumber();
    String number = phoneNumber.getNumber();

    if ((contactTypeId == null) || (defaultNumber == null) || StringUtils.isBlank(number)) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    ContactType contactType = commonController.findContactTypeById(contactTypeId);
    if (contactType == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    return Response.ok(objectFactory.createModel(userController.addStaffMemberPhoneNumber(staffMember, contactType, number, defaultNumber))).build();
  }

  @Path("/members/{STAFFMEMBERID:[0-9]*}/phoneNumbers/{ID:[0-9]*}")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response findStaffMemberPhoneNumber(@PathParam("STAFFMEMBERID") Long staffMemberId, @PathParam("ID") Long id) {
    StaffMember staffMember = userController.findStaffMemberById(staffMemberId);
    if (staffMember == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (staffMember.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (!restSecurity.hasPermission(new String[] { UserPermissions.FIND_STAFFMEMBERPHONENUMBER }, staffMember ) && !restSecurity.hasPermission(new String[] { PersonPermissions.PERSON_OWNER }, staffMember.getPerson() )) {
      return Response.status(Status.FORBIDDEN).build();
    }

    PhoneNumber phoneNumber = commonController.findPhoneNumberById(id);
    if (phoneNumber == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (!phoneNumber.getContactInfo().getId().equals(staffMember.getContactInfo().getId())) {
      return Response.status(Status.NOT_FOUND).build();
    }

    return Response.ok(objectFactory.createModel(phoneNumber)).build();
  }

  @Path("/members/{STAFFMEMBERID:[0-9]*}/phoneNumbers/{ID:[0-9]*}")
  @DELETE
  @RESTPermit(handling = Handling.INLINE)
  public Response deleteStaffMemberPhoneNumber(@PathParam("STAFFMEMBERID") Long staffMemberId, @PathParam("ID") Long id) {
    StaffMember staffMember = userController.findStaffMemberById(staffMemberId);
    if (staffMember == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (staffMember.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (!restSecurity.hasPermission(new String[] { UserPermissions.DELETE_STAFFMEMBERPHONENUMBER }, staffMember ) && !restSecurity.hasPermission(new String[] { PersonPermissions.PERSON_OWNER }, staffMember.getPerson() )) {
      return Response.status(Status.FORBIDDEN).build();
    }
    
    PhoneNumber phoneNumber = commonController.findPhoneNumberById(id);
    if (phoneNumber == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (!phoneNumber.getContactInfo().getId().equals(staffMember.getContactInfo().getId())) {
      return Response.status(Status.NOT_FOUND).build();
    }

    commonController.deletePhoneNumber(phoneNumber);

    return Response.noContent().build();
  }
  
}