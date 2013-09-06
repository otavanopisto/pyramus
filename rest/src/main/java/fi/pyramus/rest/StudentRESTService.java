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
import fi.pyramus.domainmodel.students.AbstractStudent;
import fi.pyramus.domainmodel.students.Sex;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.domainmodel.students.StudentActivityType;
import fi.pyramus.domainmodel.students.StudentEducationalLevel;
import fi.pyramus.domainmodel.students.StudentExaminationType;
import fi.pyramus.domainmodel.students.StudentGroup;
import fi.pyramus.domainmodel.students.StudentStudyEndReason;
import fi.pyramus.rest.controller.AbstractStudentController;
import fi.pyramus.rest.controller.CommonController;
import fi.pyramus.rest.controller.SchoolController;
import fi.pyramus.rest.controller.StudentController;
import fi.pyramus.rest.controller.StudentGroupController;
import fi.pyramus.rest.controller.StudentSubResourceController;
import fi.pyramus.rest.controller.TagController;
import fi.pyramus.rest.tranquil.base.LanguageEntity;
import fi.pyramus.rest.tranquil.base.MunicipalityEntity;
import fi.pyramus.rest.tranquil.base.NationalityEntity;
import fi.pyramus.rest.tranquil.base.StudyProgrammeCategoryEntity;
import fi.pyramus.rest.tranquil.base.StudyProgrammeEntity;
import fi.pyramus.rest.tranquil.base.TagEntity;
import fi.pyramus.rest.tranquil.students.AbstractStudentEntity;
import fi.pyramus.rest.tranquil.students.StudentActivityTypeEntity;
import fi.pyramus.rest.tranquil.students.StudentEducationalLevelEntity;
import fi.pyramus.rest.tranquil.students.StudentEntity;
import fi.pyramus.rest.tranquil.students.StudentExaminationTypeEntity;
import fi.pyramus.rest.tranquil.students.StudentGroupEntity;
import fi.pyramus.rest.tranquil.students.StudentStudyEndReasonEntity;
import fi.tranquil.TranquilityBuilderFactory;

@Path("/students")
@Produces("application/json")
@Consumes("application/json")
@Stateful
@RequestScoped
public class StudentRESTService extends AbstractRESTService {
  @Inject
  private TranquilityBuilderFactory tranquilityBuilderFactory;
  @Inject
  AbstractStudentController abstractStudentController;
  @Inject
  StudentSubResourceController studentSubController;
  @Inject
  CommonController commonController;
  @Inject
  StudentController studentController;
  @Inject
  SchoolController schoolController;
  @Inject
  TagController tagController;
  @Inject
  StudentGroupController studentGroupController;
  
  @Path("/languages")
  @POST
  public Response createLanguage(LanguageEntity languageEntity) {
    String name = languageEntity.getName();
    String code = languageEntity.getCode();
    if (!StringUtils.isBlank(name) && !StringUtils.isBlank(code)) {
      return Response.ok()
          .entity(tranqualise(studentSubController.createLanguage(name, code)))
          .build();
    } else {
      return Response.status(500).build();
    }
  }
  
  @Path("/municipalities")
  @POST
  public Response createMunicipality(MunicipalityEntity municipalityEntity) {
    String name = municipalityEntity.getName();
    String code = municipalityEntity.getCode();
    if (!StringUtils.isBlank(name) && !StringUtils.isBlank(code)) {
      return Response.ok()
          .entity(tranqualise(studentSubController.createMunicipality(name, code)))
          .build();
    } else {
      return Response.status(500).build();
    }
  }
  
  @Path("/nationalities")
  @POST
  public Response createNationalities(NationalityEntity nationalityEntity) {
    String name = nationalityEntity.getName();
    String code = nationalityEntity.getCode();
    if (!StringUtils.isBlank(name) && !StringUtils.isBlank(code)) {
      return Response.ok()
          .entity(tranqualise(studentSubController.createNationality(name, code)))
          .build();
    } else {
      return Response.status(500).build();
    }
  }
  
  @Path("/studentActivityTypes")
  @POST
  public Response createStudentActivityType(StudentActivityTypeEntity activityTypeEntity) {
    String name = activityTypeEntity.getName();
    if (!StringUtils.isBlank(name)) {
      return Response.ok()
          .entity(tranqualise(studentSubController.createStudentActivityType(name)))
          .build();
    } else {
      return Response.status(500).build();
    }
  }
  
