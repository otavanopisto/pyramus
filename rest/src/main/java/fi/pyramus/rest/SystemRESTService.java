package fi.pyramus.rest;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import fi.pyramus.rest.annotation.Unsecure;

@Path("/system")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateful
@RequestScoped
public class SystemRESTService extends AbstractRESTService {
  
  @GET
  @Unsecure
  @Path ("/ping")
  @Produces (MediaType.TEXT_PLAIN)
  public Response getPing() {
    return Response.ok("pong").build();
  }

}
