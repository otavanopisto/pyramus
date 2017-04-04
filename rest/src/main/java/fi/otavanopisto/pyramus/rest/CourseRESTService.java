package fi.otavanopisto.pyramus.rest;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

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

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.domainmodel.accommodation.Room;
import fi.otavanopisto.pyramus.domainmodel.base.BillingDetails;
import fi.otavanopisto.pyramus.domainmodel.base.CourseBase;
import fi.otavanopisto.pyramus.domainmodel.base.CourseBaseVariableKey;
import fi.otavanopisto.pyramus.domainmodel.base.CourseEducationType;
import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;
import fi.otavanopisto.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.base.VariableType;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseComponent;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseDescription;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseDescriptionCategory;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseParticipationType;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStaffMemberRole;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseState;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseType;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessmentRequest;
import fi.otavanopisto.pyramus.domainmodel.modules.Module;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.exception.DuplicateCourseStudentException;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit.Handling;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit.Style;
import fi.otavanopisto.pyramus.rest.controller.AssessmentController;
import fi.otavanopisto.pyramus.rest.controller.CommonController;
import fi.otavanopisto.pyramus.rest.controller.CourseController;
import fi.otavanopisto.pyramus.rest.controller.CurriculumController;
import fi.otavanopisto.pyramus.rest.controller.ModuleController;
import fi.otavanopisto.pyramus.rest.controller.StudentController;
import fi.otavanopisto.pyramus.rest.controller.UserController;
import fi.otavanopisto.pyramus.rest.controller.permissions.CommonPermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.CourseAssessmentPermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.CoursePermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.PersonPermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.UserPermissions;
import fi.otavanopisto.pyramus.rest.model.CourseEnrolmentType;
import fi.otavanopisto.pyramus.rest.security.RESTSecurity;
import fi.otavanopisto.pyramus.security.impl.SessionController;

@Path("/courses")
@Produces("application/json")
@Consumes("application/json")
@Stateful
@RequestScoped
public class CourseRESTService extends AbstractRESTService {
  
  @Inject
  private Logger logger;
  
  @Inject
  private RESTSecurity restSecurity;
  
  @Inject
  private CourseController courseController;

  @Inject
  private UserController userController;

  @Inject
  private StudentController studentController;
  
  @Inject
  private ModuleController moduleController;

  @Inject
  private CommonController commonController;

  @Inject
  private CurriculumController curriculumController;
  
  @Inject
  private SessionController sessionController;

  @Inject
  private ObjectFactory objectFactory;
  
  @Inject
  private AssessmentController assessmentController;

  @Path("/courses")
  @POST
  @RESTPermit (CoursePermissions.CREATE_COURSE)
  public Response createCourse(fi.otavanopisto.pyramus.rest.model.Course courseEntity) {
    if (courseEntity.getModuleId() == null) {
      return Response.status(Status.BAD_REQUEST).entity("moduleId is required").build();
    }

    if (courseEntity.getStateId() == null) {
      return Response.status(Status.BAD_REQUEST).entity("stateId is required").build();
    }

    Module module = moduleController.findModuleById(courseEntity.getModuleId());
    String name = courseEntity.getName();
    String nameExtension = courseEntity.getNameExtension();
    CourseState state = courseController.findCourseStateById(courseEntity.getStateId());
    CourseType type = courseEntity.getTypeId() != null ? courseController.findCourseTypeById(courseEntity.getTypeId()) : null;

    Subject subject = null;
    if (courseEntity.getSubjectId() != null) {
      subject = commonController.findSubjectById(courseEntity.getSubjectId());
      if (subject == null) {
        return Response.status(Status.NOT_FOUND).entity("specified subject does not exist").build();
      }
    }
    
    Integer courseNumber = courseEntity.getCourseNumber();
    OffsetDateTime beginDate = courseEntity.getBeginDate();
    OffsetDateTime endDate = courseEntity.getEndDate();
    Double courseLength = courseEntity.getLength();
    EducationalTimeUnit courseLengthTimeUnit = null;
    
    if (courseLength != null) {
      if (courseEntity.getLengthUnitId() == null) {
        return Response.status(Status.BAD_REQUEST).entity("length unit is missing").build();
      }
      
      courseLengthTimeUnit = commonController.findEducationalTimeUnitById(courseEntity.getLengthUnitId());
      if (courseLengthTimeUnit == null) {
        return Response.status(Status.BAD_REQUEST).entity("length unit is invalid").build();
      }
    }
    
    Double distanceTeachingDays = courseEntity.getDistanceTeachingDays();
    Double localTeachingDays = courseEntity.getLocalTeachingDays();
    Double teachingHours = courseEntity.getTeachingHours();
    Double distanceTeachingHours = courseEntity.getDistanceTeachingHours();
    Double planningHours = courseEntity.getPlanningHours();
    Double assessingHours = courseEntity.getAssessingHours();
    String description = courseEntity.getDescription();
    Long maxParticipantCount = courseEntity.getMaxParticipantCount();
    Date enrolmentTimeEnd = toDate(courseEntity.getEnrolmentTimeEnd());
    BigDecimal courseFee = null;
    Currency courseFeeCurrency = null;
    
    User loggedUser = sessionController.getUser();
    
    Course course = courseController.createCourse(module, name, nameExtension, state, type, subject, courseNumber, 
        toDate(beginDate), toDate(endDate), courseLength, courseLengthTimeUnit, distanceTeachingDays, localTeachingDays, 
        teachingHours, distanceTeachingHours, planningHours, assessingHours, description, maxParticipantCount, 
        courseFee, courseFeeCurrency, enrolmentTimeEnd, loggedUser);
    
    if (courseEntity.getTags() != null) {
      for (String tag : courseEntity.getTags()) {
        courseController.createCourseTag(course, tag);
      }
    }
    
    if (CollectionUtils.isNotEmpty(courseEntity.getCurriculumIds())) {
      Set<Curriculum> curriculums = new HashSet<Curriculum>();
      for (Long curriculumId : courseEntity.getCurriculumIds()) {
        Curriculum curriculum = curriculumId != null ? curriculumController.findCurriculumById(curriculumId) : null;
        if (curriculum != null)
          curriculums.add(curriculum);
      }
      course = courseController.updateCourseCurriculums(course, curriculums);
    }
    
    if (courseEntity.getVariables() != null) {
      course = courseController.updateCourseVariables(course, courseEntity.getVariables());
    }
    
    return Response.ok().entity(objectFactory.createModel(course)).build();
  }
  
