package fi.otavanopisto.pyramus.services.entities;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.otavanopisto.pyramus.domainmodel.base.AcademicTerm;
import fi.otavanopisto.pyramus.domainmodel.base.Address;
import fi.otavanopisto.pyramus.domainmodel.base.CourseEducationSubtype;
import fi.otavanopisto.pyramus.domainmodel.base.CourseEducationType;
import fi.otavanopisto.pyramus.domainmodel.base.EducationSubtype;
import fi.otavanopisto.pyramus.domainmodel.base.EducationType;
import fi.otavanopisto.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.otavanopisto.pyramus.domainmodel.base.Language;
import fi.otavanopisto.pyramus.domainmodel.base.Municipality;
import fi.otavanopisto.pyramus.domainmodel.base.Nationality;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.base.School;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgrammeCategory;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseComponent;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseDescription;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseDescriptionCategory;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseEnrolmentType;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseParticipationType;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStaffMember;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStaffMemberRole;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessmentRequest;
import fi.otavanopisto.pyramus.domainmodel.grading.Credit;
import fi.otavanopisto.pyramus.domainmodel.grading.Grade;
import fi.otavanopisto.pyramus.domainmodel.grading.GradingScale;
import fi.otavanopisto.pyramus.domainmodel.grading.TransferCredit;
import fi.otavanopisto.pyramus.domainmodel.modules.Module;
import fi.otavanopisto.pyramus.domainmodel.modules.ModuleComponent;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.services.entities.base.AcademicTermEntity;
import fi.otavanopisto.pyramus.services.entities.base.AcademicTermEntityFactory;
import fi.otavanopisto.pyramus.services.entities.base.AddressEntity;
import fi.otavanopisto.pyramus.services.entities.base.AddressEntityFactory;
import fi.otavanopisto.pyramus.services.entities.base.EducationSubtypeEntity;
import fi.otavanopisto.pyramus.services.entities.base.EducationSubtypeEntityFactory;
import fi.otavanopisto.pyramus.services.entities.base.EducationTypeEntity;
import fi.otavanopisto.pyramus.services.entities.base.EducationTypeEntityFactory;
import fi.otavanopisto.pyramus.services.entities.base.EducationalTimeUnitEntity;
import fi.otavanopisto.pyramus.services.entities.base.EducationalTimeUnitEntityFactory;
import fi.otavanopisto.pyramus.services.entities.base.LanguageEntity;
import fi.otavanopisto.pyramus.services.entities.base.LanguageEntityFactory;
import fi.otavanopisto.pyramus.services.entities.base.MunicipalityEntity;
import fi.otavanopisto.pyramus.services.entities.base.MunicipalityEntityFactory;
import fi.otavanopisto.pyramus.services.entities.base.NationalityEntity;
import fi.otavanopisto.pyramus.services.entities.base.NationalityEntityFactory;
import fi.otavanopisto.pyramus.services.entities.base.SchoolEntity;
import fi.otavanopisto.pyramus.services.entities.base.SchoolEntityFactory;
import fi.otavanopisto.pyramus.services.entities.base.StudyProgrammeCategoryEntity;
import fi.otavanopisto.pyramus.services.entities.base.StudyProgrammeCategoryEntityFactory;
import fi.otavanopisto.pyramus.services.entities.base.StudyProgrammeEntity;
import fi.otavanopisto.pyramus.services.entities.base.StudyProgrammeEntityFactory;
import fi.otavanopisto.pyramus.services.entities.base.SubjectEntity;
import fi.otavanopisto.pyramus.services.entities.base.SubjectEntityFactory;
import fi.otavanopisto.pyramus.services.entities.courses.CourseComponentEntity;
import fi.otavanopisto.pyramus.services.entities.courses.CourseComponentEntityFactory;
import fi.otavanopisto.pyramus.services.entities.courses.CourseDescriptionCategoryEntity;
import fi.otavanopisto.pyramus.services.entities.courses.CourseDescriptionCategoryEntityFactory;
import fi.otavanopisto.pyramus.services.entities.courses.CourseDescriptionEntity;
import fi.otavanopisto.pyramus.services.entities.courses.CourseDescriptionEntityFactory;
import fi.otavanopisto.pyramus.services.entities.courses.CourseEducationSubtypeEntity;
import fi.otavanopisto.pyramus.services.entities.courses.CourseEducationSubtypeEntityFactory;
import fi.otavanopisto.pyramus.services.entities.courses.CourseEducationTypeEntity;
import fi.otavanopisto.pyramus.services.entities.courses.CourseEducationTypeEntityFactory;
import fi.otavanopisto.pyramus.services.entities.courses.CourseEnrolmentTypeEntity;
import fi.otavanopisto.pyramus.services.entities.courses.CourseEnrolmentTypeEntityFactory;
import fi.otavanopisto.pyramus.services.entities.courses.CourseEntity;
import fi.otavanopisto.pyramus.services.entities.courses.CourseEntityFactory;
import fi.otavanopisto.pyramus.services.entities.courses.CourseParticipationTypeEntity;
import fi.otavanopisto.pyramus.services.entities.courses.CourseParticipationTypeEntityFactory;
import fi.otavanopisto.pyramus.services.entities.courses.CourseStudentEntity;
import fi.otavanopisto.pyramus.services.entities.courses.CourseStudentEntityFactory;
import fi.otavanopisto.pyramus.services.entities.courses.CourseUserEntity;
import fi.otavanopisto.pyramus.services.entities.courses.CourseUserEntityFactory;
import fi.otavanopisto.pyramus.services.entities.courses.CourseUserRoleEntity;
import fi.otavanopisto.pyramus.services.entities.courses.CourseUserRoleEntityFactory;
import fi.otavanopisto.pyramus.services.entities.grading.CourseAssessmentEntity;
import fi.otavanopisto.pyramus.services.entities.grading.CourseAssessmentEntityFactory;
import fi.otavanopisto.pyramus.services.entities.grading.CourseAssessmentRequestEntity;
import fi.otavanopisto.pyramus.services.entities.grading.CourseAssessmentRequestEntityFactory;
import fi.otavanopisto.pyramus.services.entities.grading.CreditEntity;
import fi.otavanopisto.pyramus.services.entities.grading.CreditEntityFactory;
import fi.otavanopisto.pyramus.services.entities.grading.GradeEntity;
import fi.otavanopisto.pyramus.services.entities.grading.GradeEntityFactory;
import fi.otavanopisto.pyramus.services.entities.grading.GradingScaleEntity;
import fi.otavanopisto.pyramus.services.entities.grading.GradingScaleEntityFactory;
import fi.otavanopisto.pyramus.services.entities.grading.TransferCreditEntity;
import fi.otavanopisto.pyramus.services.entities.grading.TransferCreditEntityFactory;
import fi.otavanopisto.pyramus.services.entities.modules.ModuleComponentEntity;
import fi.otavanopisto.pyramus.services.entities.modules.ModuleComponentEntityFactory;
import fi.otavanopisto.pyramus.services.entities.modules.ModuleEntity;
import fi.otavanopisto.pyramus.services.entities.modules.ModuleEntityFactory;
import fi.otavanopisto.pyramus.services.entities.students.AbstractStudentEntity;
import fi.otavanopisto.pyramus.services.entities.students.AbstractStudentEntityFactory;
import fi.otavanopisto.pyramus.services.entities.students.StudentEntity;
import fi.otavanopisto.pyramus.services.entities.students.StudentEntityFactory;
import fi.otavanopisto.pyramus.services.entities.users.UserEntity;
import fi.otavanopisto.pyramus.services.entities.users.UserEntityFactory;

