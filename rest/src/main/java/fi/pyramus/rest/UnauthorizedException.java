package fi.pyramus.rest;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class UnauthorizedException extends WebApplicationException {

  private static final long serialVersionUID = -8832262933126379045L;

  public UnauthorizedException() {
    super(Response.status(Response.Status.FORBIDDEN)
        .entity("Forbidden").type(MediaType.TEXT_PLAIN).build());
  }
  
}
