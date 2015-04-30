package fi.pyramus.rest;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.joda.time.DateTime;

import fi.pyramus.domainmodel.base.Address;
import fi.pyramus.domainmodel.base.ContactType;
import fi.pyramus.domainmodel.base.ContactURL;
import fi.pyramus.domainmodel.base.ContactURLType;
import fi.pyramus.domainmodel.base.CourseBaseVariable;
import fi.pyramus.domainmodel.base.CourseBaseVariableKey;
import fi.pyramus.domainmodel.base.EducationSubtype;
import fi.pyramus.domainmodel.base.EducationType;
import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.base.Email;
import fi.pyramus.domainmodel.base.Language;
import fi.pyramus.domainmodel.base.Municipality;
import fi.pyramus.domainmodel.base.Nationality;
import fi.pyramus.domainmodel.base.Person;
import fi.pyramus.domainmodel.base.PhoneNumber;
import fi.pyramus.domainmodel.base.School;
import fi.pyramus.domainmodel.base.SchoolField;
import fi.pyramus.domainmodel.base.SchoolVariable;
import fi.pyramus.domainmodel.base.SchoolVariableKey;
import fi.pyramus.domainmodel.base.StudyProgramme;
import fi.pyramus.domainmodel.base.StudyProgrammeCategory;
import fi.pyramus.domainmodel.base.Subject;
import fi.pyramus.domainmodel.base.Tag;
import fi.pyramus.domainmodel.courses.Course;
import fi.pyramus.domainmodel.courses.CourseComponent;
import fi.pyramus.domainmodel.courses.CourseDescriptionCategory;
import fi.pyramus.domainmodel.courses.CourseEnrolmentType;
import fi.pyramus.domainmodel.courses.CourseParticipationType;
import fi.pyramus.domainmodel.courses.CourseStaffMember;
import fi.pyramus.domainmodel.courses.CourseStaffMemberRole;
import fi.pyramus.domainmodel.courses.CourseState;
import fi.pyramus.domainmodel.courses.CourseStudent;
import fi.pyramus.domainmodel.courses.CourseType;
import fi.pyramus.domainmodel.grading.CourseAssessment;
import fi.pyramus.domainmodel.grading.Grade;
import fi.pyramus.domainmodel.grading.GradingScale;
import fi.pyramus.domainmodel.modules.Module;
import fi.pyramus.domainmodel.modules.ModuleComponent;
import fi.pyramus.domainmodel.projects.Project;
import fi.pyramus.domainmodel.projects.ProjectModule;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.domainmodel.students.StudentActivityType;
import fi.pyramus.domainmodel.students.StudentContactLogEntry;
import fi.pyramus.domainmodel.students.StudentEducationalLevel;
import fi.pyramus.domainmodel.students.StudentExaminationType;
import fi.pyramus.domainmodel.students.StudentGroup;
import fi.pyramus.domainmodel.students.StudentGroupStudent;
import fi.pyramus.domainmodel.students.StudentStudyEndReason;
import fi.pyramus.domainmodel.users.StaffMember;
import fi.pyramus.domainmodel.users.UserVariable;
import fi.pyramus.domainmodel.users.UserVariableKey;
import fi.pyramus.rest.controller.CourseController;
import fi.pyramus.rest.controller.SchoolController;
import fi.pyramus.rest.controller.UserController;
import fi.pyramus.rest.model.AcademicTerm;
import fi.pyramus.rest.model.CourseOptionality;
import fi.pyramus.rest.model.ProjectModuleOptionality;
import fi.pyramus.rest.model.Sex;
import fi.pyramus.rest.model.StudentContactLogEntryType;
import fi.pyramus.rest.model.UserRole;
import fi.pyramus.rest.model.VariableType;

