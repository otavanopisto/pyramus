package fi.otavanopisto.pyramus.rest;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
import org.apache.commons.lang3.time.DateUtils;

import fi.otavanopisto.pyramus.dao.base.OrganizationDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseModuleDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.Archived;
import fi.otavanopisto.pyramus.domainmodel.base.Address;
import fi.otavanopisto.pyramus.domainmodel.base.ContactInfo;
import fi.otavanopisto.pyramus.domainmodel.base.ContactURL;
import fi.otavanopisto.pyramus.domainmodel.base.ContactURLType;
import fi.otavanopisto.pyramus.domainmodel.base.CourseModule;
import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;
import fi.otavanopisto.pyramus.domainmodel.base.EducationSubtype;
import fi.otavanopisto.pyramus.domainmodel.base.EducationType;
import fi.otavanopisto.pyramus.domainmodel.base.Email;
import fi.otavanopisto.pyramus.domainmodel.base.Language;
import fi.otavanopisto.pyramus.domainmodel.base.Municipality;
import fi.otavanopisto.pyramus.domainmodel.base.Nationality;
import fi.otavanopisto.pyramus.domainmodel.base.Organization;
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
import fi.otavanopisto.pyramus.domainmodel.grading.CreditLink;
import fi.otavanopisto.pyramus.domainmodel.grading.Grade;
import fi.otavanopisto.pyramus.domainmodel.grading.TransferCredit;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentActivityType;
import fi.otavanopisto.pyramus.domainmodel.students.StudentContactLogEntry;
import fi.otavanopisto.pyramus.domainmodel.students.StudentContactLogEntryComment;
import fi.otavanopisto.pyramus.domainmodel.students.StudentContactLogEntryType;
import fi.otavanopisto.pyramus.domainmodel.students.StudentEducationalLevel;
import fi.otavanopisto.pyramus.domainmodel.students.StudentExaminationType;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroup;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroupStudent;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroupUser;
import fi.otavanopisto.pyramus.domainmodel.students.StudentStudyEndReason;
import fi.otavanopisto.pyramus.domainmodel.users.ContactLogAccess;
import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.StudentParent;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.domainmodel.users.UserVariable;
import fi.otavanopisto.pyramus.domainmodel.users.UserVariableKey;
import fi.otavanopisto.pyramus.domainmodel.worklist.WorklistItemTemplate;
import fi.otavanopisto.pyramus.framework.StaffMemberProperties;
import fi.otavanopisto.pyramus.framework.UserUtils;
import fi.otavanopisto.pyramus.persistence.search.SearchResult;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit.Handling;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit.Style;
import fi.otavanopisto.pyramus.rest.controller.AssessmentController;
import fi.otavanopisto.pyramus.rest.controller.CommonController;
import fi.otavanopisto.pyramus.rest.controller.CourseController;
import fi.otavanopisto.pyramus.rest.controller.CurriculumController;
import fi.otavanopisto.pyramus.rest.controller.LanguageController;
import fi.otavanopisto.pyramus.rest.controller.MatriculationEligibilityController;
import fi.otavanopisto.pyramus.rest.controller.MunicipalityController;
import fi.otavanopisto.pyramus.rest.controller.NationalityController;
import fi.otavanopisto.pyramus.rest.controller.PersonController;
import fi.otavanopisto.pyramus.rest.controller.SchoolController;
import fi.otavanopisto.pyramus.rest.controller.StudentActivityTypeController;
import fi.otavanopisto.pyramus.rest.controller.StudentContactLogEntryCommentController;
import fi.otavanopisto.pyramus.rest.controller.StudentContactLogEntryController;
import fi.otavanopisto.pyramus.rest.controller.StudentController;
import fi.otavanopisto.pyramus.rest.controller.StudentEducationalLevelController;
import fi.otavanopisto.pyramus.rest.controller.StudentExaminationTypeController;
import fi.otavanopisto.pyramus.rest.controller.StudentGroupController;
import fi.otavanopisto.pyramus.rest.controller.StudentMatriculationEligibilityResult;
import fi.otavanopisto.pyramus.rest.controller.StudentStudyEndReasonController;
import fi.otavanopisto.pyramus.rest.controller.StudyProgrammeCategoryController;
import fi.otavanopisto.pyramus.rest.controller.StudyProgrammeController;
import fi.otavanopisto.pyramus.rest.controller.UserController;
import fi.otavanopisto.pyramus.rest.controller.WorklistController;
import fi.otavanopisto.pyramus.rest.controller.permissions.CourseAssessmentPermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.LanguagePermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.MunicipalityPermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.NationalityPermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.PersonPermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.StudentActivityTypePermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.StudentEducationalLevelPermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.StudentExaminationTypePermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.StudentGroupPermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.StudentPermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.StudentStudyEndReasonPermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.StudyProgrammeCategoryPermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.StudyProgrammePermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.UserPermissions;
import fi.otavanopisto.pyramus.rest.model.CourseActivityInfo;
import fi.otavanopisto.pyramus.rest.model.SpecEdTeacher;
import fi.otavanopisto.pyramus.rest.model.StudentContactLogEntryBatch;
import fi.otavanopisto.pyramus.rest.model.StudentContactLogEntryCommentRestModel;
import fi.otavanopisto.pyramus.rest.model.StudentCourseStats;
import fi.otavanopisto.pyramus.rest.model.StudentGuidanceRelation;
import fi.otavanopisto.pyramus.rest.model.StudentMatriculationEligibility;
import fi.otavanopisto.pyramus.rest.model.UserContactInfo;
import fi.otavanopisto.pyramus.rest.model.worklist.CourseBillingRestModel;
import fi.otavanopisto.pyramus.rest.security.RESTSecurity;
import fi.otavanopisto.pyramus.rest.util.ISO8601Timestamp;
import fi.otavanopisto.pyramus.rest.util.PyramusConsts;
import fi.otavanopisto.pyramus.security.impl.SessionController;
import fi.otavanopisto.pyramus.security.impl.permissions.OrganizationPermissions;
import fi.otavanopisto.pyramus.tor.StudentTOR;
import fi.otavanopisto.pyramus.tor.StudentTORController;
import fi.otavanopisto.pyramus.tor.TORCourseLengthUnit;
import fi.otavanopisto.security.LoggedIn;

@Path("/students")
@Produces("application/json")
@Consumes("application/json")
@Stateful
@RequestScoped
public class StudentRESTService extends AbstractRESTService {

  @Inject
  private Logger logger;
  
  @Inject
  private RESTSecurity restSecurity;

  @Inject
  private UserController userController;

  @Inject
  private WorklistController worklistController;

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
  private StudentContactLogEntryCommentController studentContactLogEntryCommentController;

  @Inject
  private SchoolController schoolController;

  @Inject
  private CourseController courseController;
  
  @Inject
  private CurriculumController curriculumController;

  @Inject
  private SessionController sessionController;

  @Inject
  private MatriculationEligibilityController matriculationEligibilityController;
  
  @Inject
  private ObjectFactory objectFactory;
  
  @Inject
  private AssessmentController assessmentController;

  @Inject
  private OrganizationDAO organizationDAO;
  
  @Inject
  private CourseModuleDAO courseModuleDAO;

  @Inject
  private StaffMemberDAO staffMemberDAO;
  
  private static final String USERVARIABLE_SUBJECT_CHOICES_AIDINKIELI = "lukioAidinkieli";
  private static final String USERVARIABLE_SUBJECT_CHOICES_USKONTO = "lukioUskonto";
  private static final String USERVARIABLE_SUBJECT_CHOICES_MATEMATIIKKA = "lukioMatematiikka";
  private static final String USERVARIABLE_SUBJECT_CHOICES_KIELI_A = "lukioKieliA";
  private static final String USERVARIABLE_SUBJECT_CHOICES_KIELI_B1 = "lukioKieliB1";
  private static final String USERVARIABLE_SUBJECT_CHOICES_KIELI_B2 = "lukioKieliB2";
  private static final String USERVARIABLE_SUBJECT_CHOICES_KIELI_B3 = "lukioKieliB3";

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
    String officialEducationType = entity.getOfficialEducationType();
    Long categoryId = entity.getCategoryId();
    Long organizationId = entity.getOrganizationId();
    boolean hasEvaluationFees = entity.getHasEvaluationFees() != null ? entity.getHasEvaluationFees().booleanValue() : false;

