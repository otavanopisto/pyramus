package fi.otavanopisto.pyramus.rest;

import java.lang.reflect.ParameterizedType;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.domainmodel.base.Address;
import fi.otavanopisto.pyramus.domainmodel.base.ContactType;
import fi.otavanopisto.pyramus.domainmodel.base.ContactURL;
import fi.otavanopisto.pyramus.domainmodel.base.ContactURLType;
import fi.otavanopisto.pyramus.domainmodel.base.CourseBaseVariable;
import fi.otavanopisto.pyramus.domainmodel.base.CourseBaseVariableKey;
import fi.otavanopisto.pyramus.domainmodel.base.CourseEducationSubtype;
import fi.otavanopisto.pyramus.domainmodel.base.CourseEducationType;
import fi.otavanopisto.pyramus.domainmodel.base.EducationSubtype;
import fi.otavanopisto.pyramus.domainmodel.base.EducationType;
import fi.otavanopisto.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.otavanopisto.pyramus.domainmodel.base.Email;
import fi.otavanopisto.pyramus.domainmodel.base.Language;
import fi.otavanopisto.pyramus.domainmodel.base.Municipality;
import fi.otavanopisto.pyramus.domainmodel.base.Nationality;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.base.PhoneNumber;
import fi.otavanopisto.pyramus.domainmodel.base.School;
import fi.otavanopisto.pyramus.domainmodel.base.SchoolField;
import fi.otavanopisto.pyramus.domainmodel.base.SchoolVariable;
import fi.otavanopisto.pyramus.domainmodel.base.SchoolVariableKey;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgrammeCategory;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.base.Tag;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseComponent;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseDescription;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseDescriptionCategory;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseEnrolmentType;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseParticipationType;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStaffMember;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStaffMemberRole;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseState;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseType;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessmentRequest;
import fi.otavanopisto.pyramus.domainmodel.grading.Grade;
import fi.otavanopisto.pyramus.domainmodel.grading.GradingScale;
import fi.otavanopisto.pyramus.domainmodel.grading.TransferCredit;
import fi.otavanopisto.pyramus.domainmodel.modules.Module;
import fi.otavanopisto.pyramus.domainmodel.modules.ModuleComponent;
import fi.otavanopisto.pyramus.domainmodel.projects.Project;
import fi.otavanopisto.pyramus.domainmodel.projects.ProjectModule;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentActivityType;
import fi.otavanopisto.pyramus.domainmodel.students.StudentContactLogEntry;
import fi.otavanopisto.pyramus.domainmodel.students.StudentEducationalLevel;
import fi.otavanopisto.pyramus.domainmodel.students.StudentExaminationType;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroup;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroupStudent;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroupUser;
import fi.otavanopisto.pyramus.domainmodel.students.StudentStudyEndReason;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.UserVariable;
import fi.otavanopisto.pyramus.domainmodel.users.UserVariableKey;
import fi.otavanopisto.pyramus.rest.controller.CourseController;
import fi.otavanopisto.pyramus.rest.controller.SchoolController;
import fi.otavanopisto.pyramus.rest.controller.UserController;
import fi.otavanopisto.pyramus.rest.model.AcademicTerm;
import fi.otavanopisto.pyramus.rest.model.CourseOptionality;
import fi.otavanopisto.pyramus.rest.model.Curriculum;
import fi.otavanopisto.pyramus.rest.model.ProjectModuleOptionality;
import fi.otavanopisto.pyramus.rest.model.Sex;
import fi.otavanopisto.pyramus.rest.model.StudentContactLogEntryType;
import fi.otavanopisto.pyramus.rest.model.UserRole;
import fi.otavanopisto.pyramus.rest.model.VariableType;

@ApplicationScoped
public class ObjectFactory {

  @Inject
  private Logger logger;

  @Inject
  private SchoolController schoolController;

  @Inject
  private UserController userController;

  @Inject
  private CourseController courseController;
  