@ApplicationScoped
@Stateful
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
    addMappers(
        new Mapper<fi.pyramus.domainmodel.base.AcademicTerm>() {
          @Override
          public Object map(fi.pyramus.domainmodel.base.AcademicTerm entity) {
            return new AcademicTerm(entity.getId(), entity.getName(), toDateTime( entity.getStartDate() ), toDateTime( entity.getEndDate() ), entity.getArchived());
          }
        }, 
        
        new Mapper<CourseParticipationType>() {
          @Override
          public Object map(CourseParticipationType entity) {
            return new fi.pyramus.rest.model.CourseParticipationType(entity.getId(), entity.getName(), entity.getArchived());
          }
        }, 
        
        new Mapper<CourseEnrolmentType>() {
          @Override
          public Object map(CourseEnrolmentType entity) {
            return new fi.pyramus.rest.model.CourseEnrolmentType(entity.getId(), entity.getName());
          }
        }, 
        
        new Mapper<CourseState>() {
          @Override
          public Object map(CourseState entity) {
            return new fi.pyramus.rest.model.CourseState(entity.getId(), entity.getName(), entity.getArchived());
          }
        }, 
        
        new Mapper<CourseType>() {
          @Override
          public Object map(CourseType entity) {
            return new fi.pyramus.rest.model.CourseType(entity.getId(), entity.getName(), entity.getArchived());
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
            
            List<String> tags = new ArrayList<String>();
            Set<Tag> courseTags = entity.getTags();
            if (courseTags != null) {
              for (Tag courseTag : courseTags) {
                tags.add(courseTag.getText());
              }
            }

            Double length = entity.getCourseLength() != null ? entity.getCourseLength().getUnits() : null;
            Long lengthUnitId = entity.getCourseLength() != null && entity.getCourseLength().getUnit() != null ? entity.getCourseLength().getUnit().getId() : null;
            DateTime created = toDateTime(entity.getCreated() );
            DateTime lastModified = toDateTime(entity.getLastModified() );
            DateTime beginDate = toDateTime(entity.getBeginDate() );
            DateTime endDate = toDateTime(entity.getEndDate() );
            DateTime enrolmentTimeEnd = toDateTime( entity.getEnrolmentTimeEnd() );
            Long creatorId = entity.getCreator() != null ? entity.getCreator().getId() : null;
            Long lastModifierId = entity.getLastModifier() != null ? entity.getLastModifier().getId() : null;
            Long moduleId = entity.getModule() != null ? entity.getModule().getId() : null;
            Long stateId = entity.getState() != null ? entity.getState().getId() : null;
            Long typeId = entity.getType() != null ? entity.getType().getId() : null;
            
            List<CourseBaseVariable> entityVariables = courseController.listCourseVariablesByCourse(entity);

            Map<String, String> variables = new HashMap<>();
            for (CourseBaseVariable entityVariable : entityVariables) {
              variables.put(entityVariable.getKey().getVariableKey(), entityVariable.getValue());
            };
            
            return new fi.pyramus.rest.model.Course(entity.getId(), entity.getName(), created, 
                lastModified, entity.getDescription(), entity.getArchived(), entity.getCourseNumber(), 
                entity.getMaxParticipantCount(), beginDate, endDate, entity.getNameExtension(), 
                entity.getLocalTeachingDays(), entity.getTeachingHours(), entity.getDistanceTeachingDays(), 
                entity.getAssessingHours(), entity.getPlanningHours(), enrolmentTimeEnd, creatorId, 
                lastModifierId, subjectId, length, lengthUnitId, moduleId, stateId, typeId, variables, tags);
          }
        }, 
        
        new Mapper<CourseComponent>() {
          @Override
          public Object map(CourseComponent entity) {
            Long lengthUnitId = entity.getLength() != null ? entity.getLength().getUnit().getId() : null;
            Double length = entity.getLength() != null ? entity.getLength().getUnits() : null;
            return new fi.pyramus.rest.model.CourseComponent(entity.getId(), entity.getName(), entity.getDescription(), length, lengthUnitId, entity.getArchived());
          }
        }, 
        
        new Mapper<CourseDescriptionCategory>() {
          @Override
          public Object map(CourseDescriptionCategory entity) {
            return new fi.pyramus.rest.model.CourseDescriptionCategory(entity.getId(), entity.getName(), entity.getArchived());
          }
        }, 
        
        new Mapper<CourseAssessment>(){
          @Override
          public Object map(CourseAssessment entity) {
            return new fi.pyramus.rest.model.CourseAssessment(entity.getId(), entity.getCourseStudent().getId(), entity.getGrade().getId(), entity.getAssessor().getId(), new DateTime(entity.getDate()), entity.getVerbalAssessment());
          }
        },
        
        new Mapper<EducationType>() {
          @Override
          public Object map(EducationType entity) {
            return new fi.pyramus.rest.model.EducationType(entity.getId(), entity.getName(), entity.getCode(), entity.getArchived());
          }
        }, 
        
        new Mapper<EducationSubtype>() {
          @Override
          public Object map(EducationSubtype entity) {
            Long educationTypeId = entity.getEducationType() != null ? entity.getEducationType().getId() : null;
            return new fi.pyramus.rest.model.EducationSubtype(entity.getId(), entity.getName(), entity.getCode(), educationTypeId, entity.getArchived());
          }
        }, 
        
        new Mapper<Subject>() {
          @Override
          public Object map(Subject entity) {
            Long educationTypeId = entity.getEducationType() != null ? entity.getEducationType().getId() : null;
            return new fi.pyramus.rest.model.Subject(entity.getId(), entity.getCode(), entity.getName(), educationTypeId, entity.getArchived());
          }
        }, 
        
        new Mapper<GradingScale>() {
          @Override
          public Object map(GradingScale entity) {
            return new fi.pyramus.rest.model.GradingScale(entity.getId(), entity.getName(), entity.getDescription(), entity.getArchived());
          }
        }, 
        
        new Mapper<Grade>() {
          @Override
          public Object map(Grade entity) {
            Long gradingScaleId = entity.getGradingScale() != null ? entity.getGradingScale().getId() : null;
            return new fi.pyramus.rest.model.Grade(entity.getId(), entity.getName(), entity.getDescription(), gradingScaleId, entity.getPassingGrade(), entity.getQualification(), entity.getGPA(), entity.getArchived());
          }
        }, 
        
        new Mapper<EducationalTimeUnit>() {
          @Override
          public Object map(EducationalTimeUnit entity) {
            return new fi.pyramus.rest.model.EducationalTimeUnit(entity.getId(), entity.getName(), entity.getSymbol(), entity.getBaseUnits(), entity.getArchived());
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
            
            return new fi.pyramus.rest.model.Module(entity.getId(), entity.getName(), toDateTime(entity.getCreated()),
                toDateTime(entity.getLastModified()), entity.getDescription(), entity.getArchived(), entity.getCourseNumber(), 
                entity.getMaxParticipantCount(), creatorId, lastModifierId, subjectId, length, lenghtUnitId, tags);
          }
        }, 
        
        new Mapper<ModuleComponent>() {
          @Override
          public Object map(ModuleComponent entity) {
            Long lengthUnitId = entity.getLength() != null && entity.getLength().getUnit() != null ? entity.getLength().getUnit().getId() : null;
            Double length = entity.getLength() != null ? entity.getLength().getUnits() : null;
            return new fi.pyramus.rest.model.ModuleComponent(entity.getId(), entity.getName(), entity.getDescription(), length, lengthUnitId, entity.getArchived());
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
            
            return new fi.pyramus.rest.model.Project(entity.getId(), entity.getName(), entity.getDescription(), optionalStudiesLength, optionalStudiesLengthUnitId, toDateTime(entity.getCreated()), creatorId, toDateTime(entity.getLastModified()), lastModifierId, tags, entity.getArchived());
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
            
            return new fi.pyramus.rest.model.ProjectModule(entity.getId(), moduleId, optionality);
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
            
            return new fi.pyramus.rest.model.School(entity.getId(), entity.getCode(), entity.getName(), tags, fieldId, entity.getContactInfo().getAdditionalInfo(), entity.getArchived(), variables);
          }
        }, 
        
        new Mapper<SchoolField>() {
          @Override
          public Object map(SchoolField entity) {
            return new fi.pyramus.rest.model.SchoolField(entity.getId(), entity.getName(), entity.getArchived());
          }
        }, 
        
        new Mapper<SchoolVariableKey>() {
          @Override
          public Object map(SchoolVariableKey entity) {
            return new fi.pyramus.rest.model.VariableKey(entity.getVariableKey(), entity.getVariableName(), entity.getUserEditable(), toVariableType(entity.getVariableType()));
          }
        }, 
        
        new Mapper<CourseBaseVariableKey>() {
          @Override
          public Object map(CourseBaseVariableKey entity) {
            return new fi.pyramus.rest.model.VariableKey(entity.getVariableKey(), entity.getVariableName(), entity.getUserEditable(), toVariableType(entity.getVariableType()));
          }
        }, 
        
        new Mapper<Language>() {
          @Override
          public Object map(Language entity) {
            return new fi.pyramus.rest.model.Language(entity.getId(), entity.getCode(), entity.getName(), entity.getArchived());
          }
        }, 
        
        new Mapper<Municipality>() {
          @Override
          public Object map(Municipality entity) {
            return new fi.pyramus.rest.model.Municipality(entity.getId(), entity.getCode(), entity.getName(), entity.getArchived());
          }
        }, 
        
        new Mapper<Nationality>() {
          @Override
          public Object map(Nationality entity) {
            return new fi.pyramus.rest.model.Nationality(entity.getId(), entity.getCode(), entity.getName(), entity.getArchived());
          }
        }, 
        
        new Mapper<StudentActivityType>() {
          @Override
          public Object map(StudentActivityType entity) {
            return new fi.pyramus.rest.model.StudentActivityType(entity.getId(), entity.getName(), entity.getArchived());
          }
        }, 
        
        new Mapper<StudentEducationalLevel>() {
          @Override
          public Object map(StudentEducationalLevel entity) {
            return new fi.pyramus.rest.model.StudentEducationalLevel(entity.getId(), entity.getName(), entity.getArchived());
          }
        },
        
        new Mapper<StudentExaminationType>() {
          @Override
          public Object map(StudentExaminationType entity) {
            return new fi.pyramus.rest.model.StudentExaminationType(entity.getId(), entity.getName(), entity.getArchived());
          }
        },
        
        new Mapper<StudyProgrammeCategory>() {
          @Override
          public Object map(StudyProgrammeCategory entity) {
            Long educationTypeId = entity.getEducationType() != null ? entity.getEducationType().getId() : null;
            return new fi.pyramus.rest.model.StudyProgrammeCategory(entity.getId(), entity.getName(), educationTypeId, entity.getArchived());
          }
        },
        
        new Mapper<StudyProgramme>() {
          @Override
          public Object map(StudyProgramme entity) {
            Long categoryId = entity.getCategory() != null ? entity.getCategory().getId() : null;
            return new fi.pyramus.rest.model.StudyProgramme(entity.getId(), entity.getCode(), entity.getName(), categoryId, entity.getArchived());
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

            return new fi.pyramus.rest.model.StudentGroup(entity.getId(), entity.getName(), entity.getDescription(), 
              toDateTime(entity.getBeginDate()), creatorId, toDateTime(entity.getCreated()), lastModifierId, 
              toDateTime(entity.getLastModified()), tags, entity.getArchived() 
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
            return new fi.pyramus.rest.model.Person(entity.getId(), toDateTime(entity.getBirthday()), entity.getSocialSecurityNumber(), sex, entity.getSecureInfo(), entity.getBasicInfo(), defaultUserId);
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
            
            return new fi.pyramus.rest.model.Student(entity.getId(), personId, entity.getFirstName(), entity.getLastName(), 
                entity.getNickname(), entity.getAdditionalInfo(), additionalContectInfo, nationalityId, 
                languageId, municipalityId, schoolId, activityTypeId, examinationTypeId, educationalLevelId, 
                toDateTime(entity.getStudyTimeEnd()), studyProgrammeId, entity.getPreviousStudies(), entity.getEducation(), 
                entity.getLodging(), toDateTime(entity.getStudyStartDate()), toDateTime(entity.getStudyEndDate()), studyEndReasonId, 
                entity.getStudyEndText(), variables, tags, entity.getArchived());
          }
        },
        
        new Mapper<StudentStudyEndReason>() {
          @Override
          public Object map(StudentStudyEndReason entity) {
            Long parentReasonId = entity.getParentReason() != null ? entity.getParentReason().getId() : null;
            return new fi.pyramus.rest.model.StudentStudyEndReason(entity.getId(), entity.getName(), parentReasonId);
          }
        },
        
        new Mapper<StudentContactLogEntry>() {
          @Override
          public Object map(StudentContactLogEntry entity) {
            StudentContactLogEntryType type = StudentContactLogEntryType.valueOf(entity.getType().name());
            return new fi.pyramus.rest.model.StudentContactLogEntry(entity.getId(), entity.getText(), entity.getCreatorName(), toDateTime(entity.getEntryDate()), type, entity.getArchived());
          }
        },
        
        new Mapper<StudentGroupStudent>() {
          @Override
          public Object map(StudentGroupStudent entity) {
            Long studentId = entity.getStudent() != null ? entity.getStudent().getId() : null;
            return new fi.pyramus.rest.model.StudentGroupStudent(entity.getId(), studentId);
          }
        },
        
        new Mapper<Email>() {
          @Override
          public Object map(Email entity) {
            Long contactTypeId = entity.getContactType() != null ? entity.getContactType().getId() : null;
            return new fi.pyramus.rest.model.Email(entity.getId(), contactTypeId, entity.getDefaultAddress(), entity.getAddress());
          }
        },
        
        new Mapper<PhoneNumber>() {
          @Override
          public Object map(PhoneNumber entity) {
            Long contactTypeId = entity.getContactType() != null ? entity.getContactType().getId() : null;
            return new fi.pyramus.rest.model.PhoneNumber(entity.getId(), contactTypeId, entity.getDefaultNumber(), entity.getNumber());
          }
        },
        
        new Mapper<ContactURL>() {
          @Override
          public Object map(ContactURL entity) {
            Long contactURLTypeId = entity.getContactURLType() != null ? entity.getContactURLType().getId() : null;
            return new fi.pyramus.rest.model.ContactURL(entity.getId(), contactURLTypeId, entity.getURL());
          }
        },
        
        new Mapper<Address>() {
          @Override
          public Object map(Address entity) {
            Long contactTypeId = entity.getContactType() != null ? entity.getContactType().getId() : null;
            return new fi.pyramus.rest.model.Address(entity.getId(), contactTypeId, entity.getDefaultAddress(), entity.getName(), 
                entity.getStreetAddress(), entity.getPostalCode(), entity.getCity(), entity.getCountry());
          }
        }, 
        
        new Mapper<ContactType>() {
          @Override
          public Object map(ContactType entity) {
            return new fi.pyramus.rest.model.ContactType(entity.getId(), entity.getName(), entity.getArchived(), entity.getNonUnique());
          }
        }, 
        
        new Mapper<ContactURLType>() {
          @Override
          public Object map(ContactURLType entity) {
            return new fi.pyramus.rest.model.ContactURLType(entity.getId(), entity.getName(), entity.getArchived());
          }
        },
        
        new Mapper<UserVariableKey>() {
          @Override
          public Object map(UserVariableKey entity) {
            return new fi.pyramus.rest.model.VariableKey(entity.getVariableKey(), entity.getVariableName(), entity.getUserEditable(), toVariableType(entity.getVariableType()));
          }
        },
        
        new Mapper<CourseStaffMemberRole>() {
          @Override
          public Object map(CourseStaffMemberRole entity) {
            return new fi.pyramus.rest.model.CourseStaffMemberRole(entity.getId(), entity.getName());
          }
        },
        
        new Mapper<CourseStaffMember>() {
          @Override
          public Object map(CourseStaffMember entity) {
            Long courseId = entity.getCourse() != null ? entity.getCourse().getId() : null;
            Long userId = entity.getStaffMember() != null ? entity.getStaffMember().getId() : null;
            Long roleId = entity.getRole() != null ? entity.getRole().getId() : null;
            return new fi.pyramus.rest.model.CourseStaffMember(entity.getId(), courseId, userId, roleId);
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
            
            return new fi.pyramus.rest.model.CourseStudent(entity.getId(), courseId, studentId, toDateTime(entity.getEnrolmentTime()), entity.getArchived(), participantTypeId, courseEnrolmentTypeId, entity.getLodging(), optionality, billingDetailsId);
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
          
          return new fi.pyramus.rest.model.StaffMember(entity.getId(), personId, additionalContactInfo, 
              entity.getFirstName(), entity.getLastName(), entity.getTitle(), role, tags, variables);
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

  private DateTime toDateTime(Date date) {
    if (date == null) {
      return null;
    }
    
    return new DateTime(date.getTime());
  }
 
  private VariableType toVariableType(fi.pyramus.domainmodel.base.VariableType variableType) {
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
  
  private Map<Class<?>, Mapper<Object>> mappers = new HashMap<Class<?>, ObjectFactory.Mapper<Object>>();
}
