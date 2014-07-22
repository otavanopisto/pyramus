package fi.pyramus.rest;

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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;

import fi.pyramus.domainmodel.base.EducationType;
import fi.pyramus.domainmodel.base.Language;
import fi.pyramus.domainmodel.base.Municipality;
import fi.pyramus.domainmodel.base.Nationality;
import fi.pyramus.domainmodel.base.School;
import fi.pyramus.domainmodel.base.StudyProgramme;
import fi.pyramus.domainmodel.base.StudyProgrammeCategory;
import fi.pyramus.domainmodel.base.Tag;
import fi.pyramus.domainmodel.base.VariableType;
import fi.pyramus.domainmodel.students.AbstractStudent;
import fi.pyramus.domainmodel.students.Sex;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.domainmodel.students.StudentActivityType;
import fi.pyramus.domainmodel.students.StudentContactLogEntry;
import fi.pyramus.domainmodel.students.StudentContactLogEntryType;
import fi.pyramus.domainmodel.students.StudentEducationalLevel;
import fi.pyramus.domainmodel.students.StudentExaminationType;
import fi.pyramus.domainmodel.students.StudentGroup;
import fi.pyramus.domainmodel.students.StudentStudyEndReason;
import fi.pyramus.domainmodel.students.StudentVariable;
import fi.pyramus.domainmodel.students.StudentVariableKey;
import fi.pyramus.rest.controller.AbstractStudentController;
import fi.pyramus.rest.controller.CommonController;
import fi.pyramus.rest.controller.LanguageController;
import fi.pyramus.rest.controller.MunicipalityController;
import fi.pyramus.rest.controller.NationalityController;
import fi.pyramus.rest.controller.SchoolController;
import fi.pyramus.rest.controller.StudentActivityTypeController;
import fi.pyramus.rest.controller.StudentContactLogEntryController;
import fi.pyramus.rest.controller.StudentController;
import fi.pyramus.rest.controller.StudentEducationalLevelController;
import fi.pyramus.rest.controller.StudentExaminationTypeController;
import fi.pyramus.rest.controller.StudentGroupController;
import fi.pyramus.rest.controller.StudentSubResourceController;
import fi.pyramus.rest.controller.StudentVariableController;
import fi.pyramus.rest.controller.StudyProgrammeCategoryController;
import fi.pyramus.rest.controller.StudyProgrammeController;
import fi.pyramus.rest.controller.TagController;
import fi.pyramus.rest.model.ObjectFactory;

@Path("/students")
@Produces("application/json")
@Consumes("application/json")
@Stateful
@RequestScoped
public class StudentRESTService extends AbstractRESTService {

  @Inject
  private StudentVariableController studentVariableController;

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
  private ObjectFactory objectFactory;

  @Path("/languages")
  @POST
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
  public Response findStudyProgrammeById(@PathParam("ID") Long id) {
    StudyProgramme studyProgramme = studyProgrammeController.findStudyProgrammeById(id);
    if (studyProgramme == null) {
      return Response.status(Status.NOT_FOUND).build();
    }    
    
    if (studyProgramme.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }    

    return Response.ok(objectFactory.createModel(studyProgramme)).build();
  }