  @Path("/educationalLevels")
  @POST
  public Response createStudentEducationalLevel(StudentEducationalLevelEntity educationalLevelEntity) {
    String name = educationalLevelEntity.getName();
    if (!StringUtils.isBlank(name)) {
      return Response.ok()
          .entity(tranqualise(studentSubController.createStudentEducationalLevel(name)))
          .build();
    } else {
      return Response.status(500).build();
    }
  }
  
  @Path("/examinationTypes")
  @POST
  public Response createStudentExaminationType(StudentExaminationTypeEntity examinationTypeEntity) {
    String name = examinationTypeEntity.getName();
    if (!StringUtils.isBlank(name)) {
      return Response.ok()
          .entity(tranqualise(studentSubController.createStudentExaminationType(name)))
          .build();
    } else {
      return Response.status(500).build();
    }
  }
  
  @Path("/endReasons")
  @POST
  public Response createStudentStudyEndReason(StudentStudyEndReasonEntity endReasonEntity) {
      Long parentId = endReasonEntity.getParentReason_id();
      if (parentId != null) {
        StudentStudyEndReason parentReason = studentSubController.findStudentStudyEndReasonById(parentId);
        String name = endReasonEntity.getName();
        if (parentReason != null && !StringUtils.isBlank(name)) {
          return Response.ok()
              .entity(tranqualise(studentSubController.createStudentStudyEndReason(parentReason, name)))
              .build();
        } else {
          return Response.status(500).build();
        }
      } else {
        String name = endReasonEntity.getName();
        if (!StringUtils.isBlank(name)) {
          return Response.ok()
              .entity(tranqualise(studentSubController.createStudentStudyEndReason(null, name)))
              .build();
        } else {
          return Response.status(500).build();
        }
      }
  }
  
  @Path("/studyProgrammeCategories")
  @POST
  public Response createStudyProgrammeCategory(StudyProgrammeCategoryEntity studyProgrammeCategoryEntity) {
    String name = studyProgrammeCategoryEntity.getName();
    Long educationTypeId = studyProgrammeCategoryEntity.getEducationType_id();
    if (educationTypeId != null && !StringUtils.isBlank(name)) {
      EducationType educationType = commonController.findEducationTypeById(educationTypeId);
      if (educationType != null) {
        return Response.ok()
            .entity(tranqualise(studentSubController.createStudyProgrammeCategory(name, educationType)))
            .build();
      } else {
        return Response.status(Status.NOT_FOUND).build();
      }
    } else {
      return Response.status(500).build();
    }
  }
  
  @Path("/studyProgrammes")
  @POST
  public Response createStudyProgramme(StudyProgrammeEntity studyProgrammeEntity) {
    Long categoryId = studyProgrammeEntity.getCategory_id();
    String name = studyProgrammeEntity.getName();
    String code = studyProgrammeEntity.getCode();
    if (categoryId != null && !StringUtils.isBlank(name) && !StringUtils.isBlank(code)) {
      StudyProgrammeCategory category = studentSubController.findStudyProgrammeCategoryById(categoryId);
      if (category != null) {
        return Response.ok()
            .entity(tranqualise(studentSubController.createStudyProgramme(name, category, code)))
            .build();
      } else {
        return Response.status(Status.NOT_FOUND).build();
      }
    } else {
      return Response.status(500).build();
    }
  }
  
  @Path("/abstractStudents")
  @POST
  public Response createAbstractStudent(AbstractStudentEntity abstractStudentEntity) {
    Date birthday = abstractStudentEntity.getBirthday();
    String socialSecurityNumber = abstractStudentEntity.getSocialSecurityNumber();
    Sex sex = abstractStudentEntity.getSex();
    String basicInfo = abstractStudentEntity.getBasicInfo();
    boolean secureInfo = abstractStudentEntity.getSecureInfo();
    
    if (birthday != null && !StringUtils.isBlank(socialSecurityNumber) && sex != null && !StringUtils.isBlank(basicInfo)) {
      return Response.ok()
          .entity(tranqualise(abstractStudentController.createAbstractStudent(birthday, socialSecurityNumber, sex, basicInfo, secureInfo)))
          .build();
    } else  {
      return Response.status(500).build();
    }
  }
  
