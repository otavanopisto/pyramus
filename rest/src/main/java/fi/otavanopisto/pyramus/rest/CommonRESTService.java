package fi.otavanopisto.pyramus.rest;

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

import fi.otavanopisto.pyramus.domainmodel.base.ContactType;
import fi.otavanopisto.pyramus.domainmodel.base.ContactURLType;
import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;
import fi.otavanopisto.pyramus.domainmodel.base.EducationSubtype;
import fi.otavanopisto.pyramus.domainmodel.base.EducationType;
import fi.otavanopisto.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.grading.Grade;
import fi.otavanopisto.pyramus.domainmodel.grading.GradingScale;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit;
import fi.otavanopisto.pyramus.rest.controller.CommonController;
import fi.otavanopisto.pyramus.rest.controller.CourseController;
import fi.otavanopisto.pyramus.rest.controller.CurriculumController;
import fi.otavanopisto.pyramus.rest.controller.permissions.CommonPermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.CoursePermissions;
import fi.otavanopisto.pyramus.security.impl.SessionController;

@Path("/common")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateful
@RequestScoped
public class CommonRESTService extends AbstractRESTService {

  @Inject
  private CommonController commonController;

  @Inject
  private CourseController courseController;

  @Inject
  private CurriculumController curriculumController;
  
  @Inject
  private SessionController sessionController;
  
  @Inject
  private ObjectFactory objectFactory;
  
