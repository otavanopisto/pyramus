package fi.pyramus.rest;

import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import fi.pyramus.domainmodel.base.School;
import fi.pyramus.domainmodel.base.SchoolField;
import fi.pyramus.domainmodel.base.SchoolVariable;
import fi.pyramus.rest.controller.SchoolController;
import fi.tranquil.TranquilityBuilderFactory;

@Path("/schools")
@Produces("application/json")
@Consumes("application/json")
@Stateful
@RequestScoped
public class SchoolRESTService extends AbstractRESTService {
  @Inject
  private TranquilityBuilderFactory tranquilityBuilderFactory;
  @Inject
  private SchoolController schoolController;

//  @Path("/schools")
//  @POST
//  public Response createSchool(SchoolUpdate schoolUpdateEntity){
//    SchoolField schoolField = schoolFieldController.findSchoolFieldById(schoolUpdateEntity.getField().getId());
//    return Response.ok()
//        .entity(tranqualise(schoolController.createSchool(schoolUpdateEntity.getCode(), schoolUpdateEntity.getName(), schoolField)))
//        .build();
//  }
//  
//  @Path("/schoolFields")
//  @POST
//  public Response createSchoolField(SchoolFieldComplete schoolFieldEntity){
//    return Response.ok()
//        .entity(tranqualise(schoolController.createSchoolField(schoolFieldEntity)))
//        .build();
//  }
  
  @Path("/schools")
  @GET
  public Response getSchools() {
    return Response.ok()
        .entity(tranqualise(schoolController.getSchools()))
        .build();
  }

  @Path("/schools/{ID:[0-9]*}")
  @GET
  public Response getSchoolById(@PathParam("ID") Long id) {
    School school = schoolController.getSchoolById(id);
    if(school != null) {
      return Response.ok()
          .entity(tranqualise(school))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/schools/{ID:[0-9]*}/variables")
  @GET
  public Response getSchoolVariablesBySchool(@PathParam("ID") Long id) {
    School school = schoolController.getSchoolById(id);
    List<SchoolVariable> schoolVariables = school.getVariables();
    if(schoolVariables != null) {
      return Response.ok()
          .entity(tranqualise(schoolVariables))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }

  @Path("/schoolFields")
  @GET
  public Response getSchoolFields() {
    return Response.ok()
        .entity(tranqualise(schoolController.getSchoolFields()))
        .build();
  }
  
  @Path("/schoolFields/{ID:[0-9]*}")
  @GET
  public Response getSchoolFieldByID(@PathParam("ID") Long id) {
    SchoolField schoolField = schoolController.getSchoolFieldById(id);
    if(schoolField != null) {
      return Response.ok()
          .entity(tranqualise(schoolField))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/variables")
  @GET
  public Response getVariables() {
    return Response.ok()
        .entity(tranqualise(schoolController.getSchoolVariables()))
        .build();
  }
  
  @Path("/variables/{ID:[0-9]*}")
  @GET
  public Response getVariablesdByID(@PathParam("ID") Long id) {
    SchoolVariable schoolVariable = schoolController.getSchoolVariabledById(id);
    if(schoolVariable != null) {
      return Response.ok()
          .entity(tranqualise(schoolVariable))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }

//  @Path("/schools")
//  @PUT
//  public Response updateSchool(SchoolComplete schoolComplete) {
//    return Response.ok()
//        .entity(tranqualise(schoolController.updateSchool(, code, name, schoolField)))
//        .build();
//  }
  
//  @Path("/schoolFields/{ID:[0-9]*}")
//  @PUT
//  public Response updateSchoolField(@PathParam("ID") Long id, SchoolFieldComplete schoolFieldEntity) {
//    SchoolField schoolField = schoolFieldController.findSchoolFieldById(id);
//    String name = schoolFieldEntity.getName();
//    return Response.ok()
//        .entity(tranqualise(schoolController.updateSchoolField(schoolField, name)))
//        .build();
//  }
  
   @Path("/schools/{ID:[0-9]*}")
   @DELETE
   public Response archiveSchool(@PathParam("ID") Long id) {
     School school = schoolController.getSchoolById(id);
     if(school != null) {
       return Response.ok()
           .entity(tranqualise(schoolController.archiveSchool(school)))
           .build();
     } else {
       return Response.status(Status.NOT_FOUND).build();
     }
   }
  
  @Path("/schoolFields/{ID:[0-9]*}")
  @DELETE
  public Response archiveSchoolField(@PathParam("ID") Long id) {
    SchoolField schoolField = schoolController.getSchoolFieldById(id);
    if(schoolField != null) {
      return Response.ok()
          .entity(tranqualise(schoolController.archiveSchoolField(schoolField)))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }

  @Override
  protected TranquilityBuilderFactory getTranquilityBuilderFactory() {
    return tranquilityBuilderFactory;
  }

}
