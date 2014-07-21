package fi.pyramus.rest;

import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;

import fi.pyramus.domainmodel.base.School;
import fi.pyramus.domainmodel.base.SchoolField;
import fi.pyramus.rest.controller.SchoolController;
import fi.pyramus.rest.model.ObjectFactory;

@Path("/schools")
@Produces("application/json")
@Consumes("application/json")
@Stateful
@RequestScoped
public class SchoolRESTService extends AbstractRESTService {
  
  @Inject
  private SchoolController schoolController;

  @Inject
  private ObjectFactory objectFactory;
  
  @Path("/schools")
  @POST
  public Response createSchool(fi.pyramus.rest.model.School entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    if (entity.getFieldId() == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    String code = entity.getCode();
    String name = entity.getName();
    
    if (StringUtils.isBlank(code) || StringUtils.isBlank(name)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    SchoolField schoolField = schoolController.findSchoolFieldById(entity.getFieldId());
    if (schoolField == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }      
    
    School school = schoolController.createSchool(code, name, schoolField);
    if (entity.getTags() != null) {
      for (String tag : entity.getTags()) {
        schoolController.createSchoolTag(school, tag);
      }
    }

    return Response.ok(objectFactory.createModel(school)).build();
  }

  @Path("/schools")
  @GET
  public Response listSchools(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<School> schools;
    
    if (filterArchived) {
      schools = schoolController.listUnarchivedSchools();
    } else {
      schools = schoolController.listSchools();
    }
    
    if (schools.isEmpty()) {
      return Response.noContent().build();
    }
    
    return Response.ok(objectFactory.createModel(schools)).build();
  }
  
  @Path("/schools/{ID:[0-9]*}")
  @GET
  public Response findSchool(@PathParam("ID") Long id) {
    School school = schoolController.findSchoolById(id);
    if (school == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (school.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    return Response.ok(objectFactory.createModel(school)).build();
  }

  @Path("/schools/{ID:[0-9]*}")
  @PUT
  public Response updateSchool(@PathParam("ID") Long id, fi.pyramus.rest.model.School entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    School school = schoolController.findSchoolById(id);
    if (school == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (school.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (entity.getFieldId() == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    String code = entity.getCode();
    String name = entity.getName();
    
    if (StringUtils.isBlank(code) || StringUtils.isBlank(name)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    SchoolField schoolField = schoolController.findSchoolFieldById(entity.getFieldId());
    if (schoolField == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }      
    
    schoolController.updateSchool(school, code, name, schoolField);
    schoolController.updateSchoolTags(school, entity.getTags());
    
    return Response.ok(objectFactory.createModel(school)).build();
  }
  
  @Path("/schools/{ID:[0-9]*}")
  @DELETE
  public Response deleteSchool(@PathParam("ID") Long id, @DefaultValue ("false") @QueryParam ("permanent") Boolean permanent) {
    School school = schoolController.findSchoolById(id);
    if (school == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
  
    if (permanent) {
      schoolController.deleteSchool(school);
    } else {
      schoolController.archiveSchool(school, getLoggedUser());
    }
    
    return Response.noContent().build();
  }
  
  @Path("/schoolFields")
  @POST
  public Response createSchoolField(fi.pyramus.rest.model.SchoolField entity) {
    String name = entity.getName();
    if (StringUtils.isBlank(name)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    return Response.ok(objectFactory.createModel(schoolController.createSchoolField(name))).build();
  }
  
  @Path("/schoolFields")
  @GET
  public Response listSchoolFields() {
    List<SchoolField> schoolFields = schoolController.listSchoolFields();
    if (schoolFields.isEmpty()) {
      return Response.noContent().build();
    }
    
    return Response.ok(objectFactory.createModel(schoolFields)).build();
  }
  
  @Path("/schoolFields/{ID:[0-9]*}")
  @GET
  public Response findSchoolFieldByID(@PathParam("ID") Long id) {
    SchoolField schoolField = schoolController.findSchoolFieldById(id);
    if (schoolField == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (schoolField.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    return Response.ok(objectFactory.createModel(schoolField)).build();
  }
  
  @Path("/schoolFields/{ID:[0-9]*}")
  @PUT
  public Response updateSchoolField(@PathParam("ID") Long id, fi.pyramus.rest.model.SchoolField entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    SchoolField schoolField = schoolController.findSchoolFieldById(id);
    if (schoolField == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    String name = entity.getName();
    
    if (StringUtils.isBlank(name)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    return Response.ok(objectFactory.createModel(schoolController.updateSchoolField(schoolField, name))).build();
  }
    
  @Path("/schoolFields/{ID:[0-9]*}")
  @DELETE
  public Response deleteSchoolField(@PathParam("ID") Long id, @DefaultValue ("false") @QueryParam ("permanent") Boolean permanent) {
    SchoolField schoolField = schoolController.findSchoolFieldById(id);
    if (schoolField == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (permanent) {
      schoolController.deleteSchoolField(schoolField);
    } else {
      schoolController.archiveSchoolField(schoolField, getLoggedUser());
    }
    
    return Response.noContent().build();
  }
//  
//  @Path("/variables/{ID:[0-9]*}")
//  @DELETE
//  public Response archiveSchoolVariable(@PathParam("ID") Long id) {
//    SchoolVariable schoolVariable = schoolController.findSchoolVariableById(id);
//    if (schoolVariable != null) {
//      return Response.ok()
//          .entity(tranqualise(schoolController.archiveSchoolVariable(schoolVariable, getUser())))
//          .build();
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }

  

//  
//  @Path("/variables")
//  @POST
//  public Response createSchoolVariable(SchoolVariableEntity schoolVariableEntity) {
//    School school = schoolController.findSchoolById(schoolVariableEntity.getSchool_id());
//    Long keyId = schoolVariableEntity.getKey_id();
//    String value = schoolVariableEntity.getValue();
//    if (school != null && keyId != null && !StringUtils.isBlank(value)) {
//      return Response.ok()
//          .entity(tranqualise(schoolController.createSchoolVariable(school, keyId, value)))
//          .build();
//    } else {
//      return Response.status(501).build();
//    }
//  }
//  


//  @Path("/variables")
//  @GET
//  public Response findSchoolVariables() {
//    List<SchoolVariable> schoolVariables = schoolController.findSchoolVariables();
//    if (!schoolVariables.isEmpty()) {
//      return Response.ok()
//          .entity(tranqualise(schoolController.findSchoolVariables()))
//          .build();
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//  
//  @Path("/variables/{ID:[0-9]*}")
//  @GET
//  public Response findScoolVariableByID(@PathParam("ID") Long id) {
//    SchoolVariable schoolVariable = schoolController.findSchoolVariableById(id);
//    if (schoolVariable != null) {
//      return Response.ok()
//          .entity(tranqualise(schoolVariable))
//          .build();
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//  
//  @Path("/schools/{ID:[0-9]*}/variables")
//  @GET
//  public Response findSchoolVariablesBySchool(@PathParam("ID") Long id) {
//    School school = schoolController.findSchoolById(id);
//    if(school != null)  {
//      List<SchoolVariable> schoolVariables = school.getVariables();
//      if (schoolVariables != null) {
//        return Response.ok()
//            .entity(tranqualise(schoolVariables))
//            .build();
//      } else {
//        return Response.status(Status.NOT_FOUND).build();
//      }
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//  
//  @Path("/schools/{SID:[0-9]*}/variables/{ID:[0-9]*}")
//  @GET
//  public Response findSchoolVariableBySchool(@PathParam("SID") Long schoolId, @PathParam("ID") Long id) {
//    School school = schoolController.findSchoolById(schoolId);
//    SchoolVariable schoolVariable = schoolController.findSchoolVariableById(id);
//    if (school != null && schoolVariable != null) {
//      List<SchoolVariable> schoolVariables = school.getVariables();
//      if (schoolVariables.contains(schoolVariable)) {
//        return Response.ok()
//            .entity(tranqualise(schoolVariable))
//            .build();
//      } else {
//        return Response.status(Status.NOT_FOUND).build();
//      }
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
  
//  @Path("/variables/{ID:[0-9]*}")
//  @PUT
//  public Response updateSchoolVariable(@PathParam("ID") Long id, SchoolVariableEntity schoolVariableEntity) {
//    SchoolVariable schoolVariable = schoolController.findSchoolVariableById(id);
//    String value = schoolVariableEntity.getValue();
//    if (schoolVariable != null && !StringUtils.isBlank(value)) {
//      return Response.ok()
//          .entity(tranqualise(schoolController.updateSchoolVariable(schoolVariable, value)))
//          .build();
//    } else if (!schoolVariableEntity.getArchived()) {
//      return Response.ok()
//          .entity(tranqualise(schoolController.unarchiveSchoolVariable(schoolVariable, getUser())))
//          .build();
//    }  else {
//      return Response.status(501).build();
//    }
//  }

}