  @Path("/students")
  @POST
  public Response createStudent(StudentEntity studentEntity) {
    try {
      AbstractStudent abstractStudent = abstractStudentController.findAbstractStudentById(studentEntity.getAbstractStudent_id());
      String firstName = studentEntity.getFirstName();
      String lastName = studentEntity.getLastName();
      String nickname = studentEntity.getNickname();
      String additionalInfo = studentEntity.getAdditionalInfo();
      Date studyTimeEnd = studentEntity.getStudyTimeEnd();
      StudentActivityType activityType = studentSubController.findStudentActivityTypeById(studentEntity.getActivityType_id());
      StudentExaminationType examinationType = studentSubController.findStudentExaminationTypeById(studentEntity.getExaminationType_id());
      StudentEducationalLevel educationalLevel = studentSubController.findStudentEducationalLevelById(studentEntity.getEducationalLevel_id());
      String education = studentEntity.getEducation();
      Nationality nationality = studentSubController.findNationalityById(studentEntity.getNationality_id());
      Municipality municipality = studentSubController.findMunicipalityById(studentEntity.getMunicipality_id());
      Language language = studentSubController.findLanguageById(studentEntity.getLanguage_id());
      School school = schoolController.findSchoolById(studentEntity.getSchool_id());
      StudyProgramme studyProgramme = studentSubController.findStudyProgrammeById(studentEntity.getStudyProgramme_id());
      Double previousStudies = studentEntity.getPreviousStudies();
      Date studyStartDate = studentEntity.getStudyStartDate();
      Date studyEndDate = studentEntity.getStudyEndDate();
      StudentStudyEndReason studyEndReason = studentSubController.findStudentStudyEndReasonById(studentEntity.getStudyEndReason_id());
      String studyEndText = studentEntity.getStudyEndText();
      boolean lodging = studentEntity.getLodging();
      
      return Response.ok()
          .entity(tranqualise(studentController.createStudent(abstractStudent, firstName, lastName, nickname, additionalInfo, studyTimeEnd, activityType,
                  examinationType, educationalLevel, education, nationality, municipality, language, school, studyProgramme, previousStudies, studyStartDate,
                  studyEndDate, studyEndReason, studyEndText, lodging)))
          .build();
    } catch (Exception e) {
      return Response.status(500).build();
    }
    
  }
  
  @Path("/students/{ID:[0-9]*}/tags")
  @POST
  public Response createStudentTag(@PathParam("ID") Long id, TagEntity tagEntity) {
    String text = tagEntity.getText();
    Student student = studentController.findStudentById(id);
    if (!StringUtils.isBlank(text) && student != null) {
      return Response.ok()
          .entity(tranqualise(studentController.createStudentTag(student, text)))
          .build();
    } else {
      return Response.status(500).build();
    }
  }
  
  @Path("/studentGroups")
  @POST
  public Response createStudentGroup(StudentGroupEntity studentGroupEntity) {
    String name = studentGroupEntity.getName();
    String description = studentGroupEntity.getDescription();
    Date beginDate = studentGroupEntity.getBeginDate();
    if (!StringUtils.isBlank(name) && !StringUtils.isBlank(description) && beginDate != null) {
      return Response.ok()
          .entity(tranqualise(studentGroupController.createStudentGroup(name, description, beginDate, getUser())))
          .build();
    } else {
      return Response.status(500).build();
    }
  }
  
  @Path("/studentGroups/{ID:[0-9]*}/tags")
  @POST
  public Response createStudentGroupTag(@PathParam("ID") Long id, TagEntity tagEntity) {
    String text = tagEntity.getText();
    StudentGroup studentGroup = studentGroupController.findStudentGroupById(id);
    if (!StringUtils.isBlank(text) && studentGroup != null) {
      return Response.ok()
          .entity(tranqualise(studentGroupController.createStudentGroupTag(studentGroup, text)))
          .build();
    } else {
      return Response.status(500).build();
    }
  }
  