public class EntityFactoryVault {
  
  public static TransferCreditEntity buildFromDomainObject(TransferCredit transferCredit) {
    return (TransferCreditEntity) getEntityFactory(TransferCreditEntity.class).buildFromDomainObject(transferCredit);
  }
  
  public static CourseAssessmentEntity buildFromDomainObject(CourseAssessment courseAssessment) {
    return (CourseAssessmentEntity) getEntityFactory(CourseAssessmentEntity.class).buildFromDomainObject(courseAssessment);
  }
  
  public static CreditEntity buildFromDomainObject(Credit credit) {
    return (CreditEntity) getEntityFactory(CreditEntity.class).buildFromDomainObject(credit);
  }
  
  public static GradeEntity buildFromDomainObject(Grade grade) {
    return (GradeEntity) getEntityFactory(GradeEntity.class).buildFromDomainObject(grade);
  }

  public static GradingScaleEntity buildFromDomainObject(GradingScale gradingScale) {
    return (GradingScaleEntity) getEntityFactory(GradingScaleEntity.class).buildFromDomainObject(gradingScale);
  }
  
  public static AbstractStudentEntity buildFromDomainObject(Person person) {
    return (AbstractStudentEntity) EntityFactoryVault.getEntityFactory(AbstractStudentEntity.class).buildFromDomainObject(person);
  }

