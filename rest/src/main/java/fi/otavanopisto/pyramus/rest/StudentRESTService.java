package fi.otavanopisto.pyramus.rest;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.domainmodel.base.Address;
import fi.otavanopisto.pyramus.domainmodel.base.ContactType;
import fi.otavanopisto.pyramus.domainmodel.base.ContactURL;
import fi.otavanopisto.pyramus.domainmodel.base.ContactURLType;
import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;
import fi.otavanopisto.pyramus.domainmodel.base.EducationType;
import fi.otavanopisto.pyramus.domainmodel.base.Email;
import fi.otavanopisto.pyramus.domainmodel.base.Language;
import fi.otavanopisto.pyramus.domainmodel.base.Municipality;
import fi.otavanopisto.pyramus.domainmodel.base.Nationality;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.base.PhoneNumber;
import fi.otavanopisto.pyramus.domainmodel.base.School;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgrammeCategory;
import fi.otavanopisto.pyramus.domainmodel.base.VariableType;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessmentRequest;
import fi.otavanopisto.pyramus.domainmodel.grading.Grade;
import fi.otavanopisto.pyramus.domainmodel.grading.TransferCredit;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentActivityType;
import fi.otavanopisto.pyramus.domainmodel.students.StudentContactLogEntry;
import fi.otavanopisto.pyramus.domainmodel.students.StudentContactLogEntryType;
import fi.otavanopisto.pyramus.domainmodel.students.StudentEducationalLevel;
import fi.otavanopisto.pyramus.domainmodel.students.StudentExaminationType;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroup;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroupStudent;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroupUser;
import fi.otavanopisto.pyramus.domainmodel.students.StudentStudyEndReason;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.domainmodel.users.UserVariable;
import fi.otavanopisto.pyramus.domainmodel.users.UserVariableKey;
import fi.otavanopisto.pyramus.framework.UserEmailInUseException;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit.Handling;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit.Style;
import fi.otavanopisto.pyramus.rest.controller.AssessmentController;
import fi.otavanopisto.pyramus.rest.controller.CommonController;
import fi.otavanopisto.pyramus.rest.controller.CourseController;
import fi.otavanopisto.pyramus.rest.controller.CurriculumController;
import fi.otavanopisto.pyramus.rest.controller.LanguageController;
import fi.otavanopisto.pyramus.rest.controller.MunicipalityController;
import fi.otavanopisto.pyramus.rest.controller.NationalityController;
import fi.otavanopisto.pyramus.rest.controller.PersonController;
import fi.otavanopisto.pyramus.rest.controller.SchoolController;
import fi.otavanopisto.pyramus.rest.controller.StudentActivityTypeController;
import fi.otavanopisto.pyramus.rest.controller.StudentContactLogEntryController;
import fi.otavanopisto.pyramus.rest.controller.StudentController;
import fi.otavanopisto.pyramus.rest.controller.StudentEducationalLevelController;
import fi.otavanopisto.pyramus.rest.controller.StudentExaminationTypeController;
import fi.otavanopisto.pyramus.rest.controller.StudentGroupController;
import fi.otavanopisto.pyramus.rest.controller.StudentStudyEndReasonController;
import fi.otavanopisto.pyramus.rest.controller.StudyProgrammeCategoryController;
import fi.otavanopisto.pyramus.rest.controller.StudyProgrammeController;
import fi.otavanopisto.pyramus.rest.controller.UserController;
import fi.otavanopisto.pyramus.rest.controller.permissions.CourseAssessmentPermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.LanguagePermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.MunicipalityPermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.NationalityPermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.PersonPermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.StudentActivityTypePermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.StudentContactLogEntryPermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.StudentEducationalLevelPermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.StudentExaminationTypePermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.StudentGroupPermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.StudentPermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.StudentStudyEndReasonPermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.StudyProgrammeCategoryPermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.StudyProgrammePermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.UserPermissions;
import fi.otavanopisto.pyramus.rest.security.RESTSecurity;
import fi.otavanopisto.pyramus.rest.util.ISO8601Timestamp;
import fi.otavanopisto.pyramus.security.impl.SessionController;

@Path("/students")
@Produces("application/json")
@Consumes("application/json")
@Stateful
@RequestScoped
public class StudentRESTService extends AbstractRESTService {

  @Inject
  private RESTSecurity restSecurity;

  @Inject
  private UserController userController;

  @Inject
  private CommonController commonController;

  @Inject
  private StudentController studentController;

  @Inject
  private LanguageController languageController;

  @Inject
  private MunicipalityController municipalityController;

  @Inject
  private NationalityController nationalityController;

  @Inject
  private StudentActivityTypeController studentActivityTypeController;

  @Inject
  private StudentEducationalLevelController studentEducationalLevelController;

  @Inject
  private StudentExaminationTypeController studentExaminationTypeController;

  @Inject
  private StudyProgrammeCategoryController studyProgrammeCategoryController;

  @Inject
  private StudyProgrammeController studyProgrammeController;

  @Inject
  private StudentGroupController studentGroupController;

  @Inject
  private PersonController personController;

  @Inject
  private StudentStudyEndReasonController studentStudyEndReasonController;

  @Inject
  private StudentContactLogEntryController studentContactLogEntryController;

  @Inject
  private SchoolController schoolController;

  @Inject
  private CourseController courseController;
  
  @Inject
  private CurriculumController curriculumController;

  @Inject
  private SessionController sessionController;

  @Inject
  private ObjectFactory objectFactory;
  
  @Inject
  private AssessmentController assessmentController;

  @Path("/languages")
  @POST
  @RESTPermit(LanguagePermissions.CREATE_LANGUAGE)
  public Response createLanguage(fi.otavanopisto.pyramus.rest.model.Language entity) {
    String name = entity.getName();
    String code = entity.getCode();

    if (StringUtils.isBlank(name) || StringUtils.isBlank(code)) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    return Response.ok(objectFactory.createModel(languageController.createLanguage(name, code))).build();
  }

  @Path("/languages")
  @GET
  @RESTPermit(LanguagePermissions.LIST_LANGUAGES)
  public Response listLanguages(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<Language> languages;
    if (filterArchived) {
      languages = languageController.listUnarchivedLanguages();
    } else {
      languages = languageController.listLanguages();
    }

    if (languages.isEmpty()) {
      return Response.noContent().build();
    }

    return Response.ok(objectFactory.createModel(languages)).build();
  }