  @Path("/languages")
  @GET
  public Response findLanguages(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<Language> languages;
    if (filterArchived) {
      languages = studentSubController.findUnarchivedLanguages();
    } else {
      languages = studentSubController.findLanguages();
    }
    if (!languages.isEmpty()) {
      return Response.ok()
          .entity(tranqualise(languages))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/languages/{ID:[0-9]*}")
  @GET
  public Response findLanguageByID(@PathParam("ID") Long id) {
    Language language = studentSubController.findLanguageById(id);
    if (language != null) {
      return Response.ok()
          .entity(tranqualise(language))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/municipalities")
  @GET
  public Response findMunicipalities(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<Municipality> municipalities;
    if (filterArchived) {
      municipalities = studentSubController.findUnarchivedMunicipalities();
    } else {
      municipalities = studentSubController.findMunicipalities();
    }
    if (!municipalities.isEmpty()) {
      return Response.ok()
          .entity(tranqualise(municipalities))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/municipalities/{ID:[0-9]*}")
  @GET
  public Response findMunicipalityById(@PathParam("ID") Long id) {
    Municipality municipality = studentSubController.findMunicipalityById(id);
    if (municipality != null) {
      return Response.ok()
          .entity(tranqualise(municipality))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/nationalities")
  @GET
  public Response findNationalities(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<Nationality> nationalities;
    if (filterArchived) {
      nationalities = studentSubController.findUnarchivedNationalities();
    } else {
      nationalities = studentSubController.findNationalities();
    }
    if (!nationalities.isEmpty()) {
      return Response.ok()
          .entity(tranqualise(nationalities))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/nationalities/{ID:[0-9]*}")
  @GET
  public Response findNationalityById(@PathParam("ID") Long id) {
    Nationality nationality = studentSubController.findNationalityById(id);
    if (nationality != null) {
      return Response.ok()
          .entity(tranqualise(nationality))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/studentActivityTypes")
  @GET
  public Response findStudentActivityTypes(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<StudentActivityType> activityTypes;
    if (filterArchived) {
      activityTypes = studentSubController.findUnarchivedStudentActivityTypes();
    } else {
      activityTypes = studentSubController.findStudentActivityTypes();
    }
    if (!activityTypes.isEmpty()) {
      return Response.ok()
          .entity(tranqualise(activityTypes))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/studentActivityTypes/{ID:[0-9]*}")
  @GET
  public Response findStudentActivityTypeById(@PathParam("ID") Long id) {
    StudentActivityType activityType = studentSubController.findStudentActivityTypeById(id);
    if (activityType != null) {
      return Response.ok()
          .entity(tranqualise(activityType))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/educationalLevels")
  @GET
  public Response findStudentEducationalLevels(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<StudentEducationalLevel> educationalLevels;
    if (filterArchived) {
      educationalLevels = studentSubController.findUnarchivedStudentEducationalLevels();
    } else {
      educationalLevels = studentSubController.findStudentEducationalLevels();
    }
    if (!educationalLevels.isEmpty()) {
      return Response.ok()
          .entity(tranqualise(educationalLevels))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/educationalLevels/{ID:[0-9]*}")
  @GET
  public Response findStudentEducationalLevelById(@PathParam("ID") Long id) {
    StudentEducationalLevel educationalLevel = studentSubController.findStudentEducationalLevelById(id);
    if (educationalLevel != null) {
      return Response.ok()
          .entity(tranqualise(educationalLevel))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/examinationTypes")
  @GET
  public Response findStudentExaminationTypes(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<StudentExaminationType> examinationTypes;
    if (filterArchived) {
      examinationTypes = studentSubController.findUnarchivedStudentExaminationTypes();
    } else {
      examinationTypes = studentSubController.findStudentExaminationTypes();
    }
    if (!examinationTypes.isEmpty()) {
      return Response.ok()
          .entity(tranqualise(examinationTypes))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/examinationTypes/{ID:[0-9]*}")
  @GET
  public Response findStudentExaminationTypes(@PathParam("ID") Long id) {
    StudentExaminationType studentExaminationType = studentSubController.findStudentExaminationTypeById(id);
    if (studentExaminationType != null) {
      return Response.ok()
          .entity(tranqualise(studentExaminationType))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/endReasons")
  @GET
  public Response findStudentStudyEndReasons(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<StudentStudyEndReason> endReasons;
    if (filterArchived) {
      endReasons = studentSubController.findUnarchivedStudentStudyEndReasons();
    } else {
      endReasons = studentSubController.findStudentStudyEndReasons();
    }
    if (!endReasons.isEmpty()) {
      return Response.ok()
          .entity(tranqualise(endReasons))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }

  @Path("/endReasons/{ID:[0-9]*}")
  @GET
  public Response findStudentStudyEndReasonById(@PathParam("ID") Long id) {
    StudentStudyEndReason endReason = studentSubController.findStudentStudyEndReasonById(id);
    if (endReason != null) {
      return Response.ok()
          .entity(tranqualise(endReason))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/studyProgrammeCategories")
  @GET
  public Response findStudyProgrammeCategories(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<StudyProgrammeCategory> studyProgrammeCategories;
    if (filterArchived) {
      studyProgrammeCategories = studentSubController.findUnarchivedStudyProgrammeCategories();
    } else {
      studyProgrammeCategories = studentSubController.findStudyProgrammeCategories();
    }
    if (!studyProgrammeCategories.isEmpty()) {
      return Response.ok()
          .entity(tranqualise(studyProgrammeCategories))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/studyProgrammeCategories/{ID:[0-9]*}")
  @GET
  public Response findStudyProgrammeCategoryById(@PathParam("ID") Long id) {
    StudyProgrammeCategory studyProgrammeCategory = studentSubController.findStudyProgrammeCategoryById(id);
    if (studyProgrammeCategory != null) {
      return Response.ok()
          .entity(tranqualise(studyProgrammeCategory))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/studyProgrammes")
  @GET
  public Response findStudyProgrammes(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<StudyProgramme> studyProgrammes;
    if (filterArchived) {
      studyProgrammes = studentSubController.findUnarchivedStudyProgrammes();
    } else {
      studyProgrammes = studentSubController.findStudyProgrammes();
    }
    if (!studyProgrammes.isEmpty()) {
      return Response.ok()
          .entity(tranqualise(studyProgrammes))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/studyProgrammes/{ID:[0-9]*}")
  @GET
  public Response findStudyProgrammeById(@PathParam("ID") Long id) {
    StudyProgramme studyProgramme = studentSubController.findStudyProgrammeById(id);
    if (studyProgramme != null) {
      return Response.ok()
          .entity(tranqualise(studyProgramme))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/abstractStudents")
  @GET
  public Response findAbstractStudents(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<AbstractStudent> abstractStudents;
    if (filterArchived) {
      abstractStudents = abstractStudentController.findUnarchivedAbstractStudents();
    } else {
      abstractStudents = abstractStudentController.findAbstractStudents();
    }
    if (!abstractStudents.isEmpty()) {
      return Response.ok()
          .entity(tranqualise(abstractStudents))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/abstractStudents/{ID:[0-9]*}")
  @GET
  public Response findAbstractStudentById(@PathParam("ID") Long id) {
    AbstractStudent abstractStudent = abstractStudentController.findAbstractStudentById(id);
    if (abstractStudent != null) {
      return Response.ok()
          .entity(tranqualise(abstractStudent))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/abstractStudents/{ID:[0-9]*}/students")
  @GET
  public Response findStudentsByAbstractStudent(@PathParam("ID") Long id) {
    AbstractStudent abstractStudent = abstractStudentController.findAbstractStudentById(id);
    if (abstractStudent != null) {
      return Response.ok()
          .entity(tranqualise(studentController.findStudentByAbstractStudent(abstractStudent)))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/students")
  @GET
  public Response findStudents(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<Student> students;
    if (filterArchived) {
      students = studentController.findUnarchivedStudents();
    } else {
      students = studentController.findStudents();
    }
    if (!students.isEmpty()) {
      return Response.ok()
          .entity(tranqualise(students))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/students/{ID:[0-9]*}")
  @GET
  public Response findStudentById(@PathParam("ID") Long id) {
    Student student = studentController.findStudentById(id);
    if (student != null) {
      return Response.ok()
          .entity(tranqualise(student))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/students/{ID:[0-9]*}/abstractStudents")
  @GET
  public Response findAbstractStudentByStudent(@PathParam("ID") Long id) {
    Student student = studentController.findStudentById(id);
    if (student != null) {
      return Response.ok()
          .entity(tranqualise(student.getAbstractStudent()))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/students/{ID:[0-9]*}/tags")
  @GET
  public Response findStudentTags(@PathParam("ID") Long id) {
    Student student = studentController.findStudentById(id);
    if (student != null) {
      return Response.ok()
          .entity(tranqualise(studentController.findStudentTags(student)))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/studentGroups")
  @GET
  public Response findStudentGroups(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<StudentGroup> studentGroups;
    if (filterArchived) {
      studentGroups = studentGroupController.findUnarchivedStudentGroups();
    } else {
      studentGroups = studentGroupController.findStudentGroups();
    }
    if (!studentGroups.isEmpty()) {
      return Response.ok()
          .entity(tranqualise(studentGroups))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/studentGroups/{ID:[0-9]*}")
  @GET
  public Response findStudentGroupById(@PathParam("ID") Long id) {
    StudentGroup studentGroup = studentGroupController.findStudentGroupById(id);
    if (studentGroup != null) {
      return Response.ok()
          .entity(tranqualise(studentGroup))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/studentGroups/{ID:[0-9]*}/tags")
  @GET
  public Response findStudentGroupTags(@PathParam("ID") Long id) {
    StudentGroup studentGroup = studentGroupController.findStudentGroupById(id);
    if (studentGroup != null) {
      return Response.ok()
          .entity(tranqualise(studentGroupController.findStudentGroupTags(studentGroup)))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/languages/{ID:[0-9]*}")
  @PUT
  public Response updateLanguage(@PathParam("ID") Long id, LanguageEntity languageEntity) {
    Language language = studentSubController.findLanguageById(id);
    if (language != null) {
      String name = languageEntity.getName();
      String code = languageEntity.getCode();
      if (!StringUtils.isBlank(name) && !StringUtils.isBlank(code)) {
        return Response.ok()
            .entity(tranqualise(studentSubController.updateLanguage(language, name, code)))
            .build();
      } else {
        return Response.status(500).build();
      }
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/municipalities/{ID:[0-9]*}")
  @PUT
  public Response updateMunicipality(@PathParam("ID") Long id, MunicipalityEntity municipalityEntity) {
    Municipality municipality = studentSubController.findMunicipalityById(id);
    if (municipality != null) {
      String name = municipalityEntity.getName();
      String code = municipalityEntity.getCode();
      if (!StringUtils.isBlank(name) && !StringUtils.isBlank(code)) {
        return Response.ok()
            .entity(tranqualise(studentSubController.updateMunicipality(municipality, name, code)))
            .build();
      } else {
        return Response.status(500).build();
      }
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/nationalities/{ID:[0-9]*}")
  @PUT
  public Response updateNationality(@PathParam("ID") Long id, NationalityEntity nationalityEntity) {
    Nationality nationality = studentSubController.findNationalityById(id);
    if (nationality != null) {
      String name = nationalityEntity.getName();
      String code = nationalityEntity.getCode();
      if (!StringUtils.isBlank(name) && !StringUtils.isBlank(code)) {
        return Response.ok()
            .entity(tranqualise(studentSubController.updateNationality(nationality, name, code)))
            .build();
      } else {
        return Response.status(500).build();
      }
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/studentActivityTypes/{ID:[0-9]*}")
  @PUT
  public Response updateStudentActivityType(@PathParam("ID") Long id, StudentActivityTypeEntity activityTypeEntity) {
    StudentActivityType activityType = studentSubController.findStudentActivityTypeById(id);
    if (activityType != null) {
      String name = activityTypeEntity.getName();
      if (!StringUtils.isBlank(name)) {
        return Response.ok()
            .entity(tranqualise(studentSubController.updateStudentActivityType(activityType, name)))
            .build();
      } else {
        return Response.status(500).build();
      }
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/educationalLevels/{ID:[0-9]*}")
  @PUT
  public Response updateStudentEducationalLevel(@PathParam("ID") Long id, StudentEducationalLevelEntity educationalLevelEntity) {
    StudentEducationalLevel educationalLevel = studentSubController.findStudentEducationalLevelById(id);
    if (educationalLevel != null) {
      String name = educationalLevelEntity.getName();
      if (!StringUtils.isBlank(name)) {
        return Response.ok()
            .entity(tranqualise(studentSubController.updateStudentEducationalLevel(educationalLevel, name)))
            .build();
      } else {
        return Response.status(500).build();
      }
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/examinationTypes/{ID:[0-9]*}")
  @PUT
  public Response updateStudentExaminationType(@PathParam("ID") Long id, StudentExaminationTypeEntity examinationTypeEntity) {
    StudentExaminationType examinationType = studentSubController.findStudentExaminationTypeById(id);
    if (examinationType != null) {
      String name = examinationTypeEntity.getName();
      if (!StringUtils.isBlank(name)) {
        return Response.ok()
            .entity(tranqualise(studentSubController.updateStudentExaminationType(examinationType, name)))
            .build();
      } else {
        return Response.status(500).build();
      }
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/endReasons/{ID:[0-9]*}")
  @PUT
  public Response updateStudentStudyEndReason(@PathParam("ID") Long id, StudentStudyEndReasonEntity endReasonEntity) {
    StudentStudyEndReason endReason = studentSubController.findStudentStudyEndReasonById(id);
    if (endReason != null) {
      String name = endReasonEntity.getName();
      Long parentReasonId = endReasonEntity.getParentReason_id();
      if (!StringUtils.isBlank(name)) {
        return Response.ok()
            .entity(tranqualise(studentSubController.updateStudentStudyEndReason(endReason, name)))
            .build();
      } else if (parentReasonId != null) {
        StudentStudyEndReason parentReason = studentSubController.findStudentStudyEndReasonById(parentReasonId);
        return Response.ok()
            .entity(tranqualise(studentSubController.updateStudentStudyEndReasonParent(endReason, parentReason)))
            .build();
      } else {
        return Response.status(500).build();
      }
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/studyProgrammeCategories/{ID:[0-9]*}")
  @PUT
  public Response updateStudyProgrammeCategory(@PathParam("ID") Long id, StudyProgrammeCategoryEntity studyProgrammeCategoryEntity) {
    StudyProgrammeCategory studyProgrammeCategory = studentSubController.findStudyProgrammeCategoryById(id);
    if (studyProgrammeCategory != null) {
      String name = studyProgrammeCategoryEntity.getName();
      Long educationTypeId = studyProgrammeCategoryEntity.getEducationType_id();
      if (educationTypeId != null && !StringUtils.isBlank(name)) {
        EducationType educationType = commonController.findEducationTypeById(educationTypeId);
        if (educationType != null) {
          return Response.ok()
              .entity(tranqualise(studentSubController.updateStudyProgrammeCategory(studyProgrammeCategory, name, educationType)))
              .build();
        } else {
          return Response.status(Status.NOT_FOUND).build();
        }
      } else {
        return Response.status(500).build();
      }
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/studyProgrammes/{ID:[0-9]*}")
  @PUT
  public Response updateStudyProgramme(@PathParam("ID") Long id, StudyProgrammeEntity studyProgrammeEntity) {
    StudyProgramme studyProgramme = studentSubController.findStudyProgrammeById(id);
    if (studyProgramme != null) {
      Long categoryId = studyProgrammeEntity.getCategory_id();
      String name = studyProgrammeEntity.getName();
      String code = studyProgrammeEntity.getCode();
      if (categoryId != null && !StringUtils.isBlank(name) && !StringUtils.isBlank(code)) {
        StudyProgrammeCategory category = studentSubController.findStudyProgrammeCategoryById(categoryId);
        if (category != null) {
          return Response.ok()
              .entity(tranqualise(studentSubController.updateStudyProgramme(studyProgramme, name, category, code)))
              .build();
        } else {
          return Response.status(Status.NOT_FOUND).build();
        }
      } else {
        return Response.status(500).build();
      }
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/abstractStudents/{ID:[0-9]*}")
  @PUT
  public Response updateAbstractStudent(@PathParam("ID") Long id, AbstractStudentEntity abstractStudentEntity) {
    AbstractStudent abstractStudent = abstractStudentController.findAbstractStudentById(id);
    Date birthday = abstractStudentEntity.getBirthday();
    String socialSecurityNumber = abstractStudentEntity.getSocialSecurityNumber();
    Sex sex = abstractStudentEntity.getSex();
    String basicInfo = abstractStudentEntity.getBasicInfo();
    boolean secureInfo = abstractStudentEntity.getSecureInfo();
    
    if (abstractStudent != null &&birthday != null && !StringUtils.isBlank(socialSecurityNumber) && sex != null && !StringUtils.isBlank(basicInfo)) {
      return Response.ok()
          .entity(tranqualise(abstractStudentController.updateAbstractStudent(abstractStudent, birthday, socialSecurityNumber, sex, basicInfo, secureInfo)))
          .build();
    } else  {
      return Response.status(500).build();
    }
  }
  
  @Path("/students/{ID:[0-9]*}")
  @PUT
  public Response updateStudent(@PathParam("ID") Long id, StudentEntity studentEntity) {
    Student student = studentController.findStudentById(id);
    if (student != null) {
      if (studentEntity.getArchived() == null) {
        try {
          String firstName = studentEntity.getFirstName();
          String lastName = studentEntity.getLastName();
          String nickname = studentEntity.getNickname();
          String additionalInfo = studentEntity.getAdditionalInfo();
          Date studyTimeEnd = studentEntity.getStudyTimeEnd();
          StudentActivityType activityType = studentSubController.findStudentActivityTypeById(studentEntity.getActivityType_id());
          StudentExaminationType examinationType = studentSubController.findStudentExaminationTypeById(studentEntity.getExaminationType_id());
          StudentEducationalLevel educationalLevel = studentSubController.findStudentEducationalLevelById(studentEntity.getEducationalLevel_id());
          String education = studentEntity.getEducation();
          Nationality nationality = studentSubController.findNationalityById(studentEntity.getNationality_id());
          Municipality municipality = studentSubController.findMunicipalityById(studentEntity.getMunicipality_id());
          Language language = studentSubController.findLanguageById(studentEntity.getLanguage_id());
          School school = schoolController.findSchoolById(studentEntity.getSchool_id());
          StudyProgramme studyProgramme = studentSubController.findStudyProgrammeById(studentEntity.getStudyProgramme_id());
          Double previousStudies = studentEntity.getPreviousStudies();
          Date studyStartDate = studentEntity.getStudyStartDate();
          Date studyEndDate = studentEntity.getStudyEndDate();
          StudentStudyEndReason studyEndReason = studentSubController.findStudentStudyEndReasonById(studentEntity.getStudyEndReason_id());
          String studyEndText = studentEntity.getStudyEndText();
          boolean lodging = studentEntity.getLodging();
          
          return Response.ok()
              .entity(tranqualise(studentController.updateStudent(student, firstName, lastName, nickname, additionalInfo, studyTimeEnd, activityType,
                      examinationType, educationalLevel, education, nationality, municipality, language, school, studyProgramme, previousStudies,
                      studyStartDate, studyEndDate, studyEndReason, studyEndText, lodging)))
              .build();
          
        } catch (Exception e) {
          return Response.status(500).build();
        }
      } else if (!studentEntity.getArchived()){ 
        return Response.ok()
            .entity(tranqualise(studentController.unarchiveStudent(student, getUser())))
            .build();
      } else {
        return Response.status(500).build();
      }
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/studentGroups/{ID:[0-9]*}")
  @PUT
  public Response updateStudentGroup(@PathParam("ID") Long id, StudentGroupEntity studentGroupEntity) {
    StudentGroup studentGroup = studentGroupController.findStudentGroupById(id);
    if (studentGroup != null) {
      if (studentGroupEntity.getArchived() == null) {
        String name = studentGroupEntity.getName();
        String description = studentGroupEntity.getDescription();
        Date beginDate = studentGroupEntity.getBeginDate();
        if (!StringUtils.isBlank(name) && !StringUtils.isBlank(description) && beginDate != null) {
          return Response.ok()
              .entity(tranqualise(studentGroupController.updateStudentGroup(studentGroup,name, description, beginDate, getUser())))
              .build();
        } else {
          return Response.status(500).build();
        }
      } else if (!studentGroupEntity.getArchived()) {
        return Response.ok()
            .entity(tranqualise(studentGroupController.unarchiveStudentGroup(studentGroup, getUser())))
            .build();
      } else {
        return Response.status(500).build();
      }
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/students/{ID:[0-9]*}")
  @DELETE
  public Response archiveStudent(@PathParam("ID") Long id) {
    Student student = studentController.findStudentById(id);
    if (student != null) {
      return Response.ok()
          .entity(tranqualise(studentController.archiveStudent(student, getUser())))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/studentGroups/{ID:[0-9]*}")
  @DELETE
  public Response archiveStudentGroup(@PathParam("ID") Long id) {
    StudentGroup studentGroup = studentGroupController.findStudentGroupById(id);
    if (studentGroup != null) {
      return Response.ok()
          .entity(tranqualise(studentGroupController.archiveStudentGroup(studentGroup, getUser())))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/students/{SID:[0-9]*}/tags/{TID:[0-9]*}")
  @DELETE
  public Response deleteStudentTag(@PathParam("SID") Long studentId, @PathParam("TID") Long tagId) {
    Student student = studentController.findStudentById(studentId);
    Tag tag = tagController.findTagById(tagId);
    if (student != null && tag != null) {
      studentController.removeStudentTag(student, tag);
      return Response.status(200).build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/studentGroups/{SID:[0-9]*}/tags/{TID:[0-9]*}")
  @DELETE
  public Response deleteStudentGroupTag(@PathParam("SID") Long studentGroupId, @PathParam("TID") Long tagId) {
    StudentGroup studentGroup = studentGroupController.findStudentGroupById(studentGroupId);
    Tag tag = tagController.findTagById(tagId);
    if (studentGroup != null && tag != null) {
      studentGroupController.removeStudentGroupTag(studentGroup, tag);
      return Response.status(200).build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Override
  protected TranquilityBuilderFactory getTranquilityBuilderFactory() {
    return tranquilityBuilderFactory;
  }

}