    if (StringUtils.isBlank(name) || StringUtils.isBlank(code) || categoryId == null || organizationId == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    StudyProgrammeCategory studyProgrammeCategory = studyProgrammeCategoryController.findStudyProgrammeCategoryById(categoryId);
    Organization organization = organizationDAO.findById(organizationId);

    if (studyProgrammeCategory == null || organization == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    if (!UserUtils.canAccessOrganization(sessionController.getUser(), organization)) {
      return Response.status(Status.FORBIDDEN).build();
    }
    
    StudyProgramme studyProgramme = studyProgrammeController.createStudyProgramme(organization, name, code, officialEducationType, studyProgrammeCategory, hasEvaluationFees);
    return Response.ok(objectFactory.createModel(studyProgramme)).build();
  }

  @Path("/studyProgrammes")
  @GET
  @RESTPermit(StudyProgrammePermissions.LIST_STUDYPROGRAMMES)
  public Response listStudyProgrammes(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<StudyProgramme> studyProgrammes = studyProgrammeController.listAccessibleStudyProgrammes(
        sessionController.getUser(), filterArchived ? Archived.UNARCHIVED : Archived.BOTH);
    return Response.ok(objectFactory.createModel(studyProgrammes)).build();
  }

  @Path("/studyProgrammes/{ID:[0-9]*}")
  @GET
  @RESTPermit(StudyProgrammePermissions.FIND_STUDYPROGRAMME)
  public Response findStudyProgrammeById(@PathParam("ID") Long id, @Context Request request) {
    StudyProgramme studyProgramme = studyProgrammeController.findStudyProgrammeById(id);
    if (studyProgramme == null || studyProgramme.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (!UserUtils.canAccessOrganization(sessionController.getUser(), studyProgramme.getOrganization())) {
      return Response.status(Status.FORBIDDEN).build();
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
    String officialEducationType = entity.getOfficialEducationType();
    Long categoryId = entity.getCategoryId();
    Long organizationId = entity.getOrganizationId();
    boolean hasEvaluationFees = entity.getHasEvaluationFees() != null ? entity.getHasEvaluationFees().booleanValue() : false;

    if (StringUtils.isBlank(name) || StringUtils.isBlank(code) || categoryId == null || organizationId == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    StudyProgrammeCategory programmeCategory = studyProgrammeCategoryController.findStudyProgrammeCategoryById(categoryId);
    if (programmeCategory == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    Organization organization = organizationDAO.findById(organizationId);
    if (organization == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    // User needs to be member of both the previous organization and the new one
    if (!(UserUtils.canAccessOrganization(sessionController.getUser(), studyProgramme.getOrganization()) 
        && UserUtils.canAccessOrganization(sessionController.getUser(), organization))) {
      return Response.status(Status.FORBIDDEN).build();
    }
    
    studyProgramme = studyProgrammeController.updateStudyProgramme(studyProgramme, organization, name, code, officialEducationType, programmeCategory, hasEvaluationFees);
    
    return Response.ok().entity(objectFactory.createModel(studyProgramme))
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
    Long organizationId = entity.getOrganizationId();

    if (StringUtils.isBlank(name) || (organizationId == null)) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    Organization organization = organizationDAO.findById(organizationId);
    if (!UserUtils.canAccessOrganization(sessionController.getUser(), organization)) {
      return Response.status(Status.FORBIDDEN).build();
    }

    StudentGroup studentGroup = studentGroupController.createStudentGroup(organization, name, description, toDate(beginDate), sessionController.getUser(), entity.getGuidanceGroup());

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
    User user = sessionController.getUser();

    if (sessionController.hasEnvironmentPermission(StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION)) {
      // List only personal groups if user can't access others
      if (filterArchived) {
        studentGroups = studentGroupController.listUnarchivedStudentGroupsByMember(user, firstResult, maxResults);
      } else {
        studentGroups = studentGroupController.listStudentGroupsByMember(user, firstResult, maxResults);
      }
    } else {
      studentGroups = studentGroupController.listAccessibleStudentGroups(user, firstResult, maxResults, filterArchived);
    }
    
    if (studentGroups.isEmpty()) {
      return Response.noContent().build();
    }

    return Response.ok(objectFactory.createModel(studentGroups)).build();
  }

  @Path("/studentGroups/{ID:[0-9]*}")
  @GET
  @LoggedIn
  @RESTPermit(handling = Handling.INLINE)
  public Response findStudentGroup(@PathParam("ID") Long id) {
    StudentGroup studentGroup = studentGroupController.findStudentGroupById(id);
    if (studentGroup == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (studentGroup.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (!UserUtils.canAccessOrganization(sessionController.getUser(), studentGroup.getOrganization())) {
      return Response.status(Status.FORBIDDEN).build();
    }

    if (sessionController.hasPermission(StudentGroupPermissions.FIND_STUDENTGROUP, studentGroup) || studentGroupController.isMember(sessionController.getUser(), studentGroup)) {
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
    Long organizationId = entity.getOrganizationId();
    Boolean guidanceGroup = entity.getGuidanceGroup();

    if (StringUtils.isBlank(name) || (organizationId == null)) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    Organization organization = organizationDAO.findById(organizationId);
    StudentGroup studentGroup = studentGroupController.findStudentGroupById(id);
    
    if ((studentGroup == null) || (organization == null) || studentGroup.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    // Can the user access the new organization?
    if (!UserUtils.canAccessOrganization(sessionController.getUser(), organization)) {
      return Response.status(Status.FORBIDDEN).build();
    }

    // Can the user access the old organization?
    if (!UserUtils.canAccessOrganization(sessionController.getUser(), studentGroup.getOrganization())) {
      return Response.status(Status.FORBIDDEN).build();
    }

    studentGroupController.updateStudentGroup(studentGroup, organization, name, description, toDate(beginDate), guidanceGroup, sessionController.getUser());
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

    if (!UserUtils.canAccessOrganization(sessionController.getUser(), studentGroup.getOrganization())) {
      return Response.status(Status.FORBIDDEN).build();
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
    if (studentGroup == null || studentGroup.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (!UserUtils.canAccessOrganization(sessionController.getUser(), studentGroup.getOrganization())) {
      return Response.status(Status.FORBIDDEN).build();
    }

    StaffMember staffMember = userController.findStaffMemberById(entity.getStaffMemberId());
    if (staffMember == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    Boolean messageRecipient = Boolean.FALSE;
    StudentGroupUser studentGroupUser = studentGroupController.createStudentGroupStaffMember(studentGroup, staffMember, messageRecipient, sessionController.getUser());

    return Response.ok(objectFactory.createModel(studentGroupUser)).build();
  }

  @Path("/studentGroups/{ID:[0-9]*}/staffmembers")
  @GET
  @RESTPermit(StudentGroupPermissions.LIST_STUDENTGROUPSTAFFMEMBERS)
  public Response listStudentGroupStaffMembers(@PathParam("ID") Long id) {
    StudentGroup studentGroup = studentGroupController.findStudentGroupById(id);
    if (studentGroup == null || studentGroup.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (!UserUtils.canAccessOrganization(sessionController.getUser(), studentGroup.getOrganization())) {
      return Response.status(Status.FORBIDDEN).build();
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

    if (!UserUtils.canAccessOrganization(sessionController.getUser(), studentGroup.getOrganization())) {
      return Response.status(Status.FORBIDDEN).build();
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

    if (!UserUtils.canAccessOrganization(sessionController.getUser(), studentGroup.getOrganization())) {
      return Response.status(Status.FORBIDDEN).build();
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

    if (!UserUtils.canAccessOrganization(sessionController.getUser(), studentGroup.getOrganization())) {
      return Response.status(Status.FORBIDDEN).build();
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

    if (!UserUtils.canAccessOrganization(sessionController.getUser(), studentGroup.getOrganization())) {
      return Response.status(Status.FORBIDDEN).build();
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

    if (!UserUtils.canAccessOrganization(sessionController.getUser(), studentGroup.getOrganization())) {
      return Response.status(Status.FORBIDDEN).build();
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

    if (!UserUtils.canAccessOrganization(sessionController.getUser(), studentGroup.getOrganization())) {
      return Response.status(Status.FORBIDDEN).build();
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

    if (!sessionController.hasEnvironmentPermission(OrganizationPermissions.ACCESS_ALL_ORGANIZATIONS)) {
      if (!(studyProgramme.getOrganization() != null && UserUtils.isMemberOf(sessionController.getUser(), studyProgramme.getOrganization()))) {
        return Response.status(Status.FORBIDDEN).build();
      }
    }
    
    if (!userController.checkUserVariableKeysExist(entity.getVariables().keySet())) {
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
        entity.getStudyEndText());
    
    if (Boolean.TRUE.equals(lodging) && entity.getStudyStartDate() != null)
      studentController.addLodgingPeriod(student, toDate(entity.getStudyStartDate()), toDate(entity.getStudyEndDate()));
      
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

    User loggedUser = sessionController.getUser();
    
    Collection<Organization> organizations = 
        sessionController.hasEnvironmentPermission(OrganizationPermissions.ACCESS_ALL_ORGANIZATIONS) ?
        null : new HashSet<>(Arrays.asList(loggedUser.getOrganization()));
    
    if (sessionController.hasPermission(StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION, null)) {
      List<StudentGroup> groups = studentGroupController.listStudentGroupsByMember(sessionController.getUser());
      students = studentController.listStudents(organizations, email, groups, archived, firstResult, maxResults);
    } else {
      students = studentController.listStudents(organizations, email, null, archived, firstResult, maxResults);
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

    if (!restSecurity.hasPermission(new String[] { StudentPermissions.FIND_STUDENT, UserPermissions.USER_OWNER, UserPermissions.STUDENT_PARENT }, student, Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }

    if (!sessionController.hasEnvironmentPermission(OrganizationPermissions.ACCESS_ALL_ORGANIZATIONS)) {
      if (!UserUtils.isMemberOf(sessionController.getUser(), student.getOrganization())) {
        return Response.status(Status.FORBIDDEN).build();
      }
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

    if (personId == null || studyProgrammeId == null) {
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

    if (!sessionController.hasEnvironmentPermission(OrganizationPermissions.ACCESS_ALL_ORGANIZATIONS)) {
      // Needs to be member of both organizations
      if (!(UserUtils.isMemberOf(sessionController.getUser(), studyProgramme.getOrganization()) &&
          UserUtils.isMemberOf(sessionController.getUser(), student.getOrganization()))) {
        return Response.status(Status.FORBIDDEN).build();
      }
    }
    
    if (!userController.checkUserVariableKeysExist(entity.getVariables().keySet())) {
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

    // TODO lodging cannot be updated via boolean
    
    studentController.updateStudent(student, firstName, lastName, nickname, entity.getAdditionalInfo(), toDate(entity.getStudyTimeEnd()),
        activityType, examinationType, educationalLevel, entity.getEducation(), nationality, municipality, language, school, curriculum,
        entity.getPreviousStudies(), toDate(entity.getStudyStartDate()), toDate(entity.getStudyEndDate()), studyEndReason, entity.getStudyEndText());

    studentController.updateStudyProgramme(student, studyProgramme);
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

  @Path("/students/{ID:[0-9]*}/educationalLevel")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response getStudentEducationalLevelByStudent(@PathParam("ID") Long studentId) {
    Student student = studentController.findStudentById(studentId);
    
    if (student == null) {
      return Response.status(Status.NOT_FOUND).entity("Not found").build();
    }

    if (!restSecurity.hasPermission(new String[] { StudentPermissions.FIND_STUDENT, UserPermissions.USER_OWNER }, student, Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }
    
    StudentEducationalLevel educationalLevel = student.getEducationalLevel();
    
    if (educationalLevel == null) {
      return Response.noContent().entity("Could not find educational level").build();
    }
    return Response.ok(educationalLevel.getName()).build();
  }

  @Path("/students/{ID:[0-9]*}/matriculationEligibility")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response getStudentMatriculationEligibility(@PathParam("ID") Long studentId, @QueryParam ("subjectCode") String subjectCode) {
    if (StringUtils.isBlank(subjectCode)) {
      return Response.status(Status.BAD_REQUEST).entity("Subject is required").build();
    }
    
    Student student = studentController.findStudentById(studentId);
    if (student == null) {
      return Response.status(Status.NOT_FOUND).entity("Not found").build();
    }
    
    if (!restSecurity.hasPermission(new String[] { StudentPermissions.FIND_STUDENT, UserPermissions.USER_OWNER, UserPermissions.STUDENT_PARENT }, student, Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }

    StudentMatriculationEligibilityResult result = matriculationEligibilityController.getStudentMatriculationEligible(student, subjectCode);
    if (result == null) {
      return Response.status(Status.BAD_REQUEST).entity("Could not resolve matriculation eligibility").build();
    } else {
      return Response.ok(new StudentMatriculationEligibility(result.getEligible(), result.getRequirePassingGrades(), result.getAcceptedCourseCount(), result.getAcceptedTransferCreditCount())).build();
    }
  }

  @Path("/students/{ID:[0-9]*}/contactLogEntries")
  @POST
  @RESTPermit(handling = Handling.INLINE)
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
    
    ContactLogAccess access = studentController.resolveContactLogAccess(student);
    
    if(!access.equals(ContactLogAccess.ALL) && !access.equals(ContactLogAccess.OWN)) {
      return Response.status(Status.FORBIDDEN).build();
    }
    
    User loggedUser = sessionController.getUser();
    StaffMember creator = userController.findStaffMemberById(loggedUser.getId());
    
    StudentContactLogEntryType type = entity.getType() != null ? StudentContactLogEntryType.valueOf(entity.getType().name()) : null;
    StudentContactLogEntry contactLogEntry = studentContactLogEntryController.createContactLogEntry(student, type, entity.getText(),
        toDate(entity.getEntryDate()), loggedUser.getFirstName() + " " + loggedUser.getLastName(), creator);

    return Response.ok(objectFactory.createModel(contactLogEntry)).build();
  }

  @Path("/students/{STUDENTID:[0-9]*}/contactLogEntries")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response listStudentContactLogEntriesByStudent(@PathParam("STUDENTID") Long studentId, 
      @QueryParam("resultsPerPage") Integer resultsPerPage, @QueryParam("page") Integer page) {
    Student student = studentController.findStudentById(studentId);
    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK)
      return Response.status(studentStatus).build(); 
    
    User loggedUser = sessionController.getUser();
    
    StaffMember staffMember = userController.findStaffMemberById(loggedUser.getId());
    
    if (staffMember == null) {
      return Response.status(Status.FORBIDDEN).build();
    }
    
    ContactLogAccess access = studentController.resolveContactLogAccess(student);
    Boolean allPrivileges = false;
    if (access.equals(ContactLogAccess.ALL)) {
      SearchResult<StudentContactLogEntry> searchResult = studentContactLogEntryController.listContactLogEntriesByStudent(student, resultsPerPage, page);
      allPrivileges = true;
      @SuppressWarnings("unchecked")
      List<fi.otavanopisto.pyramus.rest.model.StudentContactLogEntry> contactLogEntryRestModels = (List<fi.otavanopisto.pyramus.rest.model.StudentContactLogEntry>) objectFactory.createModel(searchResult.getResults());
      
      StudentContactLogEntryBatch responseEntries = new StudentContactLogEntryBatch(searchResult.getFirstResult(), contactLogEntryRestModels, searchResult.getTotalHitCount(), allPrivileges);
      return Response.ok(responseEntries).build();
    } else if (access.equals(ContactLogAccess.OWN)) {
      
      SearchResult<StudentContactLogEntry> searchResult = studentContactLogEntryController.listContactLogEntriesByStudentAndCreator(student, staffMember, resultsPerPage, page);
      
      @SuppressWarnings("unchecked")
      List<fi.otavanopisto.pyramus.rest.model.StudentContactLogEntry> contactLogEntryRestModels = (List<fi.otavanopisto.pyramus.rest.model.StudentContactLogEntry>) objectFactory.createModel(searchResult.getResults());
      
      StudentContactLogEntryBatch responseEntries = new StudentContactLogEntryBatch(searchResult.getFirstResult(), contactLogEntryRestModels, searchResult.getTotalHitCount(), allPrivileges);
      return Response.ok(responseEntries).build();
    } else {
      return Response.status(Status.FORBIDDEN).build();
    }
  }
  
  @Path("/students/{STUDENTID:[0-9]*}/contactLogEntries/{ID:[0-9]*}")
  @PUT
  @RESTPermit(handling = Handling.INLINE)
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

    if (!studentId.equals(contactLogEntry.getStudent().getId())) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    User loggedUser = sessionController.getUser();
    
    StaffMember staffMember = userController.findStaffMemberById(loggedUser.getId());
    
    if (staffMember == null) {
      return Response.status(Status.FORBIDDEN).build();
    }
    
    ContactLogAccess access = studentController.resolveContactLogAccess(student);
    
    if (access.equals(ContactLogAccess.NONE)) {
      return Response.status(Status.FORBIDDEN).build();
    }
    
    if (access.equals(ContactLogAccess.OWN)) {
      if (contactLogEntry.getCreator() != null) {
        if (!contactLogEntry.getCreator().getId().equals(staffMember.getId())) {
          return Response.status(Status.FORBIDDEN).build();
        }
      } else {
        return Response.status(Status.FORBIDDEN).build();
      }
    }
    

    StudentContactLogEntryType type = entity.getType() != null ? StudentContactLogEntryType.valueOf(entity.getType().name()) : null;
    studentContactLogEntryController.updateContactLogEntry(contactLogEntry, type, entity.getText(), toDate(entity.getEntryDate()));

    return Response.ok(objectFactory.createModel(contactLogEntry)).build();
  }

  @Path("/students/{STUDENTID:[0-9]*}/contactLogEntries/{ID:[0-9]*}")
  @DELETE
  @RESTPermit(handling = Handling.INLINE)
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

    if (!studentId.equals(contactLogEntry.getStudent().getId())) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    ContactLogAccess access = studentController.resolveContactLogAccess(student);
    
    if (access.equals(ContactLogAccess.NONE)) {
      return Response.status(Status.FORBIDDEN).build();
    }
    
    if (access.equals(ContactLogAccess.OWN)) {
      if (contactLogEntry.getCreator() != null) {
        if (!contactLogEntry.getCreator().getId().equals(sessionController.getUser().getId())) {
          return Response.status(Status.FORBIDDEN).build();
        }
      } else { 
        return Response.status(Status.FORBIDDEN).build(); 
      }
    }
    if (permanent) {
      studentContactLogEntryController.deleteStudentContactLogEntry(contactLogEntry);
    } else {
      studentContactLogEntryController.archiveStudentContactLogEntry(contactLogEntry, sessionController.getUser());
    }

    return Response.noContent().build();
  }
  
  @Path("/students/{STUDENTID:[0-9]*}/contactLogEntries/{ENTRYID:[0-9]*}/comments")
  @POST
  @RESTPermit(handling = Handling.INLINE)
  public Response createStudentContactLogEntryComment(@PathParam("STUDENTID") Long studentId, @PathParam("ENTRYID") Long entryId, StudentContactLogEntryCommentRestModel entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    StudentContactLogEntry contactLogEntry = studentContactLogEntryController.findContactLogEntryById(entryId);
    Student student = studentController.findStudentById(studentId);
    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK)
      return Response.status(studentStatus).build();

    if (!restSecurity.hasPermission(new String[] { StudentPermissions.FIND_STUDENT, UserPermissions.USER_OWNER }, student, Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }
    User loggedUser = sessionController.getUser();
    StaffMember staffMember = userController.findStaffMemberById(loggedUser.getId());
    
    if (staffMember == null) {
      return Response.status(Status.FORBIDDEN).build();
    }
    
    ContactLogAccess access = studentController.resolveContactLogAccess(student);
    
    if (!access.equals(ContactLogAccess.ALL)) {
      if (access.equals(ContactLogAccess.OWN)) {
        if (contactLogEntry.getCreator() != null) {
          if (!contactLogEntry.getCreator().getId().equals(staffMember.getId())) {
            return Response.status(Status.FORBIDDEN).build();
          }
        }
      } else {
        return Response.status(Status.FORBIDDEN).build();
      }
    }
    StudentContactLogEntryComment contactLogEntryComment = studentContactLogEntryCommentController.createContactLogEntryComment(contactLogEntry, entity.getText(), entity.getCommentDate(), loggedUser.getFirstName() + " " + loggedUser.getLastName(), staffMember);
    return Response.ok(objectFactory.createModel(contactLogEntryComment)).build();
  }
  
  @Path("/students/{STUDENTID:[0-9]*}/contactLogEntries/{ENTRYID:[0-9]*}/comments/{COMMENTID:[0-9]*}")
  @PUT
  @RESTPermit(handling = Handling.INLINE)
  public Response updateStudentContactLogEntryComment(@PathParam("STUDENTID") Long studentId, @PathParam("ENTRYID") Long entryId, @PathParam("COMMENTID") Long commentId,
      fi.otavanopisto.pyramus.rest.model.StudentContactLogEntryCommentRestModel entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    Student student = studentController.findStudentById(studentId);
    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK)
      return Response.status(studentStatus).build();
    
    StudentContactLogEntryComment contactLogEntryComment = studentContactLogEntryCommentController.findContactLogEntryCommentById(commentId);
    if (contactLogEntryComment == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    StudentContactLogEntry contactLogEntry = contactLogEntryComment.getEntry();

    if (contactLogEntry == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (contactLogEntry.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (!studentId.equals(contactLogEntry.getStudent().getId())) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    User loggedUser = sessionController.getUser();
    StaffMember staffMember = userController.findStaffMemberById(loggedUser.getId());
    
    ContactLogAccess access = studentController.resolveContactLogAccess(student);
    
    if (access.equals(ContactLogAccess.ALL)) {
      return Response.ok(objectFactory.createModel(studentContactLogEntryCommentController.updateContactLogEntryComment(contactLogEntryComment, entity.getText(), entity.getCommentDate()))).build();
    } else if (access.equals(ContactLogAccess.OWN)) {
      if (contactLogEntryComment.getCreator() != null) {
        if (contactLogEntryComment.getCreator().getId().equals(staffMember.getId())) {
          return Response.ok(objectFactory.createModel(studentContactLogEntryCommentController.updateContactLogEntryComment(contactLogEntryComment, entity.getText(), entity.getCommentDate()))).build();
        }
      }
    } 
    
    return Response.status(Status.FORBIDDEN).build();
  }

  @Path("/students/{STUDENTID:[0-9]*}/contactLogEntries/entryComments/{COMMENTID:[0-9]*}")
  @DELETE
  @RESTPermit(handling = Handling.INLINE)
  public Response deleteStudentContactLogEntryComment(@PathParam("STUDENTID") Long studentId, 
      @PathParam("COMMENTID") Long commentId,
      @DefaultValue("false") @QueryParam("permanent") Boolean permanent) {
    Student student = studentController.findStudentById(studentId);
    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK)
      return Response.status(studentStatus).build();

    StudentContactLogEntryComment contactLogEntryComment = studentContactLogEntryCommentController.findContactLogEntryCommentById(commentId);
    if (contactLogEntryComment == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    StudentContactLogEntry contactLogEntry = contactLogEntryComment.getEntry();
    if (!studentId.equals(contactLogEntry.getStudent().getId())) {
      return Response.status(Status.NOT_FOUND).build();
    }

    ContactLogAccess access = studentController.resolveContactLogAccess(student);
    
    if (access.equals(ContactLogAccess.NONE)) {
      return Response.status(Status.FORBIDDEN).build();
    }
    
    if (access.equals(ContactLogAccess.OWN)) {
      if (contactLogEntry.getCreator() != null) {
        if (!contactLogEntry.getCreator().getId().equals(sessionController.getUser().getId())) {
          return Response.status(Status.FORBIDDEN).build();
        }
      } else { 
        return Response.status(Status.FORBIDDEN).build(); 
      }
    }

    if (permanent) {
      studentContactLogEntryCommentController.deleteStudentContactLogEntryComment(contactLogEntryComment);
    } else {
      studentContactLogEntryCommentController.archiveStudentContactLogEntryComment(contactLogEntryComment, sessionController.getUser());
    }

    return Response.noContent().build();
  }


  @Path("/students/{STUDENTID:[0-9]*}/courses/{COURSEID:[0-9]*}/assessments/")
  @POST
  @RESTPermit(handling = Handling.INLINE)
  public Response createCourseAssessment(@PathParam("STUDENTID") Long studentId, @PathParam("COURSEID") Long courseId,
      fi.otavanopisto.pyramus.rest.model.CourseAssessment entity) {
    if (!sessionController.isLoggedIn()) {
      return Response.status(Status.FORBIDDEN).build();
    }
    
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    Student student = studentController.findStudentById(studentId);
    if (student == null || student.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    Course course = courseController.findCourseById(courseId);
    
    if (course == null) {
      return Response.status(Status.NOT_FOUND).entity("Could not find course").build();
    }

    if (course.getArchived()) {
      return Response.status(Status.NOT_FOUND).entity("Course is archived").build();
    }

    if (!sessionController.hasPermission(CourseAssessmentPermissions.CREATE_COURSEASSESSMENT, course)) {
      return Response.status(Status.FORBIDDEN).build();
    } else {
      // User has the required permission, check if it's restricted to limited group of students 
      if (sessionController.hasEnvironmentPermission(StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION)) {
        StaffMember staffMember = sessionController.getUser() instanceof StaffMember ? (StaffMember) sessionController.getUser() : null;
        
        if (staffMember != null) {
          if (!(courseController.isCourseStaffMember(course, staffMember) || studentController.isStudentGuider(staffMember, student))) {
            return Response.status(Status.FORBIDDEN).build();
          }
        } else {
          return Response.status(Status.FORBIDDEN).build();
        }
      }
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
    
    if(!courseStudent.getCourse().getId().equals(course.getId())){
      return Response.status(Status.BAD_REQUEST).entity("Coursestudent doesnt match course").build();
    }
    
    StaffMember assessor = userController.findStaffMemberById(entity.getAssessorId());
    
    if (assessor == null) {
      return Response.status(Status.BAD_REQUEST).entity("Could not find assessor").build();
    }
    
    Grade grade = commonController.findGradeByIdId(entity.getGradeId());
    
    if (grade == null) {
      return Response.status(Status.BAD_REQUEST).entity("Could not find grade").build();
    }
    
    CourseModule courseModule = entity.getCourseModuleId() != null ? courseModuleDAO.findById(entity.getCourseModuleId()) : null;
    
    if ((courseModule == null) || !courseModule.getCourse().getId().equals(course.getId())) {
      return Response.status(Status.BAD_REQUEST).entity("Invalid course module").build();
    }
    
    CourseAssessment courseAssessment = assessmentController.createCourseAssessment(courseStudent, courseModule, assessor, grade, Date.from(entity.getDate().toInstant()), entity.getVerbalAssessment());
    
    // #1198: Create a worklist entry for this assessment, if applicable
    
    boolean isRaisedGrade = assessmentController.isRaisedGrade(courseAssessment);
    WorklistItemTemplate template = worklistController.getTemplateForCourseAssessment();
    if (template != null) {
      
      // #1228: Use billing settings together with the template to create a proper billing row

      CourseBillingRestModel courseBillingRestModel = worklistController.getCourseBillingRestModel();
      if (courseBillingRestModel != null) {
        
        // Price
        
        Double price = worklistController.getCourseModuleBasePrice(courseModule, sessionController.getUser());
        if (price != null) {

          // Determine billing number from student's study programme
          // (high school if applicable, elementary as fallback)

          String code = student.getStudyProgramme() != null &&
              student.getStudyProgramme().getCategory() !=  null &&
              student.getStudyProgramme().getCategory().getEducationType() != null &&
              student.getStudyProgramme().getCategory().getEducationType().getCode() != null
              ? student.getStudyProgramme().getCategory().getEducationType().getCode() : null;
          boolean isHighSchoolStudent = StringUtils.equalsIgnoreCase(PyramusConsts.STUDYPROGRAMME_LUKIO, code);
          String billingNumber = isHighSchoolStudent
              ? courseBillingRestModel.getHighSchoolBillingNumber()
                  : courseBillingRestModel.getElementaryBillingNumber();

          // Description part 1: type (not localized because manual worklist items do not support localization, either) 

          String description = isRaisedGrade ? "Arvosanan korotus" : "Kurssiarviointi";

          // Description part 2: student display name

          StringBuilder sb = new StringBuilder();
          sb.append(student.getFirstName());
          if (!StringUtils.isEmpty(student.getNickname())) {
            sb.append(String.format(" \"%s\"", student.getNickname()));
          }
          if (!StringUtils.isEmpty(student.getLastName())) {
            sb.append(String.format(" %s", student.getLastName()));
          }
          if (student.getStudyProgramme() != null) {
            sb.append(String.format(" (%s)", student.getStudyProgramme().getName()));
          }
          String studentDisplayName = sb.toString();

          // Description part 3: course display name

          sb = new StringBuilder();
          sb.append(course.getName());
          if (!StringUtils.isEmpty(course.getNameExtension())) {
            sb.append(String.format(" (%s)", course.getNameExtension()));
          }
          String courseDisplayName = sb.toString();

          worklistController.create(
              assessor,
              template,
              new Date(),
              String.format("%s - %s - %s", description, studentDisplayName, courseDisplayName),
              price,
              template.getFactor(),
              billingNumber,
              courseAssessment,
              sessionController.getUser());
        }
      }
    }
    
    return Response.ok(objectFactory.createModel(courseAssessment)).build();
  }
  
  @Path("/students/{STUDENTID:[0-9]*}/courses/{COURSEID:[0-9]*}/assessments/")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response listCourseAssessments(@PathParam("STUDENTID") Long studentId, @PathParam("COURSEID") Long courseId) {
    if (!sessionController.isLoggedIn()) {
      return Response.status(Status.FORBIDDEN).build();
    }
    
    Student student = studentController.findStudentById(studentId);
    if (student == null || student.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    Course course = courseController.findCourseById(courseId);
    if (course == null || course.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (!(UserUtils.isOwnerOf(sessionController.getUser(), student.getPerson()) || sessionController.hasPermission(CourseAssessmentPermissions.LIST_STUDENT_COURSEASSESSMENTS, course))) {
      return Response.status(Status.FORBIDDEN).build();
    } else {
      // User has the required permission, check if it's restricted to limited group of students 
      if (sessionController.hasEnvironmentPermission(StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION)) {
        StaffMember staffMember = sessionController.getUser() instanceof StaffMember ? (StaffMember) sessionController.getUser() : null;
        
        if (staffMember != null) {
          if (!(courseController.isCourseStaffMember(course, staffMember) || studentController.isStudentGuider(staffMember, student))) {
            return Response.status(Status.FORBIDDEN).build();
          }
        } else {
          return Response.status(Status.FORBIDDEN).build();
        }
      }
    }
    
    List<CourseAssessment> courseAssessments = assessmentController.listByCourseAndStudent(course, student);
    
    return Response.ok(objectFactory.createModel(courseAssessments)).build();
  }
  
  @Path("/students/{STUDENTID:[0-9]*}/courses/{COURSEID:[0-9]*}/assessments/{ID:[0-9]*}")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response findCourseAssessmentById(@PathParam("STUDENTID") Long studentId, @PathParam("COURSEID") Long courseId, @PathParam("ID") Long id) {
    if (!sessionController.isLoggedIn()) {
      return Response.status(Status.FORBIDDEN).build();
    }

    Student student = studentController.findStudentById(studentId);
    if (student == null || student.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    Course course = courseController.findCourseById(courseId);
    if (course == null || course.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (!(UserUtils.isOwnerOf(sessionController.getUser(), student.getPerson()) || sessionController.hasPermission(CourseAssessmentPermissions.FIND_COURSEASSESSMENT, course))) {
      return Response.status(Status.FORBIDDEN).build();
    } else {
      // User has the required permission, check if it's restricted to limited group of students 
      if (sessionController.hasEnvironmentPermission(StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION)) {
        StaffMember staffMember = sessionController.getUser() instanceof StaffMember ? (StaffMember) sessionController.getUser() : null;
        
        if (staffMember != null) {
          if (!(courseController.isCourseStaffMember(course, staffMember) || studentController.isStudentGuider(staffMember, student))) {
            return Response.status(Status.FORBIDDEN).build();
          }
        } else {
          return Response.status(Status.FORBIDDEN).build();
        }
      }
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
  @RESTPermit(handling = Handling.INLINE)
  public Response updateCourseAssessment(@PathParam("STUDENTID") Long studentId, @PathParam("COURSEID") Long courseId, @PathParam("ID") Long id, 
      fi.otavanopisto.pyramus.rest.model.CourseAssessment entity) {
    if (!sessionController.isLoggedIn()) {
      return Response.status(Status.FORBIDDEN).build();
    }
    
    Student student = studentController.findStudentById(studentId);
    if (student == null || student.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    Course course = courseController.findCourseById(courseId);
    CourseAssessment courseAssessment = assessmentController.findCourseAssessmentById(id);

    if (courseAssessment == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    if (course == null || course.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (!sessionController.hasPermission(CourseAssessmentPermissions.UPDATE_COURSEASSESSMENT, course)) {
      return Response.status(Status.FORBIDDEN).build();
    } else {
      // User has the required permission, check if it's restricted to limited group of students 
      if (sessionController.hasEnvironmentPermission(StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION)) {
        StaffMember staffMember = sessionController.getUser() instanceof StaffMember ? (StaffMember) sessionController.getUser() : null;
        
        if (staffMember != null) {
          if (!(courseController.isCourseStaffMember(course, staffMember) || studentController.isStudentGuider(staffMember, student))) {
            return Response.status(Status.FORBIDDEN).build();
          }
        } else {
          return Response.status(Status.FORBIDDEN).build();
        }
      }
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
  @RESTPermit(handling = Handling.INLINE)
  public Response deleteCourseAssessment(
      @PathParam("STUDENTID") Long studentId, 
      @PathParam("COURSEID") Long courseId, 
      @PathParam("ID") Long id,
      @DefaultValue("false") @QueryParam("permanent") Boolean permanent
      ) {
    
    Student student = studentController.findStudentById(studentId);
    Course course = courseController.findCourseById(courseId);

    if (Boolean.TRUE.equals(permanent) && !UserUtils.isAdmin(sessionController.getUser())) {
      // Allow permanent deletion only for admins
      return Response.status(Status.FORBIDDEN).build();
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
    
    if (!sessionController.hasPermission(CourseAssessmentPermissions.DELETE_COURSEASSESSMENT, course)) {
      return Response.status(Status.FORBIDDEN).build();
    }
    
    CourseAssessment courseAssessment = assessmentController.findCourseAssessmentById(id);
    
    if (courseAssessment == null) {
      return Response.status(Status.NOT_FOUND).build(); 
    }

    // #1198: Remove worklist entries based on this course assessment
    
    worklistController.removeByCourseAssessment(courseAssessment, permanent);
    
    // Do the actual delete
    
    if (Boolean.TRUE.equals(permanent)) {
      assessmentController.deleteCourseAssessment(courseAssessment);
    } else {
      assessmentController.archiveCourseAssessment(courseAssessment);
    }
    
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
  public Response listCourseAssessmentRequests(@PathParam("STUDENTID") Long studentId, @PathParam("COURSEID") Long courseId, @DefaultValue("false") @QueryParam("archived") Boolean archived) {
    Student student = studentController.findStudentById(studentId);

    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK)
      return Response.status(studentStatus).build();

    if (!restSecurity.hasPermission(new String[] { CourseAssessmentPermissions.LIST_COURSEASSESSMENTREQUESTS, PersonPermissions.PERSON_OWNER }, student.getPerson(), Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }

    Course course = courseController.findCourseById(courseId);
    
    if (course == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (course.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    List<CourseAssessmentRequest> assessmentRequests = null;
    
    if (archived.equals(Boolean.TRUE)) {
      assessmentRequests = assessmentController.listCourseAssessmentRequestsIncludingArchivedByCourseAndStudent(course, student);
    } else {
      assessmentRequests = assessmentController.listCourseAssessmentRequestsByCourseAndStudent(course, student);
    }
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

    if (!restSecurity.hasPermission(new String[] { CourseAssessmentPermissions.LIST_ALL_STUDENT_COURSEASSESSMENTS, StudentPermissions.STUDENT_OWNER }, student, Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }

    List<CourseAssessment> assessments = assessmentController.listByStudent(student);
    
    return Response.ok(objectFactory.createModel(assessments)).build();
  }

  @Path("/students/{STUDENTID:[0-9]*}/linkedAssessments/")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response listStudentsLinkedCourseAssessments(@PathParam("STUDENTID") Long studentId) {
    Student student = studentController.findStudentById(studentId);

    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK)
      return Response.status(studentStatus).build();

    if (!restSecurity.hasPermission(new String[] { CourseAssessmentPermissions.LIST_ALL_STUDENT_COURSEASSESSMENTS, StudentPermissions.STUDENT_OWNER }, student, Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }

    List<CreditLink> assessments = assessmentController.listLinkedCreditsByStudent(student);
    
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
    if (!restSecurity.hasPermission(new String[] { CourseAssessmentPermissions.LIST_ALL_STUDENT_COURSEASSESSMENTS, PersonPermissions.PERSON_OWNER }, student.getPerson(), Style.OR)) {
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
    
    User loggedUser = sessionController.getUser();
    if (loggedUser == null) {
      return Response.status(Status.FORBIDDEN).build();
    }
    
    Student student = studentController.findStudentById(studentId);
    if (student == null || student.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (!(UserUtils.isOwnerOf(loggedUser, student.getPerson()) || sessionController.hasEnvironmentPermission(CourseAssessmentPermissions.LIST_ALL_STUDENT_COURSEASSESSMENTS))) {
      return Response.status(Status.FORBIDDEN).build();
    } else {
      // User has the required permission, check if it's restricted to limited group of students 
      if (sessionController.hasEnvironmentPermission(StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION)) {
        if (loggedUser instanceof StaffMember) {        
          if (!studentController.isStudentGuider((StaffMember) loggedUser, student)) {
            return Response.status(Status.FORBIDDEN).build();
          }
        } else {
          return Response.status(Status.FORBIDDEN).build();
        }
      }
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
  
  @Path("/students/{STUDENTID:[0-9]*}/courses/{COURSEID:[0-9]*}/assessmentRequests/latest")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response findLatestCourseAssessmentRequestByWorkspaceAndStudent(@PathParam("STUDENTID") Long studentId, @PathParam("COURSEID") Long courseId) {
    
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
    CourseStudent courseStudent = courseController.findCourseStudentByCourseAndStudent(course, student);
    CourseAssessmentRequest courseAssessmentRequest = assessmentController.findLatestCourseAssessmentRequestByCourseStudent(courseStudent);
    
    if (courseAssessmentRequest == null) {
      return Response.status(Status.NO_CONTENT).build();
    }
    
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
      entity.getArchived(),
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
  
  @Path("/students/{STUDENTID:[0-9]*}/guidanceCounselors")
  @GET
  @LoggedIn
  @RESTPermit(handling = Handling.INLINE)
  public Response listStudentGuidanceCounselors(
      @PathParam("STUDENTID") Long studentId, 
      @QueryParam("onlyMessageRecipients") @DefaultValue("false") Boolean onlyMessageRecipients) {
    User loggedUser = sessionController.getUser();
    if (loggedUser == null) {
      return Response.status(Status.FORBIDDEN).build();
    }

    Student student = studentController.findStudentById(studentId);

    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK) {
      return Response.status(studentStatus).build();
    }
    
    if (!restSecurity.hasPermission(new String[] { StudentGroupPermissions.LIST_STUDENTS_GUIDANCECOUNSELORS, StudentPermissions.STUDENT_OWNER, UserPermissions.STUDENT_PARENT }, student, Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }

    List<StudentGroupUser> guidanceCounselors = studentGroupController.listStudentGuidanceCounselors(student, onlyMessageRecipients);
    
    return Response.ok(objectFactory.createModel(guidanceCounselors)).build();
  }

  @Path("/students/{STUDENTID:[0-9]*}/specEdTeachers")
  @GET
  @LoggedIn
  @RESTPermit(handling = Handling.INLINE)
  public Response listStudentSpecEdTeachers(
      @PathParam("STUDENTID") Long studentId,
      @QueryParam("includeGuidanceCouncelors") @DefaultValue("false") Boolean includeGuidanceCouncelors,
      @QueryParam("onlyMessageRecipients") @DefaultValue("false") Boolean onlyMessageRecipients) {
    User loggedUser = sessionController.getUser();
    if (loggedUser == null) {
      return Response.status(Status.FORBIDDEN).build();
    }

    Student student = studentController.findStudentById(studentId);

    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK) {
      return Response.status(studentStatus).build();
    }
    
    if (!restSecurity.hasPermission(new String[] { StudentGroupPermissions.LIST_STUDENTS_SPECEDTEACHERS, StudentPermissions.STUDENT_OWNER }, student, Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }
    
    List<SpecEdTeacher> specEdTeachers = new ArrayList<>();
    
    // Special education teachers
    
    Set<Long> ids = staffMemberDAO.listByProperty(StaffMemberProperties.SPEC_ED_TEACHER.getKey(), "1").stream().map(StaffMember::getId).collect(Collectors.toSet());
    for (Long id : ids) {
      specEdTeachers.add(new SpecEdTeacher(id, false));
    }
    
    // Optional guidance councelors

    if (includeGuidanceCouncelors) {
      List<StudentGroupUser> guidanceCounselors = studentGroupController.listStudentGuidanceCounselors(student, onlyMessageRecipients);
      for (StudentGroupUser guidanceCounselor : guidanceCounselors) {
        if (!ids.contains(guidanceCounselor.getStaffMember().getId())) {
          specEdTeachers.add(new SpecEdTeacher(guidanceCounselor.getStaffMember().getId(), true));
        }
      }
    }
    
    return Response.ok(specEdTeachers).build();
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

    if (!restSecurity.hasPermission(new String[] { StudentPermissions.FIND_STUDENTCONTACTURL, StudentPermissions.STUDENT_OWNER }, student) && !restSecurity.hasPermission(new String[] { PersonPermissions.PERSON_OWNER }, student.getPerson() )) {
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

  @Path("/students/{STUDENTID:[0-9]*}/linkedTransferCredits")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response listStudentsLinkedTransferCredits(@PathParam("STUDENTID") Long studentId) {
    Student student = studentController.findStudentById(studentId);
    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK)
      return Response.status(studentStatus).build();

    if (!restSecurity.hasPermission(new String[] { StudentPermissions.LIST_STUDENT_TRANSFER_CREDITS, PersonPermissions.PERSON_OWNER }, student.getPerson(), Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }
    
    List<CreditLink> transferCredits = studentController.listStudentLinkedTransferCredits(student);
    
    return Response.status(Status.OK).entity(objectFactory.createModel(transferCredits)).build();
  }

  @Path("/students/{STUDENTID:[0-9]*}/courseActivity")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response getStudentStudyActivity(
      @PathParam("STUDENTID") Long studentId,
      @QueryParam("courseIds") String courseIds,
      @QueryParam("includeTransferCredits") boolean includeTransferCredits) {
    
    // Access check

    Student student = studentController.findStudentById(studentId);
    if (student == null || student.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    if (!restSecurity.hasPermission(new String[] { StudentPermissions.GET_STUDENT_COURSEACTIVITY, UserPermissions.USER_OWNER }, student, Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }
    if (!sessionController.hasEnvironmentPermission(OrganizationPermissions.ACCESS_ALL_ORGANIZATIONS)) {
      if (!UserUtils.isMemberOf(sessionController.getUser(), student.getOrganization())) {
        return Response.status(Status.FORBIDDEN).build();
      }
    }
    
    // Get courses of the student (all or those specified with courseIds)
    
    List<CourseStudent> courseStudents;
    if (StringUtils.isEmpty(courseIds)) {
      courseStudents = courseController.listByStudent(student);
    }
    else {
      courseStudents = new ArrayList<>();
      String[] courseIdArray = courseIds.split(",");
      for (int i = 0; i < courseIdArray.length; i++) {
        Course course = courseController.findCourseById(Long.valueOf(courseIdArray[i]));
        if (course != null) {
          CourseStudent courseStudent = courseController.findCourseStudentByCourseAndStudent(course, student);
          if (courseStudent != null) {
            courseStudents.add(courseStudent);
          }
          else {
            logger.warning(String.format("Course student not found asking activity for student %d in course %d", student.getId(), course.getId()));
          }
        }
      }
    }
    
    // Serve data
    
    CourseActivityInfo courseActivityInfo = studentController.listCourseActivities(student, courseStudents, includeTransferCredits);
    return Response.ok(courseActivityInfo).build();
  }
  
  @Path("/students/{ID:[0-9]*}/increaseStudyTime")
  @POST
  @RESTPermit(handling = Handling.INLINE)
  public Response increaseStudyTime(@PathParam("ID") Long id, @QueryParam("months") Integer months) {
    logger.info(String.format("Increasing student %d study time for %d months", id, months));

    // Permissions
    
    User loggedUser = sessionController.getUser();
    
    StaffMember staffMember = userController.findStaffMemberById(loggedUser.getId());
    
    if (staffMember == null) {
      logger.severe("Staff member not found");
      return Response.status(Status.BAD_REQUEST).entity("Staff member not found").build();
    }
    
    if (!staffMember.hasAnyRole(Role.TRUSTED_SYSTEM, Role.ADMINISTRATOR, Role.STUDY_PROGRAMME_LEADER)) {
      boolean amICounselor = studentController.amIGuidanceCounselor(id, staffMember);
      if (!amICounselor) {
        return Response.status(Status.FORBIDDEN).entity("Logged user does not have permission").build();
      }
    }
    
    // Validation

    if (months == null || months <= 0) {
      logger.severe("Invalid months");
      return Response.status(Status.BAD_REQUEST).entity("Invalid months").build();
    }
    Student student = studentController.findStudentById(id);
    if (student == null) {
      logger.severe("Student not found");
      return Response.status(Status.BAD_REQUEST).entity("Student not found").build();
    }

    // Update study time end

    Date studyTimeEnd = student.getStudyTimeEnd();
    if (studyTimeEnd == null) {
      logger.warning("Student has no study time end set. Defaulting to now");
      studyTimeEnd = new Date();
    }
    studyTimeEnd = DateUtils.addMonths(studyTimeEnd, months);
    student = studentController.updateStudyTimeEnd(student, studyTimeEnd);
    logger.info(String.format("Student %d study time end updated to %tF", id, studyTimeEnd));

    // return student
    
    return Response.ok(objectFactory.createModel(student)).build();    
  }

  @Path("/students/{STUDENTID:[0-9]*}/contactInfo")
  @GET
  @LoggedIn
  @RESTPermit(handling = Handling.INLINE)
  public Response getStudentContactInfo(@PathParam("STUDENTID") Long studentId) {
    Student student = studentController.findStudentById(studentId);
    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK) {
      return Response.status(studentStatus).build();
    }
    
    // Access check. Allowed for administrators, special education teachers, student's guidance counselors, student's teachers, or students themselves
    
    User loggedUser = sessionController.getUser();
    boolean accessible = loggedUser.getId().equals(student.getId());
    
    if (!accessible && loggedUser instanceof StudentParent) {
      StudentParent studentParent = (StudentParent) loggedUser;
      accessible = studentParent.isActiveParentOf(student);
    }
    
    if (!accessible && loggedUser instanceof StaffMember) {
      StaffMember staffMember = (StaffMember) loggedUser;

      if (!accessible) {
        accessible = staffMember.hasRole(Role.ADMINISTRATOR);
      }
      if (!accessible) {
        accessible = "1".equals(staffMember.getProperties().get(StaffMemberProperties.SPEC_ED_TEACHER.getKey()));
      }
      if (!accessible) {
        accessible = studentController.amIGuidanceCounselor(studentId, staffMember);
      }
      if (!accessible) {
        Set<Long> studentCourseIds = courseController.listByStudent(student).stream().map(cs -> cs.getCourse().getId()).collect(Collectors.toSet());
        Set<Long> staffMemberCourseIds = courseController.listCoursesByStaffMember(staffMember).stream().map(Course::getId).collect(Collectors.toSet());
        accessible = studentCourseIds.stream().filter(staffMemberCourseIds::contains).count() > 0;
      }
    }
    
    if (!accessible) {
      return Response.status(Status.FORBIDDEN).entity(String.format("User %d no access to student %d", loggedUser.getId(), studentId)).build();
    }
    
    // Contact info

    Person person = student.getPerson();
    ContactInfo contactInfo = student.getContactInfo();
    PhoneNumber phoneNumber = contactInfo.getPhoneNumbers().stream().filter(p -> Boolean.TRUE.equals(p.getDefaultNumber())).findFirst().orElse(null);
    Address address = contactInfo.getAddresses().stream().filter(a -> Boolean.TRUE.equals(a.getDefaultAddress())).findFirst().orElse(null);
    Email email = contactInfo.getEmails().stream().filter(e -> Boolean.TRUE.equals(e.getDefaultAddress())).findFirst().orElse(null);
    
    UserContactInfo userContactInfo = new UserContactInfo();
    userContactInfo.setFirstName(student.getFirstName());
    userContactInfo.setLastName(student.getLastName());
    userContactInfo.setDateOfBirth(person.getBirthday() == null ? null : new java.sql.Date(person.getBirthday().getTime()).toLocalDate());
    userContactInfo.setPhoneNumber(phoneNumber == null ? null : phoneNumber.getNumber());
    if (address != null) {
      userContactInfo.setAddressName(address.getName());
      userContactInfo.setStreetAddress(address.getStreetAddress());
      userContactInfo.setZipCode(address.getPostalCode());
      userContactInfo.setCity(address.getCity());
      userContactInfo.setCountry(address.getCountry());
    }
    userContactInfo.setEmail(email == null ? null : email.getAddress());

    return Response.ok(userContactInfo).build();
  }

  @Path("/students/{STUDENTID:[0-9]*}/guidanceRelation")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response getStudentGuidanceRelation(@PathParam("STUDENTID") Long studentId) {
    Student student = studentController.findStudentById(studentId);
    if (student == null) {
      return Response.status(Status.NOT_FOUND).entity("Student not found").build();
    }
    if (!restSecurity.hasPermission(new String[] { StudentPermissions.GET_STUDENT_GUIDANCE_RELATION, UserPermissions.USER_OWNER, UserPermissions.STUDENT_PARENT }, student, Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }

    boolean specEdTeacher = false;
    boolean guidanceCounselor = false;
    boolean courseTeacher = false;
    boolean studentParent = (sessionController.getUser() instanceof StudentParent && ((StudentParent) sessionController.getUser()).isActiveParentOf(student));

    if (sessionController.getUser() instanceof StaffMember) {
      StaffMember staffMember = (StaffMember) sessionController.getUser();
      
      specEdTeacher = "1".equals(staffMember.getProperties().get(StaffMemberProperties.SPEC_ED_TEACHER.getKey()));
      guidanceCounselor = studentController.amIGuidanceCounselor(studentId, staffMember);
      
      Set<Long> studentCourseIds = courseController.listByStudent(student).stream().map(cs -> cs.getCourse().getId()).collect(Collectors.toSet());
      Set<Long> staffMemberCourseIds = courseController.listCoursesByStaffMember(staffMember).stream().map(Course::getId).collect(Collectors.toSet());
      courseTeacher = studentCourseIds.stream().filter(staffMemberCourseIds::contains).count() > 0;
    }

    StudentGuidanceRelation relation = new StudentGuidanceRelation();
    relation.setSpecEdTeacher(specEdTeacher);
    relation.setGuidanceCounselor(guidanceCounselor);
    relation.setCourseTeacher(courseTeacher);
    relation.setStudentParent(studentParent);
    return Response.ok(relation).build();
  }

  @Path("/students/{STUDENTID:[0-9]*}/courseStats")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response getStudentCourseStats(
      @PathParam("STUDENTID") Long studentId,
      @QueryParam("educationTypeCode") String educationTypeCode,
      @QueryParam("educationSubtypeCode") String educationSubtypeCode) {
    StudentCourseStats response = new StudentCourseStats();
    
    Student student = studentController.findStudentById(studentId);
    if (student == null) {
      return Response.status(Status.NOT_FOUND).entity("Not found").build();
    }
    
    if (!restSecurity.hasPermission(new String[] { StudentPermissions.FIND_STUDENT, UserPermissions.USER_OWNER, UserPermissions.STUDENT_PARENT }, student, Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }
    
    EducationType educationType = commonController.findEducationTypeByCode(educationTypeCode);
    if (educationType == null) {
      return Response.status(Status.BAD_REQUEST).entity("Education type does not exist").build();
    }
    
    EducationSubtype educationSubtype = commonController.findEducationSubtypeByCode(
        educationType,
        educationSubtypeCode);
    if (educationSubtype == null) {
      return Response.status(Status.BAD_REQUEST).entity("Education subtype does not exist").build();
    }
    
    // TODO StudentTOR might be able to solve this more elegantly
    
    int numCompletedCourses = assessmentController.getAcceptedCourseCount(
        student,
        null,
        educationType,
        educationSubtype,
        student.getCurriculum());

    numCompletedCourses += assessmentController.getAcceptedTransferCreditCount(
        student,
        null,
        true,
        student.getCurriculum());

    double numCreditPoints = 0;
    try {
      StudentTOR studentTOR = StudentTORController.constructStudentTOR(student);
      numCreditPoints = studentTOR.getTotalCourseLengths(TORCourseLengthUnit.op, true);
    } catch (Exception e) {
      logger.log(Level.SEVERE, "Fetching number of credit points failed", e);
    }
    
    response.setNumberCompletedCourses(numCompletedCourses);
    response.setNumberCreditPoints(numCreditPoints);
    
    return Response.ok(response).build();
  }
  
  @Path("/students/{STUDENTID:[0-9]*}/amICounselor")
  @GET
  @LoggedIn
  @RESTPermit(handling = Handling.INLINE)
  public Response amICounselor(@PathParam("STUDENTID") Long studentId) {
    Student student = studentController.findStudentById(studentId);
    Status studentStatus = checkStudent(student);
    
    if (studentStatus != Status.OK) {
      return Response.ok(false).build();
    }
    
    Boolean amICounselor = sessionController.getUser() instanceof StaffMember
        ? studentController.amIGuidanceCounselor(student.getId(), (StaffMember) sessionController.getUser())
        : Boolean.FALSE;
    
    return Response.ok(amICounselor).build();
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
    
    if (!restSecurity.hasPermission(new String[] { StudentPermissions.FIND_STUDENT, UserPermissions.USER_OWNER, UserPermissions.STUDENT_PARENT }, student, Style.OR)) {
      return Status.FORBIDDEN;
    }

    return Status.OK;
  }
  
  @Path("/students/{STUDENTID:[0-9]*}/subjectChoices")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response listStudentSubjectChoices(@PathParam("STUDENTID") Long studentId) {
    Student student = studentController.findStudentById(studentId);
    Status studentStatus = checkStudent(student);
    
    if (studentStatus != Status.OK)
      return Response.status(studentStatus).build();
    
    if (!restSecurity.hasPermission(new String[] { StudentPermissions.GET_STUDENT_SUBJECTCHOICES, UserPermissions.USER_OWNER, UserPermissions.STUDENT_PARENT }, student, Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }
    
    List<String> variableList = new ArrayList<>();
    
    List<UserVariable> variables = userController.listUserVariablesByUser(student);
    
    for (UserVariable variable : variables) {
      String variableKey = variable.getKey().getVariableKey();
      if(variableKey.equals(USERVARIABLE_SUBJECT_CHOICES_AIDINKIELI)) {
        variableList.add(variable.getValue());
      }
      
      if(variableKey.equals(USERVARIABLE_SUBJECT_CHOICES_KIELI_A)) {
        variableList.add(variable.getValue());
      }
      
      if(variableKey.equals(USERVARIABLE_SUBJECT_CHOICES_MATEMATIIKKA)) {
        variableList.add(variable.getValue());
      }
      
      if(variableKey.equals(USERVARIABLE_SUBJECT_CHOICES_USKONTO)) {
        variableList.add(variable.getValue());
      }
      
      if(variableKey.equals(USERVARIABLE_SUBJECT_CHOICES_KIELI_B1)) {
        variableList.add(variable.getValue());
      }
      
      if(variableKey.equals(USERVARIABLE_SUBJECT_CHOICES_KIELI_B2)) {
        variableList.add(variable.getValue());
      }
      
      if(variableKey.equals(USERVARIABLE_SUBJECT_CHOICES_KIELI_B3)) {
        variableList.add(variable.getValue());
      }
    }
    
    return Response.ok(variableList).build();
  }


}