  @PostConstruct
  public void init() {
    mappers = new HashMap<>();
    
    addMappers(
        new Mapper<fi.otavanopisto.pyramus.domainmodel.base.AcademicTerm>() {
          @Override
          public Object map(fi.otavanopisto.pyramus.domainmodel.base.AcademicTerm entity) {
            return new AcademicTerm(entity.getId(), entity.getName(), toOffsetDateTime( entity.getStartDate() ), toOffsetDateTime( entity.getEndDate() ), entity.getArchived());
          }
        }, 
        
        new Mapper<CourseParticipationType>() {
          @Override
          public Object map(CourseParticipationType entity) {
            return new fi.otavanopisto.pyramus.rest.model.CourseParticipationType(entity.getId(), entity.getName(), entity.getArchived());
          }
        }, 
        
        new Mapper<CourseEnrolmentType>() {
          @Override
          public Object map(CourseEnrolmentType entity) {
            return new fi.otavanopisto.pyramus.rest.model.CourseEnrolmentType(entity.getId(), entity.getName());
          }
        }, 
        
        new Mapper<CourseState>() {
          @Override
          public Object map(CourseState entity) {
            return new fi.otavanopisto.pyramus.rest.model.CourseState(entity.getId(), entity.getName(), entity.getArchived());
          }
        }, 
        
        new Mapper<CourseType>() {
          @Override
          public Object map(CourseType entity) {
            return new fi.otavanopisto.pyramus.rest.model.CourseType(entity.getId(), entity.getName(), entity.getArchived());
          }
        }, 
        
        new Mapper<CourseEducationType>() {
          @Override
          public Object map(CourseEducationType entity) {
            return new fi.otavanopisto.pyramus.rest.model.CourseEducationType(entity.getId(), entity.getEducationType().getId(), false);
          }
        }, 
        
        new Mapper<CourseEducationSubtype>() {
          @Override
          public Object map(CourseEducationSubtype entity) {
            return new fi.otavanopisto.pyramus.rest.model.CourseEducationSubtype(entity.getId(), entity.getEducationSubtype().getId(), false);
          }
        }, 
        
        new Mapper<Course>() {
          @Override
          public Object map(Course entity) {
            Long subjectId = null;
            Subject courseSubject = entity.getSubject();
            if (courseSubject != null) {
              subjectId = courseSubject.getId();
            }
            
            List<String> tags = new ArrayList<>();
            Set<Tag> courseTags = entity.getTags();
            if (courseTags != null) {
              for (Tag courseTag : courseTags) {
                tags.add(courseTag.getText());
              }
            }

            Double length = entity.getCourseLength() != null ? entity.getCourseLength().getUnits() : null;
            Long lengthUnitId = entity.getCourseLength() != null && entity.getCourseLength().getUnit() != null ? entity.getCourseLength().getUnit().getId() : null;
            OffsetDateTime created = toOffsetDateTime(entity.getCreated());
            OffsetDateTime lastModified = toOffsetDateTime(entity.getLastModified());
            OffsetDateTime beginDate = fromDateToOffsetDateTime(entity.getBeginDate());
            OffsetDateTime endDate = fromDateToOffsetDateTime(entity.getEndDate());
            OffsetDateTime enrolmentTimeEnd = toOffsetDateTime( entity.getEnrolmentTimeEnd());
            Long creatorId = entity.getCreator() != null ? entity.getCreator().getId() : null;
            Long lastModifierId = entity.getLastModifier() != null ? entity.getLastModifier().getId() : null;
            Long moduleId = entity.getModule() != null ? entity.getModule().getId() : null;
            Long stateId = entity.getState() != null ? entity.getState().getId() : null;
            Long typeId = entity.getType() != null ? entity.getType().getId() : null;
            
            Set<Long> curriculumIds = new HashSet<Long>();
            for (fi.otavanopisto.pyramus.domainmodel.base.Curriculum curriculum : entity.getCurriculums())
              curriculumIds.add(curriculum.getId());
            
            List<CourseBaseVariable> entityVariables = courseController.listCourseVariablesByCourse(entity);

            Map<String, String> variables = new HashMap<>();
            for (CourseBaseVariable entityVariable : entityVariables) {
              variables.put(entityVariable.getKey().getVariableKey(), entityVariable.getValue());
            };
            
            return new fi.otavanopisto.pyramus.rest.model.Course(entity.getId(), entity.getName(), created, 
                lastModified, entity.getDescription(), entity.getArchived(), entity.getCourseNumber(), 
                entity.getMaxParticipantCount(), beginDate, endDate, entity.getNameExtension(), 
                entity.getLocalTeachingDays(), entity.getTeachingHours(), entity.getDistanceTeachingHours(), 
                entity.getDistanceTeachingDays(), entity.getAssessingHours(), entity.getPlanningHours(), enrolmentTimeEnd, 
                creatorId, lastModifierId, subjectId, curriculumIds, length, lengthUnitId, moduleId, stateId, typeId, variables, tags);
          }
        }, 
        
        new Mapper<CourseComponent>() {
          @Override
          public Object map(CourseComponent entity) {
            Long lengthUnitId = entity.getLength() != null ? entity.getLength().getUnit().getId() : null;
            Double length = entity.getLength() != null ? entity.getLength().getUnits() : null;
            return new fi.otavanopisto.pyramus.rest.model.CourseComponent(entity.getId(), entity.getName(), entity.getDescription(), length, lengthUnitId, entity.getArchived());
          }
        }, 
        
        new Mapper<CourseDescription>() {
          @Override
          public Object map(CourseDescription entity) {
            return new fi.otavanopisto.pyramus.rest.model.CourseDescription(entity.getId(), entity.getCourseBase().getId(), entity.getCategory().getId(), entity.getDescription());
          }
        }, 

        new Mapper<CourseDescriptionCategory>() {
          @Override
          public Object map(CourseDescriptionCategory entity) {
            return new fi.otavanopisto.pyramus.rest.model.CourseDescriptionCategory(entity.getId(), entity.getName(), entity.getArchived());
          }
        }, 
        
        new Mapper<CourseAssessment>(){
          @Override
          public Object map(CourseAssessment entity) {
            Long courseStudentId = entity.getCourseStudent() != null ? entity.getCourseStudent().getId() : null;
            Long gradeId = entity.getGrade() != null ? entity.getGrade().getId() : null;
            Long gradingScaleId = entity.getGrade() != null && entity.getGrade().getGradingScale() != null ? entity.getGrade().getGradingScale().getId() : null;
            Long assessorId = entity.getAssessor() != null ? entity.getAssessor().getId() : null;
            Boolean passing = entity.getGrade().getPassingGrade();
            
            return new fi.otavanopisto.pyramus.rest.model.CourseAssessment(entity.getId(), 
                courseStudentId, 
                gradeId,
                gradingScaleId, 
                assessorId, 
                toOffsetDateTime(entity.getDate()),
                entity.getVerbalAssessment(),
                passing);
          }
        },
        
        new Mapper<TransferCredit>(){
          @Override
          public Object map(TransferCredit entity) {
            Long studentId = entity.getStudent() != null ? entity.getStudent().getId() : null;
            OffsetDateTime date = toOffsetDateTime(entity.getDate());
            Long gradeId = entity.getGrade() != null ? entity.getGrade().getId() : null;
            Long gradigScaleId = entity.getGrade() != null ? entity.getGrade().getGradingScale().getId() : null;
            Long assessorId = entity.getAssessor() != null ? entity.getAssessor().getId() : null;
            Double length = entity.getCourseLength() != null ? entity.getCourseLength().getUnits() : null;
            Long lengthUnitId = entity.getCourseLength() != null ? entity.getCourseLength().getUnit().getId() : null;
            Long schoolId = entity.getSchool() != null ? entity.getSchool().getId() : null;
            Long subjectId = entity.getSubject() != null ? entity.getSubject().getId() : null;
            CourseOptionality optionality = entity.getOptionality() != null ? CourseOptionality.valueOf(entity.getOptionality().name()) : null;
            Long curriculumId = entity.getCurriculum() != null ? entity.getCurriculum().getId() : null;
            Boolean offCurriculum = entity.getOffCurriculum() != null ? entity.getOffCurriculum() : Boolean.FALSE;
            
            return new fi.otavanopisto.pyramus.rest.model.TransferCredit(entity.getId(), studentId, date, gradeId, gradigScaleId, entity.getVerbalAssessment(), 
                assessorId, entity.getArchived(), entity.getCourseName(), entity.getCourseNumber(), length, lengthUnitId, schoolId, subjectId, optionality, curriculumId, offCurriculum);
          }
        },
        
        new Mapper<CourseAssessmentRequest>(){
          @Override
          public Object map(CourseAssessmentRequest entity) {
            OffsetDateTime created = toOffsetDateTime(entity.getCreated());
            return new fi.otavanopisto.pyramus.rest.model.CourseAssessmentRequest(entity.getId(), entity.getCourseStudent().getId(), created, entity.getRequestText(), entity.getArchived(), entity.getHandled());
          }
        },
        
        new Mapper<EducationType>() {
          @Override
          public Object map(EducationType entity) {
            return new fi.otavanopisto.pyramus.rest.model.EducationType(entity.getId(), entity.getName(), entity.getCode(), entity.getArchived());
          }
        }, 
        
        new Mapper<EducationSubtype>() {
          @Override
          public Object map(EducationSubtype entity) {
            Long educationTypeId = entity.getEducationType() != null ? entity.getEducationType().getId() : null;
            return new fi.otavanopisto.pyramus.rest.model.EducationSubtype(entity.getId(), entity.getName(), entity.getCode(), educationTypeId, entity.getArchived());
          }
        }, 
        
        new Mapper<Subject>() {
          @Override
          public Object map(Subject entity) {
            Long educationTypeId = entity.getEducationType() != null ? entity.getEducationType().getId() : null;
            return new fi.otavanopisto.pyramus.rest.model.Subject(entity.getId(), entity.getCode(), entity.getName(), educationTypeId, entity.getArchived());
          }
        }, 
        
        new Mapper<GradingScale>() {
          @Override
          public Object map(GradingScale entity) {
            return new fi.otavanopisto.pyramus.rest.model.GradingScale(entity.getId(), entity.getName(), entity.getDescription(), entity.getArchived());
          }
        }, 
        
        new Mapper<Grade>() {
          @Override
          public Object map(Grade entity) {
            Long gradingScaleId = entity.getGradingScale() != null ? entity.getGradingScale().getId() : null;
            return new fi.otavanopisto.pyramus.rest.model.Grade(entity.getId(), entity.getName(), entity.getDescription(), gradingScaleId, entity.getPassingGrade(), entity.getQualification(), entity.getGPA(), entity.getArchived());
          }
        }, 
        
        new Mapper<EducationalTimeUnit>() {
          @Override
          public Object map(EducationalTimeUnit entity) {
            return new fi.otavanopisto.pyramus.rest.model.EducationalTimeUnit(entity.getId(), entity.getName(), entity.getSymbol(), entity.getBaseUnits(), entity.getArchived());
          }
        }, 
        
        new Mapper<Module>() {
          @Override
          public Object map(Module entity) {
            Long creatorId = entity.getCreator().getId();
            Long lastModifierId = entity.getLastModifier() != null ? entity.getLastModifier().getId() : null;
            Long subjectId = entity.getSubject() != null ? entity.getSubject().getId() : null;
            Double length = entity.getCourseLength() != null ? entity.getCourseLength().getUnits() : null; 
            Long lenghtUnitId = entity.getCourseLength() != null && entity.getCourseLength().getUnit() != null ? entity.getCourseLength().getUnit().getId() : null;
            List<String> tags = new ArrayList<>();
            
            Set<Tag> moduleTags = entity.getTags();
            if (moduleTags != null) {
              for (Tag courseTag : moduleTags) {
                tags.add(courseTag.getText());
              }
            }
            
            Set<Long> curriculumIds = new HashSet<Long>();
            for (fi.otavanopisto.pyramus.domainmodel.base.Curriculum curriculum : entity.getCurriculums())
              curriculumIds.add(curriculum.getId());
            
            return new fi.otavanopisto.pyramus.rest.model.Module(entity.getId(), entity.getName(), toOffsetDateTime(entity.getCreated()),
                toOffsetDateTime(entity.getLastModified()), entity.getDescription(), entity.getArchived(), entity.getCourseNumber(), 
                entity.getMaxParticipantCount(), creatorId, lastModifierId, subjectId, curriculumIds, length, lenghtUnitId, tags);
          }
        }, 
        
        new Mapper<ModuleComponent>() {
          @Override
          public Object map(ModuleComponent entity) {
            Long lengthUnitId = entity.getLength() != null && entity.getLength().getUnit() != null ? entity.getLength().getUnit().getId() : null;
            Double length = entity.getLength() != null ? entity.getLength().getUnits() : null;
            return new fi.otavanopisto.pyramus.rest.model.ModuleComponent(entity.getId(), entity.getName(), entity.getDescription(), length, lengthUnitId, entity.getArchived());
          }
        },
        
        new Mapper<Project>() {
          @Override
          public Object map(Project entity) {
            Double optionalStudiesLength = entity.getOptionalStudiesLength() != null ? entity.getOptionalStudiesLength().getUnits() : null;
            Long optionalStudiesLengthUnitId = entity.getOptionalStudiesLength() != null && entity.getOptionalStudiesLength().getUnit() != null ? entity.getOptionalStudiesLength().getUnit().getId() : null;
            Long creatorId = entity.getCreator().getId();
            Long lastModifierId = entity.getLastModifier() != null ? entity.getLastModifier().getId() : null;
            List<String> tags = new ArrayList<>();
            
            Set<Tag> entityTags = entity.getTags();
            if (entityTags != null) {
              for (Tag entityTag : entityTags) {
                tags.add(entityTag.getText());
              }
            }
            
            return new fi.otavanopisto.pyramus.rest.model.Project(entity.getId(), entity.getName(), entity.getDescription(), optionalStudiesLength, optionalStudiesLengthUnitId, toOffsetDateTime(entity.getCreated()), creatorId, toOffsetDateTime(entity.getLastModified()), lastModifierId, tags, entity.getArchived());
          }
        },
        
        new Mapper<ProjectModule>() {
          @Override
          public Object map(ProjectModule entity) {
            ProjectModuleOptionality optionality = null;
            switch (entity.getOptionality()) {
              case MANDATORY:
                optionality = ProjectModuleOptionality.MANDATORY;
              break;
              case OPTIONAL:
                optionality = ProjectModuleOptionality.OPTIONAL;
              break;
            }
            
            Long moduleId = entity.getModule() != null ? entity.getModule().getId() : null;
            
            return new fi.otavanopisto.pyramus.rest.model.ProjectModule(entity.getId(), moduleId, optionality);
          }
        },
        
        new Mapper<School>() {
          @Override
          public Object map(School entity) {
            Long fieldId = entity.getField() != null ? entity.getField().getId() : null;
            
            List<String> tags = new ArrayList<>();
            
            Set<Tag> entityTags = entity.getTags();
            if (entityTags != null) {
              for (Tag entityTag : entityTags) {
                tags.add(entityTag.getText());
              }
            }
            
            List<SchoolVariable> entityVariables = schoolController.listSchoolVariablesBySchool(entity);
            
            Map<String, String> variables = new HashMap<>();
            for (SchoolVariable entityVariable : entityVariables) {
              variables.put(entityVariable.getKey().getVariableKey(), entityVariable.getValue());
            }
            
            String additionalInfo = entity.getContactInfo() != null ? entity.getContactInfo().getAdditionalInfo() : null;
            
            return new fi.otavanopisto.pyramus.rest.model.School(entity.getId(), entity.getCode(), entity.getName(), tags, fieldId, additionalInfo, entity.getArchived(), variables);
          }
        }, 
        
        new Mapper<SchoolField>() {
          @Override
          public Object map(SchoolField entity) {
            return new fi.otavanopisto.pyramus.rest.model.SchoolField(entity.getId(), entity.getName(), entity.getArchived());
          }
        }, 
        
        new Mapper<SchoolVariableKey>() {
          @Override
          public Object map(SchoolVariableKey entity) {
            return new fi.otavanopisto.pyramus.rest.model.VariableKey(entity.getVariableKey(), entity.getVariableName(), entity.getUserEditable(), toVariableType(entity.getVariableType()));
          }
        }, 
        
        new Mapper<CourseBaseVariableKey>() {
          @Override
          public Object map(CourseBaseVariableKey entity) {
            return new fi.otavanopisto.pyramus.rest.model.VariableKey(entity.getVariableKey(), entity.getVariableName(), entity.getUserEditable(), toVariableType(entity.getVariableType()));
          }
        }, 
        
        new Mapper<Language>() {
          @Override
          public Object map(Language entity) {
            return new fi.otavanopisto.pyramus.rest.model.Language(entity.getId(), entity.getCode(), entity.getName(), entity.getArchived());
          }
        }, 
        
        new Mapper<Municipality>() {
          @Override
          public Object map(Municipality entity) {
            return new fi.otavanopisto.pyramus.rest.model.Municipality(entity.getId(), entity.getCode(), entity.getName(), entity.getArchived());
          }
        }, 
        
        new Mapper<Nationality>() {
          @Override
          public Object map(Nationality entity) {
            return new fi.otavanopisto.pyramus.rest.model.Nationality(entity.getId(), entity.getCode(), entity.getName(), entity.getArchived());
          }
        }, 
        
        new Mapper<StudentActivityType>() {
          @Override
          public Object map(StudentActivityType entity) {
            return new fi.otavanopisto.pyramus.rest.model.StudentActivityType(entity.getId(), entity.getName(), entity.getArchived());
          }
        }, 
        
        new Mapper<StudentEducationalLevel>() {
          @Override
          public Object map(StudentEducationalLevel entity) {
            return new fi.otavanopisto.pyramus.rest.model.StudentEducationalLevel(entity.getId(), entity.getName(), entity.getArchived());
          }
        },
        
        new Mapper<StudentExaminationType>() {
          @Override
          public Object map(StudentExaminationType entity) {
            return new fi.otavanopisto.pyramus.rest.model.StudentExaminationType(entity.getId(), entity.getName(), entity.getArchived());
          }
        },
        
        new Mapper<StudyProgrammeCategory>() {
          @Override
          public Object map(StudyProgrammeCategory entity) {
            Long educationTypeId = entity.getEducationType() != null ? entity.getEducationType().getId() : null;
            return new fi.otavanopisto.pyramus.rest.model.StudyProgrammeCategory(entity.getId(), entity.getName(), educationTypeId, entity.getArchived());
          }
        },
        
        new Mapper<StudyProgramme>() {
          @Override
          public Object map(StudyProgramme entity) {
            Long categoryId = entity.getCategory() != null ? entity.getCategory().getId() : null;
            return new fi.otavanopisto.pyramus.rest.model.StudyProgramme(entity.getId(), entity.getCode(), entity.getName(), categoryId, entity.getArchived());
          }
        },
        
        new Mapper<StudentGroup>() {
          
          public Object map(StudentGroup entity) {
            Long creatorId = entity.getCreator().getId();
            Long lastModifierId = entity.getLastModifier() != null ? entity.getLastModifier().getId() : null;

            List<String> tags = new ArrayList<>();
            
            Set<Tag> entityTags = entity.getTags();
            if (entityTags != null) {
              for (Tag entityTag : entityTags) {
                tags.add(entityTag.getText());
              }
            }    

            return new fi.otavanopisto.pyramus.rest.model.StudentGroup(entity.getId(), entity.getName(), entity.getDescription(), 
              toOffsetDateTime(entity.getBeginDate()), creatorId, toOffsetDateTime(entity.getCreated()), lastModifierId, 
              toOffsetDateTime(entity.getLastModified()), tags, entity.getGuidanceGroup(), entity.getArchived() 
            );
          }
        },
        
        new Mapper<Person>() {
          
          public Object map(Person entity) {
            Sex sex = null;
            if (entity.getSex() != null) { 
              switch (entity.getSex()) {
                case FEMALE:
                  sex = Sex.FEMALE;
                break;
                case MALE:
                  sex = Sex.MALE;
                break;
              }
            }
            
            Long defaultUserId = entity.getDefaultUser() != null ? entity.getDefaultUser().getId() : null;
            return new fi.otavanopisto.pyramus.rest.model.Person(entity.getId(), toOffsetDateTime(entity.getBirthday()), entity.getSocialSecurityNumber(), sex, entity.getSecureInfo(), entity.getBasicInfo(), defaultUserId);
          }
        },
          
        new Mapper<Student>() {
          
          public Object map(Student entity) {
            Long personId = entity.getPerson() != null ? entity.getPerson().getId() : null;
            Long nationalityId = entity.getNationality()  != null ? entity.getNationality().getId() : null;
            Long languageId = entity.getLanguage()  != null ? entity.getLanguage().getId() : null;
            Long municipalityId = entity.getMunicipality()  != null ? entity.getMunicipality().getId() : null;
            Long schoolId = entity.getSchool() != null ? entity.getSchool().getId() : null;
            Long activityTypeId = entity.getActivityType() != null ? entity.getActivityType().getId() : null;
            Long examinationTypeId = entity.getExaminationType() != null ? entity.getExaminationType().getId() : null;
            Long educationalLevelId = entity.getEducationalLevel() != null ? entity.getEducationalLevel().getId() : null;
            Long studyProgrammeId = entity.getStudyProgramme() != null ? entity.getStudyProgramme().getId() : null;
            Long studyEndReasonId = entity.getStudyEndReason() != null ? entity.getStudyEndReason().getId() : null;
            Long curriculumId = entity.getCurriculum() != null ? entity.getCurriculum().getId() : null;
            
            List<String> tags = new ArrayList<>();
            
            Set<Tag> entityTags = entity.getTags();
            if (entityTags != null) {
              for (Tag entityTag : entityTags) {
                tags.add(entityTag.getText());
              }
            }    
            
            
            List<UserVariable> entityVariables = userController.listUserVariablesByUser(entity);

            Map<String, String> variables = new HashMap<>();
            for (UserVariable entityVariable : entityVariables) {
              variables.put(entityVariable.getKey().getVariableKey(), entityVariable.getValue());
            };
            
            String additionalContectInfo = entity.getContactInfo() != null ? entity.getContactInfo().getAdditionalInfo() : null;
            
            return new fi.otavanopisto.pyramus.rest.model.Student(entity.getId(), personId, entity.getFirstName(), entity.getLastName(), 
                entity.getNickname(), entity.getAdditionalInfo(), additionalContectInfo, nationalityId, 
                languageId, municipalityId, schoolId, activityTypeId, examinationTypeId, educationalLevelId, 
                toOffsetDateTime(entity.getStudyTimeEnd()), studyProgrammeId, curriculumId, entity.getPreviousStudies(), entity.getEducation(), 
                entity.getLodging(), toOffsetDateTime(entity.getStudyStartDate()), toOffsetDateTime(entity.getStudyEndDate()), studyEndReasonId, 
                entity.getStudyEndText(), variables, tags, entity.getArchived());
          }
        },
        
        new Mapper<StudentStudyEndReason>() {
          @Override
          public Object map(StudentStudyEndReason entity) {
            Long parentReasonId = entity.getParentReason() != null ? entity.getParentReason().getId() : null;
            return new fi.otavanopisto.pyramus.rest.model.StudentStudyEndReason(entity.getId(), entity.getName(), parentReasonId);
          }
        },
        
        new Mapper<StudentContactLogEntry>() {
          @Override
          public Object map(StudentContactLogEntry entity) {
            StudentContactLogEntryType type = StudentContactLogEntryType.valueOf(entity.getType().name());
            return new fi.otavanopisto.pyramus.rest.model.StudentContactLogEntry(entity.getId(), entity.getText(), entity.getCreatorName(), toOffsetDateTime(entity.getEntryDate()), type, entity.getArchived());
          }
        },
        
        new Mapper<StudentGroupStudent>() {
          @Override
          public Object map(StudentGroupStudent entity) {
            Long studentId = entity.getStudent() != null ? entity.getStudent().getId() : null;
            return new fi.otavanopisto.pyramus.rest.model.StudentGroupStudent(entity.getId(), studentId);
          }
        },
        
        new Mapper<StudentGroupUser>() {
          @Override
          public Object map(StudentGroupUser entity) {
            Long staffMemberId = entity.getStaffMember() != null ? entity.getStaffMember().getId() : null;
            return new fi.otavanopisto.pyramus.rest.model.StudentGroupUser(entity.getId(), staffMemberId);
          }
        },
        
        new Mapper<Email>() {
          @Override
          public Object map(Email entity) {
            Long contactTypeId = entity.getContactType() != null ? entity.getContactType().getId() : null;
            return new fi.otavanopisto.pyramus.rest.model.Email(entity.getId(), contactTypeId, entity.getDefaultAddress(), entity.getAddress());
          }
        },
        
        new Mapper<PhoneNumber>() {
          @Override
          public Object map(PhoneNumber entity) {
            Long contactTypeId = entity.getContactType() != null ? entity.getContactType().getId() : null;
            return new fi.otavanopisto.pyramus.rest.model.PhoneNumber(entity.getId(), contactTypeId, entity.getDefaultNumber(), entity.getNumber());
          }
        },
        
        new Mapper<ContactURL>() {
          @Override
          public Object map(ContactURL entity) {
            Long contactURLTypeId = entity.getContactURLType() != null ? entity.getContactURLType().getId() : null;
            return new fi.otavanopisto.pyramus.rest.model.ContactURL(entity.getId(), contactURLTypeId, entity.getURL());
          }
        },
        
        new Mapper<Address>() {
          @Override
          public Object map(Address entity) {
            Long contactTypeId = entity.getContactType() != null ? entity.getContactType().getId() : null;
            return new fi.otavanopisto.pyramus.rest.model.Address(entity.getId(), contactTypeId, entity.getDefaultAddress(), entity.getName(), 
                entity.getStreetAddress(), entity.getPostalCode(), entity.getCity(), entity.getCountry());
          }
        }, 
        
        new Mapper<ContactType>() {
          @Override
          public Object map(ContactType entity) {
            return new fi.otavanopisto.pyramus.rest.model.ContactType(entity.getId(), entity.getName(), entity.getArchived(), entity.getNonUnique());
          }
        }, 
        
        new Mapper<ContactURLType>() {
          @Override
          public Object map(ContactURLType entity) {
            return new fi.otavanopisto.pyramus.rest.model.ContactURLType(entity.getId(), entity.getName(), entity.getArchived());
          }
        },
        
        new Mapper<UserVariableKey>() {
          @Override
          public Object map(UserVariableKey entity) {
            return new fi.otavanopisto.pyramus.rest.model.VariableKey(entity.getVariableKey(), entity.getVariableName(), entity.getUserEditable(), toVariableType(entity.getVariableType()));
          }
        },
        
        new Mapper<CourseStaffMemberRole>() {
          @Override
          public Object map(CourseStaffMemberRole entity) {
            return new fi.otavanopisto.pyramus.rest.model.CourseStaffMemberRole(entity.getId(), entity.getName());
          }
        },
        
        new Mapper<CourseStaffMember>() {
          @Override
          public Object map(CourseStaffMember entity) {
            Long courseId = entity.getCourse() != null ? entity.getCourse().getId() : null;
            Long userId = entity.getStaffMember() != null ? entity.getStaffMember().getId() : null;
            Long roleId = entity.getRole() != null ? entity.getRole().getId() : null;
            return new fi.otavanopisto.pyramus.rest.model.CourseStaffMember(entity.getId(), courseId, userId, roleId);
          }
        },
        
        new Mapper<CourseStudent>() {
          @Override
          public Object map(CourseStudent entity) {
            Long courseId = entity.getCourse() != null ? entity.getCourse().getId() : null;
            Long studentId = entity.getStudent() != null ? entity.getStudent().getId() : null;
            Long participantTypeId = entity.getParticipationType() != null ? entity.getParticipationType().getId() : null;
            Long courseEnrolmentTypeId = entity.getCourseEnrolmentType() != null ? entity.getCourseEnrolmentType().getId() : null;
            CourseOptionality optionality = entity.getOptionality() != null ? CourseOptionality.valueOf(entity.getOptionality().name()) : null;
            Long billingDetailsId = entity.getBillingDetails() != null ? entity.getBillingDetails().getId() : null;
            
            return new fi.otavanopisto.pyramus.rest.model.CourseStudent(entity.getId(), courseId, studentId, toOffsetDateTime(entity.getEnrolmentTime()), entity.getArchived(), participantTypeId, courseEnrolmentTypeId, entity.getLodging(), optionality, billingDetailsId);
          }
        },
        
      new Mapper<StaffMember>() {
        
        public Object map(StaffMember entity) {
         List<String> tags = new ArrayList<>();
          
          Set<Tag> entityTags = entity.getTags();
          if (entityTags != null) {
            for (Tag entityTag : entityTags) {
              tags.add(entityTag.getText());
            }
          }    
          
          List<UserVariable> entityVariables = userController.listUserVariablesByUser(entity);

          Map<String, String> variables = new HashMap<>();
          for (UserVariable entityVariable : entityVariables) {
            variables.put(entityVariable.getKey().getVariableKey(), entityVariable.getValue());
          };
          
          UserRole role = UserRole.valueOf(entity.getRole().name());
          String additionalContactInfo = entity.getContactInfo() != null ? entity.getContactInfo().getAdditionalInfo() : null;
          Long personId = entity.getPerson() != null ? entity.getPerson().getId() : null;
          
          return new fi.otavanopisto.pyramus.rest.model.StaffMember(entity.getId(), personId, additionalContactInfo, 
              entity.getFirstName(), entity.getLastName(), entity.getTitle(), role, tags, variables);
        }
      },
      
      new Mapper<fi.otavanopisto.pyramus.domainmodel.base.Curriculum>() {
        @Override
        public Object map(fi.otavanopisto.pyramus.domainmodel.base.Curriculum entity) {
          return new Curriculum(entity.getId(), entity.getName(), entity.getArchived());
        }
      }
    );
  }