  @Path("/languages/{ID:[0-9]*}")
  @GET
  @RESTPermit(LanguagePermissions.FIND_LANGUAGE)
  public Response findLanguageById(@PathParam("ID") Long id) {
    Language language = languageController.findLanguageById(id);
    if (language == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (language.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    return Response.ok(objectFactory.createModel(language)).build();
  }

  @Path("/languages/{ID:[0-9]*}")
  @PUT
  @RESTPermit(LanguagePermissions.UPDATE_LANGUAGE)
  public Response updateLanguage(@PathParam("ID") Long id, fi.otavanopisto.pyramus.rest.model.Language entity) {
    Language language = languageController.findLanguageById(id);
    if (language == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (language.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    String name = entity.getName();
    String code = entity.getCode();

    if (StringUtils.isBlank(name) || StringUtils.isBlank(code)) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    return Response.ok().entity(objectFactory.createModel(languageController.updateLanguage(language, name, code))).build();
  }

  @Path("/languages/{ID:[0-9]*}")
  @DELETE
  @RESTPermit(LanguagePermissions.DELETE_LANGUAGE)
  public Response deleteLanguage(@PathParam("ID") Long id, @DefaultValue("false") @QueryParam("permanent") Boolean permanent) {
    Language language = languageController.findLanguageById(id);
    if (language == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (permanent) {
      languageController.deleteLanguage(language);
    } else {
      languageController.archiveLanguage(language, sessionController.getUser());
    }

    return Response.noContent().build();
  }

  @Path("/municipalities")
  @POST
  @RESTPermit(MunicipalityPermissions.CREATE_MUNICIPALITY)
  public Response createMunicipality(fi.otavanopisto.pyramus.rest.model.Municipality entity) {
    String name = entity.getName();
    String code = entity.getCode();

    if (StringUtils.isBlank(name) || StringUtils.isBlank(code)) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    return Response.ok(objectFactory.createModel(municipalityController.createMunicipality(name, code))).build();
  }

  @Path("/municipalities")
  @GET
  @RESTPermit(MunicipalityPermissions.LIST_MUNICIPALITIES)
  public Response listMunicipalities(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<Municipality> municipalities;
    if (filterArchived) {
      municipalities = municipalityController.listUnarchivedMunicipalities();
    } else {
      municipalities = municipalityController.listMunicipalities();
    }

    if (municipalities.isEmpty()) {
      return Response.noContent().build();
    }

    return Response.ok(objectFactory.createModel(municipalities)).build();
  }

  @Path("/municipalities/{ID:[0-9]*}")
  @GET
  @RESTPermit(MunicipalityPermissions.FIND_MUNICIPALITY)
  public Response findMunicipalityById(@PathParam("ID") Long id) {
    Municipality municipality = municipalityController.findMunicipalityById(id);
    if (municipality == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (municipality.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    return Response.ok(objectFactory.createModel(municipality)).build();
  }

  @Path("/municipalities/{ID:[0-9]*}")
  @PUT
  @RESTPermit(MunicipalityPermissions.UPDATE_MUNICIPALITY)
  public Response updateMunicipality(@PathParam("ID") Long id, fi.otavanopisto.pyramus.rest.model.Municipality entity) {
    Municipality municipality = municipalityController.findMunicipalityById(id);
    if (municipality == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (municipality.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    String name = entity.getName();
    String code = entity.getCode();

    if (StringUtils.isBlank(name) || StringUtils.isBlank(code)) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    return Response.ok().entity(objectFactory.createModel(municipalityController.updateMunicipality(municipality, name, code))).build();
  }

  @Path("/municipalities/{ID:[0-9]*}")
  @DELETE
  @RESTPermit(MunicipalityPermissions.DELETE_MUNICIPALITY)
  public Response deleteMunicipality(@PathParam("ID") Long id, @DefaultValue("false") @QueryParam("permanent") Boolean permanent) {
    Municipality municipality = municipalityController.findMunicipalityById(id);
    if (municipality == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (permanent) {
      municipalityController.deleteMunicipality(municipality);
    } else {
      municipalityController.archiveMunicipality(municipality, sessionController.getUser());
    }

    return Response.noContent().build();
  }

  @Path("/nationalities")
  @POST
  @RESTPermit(NationalityPermissions.CREATE_NATIONALITY)
  public Response createNationality(fi.otavanopisto.pyramus.rest.model.Nationality entity) {
    String name = entity.getName();
    String code = entity.getCode();

    if (StringUtils.isBlank(name) || StringUtils.isBlank(code)) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    return Response.ok(objectFactory.createModel(nationalityController.createNationality(name, code))).build();
  }

  @Path("/nationalities")
  @GET
  @RESTPermit(NationalityPermissions.LIST_NATIONALITIES)
  public Response listNationalities(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<Nationality> nationalities;
    if (filterArchived) {
      nationalities = nationalityController.listUnarchivedNationalities();
    } else {
      nationalities = nationalityController.listNationalities();
    }

    if (nationalities.isEmpty()) {
      return Response.noContent().build();
    }

    return Response.ok(objectFactory.createModel(nationalities)).build();
  }

  @Path("/nationalities/{ID:[0-9]*}")
  @GET
  @RESTPermit(NationalityPermissions.FIND_NATIONALITY)
  public Response findNationalityById(@PathParam("ID") Long id) {
    Nationality nationality = nationalityController.findNationalityById(id);
    if (nationality == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (nationality.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    return Response.ok(objectFactory.createModel(nationality)).build();
  }

  @Path("/nationalities/{ID:[0-9]*}")
  @PUT
  @RESTPermit(NationalityPermissions.UPDATE_NATIONALITY)
  public Response updateNationality(@PathParam("ID") Long id, fi.otavanopisto.pyramus.rest.model.Nationality entity) {
    Nationality nationality = nationalityController.findNationalityById(id);
    if (nationality == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (nationality.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    String name = entity.getName();
    String code = entity.getCode();

    if (StringUtils.isBlank(name) || StringUtils.isBlank(code)) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    return Response.ok().entity(objectFactory.createModel(nationalityController.updateNationality(nationality, name, code))).build();
  }

  @Path("/nationalities/{ID:[0-9]*}")
  @DELETE
  @RESTPermit(NationalityPermissions.DELETE_NATIONALITY)
  public Response deleteNationality(@PathParam("ID") Long id, @DefaultValue("false") @QueryParam("permanent") Boolean permanent) {
    Nationality nationality = nationalityController.findNationalityById(id);
    if (nationality == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (permanent) {
      nationalityController.deleteNationality(nationality);
    } else {
      nationalityController.archiveNationality(nationality, sessionController.getUser());
    }

    return Response.noContent().build();
  }

  @Path("/activityTypes")
  @POST
  @RESTPermit(StudentActivityTypePermissions.CREATE_STUDENTACTIVITYTYPE)
  public Response createStudentActivityType(fi.otavanopisto.pyramus.rest.model.StudentActivityType entity) {
    String name = entity.getName();

    if (StringUtils.isBlank(name)) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    return Response.ok(objectFactory.createModel(studentActivityTypeController.createStudentActivityType(name))).build();
  }

  @Path("/activityTypes")
  @GET
  @RESTPermit(StudentActivityTypePermissions.LIST_STUDENTACTIVITYTYPES)
  public Response listStudentActivityTypes(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<StudentActivityType> studentActivityTypes;
    if (filterArchived) {
      studentActivityTypes = studentActivityTypeController.listUnarchivedStudentActivityTypes();
    } else {
      studentActivityTypes = studentActivityTypeController.listStudentActivityTypes();
    }

    if (studentActivityTypes.isEmpty()) {
      return Response.noContent().build();
    }

    return Response.ok(objectFactory.createModel(studentActivityTypes)).build();
  }

  @Path("/activityTypes/{ID:[0-9]*}")
  @GET
  @RESTPermit(StudentActivityTypePermissions.FIND_STUDENTACTIVITYTYPE)
  public Response findStudentActivityTypeById(@PathParam("ID") Long id) {
    StudentActivityType studentActivityType = studentActivityTypeController.findStudentActivityTypeById(id);
    if (studentActivityType == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (studentActivityType.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    return Response.ok(objectFactory.createModel(studentActivityType)).build();
  }

  @Path("/activityTypes/{ID:[0-9]*}")
  @PUT
  @RESTPermit(StudentActivityTypePermissions.UPDATE_STUDENTACTIVITYTYPE)
  public Response updateStudentActivityType(@PathParam("ID") Long id, fi.otavanopisto.pyramus.rest.model.StudentActivityType entity) {
    StudentActivityType studentActivityType = studentActivityTypeController.findStudentActivityTypeById(id);
    if (studentActivityType == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (studentActivityType.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    String name = entity.getName();

    if (StringUtils.isBlank(name)) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    return Response.ok().entity(objectFactory.createModel(studentActivityTypeController.updateStudentActivityType(studentActivityType, name))).build();
  }

  @Path("/activityTypes/{ID:[0-9]*}")
  @DELETE
  @RESTPermit(StudentActivityTypePermissions.DELETE_STUDENTACTIVITYTYPE)
  public Response deleteStudentActivityType(@PathParam("ID") Long id, @DefaultValue("false") @QueryParam("permanent") Boolean permanent) {
    StudentActivityType studentActivityType = studentActivityTypeController.findStudentActivityTypeById(id);
    if (studentActivityType == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (permanent) {
      studentActivityTypeController.deleteStudentActivityType(studentActivityType);
    } else {
      studentActivityTypeController.archiveStudentActivityType(studentActivityType, sessionController.getUser());
    }

    return Response.noContent().build();
  }

  @Path("/educationalLevels")
  @POST
  @RESTPermit(StudentEducationalLevelPermissions.CREATE_STUDENTEDUCATIONALLEVEL)
  public Response createStudentEducationalLevel(fi.otavanopisto.pyramus.rest.model.StudentEducationalLevel entity) {
    String name = entity.getName();

    if (StringUtils.isBlank(name)) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    return Response.ok(objectFactory.createModel(studentEducationalLevelController.createStudentEducationalLevel(name))).build();
  }

  @Path("/educationalLevels")
  @GET
  @RESTPermit(StudentEducationalLevelPermissions.LIST_STUDENTEDUCATIONALLEVELS)
  public Response listStudentEducationalLevels(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<StudentEducationalLevel> studentEducationalLevels;
    if (filterArchived) {
      studentEducationalLevels = studentEducationalLevelController.listUnarchivedStudentEducationalLevels();
    } else {
      studentEducationalLevels = studentEducationalLevelController.listStudentEducationalLevels();
    }

    if (studentEducationalLevels.isEmpty()) {
      return Response.noContent().build();
    }

    return Response.ok(objectFactory.createModel(studentEducationalLevels)).build();
  }

  @Path("/educationalLevels/{ID:[0-9]*}")
  @GET
  @RESTPermit(StudentEducationalLevelPermissions.FIND_STUDENTEDUCATIONALLEVEL)
  public Response findStudentEducationalLevelById(@PathParam("ID") Long id) {
    StudentEducationalLevel studentEducationalLevel = studentEducationalLevelController.findStudentEducationalLevelById(id);
    if (studentEducationalLevel == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (studentEducationalLevel.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    return Response.ok(objectFactory.createModel(studentEducationalLevel)).build();
  }

  @Path("/educationalLevels/{ID:[0-9]*}")
  @PUT
  @RESTPermit(StudentEducationalLevelPermissions.UPDATE_STUDENTEDUCATIONALLEVEL)
  public Response updateStudentEducationalLevel(@PathParam("ID") Long id, fi.otavanopisto.pyramus.rest.model.StudentEducationalLevel entity) {
    StudentEducationalLevel studentEducationalLevel = studentEducationalLevelController.findStudentEducationalLevelById(id);
    if (studentEducationalLevel == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (studentEducationalLevel.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    String name = entity.getName();

    if (StringUtils.isBlank(name)) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    return Response.ok().entity(objectFactory.createModel(studentEducationalLevelController.updateStudentEducationalLevel(studentEducationalLevel, name)))
        .build();
  }

  @Path("/educationalLevels/{ID:[0-9]*}")
  @DELETE
  @RESTPermit(StudentEducationalLevelPermissions.DELETE_STUDENTEDUCATIONALLEVEL)
  public Response deleteStudentEducationalLevel(@PathParam("ID") Long id, @DefaultValue("false") @QueryParam("permanent") Boolean permanent) {
    StudentEducationalLevel studentEducationalLevel = studentEducationalLevelController.findStudentEducationalLevelById(id);
    if (studentEducationalLevel == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (permanent) {
      studentEducationalLevelController.deleteStudentEducationalLevel(studentEducationalLevel);
    } else {
      studentEducationalLevelController.archiveStudentEducationalLevel(studentEducationalLevel, sessionController.getUser());
    }

    return Response.noContent().build();
  }

  @Path("/examinationTypes")
  @POST
  @RESTPermit(StudentExaminationTypePermissions.CREATE_STUDENTEXAMINATIONTYPE)
  public Response createStudentExaminationType(fi.otavanopisto.pyramus.rest.model.StudentExaminationType entity) {
    String name = entity.getName();

    if (StringUtils.isBlank(name)) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    return Response.ok(objectFactory.createModel(studentExaminationTypeController.createStudentExaminationType(name))).build();
  }

  @Path("/examinationTypes")
  @GET
  @RESTPermit(StudentExaminationTypePermissions.LIST_STUDENTEXAMINATIONTYPES)
  public Response listStudentExaminationTypes(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<StudentExaminationType> studentExaminationTypes;
    if (filterArchived) {
      studentExaminationTypes = studentExaminationTypeController.listUnarchivedStudentExaminationTypes();
    } else {
      studentExaminationTypes = studentExaminationTypeController.listStudentExaminationTypes();
    }

    if (studentExaminationTypes.isEmpty()) {
      return Response.noContent().build();
    }

    return Response.ok(objectFactory.createModel(studentExaminationTypes)).build();
  }

  @Path("/examinationTypes/{ID:[0-9]*}")
  @GET
  @RESTPermit(StudentExaminationTypePermissions.FIND_STUDENTEXAMINATIONTYPE)
  public Response findStudentExaminationTypeById(@PathParam("ID") Long id) {
    StudentExaminationType studentExaminationType = studentExaminationTypeController.findStudentExaminationTypeById(id);
    if (studentExaminationType == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (studentExaminationType.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    return Response.ok(objectFactory.createModel(studentExaminationType)).build();
  }

  @Path("/examinationTypes/{ID:[0-9]*}")
  @PUT
  @RESTPermit(StudentExaminationTypePermissions.UPDATE_STUDENTEXAMINATIONTYPE)
  public Response updateStudentExaminationType(@PathParam("ID") Long id, fi.otavanopisto.pyramus.rest.model.StudentExaminationType entity) {
    StudentExaminationType studentExaminationType = studentExaminationTypeController.findStudentExaminationTypeById(id);
    if (studentExaminationType == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (studentExaminationType.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    String name = entity.getName();

    if (StringUtils.isBlank(name)) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    return Response.ok().entity(objectFactory.createModel(studentExaminationTypeController.updateStudentExaminationType(studentExaminationType, name))).build();
  }

  @Path("/examinationTypes/{ID:[0-9]*}")
  @DELETE
  @RESTPermit(StudentExaminationTypePermissions.DELETE_STUDENTEXAMINATIONTYPE)
  public Response deleteStudentExaminationType(@PathParam("ID") Long id, @DefaultValue("false") @QueryParam("permanent") Boolean permanent) {
    StudentExaminationType studentExaminationType = studentExaminationTypeController.findStudentExaminationTypeById(id);
    if (studentExaminationType == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (permanent) {
      studentExaminationTypeController.deleteStudentExaminationType(studentExaminationType);
    } else {
      studentExaminationTypeController.archiveStudentExaminationType(studentExaminationType, sessionController.getUser());
    }

    return Response.noContent().build();
  }

  @Path("/studyProgrammeCategories")
  @POST
  @RESTPermit(StudyProgrammeCategoryPermissions.CREATE_STUDYPROGRAMMECATEGORY)
  public Response createStudyProgrammeCategory(fi.otavanopisto.pyramus.rest.model.StudyProgrammeCategory entity) {
    String name = entity.getName();

    if (StringUtils.isBlank(name)) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    if (entity.getEducationTypeId() == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    EducationType educationType = commonController.findEducationTypeById(entity.getEducationTypeId());
    if (educationType == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    return Response.ok(objectFactory.createModel(studyProgrammeCategoryController.createStudyProgrammeCategory(name, educationType))).build();
  }

  @Path("/studyProgrammeCategories")
  @GET
  @RESTPermit(StudyProgrammeCategoryPermissions.LIST_STUDYPROGRAMMECATEGORIES)
  public Response listStudyProgrammeCategories(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<StudyProgrammeCategory> studyProgrammeCategories;
    if (filterArchived) {
      studyProgrammeCategories = studyProgrammeCategoryController.listUnarchivedStudyProgrammeCategories();
    } else {
      studyProgrammeCategories = studyProgrammeCategoryController.listStudyProgrammeCategories();
    }

    if (studyProgrammeCategories.isEmpty()) {
      return Response.noContent().build();
    }

    return Response.ok(objectFactory.createModel(studyProgrammeCategories)).build();
  }

  @Path("/studyProgrammeCategories/{ID:[0-9]*}")
  @GET
  @RESTPermit(StudyProgrammeCategoryPermissions.FIND_STUDYPROGRAMMECATEGORY)
  public Response findStudyProgrammeCategoryById(@PathParam("ID") Long id) {
    StudyProgrammeCategory studyProgrammeCategory = studyProgrammeCategoryController.findStudyProgrammeCategoryById(id);
    if (studyProgrammeCategory == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (studyProgrammeCategory.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    return Response.ok(objectFactory.createModel(studyProgrammeCategory)).build();
  }

  @Path("/studyProgrammeCategories/{ID:[0-9]*}")
  @PUT
  @RESTPermit(StudyProgrammeCategoryPermissions.UPDATE_STUDYPROGRAMMECATEGORY)
  public Response updateStudyProgrammeCategory(@PathParam("ID") Long id, fi.otavanopisto.pyramus.rest.model.StudyProgrammeCategory entity) {
    StudyProgrammeCategory studyProgrammeCategory = studyProgrammeCategoryController.findStudyProgrammeCategoryById(id);
    if (studyProgrammeCategory == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (studyProgrammeCategory.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    String name = entity.getName();

    if (StringUtils.isBlank(name)) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    if (entity.getEducationTypeId() == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    EducationType educationType = commonController.findEducationTypeById(entity.getEducationTypeId());
    if (educationType == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    return Response.ok()
        .entity(objectFactory.createModel(studyProgrammeCategoryController.updateStudyProgrammeCategory(studyProgrammeCategory, name, educationType))).build();
  }

  @Path("/studyProgrammeCategories/{ID:[0-9]*}")
  @DELETE
  @RESTPermit(StudyProgrammeCategoryPermissions.DELETE_STUDYPROGRAMMECATEGORY)
  public Response deleteStudyProgrammeCategory(@PathParam("ID") Long id, @DefaultValue("false") @QueryParam("permanent") Boolean permanent) {
    StudyProgrammeCategory studyProgrammeCategory = studyProgrammeCategoryController.findStudyProgrammeCategoryById(id);
    if (studyProgrammeCategory == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (permanent) {
      studyProgrammeCategoryController.deleteStudyProgrammeCategory(studyProgrammeCategory);
    } else {
      studyProgrammeCategoryController.archiveStudyProgrammeCategory(studyProgrammeCategory, sessionController.getUser());
    }

    return Response.noContent().build();
  }

  @Path("/studyProgrammes")
  @POST
  @RESTPermit(StudyProgrammePermissions.CREATE_STUDYPROGRAMME)
  public Response createStudyProgramme(fi.otavanopisto.pyramus.rest.model.StudyProgramme entity) {
    String name = entity.getName();
    String code = entity.getCode();
    Long categoryId = entity.getCategoryId();

    if (StringUtils.isBlank(name) || StringUtils.isBlank(code) || categoryId == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    StudyProgrammeCategory programmeCategory = studyProgrammeCategoryController.findStudyProgrammeCategoryById(categoryId);
    if (programmeCategory == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    return Response.ok(objectFactory.createModel(studyProgrammeController.createStudyProgramme(name, code, programmeCategory))).build();
  }

  @Path("/studyProgrammes")
  @GET
  @RESTPermit(StudyProgrammePermissions.LIST_STUDYPROGRAMMES)
  public Response listStudyProgrammes(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<StudyProgramme> studyProgrammes;
    if (filterArchived) {
      studyProgrammes = studyProgrammeController.listUnarchivedStudyProgrammes();
    } else {
      studyProgrammes = studyProgrammeController.listStudyProgrammes();
    }

    if (studyProgrammes.isEmpty()) {
      return Response.noContent().build();
    }

    return Response.ok(objectFactory.createModel(studyProgrammes)).build();
  }

  @Path("/studyProgrammes/{ID:[0-9]*}")
  @GET
  @RESTPermit(StudyProgrammePermissions.FIND_STUDYPROGRAMME)
  public Response findStudyProgrammeById(@PathParam("ID") Long id, @Context Request request) {
    StudyProgramme studyProgramme = studyProgrammeController.findStudyProgrammeById(id);
    if (studyProgramme == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (studyProgramme.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    EntityTag tag = new EntityTag(DigestUtils.md5Hex(String.valueOf(studyProgramme.getVersion())));
    ResponseBuilder builder = request.evaluatePreconditions(tag);
    if (builder != null) {
      return builder.build();
    }

    CacheControl cacheControl = new CacheControl();
    cacheControl.setMustRevalidate(true);

    return Response.ok(objectFactory.createModel(studyProgramme)).cacheControl(cacheControl).tag(tag).build();
  }

  @Path("/studyProgrammes/{ID:[0-9]*}")
  @PUT
  @RESTPermit(StudyProgrammePermissions.UPDATE_STUDYPROGRAMME)
  public Response updateStudyProgramme(@PathParam("ID") Long id, fi.otavanopisto.pyramus.rest.model.StudyProgramme entity) {
    StudyProgramme studyProgramme = studyProgrammeController.findStudyProgrammeById(id);
    if (studyProgramme == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (studyProgramme.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    String name = entity.getName();
    String code = entity.getCode();
    Long categoryId = entity.getCategoryId();

    if (StringUtils.isBlank(name) || StringUtils.isBlank(code) || categoryId == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    StudyProgrammeCategory programmeCategory = studyProgrammeCategoryController.findStudyProgrammeCategoryById(categoryId);
    if (programmeCategory == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    return Response.ok().entity(objectFactory.createModel(studyProgrammeController.updateStudyProgramme(studyProgramme, name, code, programmeCategory)))
        .build();
  }

  @Path("/studyProgrammes/{ID:[0-9]*}")
  @DELETE
  @RESTPermit(StudyProgrammePermissions.DELETE_STUDYPROGRAMME)
  public Response deleteStudyProgramme(@PathParam("ID") Long id, @DefaultValue("false") @QueryParam("permanent") Boolean permanent) {
    StudyProgramme studyProgramme = studyProgrammeController.findStudyProgrammeById(id);
    if (studyProgramme == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (permanent) {
      studyProgrammeController.deleteStudyProgramme(studyProgramme);
    } else {
      studyProgrammeController.archiveStudyProgramme(studyProgramme, sessionController.getUser());
    }

    return Response.noContent().build();
  }

  @Path("/studentGroups")
  @POST
  @RESTPermit(StudentGroupPermissions.CREATE_STUDENTGROUP)
  public Response createStudentGroup(fi.otavanopisto.pyramus.rest.model.StudentGroup entity) {
    String name = entity.getName();
    String description = entity.getDescription();
    OffsetDateTime beginDate = entity.getBeginDate();

    if (StringUtils.isBlank(name)) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    StudentGroup studentGroup = studentGroupController.createStudentGroup(name, description, toDate(beginDate), sessionController.getUser());

    for (String tag : entity.getTags()) {
      studentGroupController.createStudentGroupTag(studentGroup, tag);
    }

    return Response.ok(objectFactory.createModel(studentGroup)).build();
  }

  @Path("/studentGroups")
  @GET
  @RESTPermit(StudentGroupPermissions.LIST_STUDENTGROUPS)
  public Response listStudentGroups(@QueryParam("firstResult") Integer firstResult, @QueryParam("maxResults") Integer maxResults,
      @DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<StudentGroup> studentGroups;

    if (sessionController.hasEnvironmentPermission(StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION)) {
      User user = sessionController.getUser();
      // List only personal groups if user can't access others
      if (filterArchived) {
        studentGroups = studentGroupController.listUnarchivedStudentGroupsByMember(user, firstResult, maxResults);
      } else {
        studentGroups = studentGroupController.listStudentGroupsByMember(user, firstResult, maxResults);
      }
    } else {
      if (filterArchived) {
        studentGroups = studentGroupController.listUnarchivedStudentGroups(firstResult, maxResults);
      } else {
        studentGroups = studentGroupController.listStudentGroups(firstResult, maxResults);
      }
    }
    
    if (studentGroups.isEmpty()) {
      return Response.noContent().build();
    }

    return Response.ok(objectFactory.createModel(studentGroups)).build();
  }

  @Path("/studentGroups/{ID:[0-9]*}")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response findStudentGroup(@PathParam("ID") Long id) {
    StudentGroup studentGroup = studentGroupController.findStudentGroupById(id);
    if (studentGroup == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (studentGroup.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (sessionController.hasPermission(StudentGroupPermissions.FIND_STUDENTGROUP, studentGroup)) {
      return Response.ok(objectFactory.createModel(studentGroup)).build();
    } else {
      return Response.status(Status.FORBIDDEN).build();
    }
  }

  @Path("/studentGroups/{ID:[0-9]*}")
  @PUT
  @RESTPermit(StudentGroupPermissions.UPDATE_STUDENTGROUP)
  public Response updateStudentGroup(@PathParam("ID") Long id, fi.otavanopisto.pyramus.rest.model.StudentGroup entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    String name = entity.getName();
    String description = entity.getDescription();
    OffsetDateTime beginDate = entity.getBeginDate();

    if (StringUtils.isBlank(name)) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    StudentGroup studentGroup = studentGroupController.findStudentGroupById(id);
    if (studentGroup == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (studentGroup.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    studentGroupController.updateStudentGroup(studentGroup, name, description, toDate(beginDate), sessionController.getUser());
    studentGroupController.updateStudentGroupTags(studentGroup, entity.getTags());

    return Response.ok(objectFactory.createModel(studentGroup)).build();
  }

  @Path("/studentGroups/{ID:[0-9]*}")
  @DELETE
  @RESTPermit(StudentGroupPermissions.DELETE_STUDENTGROUP)
  public Response deleteStudentGroup(@PathParam("ID") Long id, @DefaultValue("false") @QueryParam("permanent") Boolean permanent) {
    StudentGroup studentGroup = studentGroupController.findStudentGroupById(id);
    if (studentGroup == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (permanent) {
      studentGroupController.deleteStudentGroup(studentGroup);
    } else {
      studentGroupController.archiveStudentGroup(studentGroup, sessionController.getUser());
    }

    return Response.noContent().build();
  }

  @Path("/studentGroups/{ID:[0-9]*}/staffmembers")
  @POST
  @RESTPermit(StudentGroupPermissions.CREATE_STUDENTGROUPSTAFFMEMBER)
  public Response createStudentGroupStaffMember(@PathParam("ID") Long id, fi.otavanopisto.pyramus.rest.model.StudentGroupUser entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    if (entity.getStaffMemberId() == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    StudentGroup studentGroup = studentGroupController.findStudentGroupById(id);
    if (studentGroup == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (studentGroup.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    StaffMember staffMember = userController.findStaffMemberById(entity.getStaffMemberId());
    if (staffMember == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    StudentGroupUser studentGroupUser = studentGroupController.createStudentGroupStaffMember(studentGroup, staffMember, sessionController.getUser());

    return Response.ok(objectFactory.createModel(studentGroupUser)).build();
  }

  @Path("/studentGroups/{ID:[0-9]*}/staffmembers")
  @GET
  @RESTPermit(StudentGroupPermissions.LIST_STUDENTGROUPSTAFFMEMBERS)
  public Response listStudentGroupStaffMembers(@PathParam("ID") Long id) {
    StudentGroup studentGroup = studentGroupController.findStudentGroupById(id);
    if (studentGroup == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (studentGroup.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    List<StudentGroupUser> studentGroupUsers = new ArrayList<>(studentGroup.getUsers());
    if (studentGroupUsers.isEmpty()) {
      return Response.noContent().build();
    }

    return Response.ok(objectFactory.createModel(studentGroupUsers)).build();
  }

  @Path("/studentGroups/{GROUPID:[0-9]*}/staffmembers/{ID:[0-9]*}")
  @GET
  @RESTPermit(StudentGroupPermissions.FIND_STUDENTGROUPSTAFFMEMBER)
  public Response findStudentGroupStaffMember(@PathParam("GROUPID") Long studentGroupId, @PathParam("ID") Long id) {
    StudentGroup studentGroup = studentGroupController.findStudentGroupById(studentGroupId);
    if (studentGroup == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (studentGroup.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    StudentGroupUser studentGroupUser = studentGroupController.findStudentGroupUserById(id);
    if (studentGroupUser == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (!studentGroupUser.getStudentGroup().getId().equals(studentGroup.getId())) {
      return Response.status(Status.NOT_FOUND).build();
    }

    return Response.ok(objectFactory.createModel(studentGroupUser)).build();
  }

  @Path("/studentGroups/{GROUPID:[0-9]*}/staffmembers/{ID:[0-9]*}")
  @DELETE
  @RESTPermit(StudentGroupPermissions.DELETE_STUDENTGROUPSTAFFMEMBER)
  public Response deleteStudentGroupStaffMember(@PathParam("GROUPID") Long studentGroupId, @PathParam("ID") Long id) {
    StudentGroup studentGroup = studentGroupController.findStudentGroupById(studentGroupId);
    if (studentGroup == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (studentGroup.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    StudentGroupUser studentGroupUser = studentGroupController.findStudentGroupUserById(id);
    if (studentGroupUser == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (!studentGroupUser.getStudentGroup().getId().equals(studentGroup.getId())) {
      return Response.status(Status.NOT_FOUND).build();
    }

    studentGroupController.deleteStudentGroupUser(studentGroupUser);

    return Response.noContent().build();
  }

  @Path("/studentGroups/{ID:[0-9]*}/students")
  @POST
  @RESTPermit(StudentGroupPermissions.CREATE_STUDENTGROUPSTUDENT)
  public Response createStudentGroupStudent(@PathParam("ID") Long id, fi.otavanopisto.pyramus.rest.model.StudentGroupStudent entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    if (entity.getStudentId() == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    StudentGroup studentGroup = studentGroupController.findStudentGroupById(id);
    if (studentGroup == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (studentGroup.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    Student student = studentController.findStudentById(entity.getStudentId());
    if (student == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    StudentGroupStudent studentGroupStudent = studentGroupController.createStudentGroupStudent(studentGroup, student, sessionController.getUser());

    return Response.ok(objectFactory.createModel(studentGroupStudent)).build();
  }

  @Path("/studentGroups/{ID:[0-9]*}/students")
  @GET
  @RESTPermit(StudentGroupPermissions.LIST_STUDENTGROUPSTUDENTS)
  public Response listStudentGroupStudents(@PathParam("ID") Long id) {
    StudentGroup studentGroup = studentGroupController.findStudentGroupById(id);
    if (studentGroup == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (studentGroup.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    List<StudentGroupStudent> studentGroupStudents = new ArrayList<>(studentGroup.getStudents());
    if (studentGroupStudents.isEmpty()) {
      return Response.noContent().build();
    }
    
    Collections.sort(studentGroupStudents, new Comparator<StudentGroupStudent>() {

      @Override
      public int compare(StudentGroupStudent o1, StudentGroupStudent o2) {
        return o2.getId().compareTo(o1.getId());
      }
      
    });

    return Response.ok(objectFactory.createModel(studentGroupStudents)).build();
  }

  @Path("/studentGroups/{GROUPID:[0-9]*}/students/{ID:[0-9]*}")
  @GET
  @RESTPermit(StudentGroupPermissions.FIND_STUDENTGROUPSTUDENT)
  public Response findStudentGroupStudent(@PathParam("GROUPID") Long studentGroupId, @PathParam("ID") Long id) {
    StudentGroup studentGroup = studentGroupController.findStudentGroupById(studentGroupId);
    if (studentGroup == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (studentGroup.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    StudentGroupStudent studentGroupStudent = studentGroupController.findStudentGroupStudentById(id);
    if (studentGroupStudent == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (!studentGroupStudent.getStudentGroup().getId().equals(studentGroup.getId())) {
      return Response.status(Status.NOT_FOUND).build();
    }

    return Response.ok(objectFactory.createModel(studentGroupStudent)).build();
  }

  @Path("/studentGroups/{GROUPID:[0-9]*}/students/{ID:[0-9]*}")
  @DELETE
  @RESTPermit(StudentGroupPermissions.DELETE_STUDENTGROUPSTUDENT)
  public Response deleteStudentGroupStudent(@PathParam("GROUPID") Long studentGroupId, @PathParam("ID") Long id) {
    StudentGroup studentGroup = studentGroupController.findStudentGroupById(studentGroupId);
    if (studentGroup == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (studentGroup.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    StudentGroupStudent studentGroupStudent = studentGroupController.findStudentGroupStudentById(id);
    if (studentGroupStudent == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (!studentGroupStudent.getStudentGroup().getId().equals(studentGroup.getId())) {
      return Response.status(Status.NOT_FOUND).build();
    }

    studentGroupController.deleteStudentGroupStudent(studentGroupStudent);

    return Response.noContent().build();
  }

  @Path("/studyEndReasons")
  @POST
  @RESTPermit(StudentStudyEndReasonPermissions.CREATE_STUDENTSTUDYENDREASON)
  public Response createStudentStudyEndReason(fi.otavanopisto.pyramus.rest.model.StudentStudyEndReason entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    StudentStudyEndReason parentReason = null;

    String name = entity.getName();
    if (StringUtils.isBlank(name)) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    if (entity.getParentReasonId() != null) {
      parentReason = studentStudyEndReasonController.findStudentStudyEndReasonById(entity.getParentReasonId());
      if (parentReason == null) {
        return Response.status(Status.BAD_REQUEST).build();
      }
    }

    return Response.ok(objectFactory.createModel(studentStudyEndReasonController.createStudentStudyEndReason(parentReason, name))).build();
  }

  @Path("/studyEndReasons")
  @GET
  @RESTPermit(StudentStudyEndReasonPermissions.LIST_STUDENTSTUDYENDREASONS)
  public Response listStudentStudyEndReasons(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<StudentStudyEndReason> endReasons;
    if (filterArchived) {
      endReasons = studentStudyEndReasonController.listUnarchivedStudentStudyEndReasons();
    } else {
      endReasons = studentStudyEndReasonController.listStudentStudyEndReasons();
    }

    if (endReasons.isEmpty()) {
      return Response.noContent().build();
    }

    return Response.ok(objectFactory.createModel(endReasons)).build();
  }

  @Path("/studyEndReasons/{ID:[0-9]*}")
  @GET
  @RESTPermit(StudentStudyEndReasonPermissions.FIND_STUDENTSTUDYENDREASON)
  public Response findStudentStudyEndReasonById(@PathParam("ID") Long id) {
    StudentStudyEndReason endReason = studentStudyEndReasonController.findStudentStudyEndReasonById(id);
    if (endReason == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    return Response.ok(objectFactory.createModel(endReason)).build();
  }

  @Path("/studyEndReasons/{ID:[0-9]*}")
  @PUT
  @RESTPermit(StudentStudyEndReasonPermissions.UPDATE_STUDENTSTUDYENDREASON)
  public Response updateStudentStudyEndReason(@PathParam("ID") Long id, fi.otavanopisto.pyramus.rest.model.StudentStudyEndReason entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    StudentStudyEndReason parentReason = null;

    String name = entity.getName();
    if (StringUtils.isBlank(name)) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    StudentStudyEndReason studyEndReason = studentStudyEndReasonController.findStudentStudyEndReasonById(id);
    if (studyEndReason == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (entity.getParentReasonId() != null) {
      parentReason = studentStudyEndReasonController.findStudentStudyEndReasonById(entity.getParentReasonId());
      if (parentReason == null) {
        return Response.status(Status.BAD_REQUEST).build();
      }
    }

    studentStudyEndReasonController.updateStudentStudyEndReason(studyEndReason, name);
    studentStudyEndReasonController.updateStudentStudyEndReasonParent(studyEndReason, parentReason);

    return Response.ok(objectFactory.createModel(studyEndReason)).build();
  }

  @Path("/studyEndReasons/{ID:[0-9]*}")
  @DELETE
  @RESTPermit(StudentStudyEndReasonPermissions.DELETE_STUDENTSTUDYENDREASON)
  public Response deleteStudentStudyEndReason(@PathParam("ID") Long id) {
    StudentStudyEndReason studyEndReason = studentStudyEndReasonController.findStudentStudyEndReasonById(id);
    if (studyEndReason == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    studentStudyEndReasonController.deleteStudentStudyEndReason(studyEndReason);

    return Response.noContent().build();
  }

  @Path("/students")
  @POST
  @RESTPermit(StudentPermissions.CREATE_STUDENT)
  public Response createStudent(fi.otavanopisto.pyramus.rest.model.Student entity) {
    Long personId = entity.getPersonId();
    Long studyProgrammeId = entity.getStudyProgrammeId();
    String firstName = StringUtils.trim(entity.getFirstName());
    String lastName = StringUtils.trim(entity.getLastName());
    String nickname = StringUtils.trim(entity.getNickname());
    Boolean lodging = entity.getLodging();

    if (personId == null || studyProgrammeId == null || lodging == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    if (StringUtils.isBlank(firstName) || StringUtils.isBlank(lastName)) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    Person person = personController.findPersonById(personId);
    if (person == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    StudyProgramme studyProgramme = studyProgrammeController.findStudyProgrammeById(studyProgrammeId);
    if (studyProgramme == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    StudentActivityType activityType = entity.getActivityTypeId() != null ? studentActivityTypeController.findStudentActivityTypeById(entity
        .getActivityTypeId()) : null;
    StudentExaminationType examinationType = entity.getExaminationTypeId() != null ? studentExaminationTypeController.findStudentExaminationTypeById(entity
        .getExaminationTypeId()) : null;
    StudentEducationalLevel educationalLevel = entity.getEducationalLevelId() != null ? studentEducationalLevelController
        .findStudentEducationalLevelById(entity.getEducationalLevelId()) : null;
    Nationality nationality = entity.getNationalityId() != null ? nationalityController.findNationalityById(entity.getNationalityId()) : null;
    Municipality municipality = entity.getMunicipalityId() != null ? municipalityController.findMunicipalityById(entity.getMunicipalityId()) : null;
    Language language = entity.getLanguageId() != null ? languageController.findLanguageById(entity.getLanguageId()) : null;
    School school = entity.getSchoolId() != null ? schoolController.findSchoolById(entity.getSchoolId()) : null;
    StudentStudyEndReason studyEndReason = entity.getStudyEndReasonId() != null ? studentStudyEndReasonController.findStudentStudyEndReasonById(entity
        .getStudyEndReasonId()) : null;
    Curriculum curriculum = entity.getCurriculumId() != null ? curriculumController.findCurriculumById(entity.getCurriculumId()) : null;

    Student student = studentController.createStudent(person, firstName, lastName, nickname, entity.getAdditionalInfo(),
        toDate(entity.getStudyTimeEnd()), activityType, examinationType, educationalLevel, entity.getEducation(), nationality, municipality, language, school,
        studyProgramme, curriculum, entity.getPreviousStudies(), toDate(entity.getStudyStartDate()), toDate(entity.getStudyEndDate()), studyEndReason,
        entity.getStudyEndText(), lodging);
    userController.updateUserVariables(student, entity.getVariables());
    studentController.updateStudentTags(student, entity.getTags());
    studentController.updateStudentAdditionalContactInfo(student, entity.getAdditionalContactInfo());

    return Response.ok(objectFactory.createModel(student)).build();
  }

  @Path("/students")
  @GET
  @RESTPermit(StudentPermissions.LIST_STUDENTS)
  public Response listStudents(
      @QueryParam("firstResult") Integer firstResult, 
      @QueryParam("maxResults") Integer maxResults, 
      @QueryParam("email") String email,
      @QueryParam("filterArchived") @DefaultValue("false") boolean filterArchived) {
    List<Student> students;

    Boolean archived = filterArchived ? Boolean.FALSE : null;
    email = StringUtils.isNotBlank(email) ? email : null;
    
    if (sessionController.hasPermission(StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION, null)) {
      List<StudentGroup> groups = studentGroupController.listStudentGroupsByMember(sessionController.getUser());
      students = studentController.listStudents(email, groups, archived, firstResult, maxResults);
    } else {
      students = studentController.listStudents(email, null, archived, firstResult, maxResults);
    }

    if (CollectionUtils.isEmpty(students)) {
      return Response.noContent().build();
    }

    return Response.ok(objectFactory.createModel(students)).build();
  }

  @Path("/students/{ID:[0-9]*}")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response findStudentById(@PathParam("ID") Long id, @Context Request request) {
    Student student = studentController.findStudentById(id);
    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK)
      return Response.status(studentStatus).build();

    if (!restSecurity.hasPermission(new String[] { StudentPermissions.FIND_STUDENT, UserPermissions.USER_OWNER }, student, Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }

    EntityTag tag = new EntityTag(DigestUtils.md5Hex(String.valueOf(student.getVersion())));

    ResponseBuilder builder = request.evaluatePreconditions(tag);
    if (builder != null) {
      return builder.build();
    }

    CacheControl cacheControl = new CacheControl();
    cacheControl.setMustRevalidate(true);

    return Response.ok(objectFactory.createModel(student)).cacheControl(cacheControl).tag(tag).build();
  }

  @Path("/students/{ID:[0-9]*}")
  @PUT
  @RESTPermit(handling = Handling.INLINE)
  public Response updateStudent(@PathParam("ID") Long id, fi.otavanopisto.pyramus.rest.model.Student entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    Student student = studentController.findStudentById(id);
    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK)
      return Response.status(studentStatus).build();

    if (!restSecurity.hasPermission(new String[] { StudentPermissions.UPDATE_STUDENT, StudentPermissions.STUDENT_OWNER }, entity, Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }

    Long personId = entity.getPersonId();
    Long studyProgrammeId = entity.getStudyProgrammeId();
    String firstName = StringUtils.trim(entity.getFirstName());
    String lastName = StringUtils.trim(entity.getLastName());
    String nickname = StringUtils.trim(entity.getNickname());
    Boolean lodging = entity.getLodging();

    if (personId == null || studyProgrammeId == null || lodging == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    if (StringUtils.isBlank(firstName) || StringUtils.isBlank(lastName)) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    Person person = personController.findPersonById(personId);
    if (person == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    StudyProgramme studyProgramme = studyProgrammeController.findStudyProgrammeById(studyProgrammeId);
    if (studyProgramme == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    StudentActivityType activityType = entity.getActivityTypeId() != null ? studentActivityTypeController.findStudentActivityTypeById(entity
        .getActivityTypeId()) : null;
    StudentExaminationType examinationType = entity.getExaminationTypeId() != null ? studentExaminationTypeController.findStudentExaminationTypeById(entity
        .getExaminationTypeId()) : null;
    StudentEducationalLevel educationalLevel = entity.getEducationalLevelId() != null ? studentEducationalLevelController
        .findStudentEducationalLevelById(entity.getEducationalLevelId()) : null;
    Nationality nationality = entity.getNationalityId() != null ? nationalityController.findNationalityById(entity.getNationalityId()) : null;
    Municipality municipality = entity.getMunicipalityId() != null ? municipalityController.findMunicipalityById(entity.getMunicipalityId()) : null;
    Language language = entity.getLanguageId() != null ? languageController.findLanguageById(entity.getLanguageId()) : null;
    School school = entity.getSchoolId() != null ? schoolController.findSchoolById(entity.getSchoolId()) : null;
    StudentStudyEndReason studyEndReason = entity.getStudyEndReasonId() != null ? studentStudyEndReasonController.findStudentStudyEndReasonById(entity
        .getStudyEndReasonId()) : null;
    Curriculum curriculum = entity.getCurriculumId() != null ? curriculumController.findCurriculumById(entity.getCurriculumId()) : null;

    studentController.updateStudent(student, firstName, lastName, nickname, entity.getAdditionalInfo(), toDate(entity.getStudyTimeEnd()),
        activityType, examinationType, educationalLevel, entity.getEducation(), nationality, municipality, language, school, studyProgramme, curriculum,
        entity.getPreviousStudies(), toDate(entity.getStudyStartDate()), toDate(entity.getStudyEndDate()), studyEndReason, entity.getStudyEndText(), lodging);

    studentController.updateStudentPerson(student, person);
    userController.updateUserVariables(student, entity.getVariables());
    studentController.updateStudentTags(student, entity.getTags());
    studentController.updateStudentAdditionalContactInfo(student, entity.getAdditionalContactInfo());

    return Response.ok(objectFactory.createModel(student)).build();
  }

  @Path("/students/{ID:[0-9]*}")
  @DELETE
  @RESTPermit(StudentPermissions.DELETE_STUDENT)
  public Response deleteStudent(@PathParam("ID") Long id, @DefaultValue("false") @QueryParam("permanent") Boolean permanent) {
    Student student = studentController.findStudentById(id);
    if (student == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (!restSecurity.hasPermission(new String[] { StudentPermissions.FIND_STUDENT, UserPermissions.USER_OWNER }, student, Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }

    if (permanent) {
      List<UserVariable> userVariables = userController.listUserVariablesByUser(student);
      for (UserVariable userVariable : userVariables) {
        userController.deleteUserVariable(userVariable);
      }

      studentController.deleteStudent(student);
    } else {
      studentController.archiveStudent(student, sessionController.getUser());
    }

    return Response.noContent().build();
  }

  @Path("/students/{ID:[0-9]*}/contactLogEntries")
  @POST
  @RESTPermit(StudentContactLogEntryPermissions.CREATE_STUDENTCONTACTLOGENTRY)
  public Response createStudentContactLogEntry(@PathParam("ID") Long id, fi.otavanopisto.pyramus.rest.model.StudentContactLogEntry entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    Student student = studentController.findStudentById(id);
    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK)
      return Response.status(studentStatus).build();

    if (!restSecurity.hasPermission(new String[] { StudentPermissions.FIND_STUDENT, UserPermissions.USER_OWNER }, student, Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }
    
    StudentContactLogEntryType type = entity.getType() != null ? StudentContactLogEntryType.valueOf(entity.getType().name()) : null;
    StudentContactLogEntry contactLogEntry = studentContactLogEntryController.createContactLogEntry(student, type, entity.getText(),
        toDate(entity.getEntryDate()), entity.getCreatorName());

    return Response.ok(objectFactory.createModel(contactLogEntry)).build();
  }

  @Path("/students/{STUDENTID:[0-9]*}/contactLogEntries")
  @GET
  @RESTPermit(StudentContactLogEntryPermissions.LIST_STUDENTCONTACTLOGENTRIES)
  public Response listStudentContactLogEntriesByStudent(@PathParam("STUDENTID") Long studentId) {
    Student student = studentController.findStudentById(studentId);
    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK)
      return Response.status(studentStatus).build();

    return Response.ok(objectFactory.createModel(studentContactLogEntryController.listContactLogEntriesByStudent(student))).build();
  }

  @Path("/students/{STUDENTID:[0-9]*}/contactLogEntries/{ID:[0-9]*}")
  @GET
  @RESTPermit(StudentContactLogEntryPermissions.FIND_STUDENTCONTACTLOGENTRY)
  public Response findStudentContactLogEntryById(@PathParam("STUDENTID") Long studentId, @PathParam("ID") Long id) {
    Student student = studentController.findStudentById(studentId);
    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK)
      return Response.status(studentStatus).build();

    StudentContactLogEntry contactLogEntry = studentContactLogEntryController.findContactLogEntryById(id);
    if (contactLogEntry == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (contactLogEntry.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (!contactLogEntry.getStudent().getId().equals(contactLogEntry.getStudent().getId())) {
      return Response.status(Status.NOT_FOUND).build();
    }

    return Response.ok(objectFactory.createModel(contactLogEntry)).build();
  }

  @Path("/students/{STUDENTID:[0-9]*}/contactLogEntries/{ID:[0-9]*}")
  @PUT
  @RESTPermit(StudentContactLogEntryPermissions.UPDATE_STUDENTCONTACTLOGENTRY)
  public Response updateStudentContactLogEntry(@PathParam("STUDENTID") Long studentId, @PathParam("ID") Long id,
      fi.otavanopisto.pyramus.rest.model.StudentContactLogEntry entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    Student student = studentController.findStudentById(studentId);
    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK)
      return Response.status(studentStatus).build();

    StudentContactLogEntry contactLogEntry = studentContactLogEntryController.findContactLogEntryById(id);
    if (contactLogEntry == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (contactLogEntry.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (!contactLogEntry.getStudent().getId().equals(contactLogEntry.getStudent().getId())) {
      return Response.status(Status.NOT_FOUND).build();
    }

    StudentContactLogEntryType type = entity.getType() != null ? StudentContactLogEntryType.valueOf(entity.getType().name()) : null;
    studentContactLogEntryController.updateContactLogEntry(contactLogEntry, type, entity.getText(), toDate(entity.getEntryDate()), entity.getCreatorName());

    return Response.ok(objectFactory.createModel(contactLogEntry)).build();
  }

  @Path("/students/{STUDENTID:[0-9]*}/contactLogEntries/{ID:[0-9]*}")
  @DELETE
  @RESTPermit(StudentContactLogEntryPermissions.DELETE_STUDENTCONTACTLOGENTRY)
  public Response deleteStudentContactLogEntry(@PathParam("STUDENTID") Long studentId, @PathParam("ID") Long id,
      @DefaultValue("false") @QueryParam("permanent") Boolean permanent) {
    Student student = studentController.findStudentById(studentId);
    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK)
      return Response.status(studentStatus).build();

    StudentContactLogEntry contactLogEntry = studentContactLogEntryController.findContactLogEntryById(id);
    if (contactLogEntry == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (!contactLogEntry.getStudent().getId().equals(contactLogEntry.getStudent().getId())) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (permanent) {
      studentContactLogEntryController.deleteStudentContactLogEntry(contactLogEntry);
    } else {
      studentContactLogEntryController.archiveStudentContactLogEntry(contactLogEntry, sessionController.getUser());
    }

    return Response.noContent().build();
  }

  @Path("/students/{STUDENTID:[0-9]*}/courses/{COURSEID:[0-9]*}/assessments/")
  @POST
  @RESTPermit(CourseAssessmentPermissions.CREATE_COURSEASSESSMENT)
  public Response createCourseAssessment(@PathParam("STUDENTID") Long studentId, @PathParam("COURSEID") Long courseId,
      fi.otavanopisto.pyramus.rest.model.CourseAssessment entity) {

    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    Student student = studentController.findStudentById(studentId);
    
    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK)
      return Response.status(studentStatus).build();

    Course course = courseController.findCourseById(courseId);
    
    if (course == null) {
      return Response.status(Status.NOT_FOUND).entity("Could not find course").build();
    }

    if (course.getArchived()) {
      return Response.status(Status.NOT_FOUND).entity("Course is archived").build();
    }

    CourseStudent courseStudent = courseController.findCourseStudentById(entity.getCourseStudentId());
    
    if(courseStudent == null){
      return Response.status(Status.BAD_REQUEST).entity("Could not find coursestudent").build();
    }
    
    if(courseStudent.getArchived()){
      return Response.status(Status.BAD_REQUEST).entity("Coursestudent is archived").build();
    }
    
    if(!courseStudent.getStudent().getId().equals(student.getId())){
      return Response.status(Status.BAD_REQUEST).entity("Coursestudent doesnt match student").build();
    }
    
    StaffMember assessor = userController.findStaffMemberById(entity.getAssessorId());
    
    if(assessor == null){
      return Response.status(Status.BAD_REQUEST).entity("Could not find assessor").build();
    }
    
    Grade grade = commonController.findGradeByIdId(entity.getGradeId());
    
    if(grade == null){
      return Response.status(Status.BAD_REQUEST).entity("Could not find grade").build();
    }
    
    CourseAssessment courseAssessment = assessmentController.createCourseAssessment(courseStudent, assessor, grade, Date.from(entity.getDate().toInstant()), entity.getVerbalAssessment());
    
    return Response.ok(objectFactory.createModel(courseAssessment)).build();
  }
  
  @Path("/students/{STUDENTID:[0-9]*}/courses/{COURSEID:[0-9]*}/assessments/")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response listCourseAssessments(@PathParam("STUDENTID") Long studentId, @PathParam("COURSEID") Long courseId) {
    
    Student student = studentController.findStudentById(studentId);

    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK)
      return Response.status(studentStatus).build();

    if (!restSecurity.hasPermission(new String[] { CourseAssessmentPermissions.LIST_COURSEASSESSMENT, PersonPermissions.PERSON_OWNER }, student.getPerson(), Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }

    Course course = courseController.findCourseById(courseId);
    
    if (course == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (course.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    List<CourseAssessment> courseAssessments = assessmentController.listByCourseAndStudent(course, student);
    
    return Response.ok(objectFactory.createModel(courseAssessments)).build();
  }
  
  @Path("/students/{STUDENTID:[0-9]*}/courses/{COURSEID:[0-9]*}/assessments/{ID:[0-9]*}")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response findCourseAssessmentById(@PathParam("STUDENTID") Long studentId, @PathParam("COURSEID") Long courseId, @PathParam("ID") Long id) {
    
    Student student = studentController.findStudentById(studentId);

    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK)
      return Response.status(studentStatus).build();

    if (!restSecurity.hasPermission(new String[] { CourseAssessmentPermissions.FIND_COURSEASSESSMENT, PersonPermissions.PERSON_OWNER }, student.getPerson(), Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }

    Course course = courseController.findCourseById(courseId);
    
    if (course == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (course.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    CourseAssessment courseAssessment = assessmentController.findCourseAssessmentById(id);
    if (!course.getId().equals(courseAssessment.getCourseStudent().getCourse().getId())) {
      return Response.status(Status.NOT_FOUND).entity("Could not find a course assessment for course student course").build();
    }
    
    if (!student.getId().equals(courseAssessment.getCourseStudent().getStudent().getId())) {
      return Response.status(Status.NOT_FOUND).entity("Could not find a course assessment for course student student").build();
    }
    
    return Response.ok(objectFactory.createModel(courseAssessment)).build();
  }
  
  @Path("/students/{STUDENTID:[0-9]*}/courses/{COURSEID:[0-9]*}/assessments/{ID:[0-9]*}")
  @PUT
  @RESTPermit(CourseAssessmentPermissions.UPDATE_COURSEASSESSMENT)
  public Response updateCourseAssessment(@PathParam("STUDENTID") Long studentId, @PathParam("COURSEID") Long courseId, @PathParam("ID") Long id, 
      fi.otavanopisto.pyramus.rest.model.CourseAssessment entity) {
    
    Student student = studentController.findStudentById(studentId);
    Course course = courseController.findCourseById(courseId);
    CourseAssessment courseAssessment = assessmentController.findCourseAssessmentById(id);

    if(courseAssessment == null){
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK)
      return Response.status(studentStatus).build();

    if (course == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (course.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    CourseStudent courseStudent = courseController.findCourseStudentById(entity.getCourseStudentId());
    
    if(courseStudent == null){
      return Response.status(Status.NOT_FOUND).build();
    }
    
    StaffMember assessor = userController.findStaffMemberById(entity.getAssessorId());
    
    if(assessor == null){
      return Response.status(Status.NOT_FOUND).build();
    }
    
    Grade grade = commonController.findGradeByIdId(entity.getGradeId());
    
    if(grade == null){
      return Response.status(Status.NOT_FOUND).build();
    }
    
    CourseAssessment newCourseAssessment = assessmentController.updateCourseAssessment(courseAssessment, assessor, grade, Date.from(entity.getDate().toInstant()), entity.getVerbalAssessment());
        
    return Response.ok(objectFactory.createModel(newCourseAssessment)).build();
  }

  @Path("/students/{STUDENTID:[0-9]*}/courses/{COURSEID}/assessments/{ID}")
  @DELETE
  @RESTPermit(CourseAssessmentPermissions.DELETE_COURSEASSESSMENT)
  public Response deleteCourseAssessment(@PathParam("STUDENTID") Long studentId, @PathParam("COURSEID") Long courseId, @PathParam("ID") Long id) {
    
    Student student = studentController.findStudentById(studentId);
    Course course = courseController.findCourseById(courseId);

    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK)
      return Response.status(studentStatus).build();

    if (course == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (course.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    CourseAssessment courseAssessment = assessmentController.findCourseAssessmentById(id);
    
    if(courseAssessment == null){
     return Response.status(Status.NOT_FOUND).build(); 
    }
    
    assessmentController.deleteCourseAssessment(courseAssessment);
    
    return Response.noContent().build();
  }
  
  @Path("/students/{STUDENTID:[0-9]*}/courses/{COURSEID:[0-9]*}/assessmentRequests/")
  @POST
  @RESTPermit(handling = Handling.INLINE)
  public Response createCourseAssessmentRequest(@PathParam("STUDENTID") Long studentId, @PathParam("COURSEID") Long courseId,
      fi.otavanopisto.pyramus.rest.model.CourseAssessmentRequest entity) {

    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    Student student = studentController.findStudentById(studentId);
    
    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK)
      return Response.status(studentStatus).build();

    Course course = courseController.findCourseById(courseId);
    
    if (course == null) {
      return Response.status(Status.NOT_FOUND).entity("Could not find course").build();
    }

    if (course.getArchived()) {
      return Response.status(Status.NOT_FOUND).entity("Course is archived").build();
    }

    if (!restSecurity.hasPermission(new String[] { CourseAssessmentPermissions.CREATE_COURSEASSESSMENTREQUEST, StudentPermissions.STUDENT_OWNER }, student, Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }

    CourseStudent courseStudent = courseController.findCourseStudentById(entity.getCourseStudentId());
    
    if (courseStudent == null) {
      return Response.status(Status.BAD_REQUEST).entity("Could not find coursestudent").build();
    }
    
    if (courseStudent.getArchived()){
      return Response.status(Status.BAD_REQUEST).entity("Coursestudent is archived").build();
    }
    
    if (!courseStudent.getStudent().getId().equals(student.getId())){
      return Response.status(Status.BAD_REQUEST).entity("Coursestudent doesnt match student").build();
    }
    
    if (!courseStudent.getCourse().getId().equals(course.getId())){
      return Response.status(Status.BAD_REQUEST).entity("Coursestudent doesnt match course").build();
    }
    
    CourseAssessmentRequest courseAssessmentRequest = assessmentController.createCourseAssessmentRequest(courseStudent, Date.from(entity.getCreated().toInstant()), entity.getRequestText());
    
    return Response.ok(objectFactory.createModel(courseAssessmentRequest)).build();
  }

  @Path("/students/{STUDENTID:[0-9]*}/courses/{COURSEID:[0-9]*}/assessmentRequests/")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response listCourseAssessmentRequests(@PathParam("STUDENTID") Long studentId, @PathParam("COURSEID") Long courseId) {
    Student student = studentController.findStudentById(studentId);

    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK)
      return Response.status(studentStatus).build();

    if (!restSecurity.hasPermission(new String[] { CourseAssessmentPermissions.LIST_COURSEASSESSMENTREQUESTS, StudentPermissions.STUDENT_OWNER }, student, Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }

    Course course = courseController.findCourseById(courseId);
    
    if (course == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (course.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    List<CourseAssessmentRequest> assessmentRequests = assessmentController.listCourseAssessmentRequestsByCourseAndStudent(course, student);
    
    return Response.ok(objectFactory.createModel(assessmentRequests)).build();
  }
  
  @Path("/students/{STUDENTID:[0-9]*}/assessmentRequests/")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response listStudentAssessmentRequests(@PathParam("STUDENTID") Long studentId) {
    Student student = studentController.findStudentById(studentId);

    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK)
      return Response.status(studentStatus).build();

    if (!restSecurity.hasPermission(new String[] { CourseAssessmentPermissions.LIST_COURSEASSESSMENTREQUESTS, StudentPermissions.STUDENT_OWNER }, student, Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }

    List<CourseAssessmentRequest> assessmentRequests = assessmentController.listCourseAssessmentRequestsByStudent(student);
    
    return Response.ok(objectFactory.createModel(assessmentRequests)).build();
  }

  @Path("/students/{STUDENTID:[0-9]*}/latestAssessmentRequest/")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response findLatestStudentAssessmentRequest(@PathParam("STUDENTID") Long studentId) {
    Student student = studentController.findStudentById(studentId);
    if (student == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    if (student.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    if (!restSecurity.hasPermission(new String[] { CourseAssessmentPermissions.LIST_COURSEASSESSMENTREQUESTS, StudentPermissions.STUDENT_OWNER }, student, Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }
    List<CourseAssessmentRequest> assessmentRequests = assessmentController.listCourseAssessmentRequestsByStudent(student);
    if (CollectionUtils.isEmpty(assessmentRequests)) {
      return Response.status(Status.NOT_FOUND).build();
    }
    Collections.sort(assessmentRequests, new Comparator<CourseAssessmentRequest>() {
      public int compare(CourseAssessmentRequest o1, CourseAssessmentRequest o2) {
        return o2.getCreated().compareTo(o1.getCreated());
      }
    });
    return Response.ok(objectFactory.createModel(assessmentRequests.get(0))).build();
  }

  @Path("/students/{STUDENTID:[0-9]*}/assessments/")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response listStudentAssessments(@PathParam("STUDENTID") Long studentId) {
    Student student = studentController.findStudentById(studentId);

    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK)
      return Response.status(studentStatus).build();

    if (!restSecurity.hasPermission(new String[] { CourseAssessmentPermissions.LIST_COURSEASSESSMENT, StudentPermissions.STUDENT_OWNER }, student, Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }

    List<CourseAssessment> assessments = assessmentController.listByStudent(student);
    
    return Response.ok(objectFactory.createModel(assessments)).build();
  }

  @Path("/students/{STUDENTID:[0-9]*}/latestCourseAssessment/")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response findLatestStudentWorkspaceAssessment(@PathParam("STUDENTID") Long studentId) {
    Student student = studentController.findStudentById(studentId);
    if (student == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    if (student.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    if (!restSecurity.hasPermission(new String[] { CourseAssessmentPermissions.LIST_COURSEASSESSMENT, PersonPermissions.PERSON_OWNER }, student.getPerson(), Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }
    List<CourseAssessment> courseAssessments = assessmentController.listByStudent(student);
    if (CollectionUtils.isEmpty(courseAssessments)) {
      return Response.status(Status.NOT_FOUND).build();
    }
    Collections.sort(courseAssessments, new Comparator<CourseAssessment>() {
      public int compare(CourseAssessment o1, CourseAssessment o2) {
        return o2.getDate().compareTo(o1.getDate());
      }
    });
    return Response.ok(objectFactory.createModel(courseAssessments.get(0))).build();
  }
  
  @Path("/students/{STUDENTID:[0-9]*}/courseAssessmentCount/")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response findStudentCourseAssessmentCount(
      @PathParam("STUDENTID") Long studentId,
      @QueryParam("from") ISO8601Timestamp from,
      @QueryParam("to") ISO8601Timestamp to,
      @QueryParam("onlyPassingGrades") @DefaultValue("false") boolean onlyPassingGrades) {
    
    Student student = studentController.findStudentById(studentId);
    if (student == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    if (student.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    if (!restSecurity.hasPermission(new String[] { CourseAssessmentPermissions.LIST_COURSEASSESSMENT, PersonPermissions.PERSON_OWNER }, student.getPerson(), Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }
    
    Boolean passingGrade = onlyPassingGrades ? Boolean.TRUE : null;
    
    Date fromDate = from != null ? from.getDate() : null;
    Date toDate = to != null ? to.getDate() : null;
    
    Long courseAssessmentCount = assessmentController.countCourseAssessments(student, fromDate, toDate, passingGrade);

    return Response.ok(courseAssessmentCount).build();
  }
  
  @Path("/students/{STUDENTID:[0-9]*}/courses/{COURSEID:[0-9]*}/assessmentRequests/{ID:[0-9]*}")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response findCourseAssessmentRequestById(@PathParam("STUDENTID") Long studentId, @PathParam("COURSEID") Long courseId, @PathParam("ID") Long id) {
    
    Student student = studentController.findStudentById(studentId);

    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK)
      return Response.status(studentStatus).build();

    Course course = courseController.findCourseById(courseId);
    
    if (course == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (course.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (!restSecurity.hasPermission(new String[] { CourseAssessmentPermissions.FIND_COURSEASSESSMENTREQUEST, StudentPermissions.STUDENT_OWNER }, student, Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }
    
    CourseAssessmentRequest courseAssessmentRequest = assessmentController.findCourseAssessmentRequestById(id);
    if (!course.getId().equals(courseAssessmentRequest.getCourseStudent().getCourse().getId())) {
      return Response.status(Status.NOT_FOUND).entity("Could not find a course assessment for course student course").build();
    }
    
    if (!student.getId().equals(courseAssessmentRequest.getCourseStudent().getStudent().getId())) {
      return Response.status(Status.NOT_FOUND).entity("Could not find a course assessment for course student student").build();
    }
    
    return Response.ok(objectFactory.createModel(courseAssessmentRequest)).build();
  }
  
  @Path("/students/{STUDENTID:[0-9]*}/courses/{COURSEID:[0-9]*}/assessmentRequests/{ID:[0-9]*}")
  @PUT
  @RESTPermit(handling = Handling.INLINE)
  public Response updateCourseAssessmentRequest(@PathParam("STUDENTID") Long studentId, @PathParam("COURSEID") Long courseId, @PathParam("ID") Long id, 
      fi.otavanopisto.pyramus.rest.model.CourseAssessmentRequest entity) {
    
    Student student = studentController.findStudentById(studentId);
    Course course = courseController.findCourseById(courseId);
    CourseAssessmentRequest courseAssessmentRequest = assessmentController.findCourseAssessmentRequestById(id);

    if (courseAssessmentRequest == null){
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK)
      return Response.status(studentStatus).build();

    if (course == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (course.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (!course.getId().equals(courseAssessmentRequest.getCourseStudent().getCourse().getId())) {
      return Response.status(Status.BAD_REQUEST).entity("Course ids mismatch.").build();
    }
    
    if (!student.getId().equals(courseAssessmentRequest.getCourseStudent().getStudent().getId())) {
      return Response.status(Status.BAD_REQUEST).entity("Student ids mismatch.").build();
    }

    if (!courseAssessmentRequest.getCourseStudent().getId().equals(entity.getCourseStudentId())) {
      return Response.status(Status.BAD_REQUEST).entity("CourseAssessmentRequest ids mismatch.").build();
    }
    
    if (!restSecurity.hasPermission(new String[] { CourseAssessmentPermissions.UPDATE_COURSEASSESSMENTREQUEST, StudentPermissions.STUDENT_OWNER }, student, Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }
    
    CourseAssessmentRequest updatedCourseAssessmentRequest = assessmentController.updateCourseAssessmentRequest(
      courseAssessmentRequest,
      Date.from(entity.getCreated().toInstant()),
      entity.getRequestText(),
      entity.getHandled());
        
    return Response.ok(objectFactory.createModel(updatedCourseAssessmentRequest)).build();
  }

  @Path("/students/{STUDENTID:[0-9]*}/courses/{COURSEID}/assessmentRequests/{ID}")
  @DELETE
  @RESTPermit(handling = Handling.INLINE)
  public Response deleteCourseAssessmentRequest(@PathParam("STUDENTID") Long studentId, @PathParam("COURSEID") Long courseId, @PathParam("ID") Long id) {
    Student student = studentController.findStudentById(studentId);
    Course course = courseController.findCourseById(courseId);

    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK)
      return Response.status(studentStatus).build();

    if (course == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (course.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (!restSecurity.hasPermission(new String[] { CourseAssessmentPermissions.DELETE_COURSEASSESSMENTREQUEST, StudentPermissions.STUDENT_OWNER }, student, Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }
    
    CourseAssessmentRequest courseAssessmentRequest = assessmentController.findCourseAssessmentRequestById(id);
    
    if (courseAssessmentRequest == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (!course.getId().equals(courseAssessmentRequest.getCourseStudent().getCourse().getId())) {
      return Response.status(Status.BAD_REQUEST).entity("Course ids mismatch.").build();
    }
    
    if (!student.getId().equals(courseAssessmentRequest.getCourseStudent().getStudent().getId())) {
      return Response.status(Status.BAD_REQUEST).entity("Student ids mismatch.").build();
    }
    
    assessmentController.deleteCourseAssessmentRequest(courseAssessmentRequest);
    
    return Response.noContent().build();
  }
  
  @Path("/variables")
  @POST
  @RESTPermit(UserPermissions.CREATE_USERVARIABLEKEY)
  public Response createVariable(fi.otavanopisto.pyramus.rest.model.VariableKey entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    if (StringUtils.isBlank(entity.getKey()) || StringUtils.isBlank(entity.getName()) || entity.getType() == null || entity.getUserEditable() == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    VariableType variableType = null;
    switch (entity.getType()) {
      case BOOLEAN:
        variableType = VariableType.BOOLEAN;
      break;
      case DATE:
        variableType = VariableType.DATE;
      break;
      case NUMBER:
        variableType = VariableType.NUMBER;
      break;
      case TEXT:
        variableType = VariableType.TEXT;
      break;
    }

    UserVariableKey userVariableKey = userController.createUserVariableKey(entity.getKey(), entity.getName(), variableType, entity.getUserEditable());
    return Response.ok(objectFactory.createModel(userVariableKey)).build();
  }

  @Path("/variables")
  @GET
  @RESTPermit(UserPermissions.LIST_USERVARIABLEKEYS)
  public Response listVariables() {
    List<UserVariableKey> variableKeys = userController.listUserVariableKeys();
    if (variableKeys.isEmpty()) {
      return Response.noContent().build();
    }

    return Response.ok(objectFactory.createModel(variableKeys)).build();
  }

  @Path("/variables/{KEY}")
  @GET
  @RESTPermit(UserPermissions.FIND_USERVARIABLEKEY)
  public Response findVariable(@PathParam("KEY") String key) {
    UserVariableKey studentVariableKey = userController.findUserVariableKeyByVariableKey(key);
    if (studentVariableKey == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    return Response.ok(objectFactory.createModel(studentVariableKey)).build();
  }

  @Path("/variables/{KEY}")
  @PUT
  @RESTPermit(UserPermissions.UPDATE_USERVARIABLEKEY)
  public Response updateVariable(@PathParam("KEY") String key, fi.otavanopisto.pyramus.rest.model.VariableKey entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    if (StringUtils.isBlank(entity.getName()) || entity.getType() == null || entity.getUserEditable() == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    UserVariableKey userVariableKey = userController.findUserVariableKeyByVariableKey(key);
    if (userVariableKey == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    VariableType variableType = null;
    switch (entity.getType()) {
      case BOOLEAN:
        variableType = VariableType.BOOLEAN;
      break;
      case DATE:
        variableType = VariableType.DATE;
      break;
      case NUMBER:
        variableType = VariableType.NUMBER;
      break;
      case TEXT:
        variableType = VariableType.TEXT;
      break;
    }

    userController.updateUserVariableKey(userVariableKey, entity.getName(), variableType, entity.getUserEditable());

    return Response.ok(objectFactory.createModel(userVariableKey)).build();
  }

  @Path("/variables/{KEY}")
  @DELETE
  @RESTPermit(UserPermissions.DELETE_USERVARIABLEKEY)
  public Response deleteVariable(@PathParam("KEY") String key) {
    UserVariableKey userVariableKey = userController.findUserVariableKeyByVariableKey(key);
    if (userVariableKey == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    userController.deleteUserVariableKey(userVariableKey);

    return Response.noContent().build();
  }

  @Path("/students/{STUDENTID:[0-9]*}/emails")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response listStudentEmails(@PathParam("STUDENTID") Long studentId) {
    Student student = studentController.findStudentById(studentId);
    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK)
      return Response.status(studentStatus).build();

    if (!restSecurity.hasPermission(new String[] { StudentPermissions.LIST_STUDENTEMAILS }, student) && !restSecurity.hasPermission(new String[] { PersonPermissions.PERSON_OWNER }, student.getPerson() )) {
      return Response.status(Status.FORBIDDEN).build();
    }

    List<Email> emails = student.getContactInfo().getEmails();
    if (emails.isEmpty()) {
      return Response.noContent().build();
    }

    return Response.ok(objectFactory.createModel(emails)).build();
  }

  @Path("/students/{STUDENTID:[0-9]*}/emails")
  @POST
  @RESTPermit(handling = Handling.INLINE)
  public Response createStudentEmail(@PathParam("STUDENTID") Long studentId, fi.otavanopisto.pyramus.rest.model.Email email) {
    if (email == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    Student student = studentController.findStudentById(studentId);
    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK)
      return Response.status(studentStatus).build();

    if (!restSecurity.hasPermission(new String[] { StudentPermissions.CREATE_STUDENTEMAIL, StudentPermissions.STUDENT_OWNER }, student, Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }

    Long contactTypeId = email.getContactTypeId();
    Boolean defaultAddress = email.getDefaultAddress();
    String address = email.getAddress();

    if (contactTypeId == null || defaultAddress == null || StringUtils.isBlank(address)) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    ContactType contactType = commonController.findContactTypeById(contactTypeId);
    if (contactType == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    try {
      return Response.ok(objectFactory.createModel(studentController.addStudentEmail(student, contactType, address, defaultAddress))).build();
    } catch (UserEmailInUseException ueiue) {
      return Response.status(Status.FORBIDDEN).build();
    }
  }
  
  @Path("/students/{STUDENTID:[0-9]*}/defaultemail")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response findStudentDefaultEmail(@PathParam("STUDENTID") Long studentId) {
    Student student = studentController.findStudentById(studentId);
    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK)
      return Response.status(studentStatus).build();

    if (!restSecurity.hasPermission(new String[] { StudentPermissions.FIND_STUDENTEMAIL, StudentPermissions.STUDENT_OWNER }, student, Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }

    Email email = commonController.findDefaultEmailByContactInfo(student.getContactInfo());
    if (email == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    return Response.ok(objectFactory.createModel(email)).build();
  }

  @Path("/students/{STUDENTID:[0-9]*}/emails/{ID:[0-9]*}")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response findStudentEmail(@PathParam("STUDENTID") Long studentId, @PathParam("ID") Long id) {
    Student student = studentController.findStudentById(studentId);
    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK)
      return Response.status(studentStatus).build();

    if (!restSecurity.hasPermission(new String[] { StudentPermissions.FIND_STUDENTEMAIL }, student) && !restSecurity.hasPermission(new String[] { PersonPermissions.PERSON_OWNER }, student.getPerson() )) {
      return Response.status(Status.FORBIDDEN).build();
    }

    Email email = commonController.findEmailById(id);
    if (email == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (!email.getContactInfo().getId().equals(student.getContactInfo().getId())) {
      return Response.status(Status.NOT_FOUND).build();
    }

    return Response.ok(objectFactory.createModel(email)).build();
  }

  @Path("/students/{STUDENTID:[0-9]*}/emails/{ID:[0-9]*}")
  @DELETE
  @RESTPermit(StudentPermissions.DELETE_STUDENTEMAIL)
  public Response deleteStudentEmail(@PathParam("STUDENTID") Long studentId, @PathParam("ID") Long id) {
    Student student = studentController.findStudentById(studentId);
    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK)
      return Response.status(studentStatus).build();

    Email email = commonController.findEmailById(id);
    if (email == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (!email.getContactInfo().getId().equals(student.getContactInfo().getId())) {
      return Response.status(Status.NOT_FOUND).build();
    }

    commonController.deleteEmail(email);

    return Response.noContent().build();
  }

  @Path("/students/{STUDENTID:[0-9]*}/addresses")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response listStudentAddresses(@PathParam("STUDENTID") Long studentId) {
    Student student = studentController.findStudentById(studentId);
    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK)
      return Response.status(studentStatus).build();

    if (!restSecurity.hasPermission(new String[] { StudentPermissions.LIST_STUDENTADDRESSS }, student) && !restSecurity.hasPermission(new String[] { PersonPermissions.PERSON_OWNER }, student.getPerson() )) {
      return Response.status(Status.FORBIDDEN).build();
    }

    List<Address> addresses = student.getContactInfo().getAddresses();
    if (addresses.isEmpty()) {
      return Response.noContent().build();
    }

    return Response.ok(objectFactory.createModel(addresses)).build();
  }

  @Path("/students/{STUDENTID:[0-9]*}/addresses")
  @POST
  @RESTPermit(handling = Handling.INLINE)
  public Response createStudentAddress(@PathParam("STUDENTID") Long studentId, fi.otavanopisto.pyramus.rest.model.Address address) {
    if (address == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    Student student = studentController.findStudentById(studentId);
    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK)
      return Response.status(studentStatus).build();

    if (!restSecurity.hasPermission(new String[] { StudentPermissions.UPDATE_STUDENT, StudentPermissions.STUDENT_OWNER }, student, Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }

    Long contactTypeId = address.getContactTypeId();
    Boolean defaultAddress = address.getDefaultAddress();
    String name = address.getName();
    String streetAddress = address.getStreetAddress();
    String postalCode = address.getPostalCode();
    String country = address.getCountry();
    String city = address.getCity();

    if (contactTypeId == null || defaultAddress == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    ContactType contactType = commonController.findContactTypeById(contactTypeId);
    if (contactType == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    return Response.ok(
        objectFactory.createModel(studentController.addStudentAddress(student, contactType, defaultAddress, name, streetAddress, postalCode, city, country)))
        .build();
  }

  @Path("/students/{STUDENTID:[0-9]*}/addresses/{ID:[0-9]*}")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response findStudentAddress(@PathParam("STUDENTID") Long studentId, @PathParam("ID") Long id) {
    Student student = studentController.findStudentById(studentId);
    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK)
      return Response.status(studentStatus).build();

    if (!restSecurity.hasPermission(new String[] { StudentPermissions.FIND_STUDENTADDRESS }, student) && !restSecurity.hasPermission(new String[] { PersonPermissions.PERSON_OWNER }, student.getPerson() )) {
      return Response.status(Status.FORBIDDEN).build();
    }

    Address address = commonController.findAddressById(id);
    if (address == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (!address.getContactInfo().getId().equals(student.getContactInfo().getId())) {
      return Response.status(Status.NOT_FOUND).build();
    }

    return Response.ok(objectFactory.createModel(address)).build();
  }

  @Path("/students/{STUDENTID:[0-9]*}/addresses/{ID:[0-9]*}")
  @PUT
  @RESTPermit(handling = Handling.INLINE)
  // @RESTPermit (StudentPermissions.UPDATE_STUDENTADDRESS)
  public Response updateStudentAddress(
      @PathParam("STUDENTID") Long studentId,
      @PathParam("ID") Long id,
      fi.otavanopisto.pyramus.rest.model.Address body
      ) {
    Student student = studentController.findStudentById(studentId);
    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK)
      return Response.status(studentStatus).build();

    if (!restSecurity.hasPermission(
          new String[] { StudentPermissions.UPDATE_STUDENTADDRESS },
          student)
        && !restSecurity.hasPermission(
            new String[] { StudentPermissions.STUDENT_OWNER },
            student )) {
      return Response.status(Status.FORBIDDEN).build();
    }

    Address address = commonController.findAddressById(id);
    if (address == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (!address.getContactInfo().getId().equals(student.getContactInfo().getId())) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    ContactType contactType = commonController.findContactTypeById(body.getContactTypeId());
    
    address = studentController.updateStudentAddress(
        address,
        contactType,
        body.getDefaultAddress(),
        body.getName(),
        body.getStreetAddress(),
        body.getPostalCode(),
        body.getCity(),
        body.getCountry());

    return Response.ok(objectFactory.createModel(address)).build();
  }

  @Path("/students/{STUDENTID:[0-9]*}/addresses/{ID:[0-9]*}")
  @DELETE
  @RESTPermit(StudentPermissions.DELETE_STUDENTADDRESS)
  public Response deleteStudentAddress(@PathParam("STUDENTID") Long studentId, @PathParam("ID") Long id) {
    Student student = studentController.findStudentById(studentId);
    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK)
      return Response.status(studentStatus).build();

    Address address = commonController.findAddressById(id);
    if (address == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (!address.getContactInfo().getId().equals(student.getContactInfo().getId())) {
      return Response.status(Status.NOT_FOUND).build();
    }

    commonController.deleteAddress(address);

    return Response.noContent().build();
  }

  @Path("/students/{STUDENTID:[0-9]*}/phoneNumbers")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response listStudentPhoneNumbers(@PathParam("STUDENTID") Long studentId) {
    Student student = studentController.findStudentById(studentId);
    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK)
      return Response.status(studentStatus).build();

    if (!restSecurity.hasPermission(new String[] { StudentPermissions.LIST_STUDENTPHONENUMBERS }, student) && !restSecurity.hasPermission(new String[] { PersonPermissions.PERSON_OWNER }, student.getPerson() )) {
      return Response.status(Status.FORBIDDEN).build();
    }

    List<PhoneNumber> phoneNumbers = student.getContactInfo().getPhoneNumbers();
    if (phoneNumbers.isEmpty()) {
      return Response.noContent().build();
    }

    return Response.ok(objectFactory.createModel(phoneNumbers)).build();
  }

  @Path("/students/{STUDENTID:[0-9]*}/phoneNumbers")
  @POST
  @RESTPermit(handling = Handling.INLINE)
  // @RESTPermit (StudentPermissions.CREATE_STUDENTPHONENUMBER)
  public Response createStudentPhoneNumber(@PathParam("STUDENTID") Long studentId, fi.otavanopisto.pyramus.rest.model.PhoneNumber phoneNumber) {
    if (phoneNumber == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    Student student = studentController.findStudentById(studentId);
    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK)
      return Response.status(studentStatus).build();

    if (!restSecurity.hasPermission(new String[] { StudentPermissions.UPDATE_STUDENT, StudentPermissions.STUDENT_OWNER }, student, Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }
    Long contactTypeId = phoneNumber.getContactTypeId();
    Boolean defaultNumber = phoneNumber.getDefaultNumber();
    String number = phoneNumber.getNumber();

    if (contactTypeId == null || defaultNumber == null || StringUtils.isBlank(number)) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    ContactType contactType = commonController.findContactTypeById(contactTypeId);
    if (contactType == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    return Response.ok(objectFactory.createModel(studentController.addStudentPhoneNumber(student, contactType, number, defaultNumber))).build();
  }

  @Path("/students/{STUDENTID:[0-9]*}/phoneNumbers/{ID:[0-9]*}")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response findStudentPhoneNumber(@PathParam("STUDENTID") Long studentId, @PathParam("ID") Long id) {
    Student student = studentController.findStudentById(studentId);
    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK)
      return Response.status(studentStatus).build();

    if (!restSecurity.hasPermission(new String[] { StudentPermissions.FIND_STUDENTPHONENUMBER }, student) && !restSecurity.hasPermission(new String[] { PersonPermissions.PERSON_OWNER }, student.getPerson() )) {
      return Response.status(Status.FORBIDDEN).build();
    }

    PhoneNumber phoneNumber = commonController.findPhoneNumberById(id);
    if (phoneNumber == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (!phoneNumber.getContactInfo().getId().equals(student.getContactInfo().getId())) {
      return Response.status(Status.NOT_FOUND).build();
    }

    return Response.ok(objectFactory.createModel(phoneNumber)).build();
  }

  @Path("/students/{STUDENTID:[0-9]*}/phoneNumbers/{ID:[0-9]*}")
  @DELETE
  @RESTPermit(StudentPermissions.DELETE_STUDENTPHONENUMBER)
  public Response deleteStudentPhoneNumber(@PathParam("STUDENTID") Long studentId, @PathParam("ID") Long id) {
    Student student = studentController.findStudentById(studentId);
    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK)
      return Response.status(studentStatus).build();

    PhoneNumber phoneNumber = commonController.findPhoneNumberById(id);
    if (phoneNumber == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (!phoneNumber.getContactInfo().getId().equals(student.getContactInfo().getId())) {
      return Response.status(Status.NOT_FOUND).build();
    }

    commonController.deletePhoneNumber(phoneNumber);

    return Response.noContent().build();
  }

  @Path("/students/{STUDENTID:[0-9]*}/contactURLs")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response listStudentContactURLs(@PathParam("STUDENTID") Long studentId) {
    Student student = studentController.findStudentById(studentId);
    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK)
      return Response.status(studentStatus).build();

    if (!restSecurity.hasPermission(new String[] { StudentPermissions.LIST_STUDENTCONTACTURLS }, student) && !restSecurity.hasPermission(new String[] { PersonPermissions.PERSON_OWNER }, student.getPerson() )) {
      return Response.status(Status.FORBIDDEN).build();
    }

    List<ContactURL> contactUrls = student.getContactInfo().getContactURLs();
    if (contactUrls.isEmpty()) {
      return Response.noContent().build();
    }

    return Response.ok(objectFactory.createModel(contactUrls)).build();
  }

  @Path("/students/{STUDENTID:[0-9]*}/contactURLs")
  @POST
  @RESTPermit(StudentPermissions.CREATE_STUDENTCONTACTURL)
  public Response createStudentContactURL(@PathParam("STUDENTID") Long studentId, fi.otavanopisto.pyramus.rest.model.ContactURL contactURL) {
    if (contactURL == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    Long contactURLTypeId = contactURL.getContactURLTypeId();
    String url = contactURL.getUrl();

    if (contactURLTypeId == null || StringUtils.isBlank(url)) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    Student student = studentController.findStudentById(studentId);
    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK)
      return Response.status(studentStatus).build();

    ContactURLType contactURLType = commonController.findContactURLTypeById(contactURLTypeId);
    if (contactURLType == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    return Response.ok(objectFactory.createModel(studentController.addStudentContactURL(student, contactURLType, url))).build();
  }

  @Path("/students/{STUDENTID:[0-9]*}/contactURLs/{ID:[0-9]*}")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response findStudentContactURL(@PathParam("STUDENTID") Long studentId, @PathParam("ID") Long id) {
    Student student = studentController.findStudentById(studentId);
    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK)
      return Response.status(studentStatus).build();

    if (!restSecurity.hasPermission(new String[] { StudentPermissions.FIND_STUDENTCONTACTURL, StudentPermissions.STUDENT }, student) && !restSecurity.hasPermission(new String[] { PersonPermissions.PERSON_OWNER }, student.getPerson() )) {
      return Response.status(Status.FORBIDDEN).build();
    }

    ContactURL contactURL = commonController.findContactURLById(id);
    if (contactURL == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (!contactURL.getContactInfo().getId().equals(student.getContactInfo().getId())) {
      return Response.status(Status.NOT_FOUND).build();
    }

    return Response.ok(objectFactory.createModel(contactURL)).build();
  }

  @Path("/students/{STUDENTID:[0-9]*}/contactURLs/{ID:[0-9]*}")
  @DELETE
  @RESTPermit(StudentPermissions.DELETE_STUDENTCONTACTURL)
  public Response deleteStudentContactURL(@PathParam("STUDENTID") Long studentId, @PathParam("ID") Long id) {
    Student student = studentController.findStudentById(studentId);
    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK)
      return Response.status(studentStatus).build();

    ContactURL contactURL = commonController.findContactURLById(id);
    if (contactURL == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (!contactURL.getContactInfo().getId().equals(student.getContactInfo().getId())) {
      return Response.status(Status.NOT_FOUND).build();
    }

    commonController.deleteContactURL(contactURL);

    return Response.noContent().build();
  }

  @Path("/students/{STUDENTID:[0-9]*}/courses")
  @GET
  @RESTPermit(StudentPermissions.LIST_COURSESTUDENTSBYSTUDENT)
  public Response listCourseStudents(@PathParam("STUDENTID") Long studentId) {
    Student student = studentController.findStudentById(studentId);
    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK)
      return Response.status(studentStatus).build();

    List<fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent> courseStudents = courseController.listCourseStudentsByStudent(student);
    if (courseStudents.isEmpty()) {
      return Response.status(Status.NO_CONTENT).build();
    }

    List<fi.otavanopisto.pyramus.domainmodel.courses.Course> courses = new ArrayList<>();
    for (fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent courseStudent : courseStudents) {
      courses.add(courseStudent.getCourse());
    }

    return Response.status(Status.OK).entity(objectFactory.createModel(courses)).build();
  }
  
  @Path("/students/{STUDENTID:[0-9]*}/transferCredits")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response listStudentsTransferCredits(@PathParam("STUDENTID") Long studentId) {
    Student student = studentController.findStudentById(studentId);
    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK)
      return Response.status(studentStatus).build();

    if (!restSecurity.hasPermission(new String[] { StudentPermissions.LIST_STUDENT_TRANSFER_CREDITS, PersonPermissions.PERSON_OWNER }, student.getPerson(), Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }
    
    List<TransferCredit> transferCredits = studentController.listStudentTransferCredits(student);
    
    return Response.status(Status.OK).entity(objectFactory.createModel(transferCredits)).build();
  }

  /**
   * Checks for student to be non-null, not archived and find_student permission.
   * 
   * @param student
   * @return
   */
  private Status checkStudent(Student student) {
    if (student == null || student.getArchived()) {
      return Status.NOT_FOUND;
    }
    
    if (!restSecurity.hasPermission(new String[] { StudentPermissions.FIND_STUDENT, UserPermissions.USER_OWNER }, student, Style.OR)) {
      return Status.FORBIDDEN;
    }

    return Status.OK;
  }
}
