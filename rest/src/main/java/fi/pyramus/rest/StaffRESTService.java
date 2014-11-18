package fi.pyramus.rest;

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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;

import fi.pyramus.domainmodel.base.Email;
import fi.pyramus.domainmodel.users.StaffMember;
import fi.pyramus.rest.annotation.RESTPermit;
import fi.pyramus.rest.annotation.RESTPermit.Handle;
import fi.pyramus.rest.controller.UserController;
import fi.pyramus.rest.controller.permissions.UserPermissions;

@Path("/staff")
@Produces("application/json")
@Consumes("application/json")
@Stateful
@RequestScoped
public class StaffRESTService extends AbstractRESTService {

  @Inject
  private UserController userController;
  
  @Inject
  private ObjectFactory objectFactory;

  @Path("/members")
  @GET
  @RESTPermit (handle = Handle.EXCEPTION, value = UserPermissions.LIST_STAFFMEMBERS)
  public Response listUsers(@QueryParam ("firstResult") Integer firstResult, @QueryParam ("maxResults") Integer maxResults, @QueryParam ("email") String email) {
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
  @RESTPermit (handle = Handle.EXCEPTION, value = UserPermissions.FIND_STAFFMEMBER)
  public Response findUserById(@PathParam("ID") Long id) {
    StaffMember user = userController.findStaffMemberById(id);
    if (user == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    return Response.ok(objectFactory.createModel(user)).build();
  }

  @Path("/members/{ID:[0-9]*}/emails")
  @GET
  @RESTPermit (handle = Handle.EXCEPTION, value = UserPermissions.LIST_STAFFMEMBER_EMAILS)
  public Response listUserEmails(@PathParam("ID") Long id) {
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
  
  
}