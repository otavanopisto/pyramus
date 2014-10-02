package fi.pyramus.rest;

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

import fi.pyramus.domainmodel.users.User;
import fi.pyramus.rest.controller.UserController;

@Path("/users")
@Produces("application/json")
@Consumes("application/json")
@Stateful
@RequestScoped
public class UserRESTService extends AbstractRESTService {

  @Inject
  private UserController userController;
  
  @Inject
  private ObjectFactory objectFactory;

  @Path("/users")
  @GET
  public Response listUsers(@QueryParam ("firstResult") Integer firstResult, @QueryParam ("maxResults") Integer maxResults) {
    return Response.ok(objectFactory.createModel(userController.listNonStudentUsers(firstResult, maxResults))).build();
  }
  
  @Path("/users/{ID:[0-9]*}")
  @GET
  public Response findUserById(@PathParam("ID") Long id) {
    User user = userController.findUserById(id);
    if (user == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    return Response.ok(objectFactory.createModel(user)).build();
  }

}