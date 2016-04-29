package fi.otavanopisto.pyramus.services;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.Date;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.BindingType;

import org.apache.commons.lang.StringUtils;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.CourseBaseVariableDAO;
import fi.otavanopisto.pyramus.dao.base.CourseEducationSubtypeDAO;
import fi.otavanopisto.pyramus.dao.base.CourseEducationTypeDAO;
import fi.otavanopisto.pyramus.dao.base.DefaultsDAO;
import fi.otavanopisto.pyramus.dao.base.EducationSubtypeDAO;
import fi.otavanopisto.pyramus.dao.base.EducationTypeDAO;
import fi.otavanopisto.pyramus.dao.base.EducationalTimeUnitDAO;
import fi.otavanopisto.pyramus.dao.base.SubjectDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseComponentDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseDescriptionCategoryDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseDescriptionDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseEnrolmentTypeDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseParticipationTypeDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseStaffMemberDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseStaffMemberRoleDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseStateDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseStudentDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseStudentVariableDAO;
import fi.otavanopisto.pyramus.dao.modules.ModuleDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.accommodation.Room;
import fi.otavanopisto.pyramus.domainmodel.base.CourseBase;
import fi.otavanopisto.pyramus.domainmodel.base.CourseEducationSubtype;
import fi.otavanopisto.pyramus.domainmodel.base.CourseEducationType;
import fi.otavanopisto.pyramus.domainmodel.base.CourseOptionality;
import fi.otavanopisto.pyramus.domainmodel.base.Defaults;
import fi.otavanopisto.pyramus.domainmodel.base.EducationSubtype;
import fi.otavanopisto.pyramus.domainmodel.base.EducationType;
import fi.otavanopisto.pyramus.domainmodel.base.EducationalLength;
import fi.otavanopisto.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseComponent;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseDescriptionCategory;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseEnrolmentType;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseParticipationType;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStaffMember;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStaffMemberRole;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseState;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseType;
import fi.otavanopisto.pyramus.domainmodel.modules.Module;
import fi.otavanopisto.pyramus.domainmodel.modules.ModuleComponent;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.persistence.search.SearchResult;
import fi.otavanopisto.pyramus.persistence.search.SearchTimeFilterMode;
import fi.otavanopisto.pyramus.services.entities.EntityFactoryVault;
import fi.otavanopisto.pyramus.services.entities.courses.CourseComponentEntity;
import fi.otavanopisto.pyramus.services.entities.courses.CourseDescriptionCategoryEntity;
import fi.otavanopisto.pyramus.services.entities.courses.CourseDescriptionEntity;
import fi.otavanopisto.pyramus.services.entities.courses.CourseEducationSubtypeEntity;
import fi.otavanopisto.pyramus.services.entities.courses.CourseEducationTypeEntity;
import fi.otavanopisto.pyramus.services.entities.courses.CourseEnrolmentTypeEntity;
import fi.otavanopisto.pyramus.services.entities.courses.CourseEntity;
import fi.otavanopisto.pyramus.services.entities.courses.CourseEntitySearchResult;
import fi.otavanopisto.pyramus.services.entities.courses.CourseParticipationTypeEntity;
import fi.otavanopisto.pyramus.services.entities.courses.CourseStudentEntity;
import fi.otavanopisto.pyramus.services.entities.courses.CourseUserEntity;