  public static StudentEntity buildFromDomainObject(Student student) {
    return (StudentEntity) EntityFactoryVault.getEntityFactory(StudentEntity.class).buildFromDomainObject(student);
  }

  public static NationalityEntity buildFromDomainObject(Nationality nationality) {
    return (NationalityEntity) EntityFactoryVault.getEntityFactory(NationalityEntity.class).buildFromDomainObject(nationality);
  }
  
  public static UserEntity buildFromDomainObject(StaffMember staffMember) {
    return (UserEntity) EntityFactoryVault.getEntityFactory(UserEntity.class).buildFromDomainObject(staffMember);
  }

  public static AddressEntity buildFromDomainObject(Address address) {
    return (AddressEntity) EntityFactoryVault.getEntityFactory(AddressEntity.class).buildFromDomainObject(address);
  }
  
  public static LanguageEntity buildFromDomainObject(Language language) {
    return (LanguageEntity) EntityFactoryVault.getEntityFactory(LanguageEntity.class).buildFromDomainObject(language);
  }

  public static MunicipalityEntity buildFromDomainObject(Municipality municipality) {
    return (MunicipalityEntity) EntityFactoryVault.getEntityFactory(MunicipalityEntity.class).buildFromDomainObject(municipality);
  }
  
  public static SchoolEntity buildFromDomainObject(School school) {
    return (SchoolEntity) EntityFactoryVault.getEntityFactory(SchoolEntity.class).buildFromDomainObject(school);
  }
  
  public static EducationalTimeUnitEntity buildFromDomainObject(EducationalTimeUnit educationalTimeUnit) {
    return (EducationalTimeUnitEntity) EntityFactoryVault.getEntityFactory(EducationalTimeUnitEntity.class).buildFromDomainObject(educationalTimeUnit);
  }
  
  public static AcademicTermEntity buildFromDomainObject(AcademicTerm academicTerm) {
    return (AcademicTermEntity) EntityFactoryVault.getEntityFactory(AcademicTermEntity.class).buildFromDomainObject(academicTerm);
  }
  
  public static EducationTypeEntity buildFromDomainObject(EducationType educationType) {
    return (EducationTypeEntity) EntityFactoryVault.getEntityFactory(EducationTypeEntity.class).buildFromDomainObject(educationType);
  }
  
  public static EducationSubtypeEntity buildFromDomainObject(EducationSubtype educationSubtype) {
    return (EducationSubtypeEntity) EntityFactoryVault.getEntityFactory(EducationSubtypeEntity.class).buildFromDomainObject(educationSubtype);
  }
  
  public static SubjectEntity buildFromDomainObject(Subject subject) {
    return (SubjectEntity) EntityFactoryVault.getEntityFactory(SubjectEntity.class).buildFromDomainObject(subject);
  }
  
  public static CourseComponentEntity buildFromDomainObject(CourseComponent courseComponent) {
    return (CourseComponentEntity) EntityFactoryVault.getEntityFactory(CourseComponentEntity.class).buildFromDomainObject(courseComponent);
  }
  
  public static CourseEnrolmentTypeEntity buildFromDomainObject(CourseEnrolmentType courseEnrolmentType) {
    return (CourseEnrolmentTypeEntity) EntityFactoryVault.getEntityFactory(CourseEnrolmentTypeEntity.class).buildFromDomainObject(courseEnrolmentType);
  }
  
  public static CourseParticipationTypeEntity buildFromDomainObject(CourseParticipationType courseParticipationType) {
    return (CourseParticipationTypeEntity) EntityFactoryVault.getEntityFactory(CourseParticipationTypeEntity.class).buildFromDomainObject(courseParticipationType);
  }
  
  public static CourseStudentEntity buildFromDomainObject(CourseStudent courseStudent) {
    return (CourseStudentEntity) EntityFactoryVault.getEntityFactory(CourseStudentEntity.class).buildFromDomainObject(courseStudent);
  }
  
  public static CourseUserEntity buildFromDomainObject(CourseStaffMember courseUser) {
    return (CourseUserEntity) EntityFactoryVault.getEntityFactory(CourseUserEntity.class).buildFromDomainObject(courseUser);
  }

