package fi.pyramus.rest;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.students.StudentDAO;
import fi.pyramus.domainmodel.students.Student;

@Path("/students")
public class StudentRESTService {
  @Path("/languages")
  @GET
  public Response getLanguages(){
    return Response.ok().entity("laama").build();
  }
  
}