  @Path("/educationTypes")
  @POST
  @RESTPermit (CommonPermissions.CREATE_EDUCATIONTYPE)
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
  @RESTPermit (CommonPermissions.LIST_EDUCATIONTYPES)
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
  @RESTPermit (CommonPermissions.FIND_EDUCATIONTYPE)
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
  @RESTPermit (CommonPermissions.UPDATE_EDUCATIONTYPE)
  public Response updateEducationType(@PathParam("ID") Long id, fi.otavanopisto.pyramus.rest.model.EducationType entity) {
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
  @RESTPermit (CommonPermissions.DELETE_EDUCATIONTYPE)
  public Response deleteEducationType(@PathParam("ID") Long id, @DefaultValue ("false") @QueryParam ("permanent") Boolean permanent) {
    EducationType educationType = commonController.findEducationTypeById(id);
    if (educationType == null) {
      return Response.status(Status.NOT_FOUND).build();
    }    
    
    if (permanent) {
      commonController.deleteEducationType(educationType);
    } else {
      commonController.archiveEducationType(educationType, sessionController.getUser());
    }
    
    return Response.noContent().build();
  }
  
  @Path("/educationTypes/{ID}/subtypes")
  @POST
  @RESTPermit (CommonPermissions.CREATE_EDUCATIONSUBTYPE)
  public Response createEducationSubtype(@PathParam ("ID") Long educationTypeId, fi.otavanopisto.pyramus.rest.model.EducationSubtype entity) {
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
  @RESTPermit (CommonPermissions.LIST_EDUCATIONSUBTYPES)
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
  @RESTPermit (CommonPermissions.FIND_EDUCATIONSUBTYPE)
  public Response findEducationTypeById(@PathParam("EDUCATIONTYPEID") Long educationTypeId, @PathParam ("EDUCATIONSUBTYPEID") Long educationSubtypeId) {
    if (educationTypeId == null || educationSubtypeId == null) {
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
  @RESTPermit (CommonPermissions.UPDATE_EDUCATIONSUBTYPE)
  public Response updateEducationSubtype(@PathParam("EDUCATIONTYPEID") Long educationTypeId, @PathParam ("EDUCATIONSUBTYPEID") Long educationSubtypeId, fi.otavanopisto.pyramus.rest.model.EducationSubtype entity) {
    if (educationTypeId == null || educationSubtypeId == null) {
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
  @RESTPermit (CommonPermissions.DELETE_EDUCATIONSUBTYPE)
  public Response deleteEducationSubtype(@PathParam("EDUCATIONTYPEID") Long educationTypeId, @PathParam ("EDUCATIONSUBTYPEID") Long educationSubtypeId, @DefaultValue ("false") @QueryParam ("permanent") Boolean permanent) {
    if (educationTypeId == null || educationSubtypeId == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    EducationType educationType = commonController.findEducationTypeById(educationTypeId);
    if (educationType == null) {
      return Response.status(Status.NOT_FOUND).entity("Could not find educationType").build();
    }    
    
    if (educationType.getArchived()) {
      return Response.status(Status.NOT_FOUND).entity("Could not find educationType").build();
    }    
    
    EducationSubtype educationSubtype = commonController.findEducationSubtypeById(educationSubtypeId);
    if (educationSubtype == null) {
      return Response.status(Status.NOT_FOUND).entity("Could not find subtype").build();
    }    
    
    if (educationSubtype.getArchived() && permanent != true) {
      return Response.status(Status.NOT_FOUND).entity("Could not find subtype").build();
    }    
    
    if (!educationSubtype.getEducationType().getId().equals(educationTypeId)) {
      return Response.status(Status.NOT_FOUND).entity("Could not find subtype " + educationSubtype.getEducationType().getId() + " != " + educationTypeId).build();
    }  
    
    if (permanent) {
      commonController.deleteEducationSubtype(educationSubtype);
    } else {
      commonController.archiveEducationSubtype(educationSubtype, sessionController.getUser());
    }
    
    return Response.noContent().build();
  }

  @Path("/educationTypes/{ID:[0-9]*}/subjects")
  @GET
  @RESTPermit (CommonPermissions.LIST_SUBJECTSBYEDUCATIONTYPE)
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
  @RESTPermit (CommonPermissions.CREATE_SUBJECT)
  public Response createSubject(fi.otavanopisto.pyramus.rest.model.Subject entity) {
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
  
  @Path("/subjectByCode/{CODE}")
  @GET
  @RESTPermit (CommonPermissions.FIND_SUBJECT)
  public Response findSubjectByCode(@PathParam("CODE") String code) {
    Subject subject = commonController.findSubjectByCode(code);
    if (subject == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    return Response.ok().entity(objectFactory.createModel(subject)).build();
  }

  @Path("/subjects")
  @GET
  @RESTPermit (CommonPermissions.LIST_SUBJECTS)
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
  @RESTPermit (CommonPermissions.FIND_SUBJECT)
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
  @RESTPermit (CommonPermissions.UPDATE_SUBJECT)
  public Response updateSubject(@PathParam("ID") Long id, fi.otavanopisto.pyramus.rest.model.Subject entity) {
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
  @RESTPermit (CommonPermissions.ARCHIVE_SUBJECT)
  public Response archiveSubject(@PathParam("ID") Long id, @DefaultValue ("false") @QueryParam ("permanent") Boolean permanent) {
    Subject subject = commonController.findSubjectById(id);
    if (subject == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (permanent) {
      commonController.deleteSubject(subject);
    } else {
      commonController.archiveSubject(subject, sessionController.getUser());
    }

    return Response.noContent().build();
  }
  
  @Path("/subjects/{ID:[0-9]*}/courses")
  @GET
  @RESTPermit (CoursePermissions.LIST_COURSESBYSUBJECT)
  public Response listCoursesBySubject(@PathParam("ID") Long id) {
    Subject subject = commonController.findSubjectById(id);
    if (subject == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (subject.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    List<Course> courses = courseController.listCoursesBySubject(subject);
    
    return Response.ok()
        .entity(objectFactory.createModel(courses))
        .build();
  }

  @Path("/gradingScales")
  @POST
  @RESTPermit (CommonPermissions.CREATE_GRADINGSCALE)
  public Response createGradingScale(fi.otavanopisto.pyramus.rest.model.GradingScale entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    String name = entity.getName();
    String description = entity.getDescription();
    
    if (StringUtils.isBlank(name) || StringUtils.isBlank(description)) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    return Response.ok(objectFactory.createModel(commonController.createGradingScale(name, description))).build();
  }
  
  @Path("/gradingScales")
  @GET
  @RESTPermit (CommonPermissions.LIST_GRADINGSCALES)
  public Response findGradingScales(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<GradingScale> gradingScales;
    if (filterArchived) {
      gradingScales = commonController.listUnarchivedGradingScales();
    } else {
      gradingScales = commonController.listGradingScales();
    }
    
    if (gradingScales.isEmpty()) {
      return Response.noContent().build();
    }
    
    return Response.ok(objectFactory.createModel(gradingScales)).build();
  }
  
  @Path("/gradingScales/{ID:[0-9]*}")
  @GET
  @RESTPermit (CommonPermissions.FIND_GRADINGSCALE)
  public Response findGradingScalesById(@PathParam("ID") Long id) {
    GradingScale gradingScale = commonController.findGradingScaleById(id);
    if (gradingScale == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (gradingScale.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    return Response.ok(objectFactory.createModel(gradingScale)).build();
  }
  
  @Path("/gradingScales/{ID:[0-9]*}")
  @PUT
  @RESTPermit (CommonPermissions.UPDATE_GRADINGSCALE)
  public Response updateGradingScale(@PathParam("ID") Long id, GradingScale entity) {
    fi.otavanopisto.pyramus.domainmodel.grading.GradingScale gradingScale = commonController.findGradingScaleById(id);
    if (gradingScale == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (gradingScale.getArchived()) {
      Response.status(Status.NOT_FOUND).build();
    }
    
    String name = entity.getName();
    String description = entity.getDescription();
    
    if (StringUtils.isBlank(name) || StringUtils.isBlank(description)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    return Response.ok(objectFactory.createModel(commonController.updateGradingScale(gradingScale, name, description)))
        .build();
  }

  @Path("/gradingScales/{ID:[0-9]*}")
  @DELETE
  @RESTPermit (CommonPermissions.DELETE_GRADINGSCALE)
  public Response deleteGradingScale(@PathParam("ID") Long id, @DefaultValue ("false") @QueryParam ("permanent") Boolean permanent) {
    GradingScale gradingScale = commonController.findGradingScaleById(id);
    if (gradingScale == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (permanent) {
      commonController.deleteGradingScale(gradingScale);
    } else {
      commonController.archiveGradingScale(gradingScale, sessionController.getUser());
    }
    
    return Response.noContent().build();
  }
  
  @Path("/gradingScales/{SCALEID:[0-9]*}/grades")
  @POST
  @RESTPermit (CommonPermissions.CREATE_GRADE)
  public Response createGrade(@PathParam ("SCALEID") Long gradingScaleId, fi.otavanopisto.pyramus.rest.model.Grade entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    GradingScale gradingScale = commonController.findGradingScaleById(gradingScaleId);
    if (gradingScale == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (gradingScale.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    String name = entity.getName();
    String description = entity.getDescription();
    Boolean passingGrade = entity.getPassingGrade();
    Double gpa = entity.getGpa();
    String qualification = entity.getQualification();
    
    if (StringUtils.isBlank(name) || StringUtils.isBlank(description) || passingGrade == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    return Response.ok(objectFactory.createModel(commonController.createGrade(gradingScale, name, description, passingGrade, gpa, qualification))).build();
  }
  
  @Path("/gradingScales/{SCALEID:[0-9]*}/grades")
  @GET
  @RESTPermit (CommonPermissions.LIST_GRADES)
  public Response listGrades(@PathParam ("SCALEID") Long gradingScaleId) {
    GradingScale gradingScale = commonController.findGradingScaleById(gradingScaleId);
    if (gradingScale == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (gradingScale.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    List<Grade> grades = gradingScale.getGrades();
    
    if (grades.isEmpty()) {
      return Response.noContent().build();
    }
    
    return Response.ok(objectFactory.createModel(grades)).build();
  }
  
  @Path("/gradingScales/{SCALEID:[0-9]*}/grades/{ID:[0-9]*}")
  @GET
  @RESTPermit (CommonPermissions.FIND_GRADE)
  public Response findGrade(@PathParam ("SCALEID") Long gradingScaleId, @PathParam("ID") Long id) {
    GradingScale gradingScale = commonController.findGradingScaleById(gradingScaleId);
    if (gradingScale == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (gradingScale.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    Grade grade = commonController.findGradeByIdId(id);
    if (grade == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (grade.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (!grade.getGradingScale().getId().equals(gradingScale.getId())) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    return Response.ok(objectFactory.createModel(grade)).build();
  }
  
  @Path("/gradingScales/{SCALEID:[0-9]*}/grades/{ID:[0-9]*}")
  @PUT
  @RESTPermit (CommonPermissions.UPDATE_GRADE)
  public Response updateGrade(@PathParam ("SCALEID") Long gradingScaleId, @PathParam("ID") Long id, fi.otavanopisto.pyramus.rest.model.Grade entity) {
    GradingScale gradingScale = commonController.findGradingScaleById(gradingScaleId);
    if (gradingScale == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (gradingScale.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    Grade grade = commonController.findGradeByIdId(id);
    if (grade == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (grade.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (!grade.getGradingScale().getId().equals(gradingScale.getId())) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    String name = entity.getName();
    String description = entity.getDescription();
    Boolean passingGrade = entity.getPassingGrade();
    Double gpa = entity.getGpa();
    String qualification = entity.getQualification();
    
    if (StringUtils.isBlank(name) || StringUtils.isBlank(description) || passingGrade == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    if (!entity.getGradingScaleId().equals(gradingScaleId)) {
      return Response.status(Status.BAD_REQUEST).entity("Cannot change grading scale of grade by updating").build();
    }
    
    return Response
        .ok(objectFactory.createModel(commonController.updateGrade(grade, name, description, passingGrade, gpa, qualification)))
        .build();
  }

  @Path("/gradingScales/{SCALEID:[0-9]*}/grades/{ID:[0-9]*}")
  @DELETE
  @RESTPermit (CommonPermissions.DELETE_GRADE)
  public Response deleteGrade(@PathParam ("SCALEID") Long gradingScaleId, @PathParam("ID") Long id, @DefaultValue ("false") @QueryParam ("permanent") Boolean permanent) {
    GradingScale gradingScale = commonController.findGradingScaleById(gradingScaleId);
    if (gradingScale == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (gradingScale.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    Grade grade = commonController.findGradeByIdId(id);
    if (grade == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (permanent) {
      commonController.deleteGrade(grade);
    } else {
      commonController.archiveGrade(grade, sessionController.getUser());
    }
    
    return Response.noContent().build();
  }
  
  @Path("/educationalTimeUnits") 
  @POST
  @RESTPermit (CommonPermissions.CREATE_EDUCATIONALTIMEUNIT)
  public Response createEducationalTimeUnit(fi.otavanopisto.pyramus.rest.model.EducationalTimeUnit entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    String name = entity.getName();
    String symbol = entity.getSymbol();
    Double baseUnits = entity.getBaseUnits();
    
    if (StringUtils.isBlank(name)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    if (baseUnits == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    if (StringUtils.isEmpty(symbol)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    return Response.ok()
      .entity(objectFactory.createModel(commonController.createEducationalTimeUnit(baseUnits, name, symbol)))
      .build();
  }
    
  @Path("/educationalTimeUnits")
  @GET
  @RESTPermit (CommonPermissions.LIST_EDUCATIONALTIMEUNITS)
  public Response listEducationalTimeUnits(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    
    List<EducationalTimeUnit> educationalTimeUnits;
    if (filterArchived) {
      educationalTimeUnits = commonController.listUnarchivedEducationalTimeUnits();
    } else {
      educationalTimeUnits = commonController.listEducationalTimeUnits();
    }
    
    if (educationalTimeUnits.isEmpty()) {
      return Response.status(Status.NO_CONTENT).build();
    }
    
    return Response.ok()
        .entity(objectFactory.createModel(educationalTimeUnits))
        .build();
  }
    
  @Path("/educationalTimeUnits/{ID:[0-9]*}")
  @GET
  @RESTPermit (CommonPermissions.FIND_EDUCATIONALTIMEUNIT)
  public Response findEducationalTimeUnitsById(@PathParam("ID") Long id) {
    EducationalTimeUnit educationalTimeUnit = commonController.findEducationalTimeUnitById(id);
    
    if (educationalTimeUnit == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (educationalTimeUnit.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    return Response.ok()
        .entity(objectFactory.createModel(educationalTimeUnit))
        .build();
  }

  @Path("/educationalTimeUnits/{ID:[0-9]*}")
  @PUT
  @RESTPermit (CommonPermissions.UPDATE_EDUCATIONALTIMEUNIT)
  public Response updateEducationalTimeUnit(@PathParam("ID") Long id, fi.otavanopisto.pyramus.rest.model.EducationalTimeUnit entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    EducationalTimeUnit educationalTimeUnit = commonController.findEducationalTimeUnitById(id);
    
    if (educationalTimeUnit == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (educationalTimeUnit.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    String name = entity.getName();
    String symbol = entity.getSymbol();
    Double baseUnits = entity.getBaseUnits();
    
    if (StringUtils.isBlank(name)) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    if (baseUnits == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    if (StringUtils.isEmpty(symbol)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    return Response.ok()
      .entity(objectFactory.createModel(commonController.updateEducationalTimeUnit(educationalTimeUnit, baseUnits, name, symbol)))
      .build();
  }
  
  @Path("/educationalTimeUnits/{ID:[0-9]*}")
  @DELETE
  @RESTPermit (CommonPermissions.ARCHIVE_EDUCATIONALTIMEUNIT)
  public Response archiveEducationalTimeUnit(@PathParam("ID") Long id, @DefaultValue ("false") @QueryParam ("permanent") Boolean permanent) {
    EducationalTimeUnit educationalTimeUnit = commonController.findEducationalTimeUnitById(id);
    
    if (educationalTimeUnit == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (permanent) {
      commonController.deleteEducationalTimeUnit(educationalTimeUnit);
    } else {
      commonController.archiveEducationalTimeUnit(educationalTimeUnit, sessionController.getUser());
    }
    
    return Response.status(Status.NO_CONTENT).build();
  }

  @Path("/contactTypes")
  @POST
  @RESTPermit (CommonPermissions.CREATE_CONTACTTYPE)
  public Response createContactType(fi.otavanopisto.pyramus.rest.model.ContactType entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    String name = entity.getName();
    Boolean nonUnique = entity.getNonUnique() != null ? entity.getNonUnique() : false;
    
    if (StringUtils.isBlank(name)) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    return Response.ok().entity(objectFactory.createModel(commonController.createContactType(name, nonUnique))).build();
  }
  
  @Path("/contactTypes")
  @GET
  @RESTPermit (CommonPermissions.LIST_CONTACTTYPES)
  public Response listContactTypes(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<ContactType> contactTypes;
    
    if (filterArchived) {
      contactTypes = commonController.listUnarchivedContactTypes();
    } else {
      contactTypes = commonController.listContactTypes();
    }
    
    if (contactTypes.isEmpty()) {
      return Response.noContent().build();
    }
    
    return Response.ok()
      .entity(objectFactory.createModel(contactTypes))
      .build();
  }
  
  @Path("/contactTypes/{ID:[0-9]*}")
  @GET
  @RESTPermit (CommonPermissions.FIND_CONTACTTYPE)
  public Response findContactType(@PathParam("ID") Long id) {
    ContactType contactType = commonController.findContactTypeById(id);
    if (contactType == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (contactType.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    return Response.ok()
        .entity(objectFactory.createModel(contactType))
        .build();
  }

  @Path("/contactTypes/{ID:[0-9]*}")
  @PUT
  @RESTPermit (CommonPermissions.UPDATE_CONTACTTYPE)
  public Response updateContactType(@PathParam("ID") Long id, fi.otavanopisto.pyramus.rest.model.ContactType entity) {
    ContactType contactType = commonController.findContactTypeById(id);
    if (contactType == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (contactType.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    String name = entity.getName();
    Boolean nonUnique = entity.getNonUnique() != null ? entity.getNonUnique() : false;
    
    if (StringUtils.isBlank(name)) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    return Response.ok(objectFactory.createModel(commonController.updateContactType(contactType, name, nonUnique))).build();
  }

  @Path("/contactTypes/{ID:[0-9]*}")
  @DELETE
  @RESTPermit (CommonPermissions.DELETE_CONTACTTYPE)
  public Response deleteContactType(@PathParam("ID") Long id, @DefaultValue ("false") @QueryParam ("permanent") Boolean permanent) {
    ContactType contactType = commonController.findContactTypeById(id);
    if (contactType == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (permanent) {
      commonController.deleteContactType(contactType);
    } else {
      commonController.archiveContactType(contactType, sessionController.getUser());
    }

    return Response.noContent().build();
  }

  @Path("/contactURLTypes")
  @POST
  @RESTPermit (CommonPermissions.CREATE_CONTACTURLTYPE)
  public Response createContactURLType(fi.otavanopisto.pyramus.rest.model.ContactURLType entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    String name = entity.getName();
    
    if (StringUtils.isBlank(name)) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    return Response.ok().entity(objectFactory.createModel(commonController.createContactURLType(name))).build();
  }
  
  @Path("/contactURLTypes")
  @GET
  @RESTPermit (CommonPermissions.LIST_CONTACTURLTYPES)
  public Response listContactURLTypes(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<ContactURLType> contactURLTypes;
    
    if (filterArchived) {
      contactURLTypes = commonController.listUnarchivedContactURLTypes();
    } else {
      contactURLTypes = commonController.listContactURLTypes();
    }
    
    if (contactURLTypes.isEmpty()) {
      return Response.noContent().build();
    }
    
    return Response.ok()
      .entity(objectFactory.createModel(contactURLTypes))
      .build();
  }
  
  @Path("/contactURLTypes/{ID:[0-9]*}")
  @GET
  @RESTPermit (CommonPermissions.FIND_CONTACTURLTYPE)
  public Response findContactURLType(@PathParam("ID") Long id) {
    ContactURLType contactURLType = commonController.findContactURLTypeById(id);
    if (contactURLType == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (contactURLType.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    return Response.ok()
        .entity(objectFactory.createModel(contactURLType))
        .build();
  }

  @Path("/contactURLTypes/{ID:[0-9]*}")
  @PUT
  @RESTPermit (CommonPermissions.UPDATE_CONTACTURLTYPE)
  public Response updateContactURLType(@PathParam("ID") Long id, fi.otavanopisto.pyramus.rest.model.ContactURLType entity) {
    ContactURLType contactURLType = commonController.findContactURLTypeById(id);
    if (contactURLType == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (contactURLType.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    String name = entity.getName();
    
    if (StringUtils.isBlank(name)) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    return Response.ok(objectFactory.createModel(commonController.updateContactURLType(contactURLType, name))).build();
  }

  @Path("/contactURLTypes/{ID:[0-9]*}")
  @DELETE
  @RESTPermit (CommonPermissions.DELETE_CONTACTURLTYPE)
  public Response deleteContactURLType(@PathParam("ID") Long id, @DefaultValue ("false") @QueryParam ("permanent") Boolean permanent) {
    ContactURLType contactURLType = commonController.findContactURLTypeById(id);
    if (contactURLType == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (permanent) {
      commonController.deleteContactURLType(contactURLType);
    } else {
      commonController.archiveContactURLType(contactURLType, sessionController.getUser());
    }

    return Response.noContent().build();
  }
  
  @Path("/curriculums")
  @POST
  @RESTPermit (CommonPermissions.CREATE_CURRICULUM)
  public Response createSubject(fi.otavanopisto.pyramus.rest.model.Curriculum entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    String name = entity.getName();
    
    if (StringUtils.isBlank(name)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    return Response.ok().entity(objectFactory.createModel(curriculumController.createCurriculum(name))).build();
  }
  
  @Path("/curriculums")
  @GET
  @RESTPermit (CommonPermissions.LIST_CURRICULUMS)
  public Response listCurriculums(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<Curriculum> curriculums;
    
    if (filterArchived) {
      curriculums = curriculumController.listUnarchivedCurriculums();
    } else {
      curriculums = curriculumController.listCurriculums();
    }
    
    if (curriculums.isEmpty()) {
      return Response.noContent().build();
    }
    
    return Response.ok()
      .entity(objectFactory.createModel(curriculums))
      .build();
  }
  
  @Path("/curriculums/{ID:[0-9]*}")
  @GET
  @RESTPermit (CommonPermissions.FIND_CURRICULUM)
  public Response findCurriculum(@PathParam("ID") Long id) {
    Curriculum curriculum = curriculumController.findCurriculumById(id);
    if (curriculum == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (curriculum.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    return Response.ok()
        .entity(objectFactory.createModel(curriculum))
        .build();
  }

  @Path("/curriculums/{ID:[0-9]*}")
  @PUT
  @RESTPermit (CommonPermissions.UPDATE_CURRICULUM)
  public Response updateCurriculum(@PathParam("ID") Long id, fi.otavanopisto.pyramus.rest.model.Curriculum entity) {
    Curriculum curriculum = curriculumController.findCurriculumById(id);
    if (curriculum == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (curriculum.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    String name = entity.getName();
    
    if (StringUtils.isBlank(name)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    return Response.ok(objectFactory.createModel(curriculumController.updateCurriculum(curriculum, name))).build();
  }

  @Path("/curriculums/{ID:[0-9]*}")
  @DELETE
  @RESTPermit (CommonPermissions.ARCHIVE_CURRICULUM)
  public Response archiveCurriculum(@PathParam("ID") Long id, @DefaultValue ("false") @QueryParam ("permanent") Boolean permanent) {
    Curriculum curriculum = curriculumController.findCurriculumById(id);
    if (curriculum == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (permanent) {
      curriculumController.deleteCurriculum(curriculum);
    } else {
      curriculumController.archiveCurriculum(curriculum, sessionController.getUser());
    }

    return Response.noContent().build();
  }
  
}