  @Path("/courses/{ID:[0-9]*}")
  @GET
  @RESTPermit (CoursePermissions.FIND_COURSE)
  public Response getCourse(@PathParam("ID") Long id, @Context Request request) {
    Course course = courseController.findCourseById(id);
    if (course != null) {
      if (course.getArchived()) {
        return Response.status(Status.NOT_FOUND).build();  
      }
      
      EntityTag tag = new EntityTag(DigestUtils.md5Hex(String.valueOf(course.getLastModified().getTime())));
      
      ResponseBuilder builder = request.evaluatePreconditions(tag);
      if (builder != null) {
        return builder.build();
      }
      
      CacheControl cacheControl = new CacheControl();
      cacheControl.setMustRevalidate(true);
      
      return Response.ok(objectFactory.createModel(course))
          .cacheControl(cacheControl)
          .tag(tag)
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/courses")
  @GET
  @RESTPermit (CoursePermissions.LIST_COURSES)
  public Response listCourses(@QueryParam ("firstResult") Integer firstResult, @QueryParam ("maxResults") Integer maxResults, @DefaultValue("true") @QueryParam("filterArchived") boolean filterArchived) {
    List<Course> courses;
    
    if (filterArchived) {
      courses = courseController.listUnarchivedCourses(firstResult, maxResults);
    } else {
      courses = courseController.listCourses(firstResult, maxResults);
    }
    
    if (!courses.isEmpty()) {
      return Response.ok().entity(objectFactory.createModel(courses)).build();
    } else {
      return Response.status(Status.NO_CONTENT).build();
    }
  }
  
  @Path("/courses/{ID:[0-9]*}")
  @PUT
  @RESTPermit (CoursePermissions.UPDATE_COURSE)
  public Response updateCourse(@PathParam("ID") Long id, fi.otavanopisto.pyramus.rest.model.Course courseEntity) {
    Course course = courseController.findCourseById(id);
    if (course == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (course.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (!course.getId().equals(courseEntity.getId())) {
      return Response.status(Status.BAD_REQUEST).entity("Cannot change entity id in update request").build();
    }
    
    String name = courseEntity.getName();
    String nameExtension = courseEntity.getNameExtension();
    CourseState state = courseController.findCourseStateById(courseEntity.getStateId());
    CourseType type = courseEntity.getTypeId() != null ? courseController.findCourseTypeById(courseEntity.getTypeId()) : null;

    Subject subject = null;
    if (courseEntity.getSubjectId() != null) {
      subject = commonController.findSubjectById(courseEntity.getSubjectId());
      if (subject == null) {
        return Response.status(Status.NOT_FOUND).entity("specified subject does not exist").build();
      }
    }
    
    Integer courseNumber = courseEntity.getCourseNumber();
    OffsetDateTime beginDate = courseEntity.getBeginDate();
    OffsetDateTime endDate = courseEntity.getEndDate();
    Double courseLength = courseEntity.getLength();
    EducationalTimeUnit courseLengthTimeUnit = null;
    
    if (courseLength != null) {
      if (courseEntity.getLengthUnitId() == null) {
        return Response.status(Status.BAD_REQUEST).entity("length unit is missing").build();
      }
      
      courseLengthTimeUnit = commonController.findEducationalTimeUnitById(courseEntity.getLengthUnitId());
      if (courseLengthTimeUnit == null) {
        return Response.status(Status.BAD_REQUEST).entity("length unit is invalid").build();
      }
    }
    
    Double distanceTeachingDays = courseEntity.getDistanceTeachingDays();
    Double localTeachingDays = courseEntity.getLocalTeachingDays();
    Double teachingHours = courseEntity.getTeachingHours();
    Double distanceTeachingHours = courseEntity.getDistanceTeachingHours();
    Double planningHours = courseEntity.getPlanningHours();
    Double assessingHours = courseEntity.getAssessingHours();
    String description = courseEntity.getDescription();
    Long maxParticipantCount = courseEntity.getMaxParticipantCount();
    Date enrolmentTimeEnd = toDate(courseEntity.getEnrolmentTimeEnd());
    User loggedUser = sessionController.getUser();
    
    Course updatedCourse = courseController.updateCourse(course, name, nameExtension, state, type, subject, courseNumber, toDate(beginDate), toDate(endDate), courseLength,
        courseLengthTimeUnit, distanceTeachingDays, localTeachingDays, teachingHours, distanceTeachingHours, planningHours, assessingHours, description,
        maxParticipantCount, enrolmentTimeEnd, loggedUser);
    
    Set<Curriculum> curriculums = new HashSet<Curriculum>();
    if (CollectionUtils.isNotEmpty(courseEntity.getCurriculumIds())) {
      for (Long curriculumId : courseEntity.getCurriculumIds()) {
        Curriculum curriculum = curriculumId != null ? curriculumController.findCurriculumById(curriculumId) : null;
        if (curriculum != null)
          curriculums.add(curriculum);
      }
    }
    updatedCourse = courseController.updateCourseCurriculums(updatedCourse, curriculums);
    
    updatedCourse = courseController.updateCourseTags(updatedCourse, courseEntity.getTags() == null ? new ArrayList<String>() : courseEntity.getTags());
    updatedCourse = courseController.updateCourseVariables(updatedCourse, courseEntity.getVariables() == null ? new HashMap<String, String>() : courseEntity.getVariables());
    
    return Response.ok().entity(objectFactory.createModel(updatedCourse)).build();
  }
  
  @Path("/courses/{ID:[0-9]*}")
  @DELETE
  @RESTPermit (CoursePermissions.DELETE_COURSE)
  public Response deleteCourse(@PathParam("ID") Long id, @DefaultValue ("false") @QueryParam ("permanent") Boolean permanent) {
    User loggedUser = sessionController.getUser();
    
    Course course = courseController.findCourseById(id);
    if (course != null) {
      if (permanent) {
        courseController.deleteCourse(course); 
      } else {
        courseController.archiveCourse(course, loggedUser);
      }
      
      return Response.status(Status.NO_CONTENT).build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/courses/{COURSEID:[0-9]*}/components")
  @POST
  @RESTPermit (CoursePermissions.CREATE_COURSECOMPONENT)
  public Response createCourseComponent(@PathParam("COURSEID") Long courseId, fi.otavanopisto.pyramus.rest.model.CourseComponent courseComponent) {
    Course course = courseController.findCourseById(courseId);
    if (course == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (StringUtils.isBlank(courseComponent.getName())) {
      return Response.status(Status.BAD_REQUEST).entity("name is required").build();
    }
    
    EducationalTimeUnit componentLengthTimeUnit = null;
      
    if (courseComponent.getLength() != null) {
      if (courseComponent.getLengthUnitId() == null) {
        return Response.status(Status.BAD_REQUEST).entity("lengthUnitId is required when length is defined").build();
      }
      
      componentLengthTimeUnit = commonController.findEducationalTimeUnitById(courseComponent.getLengthUnitId());
      if (componentLengthTimeUnit == null) {
        return Response.status(Status.BAD_REQUEST).entity("lengthUnitId is required when length is defined").build();
      }
    }
    
    return Response
      .status(Status.OK)
      .entity(objectFactory.createModel(courseController.createCourseComponent(course, courseComponent.getLength(), componentLengthTimeUnit, courseComponent.getName(), courseComponent.getDescription())))
      .build();
  }

  @Path("/courses/{COURSEID:[0-9]*}/descriptions")
  @POST
  @RESTPermit (CoursePermissions.CREATE_COURSEDESCRIPTION)
  public Response createCourseDescription(
      @PathParam("COURSEID") Long courseId,
      fi.otavanopisto.pyramus.rest.model.CourseDescription dto
  ) {
    Course course = courseController.findCourseById(courseId);
    if (course == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    CourseDescriptionCategory category = courseController.findCourseDescriptionCategoryById(
        dto.getCourseDescriptionCategoryId()
    );
    CourseDescription cd = courseController.createCourseDescription(course, category, dto.getDescription());
    
    return Response
      .status(Status.OK)
      .entity(
          new fi.otavanopisto.pyramus.rest.model.CourseDescription(
              cd.getId(),
              courseId,
              category.getId(),
              cd.getDescription()))
      .build();
  }

  @Path("/courses/{COURSEID:[0-9]*}/descriptions")
  @GET
  @RESTPermit (CoursePermissions.LIST_COURSEDESCRIPTIONS)
  public Response listCourseDescriptions(@PathParam("COURSEID") Long courseId) {
    Course course = courseController.findCourseById(courseId);
    if (course == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    
    
    return Response
      .status(Status.OK)
      .build();
  }

  @Path("/courses/{ID:[0-9]*}/assessmentsRequests/")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response listStudentAssessmentRequests(@PathParam("ID") Long courseId, @QueryParam("activeStudents") Boolean activeStudents) {
    Course course = courseController.findCourseById(courseId);

    if (course == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (course.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (!restSecurity.hasPermission(new String[] { CourseAssessmentPermissions.LIST_COURSEASSESSMENTREQUESTS }, course, Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }

    List<CourseAssessmentRequest> assessmentRequests = assessmentController.listCourseAssessmentRequestsByCourse(course);
        
    if (activeStudents != null) {
      for (int i = assessmentRequests.size() - 1; i >= 0; i--) {
        CourseAssessmentRequest assessmentRequest = assessmentRequests.get(i);
        boolean remove = true;
        
        if (assessmentRequest != null) {
          CourseStudent courseStudent = assessmentRequest.getCourseStudent();
          if (courseStudent != null) {
            remove = !activeStudents.equals(isActiveStudent(courseStudent));
          }
        }
        
        if (remove) {
          assessmentRequests.remove(i);
        }
      }
    }
    
    return Response.ok(objectFactory.createModel(assessmentRequests)).build();
  }
  
  @Path("/courses/{ID:[0-9]*}/components")
  @GET
  @RESTPermit (CoursePermissions.LIST_COURSECOMPONENTS)
  public Response listCourseComponents(@PathParam("ID") Long courseId) {
    Course course = courseController.findCourseById(courseId);
    if (course == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    List<CourseComponent> components = courseController.findCourseComponentsByCourse(course);
    if (components.isEmpty()) {
      return Response.status(Status.NO_CONTENT).build();
    }
    
    return Response.status(Status.OK).entity(objectFactory.createModel(components)).build();
  }

  @Path("/courses/{CID:[0-9]*}/components/{ID:[0-9]*}")
  @GET
  @RESTPermit (CoursePermissions.FIND_COURSECOMPONENT)
  public Response findCourseComponentById(@PathParam("CID") Long courseId, @PathParam("ID") Long componentId) {
    Course course = courseController.findCourseById(courseId);
    if (course == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    CourseComponent component = courseController.findCourseComponentById(componentId);
    if (component == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (component.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (!component.getCourse().getId().equals(courseId)) {
      return Response.status(Status.NOT_FOUND).build();
    }

    return Response.status(Status.OK).entity(objectFactory.createModel(component)).build();
  }
  
  @Path("/courses/{COURSEID:[0-9]*}/components/{COMPONENTID:[0-9]*}")
  @PUT
  @RESTPermit (CoursePermissions.UPDATE_COURSECOMPONENT)
  public Response updateCourseComponent(@PathParam("COURSEID") Long courseId, @PathParam("COMPONENTID") Long courseComponentId, fi.otavanopisto.pyramus.rest.model.CourseComponent courseComponentEntity) {
    if (courseComponentEntity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    CourseComponent courseComponent = courseController.findCourseComponentById(courseComponentId);
    if (courseComponent == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (!courseComponent.getId().equals(courseComponentEntity.getId())) {
      return Response.status(Status.BAD_REQUEST).entity(String.format("Cannot update id (%d !=%d)", courseComponent.getId(), courseComponentEntity.getId())).build();
    }
    
    Course course = courseController.findCourseById(courseId);
    if (course == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (StringUtils.isBlank(courseComponent.getName())) {
      return Response.status(Status.BAD_REQUEST).entity("name is required").build();
    }
    
    EducationalTimeUnit componentLengthTimeUnit = null;
      
    if (courseComponentEntity.getLength() != null) {
      if (courseComponentEntity.getLengthUnitId() == null) {
        return Response.status(Status.BAD_REQUEST).entity("lengthUnitId is required when length is defined").build();
      }
      
      componentLengthTimeUnit = commonController.findEducationalTimeUnitById(courseComponentEntity.getLengthUnitId());
      if (componentLengthTimeUnit == null) {
        return Response.status(Status.BAD_REQUEST).entity("lengthUnitId is required when length is defined").build();
      }
    }
    
    return Response
      .status(Status.OK)
      .entity(objectFactory.createModel(courseController.updateCourseComponent(courseComponent, courseComponentEntity.getLength(), componentLengthTimeUnit, courseComponentEntity.getName(), courseComponentEntity.getDescription())))
      .build();
  }
  
  @Path("/courses/{COURSEID:[0-9]*}/components/{COMPONENTID:[0-9]*}")
  @DELETE
  @RESTPermit (CoursePermissions.DELETE_COURSECOMPONENT)
  public Response deleteCourseComponent(@PathParam("COURSEID") Long courseId, @PathParam("COMPONENTID") Long componentId, @DefaultValue ("false") @QueryParam ("permanent") Boolean permanent) {
    if (courseId == null || componentId == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    Course course = courseController.findCourseById(courseId);
    if (course == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    CourseComponent courseComponent = courseController.findCourseComponentById(componentId);
    if (courseComponent == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (!courseComponent.getCourse().getId().equals(course.getId())) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (permanent) {
      courseController.deleteCourseComponent(courseComponent);
    } else {
      courseController.archiveCourseComponent(courseComponent, sessionController.getUser());
    }
    
    return Response.status(Status.NO_CONTENT).build();
  }
  
  @Path("/courses/{COURSEID:[0-9]*}/students")
  @POST
  @RESTPermit (handling = Handling.INLINE)
//  @RESTPermit (CoursePermissions.CREATE_COURSESTUDENT)
  public Response createCourseStudent(@PathParam("COURSEID") Long courseId, fi.otavanopisto.pyramus.rest.model.CourseStudent entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).entity("Request payload missing").build(); 
    }
    
    if (entity.getStudentId() == null) {
      return Response.status(Status.BAD_REQUEST).entity("studentId is missing").build(); 
    }
    
    if (entity.getLodging() == null) {
      return Response.status(Status.BAD_REQUEST).entity("lodging is missing").build(); 
    }
    
    if (entity.getEnrolmentTime() == null) {
      return Response.status(Status.BAD_REQUEST).entity("enrolmentTime is missing").build(); 
    }
    
    if (!restSecurity.hasPermission(new String[] { CoursePermissions.CREATE_COURSESTUDENT, UserPermissions.USER_OWNER }, entity, Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }

    Course course = courseController.findCourseById(courseId);
    if (course == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    Student student = studentController.findStudentById(entity.getStudentId());
    if (student == null) {
      return Response.status(Status.BAD_REQUEST).entity("could not find the student #" + entity.getStudentId()).build();
    }

    if (courseController.findCourseStudentByCourseAndStudent(course, student) != null) {
      return Response.status(Status.BAD_REQUEST).entity("student #" + entity.getStudentId() + " is already on course #" + courseId).build();
    }

    BillingDetails billingDetails = null;
    if (entity.getBillingDetailsId() != null) {
      billingDetails = commonController.findBillingDetailsById(entity.getBillingDetailsId());
      if (billingDetails == null) {
        return Response.status(Status.BAD_REQUEST).entity("could not find billingDetails #" + entity.getBillingDetailsId()).build();
      }
    }
    
    fi.otavanopisto.pyramus.domainmodel.courses.CourseEnrolmentType enrolmentType;
    if (entity.getEnrolmentTypeId() != null) {
      enrolmentType = courseController.findCourseEnrolmentTypeById(entity.getEnrolmentTypeId());
      if (enrolmentType == null) {
        return Response.status(Status.BAD_REQUEST).entity("could not find enrolmentType #" + entity.getEnrolmentTypeId()).build();
      }
    } else {
      enrolmentType = courseController.getDefaultCourseEnrolmentType();
    }
    
    fi.otavanopisto.pyramus.domainmodel.base.CourseOptionality optionality = entity.getOptionality() != null ? fi.otavanopisto.pyramus.domainmodel.base.CourseOptionality.valueOf(entity.getOptionality().name()) : null;
    fi.otavanopisto.pyramus.domainmodel.courses.CourseParticipationType participantionType;
        
    if (entity.getParticipationTypeId() != null) {
      participantionType = courseController.findCourseParticipationTypeById(entity.getParticipationTypeId());
      if (participantionType == null) {
        return Response.status(Status.BAD_REQUEST).entity("could not find participationType #" + entity.getParticipationTypeId()).build();
      }
    } else {
      participantionType = courseController.getDefaultCourseParticipationType();
    }
    
    // TODO: Add support for room, organization, additionalInfo and lodging fee 
    String organization = null;
    String additionalInfo = null;
    Room room = null;
    BigDecimal lodgingFee = null;
    Currency lodgingFeeCurrency = null;

    try {
      return Response.status(Status.OK).entity(
          objectFactory.createModel(courseController.createCourseStudent(course, student, enrolmentType, participantionType,  
              toDate(entity.getEnrolmentTime()), entity.getLodging(), optionality, billingDetails, lodgingFee, lodgingFeeCurrency, 
              organization, additionalInfo, room)))
        .build();
    } catch (DuplicateCourseStudentException dcse) {
      logger.log(Level.SEVERE, "Attempt to add CourseStudent when it already exists (student=" + 
          entity.getStudentId() + ", course=" + courseId + ".", dcse);
      
      return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Student is already member of the course.").build();
    }
  }

  @Path("/courses/{ID:[0-9]*}/students")
  @GET
  @RESTPermit (handling = Handling.INLINE)
  public Response listCourseStudents(@PathParam("ID") Long courseId,
      @QueryParam("participationTypes") String participationTypes,
      @QueryParam("activeStudents") Boolean activeStudents,      
      @QueryParam("studentId") Long studentId,
      @DefaultValue("true") @QueryParam("filterArchived") boolean filterArchived) {
    
    if (!restSecurity.hasPermission(new String[] { CoursePermissions.LIST_COURSESTUDENTS } )) {
      if (studentId == null) {
        return Response.status(Status.FORBIDDEN).build();
      } else {
        Student student = DAOFactory.getInstance().getStudentDAO().findById(studentId);
        if (student == null) {
          return Response.status(Status.BAD_REQUEST).entity(String.format("student %d could not be found", studentId)).build();
        }
        
        if (!restSecurity.hasPermission(new String[] { PersonPermissions.PERSON_OWNER }, student.getPerson() )) {
          return Response.status(Status.FORBIDDEN).build();
        }
      }
    }

    Course course = courseController.findCourseById(courseId);
    if (course == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    List<CourseParticipationType> courseParticipationTypes = new ArrayList<>();
    if (StringUtils.isNotEmpty(participationTypes)) {
      String[] typeArray = participationTypes.split(",");
      for (int i = 0; i < typeArray.length; i++) {
        CourseParticipationType participationType = courseController.findCourseParticipationTypeById(Long.valueOf(typeArray[i]));
        if (participationType != null) {
          courseParticipationTypes.add(participationType);
        }
        // TODO error logging if participation type not found
      }
    }
    
    List<fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent> students;
    
    if (filterArchived) {
      students = courseParticipationTypes.isEmpty()
        ? courseController.listCourseStudentsByCourse(course)
        : courseController.listCourseStudentsByCourseAndParticipationTypes(course, courseParticipationTypes);
    } else {
      students = courseParticipationTypes.isEmpty()
          ? courseController.listCourseStudentsByCourseIncludeArchived(course)
          : courseController.listCourseStudentsByCourseAndParticipationTypesIncludeArchived(course, courseParticipationTypes);
    }
    
    if (studentId != null) {
      for (int i = students.size() - 1; i >= 0; i--) {
        if (!studentId.equals(students.get(i).getStudent().getId())) {
          students.remove(i);
        }
      }
    }
        
    if (students.isEmpty()) {
      return Response.status(Status.NO_CONTENT).build();
    }
    
    if (activeStudents != null) {
      for (int i = students.size() - 1; i >= 0; i--) {
        if (!activeStudents.equals(isActiveStudent(students.get(i)))) {
          students.remove(i);
        }
      }
    }
    
    return Response.status(Status.OK).entity(objectFactory.createModel(students)).build();
  }
  
  private boolean isActiveStudent(CourseStudent courseStudent) {
    Student student = courseStudent.getStudent();

    Date studyStartDate = student.getStudyStartDate();
    Date studyEndDate = student.getStudyEndDate();
    
    if (studyStartDate == null && studyEndDate == null) {
      // It's a never ending study programme
      return true;
    }

    boolean hasStarted = studyStartDate != null && studyStartDate.before(new Date());
    boolean hasFinished = studyEndDate != null && studyEndDate.before(new Date());
    
    return hasStarted && !hasFinished;
  }

  @Path("/courses/{CID:[0-9]*}/students/{ID:[0-9]*}")
  @GET
  @RESTPermit (handling = Handling.INLINE)
  public Response findCourseStudentById(@PathParam("CID") Long courseId, @PathParam("ID") Long id) {
    Course course = courseController.findCourseById(courseId);
    if (course == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent courseStudent = courseController.findCourseStudentById(id);
    if (courseStudent == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (!courseStudent.getCourse().getId().equals(courseId)) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (courseStudent.getArchived() || courseStudent.getStudent().getArchived() || courseStudent.getCourse().getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (!restSecurity.hasPermission(new String[] { CoursePermissions.FIND_COURSESTUDENT, PersonPermissions.PERSON_OWNER }, courseStudent.getStudent().getPerson(), Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }

    return Response.status(Status.OK).entity(objectFactory.createModel(courseStudent)).build();
  }
  
  @Path("/courses/{COURSEID:[0-9]*}/students/{ID:[0-9]*}")
  @PUT
  @RESTPermit (handling = Handling.INLINE)
  public Response updateCourseStudent(@PathParam("COURSEID") Long courseId, @PathParam("ID") Long id, fi.otavanopisto.pyramus.rest.model.CourseStudent entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build(); 
    }
    
    if (entity.getStudentId() == null) {
      return Response.status(Status.BAD_REQUEST).build(); 
    }
    
    if (entity.getEnrolmentTime() == null) {
      return Response.status(Status.BAD_REQUEST).build(); 
    }
    
    if (entity.getParticipationTypeId() == null) {
      return Response.status(Status.BAD_REQUEST).build(); 
    }
    
    if (entity.getLodging() == null) {
      return Response.status(Status.BAD_REQUEST).build(); 
    }

    CourseStudent courseStudent = courseController.findCourseStudentById(id);
    if (courseStudent == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    Course course = courseController.findCourseById(courseId);
    if (course == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    Student student = studentController.findStudentById(entity.getStudentId());
    if (student == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    if (!restSecurity.hasPermission(new String[] { CoursePermissions.UPDATE_COURSESTUDENT, PersonPermissions.PERSON_OWNER }, student.getPerson(), Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }

    if (!courseStudent.getCourse().getId().equals(course.getId())) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (!courseStudent.getStudent().getId().equals(student.getId())) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    BillingDetails billingDetails = null;
    if (entity.getBillingDetailsId() != null) {
      billingDetails = commonController.findBillingDetailsById(entity.getBillingDetailsId());
      if (billingDetails == null) {
        return Response.status(Status.BAD_REQUEST).build();
      }
    }
    
    fi.otavanopisto.pyramus.domainmodel.courses.CourseEnrolmentType courseEnrolmentType = null;
    if (entity.getEnrolmentTypeId() != null) {
      courseEnrolmentType = courseController.findCourseEnrolmentTypeById(entity.getEnrolmentTypeId());
      if (courseEnrolmentType == null) {
        return Response.status(Status.BAD_REQUEST).build();
      }
    }
    
    fi.otavanopisto.pyramus.domainmodel.base.CourseOptionality optionality = entity.getOptionality() != null ? fi.otavanopisto.pyramus.domainmodel.base.CourseOptionality.valueOf(entity.getOptionality().name()) : null;
    
    fi.otavanopisto.pyramus.domainmodel.courses.CourseParticipationType courseParticipantionType = courseController.findCourseParticipationTypeById(entity.getParticipationTypeId());
    if (courseParticipantionType == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    return Response.status(Status.OK).entity(objectFactory.createModel(
      courseController.updateCourseStudent(courseStudent, entity.getLodging(), billingDetails, courseEnrolmentType, toDate(entity.getEnrolmentTime()), optionality, courseParticipantionType))
    ).build();
  }

  @Path("/courses/{COURSEID:[0-9]*}/students/{ID:[0-9]*}")
  @DELETE
  @RESTPermit (CoursePermissions.DELETE_COURSESTUDENT)
  public Response deleteCourseStudent(@PathParam("COURSEID") Long courseId, @PathParam("ID") Long id, @DefaultValue ("false") @QueryParam ("permanent") Boolean permanent) {
    Course course = courseController.findCourseById(courseId);
    if (course == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent courseStudent = courseController.findCourseStudentById(id);
    if (courseStudent == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (!courseStudent.getCourse().getId().equals(courseId)) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (permanent) {
      courseController.deleteCourseStudent(courseStudent);
    } else {
      courseController.archiveCourseStudent(courseStudent, sessionController.getUser());
    }
    
    return Response.status(Status.NO_CONTENT).build();
  }
  
  @Path("/courses/{COURSEID:[0-9]*}/staffMembers")
  @POST
  @RESTPermit (CoursePermissions.CREATE_COURSESTAFFMEMBER)
  public Response createCourseStaffMember(@PathParam("COURSEID") Long courseId, fi.otavanopisto.pyramus.rest.model.CourseStaffMember entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build(); 
    }
    
    if (entity.getRoleId() == null) {
      return Response.status(Status.BAD_REQUEST).build(); 
    }
    
    if (entity.getStaffMemberId() == null) {
      return Response.status(Status.BAD_REQUEST).build(); 
    }
    
    Course course = courseController.findCourseById(courseId);
    if (course == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    StaffMember staffMember = userController.findStaffMemberById(entity.getStaffMemberId());
    if (staffMember == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    CourseStaffMemberRole role = courseController.findStaffMemberRoleById(entity.getRoleId());
    if (role == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    return Response.status(Status.OK)
      .entity(objectFactory.createModel(courseController.createStaffMember(course, staffMember, role)))
      .build();
  }

  @Path("/courses/{ID:[0-9]*}/staffMembers")
  @GET
  @RESTPermit (CoursePermissions.LIST_COURSESTAFFMEMBERS)
  public Response listCourseStaffMembers(@PathParam("ID") Long courseId) {
    Course course = courseController.findCourseById(courseId);
    if (course == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    List<fi.otavanopisto.pyramus.domainmodel.courses.CourseStaffMember> members = courseController.listStaffMembersByCourse(course);
    if (members.isEmpty()) {
      return Response.status(Status.NO_CONTENT).build();
    }
    
    return Response.status(Status.OK).entity(objectFactory.createModel(members)).build();
  }

  @Path("/courses/{CID:[0-9]*}/staffMembers/{ID:[0-9]*}")
  @GET
  @RESTPermit (CoursePermissions.FIND_COURSESTAFFMEMBER)
  public Response findCourseStaffMemberById(@PathParam("CID") Long courseId, @PathParam("ID") Long id) {
    Course course = courseController.findCourseById(courseId);
    if (course == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    fi.otavanopisto.pyramus.domainmodel.courses.CourseStaffMember courseStaffMember = courseController.findStaffMemberById(id);
    if (courseStaffMember == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (!courseStaffMember.getCourse().getId().equals(courseId)) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if ((courseStaffMember.getStaffMember() == null) || (courseStaffMember.getStaffMember().getArchived())) {
      return Response.status(Status.NOT_FOUND).build();
    }

    return Response.status(Status.OK).entity(objectFactory.createModel(courseStaffMember)).build();
  }

  @Path("/courses/{CID:[0-9]*}/educationTypes")
  @GET
  @RESTPermit (CoursePermissions.LIST_COURSEEDUCATIONTYPES)
  public Response listCourseEducationTypes(@PathParam("CID") Long courseId) {
    Course course = courseController.findCourseById(courseId);
    if (course == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    return Response.status(Status.OK).entity(objectFactory.createModel(course.getCourseEducationTypes())).build();
  }

  @Path("/courses/{CID:[0-9]*}/educationTypes/{EID:[0-9]*}/educationSubtypes")
  @GET
  @RESTPermit (CoursePermissions.LIST_COURSEEDUCATIONSUBTYPES)
  public Response listCourseEducationSubtypes(
      @PathParam("CID") Long courseId,
      @PathParam("EID") Long courseEducationTypeId) {
    Course course = courseController.findCourseById(courseId);
    if (course == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    CourseEducationType cedTypeWithRequestedId = courseController.findCourseEducationTypeById(courseEducationTypeId);
    
    if (cedTypeWithRequestedId == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    CourseEducationType cedTypeAssociatedToCourse = course.getCourseEducationTypeByEducationTypeId(cedTypeWithRequestedId.getEducationType().getId());
    
    if (cedTypeAssociatedToCourse == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    return Response.status(Status.OK).entity(objectFactory.createModel(cedTypeWithRequestedId.getCourseEducationSubtypes())).build();
  }
  
  @Path("/courses/{COURSEID:[0-9]*}/staffMembers/{ID:[0-9]*}")
  @PUT
  @RESTPermit (CoursePermissions.UPDATE_COURSESTAFFMEMBER)
  public Response updateCourseStaffMember(@PathParam("COURSEID") Long courseId, @PathParam("ID") Long id, fi.otavanopisto.pyramus.rest.model.CourseStaffMember entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    if (entity.getRoleId() == null) {
      return Response.status(Status.BAD_REQUEST).build(); 
    }
    
    if (entity.getStaffMemberId() == null) {
      return Response.status(Status.BAD_REQUEST).build(); 
    }
    
    Course course = courseController.findCourseById(courseId);
    if (course == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    fi.otavanopisto.pyramus.domainmodel.courses.CourseStaffMember staffMember = courseController.findStaffMemberById(id);
    if (staffMember == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (!staffMember.getCourse().getId().equals(courseId)) {
      return Response.status(Status.NOT_FOUND).build();
    }

    CourseStaffMemberRole role = courseController.findStaffMemberRoleById(entity.getRoleId());
    if (role == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    return Response
      .status(Status.OK)
      .entity(objectFactory.createModel(courseController.updateStaffMemberRole(staffMember, role)))
      .build();
  }

  @Path("/courses/{COURSEID:[0-9]*}/staffMembers/{ID:[0-9]*}")
  @DELETE
  @RESTPermit (CoursePermissions.DELETE_COURSESTAFFMEMBER)
  public Response deleteCourseStaffMember(@PathParam("COURSEID") Long courseId, @PathParam("ID") Long id) {
    Course course = courseController.findCourseById(courseId);
    if (course == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    fi.otavanopisto.pyramus.domainmodel.courses.CourseStaffMember staffMember = courseController.findStaffMemberById(id);
    if (staffMember == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (!staffMember.getCourse().getId().equals(courseId)) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    courseController.deleteStaffMember(staffMember);
    
    return Response.status(Status.NO_CONTENT).build();
  }
  
  @Path("/courseStates")
  @POST
  @RESTPermit (CoursePermissions.CREATE_COURSESTATE)
  public Response createCourseState(fi.otavanopisto.pyramus.rest.model.CourseState entity) {
    if (StringUtils.isBlank(entity.getName())) {
      return Response.status(Status.BAD_REQUEST).entity("name is required").build();
    }
    
    return Response
      .status(Status.OK)
      .entity(objectFactory.createModel(courseController.createCourseState(entity.getName())))
      .build();
  }
  
  @Path("/courseStates")
  @GET
  @RESTPermit (CoursePermissions.LIST_COURSESTATES)
  public Response listCourseStates(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<CourseState> courseStates;
    
    if (filterArchived) {
      courseStates = courseController.listUnarchivedCourseStates();
    } else {
      courseStates = courseController.listCourseStates();
    }
    
    if (courseStates.isEmpty()) {
      return Response.noContent().build();
    }
    
    return Response.ok().entity(objectFactory.createModel(courseStates)).build();
  }
  
  @Path("/courseStates/{ID:[0-9]*}")
  @GET
  @RESTPermit (CoursePermissions.FIND_COURSESTATE)
  public Response findCourseStateById(@PathParam("ID") Long id) {
    CourseState courseState = courseController.findCourseStateById(id);
    if (courseState == null) {
      return Response.status(Status.NOT_FOUND).build(); 
    }
    
    if (courseState.getArchived()) {
      return Response.status(Status.NOT_FOUND).build(); 
    }    
    
    return Response.ok().entity(objectFactory.createModel(courseState)).build();
  }
  
  @Path("/courseStates/{ID:[0-9]*}")
  @PUT
  @RESTPermit (CoursePermissions.UPDATE_COURSESTATE)
  public Response updateCourseState(@PathParam("ID") Long id, fi.otavanopisto.pyramus.rest.model.CourseState entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    CourseState courseState = courseController.findCourseStateById(id);
    if (courseState == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (courseState.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (StringUtils.isBlank(entity.getName())) {
      return Response.status(Status.BAD_REQUEST).entity("Name is required").build();
    }
    
    return Response
        .status(Status.OK)
        .entity(objectFactory.createModel(courseController.updateCourseState(courseState, entity.getName())))
        .build();
  }
  
  @Path("/courseStates/{ID:[0-9]*}") 
  @DELETE
  @RESTPermit (CoursePermissions.ARCHIVE_COURSESTATE)
  public Response archiveCourseState(@PathParam("ID") Long id, @DefaultValue ("false") @QueryParam ("permanent") Boolean permanent) {
    CourseState courseState = courseController.findCourseStateById(id);
    if (courseState == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (permanent) {
      courseController.deleteCourseState(courseState);
    } else {
      courseController.archiveCourseState(courseState, sessionController.getUser());
    }
    
    return Response.noContent().build();
  }
  
  @Path("/courseTypes")
  @POST
  @RESTPermit (CoursePermissions.CREATE_COURSETYPE)
  public Response createCourseType(fi.otavanopisto.pyramus.rest.model.CourseType entity) {
    if (StringUtils.isBlank(entity.getName())) {
      return Response.status(Status.BAD_REQUEST).entity("name is required").build();
    }
    
    return Response
      .status(Status.OK)
      .entity(objectFactory.createModel(courseController.createCourseType(entity.getName())))
      .build();
  }
  
  @Path("/courseTypes")
  @GET
  @RESTPermit (CoursePermissions.LIST_COURSETYPES)
  public Response listCourseTypes(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<CourseType> courseTypes;
    
    if (filterArchived) {
      courseTypes = courseController.listUnarchivedCourseTypes();
    } else {
      courseTypes = courseController.listCourseTypes();
    }
    
    if (courseTypes.isEmpty()) {
      return Response.noContent().build();
    }
    
    return Response.ok().entity(objectFactory.createModel(courseTypes)).build();
  }
  
  @Path("/courseTypes/{ID:[0-9]*}")
  @GET
  @RESTPermit (CoursePermissions.FIND_COURSETYPE)
  public Response findCourseTypeById(@PathParam("ID") Long id, @Context Request request) {
    CourseType courseType = courseController.findCourseTypeById(id);
    if (courseType == null) {
      return Response.status(Status.NOT_FOUND).build(); 
    }
    
    if (courseType.getArchived()) {
      return Response.status(Status.NOT_FOUND).build(); 
    }    
    
    EntityTag tag = new EntityTag(DigestUtils.md5Hex(String.valueOf(courseType.getVersion())));
    
    ResponseBuilder builder = request.evaluatePreconditions(tag);
    if (builder != null) {
      return builder.build();
    }
    
    CacheControl cacheControl = new CacheControl();
    cacheControl.setMustRevalidate(true);
    
    return Response.ok()
      .cacheControl(cacheControl)
      .tag(tag)
      .entity(objectFactory.createModel(courseType)).build();
  }
  
  @Path("/courseTypes/{ID:[0-9]*}")
  @PUT
  @RESTPermit (CoursePermissions.UPDATE_COURSETYPE)
  public Response updateCourseType(@PathParam("ID") Long id, fi.otavanopisto.pyramus.rest.model.CourseType entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    CourseType courseType = courseController.findCourseTypeById(id);
    if (courseType == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (courseType.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (StringUtils.isBlank(entity.getName())) {
      return Response.status(Status.BAD_REQUEST).entity("Name is required").build();
    }
    
    return Response
        .status(Status.OK)
        .entity(objectFactory.createModel(courseController.updateCourseType(courseType, entity.getName())))
        .build();
  }
  
  @Path("/courseTypes/{ID:[0-9]*}") 
  @DELETE
  @RESTPermit (CoursePermissions.ARCHIVE_COURSETYPE)
  public Response deleteCourseType(@PathParam("ID") Long id, @DefaultValue ("false") @QueryParam ("permanent") Boolean permanent) {
    CourseType type = courseController.findCourseTypeById(id);
    if (type == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (permanent) {
      courseController.deleteCourseType(type);
    } else {
      courseController.archiveCourseType(type, sessionController.getUser());
    }
    
    return Response.noContent().build();
  }
  
  @Path("/enrolmentTypes")
  @POST
  @RESTPermit (CoursePermissions.CREATE_COURSEENROLMENTTYPE)
  public Response createCourseEnrolmentType(CourseEnrolmentType entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    if (StringUtils.isBlank(entity.getName())) {
      return Response.status(Status.BAD_REQUEST).entity("Name is required").build();
    }
    
    return Response.ok().entity(objectFactory.createModel(courseController.createCourseEnrolmentType(entity.getName()))).build();
  }
  
  @Path("/enrolmentTypes")
  @GET
  @RESTPermit (CoursePermissions.LIST_COURSEENROLMENTTYPES)
  public Response listCourseEnrolmentTypes() {
    List<fi.otavanopisto.pyramus.domainmodel.courses.CourseEnrolmentType> courseEnrolmentTypes = courseController.listCourseEnrolmentTypes();
    
    
    if (courseEnrolmentTypes.isEmpty()) {
      return Response.status(Status.NO_CONTENT).build();
    }
    
    return Response.ok().entity(objectFactory.createModel(courseEnrolmentTypes)).build();
  }
  
  @Path("/enrolmentTypes/{ID:[0-9]*}")
  @GET
  @RESTPermit (CoursePermissions.FIND_COURSEENROLMENTTYPE)
  public Response findCourseEnrolmentType(@PathParam("ID") Long id) {
    fi.otavanopisto.pyramus.domainmodel.courses.CourseEnrolmentType enrolmentType = courseController.findCourseEnrolmentTypeById(id);
    if (enrolmentType == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    return Response.ok()
        .entity(objectFactory.createModel(enrolmentType))
        .build();
  }
  
  @Path("/enrolmentTypes/{ID:[0-9]*}")
  @PUT
  @RESTPermit (CoursePermissions.UPDATE_COURSEENROLMENTTYPE)
  public Response updateCourseEnrolmentType(@PathParam("ID") Long id, CourseEnrolmentType entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    if (StringUtils.isBlank(entity.getName())) {
      return Response.status(Status.BAD_REQUEST).entity("Name is required").build();
    }
    
    if (!entity.getId().equals(id)) {
      return Response.status(Status.BAD_REQUEST).entity("Cannot update id").build();
    }
    
    fi.otavanopisto.pyramus.domainmodel.courses.CourseEnrolmentType enrolmentType = courseController.findCourseEnrolmentTypeById(id);
    if (enrolmentType == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    return Response.ok()
        .entity(objectFactory.createModel(courseController.updateCourseEnrolmentType(enrolmentType, entity.getName())))
        .build();
  }

  @Path("/enrolmentTypes/{ID:[0-9]*}")
  @DELETE
  @RESTPermit (CoursePermissions.DELETE_COURSEENROLMENTTYPE)
  public Response deleteCourseEnrolmentType(@PathParam("ID") Long id) {
    fi.otavanopisto.pyramus.domainmodel.courses.CourseEnrolmentType enrolmentType = courseController.findCourseEnrolmentTypeById(id);
    if (enrolmentType == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    courseController.deleteCourseEnrolmentType(enrolmentType);
    
    return Response.noContent().build();
  }
    
  @Path("/participationTypes")
  @POST
  @RESTPermit (CoursePermissions.CREATE_COURSEPARTICIPATIONTYPE)
  public Response createCourseParticipationType(fi.otavanopisto.pyramus.rest.model.CourseParticipationType entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    String name = entity.getName();
    if (StringUtils.isBlank(name)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    return Response.ok().entity(objectFactory.createModel(courseController.createCourseParticipationType(name))).build();
  }

  @Path("/participationTypes")
  @GET
  @RESTPermit (CoursePermissions.LIST_COURSEPARTICIPATIONTYPES)
  public Response listCourseParticipationTypes(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<CourseParticipationType> participationTypes;
    
    if (filterArchived) {
      participationTypes = courseController.findUnarchivedCourseParticiPationTypes();
    } else {
      participationTypes = courseController.findCourseParticiPationTypes();
    }
    
    if (participationTypes.isEmpty()) {
      return Response.status(Status.NO_CONTENT).build();
    }
    
    return Response.ok()
        .entity(objectFactory.createModel(participationTypes))
        .build();
  }
  
  @Path("/participationTypes/{ID:[0-9]*}")
  @GET
  @RESTPermit (CoursePermissions.FIND_COURSEPARTICIPATIONTYPE)
  public Response findCourseParticipationType(@PathParam("ID") Long id) {
    CourseParticipationType participationType = courseController.findCourseParticipationTypeById(id);
    if (participationType == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (participationType.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    return Response.ok()
        .entity(objectFactory.createModel(participationType))
        .build();
  }

  @Path("/participationTypes/{ID:[0-9]*}")
  @PUT
  @RESTPermit (CoursePermissions.UPDATE_COURSEPARTICIPATIONTYPE)
  public Response updateCourseParticipationType(@PathParam("ID") Long id, CourseParticipationType entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    CourseParticipationType participationType = courseController.findCourseParticipationTypeById(id);
    if (participationType == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (participationType.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (StringUtils.isBlank(participationType.getName())) {
      return Response.status(Status.BAD_REQUEST).entity("Name is required").build();
    }
    
    return Response.ok().entity(objectFactory.createModel(courseController.updateCourseParticipationType(participationType, entity.getName()))).build();
  }
  
  @Path("/participationTypes/{ID:[0-9]*}")
  @DELETE
  @RESTPermit (CoursePermissions.ARCHIVE_COURSEPARTICIPATIONTYPE)
  public Response archiveCourseParticipationType(@PathParam("ID") Long id, @DefaultValue ("false") @QueryParam ("permanent") Boolean permanent) {
    CourseParticipationType participationType = courseController.findCourseParticipationTypeById(id);
    if (participationType == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (permanent) {
      courseController.deleteCourseParticipationType(participationType);
    } else {
      courseController.archiveCourseParticipationType(participationType, sessionController.getUser());
    }
    
    return Response.noContent().build();
  }
  
  @Path("/descriptionCategories")
  @POST
  @RESTPermit (CoursePermissions.CREATE_COURSEDESCRIPTIONCATEGORY)
  public Response createCourseDescriptionCategory(fi.otavanopisto.pyramus.rest.model.CourseDescriptionCategory entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    String name = entity.getName();
    if (StringUtils.isBlank(name)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    return Response.ok(objectFactory.createModel(courseController.createCourseDescriptionCategory(name)))
       .build();
  }
  
  @Path("/descriptionCategories")
  @GET
  @RESTPermit (CoursePermissions.LIST_COURSEDESCRIPTIONCATEGORIES)
  public Response listCourseDescriptionCategories(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<CourseDescriptionCategory> categories;
    if (filterArchived) {
      categories = courseController.findUnarchivedCourseDescriptionCategories();
    } else {
      categories = courseController.findCourseDescriptionCategories();
    }
    
    if (categories.isEmpty()) {
      return Response.status(Status.NO_CONTENT).build();
    }
    
    return Response.ok()
        .entity(objectFactory.createModel(categories))
        .build();
  }
  
  @Path("/descriptionCategories/{ID:[0-9]*}")
  @GET
  @RESTPermit (CoursePermissions.FIND_COURSEDESCRIPTIONCATEGORY)
  public Response findCourseDescriptionCategoryById(@PathParam("ID") Long id) {
    CourseDescriptionCategory category = courseController.findCourseDescriptionCategoryById(id);
    if (category == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (category.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    return Response.ok()
        .entity(objectFactory.createModel(category))
        .build();
  }

  @Path("/descriptionCategories/{ID:[0-9]*}")
  @PUT
  @RESTPermit (CoursePermissions.UPDATE_COURSEDESCRIPTIONCATEGORY)
  public Response updateCourseDescriptionCategory(@PathParam("ID") Long id, fi.otavanopisto.pyramus.rest.model.CourseDescriptionCategory entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    CourseDescriptionCategory category = courseController.findCourseDescriptionCategoryById(id);
    if (category == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (category.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (StringUtils.isBlank(entity.getName())) {
      return Response.status(Status.BAD_REQUEST).entity("Name is required").build();
    }
    
    return Response.ok(objectFactory.createModel(courseController.updateCourseDescriptionCategory(category, entity.getName()))).build();
  }
  
  @Path("/descriptionCategories/{ID:[0-9]*}")
  @DELETE
  @RESTPermit (CoursePermissions.DELETE_COURSEDESCRIPTIONCATEGORY)
  public Response deleteCourseDescriptionCategory(@PathParam("ID") Long id, @DefaultValue ("false") @QueryParam ("permanent") Boolean permanent) {
    CourseDescriptionCategory category = courseController.findCourseDescriptionCategoryById(id);
    if (category == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (permanent) {
      courseController.deleteCourseDescriptionCategory(category);
    } else {
      courseController.archiveCourseDescriptionCategory(category, sessionController.getUser());
    }

    return Response.noContent().build();
  }
  
  @Path("/variables")
  @POST
  @RESTPermit (CommonPermissions.CREATE_COURSEBASEVARIABLEKEY)
  public Response createVariable(fi.otavanopisto.pyramus.rest.model.VariableKey entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    if (StringUtils.isBlank(entity.getKey())||StringUtils.isBlank(entity.getName())|| entity.getType() == null || entity.getUserEditable() == null) {
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
    
    CourseBaseVariableKey courseBaseVariableKey = commonController.createCourseBaseVariableKey(entity.getKey(), entity.getName(), variableType, entity.getUserEditable());
    return Response.ok(objectFactory.createModel(courseBaseVariableKey)).build();
  }
  
  @Path("/variables")
  @GET
  @RESTPermit (CommonPermissions.LIST_COURSEBASEVARIABLEKEYS)
  public Response listVariables() {
    List<CourseBaseVariableKey> variableKeys = commonController.listCourseBaseVariableKeys();
    if (variableKeys.isEmpty()) {
      return Response.noContent().build();
    }
    
    return Response.ok(objectFactory.createModel(variableKeys)).build();
  }
  
  @Path("/variables/{KEY}")
  @GET
  @RESTPermit (CommonPermissions.FIND_COURSEBASEVARIABLEKEY)
  public Response findVariable(@PathParam ("KEY") String key) {
    CourseBaseVariableKey courseBaseVariableKey = commonController.findCourseBaseVariableKeyByVariableKey(key);
    if (courseBaseVariableKey == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    return Response.ok(objectFactory.createModel(courseBaseVariableKey)).build();
  }
  
  @Path("/variables/{KEY}")
  @PUT
  @RESTPermit (CommonPermissions.UPDATE_COURSEBASEVARIABLEKEY)
  public Response updateVariable(@PathParam ("KEY") String key, fi.otavanopisto.pyramus.rest.model.VariableKey entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    if (StringUtils.isBlank(entity.getName())|| entity.getType() == null || entity.getUserEditable() == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    CourseBaseVariableKey courseBaseVariableKey = commonController.findCourseBaseVariableKeyByVariableKey(key);
    if (courseBaseVariableKey == null) {
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
    
    commonController.updateCourseBaseVariableKey(courseBaseVariableKey, entity.getName(), variableType, entity.getUserEditable());
    
    return Response.ok(objectFactory.createModel(courseBaseVariableKey)).build();
  }
  
  @Path("/variables/{KEY}")
  @DELETE
  @RESTPermit (CommonPermissions.DELETE_COURSEBASEVARIABLEKEY)
  public Response deleteVariable(@PathParam ("KEY") String key) {
    CourseBaseVariableKey courseBaseVariableKey = commonController.findCourseBaseVariableKeyByVariableKey(key);
    if (courseBaseVariableKey == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    commonController.deleteCourseBaseVariableKey(courseBaseVariableKey);
    
    return Response.noContent().build();
  }
  
  @Path("/staffMemberRoles")
  @POST
  @RESTPermit (CoursePermissions.CREATE_STAFFMEMBERROLE)
  public Response createStaffMemberRole(fi.otavanopisto.pyramus.rest.model.CourseStaffMemberRole entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    if (StringUtils.isBlank(entity.getName())) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    CourseStaffMemberRole staffMemberRole = courseController.createStaffMemberRole(entity.getName());
    
    return Response.ok(objectFactory.createModel(staffMemberRole)).build();
  }
  
  @Path("/staffMemberRoles")
  @GET
  @RESTPermit (CoursePermissions.LIST_STAFFMEMBERROLES)
  public Response listStaffMemberRoles() {
    List<CourseStaffMemberRole> staffMemberRoles = courseController.listStaffMemberRoles();
    if (staffMemberRoles.isEmpty()) {
      return Response.noContent().build();
    }
    
    return Response.ok(objectFactory.createModel(staffMemberRoles)).build();
  }
  
  @Path("/staffMemberRoles/{ID}")
  @GET
  @RESTPermit (CoursePermissions.FIND_STAFFMEMBERROLE)
  public Response findStaffMemberRrole(@PathParam ("ID") Long id) {
    CourseStaffMemberRole staffMemberRole = courseController.findStaffMemberRoleById(id);
    if (staffMemberRole == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    return Response.ok(objectFactory.createModel(staffMemberRole)).build();
  }
  
  @Path("/staffMemberRoles/{ID}")
  @PUT
  @RESTPermit (CoursePermissions.UPDATE_STAFFMEMBERROLE)
  public Response updateStaffMemberRrole(@PathParam ("ID") Long id, fi.otavanopisto.pyramus.rest.model.CourseStaffMemberRole entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    if (StringUtils.isBlank(entity.getName())) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    CourseStaffMemberRole staffMemberRole = courseController.findStaffMemberRoleById(id);
    if (staffMemberRole == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    staffMemberRole = courseController.updateCourseStaffMemberRoleName(staffMemberRole, entity.getName());
    
    return Response.ok(objectFactory.createModel(staffMemberRole)).build();
  }
  
  @Path("/staffMemberRoles/{ID}")
  @DELETE
  @RESTPermit (CoursePermissions.DELETE_STAFFMEMBERROLE)
  public Response deleteStaffMemberRrole(@PathParam ("ID") Long id) {
    CourseStaffMemberRole staffMemberRole = courseController.findStaffMemberRoleById(id);
    if (staffMemberRole == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    courseController.deleteStaffMemberRole(staffMemberRole);
    
    return Response.noContent().build();
  }
}
