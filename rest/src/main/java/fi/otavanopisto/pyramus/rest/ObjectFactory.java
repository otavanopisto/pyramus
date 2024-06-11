package fi.otavanopisto.pyramus.rest;

import static fi.otavanopisto.pyramus.rest.util.PyramusRestUtils.toOffsetDateTime;

import java.lang.reflect.ParameterizedType;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;

import fi.otavanopisto.pyramus.dao.base.OrganizationContactPersonDAO;
import fi.otavanopisto.pyramus.dao.base.OrganizationContractPeriodDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Address;
import fi.otavanopisto.pyramus.domainmodel.base.BillingDetails;
import fi.otavanopisto.pyramus.domainmodel.base.ContactType;
import fi.otavanopisto.pyramus.domainmodel.base.ContactURL;
import fi.otavanopisto.pyramus.domainmodel.base.ContactURLType;
import fi.otavanopisto.pyramus.domainmodel.base.CourseBaseVariable;
import fi.otavanopisto.pyramus.domainmodel.base.CourseBaseVariableKey;
import fi.otavanopisto.pyramus.domainmodel.base.CourseEducationSubtype;
import fi.otavanopisto.pyramus.domainmodel.base.CourseEducationType;
import fi.otavanopisto.pyramus.domainmodel.base.CourseModule;
import fi.otavanopisto.pyramus.domainmodel.base.EducationSubtype;
import fi.otavanopisto.pyramus.domainmodel.base.EducationType;
import fi.otavanopisto.pyramus.domainmodel.base.EducationalLength;
import fi.otavanopisto.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.otavanopisto.pyramus.domainmodel.base.Email;
import fi.otavanopisto.pyramus.domainmodel.base.Language;
import fi.otavanopisto.pyramus.domainmodel.base.Municipality;
import fi.otavanopisto.pyramus.domainmodel.base.Nationality;
import fi.otavanopisto.pyramus.domainmodel.base.Organization;
import fi.otavanopisto.pyramus.domainmodel.base.OrganizationContactPerson;
import fi.otavanopisto.pyramus.domainmodel.base.OrganizationContractPeriod;
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
import fi.otavanopisto.pyramus.domainmodel.courses.CourseSignupStudentGroup;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseSignupStudyProgramme;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStaffMember;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseState;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseType;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessmentRequest;
import fi.otavanopisto.pyramus.domainmodel.grading.CreditLink;
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
import fi.otavanopisto.pyramus.domainmodel.students.StudentContactLogEntryComment;
import fi.otavanopisto.pyramus.domainmodel.students.StudentEducationalLevel;
import fi.otavanopisto.pyramus.domainmodel.students.StudentExaminationType;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroup;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroupStudent;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroupUser;
import fi.otavanopisto.pyramus.domainmodel.students.StudentStudyEndReason;
import fi.otavanopisto.pyramus.domainmodel.students.StudentStudyPeriod;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.StudentParent;
import fi.otavanopisto.pyramus.domainmodel.users.StudentParentChild;
import fi.otavanopisto.pyramus.domainmodel.users.UserVariable;
import fi.otavanopisto.pyramus.domainmodel.users.UserVariableKey;
import fi.otavanopisto.pyramus.rest.controller.CommonController;
import fi.otavanopisto.pyramus.rest.controller.CourseController;
import fi.otavanopisto.pyramus.rest.controller.MatriculationEligibilityController;
import fi.otavanopisto.pyramus.rest.controller.SchoolController;
import fi.otavanopisto.pyramus.rest.controller.StudentContactLogEntryCommentController;
import fi.otavanopisto.pyramus.rest.controller.UserController;
import fi.otavanopisto.pyramus.rest.model.AcademicTerm;
import fi.otavanopisto.pyramus.rest.model.CourseOptionality;
import fi.otavanopisto.pyramus.rest.model.CourseStaffMemberRoleEnum;
import fi.otavanopisto.pyramus.rest.model.Curriculum;
import fi.otavanopisto.pyramus.rest.model.MatriculationEligibilities;
import fi.otavanopisto.pyramus.rest.model.OrganizationContactPersonType;
import fi.otavanopisto.pyramus.rest.model.ProjectModuleOptionality;
import fi.otavanopisto.pyramus.rest.model.Sex;
import fi.otavanopisto.pyramus.rest.model.StudentCardActivity;
import fi.otavanopisto.pyramus.rest.model.StudentCardType;
import fi.otavanopisto.pyramus.rest.model.StudentContactLogEntryCommentRestModel;
import fi.otavanopisto.pyramus.rest.model.StudentContactLogEntryType;
import fi.otavanopisto.pyramus.rest.model.UserRole;
import fi.otavanopisto.pyramus.rest.model.VariableType;
import fi.otavanopisto.pyramus.rest.model.students.StudentStudyPeriodType;
import fi.otavanopisto.pyramus.rest.util.PyramusRestUtils;

