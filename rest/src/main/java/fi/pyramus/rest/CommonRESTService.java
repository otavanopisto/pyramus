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

import fi.pyramus.domainmodel.base.EducationType;
import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.base.Subject;
import fi.pyramus.domainmodel.grading.GradingScale;
import fi.pyramus.persistence.search.SearchResult;
import fi.pyramus.rest.controller.CommonController;

@Path("/common")
@Produces("application/json")
@Consumes("application/json")
@Stateful
@RequestScoped
public class CommonRESTService extends AbstractRESTService {
//  
//  @Inject
//  private CommonController commonController;
//  
//  @Path("/educationTypes")
//  @POST
//  public Response createEducationType(EducationTypeEntity educationTypeEntity) {
//    String name = educationTypeEntity.getName();
//    String code = educationTypeEntity.getCode();
//    if (!StringUtils.isBlank(name) && !StringUtils.isBlank(code)) {
//      return Response.ok()
//          .entity(tranqualise(commonController.createEducationType(name, code)))
//          .build();
//    } else {
//      return Response.status(500).build();
//    }
//  }
//
//  @Path("/subjects")
//  @POST
//  public Response createSubject(SubjectEntity subjectEntity) {
//    String name = subjectEntity.getName();
//    String code = subjectEntity.getCode();
//    EducationType educationType = commonController.findEducationTypeById(subjectEntity.getEducationType_id());
//    if (!StringUtils.isBlank(name) && !StringUtils.isBlank(code) && educationType != null) {
//      return Response.ok()
//          .entity(tranqualise(commonController.createSubject(code, name, educationType)))
//          .build();
//    } else {
//      return Response.status(500).build();
//    }
//  }
//  
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
//  @Path("/educationTypes")
//  @GET
//  public Response findEducationTypes(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
//    List<EducationType> educationTypes;
//    if (filterArchived) {
//      educationTypes = commonController.findUnarchivedEducationTypes();
//    } else {
//      educationTypes = commonController.findEducationTypes();
//    }
//    if (!educationTypes.isEmpty()) {
//      return Response.ok()
//          .entity(tranqualise(educationTypes))
//          .build();
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//  
//  @Path("/educationTypes/{ID:[0-9]*}")
//  @GET
//  public Response findEducationTypeById(@PathParam("ID") Long id) {
//    EducationType educationType = commonController.findEducationTypeById(id);
//    if (educationType != null) {
//      return Response.ok()
//          .entity(tranqualise(educationType))
//          .build();
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//  
//  @Path("/educationTypes/{ID:[0-9]*}/subjects")
//  @GET
//  public Response findSubjectsByEducationType(@PathParam("ID") Long id) {
//    EducationType educationType = commonController.findEducationTypeById(id);
//    if (educationType != null) {
//      return Response.ok()
//          .entity(tranqualise(commonController.findSubjectsByEducationType(educationType)))
//          .build();
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//  
//  @Path("/subjects")
//  @GET
//  public Response findSubjects(@QueryParam("text") String text, @DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
//    if(StringUtils.isBlank(text)) {
//      List<Subject> subjects;
//      if (filterArchived) {
//        subjects = commonController.findUnarchivedSubjects();
//      } else {
//        subjects = commonController.findSubjects();
//      }
//      if (!subjects.isEmpty()) {
//        return Response.ok()
//            .entity(tranqualise(subjects))
//            .build();
//      } else {
//        return Response.status(Status.NOT_FOUND).build();
//      }
//    } else {
//      SearchResult<Subject> subjects = commonController.searchSubjects(100, 0, text);
//      return Response.ok()
//          .entity(tranqualise(subjects.getResults()))
//          .build();
//    }
//  }
//  
//  @Path("/subjects/{ID:[0-9]*}")
//  @GET
//  public Response findSubjectById(@PathParam("ID") Long id) {
//    Subject subject = commonController.findSubjectById(id);
//    if (subject != null) {
//      return Response.ok()
//          .entity(tranqualise(subject))
//          .build();
//    } else  {
//      return Response.status(Status.NOT_FOUND).build();
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
//  
//  @Path("/educationTypes/{ID:[0-9]*}")
//  @PUT
//  public Response updateEducationType(@PathParam("ID") Long id, EducationTypeEntity educationTypeEntity) {
//    EducationType educationType = commonController.findEducationTypeById(id);
//    if (educationType != null) {
//      String name = educationTypeEntity.getName();
//      String code = educationTypeEntity.getCode();
//      if(!StringUtils.isBlank(name) && !StringUtils.isBlank(code)) {
//        return Response.ok()
//            .entity(tranqualise(commonController.updateEducationType(educationType, name, code)))
//            .build();
//      }
//      if (!educationTypeEntity.getArchived()) {
//        return Response.ok()
//            .entity(tranqualise(commonController.unarchiveEducationType(educationType, getUser())))
//            .build();
//      } else {
//        return Response.status(500).build();
//      }
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//  
//  @Path("/subjects/{ID:[0-9]*}")
//  @PUT
//  public Response updateSubject(@PathParam("ID") Long id, SubjectEntity subjectEntity) {
//    Subject subject = commonController.findSubjectById(id);
//    if (subject != null) {
//      Long educationTypeId = subjectEntity.getEducationType_id();
//      String name = subjectEntity.getName();
//      String code = subjectEntity.getCode();
//      if(!StringUtils.isBlank(name) && !StringUtils.isBlank(code) && educationTypeId != null) {
//        EducationType educationType = commonController.findEducationTypeById(educationTypeId);
//        return Response.ok()
//            .entity(tranqualise(commonController.updateSubject(subject, code, name, educationType)))
//            .build();
//      }
//      if (!subjectEntity.getArchived()) {
//        return Response.ok()
//            .entity(tranqualise(commonController.unarchiveSubject(subject, getUser())))
//            .build();
//      } else {
//        return Response.status(500).build();
//      }
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//  
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
//  @Path("/educationTypes/{ID:[0-9]*}")
//  @DELETE
//  public Response archiveEducationType(@PathParam("ID") Long id) {
//    EducationType educationType = commonController.findEducationTypeById(id);
//    if (educationType != null) {
//      return Response.ok()
//          .entity(tranqualise(commonController.archiveEducationType(educationType, getUser())))
//          .build();
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//  
//  @Path("/subjects/{ID:[0-9]*}")
//  @DELETE
//  public Response archiveSubject(@PathParam("ID") Long id) {
//    Subject subject = commonController.findSubjectById(id);
//    if (subject != null) {
//      return Response.ok()
//          .entity(tranqualise(commonController.archiveSubject(subject, getUser())))
//          .build();
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//  
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
