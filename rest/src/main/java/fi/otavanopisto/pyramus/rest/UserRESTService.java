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

import fi.otavanopisto.pyramus.domainmodel.base.Email;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit;
import fi.otavanopisto.pyramus.rest.controller.UserController;
import fi.otavanopisto.pyramus.rest.controller.permissions.UserPermissions;

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
  
}
