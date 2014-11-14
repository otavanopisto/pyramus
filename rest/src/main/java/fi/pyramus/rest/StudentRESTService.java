package fi.pyramus.rest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import fi.muikku.security.Permit;
import fi.muikku.security.Permit.Handle;
import fi.pyramus.domainmodel.base.Address;
import fi.pyramus.domainmodel.base.ContactType;
import fi.pyramus.domainmodel.base.ContactURL;
import fi.pyramus.domainmodel.base.ContactURLType;
import fi.pyramus.domainmodel.base.EducationType;
import fi.pyramus.domainmodel.base.Email;
import fi.pyramus.domainmodel.base.Language;
import fi.pyramus.domainmodel.base.Municipality;
import fi.pyramus.domainmodel.base.Nationality;
import fi.pyramus.domainmodel.base.Person;
import fi.pyramus.domainmodel.base.PhoneNumber;
import fi.pyramus.domainmodel.base.School;
import fi.pyramus.domainmodel.base.StudyProgramme;
import fi.pyramus.domainmodel.base.StudyProgrammeCategory;
import fi.pyramus.domainmodel.base.VariableType;
import fi.pyramus.domainmodel.students.Sex;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.domainmodel.students.StudentActivityType;
import fi.pyramus.domainmodel.students.StudentContactLogEntry;
import fi.pyramus.domainmodel.students.StudentContactLogEntryType;
import fi.pyramus.domainmodel.students.StudentEducationalLevel;
import fi.pyramus.domainmodel.students.StudentExaminationType;
import fi.pyramus.domainmodel.students.StudentGroup;
import fi.pyramus.domainmodel.students.StudentGroupStudent;
import fi.pyramus.domainmodel.students.StudentStudyEndReason;
import fi.pyramus.domainmodel.users.UserVariable;
import fi.pyramus.domainmodel.users.UserVariableKey;
import fi.pyramus.rest.controller.CommonController;
import fi.pyramus.rest.controller.CourseController;
import fi.pyramus.rest.controller.LanguageController;
import fi.pyramus.rest.controller.MunicipalityController;
import fi.pyramus.rest.controller.NationalityController;
import fi.pyramus.rest.controller.PersonController;
import fi.pyramus.rest.controller.SchoolController;
import fi.pyramus.rest.controller.StudentActivityTypeController;
import fi.pyramus.rest.controller.StudentContactLogEntryController;
import fi.pyramus.rest.controller.StudentController;
import fi.pyramus.rest.controller.StudentEducationalLevelController;
import fi.pyramus.rest.controller.StudentExaminationTypeController;
import fi.pyramus.rest.controller.StudentGroupController;
import fi.pyramus.rest.controller.StudentStudyEndReasonController;
import fi.pyramus.rest.controller.StudyProgrammeCategoryController;
import fi.pyramus.rest.controller.StudyProgrammeController;
import fi.pyramus.rest.controller.UserController;
import fi.pyramus.rest.controller.permissions.LanguagePermissions;
import fi.pyramus.rest.controller.permissions.MunicipalityPermissions;
import fi.pyramus.rest.controller.permissions.NationalityPermissions;
import fi.pyramus.rest.controller.permissions.PersonPermissions;
import fi.pyramus.rest.controller.permissions.StudentActivityTypePermissions;
import fi.pyramus.rest.controller.permissions.StudentContactLogEntryPermissions;
import fi.pyramus.rest.controller.permissions.StudentEducationalLevelPermissions;
import fi.pyramus.rest.controller.permissions.StudentExaminationTypePermissions;
import fi.pyramus.rest.controller.permissions.StudentGroupPermissions;
import fi.pyramus.rest.controller.permissions.StudentPermissions;
import fi.pyramus.rest.controller.permissions.StudentStudyEndReasonPermissions;
import fi.pyramus.rest.controller.permissions.StudyProgrammeCategoryPermissions;
import fi.pyramus.rest.controller.permissions.StudyProgrammePermissions;
import fi.pyramus.rest.controller.permissions.UserPermissions;

@Path("/students")
@Produces("application/json")
@Consumes("application/json")
@Stateful
@RequestScoped
public class StudentRESTService extends AbstractRESTService {

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
  private ObjectFactory objectFactory;