  public static CourseUserRoleEntity buildFromDomainObject(CourseStaffMemberRole courseUserRole) {
    return (CourseUserRoleEntity) EntityFactoryVault.getEntityFactory(CourseUserRoleEntity.class).buildFromDomainObject(courseUserRole);
  }
  
  public static CourseEducationTypeEntity buildFromDomainObject(CourseEducationType courseEducationType) {
    return (CourseEducationTypeEntity) EntityFactoryVault.getEntityFactory(CourseEducationTypeEntity.class).buildFromDomainObject(courseEducationType);
  }
  
  public static CourseEducationSubtypeEntity buildFromDomainObject(CourseEducationSubtype courseEducationSubtype) {
    return (CourseEducationSubtypeEntity) EntityFactoryVault.getEntityFactory(CourseEducationSubtypeEntity.class).buildFromDomainObject(courseEducationSubtype);
  }
  
  public static CourseEntity buildFromDomainObject(Course course) {
    return (CourseEntity) EntityFactoryVault.getEntityFactory(CourseEntity.class).buildFromDomainObject(course);
  }

  public static CourseDescriptionEntity buildFromDomainObject(CourseDescription courseDescription) {
    return (CourseDescriptionEntity) EntityFactoryVault.getEntityFactory(CourseDescriptionEntity.class).buildFromDomainObject(courseDescription);
  }

  public static CourseDescriptionCategoryEntity buildFromDomainObject(CourseDescriptionCategory category) {
    return (CourseDescriptionCategoryEntity) EntityFactoryVault.getEntityFactory(CourseDescriptionCategoryEntity.class).buildFromDomainObject(category);
  }
  
  public static ModuleEntity buildFromDomainObject(Module module) {
    return (ModuleEntity) EntityFactoryVault.getEntityFactory(ModuleEntity.class).buildFromDomainObject(module);
  }
  
  public static ModuleComponentEntity buildFromDomainObject(ModuleComponent moduleComponent) {
    return (ModuleComponentEntity) EntityFactoryVault.getEntityFactory(ModuleComponentEntity.class).buildFromDomainObject(moduleComponent);
  }

  public static StudyProgrammeEntity buildFromDomainObject(StudyProgramme studyProgramme) {
    return (StudyProgrammeEntity) EntityFactoryVault.getEntityFactory(StudyProgrammeEntity.class).buildFromDomainObject(studyProgramme);
  }

  public static StudyProgrammeCategoryEntity buildFromDomainObject(StudyProgrammeCategory studyProgrammeCategory) {
    return (StudyProgrammeCategoryEntity) EntityFactoryVault.getEntityFactory(StudyProgrammeCategoryEntity.class).buildFromDomainObject(studyProgrammeCategory);
  }

  public static CourseAssessmentRequestEntity buildFromDomainObject(CourseAssessmentRequest courseAssessmentRequest) {
    return (CourseAssessmentRequestEntity) EntityFactoryVault.getEntityFactory(CourseAssessmentRequestEntity.class).buildFromDomainObject(courseAssessmentRequest);
  }
  
  public static Object[] buildFromDomainObjects(Collection<?> entities) {
    Class<?> listClass = null;
    if (!entities.isEmpty()) {
      if (entities instanceof List) {
        listClass = ((List<?>) entities).get(0).getClass();
      } else {
        listClass = entities.iterator().next().getClass();
      }
      
      return buildCollection(getEntityClassForPojoClass(listClass), entities);
    } else {
      return null;  
    }
  }
  
  private static Object[] buildCollection(Class<?> entityClass, Collection<?> objects) {
    Object[] entities = (Object[]) Array.newInstance(entityClass, objects.size());
    int i = 0;
    for (Object domainObject : objects) {
      entities[i++] = getEntityFactory(entityClass).buildFromDomainObject(domainObject);
    }
    
    return entities;
  }
  
  private static EntityFactory<?> getEntityFactory(Class<?> entityClass) {
    return factories.get(entityClass);
  }
  
  private static void registerEntityFactory(Class<?> pojoClass, Class<?> entityClass, EntityFactory<?> entityFactory) {
    factories.put(entityClass, entityFactory);
    pojoEntityClassMap.put(pojoClass, entityClass);
  }
  
  private static Class<?> getEntityClassForPojoClass(Class<?> pojoClass) {
    return pojoEntityClassMap.get(pojoClass);
  }
  
  private static Map<Class<?>, EntityFactory<?>> factories = new HashMap<>();
  private static Map<Class<?>, Class<?>> pojoEntityClassMap = new HashMap<>();