@Stateless
@WebService
@BindingType(javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@RolesAllowed("WebServices")
public class CoursesService extends PyramusService {

  public CourseEntity createCourse(@WebParam (name = "moduleId") Long moduleId, @WebParam (name = "name") String name, @WebParam (name = "nameExtension") String nameExtension, @WebParam (name = "subjectId") Long subjectId,
      @WebParam (name = "courseNumber") Integer courseNumber, @WebParam (name = "beginDate") Date beginDate, @WebParam (name = "endDate") Date endDate, @WebParam (name = "courseLength") Double courseLength, @WebParam (name = "courseLengthTimeUnitId") Long courseLengthTimeUnitId,
      @WebParam (name = "description") String description, @WebParam (name = "creatingUserId") Long creatingUserId) {

    StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffMemberDAO();
    CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();
    ModuleDAO moduleDAO = DAOFactory.getInstance().getModuleDAO();
    CourseComponentDAO componentDAO = DAOFactory.getInstance().getCourseComponentDAO();
    CourseDescriptionDAO descriptionDAO = DAOFactory.getInstance().getCourseDescriptionDAO();
    CourseEducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getCourseEducationTypeDAO();
    CourseEducationSubtypeDAO educationSubtypeDAO = DAOFactory.getInstance().getCourseEducationSubtypeDAO();
    EducationalTimeUnitDAO educationalTimeUnitDAO = DAOFactory.getInstance().getEducationalTimeUnitDAO();
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();
    DefaultsDAO defaultsDAO = DAOFactory.getInstance().getDefaultsDAO();

    Module module = moduleId == null ? null : moduleDAO.findById(moduleId);
    Subject subject = subjectId == null ? null : subjectDAO.findById(subjectId);
    EducationalTimeUnit courseLengthTimeUnit = courseLengthTimeUnitId == null ? null : educationalTimeUnitDAO.findById(courseLengthTimeUnitId);
    User creatingUser = userDAO.findById(creatingUserId);

    // If the course is based on a module, replace all null values with those from the module

    if (module != null) {
      name = name == null ? module.getName() : name;
      subject = subject == null ? module.getSubject() : subject;
      courseNumber = courseNumber == null ? module.getCourseNumber() : courseNumber;
      if (courseLength == null && module.getCourseLength() != null) {
        courseLength = module.getCourseLength().getUnits();
        courseLengthTimeUnit = module.getCourseLength().getUnit();
      }
      description = description == null ? module.getDescription() : description;
    }
    
    CourseState state = defaultsDAO.getDefaults().getInitialCourseState();
    CourseType type = null;
    
    // Course creation

    Course course = courseDAO.create(module, name, nameExtension, state, type, subject, courseNumber, beginDate, endDate,
        courseLength, courseLengthTimeUnit, null, null, null, null, null, null, description, null, null, creatingUser);
    
    validateEntity(course);

    // Components, education types, and education subtypes from the possible module

    if (module != null) {

      // Course Description copying from module to course
      descriptionDAO.copy(module, course);
      
      // Components

      List<ModuleComponent> moduleComponents = module.getModuleComponents();
      if (moduleComponents != null) {
        for (ModuleComponent moduleComponent : moduleComponents) {
          EducationalLength educationalLength = moduleComponent.getLength();
          CourseComponent courseComponent = componentDAO.create(
              course,
              educationalLength == null ? null : educationalLength.getUnits(),
              educationalLength == null ? null : educationalLength.getUnit(),
              moduleComponent.getName(),
              moduleComponent.getDescription());
          validateEntity(courseComponent);
        }
      }

      // Education types

      List<CourseEducationType> typesInModule = module.getCourseEducationTypes();
      if (typesInModule != null) {
        for (CourseEducationType typeInModule : typesInModule) {
          CourseEducationType typeInCourse = educationTypeDAO.create(course, typeInModule.getEducationType());
          validateEntity(typeInCourse);

          // Education subtypes

          List<CourseEducationSubtype> subTypesInModule = typeInModule.getCourseEducationSubtypes();
          if (subTypesInModule != null) {
            for (CourseEducationSubtype subtypeInModule : subTypesInModule) {
              CourseEducationSubtype courseEducationSubtype = educationSubtypeDAO.create(typeInCourse, subtypeInModule.getEducationSubtype());
              validateEntity(courseEducationSubtype);
            }
          }
        }
      }
    }

    return EntityFactoryVault.buildFromDomainObject(course);
  }

  public void updateCourse(@WebParam (name = "courseId") Long courseId, @WebParam (name = "name") String name, @WebParam (name = "nameExtension") String nameExtension, @WebParam (name = "subjectId") Long subjectId, @WebParam (name = "courseNumber") Integer courseNumber,
      @WebParam (name = "beginDate") Date beginDate, @WebParam (name = "endDate") Date endDate, @WebParam (name = "courseLength") Double courseLength, @WebParam (name = "courseLengthTimeUnitId") Long courseLengthTimeUnitId, @WebParam (name = "description") String description,
      @WebParam (name = "modifyingUserId") Long modifyingUserId) {

    StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffMemberDAO();
    CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();
    EducationalTimeUnitDAO educationalTimeUnitDAO = DAOFactory.getInstance().getEducationalTimeUnitDAO();
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();

    Course course = courseDAO.findById(courseId);
    Subject subject = subjectId == null ? null : subjectDAO.findById(subjectId);
    EducationalTimeUnit courseLengthTimeUnit = courseLengthTimeUnitId == null ? null : educationalTimeUnitDAO.findById(courseLengthTimeUnitId);
    User modifyingUser = userDAO.findById(modifyingUserId);

    courseDAO.update(course, name, nameExtension, course.getState(), course.getType(), subject, courseNumber, beginDate, endDate, courseLength,
        courseLengthTimeUnit, course.getDistanceTeachingDays(), course.getLocalTeachingDays(), course.getTeachingHours(), course.getDistanceTeachingHours(),
        course.getPlanningHours(), course.getAssessingHours(), description, course.getMaxParticipantCount(), course.getEnrolmentTimeEnd(), modifyingUser);
    validateEntity(course);
  }

  public CourseComponentEntity createCourseComponent(@WebParam (name = "courseId") Long courseId, @WebParam (name = "componentLength") Double componentLength,
      @WebParam (name = "componentLengthTimeUnitId") Long componentLengthTimeUnitId, @WebParam (name = "name") String name, @WebParam (name = "description") String description) {

    CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();
    CourseComponentDAO componentDAO = DAOFactory.getInstance().getCourseComponentDAO();
    EducationalTimeUnitDAO educationalTimeUnitDAO = DAOFactory.getInstance().getEducationalTimeUnitDAO();

    Course course = courseDAO.findById(courseId);
    EducationalTimeUnit componentLengthTimeUnit = componentLengthTimeUnitId == null ? null : educationalTimeUnitDAO.findById(componentLengthTimeUnitId);

    CourseComponent courseComponent = componentDAO.create(course, componentLength,
            componentLengthTimeUnit, name, description);
    
    validateEntity(courseComponent);
    
    return EntityFactoryVault.buildFromDomainObject(courseComponent);
  }

  public void updateCourseComponent(@WebParam (name = "courseComponentId") Long courseComponentId, @WebParam (name = "componentLength") Double componentLength, @WebParam (name = "componentLengthTimeUnitId") Long componentLengthTimeUnitId,
      @WebParam (name = "name") String name, @WebParam (name = "description") String description) {
    
    CourseComponentDAO componentDAO = DAOFactory.getInstance().getCourseComponentDAO();
    EducationalTimeUnitDAO educationalTimeUnitDAO = DAOFactory.getInstance().getEducationalTimeUnitDAO();

    CourseComponent courseComponent = componentDAO.findById(courseComponentId);
    EducationalTimeUnit componentLengthTimeUnit = componentLengthTimeUnitId == null ? null : educationalTimeUnitDAO.findById(componentLengthTimeUnitId);

    componentDAO.update(courseComponent, componentLength, componentLengthTimeUnit, name, description);
    validateEntity(courseComponent);
  }

  public CourseComponentEntity getCourseComponentById(@WebParam (name = "courseComponentId") Long courseComponentId) {
    CourseComponentDAO componentDAO = DAOFactory.getInstance().getCourseComponentDAO();
    return EntityFactoryVault.buildFromDomainObject(componentDAO.findById(courseComponentId));
  }

  public CourseEducationTypeEntity[] listCourseEducationTypes(@WebParam (name = "courseId") Long courseId) {
    CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();
    Course course = courseDAO.findById(courseId);
    return (CourseEducationTypeEntity[]) EntityFactoryVault.buildFromDomainObjects(course.getCourseEducationTypes());
  }

  public CourseEducationTypeEntity getCourseEducationTypeById(@WebParam (name = "courseEducationTypeId") Long courseEducationTypeId) {
    CourseEducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getCourseEducationTypeDAO();
    return EntityFactoryVault.buildFromDomainObject(educationTypeDAO.findById(courseEducationTypeId));
  }

  public CourseEducationTypeEntity addCourseEducationType(@WebParam (name = "courseId") Long courseId, @WebParam (name = "educationTypeId") Long educationTypeId) {
    CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();
    CourseEducationTypeDAO courseEducationTypeDAO = DAOFactory.getInstance().getCourseEducationTypeDAO();
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();    

    Course course = courseDAO.findById(courseId);
    EducationType educationType = educationTypeDAO.findById(educationTypeId);
    CourseEducationType courseEducationType = courseEducationTypeDAO.create(course, educationType);
    validateEntity(courseEducationType);
    return EntityFactoryVault.buildFromDomainObject(courseEducationType);
  }

  public CourseEducationSubtypeEntity addCourseEducationSubtype(@WebParam (name = "courseEducationTypeId") Long courseEducationTypeId, @WebParam (name = "educationSubtypeId") Long educationSubtypeId) {
    CourseEducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getCourseEducationTypeDAO();
    CourseEducationSubtypeDAO courseEducationSubtypeDAO = DAOFactory.getInstance().getCourseEducationSubtypeDAO();
    EducationSubtypeDAO educationSubtypeDAO = DAOFactory.getInstance().getEducationSubtypeDAO();    

    CourseEducationType courseEducationType = educationTypeDAO.findById(courseEducationTypeId);
    EducationSubtype educationSubtype = educationSubtypeDAO.findById(educationSubtypeId);
    CourseEducationSubtype courseEducationSubtype = courseEducationSubtypeDAO.create(courseEducationType,
            educationSubtype);
    validateEntity(courseEducationSubtype);
    return EntityFactoryVault.buildFromDomainObject(courseEducationSubtype);
  }

  public CourseEntity[] listCourses() {
    CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();
    return (CourseEntity[]) EntityFactoryVault.buildFromDomainObjects(courseDAO.listUnarchived());
  }

  public CourseStudentEntity addCourseStudent(@WebParam (name = "courseId") Long courseId, @WebParam (name = "studentId") Long studentId, @WebParam (name = "courseEnrolmentTypeId") Long courseEnrolmentTypeId,
      @WebParam (name = "participationTypeId") Long participationTypeId, @WebParam (name = "enrolmentDate") Date enrolmentDate, @WebParam (name = "lodging") Boolean lodging, @WebParam (name = "optionality") String optionality) {
    
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();
    CourseParticipationTypeDAO participationTypeDAO = DAOFactory.getInstance().getCourseParticipationTypeDAO();
    CourseEnrolmentTypeDAO enrolmentTypeDAO = DAOFactory.getInstance().getCourseEnrolmentTypeDAO();
    CourseStudentDAO courseStudentDAO = DAOFactory.getInstance().getCourseStudentDAO();
    DefaultsDAO defaultsDAO = DAOFactory.getInstance().getDefaultsDAO();
    Defaults defaults = defaultsDAO.getDefaults();

    Course course = courseDAO.findById(courseId);
    Student student = studentDAO.findById(studentId);
    CourseEnrolmentType courseEnrolmentType = courseEnrolmentTypeId == null ? defaults.getInitialCourseEnrolmentType() : enrolmentTypeDAO.findById(courseEnrolmentTypeId);
    CourseParticipationType participationType = participationTypeId == null ? defaults.getInitialCourseParticipationType() : participationTypeDAO.findById(participationTypeId);
    CourseOptionality cOptionality = null; 
    if (!StringUtils.isBlank(optionality))
      cOptionality = CourseOptionality.valueOf(optionality);

    Room room = null;
    BigDecimal lodgingFee = null;
    Currency lodgingFeeCurrency = null;
    String organization = null;
    String additionalInfo = null;
    
    CourseStudent courseStudent = courseStudentDAO.create(course, student, courseEnrolmentType, participationType, 
        enrolmentDate, lodging, cOptionality, null, organization, additionalInfo, room, lodgingFee, lodgingFeeCurrency, Boolean.FALSE);

    validateEntity(courseStudent);
    
    return EntityFactoryVault.buildFromDomainObject(courseStudent);
  }

  public void updateCourseStudent(@WebParam (name = "courseStudentId") Long courseStudentId, @WebParam (name = "courseEnrolmentTypeId") Long courseEnrolmentTypeId, @WebParam (name = "participationTypeId") Long participationTypeId,
      @WebParam (name = "enrolmentDate") Date enrolmentDate, @WebParam (name = "lodging") Boolean lodging, @WebParam (name = "optionality") String optionality) {

    CourseStudentDAO courseStudentDAO = DAOFactory.getInstance().getCourseStudentDAO();
    CourseParticipationTypeDAO participationTypeDAO = DAOFactory.getInstance().getCourseParticipationTypeDAO();
    CourseEnrolmentTypeDAO enrolmentTypeDAO = DAOFactory.getInstance().getCourseEnrolmentTypeDAO();

    CourseStudent courseStudent = courseStudentDAO.findById(courseStudentId);
    CourseEnrolmentType courseEnrolmentType = courseEnrolmentTypeId == null ? null : enrolmentTypeDAO.findById(courseEnrolmentTypeId);
    CourseParticipationType participationType = participationTypeId == null ? null : participationTypeDAO.findById(participationTypeId);
    CourseOptionality cOptionality = null; 
    if (!StringUtils.isBlank(optionality))
      cOptionality = CourseOptionality.valueOf(optionality);
    
    // TODO: student-parameter (?)
    courseStudentDAO.update(courseStudent, courseStudent.getStudent(), courseEnrolmentType, participationType, enrolmentDate, lodging, cOptionality);
    validateEntity(courseStudent);
  }

  public CourseStudentEntity getCourseStudentById(@WebParam (name = "courseStudentId") Long courseStudentId) {
    CourseStudentDAO courseStudentDAO = DAOFactory.getInstance().getCourseStudentDAO();
    return EntityFactoryVault.buildFromDomainObject(courseStudentDAO.findById(courseStudentId));
  }

  public CourseStudentEntity[] listCourseStudentsByCourse(@WebParam (name = "courseId") Long courseId) {
    CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();
    Course course = courseDAO.findById(courseId);
    CourseStudentDAO courseStudentDAO = DAOFactory.getInstance().getCourseStudentDAO();
    return (CourseStudentEntity[]) EntityFactoryVault.buildFromDomainObjects(courseStudentDAO.listByCourse(course));
  }

  public CourseStudentEntity[] listCourseStudentsByStudent(@WebParam (name = "studentId") Long studentId) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    CourseStudentDAO courseStudentDAO = DAOFactory.getInstance().getCourseStudentDAO();

    Student student = studentDAO.findById(studentId);
    return (CourseStudentEntity[]) EntityFactoryVault.buildFromDomainObjects(courseStudentDAO.listByStudent(student));
  }

  public CourseEntity getCourseById(@WebParam (name = "courseId") Long courseId) {
    CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();
    return EntityFactoryVault.buildFromDomainObject(courseDAO.findById(courseId));
  }

  public CourseEnrolmentTypeEntity createCourseEnrolmentType(@WebParam (name = "name") String name) {
    CourseEnrolmentTypeDAO courseEnrolmentTypeDAO = DAOFactory.getInstance().getCourseEnrolmentTypeDAO();
    CourseEnrolmentType courseEnrolmentType = courseEnrolmentTypeDAO.create(name);
    validateEntity(courseEnrolmentType);
    return EntityFactoryVault.buildFromDomainObject(courseEnrolmentType);
  }

  public CourseEnrolmentTypeEntity getCourseEnrolmentTypeById(@WebParam (name = "courseEnrolmentTypeId") Long courseEnrolmentTypeId) {
    CourseEnrolmentTypeDAO enrolmentTypeDAO = DAOFactory.getInstance().getCourseEnrolmentTypeDAO();
    return EntityFactoryVault.buildFromDomainObject(enrolmentTypeDAO.findById(courseEnrolmentTypeId));
  }

  public CourseParticipationTypeEntity createCourseParticipationType(@WebParam (name = "name") String name) {
    CourseParticipationTypeDAO participationTypeDAO = DAOFactory.getInstance().getCourseParticipationTypeDAO();
    CourseParticipationType courseParticipationType = participationTypeDAO.create(name);
    validateEntity(courseParticipationType);
    return EntityFactoryVault.buildFromDomainObject(courseParticipationType);
  }

  public CourseUserEntity createCourseUser(@WebParam (name = "courseId") Long courseId, @WebParam (name = "userId") Long userId, @WebParam (name = "courseUserRoleId") Long courseUserRoleId) {
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();
    CourseStaffMemberDAO courseStaffMemberDAO = DAOFactory.getInstance().getCourseStaffMemberDAO();
    CourseStaffMemberRoleDAO courseStaffMemberRoleDAO = DAOFactory.getInstance().getCourseStaffMemberRoleDAO();

    Course course = courseDAO.findById(courseId);
    StaffMember staffMember = staffMemberDAO.findById(userId);
    CourseStaffMemberRole role = courseStaffMemberRoleDAO.findById(courseUserRoleId);

    CourseStaffMember courseUser = courseStaffMemberDAO.create(course, staffMember, role);
    
    validateEntity(courseUser);

    return EntityFactoryVault.buildFromDomainObject(courseUser);
  }

  public CourseParticipationTypeEntity getCourseParticipationTypeById(@WebParam (name = "courseParticipationTypeId") Long courseParticipationTypeId) {
    CourseParticipationTypeDAO participationTypeDAO = DAOFactory.getInstance().getCourseParticipationTypeDAO();
    return EntityFactoryVault.buildFromDomainObject(participationTypeDAO.findById(courseParticipationTypeId));
  }

  public CourseComponentEntity[] listCourseComponents(@WebParam (name = "courseId") Long courseId) {
    CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();
    CourseComponentDAO componentDAO = DAOFactory.getInstance().getCourseComponentDAO();
    return (CourseComponentEntity[]) EntityFactoryVault.buildFromDomainObjects(componentDAO.listByCourse(courseDAO.findById(courseId)));
  }

  public CourseEnrolmentTypeEntity[] listCourseEnrolmentTypes() {
    CourseEnrolmentTypeDAO courseEnrolmentTypeDAO = DAOFactory.getInstance().getCourseEnrolmentTypeDAO();
    return (CourseEnrolmentTypeEntity[]) EntityFactoryVault.buildFromDomainObjects(courseEnrolmentTypeDAO.listAll());
  }

  public CourseParticipationTypeEntity[] listCourseParticipationTypes() {
    CourseParticipationTypeDAO participationTypeDAO = DAOFactory.getInstance().getCourseParticipationTypeDAO();
   
    List<CourseParticipationType> courseParticipationTypes = participationTypeDAO.listUnarchived();
    Collections.sort(courseParticipationTypes, new Comparator<CourseParticipationType>() {
      public int compare(CourseParticipationType o1, CourseParticipationType o2) {
        return o1.getIndexColumn() == null ? -1 : o2.getIndexColumn() == null ? 1 : o1.getIndexColumn().compareTo(o2.getIndexColumn());
      }
    });
    
    return (CourseParticipationTypeEntity[]) EntityFactoryVault.buildFromDomainObjects(courseParticipationTypes);
  }

  public CourseEntity[] listCoursesByCourseVariable(@WebParam (name = "key") String key, @WebParam (name = "value") String value) {
    CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();
    return (CourseEntity[]) EntityFactoryVault.buildFromDomainObjects(courseDAO.listByCourseVariable(key, value));
  }

  public String getCourseVariable(@WebParam (name = "courseId") Long courseId, @WebParam (name = "key") String key) {
    CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();
    CourseBaseVariableDAO variableDAO = DAOFactory.getInstance().getCourseBaseVariableDAO();
    return variableDAO.findByCourseAndVariableKey(courseDAO.findById(courseId), key);
  }

  public void setCourseVariable(@WebParam (name = "courseId") Long courseId, @WebParam (name = "key") String key, @WebParam (name = "value") String value) {
    CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();
    CourseBaseVariableDAO variableDAO = DAOFactory.getInstance().getCourseBaseVariableDAO();
    variableDAO.setCourseVariable(courseDAO.findById(courseId), key, value);
  }

  public void archiveCourse(@WebParam (name = "courseId") Long courseId) {
    CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();
    courseDAO.archive(courseDAO.findById(courseId));
  }

  public void unarchiveCourse(@WebParam (name = "courseId") Long courseId) {
    CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();
    courseDAO.unarchive(courseDAO.findById(courseId));
  }

  public void archiveCourseStudent(@WebParam (name = "courseId") Long courseId, @WebParam (name = "studentId") Long studentId) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();
    CourseStudentDAO courseStudentDAO = DAOFactory.getInstance().getCourseStudentDAO();

    Course course = courseDAO.findById(courseId);
    Student student = studentDAO.findById(studentId);
    CourseStudent courseStudent = courseStudentDAO.findByCourseAndStudent(course, student);
    courseStudentDAO.archive(courseStudent);
  }

  public void unarchiveCourseStudent(@WebParam (name = "courseId") Long courseId, @WebParam (name = "studentId") Long studentId) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();
    CourseStudentDAO courseStudentDAO = DAOFactory.getInstance().getCourseStudentDAO();

    Course course = courseDAO.findById(courseId);
    Student student = studentDAO.findById(studentId);
    CourseStudent courseStudent = courseStudentDAO.findByCourseAndStudent(course, student);
    courseDAO.unarchive(courseStudent);
  }

  public CourseStudentEntity getCourseStudentByCourseIdAndStudentId(@WebParam (name = "courseId") Long courseId, @WebParam (name = "studentId") Long studentId) {
    CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    CourseStudentDAO courseStudentDAO = DAOFactory.getInstance().getCourseStudentDAO();

    Course course = courseDAO.findById(courseId);
    Student student = studentDAO.findById(studentId);
    CourseStudent courseStudent = courseStudentDAO.findByCourseAndStudent(course, student);

    return EntityFactoryVault.buildFromDomainObject(courseStudent);
  }
  
  public CourseEntitySearchResult searchCourses(@WebParam (name = "resultsPerPage") Integer resultsPerPage, @WebParam (name = "page") Integer page, @WebParam (name = "name") String name, @WebParam (name = "tags") String tags, @WebParam (name = "nameExtension") String nameExtension, @WebParam (name = "description") String description, @WebParam (name = "courseStateId") Long courseStateId, @WebParam (name = "subjectId") Long subjectId, @WebParam (name = "timeFilterMode") String timeFilterMode, @WebParam (name = "timeframeStart") Date timeframeStart, @WebParam (name = "timeframeEnd") Date timeframeEnd) {
    CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();
    CourseStateDAO courseStateDAO = DAOFactory.getInstance().getCourseStateDAO();
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();

    CourseState courseState = courseStateId != null ? courseStateDAO.findById(courseStateId) : null;
    Subject subject = subjectId != null ? subjectDAO.findById(subjectId) : null;
    SearchTimeFilterMode tFilterMode = timeFilterMode != null ? SearchTimeFilterMode.valueOf(timeFilterMode) : null;
    
    SearchResult<Course> searchResult = courseDAO.searchCourses(resultsPerPage, page, name, tags, nameExtension, description, courseState, subject, tFilterMode, timeframeStart, timeframeEnd, true);
    
    return new CourseEntitySearchResult(searchResult.getPage(), searchResult.getPages(), searchResult.getTotalHitCount(), (CourseEntity[]) EntityFactoryVault.buildFromDomainObjects(searchResult.getResults()));
  }
  
  public CourseDescriptionCategoryEntity[] listCourseDescriptionCategories() {
    CourseDescriptionCategoryDAO descriptionCategoryDAO = DAOFactory.getInstance().getCourseDescriptionCategoryDAO();
    return (CourseDescriptionCategoryEntity[]) EntityFactoryVault.buildFromDomainObjects(descriptionCategoryDAO.listUnarchived());
  }

  public CourseDescriptionEntity[] listCourseDescriptionsByCourseId(@WebParam (name = "courseId") Long courseId) {
    CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();
    CourseBase courseBase = courseDAO.findById(courseId);
    CourseDescriptionDAO descriptionDAO = DAOFactory.getInstance().getCourseDescriptionDAO();
    return (CourseDescriptionEntity[]) EntityFactoryVault.buildFromDomainObjects(descriptionDAO.listByCourseBase(courseBase));
  }

  public CourseDescriptionEntity getCourseDescriptionByCourseIdAndCategoryId(@WebParam (name = "courseId") Long courseId, @WebParam (name = "categoryId") Long categoryId) {
    CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();
    CourseDescriptionDAO descriptionDAO = DAOFactory.getInstance().getCourseDescriptionDAO();
    
    CourseBase courseBase = courseDAO.findById(courseId);
    CourseDescriptionCategoryDAO descriptionCategoryDAO = DAOFactory.getInstance().getCourseDescriptionCategoryDAO();
    CourseDescriptionCategory category = descriptionCategoryDAO.findById(categoryId);
    
    return (CourseDescriptionEntity) EntityFactoryVault.buildFromDomainObject(descriptionDAO.findByCourseAndCategory(courseBase, category));
  }
  
  public void setCourseStudentVariable(@WebParam (name="courseStudentId") Long courseStudentId, @WebParam (name="key") String key, @WebParam (name="value") String value) {
    CourseStudentDAO courseStudentDAO = DAOFactory.getInstance().getCourseStudentDAO();
    CourseStudentVariableDAO courseStudentVariableDAO = DAOFactory.getInstance().getCourseStudentVariableDAO();
    CourseStudent courseStudent = courseStudentDAO.findById(courseStudentId);
    courseStudentVariableDAO.setCourseStudentVariable(courseStudent, key, value);
  }

  public String getCourseStudentVariable(@WebParam (name="courseStudentId") Long courseStudentId, @WebParam (name="key") String key) {
    CourseStudentDAO courseStudentDAO = DAOFactory.getInstance().getCourseStudentDAO();
    CourseStudentVariableDAO courseStudentVariableDAO = DAOFactory.getInstance().getCourseStudentVariableDAO();

    CourseStudent courseStudent = courseStudentDAO.findById(courseStudentId);
    return courseStudentVariableDAO.findByCourseStudentAndKey(courseStudent, key);
  }

}