  @Path("/languages")
  @POST
  @Permit (handle = Handle.EXCEPTION, value = LanguagePermissions.CREATE_LANGUAGE)
  public Response createLanguage(fi.pyramus.rest.model.Language entity) {
    String name = entity.getName();
    String code = entity.getCode();
    
    if (StringUtils.isBlank(name) || StringUtils.isBlank(code)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    return Response
        .ok(objectFactory.createModel(languageController.createLanguage(name, code)))
        .build();
  }

  @Path("/languages")
  @GET
  @Permit (handle = Handle.EXCEPTION, value = LanguagePermissions.LIST_LANGUAGES)
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
  @Permit (handle = Handle.EXCEPTION, value = LanguagePermissions.FIND_LANGUAGE)
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
  @Permit (handle = Handle.EXCEPTION, value = LanguagePermissions.UPDATE_LANGUAGE)
  public Response updateLanguage(@PathParam("ID") Long id, fi.pyramus.rest.model.Language entity) {
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
  @Permit (handle = Handle.EXCEPTION, value = LanguagePermissions.DELETE_LANGUAGE)
  public Response deleteLanguage(@PathParam("ID") Long id, @DefaultValue ("false") @QueryParam ("permanent") Boolean permanent) {
    Language language = languageController.findLanguageById(id);
    if (language == null) {
      return Response.status(Status.NOT_FOUND).build();
    }    
    
    if (permanent) {
      languageController.deleteLanguage(language);
    } else {
      languageController.archiveLanguage(language, getLoggedUser());
    }
    
    return Response.noContent().build();
  }

  @Path("/municipalities")
  @POST
  @Permit (handle = Handle.EXCEPTION, value = MunicipalityPermissions.CREATE_MUNICIPALITY)
  public Response createMunicipality(fi.pyramus.rest.model.Municipality entity) {
    String name = entity.getName();
    String code = entity.getCode();
    
    if (StringUtils.isBlank(name) || StringUtils.isBlank(code)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    return Response
        .ok(objectFactory.createModel(municipalityController.createMunicipality(name, code)))
        .build();
  }

  @Path("/municipalities")
  @GET
  @Permit (handle = Handle.EXCEPTION, value = MunicipalityPermissions.LIST_MUNICIPALITIES)
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
  @Permit (handle = Handle.EXCEPTION, value = MunicipalityPermissions.FIND_MUNICIPALITY)
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
  @Permit (handle = Handle.EXCEPTION, value = MunicipalityPermissions.UPDATE_MUNICIPALITY)
  public Response updateMunicipality(@PathParam("ID") Long id, fi.pyramus.rest.model.Municipality entity) {
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
  @Permit (handle = Handle.EXCEPTION, value = MunicipalityPermissions.DELETE_MUNICIPALITY)
  public Response deleteMunicipality(@PathParam("ID") Long id, @DefaultValue ("false") @QueryParam ("permanent") Boolean permanent) {
    Municipality municipality = municipalityController.findMunicipalityById(id);
    if (municipality == null) {
      return Response.status(Status.NOT_FOUND).build();
    }    
    
    if (permanent) {
      municipalityController.deleteMunicipality(municipality);
    } else {
      municipalityController.archiveMunicipality(municipality, getLoggedUser());
    }
    
    return Response.noContent().build();
  }

  @Path("/nationalities")
  @POST
  @Permit (handle = Handle.EXCEPTION, value = NationalityPermissions.CREATE_NATIONALITY)
  public Response createNationality(fi.pyramus.rest.model.Nationality entity) {
    String name = entity.getName();
    String code = entity.getCode();
    
    if (StringUtils.isBlank(name) || StringUtils.isBlank(code)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    return Response
        .ok(objectFactory.createModel(nationalityController.createNationality(name, code)))
        .build();
  }

  @Path("/nationalities")
  @GET
  @Permit (handle = Handle.EXCEPTION, value = NationalityPermissions.LIST_NATIONALITIES)
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
  @Permit (handle = Handle.EXCEPTION, value = NationalityPermissions.FIND_NATIONALITY)
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
  @Permit (handle = Handle.EXCEPTION, value = NationalityPermissions.UPDATE_NATIONALITY)
  public Response updateNationality(@PathParam("ID") Long id, fi.pyramus.rest.model.Nationality entity) {
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
  @Permit (handle = Handle.EXCEPTION, value = NationalityPermissions.DELETE_NATIONALITY)
  public Response deleteNationality(@PathParam("ID") Long id, @DefaultValue ("false") @QueryParam ("permanent") Boolean permanent) {
    Nationality nationality = nationalityController.findNationalityById(id);
    if (nationality == null) {
      return Response.status(Status.NOT_FOUND).build();
    }    
    
    if (permanent) {
      nationalityController.deleteNationality(nationality);
    } else {
      nationalityController.archiveNationality(nationality, getLoggedUser());
    }
    
    return Response.noContent().build();
  }

  @Path("/activityTypes")
  @POST
  @Permit (handle = Handle.EXCEPTION, value = StudentActivityTypePermissions.CREATE_STUDENTACTIVITYTYPE)
  public Response createStudentActivityType(fi.pyramus.rest.model.StudentActivityType entity) {
    String name = entity.getName();
    
    if (StringUtils.isBlank(name)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    return Response
        .ok(objectFactory.createModel(studentActivityTypeController.createStudentActivityType(name)))
        .build();
  }

  @Path("/activityTypes")
  @GET
  @Permit (handle = Handle.EXCEPTION, value = StudentActivityTypePermissions.LIST_STUDENTACTIVITYTYPES)
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
  @Permit (handle = Handle.EXCEPTION, value = StudentActivityTypePermissions.FIND_STUDENTACTIVITYTYPE)
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
  @Permit (handle = Handle.EXCEPTION, value = StudentActivityTypePermissions.UPDATE_STUDENTACTIVITYTYPE)
  public Response updateStudentActivityType(@PathParam("ID") Long id, fi.pyramus.rest.model.StudentActivityType entity) {
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
  @Permit (handle = Handle.EXCEPTION, value = StudentActivityTypePermissions.DELETE_STUDENTACTIVITYTYPE)
  public Response deleteStudentActivityType(@PathParam("ID") Long id, @DefaultValue ("false") @QueryParam ("permanent") Boolean permanent) {
    StudentActivityType studentActivityType = studentActivityTypeController.findStudentActivityTypeById(id);
    if (studentActivityType == null) {
      return Response.status(Status.NOT_FOUND).build();
    }    
    
    if (permanent) {
      studentActivityTypeController.deleteStudentActivityType(studentActivityType);
    } else {
      studentActivityTypeController.archiveStudentActivityType(studentActivityType, getLoggedUser());
    }
    
    return Response.noContent().build();
  }

  @Path("/educationalLevels")
  @POST
  @Permit (handle = Handle.EXCEPTION, value = StudentEducationalLevelPermissions.CREATE_STUDENTEDUCATIONALLEVEL)
  public Response createStudentEducationalLevel(fi.pyramus.rest.model.StudentEducationalLevel entity) {
    String name = entity.getName();
    
    if (StringUtils.isBlank(name)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    return Response
        .ok(objectFactory.createModel(studentEducationalLevelController.createStudentEducationalLevel(name)))
        .build();
  }

  @Path("/educationalLevels")
  @GET
  @Permit (handle = Handle.EXCEPTION, value = StudentEducationalLevelPermissions.LIST_STUDENTEDUCATIONALLEVELS)
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
  @Permit (handle = Handle.EXCEPTION, value = StudentEducationalLevelPermissions.FIND_STUDENTEDUCATIONALLEVEL)
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
  @Permit (handle = Handle.EXCEPTION, value = StudentEducationalLevelPermissions.UPDATE_STUDENTEDUCATIONALLEVEL)
  public Response updateStudentEducationalLevel(@PathParam("ID") Long id, fi.pyramus.rest.model.StudentEducationalLevel entity) {
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
    
    return Response.ok().entity(objectFactory.createModel(studentEducationalLevelController.updateStudentEducationalLevel(studentEducationalLevel, name))).build();
  }
      
  @Path("/educationalLevels/{ID:[0-9]*}")
  @DELETE
  @Permit (handle = Handle.EXCEPTION, value = StudentEducationalLevelPermissions.DELETE_STUDENTEDUCATIONALLEVEL)
  public Response deleteStudentEducationalLevel(@PathParam("ID") Long id, @DefaultValue ("false") @QueryParam ("permanent") Boolean permanent) {
    StudentEducationalLevel studentEducationalLevel = studentEducationalLevelController.findStudentEducationalLevelById(id);
    if (studentEducationalLevel == null) {
      return Response.status(Status.NOT_FOUND).build();
    }    
    
    if (permanent) {
      studentEducationalLevelController.deleteStudentEducationalLevel(studentEducationalLevel);
    } else {
      studentEducationalLevelController.archiveStudentEducationalLevel(studentEducationalLevel, getLoggedUser());
    }
    
    return Response.noContent().build();
  }

  @Path("/examinationTypes")
  @POST
  @Permit (handle = Handle.EXCEPTION, value = StudentExaminationTypePermissions.CREATE_STUDENTEXAMINATIONTYPE)
  public Response createStudentExaminationType(fi.pyramus.rest.model.StudentExaminationType entity) {
    String name = entity.getName();
    
    if (StringUtils.isBlank(name)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    return Response
        .ok(objectFactory.createModel(studentExaminationTypeController.createStudentExaminationType(name)))
        .build();
  }

  @Path("/examinationTypes")
  @GET
  @Permit (handle = Handle.EXCEPTION, value = StudentExaminationTypePermissions.LIST_STUDENTEXAMINATIONTYPES)
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
  @Permit (handle = Handle.EXCEPTION, value = StudentExaminationTypePermissions.FIND_STUDENTEXAMINATIONTYPE)
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
  @Permit (handle = Handle.EXCEPTION, value = StudentExaminationTypePermissions.UPDATE_STUDENTEXAMINATIONTYPE)
  public Response updateStudentExaminationType(@PathParam("ID") Long id, fi.pyramus.rest.model.StudentExaminationType entity) {
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
  @Permit (handle = Handle.EXCEPTION, value = StudentExaminationTypePermissions.DELETE_STUDENTEXAMINATIONTYPE)
  public Response deleteStudentExaminationType(@PathParam("ID") Long id, @DefaultValue ("false") @QueryParam ("permanent") Boolean permanent) {
    StudentExaminationType studentExaminationType = studentExaminationTypeController.findStudentExaminationTypeById(id);
    if (studentExaminationType == null) {
      return Response.status(Status.NOT_FOUND).build();
    }    
    
    if (permanent) {
      studentExaminationTypeController.deleteStudentExaminationType(studentExaminationType);
    } else {
      studentExaminationTypeController.archiveStudentExaminationType(studentExaminationType, getLoggedUser());
    }
    
    return Response.noContent().build();
  }

  @Path("/studyProgrammeCategories")
  @POST
  @Permit (handle = Handle.EXCEPTION, value = StudyProgrammeCategoryPermissions.CREATE_STUDYPROGRAMMECATEGORY)
  public Response createStudyProgrammeCategory(fi.pyramus.rest.model.StudyProgrammeCategory entity) {
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
    
    return Response
        .ok(objectFactory.createModel(studyProgrammeCategoryController.createStudyProgrammeCategory(name, educationType)))
        .build();
  }

  @Path("/studyProgrammeCategories")
  @GET
  @Permit (handle = Handle.EXCEPTION, value = StudyProgrammeCategoryPermissions.LIST_STUDYPROGRAMMECATEGORIES)
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
  @Permit (handle = Handle.EXCEPTION, value = StudyProgrammeCategoryPermissions.FIND_STUDYPROGRAMMECATEGORY)
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
  @Permit (handle = Handle.EXCEPTION, value = StudyProgrammeCategoryPermissions.UPDATE_STUDYPROGRAMMECATEGORY)
  public Response updateStudyProgrammeCategory(@PathParam("ID") Long id, fi.pyramus.rest.model.StudyProgrammeCategory entity) {
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
    
    return Response.ok().entity(objectFactory.createModel(studyProgrammeCategoryController.updateStudyProgrammeCategory(studyProgrammeCategory, name, educationType))).build();
  }
      
  @Path("/studyProgrammeCategories/{ID:[0-9]*}")
  @DELETE
  @Permit (handle = Handle.EXCEPTION, value = StudyProgrammeCategoryPermissions.DELETE_STUDYPROGRAMMECATEGORY)
  public Response deleteStudyProgrammeCategory(@PathParam("ID") Long id, @DefaultValue ("false") @QueryParam ("permanent") Boolean permanent) {
    StudyProgrammeCategory studyProgrammeCategory = studyProgrammeCategoryController.findStudyProgrammeCategoryById(id);
    if (studyProgrammeCategory == null) {
      return Response.status(Status.NOT_FOUND).build();
    }    
    
    if (permanent) {
      studyProgrammeCategoryController.deleteStudyProgrammeCategory(studyProgrammeCategory);
    } else {
      studyProgrammeCategoryController.archiveStudyProgrammeCategory(studyProgrammeCategory, getLoggedUser());
    }
    
    return Response.noContent().build();
  }
  
  @Path("/studyProgrammes")
  @POST
  @Permit (handle = Handle.EXCEPTION, value = StudyProgrammeCategoryPermissions.CREATE_STUDYPROGRAMMECATEGORY)
  public Response createStudyProgramme(fi.pyramus.rest.model.StudyProgramme entity) {
    String name = entity.getName();
    String code = entity.getCode();
    Long categoryId = entity.getCategoryId();
    
    if (StringUtils.isBlank(name) || StringUtils.isBlank(code) || (categoryId == null)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    StudyProgrammeCategory programmeCategory = studyProgrammeCategoryController.findStudyProgrammeCategoryById(categoryId);
    if (programmeCategory == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    return Response
        .ok(objectFactory.createModel(studyProgrammeController.createStudyProgramme(name, code, programmeCategory)))
        .build();
  }

  @Path("/studyProgrammes")
  @GET
  @Permit (handle = Handle.EXCEPTION, value = StudyProgrammePermissions.LIST_STUDYPROGRAMMES)
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
  @Permit (handle = Handle.EXCEPTION, value = StudyProgrammePermissions.FIND_STUDYPROGRAMME)
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
    
    return Response.ok(objectFactory.createModel(studyProgramme))
        .cacheControl(cacheControl)
        .tag(tag)
        .build();
  }

  @Path("/studyProgrammes/{ID:[0-9]*}")
  @PUT
  @Permit (handle = Handle.EXCEPTION, value = StudyProgrammePermissions.UPDATE_STUDYPROGRAMME)
  public Response updateStudyProgramme(@PathParam("ID") Long id, fi.pyramus.rest.model.StudyProgramme entity) {
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
    
    if (StringUtils.isBlank(name) || StringUtils.isBlank(code) || (categoryId == null)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    StudyProgrammeCategory programmeCategory = studyProgrammeCategoryController.findStudyProgrammeCategoryById(categoryId);
    if (programmeCategory == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    return Response.ok().entity(objectFactory.createModel(studyProgrammeController.updateStudyProgramme(studyProgramme, name, code, programmeCategory))).build();
  }
      
  @Path("/studyProgrammes/{ID:[0-9]*}")
  @DELETE
  @Permit (handle = Handle.EXCEPTION, value = StudyProgrammePermissions.DELETE_STUDYPROGRAMME)
  public Response deleteStudyProgramme(@PathParam("ID") Long id, @DefaultValue ("false") @QueryParam ("permanent") Boolean permanent) {
    StudyProgramme studyProgramme = studyProgrammeController.findStudyProgrammeById(id);
    if (studyProgramme == null) {
      return Response.status(Status.NOT_FOUND).build();
    }    
    
    if (permanent) {
      studyProgrammeController.deleteStudyProgramme(studyProgramme);
    } else {
      studyProgrammeController.archiveStudyProgramme(studyProgramme, getLoggedUser());
    }
    
    return Response.noContent().build();
  }

  @Path("/studentGroups")
  @POST
  @Permit (handle = Handle.EXCEPTION, value = StudentGroupPermissions.CREATE_STUDENTGROUP)
  public Response createStudentGroup(fi.pyramus.rest.model.StudentGroup entity) {
    String name = entity.getName();
    String description = entity.getDescription();
    DateTime beginDate = entity.getBeginDate();
    
    if (StringUtils.isBlank(name)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    StudentGroup studentGroup = studentGroupController.createStudentGroup(name, description, toDate(beginDate), getLoggedUser());
    
    for (String tag : entity.getTags()) {
      studentGroupController.createStudentGroupTag(studentGroup, tag);
    }
    
    return Response.ok(objectFactory.createModel(studentGroup)).build();
  }
  
  @Path("/studentGroups")
  @GET
  @Permit (handle = Handle.EXCEPTION, value = StudentGroupPermissions.LIST_STUDENTGROUPS)
  public Response findStudentGroups(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<StudentGroup> studentGroups;
    if (filterArchived) {
      studentGroups = studentGroupController.listUnarchivedStudentGroups();
    } else {
      studentGroups = studentGroupController.listStudentGroups();
    }
    
    
    if (studentGroups.isEmpty()) {
      return Response.noContent().build();
    }
    
    return Response.ok(objectFactory.createModel(studentGroups)).build();
  }
  
  @Path("/studentGroups/{ID:[0-9]*}")
  @GET
  @Permit (handle = Handle.EXCEPTION, value = StudentGroupPermissions.FIND_STUDENTGROUP)
  public Response findStudentGroup(@PathParam("ID") Long id) {
    StudentGroup studentGroup = studentGroupController.findStudentGroupById(id);
    if (studentGroup == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (studentGroup.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    return Response.ok(objectFactory.createModel(studentGroup)).build();
  }

  @Path("/studentGroups/{ID:[0-9]*}")
  @PUT
  @Permit (handle = Handle.EXCEPTION, value = StudentGroupPermissions.UPDATE_STUDENTGROUP)
  public Response updateStudentGroup(@PathParam("ID") Long id, fi.pyramus.rest.model.StudentGroup entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    String name = entity.getName();
    String description = entity.getDescription();
    DateTime beginDate = entity.getBeginDate();
    
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
    
    studentGroupController.updateStudentGroup(studentGroup, name, description, toDate(beginDate), getLoggedUser());
    studentGroupController.updateStudentGroupTags(studentGroup, entity.getTags());
    
    return Response.ok(objectFactory.createModel(studentGroup)).build();
  }
  
  @Path("/studentGroups/{ID:[0-9]*}")
  @DELETE
  @Permit (handle = Handle.EXCEPTION, value = StudentGroupPermissions.DELETE_STUDENTGROUP)
  public Response deleteStudentGroup(@PathParam("ID") Long id, @DefaultValue("false") @QueryParam("permanent") Boolean permanent) {
    StudentGroup studentGroup = studentGroupController.findStudentGroupById(id);
    if (studentGroup == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (permanent) {
      studentGroupController.deleteStudentGroup(studentGroup);
    } else {
      studentGroupController.archiveStudentGroup(studentGroup, getLoggedUser());
    }

    return Response.noContent().build();
  }
  
  @Path("/studentGroups/{ID:[0-9]*}/students")
  @POST
  @Permit (handle = Handle.EXCEPTION, value = StudentGroupPermissions.CREATE_STUDENTGROUPSTUDENT)
  public Response createStudentGroupStudent(@PathParam("ID") Long id, fi.pyramus.rest.model.StudentGroupStudent entity) {
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
    
    StudentGroupStudent studentGroupStudent = studentGroupController.createStudentGroupStudent(studentGroup, student, getLoggedUser());
    
    return Response.ok(objectFactory.createModel(studentGroupStudent)).build();
  }

  @Path("/studentGroups/{ID:[0-9]*}/students")
  @GET
  @Permit (handle = Handle.EXCEPTION, value = StudentGroupPermissions.LIST_STUDENTGROUPSTUDENTS)
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
        return o1.getId().compareTo(o2.getId());
      }
    });
    
    return Response.ok(objectFactory.createModel(studentGroupStudents)).build();
  }
  
  @Path("/studentGroups/{GROUPID:[0-9]*}/students/{ID:[0-9]*}")
  @GET
  @Permit (handle = Handle.EXCEPTION, value = StudentGroupPermissions.FIND_STUDENTGROUPSTUDENT)
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
  @Permit (handle = Handle.EXCEPTION, value = StudentGroupPermissions.DELETE_STUDENTGROUPSTUDENT)
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
  @Permit (handle = Handle.EXCEPTION, value = StudentStudyEndReasonPermissions.CREATE_STUDENTSTUDYENDREASON)
  public Response createStudentStudyEndReason(fi.pyramus.rest.model.StudentStudyEndReason entity) {
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
  @Permit (handle = Handle.EXCEPTION, value = StudentStudyEndReasonPermissions.LIST_STUDENTSTUDYENDREASONS)
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
  @Permit (handle = Handle.EXCEPTION, value = StudentStudyEndReasonPermissions.FIND_STUDENTSTUDYENDREASON)
  public Response findStudentStudyEndReasonById(@PathParam("ID") Long id) {
    StudentStudyEndReason endReason = studentStudyEndReasonController.findStudentStudyEndReasonById(id);
    if (endReason == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    return Response.ok(objectFactory.createModel(endReason)).build();
  }

  @Path("/studyEndReasons/{ID:[0-9]*}")
  @PUT
  @Permit (handle = Handle.EXCEPTION, value = StudentStudyEndReasonPermissions.UPDATE_STUDENTSTUDYENDREASON)
  public Response updateStudentStudyEndReason(@PathParam("ID") Long id, fi.pyramus.rest.model.StudentStudyEndReason entity) {
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
  @Permit (handle = Handle.EXCEPTION, value = StudentStudyEndReasonPermissions.DELETE_STUDENTSTUDYENDREASON)
  public Response deleteStudentStudyEndReason(@PathParam("ID") Long id) {
    StudentStudyEndReason studyEndReason = studentStudyEndReasonController.findStudentStudyEndReasonById(id);
    if (studyEndReason == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    studentStudyEndReasonController.deleteStudentStudyEndReason(studyEndReason);
    
    return Response.noContent().build();
  }
  
  @Path("/persons")
  @POST
  @Permit (handle = Handle.EXCEPTION, value = PersonPermissions.CREATE_PERSON)
  public Response createPerson(fi.pyramus.rest.model.Person entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    if ((entity.getSex() == null) || (entity.getSecureInfo() == null)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    Sex sex = null;
    
    switch (entity.getSex()) {
      case FEMALE:
        sex = Sex.FEMALE;
      break;
      case MALE:
        sex = Sex.MALE;
      break;
    }
    
    return Response.ok(objectFactory.createModel(personController.createPerson(toDate(entity.getBirthday()), 
        entity.getSocialSecurityNumber(), sex, entity.getBasicInfo(), entity.getSecureInfo()))).build();
  }
  
  @Path("/persons")
  @GET
  @Permit (handle = Handle.EXCEPTION, value = PersonPermissions.LIST_PERSONS)
  public Response findPersons(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<Person> persons;
    if (filterArchived) {
      persons = personController.findUnarchivedPersons();
    } else {
      persons = personController.findPersons();
    }
    
    return Response.ok(objectFactory.createModel(persons)).build();
  }

  @Path("/persons/{ID:[0-9]*}")
  @GET
  @Permit (handle = Handle.EXCEPTION, value = PersonPermissions.FIND_PERSON)
  public Response findPersonById(@PathParam("ID") Long id) {
    Person person = personController.findPersonById(id);
    if (person == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    return Response.ok(objectFactory.createModel(person)).build();
  }

  @Path("/persons/{ID:[0-9]*}")
  @PUT
  @Permit (handle = Handle.EXCEPTION, value = PersonPermissions.UPDATE_PERSON)
  public Response updatePerson(@PathParam("ID") Long id, fi.pyramus.rest.model.Person entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    if ((entity.getSex() == null) || (entity.getSecureInfo() == null)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    Sex sex = null;
    
    switch (entity.getSex()) {
      case FEMALE:
        sex = Sex.FEMALE;
      break;
      case MALE:
        sex = Sex.MALE;
      break;
    }
    
    Person person = personController.findPersonById(id);
    if (person == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    return Response.ok(objectFactory.createModel(personController.updatePerson(person, toDate(entity.getBirthday()), 
        entity.getSocialSecurityNumber(), sex, entity.getBasicInfo(), entity.getSecureInfo()))).build();
  }

  @Path("/persons/{ID:[0-9]*}")
  @DELETE
  @Permit (handle = Handle.EXCEPTION, value = PersonPermissions.DELETE_PERSON)
  public Response deletePerson(@PathParam("ID") Long id) {
    Person person = personController.findPersonById(id);
    if (person == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    personController.deletePerson(person);
    
    return Response.noContent().build();
  }

  @Path("/persons/{ID:[0-9]*}/students")
  @GET
  @Permit (handle = Handle.EXCEPTION, value = StudentPermissions.LIST_STUDENTSBYPERSON)
  public Response listStudentsByPerson(@PathParam("ID") Long id) {
    Person person = personController.findPersonById(id);
    if (person == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    return Response.ok(objectFactory.createModel(studentController.listStudentByPerson(person))).build();
  }
  
  @Path("/students")
  @POST
  @Permit (handle = Handle.EXCEPTION, value = StudentPermissions.CREATE_STUDENT)
  public Response createStudent(fi.pyramus.rest.model.Student entity) {
    Long personId = entity.getPersonId();
    Long studyProgrammeId = entity.getStudyProgrammeId();
    String firstName = entity.getFirstName();
    String lastName = entity.getLastName();
    Boolean lodging = entity.getLodging();
    
    if ((personId == null)||(studyProgrammeId == null)||(lodging == null)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    if (StringUtils.isBlank(firstName)||StringUtils.isBlank(lastName)) {
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
    
    StudentActivityType activityType = entity.getActivityTypeId() != null ? studentActivityTypeController.findStudentActivityTypeById(entity.getActivityTypeId()) : null;
    StudentExaminationType examinationType = entity.getExaminationTypeId() != null ? studentExaminationTypeController.findStudentExaminationTypeById(entity.getExaminationTypeId()) : null;
    StudentEducationalLevel educationalLevel = entity.getEducationalLevelId() != null ? studentEducationalLevelController.findStudentEducationalLevelById(entity.getEducationalLevelId()) : null;
    Nationality nationality = entity.getNationalityId() != null ? nationalityController.findNationalityById(entity.getNationalityId()) : null;
    Municipality municipality = entity.getMunicipalityId() != null ? municipalityController.findMunicipalityById(entity.getMunicipalityId()) : null;
    Language language = entity.getLanguageId() != null ? languageController.findLanguageById(entity.getLanguageId()) : null;
    School school = entity.getSchoolId() != null ? schoolController.findSchoolById(entity.getSchoolId()) : null;
    StudentStudyEndReason studyEndReason = entity.getStudyEndReasonId() != null ? studentStudyEndReasonController.findStudentStudyEndReasonById(entity.getStudyEndReasonId()) : null;
    
    Student student = studentController.createStudent(person, firstName, lastName, entity.getNickname(), entity.getAdditionalInfo(), 
        toDate(entity.getStudyTimeEnd()), activityType, examinationType, educationalLevel, entity.getEducation(), nationality,
        municipality, language, school, studyProgramme, entity.getPreviousStudies(), toDate(entity.getStudyStartDate()),
        toDate(entity.getStudyEndDate()), studyEndReason, entity.getStudyEndText(), lodging);
    userController.updateUserVariables(student, entity.getVariables());
    studentController.updateStudentTags(student, entity.getTags());
    studentController.updateStudentAdditionalContactInfo(student, entity.getAdditionalContactInfo());

    return Response.ok(objectFactory.createModel(student)).build();
  }
  
  @Path("/students")
  @GET
  @Permit (handle = Handle.EXCEPTION, value = StudentPermissions.LIST_STUDENTS)
  public Response listStudents(@QueryParam ("firstResult") Integer firstResult, @QueryParam ("maxResults") Integer maxResults, @QueryParam ("email") String email, @DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<Student> students;
    
    if (StringUtils.isNotBlank(email)) {
      if (!filterArchived) {
        students = studentController.listStudentsByEmail(email, firstResult, maxResults);
      } else {
        students = studentController.listStudentsByEmailAndArchived(email, Boolean.FALSE, firstResult, maxResults);
      }
    } else {
      if (filterArchived) {
        students = studentController.listUnarchivedStudents(firstResult, maxResults);
      } else {
        students = studentController.listStudents(firstResult, maxResults);
      }
    }
    
    if (students.isEmpty()) {
      return Response.noContent().build();
    }

    return Response.ok(objectFactory.createModel(students)).build();
  }
  
  @Path("/students/{ID:[0-9]*}")
  @GET
  @Permit (handle = Handle.EXCEPTION, value = StudentPermissions.FIND_STUDENT)
  public Response findStudentById(@PathParam("ID") Long id, @Context Request request) {
    Student student = studentController.findStudentById(id);
    if (student == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (student.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    EntityTag tag = new EntityTag(DigestUtils.md5Hex(String.valueOf(student.getVersion())));

    ResponseBuilder builder = request.evaluatePreconditions(tag);
    if (builder != null) {
      return builder.build();
    }
    
    CacheControl cacheControl = new CacheControl();
    cacheControl.setMustRevalidate(true);
    
    return Response.ok(objectFactory.createModel(student))
        .cacheControl(cacheControl)
        .tag(tag)
        .build();
  }


  @Path("/students/{ID:[0-9]*}")
  @PUT
  @Permit (handle = Handle.EXCEPTION, value = StudentPermissions.UPDATE_STUDENT)
  public Response updateStudent(@PathParam("ID") Long id, fi.pyramus.rest.model.Student entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    Long personId = entity.getPersonId();
    Long studyProgrammeId = entity.getStudyProgrammeId();
    String firstName = entity.getFirstName();
    String lastName = entity.getLastName();
    Boolean lodging = entity.getLodging();
    
    if ((personId == null)||(studyProgrammeId == null)||(lodging == null)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    if (StringUtils.isBlank(firstName)||StringUtils.isBlank(lastName)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    Student student = studentController.findStudentById(id);
    if (student == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (student.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    Person person = personController.findPersonById(personId);
    if (person == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    StudyProgramme studyProgramme = studyProgrammeController.findStudyProgrammeById(studyProgrammeId);
    if (studyProgramme == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    StudentActivityType activityType = entity.getActivityTypeId() != null ? studentActivityTypeController.findStudentActivityTypeById(entity.getActivityTypeId()) : null;
    StudentExaminationType examinationType = entity.getExaminationTypeId() != null ? studentExaminationTypeController.findStudentExaminationTypeById(entity.getExaminationTypeId()) : null;
    StudentEducationalLevel educationalLevel = entity.getEducationalLevelId() != null ? studentEducationalLevelController.findStudentEducationalLevelById(entity.getEducationalLevelId()) : null;
    Nationality nationality = entity.getNationalityId() != null ? nationalityController.findNationalityById(entity.getNationalityId()) : null;
    Municipality municipality = entity.getMunicipalityId() != null ? municipalityController.findMunicipalityById(entity.getMunicipalityId()) : null;
    Language language = entity.getLanguageId() != null ? languageController.findLanguageById(entity.getLanguageId()) : null;
    School school = entity.getSchoolId() != null ? schoolController.findSchoolById(entity.getSchoolId()) : null;
    StudentStudyEndReason studyEndReason = entity.getStudyEndReasonId() != null ? studentStudyEndReasonController.findStudentStudyEndReasonById(entity.getStudyEndReasonId()) : null;
    
    studentController.updateStudent(student, firstName, lastName, entity.getNickname(), entity.getAdditionalInfo(), 
        toDate(entity.getStudyTimeEnd()), activityType, examinationType, educationalLevel, entity.getEducation(), nationality,
        municipality, language, school, studyProgramme, entity.getPreviousStudies(), toDate(entity.getStudyStartDate()),
        toDate(entity.getStudyEndDate()), studyEndReason, entity.getStudyEndText(), lodging);
    
    studentController.updateStudentPerson(student, person);
    userController.updateUserVariables(student, entity.getVariables());
    studentController.updateStudentTags(student, entity.getTags());
    studentController.updateStudentAdditionalContactInfo(student, entity.getAdditionalContactInfo());
    
    return Response.ok(objectFactory.createModel(student)).build();
  }

  @Path("/students/{ID:[0-9]*}")
  @DELETE
  @Permit (handle = Handle.EXCEPTION, value = StudentPermissions.DELETE_STUDENT)
  public Response deleteStudent(@PathParam("ID") Long id, @DefaultValue ("false") @QueryParam ("permanent") Boolean permanent) {
    Student student = studentController.findStudentById(id);
    if (student == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (permanent) {
      List<UserVariable> userVariables = userController.listUserVariablesByUser(student);
      for (UserVariable userVariable : userVariables) {
        userController.deleteUserVariable(userVariable);
      }
      
      studentController.deleteStudent(student);
    } else {
      studentController.archiveStudent(student, getLoggedUser());
    }
    
    return Response.noContent().build();
  }

  @Path("/students/{ID:[0-9]*}/contactLogEntries")
  @POST
  @Permit (handle = Handle.EXCEPTION, value = StudentContactLogEntryPermissions.CREATE_STUDENTCONTACTLOGENTRY)
  public Response createStudentContactLogEntry(@PathParam("ID") Long id, fi.pyramus.rest.model.StudentContactLogEntry entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    Student student = studentController.findStudentById(id);
    if (student == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (student.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    StudentContactLogEntryType type = entity.getType() != null ? StudentContactLogEntryType.valueOf(entity.getType().name()) : null;
    StudentContactLogEntry contactLogEntry = studentContactLogEntryController.createContactLogEntry(student, type, entity.getText(), toDate(entity.getEntryDate()), entity.getCreatorName());
    
    return Response.ok(objectFactory.createModel(contactLogEntry)).build();
  }

  @Path("/students/{STUDENTID:[0-9]*}/contactLogEntries")
  @GET
  @Permit (handle = Handle.EXCEPTION, value = StudentContactLogEntryPermissions.LIST_STUDENTCONTACTLOGENTRIES)
  public Response findStudentContactLogEntriesByStudent(@PathParam("STUDENTID") Long studentId) {
    Student student = studentController.findStudentById(studentId);
    if (student == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (student.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    return Response.ok(objectFactory.createModel(studentContactLogEntryController.listContactLogEntriesByStudent(student))).build();
  }
  
  @Path("/students/{STUDENTID:[0-9]*}/contactLogEntries/{ID:[0-9]*}")
  @GET
  @Permit (handle = Handle.EXCEPTION, value = StudentContactLogEntryPermissions.FIND_STUDENTCONTACTLOGENTRY)
  public Response findStudentContactLogEntryById(@PathParam("STUDENTID") Long studentId, @PathParam("ID") Long id) {
    Student student = studentController.findStudentById(studentId);
    if (student == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (student.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
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
  @Permit (handle = Handle.EXCEPTION, value = StudentContactLogEntryPermissions.UPDATE_STUDENTCONTACTLOGENTRY)
  public Response updateStudentContactLogEntry(@PathParam("STUDENTID") Long studentId, @PathParam("ID") Long id, fi.pyramus.rest.model.StudentContactLogEntry entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    Student student = studentController.findStudentById(studentId);
    if (student == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (student.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
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
  @Permit (handle = Handle.EXCEPTION, value = StudentContactLogEntryPermissions.DELETE_STUDENTCONTACTLOGENTRY)
  public Response deleteStudentContactLogEntry(@PathParam("STUDENTID") Long studentId, @PathParam("ID") Long id, @DefaultValue ("false") @QueryParam ("permanent") Boolean permanent) {
    Student student = studentController.findStudentById(studentId);
    if (student == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (student.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
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
      studentContactLogEntryController.archiveStudentContactLogEntry(contactLogEntry, getLoggedUser());
    }
      
    return Response.noContent().build();
  }
  
  @Path("/variables")
  @POST
  @Permit (handle = Handle.EXCEPTION, value = UserPermissions.CREATE_USERVARIABLEKEY)
  public Response createVariable(fi.pyramus.rest.model.VariableKey entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    if (StringUtils.isBlank(entity.getKey())||StringUtils.isBlank(entity.getName())||(entity.getType() == null)||(entity.getUserEditable() == null)) {
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
  @Permit (handle = Handle.EXCEPTION, value = UserPermissions.LIST_USERVARIABLEKEYS)
  public Response listVariables() {
    List<UserVariableKey> variableKeys = userController.listUserVariableKeys();
    if (variableKeys.isEmpty()) {
      return Response.noContent().build();
    }
    
    return Response.ok(objectFactory.createModel(variableKeys)).build();
  }
  
  @Path("/variables/{KEY}")
  @GET
  @Permit (handle = Handle.EXCEPTION, value = UserPermissions.FIND_USERVARIABLEKEY)
  public Response findVariable(@PathParam ("KEY") String key) {
    UserVariableKey studentVariableKey = userController.findUserVariableKeyByVariableKey(key);
    if (studentVariableKey == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    return Response.ok(objectFactory.createModel(studentVariableKey)).build();
  }
  
  @Path("/variables/{KEY}")
  @PUT
  @Permit (handle = Handle.EXCEPTION, value = UserPermissions.UPDATE_USERVARIABLEKEY)
  public Response updateVariable(@PathParam ("KEY") String key, fi.pyramus.rest.model.VariableKey entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    if (StringUtils.isBlank(entity.getName())||(entity.getType() == null)||(entity.getUserEditable() == null)) {
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
  @Permit (handle = Handle.EXCEPTION, value = UserPermissions.DELETE_USERVARIABLEKEY)
  public Response deleteVariable(@PathParam ("KEY") String key) {
    UserVariableKey userVariableKey = userController.findUserVariableKeyByVariableKey(key);
    if (userVariableKey == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    userController.deleteUserVariableKey(userVariableKey);
    
    return Response.noContent().build();
  }
  
  @Path("/students/{STUDENTID:[0-9]*}/emails")
  @GET
  @Permit (handle = Handle.EXCEPTION, value = StudentPermissions.LIST_STUDENTEMAILS)
  public Response listStudentEmails(@PathParam("STUDENTID") Long studentId) {
    Student student = studentController.findStudentById(studentId);
    if (student == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (student.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    List<Email> emails = student.getContactInfo().getEmails();
    if (emails.isEmpty()) {
      return Response.noContent().build();
    }
    
    return Response.ok(objectFactory.createModel(emails)).build();
  }
  
  @Path("/students/{STUDENTID:[0-9]*}/emails")
  @POST
  @Permit (handle = Handle.EXCEPTION, value = StudentPermissions.CREATE_STUDENTEMAIL)
  public Response createStudentEmail(@PathParam("STUDENTID") Long studentId, fi.pyramus.rest.model.Email email) {
    if (email == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    Long contactTypeId = email.getContactTypeId();
    Boolean defaultAddress = email.getDefaultAddress();
    String address = email.getAddress();
    
    if ((contactTypeId == null) || (defaultAddress == null) || StringUtils.isBlank(address)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    Student student = studentController.findStudentById(studentId);
    if (student == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (student.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    ContactType contactType = commonController.findContactTypeById(contactTypeId);
    if (contactType == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    return Response.ok(objectFactory.createModel(studentController.addStudentEmail(student, contactType, address, defaultAddress))).build();
  }
  
  @Path("/students/{STUDENTID:[0-9]*}/emails/{ID:[0-9]*}")
  @GET
  @Permit (handle = Handle.EXCEPTION, value = StudentPermissions.FIND_STUDENTEMAIL)
  public Response findStudentEmail(@PathParam("STUDENTID") Long studentId, @PathParam("ID") Long id) {
    Student student = studentController.findStudentById(studentId);
    if (student == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (student.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
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
  @Permit (handle = Handle.EXCEPTION, value = StudentPermissions.DELETE_STUDENTEMAIL)
  public Response deleteStudentEmail(@PathParam("STUDENTID") Long studentId, @PathParam("ID") Long id) {
    Student student = studentController.findStudentById(studentId);
    if (student == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (student.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
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
  @Permit (handle = Handle.EXCEPTION, value = StudentPermissions.LIST_STUDENTADDRESSS)
  public Response listStudentAddresses(@PathParam("STUDENTID") Long studentId) {
    Student student = studentController.findStudentById(studentId);
    if (student == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (student.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    List<Address> addresses = student.getContactInfo().getAddresses();
    if (addresses.isEmpty()) {
      return Response.noContent().build();
    }
    
    return Response.ok(objectFactory.createModel(addresses)).build();
  }
  
  @Path("/students/{STUDENTID:[0-9]*}/addresses")
  @POST
  @Permit (handle = Handle.EXCEPTION, value = StudentPermissions.CREATE_STUDENTADDRESS)
  public Response createStudentAddress(@PathParam("STUDENTID") Long studentId, fi.pyramus.rest.model.Address address) {
    if (address == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    Long contactTypeId = address.getContactTypeId();
    Boolean defaultAddress = address.getDefaultAddress();
    String name = address.getName();
    String streetAddress = address.getStreetAddress();
    String postalCode = address.getPostalCode();
    String country = address.getCountry();
    String city = address.getCity();
    
    if ((contactTypeId == null) || (defaultAddress == null)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    Student student = studentController.findStudentById(studentId);
    if (student == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (student.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    ContactType contactType = commonController.findContactTypeById(contactTypeId);
    if (contactType == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    return Response.ok(objectFactory.createModel(studentController.addStudentAddress(student, contactType, defaultAddress, name, streetAddress, postalCode, city, country))).build();
  }
  
  @Path("/students/{STUDENTID:[0-9]*}/addresses/{ID:[0-9]*}")
  @GET
  @Permit (handle = Handle.EXCEPTION, value = StudentPermissions.FIND_STUDENTADDRESS)
  public Response findStudentAddress(@PathParam("STUDENTID") Long studentId, @PathParam("ID") Long id) {
    Student student = studentController.findStudentById(studentId);
    if (student == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (student.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
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
  @DELETE
  @Permit (handle = Handle.EXCEPTION, value = StudentPermissions.DELETE_STUDENTADDRESS)
  public Response deleteStudentAddress(@PathParam("STUDENTID") Long studentId, @PathParam("ID") Long id) {
    Student student = studentController.findStudentById(studentId);
    if (student == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (student.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
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
  @Permit (handle = Handle.EXCEPTION, value = StudentPermissions.LIST_STUDENTPHONENUMBERS)
  public Response listStudentPhoneNumbers(@PathParam("STUDENTID") Long studentId) {
    Student student = studentController.findStudentById(studentId);
    if (student == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (student.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    List<PhoneNumber> phoneNumbers = student.getContactInfo().getPhoneNumbers();
    if (phoneNumbers.isEmpty()) {
      return Response.noContent().build();
    }
    
    return Response.ok(objectFactory.createModel(phoneNumbers)).build();
  }
  
  @Path("/students/{STUDENTID:[0-9]*}/phoneNumbers")
  @POST
  @Permit (handle = Handle.EXCEPTION, value = StudentPermissions.CREATE_STUDENTPHONENUMBER)
  public Response createStudentPhoneNumber(@PathParam("STUDENTID") Long studentId, fi.pyramus.rest.model.PhoneNumber phoneNumber) {
    if (phoneNumber == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    Long contactTypeId = phoneNumber.getContactTypeId();
    Boolean defaultNumber = phoneNumber.getDefaultNumber();
    String number = phoneNumber.getNumber();
    
    if ((contactTypeId == null) || (defaultNumber == null) || StringUtils.isBlank(number)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    Student student = studentController.findStudentById(studentId);
    if (student == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (student.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    ContactType contactType = commonController.findContactTypeById(contactTypeId);
    if (contactType == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    return Response.ok(objectFactory.createModel(studentController.addStudentPhoneNumber(student, contactType, number, defaultNumber))).build();
  }
  
  @Path("/students/{STUDENTID:[0-9]*}/phoneNumbers/{ID:[0-9]*}")
  @GET
  @Permit (handle = Handle.EXCEPTION, value = StudentPermissions.FIND_STUDENTPHONENUMBER)
  public Response findStudentPhoneNumber(@PathParam("STUDENTID") Long studentId, @PathParam("ID") Long id) {
    Student student = studentController.findStudentById(studentId);
    if (student == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (student.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
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
  @Permit (handle = Handle.EXCEPTION, value = StudentPermissions.DELETE_STUDENTPHONENUMBER)
  public Response deleteStudentPhoneNumber(@PathParam("STUDENTID") Long studentId, @PathParam("ID") Long id) {
    Student student = studentController.findStudentById(studentId);
    if (student == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (student.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
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
  @Permit (handle = Handle.EXCEPTION, value = StudentPermissions.LIST_STUDENTCONTACTURLS)
  public Response listStudentContactURLs(@PathParam("STUDENTID") Long studentId) {
    Student student = studentController.findStudentById(studentId);
    if (student == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (student.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    List<ContactURL> contactUrls = student.getContactInfo().getContactURLs();
    if (contactUrls.isEmpty()) {
      return Response.noContent().build();
    }
    
    return Response.ok(objectFactory.createModel(contactUrls)).build();
  }
  
  @Path("/students/{STUDENTID:[0-9]*}/contactURLs")
  @POST
  @Permit (handle = Handle.EXCEPTION, value = StudentPermissions.CREATE_STUDENTCONTACTURL)
  public Response createStudentContactURL(@PathParam("STUDENTID") Long studentId, fi.pyramus.rest.model.ContactURL contactURL) {
    if (contactURL == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    Long contactURLTypeId = contactURL.getContactURLTypeId();
    String url = contactURL.getUrl();
    
    if ((contactURLTypeId == null) || StringUtils.isBlank(url)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    Student student = studentController.findStudentById(studentId);
    if (student == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (student.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    ContactURLType contactURLType = commonController.findContactURLTypeById(contactURLTypeId);
    if (contactURLType == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    return Response.ok(objectFactory.createModel(studentController.addStudentContactURL(student, contactURLType, url))).build();
  }
  
  @Path("/students/{STUDENTID:[0-9]*}/contactURLs/{ID:[0-9]*}")
  @GET
  @Permit (handle = Handle.EXCEPTION, value = StudentPermissions.FIND_STUDENTCONTACTURL)
  public Response findStudentContactURL(@PathParam("STUDENTID") Long studentId, @PathParam("ID") Long id) {
    Student student = studentController.findStudentById(studentId);
    if (student == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (student.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
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
  @Permit (handle = Handle.EXCEPTION, value = StudentPermissions.DELETE_STUDENTCONTACTURL)
  public Response deleteStudentContactURL(@PathParam("STUDENTID") Long studentId, @PathParam("ID") Long id) {
    Student student = studentController.findStudentById(studentId);
    if (student == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (student.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
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
  @Permit (handle = Handle.EXCEPTION, value = StudentPermissions.LIST_COURSESTUDENTSBYSTUDENT)
  public Response listCourseStudents(@PathParam("STUDENTID") Long studentId) {
    Student student = studentController.findStudentById(studentId);
    if (student == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (student.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    List<fi.pyramus.domainmodel.courses.CourseStudent> courseStudents = courseController.listCourseStudentsByStudent(student);
    if (courseStudents.isEmpty()) {
      return Response.status(Status.NO_CONTENT).build();
    }
    
    List<fi.pyramus.domainmodel.courses.Course> courses = new ArrayList<>();
    for (fi.pyramus.domainmodel.courses.CourseStudent courseStudent : courseStudents) {
      courses.add(courseStudent.getCourse());
    }
    
    return Response.status(Status.OK).entity(objectFactory.createModel(courses)).build();
  }
}