@ApplicationScoped
public class ObjectFactory {

  @Inject
  private Logger logger;

  @Inject
  private CommonController commonController;
  
  @Inject
  private SchoolController schoolController;

  @Inject
  private UserController userController;

  @Inject
  private CourseController courseController;
  
  @Inject
  private OrganizationContractPeriodDAO organizationContractPeriodDAO;
  
  @Inject
  private OrganizationContactPersonDAO organizationContactPersonDAO;
  
  @Inject
  private MatriculationEligibilityController matriculationEligibilityController;
  
  @Inject
  private StudentContactLogEntryCommentController studentContactLogEntryCommentController;

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

        new Mapper<CourseModule>() {
          @Override
          public Object map(CourseModule entity) {
            Subject subject = entity.getSubject();
            Long educationTypeId = (subject != null && subject.getEducationType() != null) 
                ? subject.getEducationType().getId() : null;
            fi.otavanopisto.pyramus.rest.model.Subject subjectModel = subject != null 
                ? new fi.otavanopisto.pyramus.rest.model.Subject(subject.getId(), subject.getCode(), subject.getName(), educationTypeId, subject.getArchived()) : null;
            
            EducationalLength courseLength = entity.getCourseLength();
            EducationalTimeUnit courseLengthTimeUnit = courseLength != null ? courseLength.getUnit() : null;
            fi.otavanopisto.pyramus.rest.model.EducationalTimeUnit educationalTimeUnitModel = courseLengthTimeUnit != null
                ? new fi.otavanopisto.pyramus.rest.model.EducationalTimeUnit(courseLengthTimeUnit.getId(), courseLengthTimeUnit.getName(), courseLengthTimeUnit.getSymbol(), courseLengthTimeUnit.getBaseUnits(), courseLengthTimeUnit.getArchived()) : null;
            fi.otavanopisto.pyramus.rest.model.CourseLength courseLengthModel = courseLength != null 
                ? new fi.otavanopisto.pyramus.rest.model.CourseLength(courseLength.getId(), courseLength.getBaseUnits(), courseLength.getUnits(), educationalTimeUnitModel) : null;
                
            Integer courseNumber = entity.getCourseNumber();
            
            return new fi.otavanopisto.pyramus.rest.model.CourseModule(entity.getId(), subjectModel, courseNumber, courseLengthModel);
          }
        }, 
        
