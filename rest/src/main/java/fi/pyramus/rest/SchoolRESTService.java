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
    SchoolField schoolField = schoolController.findSchoolFieldById(schoolEntity.getField_id());
    String schoolCode = schoolEntity.getCode();
    String schoolName = schoolEntity.getName();
    if(!schoolCode.isEmpty() && !schoolName.isEmpty() && !schoolField.equals(null)) {
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
    if(!name.isEmpty()) {
      return Response.ok()
          .entity(tranqualise(schoolController.createSchoolField(name)))
          .build();
    } else {
      return Response.status(501).build();
    }
  }
  
  @Path("/schools")
  @GET
  public Response findSchools() {
    return Response.ok()
        .entity(tranqualise(schoolController.findSchools()))
        .build();
  }

  @Path("/schools/{ID:[0-9]*}")
  @GET
  public Response findSchoolById(@PathParam("ID") Long id) {
    School school = schoolController.findSchoolById(id);
    if(!school.equals(null)) {
      return Response.ok()
          .entity(tranqualise(school))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }

  @Path("/schoolFields")
  @GET
  public Response findSchoolFields() {
    return Response.ok()
        .entity(tranqualise(schoolController.findSchoolFields()))
        .build();
  }
  
  @Path("/schoolFields/{ID:[0-9]*}")
  @GET
  public Response findSchoolFieldByID(@PathParam("ID") Long id) {
    SchoolField schoolField = schoolController.findSchoolFieldById(id);
    if(!schoolField.equals(null)) {
      return Response.ok()
          .entity(tranqualise(schoolField))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/variables")
  @GET
  public Response findSchoolVariables() {
    return Response.ok()
        .entity(tranqualise(schoolController.findSchoolVariables()))
        .build();
  }
  
  @Path("/variables/{ID:[0-9]*}")
  @GET
  public Response findVariablesByID(@PathParam("ID") Long id) {
    SchoolVariable schoolVariable = schoolController.findSchoolVariablesById(id);
    if(!schoolVariable.equals(null)) {
      return Response.ok()
          .entity(tranqualise(schoolVariable))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/schools/{ID:[0-9]*}/variables")
  @GET
  public Response findSchoolVariablesBySchool(@PathParam("ID") Long id) {
    School school = schoolController.findSchoolById(id);
    List<SchoolVariable> schoolVariables = school.getVariables();
    if(!schoolVariables.equals(null)) {
      return Response.ok()
          .entity(tranqualise(schoolVariables))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }

  @Path("/schools/{ID:[0-9]*}")
  @POST
  public Response updateSchool(@PathParam("ID") Long id, SchoolEntity schoolEntity) {
    School school = schoolController.findSchoolById(id);
    SchoolField schoolField = schoolController.findSchoolFieldById(schoolEntity.getField_id());
    String schoolCode = schoolEntity.getCode();
    String schoolName = schoolEntity.getName();
    if(!school.equals(null) && !schoolCode.isEmpty() && !schoolName.isEmpty() && !schoolField.equals(null)) {
      return Response.ok()
          .entity(tranqualise(schoolController.updateSchool(school, schoolCode, schoolName, schoolField)))
          .build();
    } else {
      return Response.status(501).build();
    }
  }
  
  @Path("/schoolFields/{ID:[0-9]*}")
  @POST
  public Response updateSchoolField(@PathParam("ID") Long id, SchoolFieldEntity schoolFieldEntity) {
    SchoolField schoolField = schoolController.findSchoolFieldById(id);
    String name = schoolFieldEntity.getName();
    if(!schoolField.equals(null) && !name.isEmpty()){
      return Response.ok()
          .entity(tranqualise(schoolController.updateSchoolField(schoolField, name)))
          .build();
    } else {
      return Response.status(501).build();
    }
  }
  
  @Path("/variables/{ID:[0-9]*}")
  @POST
  public Response updateSchoolVariable(@PathParam("ID") Long id, SchoolVariableEntity schoolVariableEntity) {
    SchoolVariable schoolVariable = schoolController.findSchoolVariablesById(id);
    String value = schoolVariableEntity.getValue();
    if(!schoolVariable.equals(null) && !value.isEmpty()) {
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
    School school = schoolController.findSchoolById(id);
       if(!school.equals(null)) {
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
    SchoolField schoolField = schoolController.findSchoolFieldById(id);
    if(!schoolField.equals(null)) {
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
