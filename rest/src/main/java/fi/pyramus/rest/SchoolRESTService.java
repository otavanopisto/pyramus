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

import org.apache.commons.lang.StringUtils;

import fi.pyramus.domainmodel.base.School;
import fi.pyramus.domainmodel.base.SchoolField;
import fi.pyramus.domainmodel.base.SchoolVariable;
import fi.pyramus.persistence.search.SearchResult;
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
    if (!StringUtils.isBlank(schoolCode) && !StringUtils.isBlank(schoolName) && schoolField != null) {
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
    if (!StringUtils.isBlank(name)) {
      return Response.ok()
          .entity(tranqualise(schoolController.createSchoolField(name)))
          .build();
    } else {
      return Response.status(501).build();
    }
  }
  
  @Path("/variables")
  @POST
  public Response createSchoolVariable(SchoolVariableEntity schoolVariableEntity) {
    School school = schoolController.findSchoolById(schoolVariableEntity.getSchool_id());
    Long keyId = schoolVariableEntity.getKey_id();
    String value = schoolVariableEntity.getValue();
    if (school != null && keyId != null && !StringUtils.isBlank(value)) {
      return Response.ok()
          .entity(tranqualise(schoolController.createSchoolVariable(school, keyId, value)))
          .build();
    } else {
      return Response.status(501).build();
    }
  }
  
  @Path("/schools")
  @GET
  public Response findSchools(@QueryParam("code") String code,
                              @QueryParam("name") String name,
                              @QueryParam("tags") String tags,
                              @DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    if (StringUtils.isBlank(code) && StringUtils.isBlank(name) && StringUtils.isBlank(tags)) {
      List<School> schools;
      if (filterArchived) {
        schools = schoolController.findUnarchivedSchools();
      } else {
        schools = schoolController.findSchools();
      }
      if (!schools.isEmpty()){
        return Response.ok()
            .entity(tranqualise(schools))
            .build();
      } else {
        return Response.status(Status.NOT_FOUND).build();
      }
    } else {
      SearchResult<School> schools = schoolController.searchSchools(100,0,code,name,tags,filterArchived);
      if (!schools.getResults().isEmpty()) {
        return Response.ok()
          .entity(tranqualise(schools.getResults()))
          .build();
      } else {
        return Response.status(Status.NOT_FOUND).build();
      }
    }
  }

  @Path("/schools/{ID:[0-9]*}")
  @GET
  public Response findSchoolById(@PathParam("ID") Long id) {
    School school = schoolController.findSchoolById(id);
    if (school != null) {
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
    List<SchoolField> schoolFields = schoolController.findSchoolFields();
    if (!schoolFields.isEmpty()) {
      return Response.ok()
          .entity(tranqualise(schoolFields))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/schoolFields/{ID:[0-9]*}")
  @GET
  public Response findSchoolFieldByID(@PathParam("ID") Long id) {
    SchoolField schoolField = schoolController.findSchoolFieldById(id);
    if (schoolField != null) {
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
    List<SchoolVariable> schoolVariables = schoolController.findSchoolVariables();
    if (!schoolVariables.isEmpty()) {
      return Response.ok()
          .entity(tranqualise(schoolController.findSchoolVariables()))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/variables/{ID:[0-9]*}")
  @GET
  public Response findScoolVariableByID(@PathParam("ID") Long id) {
    SchoolVariable schoolVariable = schoolController.findSchoolVariableById(id);
    if (schoolVariable != null) {
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
    if(school != null)  {
      List<SchoolVariable> schoolVariables = school.getVariables();
      if (schoolVariables != null) {
        return Response.ok()
            .entity(tranqualise(schoolVariables))
            .build();
      } else {
        return Response.status(Status.NOT_FOUND).build();
      }
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/schools/{SID:[0-9]*}/variables/{ID:[0-9]*}")
  @GET
  public Response findSchoolVariableBySchool(@PathParam("SID") Long schoolId, @PathParam("ID") Long id) {
    School school = schoolController.findSchoolById(schoolId);
    SchoolVariable schoolVariable = schoolController.findSchoolVariableById(id);
    if (school != null && schoolVariable != null) {
      List<SchoolVariable> schoolVariables = school.getVariables();
      if (schoolVariables.contains(schoolVariable)) {
        return Response.ok()
            .entity(tranqualise(schoolVariable))
            .build();
      } else {
        return Response.status(Status.NOT_FOUND).build();
      }
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }

  @Path("/schools/{ID:[0-9]*}")
  @PUT
  public Response updateSchool(@PathParam("ID") Long id, SchoolEntity schoolEntity) {
    School school = schoolController.findSchoolById(id);
    SchoolField schoolField = school.getField();
    String schoolCode = schoolEntity.getCode();
    String schoolName = schoolEntity.getName();
    if (school != null && !StringUtils.isBlank(schoolCode) && !StringUtils.isBlank(schoolName) && schoolField != null) {
      schoolField = schoolController.findSchoolFieldById(schoolEntity.getField_id());
      return Response.ok()
          .entity(tranqualise(schoolController.updateSchool(school, schoolCode, schoolName, schoolField)))
          .build();
    } else if (!schoolEntity.getArchived()) {
        return Response.ok()
            .entity(tranqualise(schoolController.unarchiveSchool(school, getUser())))
            .build();
    } else {
      return Response.status(501).build();
    }
  }
  
  @Path("/schoolFields/{ID:[0-9]*}")
  @PUT
  public Response updateSchoolField(@PathParam("ID") Long id, SchoolFieldEntity schoolFieldEntity) {
    SchoolField schoolField = schoolController.findSchoolFieldById(id);
    String name = schoolFieldEntity.getName();
    if (schoolField != null && !StringUtils.isBlank(name)){
      return Response.ok()
          .entity(tranqualise(schoolController.updateSchoolField(schoolField, name)))
          .build();
    } else if (!schoolFieldEntity.getArchived()) {
      return Response.ok()
          .entity(tranqualise(schoolController.unarchiveSchoolField(schoolField, getUser())))
          .build();
    } else {
      return Response.status(501).build();
    }
  }
  
  @Path("/variables/{ID:[0-9]*}")
  @PUT
  public Response updateSchoolVariable(@PathParam("ID") Long id, SchoolVariableEntity schoolVariableEntity) {
    SchoolVariable schoolVariable = schoolController.findSchoolVariableById(id);
    String value = schoolVariableEntity.getValue();
    if (schoolVariable != null && !StringUtils.isBlank(value)) {
      return Response.ok()
          .entity(tranqualise(schoolController.updateSchoolVariable(schoolVariable, value)))
          .build();
    } else if (!schoolVariableEntity.getArchived()) {
      return Response.ok()
          .entity(tranqualise(schoolController.unarchiveSchoolVariable(schoolVariable, getUser())))
          .build();
    }  else {
      return Response.status(501).build();
    }
  }
  
  @Path("/schools/{ID:[0-9]*}")
  @DELETE
  public Response archiveSchool(@PathParam("ID") Long id) {
    School school = schoolController.findSchoolById(id);
       if (school != null) {
         return Response.ok()
           .entity(tranqualise(schoolController.archiveSchool(school, getUser())))
           .build();
       } else {
         return Response.status(Status.NOT_FOUND).build();
       }
  }
  
  @Path("/schoolFields/{ID:[0-9]*}")
  @DELETE
  public Response archiveSchoolField(@PathParam("ID") Long id) {
    SchoolField schoolField = schoolController.findSchoolFieldById(id);
    if (schoolField != null) {
      return Response.ok()
          .entity(tranqualise(schoolController.archiveSchoolField(schoolField, getUser())))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/variables/{ID:[0-9]*}")
  @DELETE
  public Response archiveSchoolVariable(@PathParam("ID") Long id) {
    SchoolVariable schoolVariable = schoolController.findSchoolVariableById(id);
    if (schoolVariable != null) {
      return Response.ok()
          .entity(tranqualise(schoolController.archiveSchoolVariable(schoolVariable, getUser())))
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