  @Path("/studyProgrammes/{ID:[0-9]*}")
  @PUT
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

//  @Path("/endReasons")
//  @POST
//  public Response createStudentStudyEndReason(StudentStudyEndReasonEntity endReasonEntity) {
//      Long parentId = endReasonEntity.getParentReason_id();
//      if (parentId != null) {
//        StudentStudyEndReason parentReason = studentSubController.findStudentStudyEndReasonById(parentId);
//        String name = endReasonEntity.getName();
//        if (parentReason != null && !StringUtils.isBlank(name)) {
//          return Response.ok()
//              .entity(tranqualise(studentSubController.createStudentStudyEndReason(parentReason, name)))
//              .build();
//        } else {
//          return Response.status(500).build();
//        }
//      } else {
//        String name = endReasonEntity.getName();
//        if (!StringUtils.isBlank(name)) {
//          return Response.ok()
//              .entity(tranqualise(studentSubController.createStudentStudyEndReason(null, name)))
//              .build();
//        } else {
//          return Response.status(500).build();
//        }
//      }
//  }
//  
//  @Path("/variables")
//  @POST
//  public Response createStudentVariable(StudentVariableEntity studentVariableEntity) {
//    String value = studentVariableEntity.getValue();
//    Long studentId = studentVariableEntity.getStudent_id();
//    Long keyId = studentVariableEntity.getKey_id();
//    if (!StringUtils.isBlank(value) && studentId != null && keyId != null) {
//      Student student = studentController.findStudentById(studentId);
//      StudentVariableKey variableKey = variableController.findStudentVariableKeyById(keyId);
//      if (student != null) {
//        return Response.ok()
//            .entity(tranqualise(variableController.createStudentVariable(student, variableKey, value)))
//            .build();
//      } else {
//        return Response.status(Status.NOT_FOUND).build();
//      }
//    } else {
//      return Response.status(500).build();
//    }
//  }
//  
//  @Path("/abstractStudents")
//  @POST
//  public Response createAbstractStudent(AbstractStudentEntity abstractStudentEntity) {
//    Date birthday = abstractStudentEntity.getBirthday();
//    String socialSecurityNumber = abstractStudentEntity.getSocialSecurityNumber();
//    Sex sex = abstractStudentEntity.getSex();
//    String basicInfo = abstractStudentEntity.getBasicInfo();
//    boolean secureInfo = abstractStudentEntity.getSecureInfo();
//    
//    if (birthday != null && !StringUtils.isBlank(socialSecurityNumber) && sex != null && !StringUtils.isBlank(basicInfo)) {
//      return Response.ok()
//          .entity(tranqualise(abstractStudentController.createAbstractStudent(birthday, socialSecurityNumber, sex, basicInfo, secureInfo)))
//          .build();
//    } else  {
//      return Response.status(500).build();
//    }
//  }
//  
//  @Path("/students")
//  @POST
//  public Response createStudent(StudentEntity studentEntity) {
//    try {
//      AbstractStudent abstractStudent = abstractStudentController.findAbstractStudentById(studentEntity.getAbstractStudent_id());
//      String firstName = studentEntity.getFirstName();
//      String lastName = studentEntity.getLastName();
//      String nickname = studentEntity.getNickname();
//      String additionalInfo = studentEntity.getAdditionalInfo();
//      Date studyTimeEnd = studentEntity.getStudyTimeEnd();
//      StudentActivityType activityType = studentSubController.findStudentActivityTypeById(studentEntity.getActivityType_id());
//      StudentExaminationType examinationType = studentSubController.findStudentExaminationTypeById(studentEntity.getExaminationType_id());
//      StudentEducationalLevel educationalLevel = studentSubController.findStudentEducationalLevelById(studentEntity.getEducationalLevel_id());
//      String education = studentEntity.getEducation();
//      Nationality nationality = studentSubController.findNationalityById(studentEntity.getNationality_id());
//      Municipality municipality = studentSubController.findMunicipalityById(studentEntity.getMunicipality_id());
//      Language language = studentSubController.findLanguageById(studentEntity.getLanguage_id());
//      School school = schoolController.findSchoolById(studentEntity.getSchool_id());
//      StudyProgramme studyProgramme = studentSubController.findStudyProgrammeById(studentEntity.getStudyProgramme_id());
//      Double previousStudies = studentEntity.getPreviousStudies();
//      Date studyStartDate = studentEntity.getStudyStartDate();
//      Date studyEndDate = studentEntity.getStudyEndDate();
//      StudentStudyEndReason studyEndReason = studentSubController.findStudentStudyEndReasonById(studentEntity.getStudyEndReason_id());
//      String studyEndText = studentEntity.getStudyEndText();
//      boolean lodging = studentEntity.getLodging();
//      
//      return Response.ok()
//          .entity(tranqualise(studentController.createStudent(abstractStudent, firstName, lastName, nickname, additionalInfo, studyTimeEnd, activityType,
//                  examinationType, educationalLevel, education, nationality, municipality, language, school, studyProgramme, previousStudies, studyStartDate,
//                  studyEndDate, studyEndReason, studyEndText, lodging)))
//          .build();
//    } catch (Exception e) {
//      return Response.status(500).build();
//    }
//    
//  }
//  
//  @Path("/students/{ID:[0-9]*}/tags")
//  @POST
//  public Response createStudentTag(@PathParam("ID") Long id, TagEntity tagEntity) {
//    String text = tagEntity.getText();
//    Student student = studentController.findStudentById(id);
//    if (!StringUtils.isBlank(text) && student != null) {
//      return Response.ok()
//          .entity(tranqualise(studentController.createStudentTag(student, text)))
//          .build();
//    } else {
//      return Response.status(500).build();
//    }
//  }
//  
//  @Path("/students/{ID:[0-9]*}/contactLogEntries")
//  @POST
//  public Response createStudentContactLogEntry(@PathParam("ID") Long id, StudentContactLogEntryEntity contactLogEntryEntity) {
//    Student student = studentController.findStudentById(id);
//    StudentContactLogEntryType type = contactLogEntryEntity.getType();
//    String text = contactLogEntryEntity.getText();
//    Date entryDate = contactLogEntryEntity.getEntryDate();
//    String creator = getUser().getFullName();
//    
//    if (student != null && type != null && !StringUtils.isBlank(text) && entryDate != null &&!StringUtils.isBlank(creator)) {
//      return Response.ok()
//          .entity(tranqualise(contactLogEntryController.createContactLogEntry(student, type, text, entryDate, creator)))
//          .build();
//    } else {
//      return Response.status(500).build();
//    }
//  }
//  
//  @Path("/studentGroups")
//  @POST
//  public Response createStudentGroup(StudentGroupEntity studentGroupEntity) {
//    String name = studentGroupEntity.getName();
//    String description = studentGroupEntity.getDescription();
//    Date beginDate = studentGroupEntity.getBeginDate();
//    if (!StringUtils.isBlank(name) && !StringUtils.isBlank(description) && beginDate != null) {
//      return Response.ok()
//          .entity(tranqualise(studentGroupController.createStudentGroup(name, description, beginDate, getUser())))
//          .build();
//    } else {
//      return Response.status(500).build();
//    }
//  }
//  
//  @Path("/studentGroups/{ID:[0-9]*}/tags")
//  @POST
//  public Response createStudentGroupTag(@PathParam("ID") Long id, TagEntity tagEntity) {
//    String text = tagEntity.getText();
//    StudentGroup studentGroup = studentGroupController.findStudentGroupById(id);
//    if (!StringUtils.isBlank(text) && studentGroup != null) {
//      return Response.ok()
//          .entity(tranqualise(studentGroupController.createStudentGroupTag(studentGroup, text)))
//          .build();
//    } else {
//      return Response.status(500).build();
//    }
//  }
//  
//  @Path("/endReasons")
//  @GET
//  public Response findStudentStudyEndReasons(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
//    List<StudentStudyEndReason> endReasons;
//    if (filterArchived) {
//      endReasons = studentSubController.findUnarchivedStudentStudyEndReasons();
//    } else {
//      endReasons = studentSubController.findStudentStudyEndReasons();
//    }
//    if (!endReasons.isEmpty()) {
//      return Response.ok()
//          .entity(tranqualise(endReasons))
//          .build();
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//
//  @Path("/endReasons/{ID:[0-9]*}")
//  @GET
//  public Response findStudentStudyEndReasonById(@PathParam("ID") Long id) {
//    StudentStudyEndReason endReason = studentSubController.findStudentStudyEndReasonById(id);
//    if (endReason != null) {
//      return Response.ok()
//          .entity(tranqualise(endReason))
//          .build();
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//  

//  @Path("/variables")
//  @GET
//  public Response findStudentVariables() {
//    List<StudentVariable> studentVariables = variableController.findStudentVariables();
//    if (!studentVariables.isEmpty()) {
//      return Response.ok()
//          .entity(tranqualise(studentVariables))
//          .build();
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//  
//  @Path("students/{SID:[0-9]*}/variables/{ID:[0-9]*}")
//  @GET
//  public Response findStudentVariableById(@PathParam("SID") Long studentId, @PathParam("ID") Long variableId) {
//    StudentVariable studentVariable = variableController.findStudentVariableById(variableId);
//    if (studentVariable != null) {
//      if (studentId.equals(studentVariable.getStudent().getId())) {
//        return Response.ok()
//            .entity(tranqualise(studentVariable))
//            .build();
//      } else {
//        return Response.status(Status.NOT_FOUND).build();
//      }
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//  
//  @Path("/abstractStudents")
//  @GET
//  public Response findAbstractStudents(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
//    List<AbstractStudent> abstractStudents;
//    if (filterArchived) {
//      abstractStudents = abstractStudentController.findUnarchivedAbstractStudents();
//    } else {
//      abstractStudents = abstractStudentController.findAbstractStudents();
//    }
//    if (!abstractStudents.isEmpty()) {
//      return Response.ok()
//          .entity(tranqualise(abstractStudents))
//          .build();
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//  
//  @Path("/abstractStudents/{ID:[0-9]*}")
//  @GET
//  public Response findAbstractStudentById(@PathParam("ID") Long id) {
//    AbstractStudent abstractStudent = abstractStudentController.findAbstractStudentById(id);
//    if (abstractStudent != null) {
//      return Response.ok()
//          .entity(tranqualise(abstractStudent))
//          .build();
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//  
//  @Path("/abstractStudents/{ID:[0-9]*}/students")
//  @GET
//  public Response findStudentsByAbstractStudent(@PathParam("ID") Long id) {
//    AbstractStudent abstractStudent = abstractStudentController.findAbstractStudentById(id);
//    if (abstractStudent != null) {
//      return Response.ok()
//          .entity(tranqualise(studentController.findStudentByAbstractStudent(abstractStudent)))
//          .build();
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//  
//  @Path("/students")
//  @GET
//  public Response findStudents(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
//    List<Student> students;
//    if (filterArchived) {
//      students = studentController.findUnarchivedStudents();
//    } else {
//      students = studentController.findStudents();
//    }
//    if (!students.isEmpty()) {
//      return Response.ok()
//          .entity(tranqualise(students))
//          .build();
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//  
//  @Path("/students/{ID:[0-9]*}")
//  @GET
//  public Response findStudentById(@PathParam("ID") Long id) {
//    Student student = studentController.findStudentById(id);
//    if (student != null) {
//      return Response.ok()
//          .entity(tranqualise(student))
//          .build();
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//  
//  @Path("/students/{ID:[0-9]*}/abstractStudents")
//  @GET
//  public Response findAbstractStudentByStudent(@PathParam("ID") Long id) {
//    Student student = studentController.findStudentById(id);
//    if (student != null) {
//      return Response.ok()
//          .entity(tranqualise(student.getAbstractStudent()))
//          .build();
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//  
//  @Path("/students/{ID:[0-9]*}/tags")
//  @GET
//  public Response findStudentTags(@PathParam("ID") Long id) {
//    Student student = studentController.findStudentById(id);
//    if (student != null) {
//      return Response.ok()
//          .entity(tranqualise(studentController.findStudentTags(student)))
//          .build();
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//  
//  @Path("/students/{ID:[0-9]*}/contactLogEntries")
//  @GET
//  public Response findStudentContactLogEntriesByStudent(@PathParam("ID") Long id) {
//    Student student = studentController.findStudentById(id);
//    if (student != null) {
//      return Response.ok()
//          .entity(tranqualise(contactLogEntryController.findContactLogEntriesByStudent(student)))
//          .build();
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//  
//  @Path("/students/{SID:[0-9]*}/contactLogEntries/{ID:[0-9]*}")
//  @GET
//  public Response findStudentContactLogEntryById(@PathParam("SID") Long studentId, @PathParam("ID") Long id) {
//    Student student = studentController.findStudentById(studentId);
//    if (student != null) {
//      StudentContactLogEntry contactLogEntry = contactLogEntryController.findContactLogEntryByIdAndStudent(id, student);
//      if (contactLogEntry != null) {
//        return Response.ok()
//            .entity(tranqualise(contactLogEntry))
//            .build();
//      }
//    }
//    return Response.status(Status.NOT_FOUND).build();
//  }
//  
//  @Path("/studentGroups")
//  @GET
//  public Response findStudentGroups(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
//    List<StudentGroup> studentGroups;
//    if (filterArchived) {
//      studentGroups = studentGroupController.findUnarchivedStudentGroups();
//    } else {
//      studentGroups = studentGroupController.findStudentGroups();
//    }
//    if (!studentGroups.isEmpty()) {
//      return Response.ok()
//          .entity(tranqualise(studentGroups))
//          .build();
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//  
//  @Path("/studentGroups/{ID:[0-9]*}")
//  @GET
//  public Response findStudentGroupById(@PathParam("ID") Long id) {
//    StudentGroup studentGroup = studentGroupController.findStudentGroupById(id);
//    if (studentGroup != null) {
//      return Response.ok()
//          .entity(tranqualise(studentGroup))
//          .build();
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//  
//  @Path("/studentGroups/{ID:[0-9]*}/tags")
//  @GET
//  public Response findStudentGroupTags(@PathParam("ID") Long id) {
//    StudentGroup studentGroup = studentGroupController.findStudentGroupById(id);
//    if (studentGroup != null) {
//      return Response.ok()
//          .entity(tranqualise(studentGroupController.findStudentGroupTags(studentGroup)))
//          .build();
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }

//  @Path("/endReasons/{ID:[0-9]*}")
//  @PUT
//  public Response updateStudentStudyEndReason(@PathParam("ID") Long id, StudentStudyEndReasonEntity endReasonEntity) {
//    StudentStudyEndReason endReason = studentSubController.findStudentStudyEndReasonById(id);
//    if (endReason != null) {
//      String name = endReasonEntity.getName();
//      Long parentReasonId = endReasonEntity.getParentReason_id();
//      if (!StringUtils.isBlank(name)) {
//        return Response.ok()
//            .entity(tranqualise(studentSubController.updateStudentStudyEndReason(endReason, name)))
//            .build();
//      } else if (parentReasonId != null) {
//        StudentStudyEndReason parentReason = studentSubController.findStudentStudyEndReasonById(parentReasonId);
//        return Response.ok()
//            .entity(tranqualise(studentSubController.updateStudentStudyEndReasonParent(endReason, parentReason)))
//            .build();
//      } else {
//        return Response.status(500).build();
//      }
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
// 
//  @Path("students/{SID:[0-9]*}/variables/{ID:[0-9]*}")
//  @PUT
//  public Response updateStudentVariable(@PathParam("SID") Long studentId, @PathParam("ID") Long variableId, StudentVariableEntity studentVariableEntity) {
//    String value = studentVariableEntity.getValue();
//    StudentVariable studentVariable = variableController.findStudentVariableById(variableId);
//    if (!StringUtils.isBlank(value) && studentVariable != null) {
//      if(studentId.equals(studentVariable.getStudent().getId())) {
//        return Response.ok()
//            .entity(tranqualise(variableController.updateStudentVariable(studentVariable, value)))
//            .build();
//      } else {
//        return Response.status(Status.NOT_FOUND).build();
//      }
//    } else {
//      return Response.status(500).build();
//    }
//  }
//  
//  @Path("/abstractStudents/{ID:[0-9]*}")
//  @PUT
//  public Response updateAbstractStudent(@PathParam("ID") Long id, AbstractStudentEntity abstractStudentEntity) {
//    AbstractStudent abstractStudent = abstractStudentController.findAbstractStudentById(id);
//    Date birthday = abstractStudentEntity.getBirthday();
//    String socialSecurityNumber = abstractStudentEntity.getSocialSecurityNumber();
//    Sex sex = abstractStudentEntity.getSex();
//    String basicInfo = abstractStudentEntity.getBasicInfo();
//    boolean secureInfo = abstractStudentEntity.getSecureInfo();
//    
//    if (abstractStudent != null &&birthday != null && !StringUtils.isBlank(socialSecurityNumber) && sex != null && !StringUtils.isBlank(basicInfo)) {
//      return Response.ok()
//          .entity(tranqualise(abstractStudentController.updateAbstractStudent(abstractStudent, birthday, socialSecurityNumber, sex, basicInfo, secureInfo)))
//          .build();
//    } else  {
//      return Response.status(500).build();
//    }
//  }
//  
//  @Path("/students/{ID:[0-9]*}")
//  @PUT
//  public Response updateStudent(@PathParam("ID") Long id, StudentEntity studentEntity) {
//    Student student = studentController.findStudentById(id);
//    if (student != null) {
//      if (studentEntity.getArchived() == null) {
//        try {
//          String firstName = studentEntity.getFirstName();
//          String lastName = studentEntity.getLastName();
//          String nickname = studentEntity.getNickname();
//          String additionalInfo = studentEntity.getAdditionalInfo();
//          Date studyTimeEnd = studentEntity.getStudyTimeEnd();
//          StudentActivityType activityType = studentSubController.findStudentActivityTypeById(studentEntity.getActivityType_id());
//          StudentExaminationType examinationType = studentSubController.findStudentExaminationTypeById(studentEntity.getExaminationType_id());
//          StudentEducationalLevel educationalLevel = studentSubController.findStudentEducationalLevelById(studentEntity.getEducationalLevel_id());
//          String education = studentEntity.getEducation();
//          Nationality nationality = studentSubController.findNationalityById(studentEntity.getNationality_id());
//          Municipality municipality = studentSubController.findMunicipalityById(studentEntity.getMunicipality_id());
//          Language language = studentSubController.findLanguageById(studentEntity.getLanguage_id());
//          School school = schoolController.findSchoolById(studentEntity.getSchool_id());
//          StudyProgramme studyProgramme = studentSubController.findStudyProgrammeById(studentEntity.getStudyProgramme_id());
//          Double previousStudies = studentEntity.getPreviousStudies();
//          Date studyStartDate = studentEntity.getStudyStartDate();
//          Date studyEndDate = studentEntity.getStudyEndDate();
//          StudentStudyEndReason studyEndReason = studentSubController.findStudentStudyEndReasonById(studentEntity.getStudyEndReason_id());
//          String studyEndText = studentEntity.getStudyEndText();
//          boolean lodging = studentEntity.getLodging();
//          
//          return Response.ok()
//              .entity(tranqualise(studentController.updateStudent(student, firstName, lastName, nickname, additionalInfo, studyTimeEnd, activityType,
//                      examinationType, educationalLevel, education, nationality, municipality, language, school, studyProgramme, previousStudies,
//                      studyStartDate, studyEndDate, studyEndReason, studyEndText, lodging)))
//              .build();
//          
//        } catch (Exception e) {
//          return Response.status(500).build();
//        }
//      } else if (!studentEntity.getArchived()){ 
//        return Response.ok()
//            .entity(tranqualise(studentController.unarchiveStudent(student, getUser())))
//            .build();
//      } else {
//        return Response.status(500).build();
//      }
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//  
//  @Path("/students/{SID:[0-9]*}/contactLogEntries/{ID:[0-9]*}")
//  @PUT
//  public Response updateStudentContactLogEntry(@PathParam("SID") Long studentId, @PathParam("ID") Long id, StudentContactLogEntryEntity contactLogEntryEntity) {
//    Student student = studentController.findStudentById(studentId);
//    if (student != null) {
//      StudentContactLogEntry contactLogEntry = contactLogEntryController.findContactLogEntryByIdAndStudent(id, student);
//      if (contactLogEntry != null) {
//        StudentContactLogEntryType type = contactLogEntryEntity.getType();
//        String text = contactLogEntryEntity.getText();
//        Date entryDate = contactLogEntryEntity.getEntryDate();
//        String creator = getUser().getFullName();
//        
//        if (student != null && type != null && !StringUtils.isBlank(text) && entryDate != null &&!StringUtils.isBlank(creator)) {
//          return Response.ok()
//              .entity(tranqualise(contactLogEntryController.updateContactLogEntry(contactLogEntry, type, text, entryDate, creator)))
//              .build();
//        } else {
//          return Response.status(500).build();
//        }
//      }
//    }
//    return Response.status(Status.NOT_FOUND).build();
//  }
//  
//  @Path("/studentGroups/{ID:[0-9]*}")
//  @PUT
//  public Response updateStudentGroup(@PathParam("ID") Long id, StudentGroupEntity studentGroupEntity) {
//    StudentGroup studentGroup = studentGroupController.findStudentGroupById(id);
//    if (studentGroup != null) {
//      if (studentGroupEntity.getArchived() == null) {
//        String name = studentGroupEntity.getName();
//        String description = studentGroupEntity.getDescription();
//        Date beginDate = studentGroupEntity.getBeginDate();
//        if (!StringUtils.isBlank(name) && !StringUtils.isBlank(description) && beginDate != null) {
//          return Response.ok()
//              .entity(tranqualise(studentGroupController.updateStudentGroup(studentGroup,name, description, beginDate, getUser())))
//              .build();
//        } else {
//          return Response.status(500).build();
//        }
//      } else if (!studentGroupEntity.getArchived()) {
//        return Response.ok()
//            .entity(tranqualise(studentGroupController.unarchiveStudentGroup(studentGroup, getUser())))
//            .build();
//      } else {
//        return Response.status(500).build();
//      }
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//  
//  @Path("/students/{ID:[0-9]*}")
//  @DELETE
//  public Response archiveStudent(@PathParam("ID") Long id) {
//    Student student = studentController.findStudentById(id);
//    if (student != null) {
//      return Response.ok()
//          .entity(tranqualise(studentController.archiveStudent(student, getUser())))
//          .build();
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//  
//  @Path("/studentGroups/{ID:[0-9]*}")
//  @DELETE
//  public Response archiveStudentGroup(@PathParam("ID") Long id) {
//    StudentGroup studentGroup = studentGroupController.findStudentGroupById(id);
//    if (studentGroup != null) {
//      return Response.ok()
//          .entity(tranqualise(studentGroupController.archiveStudentGroup(studentGroup, getUser())))
//          .build();
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//  
//  @Path("/students/{SID:[0-9]*}/tags/{TID:[0-9]*}")
//  @DELETE
//  public Response deleteStudentTag(@PathParam("SID") Long studentId, @PathParam("TID") Long tagId) {
//    Student student = studentController.findStudentById(studentId);
//    Tag tag = tagController.findTagById(tagId);
//    if (student != null && tag != null) {
//      studentController.removeStudentTag(student, tag);
//      return Response.status(200).build();
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//  
//  @Path("/studentGroups/{SID:[0-9]*}/tags/{TID:[0-9]*}")
//  @DELETE
//  public Response deleteStudentGroupTag(@PathParam("SID") Long studentGroupId, @PathParam("TID") Long tagId) {
//    StudentGroup studentGroup = studentGroupController.findStudentGroupById(studentGroupId);
//    Tag tag = tagController.findTagById(tagId);
//    if (studentGroup != null && tag != null) {
//      studentGroupController.removeStudentGroupTag(studentGroup, tag);
//      return Response.status(200).build();
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//  
  

  
  @Path("/variables")
  @POST
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
    
    StudentVariableKey studentVariableKey = studentVariableController.createStudentVariableKey(entity.getKey(), entity.getName(), variableType, entity.getUserEditable());
    return Response.ok(objectFactory.createModel(studentVariableKey)).build();
  }
  
  @Path("/variables")
  @GET
  public Response listVariables() {
    List<StudentVariableKey> variableKeys = studentVariableController.listStudentVariableKeys();
    if (variableKeys.isEmpty()) {
      return Response.noContent().build();
    }
    
    return Response.ok(objectFactory.createModel(variableKeys)).build();
  }
  
  @Path("/variables/{KEY}")
  @GET
  public Response findVariable(@PathParam ("KEY") String key) {
    StudentVariableKey studentVariableKey = studentVariableController.findStudentVariableKeyByVariableKey(key);
    if (studentVariableKey == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    return Response.ok(objectFactory.createModel(studentVariableKey)).build();
  }
  
  @Path("/variables/{KEY}")
  @PUT
  public Response updateVariable(@PathParam ("KEY") String key, fi.pyramus.rest.model.VariableKey entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    if (StringUtils.isBlank(entity.getName())||(entity.getType() == null)||(entity.getUserEditable() == null)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    StudentVariableKey studentVariableKey = studentVariableController.findStudentVariableKeyByVariableKey(key);
    if (studentVariableKey == null) {
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
    
    studentVariableController.updateStudentVariableKey(studentVariableKey, entity.getName(), variableType, entity.getUserEditable());
    
    return Response.ok(objectFactory.createModel(studentVariableKey)).build();
  }
  
  @Path("/variables/{KEY}")
  @DELETE
  public Response deleteVariable(@PathParam ("KEY") String key) {
    StudentVariableKey studentVariableKey = studentVariableController.findStudentVariableKeyByVariableKey(key);
    if (studentVariableKey == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    studentVariableController.deleteStudentVariableKey(studentVariableKey);
    
    return Response.noContent().build();
  }

}