  @SuppressWarnings("unchecked")
  public Object createModel(Object object) {
    if (object == null) {
      logger.log(Level.WARNING, "Null object was passed to createModel");
      return null;
    }
    
    if (object instanceof List) {
      List<Object> result = new ArrayList<>();
      
      for (Object item : (List<Object>) object) {
        result.add(createModel(item));
      }
      
      return result;
    }
    
    Mapper<Object> mapper = mappers.get(object.getClass());
    if (mapper == null) {
      logger.log(Level.SEVERE, "Could not find a mapper for " + object.getClass());
      return null;
    } 
    
    return mappers.get(object.getClass()).map(object);
  }

  private OffsetDateTime toOffsetDateTime(Date date) {
    if (date == null) {
      return null;
    }
    // If (as) date is java.sql.Date then toInstant() would cause UnsupportedOperationException
    Date tmpDate = new Date(date.getTime()); 
    Instant instant = tmpDate.toInstant();
    ZoneId systemId = ZoneId.systemDefault();
    ZoneOffset offset = systemId.getRules().getOffset(instant);
    return tmpDate.toInstant().atOffset(offset);
  }

  private OffsetDateTime fromDateToOffsetDateTime(Date date) {
    if (date == null) {
      return null;
    }
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T00:00:00'");
    LocalDateTime ldt = LocalDateTime.parse(sdf.format(date));
    ZoneId systemId = ZoneId.systemDefault();
    ZoneOffset offset = systemId.getRules().getOffset(ldt);
    return ldt.atOffset(offset);
  }
 
  private VariableType toVariableType(fi.otavanopisto.pyramus.domainmodel.base.VariableType variableType) {
    switch (variableType) {
      case BOOLEAN:
        return VariableType.BOOLEAN;
      case DATE:
        return VariableType.DATE;
      case NUMBER:
        return VariableType.NUMBER;
      case TEXT:
        return VariableType.TEXT;
    }
    
    return null;
  }
  
  private static interface Mapper <T> {
    public Object map(T entity);
  }
  
  private void addMappers(Mapper<?>... mappers) {
    for (Mapper<?> mapper : mappers) {
      addMapper(mapper);
    }
  }

  @SuppressWarnings("unchecked")
  private void addMapper(Mapper<?> mapper) {
    ParameterizedType parameterizedType = (ParameterizedType) mapper.getClass().getGenericInterfaces()[0];
    Class<?> type = (Class<?>) parameterizedType.getActualTypeArguments()[0];
    mappers.put(type, (Mapper<Object>) mapper);
  }
  
  private Map<Class<?>, Mapper<Object>> mappers;
}
