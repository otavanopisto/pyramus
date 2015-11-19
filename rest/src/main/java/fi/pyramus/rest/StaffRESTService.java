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
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import fi.pyramus.domainmodel.base.Email;
import fi.pyramus.domainmodel.users.StaffMember;
import fi.pyramus.rest.annotation.RESTPermit;
import fi.pyramus.rest.annotation.RESTPermit.Handling;
import fi.pyramus.rest.annotation.RESTPermit.Style;
import fi.pyramus.rest.controller.UserController;
import fi.pyramus.rest.controller.permissions.UserPermissions;
import fi.pyramus.rest.security.RESTSecurity;

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
  
  
}