        new Mapper<Course>() {
          @Override
          public Object map(Course entity) {
            List<String> tags = new ArrayList<>();
            Set<Tag> courseTags = entity.getTags();
            if (courseTags != null) {
              for (Tag courseTag : courseTags) {
                tags.add(courseTag.getText());
              }
            }

            OffsetDateTime created = toOffsetDateTime(entity.getCreated());
            OffsetDateTime lastModified = toOffsetDateTime(entity.getLastModified());
            OffsetDateTime beginDate = fromDateToOffsetDateTime(entity.getBeginDate());
            OffsetDateTime endDate = fromDateToOffsetDateTime(entity.getEndDate());
            OffsetDateTime signupStart = fromDateToOffsetDateTime(entity.getSignupStart());
            OffsetDateTime signupEnd = fromDateToOffsetDateTime(entity.getSignupEnd());
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
            
            // #1257: Course's primary education type and subtype
            // If the course has subject defined, primary education type is that of the subject.
            // If no subject is defined, and course has only one education type, that is primary.
            // Primary subtype exists, if the course has only one subtype belonging to the primary education type.

            EducationType educationType = null;
            EducationSubtype educationSubtype = null;
            
            Set<Long> courseModuleEducationTypeIds = entity.getCourseModules().stream()
              .map(courseModule -> {
                // There may be nulls coming out of this but we'll keep them as they 
                // help indicate there may be conflicting education types
                return (courseModule.getSubject() != null && courseModule.getSubject().getEducationType() != null) 
                    ? courseModule.getSubject().getEducationType().getId() : null;
              })
              .collect(Collectors.toSet());
            boolean allNull = courseModuleEducationTypeIds.stream().allMatch(Objects::isNull);
            
            if (CollectionUtils.isNotEmpty(courseModuleEducationTypeIds) && courseModuleEducationTypeIds.size() == 1 && !allNull) {
              educationType = entity.getCourseModules().get(0).getSubject().getEducationType();
            }
            else if (entity.getCourseEducationTypes().size() == 1) {
              educationType = entity.getCourseEducationTypes().get(0).getEducationType();
            }
            if (educationType != null && !entity.getCourseEducationTypes().isEmpty()) {
              for (CourseEducationType cet : entity.getCourseEducationTypes()) {
                if (cet.getEducationType().getId().equals(educationType.getId())) {
                  if (cet.getCourseEducationSubtypes().size() == 1) {
                    educationSubtype = cet.getCourseEducationSubtypes().get(0).getEducationSubtype();
                  }
                  break;
                }
              }
            }
            
            Set<fi.otavanopisto.pyramus.rest.model.CourseModule> courseModuleModels = entity.getCourseModules().stream()
                .map(courseModule -> (fi.otavanopisto.pyramus.rest.model.CourseModule) createModel(courseModule))
                .collect(Collectors.toSet());
              
            return new fi.otavanopisto.pyramus.rest.model.Course(entity.getId(), entity.getName(), created, 
                lastModified, entity.getDescription(), entity.getArchived(), 
                entity.getMaxParticipantCount(), beginDate, endDate, signupStart, signupEnd, entity.getNameExtension(), 
                entity.getLocalTeachingDays(), entity.getTeachingHours(), entity.getDistanceTeachingHours(), 
                entity.getDistanceTeachingDays(), entity.getAssessingHours(), entity.getPlanningHours(), enrolmentTimeEnd, 
                creatorId, lastModifierId, curriculumIds, moduleId, stateId, typeId, variables, tags,
                entity.getOrganization() == null ? null : entity.getOrganization().getId(), entity.isCourseTemplate(),
                educationType == null ? null : educationType.getId(),
                educationSubtype == null ? null : educationSubtype.getId(), courseModuleModels);
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
            Long courseModuleId = entity.getCourseModule() != null ? entity.getCourseModule().getId() : null;
            Long gradeId = entity.getGrade() != null ? entity.getGrade().getId() : null;
            Long gradingScaleId = entity.getGrade() != null && entity.getGrade().getGradingScale() != null ? entity.getGrade().getGradingScale().getId() : null;
            Long assessorId = entity.getAssessor() != null ? entity.getAssessor().getId() : null;
            Boolean passing = entity.getGrade() != null ? entity.getGrade().getPassingGrade() : null;
            
            return new fi.otavanopisto.pyramus.rest.model.CourseAssessment(entity.getId(), 
                courseStudentId, 
                courseModuleId,
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
        
        new Mapper<CreditLink>(){
          @Override
          public Object map(CreditLink entity) {
            Long studentId = entity.getStudent() != null ? entity.getStudent().getId() : null;
            
            if (entity.getCredit() != null) {
              switch (entity.getCredit().getCreditType()) {
                case CourseAssessment:
                  fi.otavanopisto.pyramus.rest.model.CourseAssessment credit = (fi.otavanopisto.pyramus.rest.model.CourseAssessment) createModel(entity.getCredit());
                  return new fi.otavanopisto.pyramus.rest.model.CreditLinkCourseAssessment(entity.getId(), studentId, credit);
                case TransferCredit:
                  fi.otavanopisto.pyramus.rest.model.TransferCredit tc = (fi.otavanopisto.pyramus.rest.model.TransferCredit) createModel(entity.getCredit());
                  return new fi.otavanopisto.pyramus.rest.model.CreditLinkTransferCredit(entity.getId(), studentId, tc);
                default:
                  return null;
              }
            }
            
            return null;
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
            List<String> tags = new ArrayList<>();
            
            Set<Tag> moduleTags = entity.getTags();
            if (moduleTags != null) {
              for (Tag courseTag : moduleTags) {
                tags.add(courseTag.getText());
              }
            }

            Set<fi.otavanopisto.pyramus.rest.model.CourseModule> courseModuleModels = entity.getCourseModules().stream()
              .map(courseModule -> (fi.otavanopisto.pyramus.rest.model.CourseModule) createModel(courseModule))
              .collect(Collectors.toSet());
            
            Set<Long> curriculumIds = entity.getCurriculums().stream()
                .map(curriculum -> curriculum.getId())
                .collect(Collectors.toSet());

            return new fi.otavanopisto.pyramus.rest.model.Module(entity.getId(), entity.getName(), toOffsetDateTime(entity.getCreated()),
                toOffsetDateTime(entity.getLastModified()), entity.getDescription(), entity.getArchived(), 
                entity.getMaxParticipantCount(), creatorId, lastModifierId, curriculumIds, tags, courseModuleModels);
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
            String educationTypeCode = null;
            if (entity.getCategory() != null) {
              educationTypeCode = entity.getCategory().getEducationType() != null ? entity.getCategory().getEducationType().getCode() : null;
            }
            Long organizationId = entity.getOrganization() != null ? entity.getOrganization().getId() : null;
            return new fi.otavanopisto.pyramus.rest.model.StudyProgramme(entity.getId(), organizationId, entity.getCode(), entity.getName(), categoryId, entity.getOfficialEducationType(), entity.getHasEvaluationFees(), entity.getArchived(), educationTypeCode);
          }
        },
        
        new Mapper<StudentGroup>() {
          
          public Object map(StudentGroup entity) {
            Long creatorId = entity.getCreator().getId();
            Long lastModifierId = entity.getLastModifier() != null ? entity.getLastModifier().getId() : null;
            Long organizationId = entity.getOrganization() != null ? entity.getOrganization().getId() : null;

            List<String> tags = new ArrayList<>();
            
            Set<Tag> entityTags = entity.getTags();
            if (entityTags != null) {
              for (Tag entityTag : entityTags) {
                tags.add(entityTag.getText());
              }
            }    

            return new fi.otavanopisto.pyramus.rest.model.StudentGroup(entity.getId(), entity.getName(), entity.getDescription(), 
              toOffsetDateTime(entity.getBeginDate()), creatorId, toOffsetDateTime(entity.getCreated()), lastModifierId, 
              toOffsetDateTime(entity.getLastModified()), tags, entity.getGuidanceGroup(), organizationId, entity.getArchived() 
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
                case OTHER:
                  sex = Sex.OTHER;
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
            
            MatriculationEligibilities eligibility = new MatriculationEligibilities(matriculationEligibilityController.hasGroupEligibility(entity));
            
            String additionalContectInfo = entity.getContactInfo() != null ? entity.getContactInfo().getAdditionalInfo() : null;
            // TODO Remove this from the rest model
            boolean lodging = false;
            
            return new fi.otavanopisto.pyramus.rest.model.Student(entity.getId(), personId, entity.getFirstName(), entity.getLastName(), 
                entity.getNickname(), entity.getAdditionalInfo(), additionalContectInfo, nationalityId, 
                languageId, municipalityId, schoolId, activityTypeId, examinationTypeId, educationalLevelId, 
                toOffsetDateTime(entity.getStudyTimeEnd()), studyProgrammeId, curriculumId, entity.getPreviousStudies(), entity.getEducation(), 
                lodging, toOffsetDateTime(entity.getStudyStartDate()), toOffsetDateTime(entity.getStudyEndDate()), studyEndReasonId, 
                entity.getStudyEndText(), variables, tags, entity.getArchived(), eligibility);
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
          public fi.otavanopisto.pyramus.rest.model.StudentContactLogEntry map(StudentContactLogEntry entity) {
            StudentContactLogEntryType type = StudentContactLogEntryType.valueOf(entity.getType().name());
            Long creatorId = entity.getCreator() != null ? entity.getCreator().getId() : null;
            @SuppressWarnings("unchecked")
            List<StudentContactLogEntryCommentRestModel> comments = (List<StudentContactLogEntryCommentRestModel>) createModel(studentContactLogEntryCommentController.listContactLogEntryCommentsByEntry(entity));
            return new fi.otavanopisto.pyramus.rest.model.StudentContactLogEntry(entity.getId(), entity.getText(), creatorId, entity.getCreatorName(), toOffsetDateTime(entity.getEntryDate()), type, comments, entity.getArchived());
          }
        },
        
        new Mapper<StudentContactLogEntryComment>() {
          @Override
          public Object map(StudentContactLogEntryComment entity) {
            Long entryId = entity.getEntry() != null ? entity.getEntry().getId() : null;
            Long creatorId = entity.getCreator() != null ? entity.getCreator().getId() : null;

            return new fi.otavanopisto.pyramus.rest.model.StudentContactLogEntryCommentRestModel(entity.getId(), entity.getText(), creatorId, entity.getCreatorName(), entity.getCommentDate(), entryId);
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
        
        new Mapper<CourseStaffMember>() {
          @Override
          public Object map(CourseStaffMember entity) {
            Long courseId = entity.getCourse() != null ? entity.getCourse().getId() : null;
            Long userId = entity.getStaffMember() != null ? entity.getStaffMember().getId() : null;
            CourseStaffMemberRoleEnum role = PyramusRestUtils.convert(entity.getRole());
            return new fi.otavanopisto.pyramus.rest.model.CourseStaffMember(entity.getId(), courseId, userId, role);
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
        
        new Mapper<StudentParent>() {
          
          public Object map(StudentParent entity) {
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
            
            EnumSet<UserRole> userRoles = EnumSet.noneOf(UserRole.class);
            if (entity.getRoles() != null) {
              entity.getRoles().forEach(role -> userRoles.add(UserRole.valueOf(role.name())));
            }

            String additionalContactInfo = entity.getContactInfo() != null ? entity.getContactInfo().getAdditionalInfo() : null;
            Long personId = entity.getPerson() != null ? entity.getPerson().getId() : null;
            Long organizationId = entity.getOrganization() != null ? entity.getOrganization().getId() : null;
            
            return new fi.otavanopisto.pyramus.rest.model.StudentParent(entity.getId(), personId, organizationId, additionalContactInfo, 
                entity.getFirstName(), entity.getLastName(), userRoles, tags, variables);
          }
        },
        
        new Mapper<StudentParentChild>() {
          
          public Object map(StudentParentChild entity) {
            Student student = entity.getStudent();
            
            String studyProgrammeName = student.getStudyProgramme() != null ? student.getStudyProgramme().getName() : null;
            
            String defaultEmail = null;
            String defaultPhoneNumber = null;
            fi.otavanopisto.pyramus.rest.model.Address defaultAddress = null;
            
            if (student.getContactInfo() != null) {
              Email email = commonController.findDefaultEmailByContactInfo(student.getContactInfo());
              defaultEmail = email != null ? email.getAddress() : null;
              
              PhoneNumber phoneNumber = commonController.findDefaultPhoneNumberByContactInfo(student.getContactInfo());
              defaultPhoneNumber = phoneNumber != null ? phoneNumber.getNumber() : null;
              
              Address address = commonController.findDefaultAddressByContactInfo(student.getContactInfo());
              if (address != null) {
                Long contactTypeId = address.getContactType() != null ? address.getContactType().getId() : null;
                defaultAddress = new fi.otavanopisto.pyramus.rest.model.Address(address.getId(), contactTypeId, address.getDefaultAddress(), address.getName(), 
                    address.getStreetAddress(), address.getPostalCode(), address.getCity(), address.getCountry());
              }
            }

            return new fi.otavanopisto.pyramus.rest.model.StudentParentChild(
                student.getId(),
                student.getPersonId(),
                student.getFirstName(),
                student.getLastName(),
                student.getNickname(),
                studyProgrammeName, 
                defaultEmail, 
                defaultPhoneNumber, 
                defaultAddress
            );
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
          
          EnumSet<UserRole> userRoles = EnumSet.noneOf(UserRole.class);
          if (entity.getRoles() != null) {
            entity.getRoles().forEach(role -> userRoles.add(UserRole.valueOf(role.name())));
          }

          String additionalContactInfo = entity.getContactInfo() != null ? entity.getContactInfo().getAdditionalInfo() : null;
          Long personId = entity.getPerson() != null ? entity.getPerson().getId() : null;
          Long organizationId = entity.getOrganization() != null ? entity.getOrganization().getId() : null;
          
          return new fi.otavanopisto.pyramus.rest.model.StaffMember(entity.getId(), personId, organizationId, additionalContactInfo, 
              entity.getFirstName(), entity.getLastName(), entity.getTitle(), userRoles, tags, variables,
              entity.getStudyProgrammes().stream().map(StudyProgramme::getId).collect(Collectors.toSet()));
        }
      },
      
      new Mapper<fi.otavanopisto.pyramus.domainmodel.base.Curriculum>() {
        @Override
        public Object map(fi.otavanopisto.pyramus.domainmodel.base.Curriculum entity) {
          return new Curriculum(entity.getId(), entity.getName(), entity.getArchived());
        }
      },
      
      new Mapper<fi.otavanopisto.pyramus.domainmodel.base.Organization>() {
        @Override
        public Object map(Organization entity) {
          BillingDetails billingDetails = entity.getBillingDetails();
          fi.otavanopisto.pyramus.rest.model.BillingDetails billingDetailsRestModel = null;

          if (billingDetails != null) {
            billingDetailsRestModel = new fi.otavanopisto.pyramus.rest.model.BillingDetails(
                billingDetails.getId(),
                billingDetails.getPersonName(),
                billingDetails.getCompanyName(),
                billingDetails.getStreetAddress1(),
                billingDetails.getStreetAddress2(),
                billingDetails.getPostalCode(),
                billingDetails.getCity(),
                billingDetails.getRegion(),
                billingDetails.getCountry(),
                billingDetails.getPhoneNumber(),
                billingDetails.getEmailAddress(),
                billingDetails.getCompanyIdentifier(),
                billingDetails.getReferenceNumber(),
                billingDetails.getElectronicBillingAddress(),
                billingDetails.getElectronicBillingOperator(),
                billingDetails.getNotes()
            );
          }
          
          List<OrganizationContractPeriod> contractPeriods = organizationContractPeriodDAO.listBy(entity);
          List<fi.otavanopisto.pyramus.rest.model.OrganizationContractPeriod> contractPeriodsModel = contractPeriods
              .stream().map(contractPeriod -> {
                Long id = contractPeriod.getId();
                LocalDate begin = contractPeriod.getBegin() != null ? new java.sql.Date(contractPeriod.getBegin().getTime()).toLocalDate() : null;
                LocalDate end = contractPeriod.getEnd() != null ? new java.sql.Date(contractPeriod.getEnd().getTime()).toLocalDate() : null;
                return new fi.otavanopisto.pyramus.rest.model.OrganizationContractPeriod(id, begin, end);
              }).collect(Collectors.toList());
          
          List<OrganizationContactPerson> contactPersons = organizationContactPersonDAO.listBy(entity);
          List<fi.otavanopisto.pyramus.rest.model.OrganizationContactPerson> contactPersonsModel = contactPersons
              .stream().map(contactPerson -> {
                Long id = contactPerson.getId();
                OrganizationContactPersonType type = contactPerson.getType() != null ? OrganizationContactPersonType.valueOf(contactPerson.getType().name()) : null;
                String name = contactPerson.getName();
                String email = contactPerson.getEmail();
                String phone = contactPerson.getPhone();
                String title = contactPerson.getTitle();
                return new fi.otavanopisto.pyramus.rest.model.OrganizationContactPerson(id, type, name, email, phone, title);
              }).collect(Collectors.toList());
          
          fi.otavanopisto.pyramus.rest.model.Organization organizationModel = new fi.otavanopisto.pyramus.rest.model.Organization(entity.getId(), entity.getName(), billingDetailsRestModel, entity.getArchived());
          organizationModel.setContactPersons(contactPersonsModel);
          organizationModel.setContractPeriods(contractPeriodsModel);
          return organizationModel;
        }
      },
      
      new Mapper<fi.otavanopisto.pyramus.domainmodel.students.StudentStudyPeriod>() {
        @Override
        public Object map(StudentStudyPeriod entity) {
          Long studentId = entity.getStudent() != null ? entity.getStudent().getId() : null;
          StudentStudyPeriodType type = entity.getPeriodType() != null ? StudentStudyPeriodType.valueOf(entity.getPeriodType().toString()) : null;
          
          LocalDate begin = entity.getBegin() != null ? Instant.ofEpochMilli(entity.getBegin().getTime()).atZone(ZoneId.systemDefault()).toLocalDate() : null;
          LocalDate end = entity.getEnd() != null ? Instant.ofEpochMilli(entity.getEnd().getTime()).atZone(ZoneId.systemDefault()).toLocalDate() : null;
                    
          return new fi.otavanopisto.pyramus.rest.model.students.StudentStudyPeriod(entity.getId(), studentId, 
              type, begin, end);
        }
      },
      
      new Mapper<fi.otavanopisto.pyramus.domainmodel.courses.CourseSignupStudentGroup>() {
        @Override
        public Object map(CourseSignupStudentGroup entity) {
          Long courseId = entity.getCourse() != null ? entity.getCourse().getId() : null;
          Long studentGroupId = entity.getStudentGroup() != null ? entity.getStudentGroup().getId() : null;
          String studentGroupName = entity.getStudentGroup() != null ? entity.getStudentGroup().getName() : null;

          Organization studentGroupOrganization = entity.getStudentGroup() != null ? entity.getStudentGroup().getOrganization() : null;
          fi.otavanopisto.pyramus.rest.model.OrganizationBasicInfo organization = studentGroupOrganization != null
              ? new fi.otavanopisto.pyramus.rest.model.OrganizationBasicInfo(studentGroupOrganization.getId(), studentGroupOrganization.getName(), studentGroupOrganization.getArchived())
              : null;

          return new fi.otavanopisto.pyramus.rest.model.course.CourseSignupStudentGroup(entity.getId(), courseId, studentGroupId, studentGroupName, organization);
        }
      },
      
      new Mapper<fi.otavanopisto.pyramus.domainmodel.courses.CourseSignupStudyProgramme>() {
        @Override
        public Object map(CourseSignupStudyProgramme entity) {
          Long courseId = entity.getCourse() != null ? entity.getCourse().getId() : null;
          Long studyProgrammeId = entity.getStudyProgramme() != null ? entity.getStudyProgramme().getId() : null;
          String studyProgrammeName = entity.getStudyProgramme() != null ? entity.getStudyProgramme().getName() : null;

          Organization studyProgrammeOrganization = entity.getStudyProgramme() != null ? entity.getStudyProgramme().getOrganization() : null;
          fi.otavanopisto.pyramus.rest.model.OrganizationBasicInfo organization = studyProgrammeOrganization != null
              ? new fi.otavanopisto.pyramus.rest.model.OrganizationBasicInfo(studyProgrammeOrganization.getId(), studyProgrammeOrganization.getName(), studyProgrammeOrganization.getArchived())
              : null;
              
          return new fi.otavanopisto.pyramus.rest.model.course.CourseSignupStudyProgramme(entity.getId(), courseId, studyProgrammeId, studyProgrammeName, organization);
        }
      },
      
      new Mapper<fi.otavanopisto.pyramus.domainmodel.students.StudentCard>() {
        @Override
        public Object map(fi.otavanopisto.pyramus.domainmodel.students.StudentCard entity) {
          
          Student student = entity.getStudent();
          
          StudentCardType type = entity.getType() != null ? StudentCardType.valueOf(entity.getType().name()) : null;
          String studyProgrammeName = student.getStudyProgramme() != null ? student.getStudyProgramme().getName() : null;
          StudentCardActivity activity = entity.getActivity() != null ? StudentCardActivity.valueOf(entity.getActivity().name()) : null;
          
          return new fi.otavanopisto.pyramus.rest.model.StudentCard(
              entity.getId(), 
              student.getId(), 
              student.getFirstName(), 
              student.getLastName(), 
              studyProgrammeName,
              entity.getExpiryDate(), 
              activity, 
              type);
         
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