  static {
    /* Base */ 
    
    registerEntityFactory(Address.class, AddressEntity.class, new AddressEntityFactory());
    registerEntityFactory(School.class, SchoolEntity.class, new SchoolEntityFactory());
    registerEntityFactory(Nationality.class, NationalityEntity.class, new NationalityEntityFactory());
    registerEntityFactory(School.class, SchoolEntity.class, new SchoolEntityFactory());
    registerEntityFactory(Nationality.class, NationalityEntity.class, new NationalityEntityFactory());
    registerEntityFactory(Municipality.class, MunicipalityEntity.class, new MunicipalityEntityFactory());
    registerEntityFactory(Language.class, LanguageEntity.class, new LanguageEntityFactory());
    registerEntityFactory(EducationalTimeUnit.class, EducationalTimeUnitEntity.class, new EducationalTimeUnitEntityFactory());
    registerEntityFactory(AcademicTerm.class, AcademicTermEntity.class, new AcademicTermEntityFactory());
    registerEntityFactory(EducationType.class, EducationTypeEntity.class, new EducationTypeEntityFactory());
    registerEntityFactory(EducationSubtype.class, EducationSubtypeEntity.class, new EducationSubtypeEntityFactory());
    registerEntityFactory(Subject.class, SubjectEntity.class, new SubjectEntityFactory());
    registerEntityFactory(StudyProgramme.class, StudyProgrammeEntity.class, new StudyProgrammeEntityFactory());
    registerEntityFactory(StudyProgrammeCategory.class, StudyProgrammeCategoryEntity.class, new StudyProgrammeCategoryEntityFactory());
    registerEntityFactory(Person.class ,AbstractStudentEntity.class, new AbstractStudentEntityFactory());
    
    /* Users */ 
    
    registerEntityFactory(StaffMember.class, UserEntity.class, new UserEntityFactory());
    
    /* Students */ 
    
    registerEntityFactory(Student.class, StudentEntity.class, new StudentEntityFactory());
    
    /* Courses */
        
    registerEntityFactory(Course.class, CourseEntity.class, new CourseEntityFactory());
    registerEntityFactory(CourseComponent.class, CourseComponentEntity.class, new CourseComponentEntityFactory());
    registerEntityFactory(CourseEnrolmentType.class, CourseEnrolmentTypeEntity.class, new CourseEnrolmentTypeEntityFactory());
    registerEntityFactory(CourseParticipationType.class, CourseParticipationTypeEntity.class, new CourseParticipationTypeEntityFactory());
    registerEntityFactory(CourseStudent.class, CourseStudentEntity.class, new CourseStudentEntityFactory());
    registerEntityFactory(CourseEducationType.class, CourseEducationTypeEntity.class, new CourseEducationTypeEntityFactory());
    registerEntityFactory(CourseEducationSubtype.class, CourseEducationSubtypeEntity.class, new CourseEducationSubtypeEntityFactory());
    registerEntityFactory(CourseStaffMember.class, CourseUserEntity.class, new CourseUserEntityFactory());
    registerEntityFactory(CourseStaffMemberRole.class, CourseUserRoleEntity.class, new CourseUserRoleEntityFactory());
    registerEntityFactory(CourseDescriptionCategory.class, CourseDescriptionCategoryEntity.class, new CourseDescriptionCategoryEntityFactory());
    registerEntityFactory(CourseDescription.class, CourseDescriptionEntity.class, new CourseDescriptionEntityFactory());
    
    /* Grading */
    
    registerEntityFactory(CourseAssessment.class, CourseAssessmentEntity.class, new CourseAssessmentEntityFactory());
    registerEntityFactory(Credit.class, CreditEntity.class, new CreditEntityFactory());
    registerEntityFactory(Grade.class, GradeEntity.class, new GradeEntityFactory());
    registerEntityFactory(GradingScale.class, GradingScaleEntity.class, new GradingScaleEntityFactory());
    registerEntityFactory(TransferCredit.class, TransferCreditEntity.class, new TransferCreditEntityFactory());
    registerEntityFactory(CourseAssessmentRequest.class, CourseAssessmentRequestEntity.class, new CourseAssessmentRequestEntityFactory());
    
    /* Modules */
  
    registerEntityFactory(Module.class, ModuleEntity.class, new ModuleEntityFactory());
    registerEntityFactory(ModuleComponent.class, ModuleComponentEntity.class, new ModuleComponentEntityFactory());
  }
}
