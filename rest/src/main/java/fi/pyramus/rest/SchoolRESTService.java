package fi.pyramus.rest;

import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import fi.pyramus.domainmodel.base.School;
import fi.pyramus.domainmodel.base.SchoolField;
import fi.pyramus.domainmodel.base.SchoolVariable;
import fi.pyramus.rest.controller.SchoolController;
import fi.pyramus.rest.tranquil.base.SchoolEntity;
import fi.pyramus.rest.tranquil.base.SchoolFieldEntity;
import fi.pyramus.rest.tranquil.base.SchoolVariableEntity;
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

  @Path("/schools")
  @POST
  public Response createSchool(SchoolEntity schoolEntity) {
    SchoolField schoolField = schoolController.getSchoolFieldById(schoolEntity.getField_id());
    String schoolCode = schoolEntity.getCode();
    String schoolName = schoolEntity.getName();
    if(schoolCode != null && schoolName != null && schoolField != null) {
      return Response.ok()
          .entity(tranqualise(schoolController.createSchool(schoolCode, schoolName, schoolField)))
          .build();
    } else {
      return Response.status(501).build();
    }
  }
  
  @Path("/schoolFields")
  @POST
  public Response createSchoolField(SchoolFieldEntity schoolFieldEntity) {
    String name = schoolFieldEntity.getName();
    if(name != null) {
      return Response.ok()
          .entity(tranqualise(schoolController.createSchoolField(name)))
          .build();
    } else {
      return Response.status(501).build();
    }
  }
  
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

  @Path("/schools/{ID:[0-9]*}")
  @PUT
  public Response updateSchool(@PathParam("ID") Long id, SchoolEntity schoolEntity) {
    School school = schoolController.getSchoolById(id);
    SchoolField schoolField = schoolController.getSchoolFieldById(schoolEntity.getField_id());
    String schoolCode = schoolEntity.getCode();
    String schoolName = schoolEntity.getName();
    if(school != null && schoolCode != null && schoolName != null && schoolField != null) {
      return Response.ok()
          .entity(tranqualise(schoolController.updateSchool(school, schoolCode, schoolName, schoolField)))
          .build();
    } else {
      return Response.status(501).build();
    }
  }
  
  @Path("/schoolFields/{ID:[0-9]*}")
  @PUT
  public Response updateSchoolField(@PathParam("ID") Long id, SchoolFieldEntity schoolFieldEntity) {
    SchoolField schoolField = schoolController.getSchoolFieldById(id);
    String name = schoolFieldEntity.getName();
    if(schoolField != null && name != null){
      return Response.ok()
          .entity(tranqualise(schoolController.updateSchoolField(schoolField, name)))
          .build();
    } else {
      return Response.status(501).build();
    }
  }
  
  @Path("/variables/{ID:[0-9]*}")
  @PUT
  public Response updateSchoolVariable(@PathParam("ID") Long id, SchoolVariableEntity schoolVariableEntity) {
    SchoolVariable schoolVariable = schoolController.getSchoolVariabledById(id);
    String value = schoolVariableEntity.getValue();
    if(schoolVariable != null && value != null) {
      return Response.ok()
          .entity(tranqualise(schoolController.updateSchoolVariable(schoolVariable, value)))
          .build();
    } else {
      return Response.status(501).build();
    }
  }
  
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
