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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;

import fi.pyramus.domainmodel.base.EducationSubtype;
import fi.pyramus.domainmodel.base.EducationType;
import fi.pyramus.domainmodel.base.Subject;
import fi.pyramus.rest.controller.CommonController;
import fi.pyramus.rest.model.ObjectFactory;

@Path("/common")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateful
@RequestScoped
public class CommonRESTService extends AbstractRESTService {

  @Inject
  private CommonController commonController;

  @Inject
  private ObjectFactory objectFactory;
  
  @Path("/educationTypes")
  @POST
  public Response createEducationType(EducationType educationTypeEntity) {
    String name = educationTypeEntity.getName();
    String code = educationTypeEntity.getCode();
    
    if (StringUtils.isBlank(name) || StringUtils.isBlank(code)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    return Response
        .ok(objectFactory.createModel(commonController.createEducationType(name, code)))
        .build();
  }

  @Path("/educationTypes")
  @GET
  public Response listEducationTypes(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<EducationType> educationTypes;
    if (filterArchived) {
      educationTypes = commonController.listUnarchivedEducationTypes();
    } else {
      educationTypes = commonController.listEducationTypes();
    }
    
    if (educationTypes.isEmpty()) {
      return Response.noContent().build();
    }
    
    return Response.ok(objectFactory.createModel(educationTypes)).build();
  }
  
  @Path("/educationTypes/{ID:[0-9]*}")
  @GET
  public Response findEducationTypeById(@PathParam("ID") Long id) {
    EducationType educationType = commonController.findEducationTypeById(id);
    if (educationType == null) {
      return Response.status(Status.NOT_FOUND).build();
    }    
    
    if (educationType.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }    

    return Response.ok(objectFactory.createModel(educationType)).build();
  }

  @Path("/educationTypes/{ID:[0-9]*}")
  @PUT
  public Response updateEducationType(@PathParam("ID") Long id, fi.pyramus.rest.model.EducationType entity) {
    EducationType educationType = commonController.findEducationTypeById(id);
    if (educationType == null) {
      return Response.status(Status.NOT_FOUND).build();
    }    
    
    if (educationType.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }    
    
    String name = entity.getName();
    String code = entity.getCode();
    
    if (StringUtils.isBlank(name) || StringUtils.isBlank(code)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    return Response.ok().entity(objectFactory.createModel(commonController.updateEducationType(educationType, name, code))).build();
  }
      
  @Path("/educationTypes/{ID:[0-9]*}")
  @DELETE
  public Response deleteEducationType(@PathParam("ID") Long id, @DefaultValue ("false") @QueryParam ("permanent") Boolean permanent) {
    EducationType educationType = commonController.findEducationTypeById(id);
    if (educationType == null) {
      return Response.status(Status.NOT_FOUND).build();
    }    
    
    if (permanent) {
      commonController.deleteEducationType(educationType);
    } else {
      commonController.archiveEducationType(educationType, getLoggedUser());
    }
    
    return Response.noContent().build();
  }
  
  @Path("/educationTypes/{ID}/subtypes")
  @POST
  public Response createEducationSubtype(@PathParam ("ID") Long educationTypeId, fi.pyramus.rest.model.EducationSubtype entity) {
    String name = entity.getName();
    String code = entity.getCode();
    
    if (educationTypeId == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    if (StringUtils.isBlank(name) || StringUtils.isBlank(code)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    EducationType educationType = commonController.findEducationTypeById(educationTypeId);
    if (educationType == null) {
      return Response.status(Status.NOT_FOUND).build(); 
    }
    
    return Response
        .ok(objectFactory.createModel(commonController.createEducationSubtype(educationType, name, code)))
        .build();
  }

  @Path("/educationTypes/{ID}/subtypes")
  @GET
  public Response listEducationTypes(@PathParam ("ID") Long educationTypeId) {
    if (educationTypeId == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    EducationType educationType = commonController.findEducationTypeById(educationTypeId);
    if (educationType == null) {
      return Response.status(Status.NOT_FOUND).build(); 
    }
    
    List<EducationSubtype> educationSubtypes = commonController.listEducationSubtypesByEducationType(educationType);
    if (educationSubtypes.isEmpty()) {
      return Response.noContent().build();
    }
    
    return Response.ok(objectFactory.createModel(educationSubtypes)).build();
  }
  
  @Path("/educationTypes/{EDUCATIONTYPEID}/subtypes/{EDUCATIONSUBTYPEID:[0-9]*}")
  @GET
  public Response findEducationTypeById(@PathParam("EDUCATIONTYPEID") Long educationTypeId, @PathParam ("EDUCATIONSUBTYPEID") Long educationSubtypeId) {
    if ((educationTypeId == null) || (educationSubtypeId == null)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    EducationType educationType = commonController.findEducationTypeById(educationTypeId);
    if (educationType == null) {
      return Response.status(Status.NOT_FOUND).build();
    }    
    
    if (educationType.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }    
    
    EducationSubtype educationSubtype = commonController.findEducationSubtypeById(educationSubtypeId);
    if (educationSubtype == null) {
      return Response.status(Status.NOT_FOUND).build();
    }    
    
    if (educationSubtype.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }    
    
    if (!educationSubtype.getEducationType().getId().equals(educationTypeId)) {
      return Response.status(Status.NOT_FOUND).build();
    }    

    return Response.ok(objectFactory.createModel(educationSubtype)).build();
  }

  @Path("/educationTypes/{EDUCATIONTYPEID}/subtypes/{EDUCATIONSUBTYPEID:[0-9]*}")
  @PUT
  public Response updateEducationSubtype(@PathParam("EDUCATIONTYPEID") Long educationTypeId, @PathParam ("EDUCATIONSUBTYPEID") Long educationSubtypeId, fi.pyramus.rest.model.EducationSubtype entity) {
    if ((educationTypeId == null) || (educationSubtypeId == null)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    String name = entity.getName();
    String code = entity.getCode();
    
    if (StringUtils.isBlank(name) || StringUtils.isBlank(code)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    EducationSubtype educationSubtype = commonController.findEducationSubtypeById(educationSubtypeId);
    if (educationSubtype == null) {
      return Response.status(Status.NOT_FOUND).build();
    }    
    
    if (educationSubtype.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }    
    
    if (!educationSubtype.getEducationType().getId().equals(educationTypeId)) {
      return Response.status(Status.NOT_FOUND).build();
    }    
    
    EducationType educationType = commonController.findEducationTypeById(entity.getEducationTypeId());
    if (educationType == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }    
    
    if (educationType.getArchived()) {
      return Response.status(Status.BAD_REQUEST).build();
    }    
    
    return Response.ok().entity(objectFactory.createModel(commonController.updateEducationSubtype(educationSubtype, educationType, name, code))).build();
  }
      
  @Path("/educationTypes/{EDUCATIONTYPEID}/subtypes/{EDUCATIONSUBTYPEID:[0-9]*}")
  @DELETE
  public Response deleteEducationSubtype(@PathParam("EDUCATIONTYPEID") Long educationTypeId, @PathParam ("EDUCATIONSUBTYPEID") Long educationSubtypeId, @DefaultValue ("false") @QueryParam ("permanent") Boolean permanent) {
    if ((educationTypeId == null) || (educationSubtypeId == null)) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    EducationType educationType = commonController.findEducationTypeById(educationTypeId);
    if (educationType == null) {
      return Response.status(Status.NOT_FOUND).entity("Could not find educationType").build();
    }    
    
    if (educationType.getArchived()) {
      return Response.status(Status.NOT_FOUND).entity("Could not find educationType/A").build();
    }    
    
    EducationSubtype educationSubtype = commonController.findEducationSubtypeById(educationSubtypeId);
    if (educationSubtype == null) {
      return Response.status(Status.NOT_FOUND).entity("Could not find subtype").build();
    }    
    
    if (educationSubtype.getArchived()) {
      return Response.status(Status.NOT_FOUND).entity("Could not find subtype/2").build();
    }    
    
    if (!educationSubtype.getEducationType().getId().equals(educationTypeId)) {
      return Response.status(Status.NOT_FOUND).entity("Could not find subtype/3 " + educationSubtype.getEducationType().getId() + " != " + educationTypeId).build();
    }  
    
    if (permanent) {
      commonController.deleteEducationSubtype(educationSubtype);
    } else {
      commonController.archiveEducationSubtype(educationSubtype, getLoggedUser());
    }
    
    return Response.noContent().build();
  }

  @Path("/educationTypes/{ID:[0-9]*}/subjects")
  @GET
  public Response findSubjectsByEducationType(@PathParam("ID") Long id) {
    EducationType educationType = commonController.findEducationTypeById(id);
    if (educationType == null) {
      return Response.status(Status.NOT_FOUND).build();
    }   
    
    if (educationType.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }   
    
    return Response.ok()
        .entity(objectFactory.createModel(commonController.listSubjectsByEducationType(educationType)))
        .build();
  }
  
  @Path("/subjects")
  @POST
  public Response createSubject(fi.pyramus.rest.model.Subject entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    String name = entity.getName();
    String code = entity.getCode();
    
    if (entity.getEducationTypeId() == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    if (StringUtils.isBlank(name) || StringUtils.isBlank(code)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    EducationType educationType = commonController.findEducationTypeById(entity.getEducationTypeId());
    if (educationType == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    return Response.ok().entity(objectFactory.createModel(commonController.createSubject(code, name, educationType))).build();
  }
  
  @Path("/subjects")
  @GET
  public Response listSubjects(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<Subject> subjects;
    
    if (filterArchived) {
      subjects = commonController.listUnarchivedSubjects();
    } else {
      subjects = commonController.listSubjects();
    }
    
    if (subjects.isEmpty()) {
      return Response.noContent().build();
    }
    
    return Response.ok()
      .entity(objectFactory.createModel(subjects))
      .build();
  }
  
  @Path("/subjects/{ID:[0-9]*}")
  @GET
  public Response findSubject(@PathParam("ID") Long id) {
    Subject subject = commonController.findSubjectById(id);
    if (subject == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (subject.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    return Response.ok()
        .entity(objectFactory.createModel(subject))
        .build();
  }

  @Path("/subjects/{ID:[0-9]*}")
  @PUT
  public Response updateSubject(@PathParam("ID") Long id, fi.pyramus.rest.model.Subject entity) {
    Subject subject = commonController.findSubjectById(id);
    if (subject == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (subject.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    String name = entity.getName();
    String code = entity.getCode();
    
    if (entity.getEducationTypeId() == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    if (StringUtils.isBlank(name) || StringUtils.isBlank(code)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    EducationType educationType = commonController.findEducationTypeById(entity.getEducationTypeId());
    if (educationType == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    return Response.ok(objectFactory.createModel(commonController.updateSubject(subject, code, name, educationType))).build();
  }

  @Path("/subjects/{ID:[0-9]*}")
  @DELETE
  public Response archiveSubject(@PathParam("ID") Long id, @DefaultValue ("false") @QueryParam ("permanent") Boolean permanent) {
    Subject subject = commonController.findSubjectById(id);
    if (subject == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (permanent) {
      commonController.deleteSubject(subject);
    } else {
      commonController.archiveSubject(subject, getLoggedUser());
    }

    return Response.noContent().build();
  }
  
//  @Path("/gradingScales/{ID:[0-9]*}")
//  @DELETE
//  public Response archiveGradingScale(@PathParam("ID") Long id)  {
//    GradingScale gradingScale = commonController.findGradingScaleById(id);
//    if (gradingScale != null) {
//      return Response.ok()
//          .entity(tranqualise(commonController.archiveGradingScale(gradingScale, getUser())))
//          .build();
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
  

//  @Path("/gradingScales")
//  @POST
//  public Response createGradingScale(GradingScaleEntity gradingScaleEntity) {
//    String name = gradingScaleEntity.getName();
//    String description = gradingScaleEntity.getDescription();
//    if (!StringUtils.isBlank(name) && !StringUtils.isBlank(description)) {
//      return Response.ok()
//          .entity(tranqualise(commonController.createGradingScale(name, description)))
//          .build();
//    } else {
//      return Response.status(500).build();
//    }
//  }
//  
//  @Path("/educationalTimeUnits") 
//  @POST
//  public Response createEducationalTimeUnit(EducationalTimeUnitEntity educationalTimeUnitEntity) {
//    Double baseUnits = educationalTimeUnitEntity.getBaseUnits();
//    String name = educationalTimeUnitEntity.getName();
//    if (!StringUtils.isBlank(name) && baseUnits != null) {
//      return Response.ok()
//          .entity(tranqualise(commonController.createEducationalTimeUnit(baseUnits, name)))
//          .build();
//    } else {
//      return Response.status(500).build();
//    }
//  }
//  

//  @Path("/gradingScales")
//  @GET
//  public Response findGradingScales(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
//    List<GradingScale> gradingScales;
//    if (filterArchived) {
//      gradingScales = commonController.findUnarchivedGradingScales();
//    } else {
//      gradingScales = commonController.findGradingScales();
//    }
//    if (!gradingScales.isEmpty()) {
//      return Response.ok()
//          .entity(tranqualise(gradingScales))
//          .build();
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//  
//  @Path("/gradingScales/{ID:[0-9]*}")
//  @GET
//  public Response findGradingScalesById(@PathParam("ID") Long id) {
//    GradingScale gradingScale = commonController.findGradingScaleById(id);
//    if (gradingScale != null) {
//      return Response.ok()
//          .entity(tranqualise(gradingScale))
//          .build();
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//  
//  @Path("/educationalTimeUnits")
//  @GET
//  public Response findEducationalTimeUnits(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
//    List<EducationalTimeUnit> educationalTimeUnits;
//    if (filterArchived) {
//      educationalTimeUnits = commonController.findUnarchivedEducationalTimeUnits();
//    } else {
//      educationalTimeUnits = commonController.findEducationalTimeUnits();
//    }
//    if (!educationalTimeUnits.isEmpty()) {
//      return Response.ok()
//          .entity(tranqualise(educationalTimeUnits))
//          .build();
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//  
//  @Path("/educationalTimeUnits/{ID:[0-9]*}")
//  @GET
//  public Response findEducationalTimeUnitsById(@PathParam("ID") Long id) {
//    EducationalTimeUnit educationalTimeUnit = commonController.findEducationalTimeUnitById(id);
//    if (educationalTimeUnit != null) {
//      return Response.ok()
//          .entity(tranqualise(educationalTimeUnit))
//          .build();
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }


//  @Path("/gradingScales/{ID:[0-9]*}")
//  @PUT
//  public Response updateGradingScale(@PathParam("ID") Long id, GradingScaleEntity gradingScaleEntity) {
//    GradingScale gradingScale = commonController.findGradingScaleById(id);
//    if (gradingScale != null) {
//      String name = gradingScaleEntity.getName();
//      String description = gradingScaleEntity.getDescription();
//      if (!StringUtils.isBlank(name) && !StringUtils.isBlank(description)) {
//        return Response.ok()
//            .entity(tranqualise(commonController.updateGradingScale(gradingScale, name, description)))
//            .build();
//      }
//      if (!gradingScaleEntity.getArchived()) {
//        return Response.ok()
//            .entity(tranqualise(commonController.unarchiveGradingScale(gradingScale, getUser())))
//            .build();
//      } else {
//        return Response.status(500).build();
//      }
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//  
//  @Path("/educationalTimeUnits/{ID:[0-9]*}")
//  @PUT
//  public Response updateEducationalTimeUnit(@PathParam("ID") Long id, EducationalTimeUnitEntity educationalTimeUnitEntity) {
//    EducationalTimeUnit educationalTimeUnit = commonController.findEducationalTimeUnitById(id);
//    if (educationalTimeUnit != null) {
//      Double baseUnits = educationalTimeUnitEntity.getBaseUnits();
//      String name = educationalTimeUnitEntity.getName();
//      if (!StringUtils.isBlank(name) && baseUnits != null) {
//        return Response.ok()
//            .entity(tranqualise(commonController.updateEducationalTimeUnit(educationalTimeUnit, baseUnits, name)))
//            .build();
//      }
//      if (!educationalTimeUnitEntity.getArchived()) {
//        return Response.ok()
//            .entity(tranqualise(commonController.unarchiveEducationalTimeUnit(educationalTimeUnit, getUser())))
//            .build();
//      } else {
//        return Response.status(500).build();
//      }
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//  
//  @Path("/educationalTimeUnits/{ID:[0-9]*}")
//  @DELETE
//  public Response archiveEducationalTimeUnit(@PathParam("ID") Long id) {
//    EducationalTimeUnit educationalTimeUnit = commonController.findEducationalTimeUnitById(id);
//    if (educationalTimeUnit != null) {
//      return Response.ok()
//          .entity(tranqualise(commonController.archiveEducationalTimeUnit(educationalTimeUnit, getUser())))
//          .build();
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }

}
