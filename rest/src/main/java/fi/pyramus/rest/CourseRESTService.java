package fi.pyramus.rest;

import java.util.ArrayList;
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

import fi.pyramus.domainmodel.base.BillingDetails;
import fi.pyramus.domainmodel.base.CourseBaseVariableKey;
import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.base.Subject;
import fi.pyramus.domainmodel.base.VariableType;
import fi.pyramus.domainmodel.courses.Course;
import fi.pyramus.domainmodel.courses.CourseComponent;
import fi.pyramus.domainmodel.courses.CourseDescriptionCategory;
import fi.pyramus.domainmodel.courses.CourseParticipationType;
import fi.pyramus.domainmodel.courses.CourseStaffMemberRole;
import fi.pyramus.domainmodel.courses.CourseState;
import fi.pyramus.domainmodel.courses.CourseStudent;
import fi.pyramus.domainmodel.modules.Module;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.domainmodel.users.StaffMember;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.rest.annotation.RESTPermit;
import fi.pyramus.rest.annotation.RESTPermit.Handle;
import fi.pyramus.rest.controller.CommonController;
import fi.pyramus.rest.controller.CourseController;
import fi.pyramus.rest.controller.ModuleController;
import fi.pyramus.rest.controller.StudentController;
import fi.pyramus.rest.controller.UserController;
import fi.pyramus.rest.controller.permissions.CommonPermissions;
import fi.pyramus.rest.controller.permissions.CoursePermissions;
import fi.pyramus.rest.model.CourseEnrolmentType;

@Path("/courses")
@Produces("application/json")
@Consumes("application/json")
@Stateful
@RequestScoped
public class CourseRESTService extends AbstractRESTService {
  
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
  private ObjectFactory objectFactory;
  
  @Path("/courses")
  @POST
  @RESTPermit (handle = Handle.EXCEPTION, value = CoursePermissions.CREATE_COURSE)
  public Response createCourse(fi.pyramus.rest.model.Course courseEntity) {
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

    Subject subject = null;
    if (courseEntity.getSubjectId() != null) {
      subject = commonController.findSubjectById(courseEntity.getSubjectId());
      if (subject == null) {
        return Response.status(Status.NOT_FOUND).entity("specified subject does not exist").build();
      }
    }
    
    Integer courseNumber = courseEntity.getCourseNumber();
    DateTime beginDate = courseEntity.getBeginDate();
    DateTime endDate = courseEntity.getEndDate();
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
    Double planningHours = courseEntity.getPlanningHours();
    Double assessingHours = courseEntity.getAssessingHours();
    String description = courseEntity.getDescription();
    Long maxParticipantCount = courseEntity.getMaxParticipantCount();
    Date enrolmentTimeEnd = toDate(courseEntity.getEnrolmentTimeEnd());
    
    User loggedUser = getLoggedUser();
    
    Course course = courseController.createCourse(module, name, nameExtension, state, subject, courseNumber, 
        toDate(beginDate), toDate(endDate), courseLength, courseLengthTimeUnit, distanceTeachingDays, localTeachingDays, teachingHours, 
        planningHours, assessingHours, description, maxParticipantCount, enrolmentTimeEnd, loggedUser);
    
    if (courseEntity.getTags() != null) {
      for (String tag : courseEntity.getTags()) {
        courseController.createCourseTag(course, tag);
      }
    }
    
    return Response.ok().entity(objectFactory.createModel(course)).build();
  }
  
  @Path("/courses/{ID:[0-9]*}")
  @GET
  @RESTPermit (handle = Handle.EXCEPTION, value = CoursePermissions.FIND_COURSE)
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
  @RESTPermit (handle = Handle.EXCEPTION, value = CoursePermissions.LIST_COURSES)
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
  @RESTPermit (handle = Handle.EXCEPTION, value = CoursePermissions.UPDATE_COURSE)
  public Response updateCourse(@PathParam("ID") Long id, fi.pyramus.rest.model.Course courseEntity) {
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

    Subject subject = null;
    if (courseEntity.getSubjectId() != null) {
      subject = commonController.findSubjectById(courseEntity.getSubjectId());
      if (subject == null) {
        return Response.status(Status.NOT_FOUND).entity("specified subject does not exist").build();
      }
    }
    
    Integer courseNumber = courseEntity.getCourseNumber();
    DateTime beginDate = courseEntity.getBeginDate();
    DateTime endDate = courseEntity.getEndDate();
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
    Double planningHours = courseEntity.getPlanningHours();
    Double assessingHours = courseEntity.getAssessingHours();
    String description = courseEntity.getDescription();
    Long maxParticipantCount = courseEntity.getMaxParticipantCount();
    Date enrolmentTimeEnd = toDate(courseEntity.getEnrolmentTimeEnd());
    User loggedUser = getLoggedUser();
    
    Course updatedCourse = courseController.updateCourse(course, name, nameExtension, state, subject, courseNumber, toDate(beginDate), toDate(endDate), courseLength,
        courseLengthTimeUnit, distanceTeachingDays, localTeachingDays, teachingHours, planningHours, assessingHours, description,
        maxParticipantCount, enrolmentTimeEnd, loggedUser);
    
    courseController.updateCourseTags(updatedCourse, courseEntity.getTags() == null ? new ArrayList<String>() : courseEntity.getTags());
    
    return Response.ok().entity(objectFactory.createModel(updatedCourse)).build();
  }
  
  @Path("/courses/{ID:[0-9]*}")
  @DELETE
  @RESTPermit (handle = Handle.EXCEPTION, value = CoursePermissions.DELETE_COURSE)
  public Response deleteCourse(@PathParam("ID") Long id, @DefaultValue ("false") @QueryParam ("permanent") Boolean permanent) {
    User loggedUser = getLoggedUser();
    
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
  @RESTPermit (handle = Handle.EXCEPTION, value = CoursePermissions.CREATE_COURSECOMPONENT)
  public Response createCourseComponent(@PathParam("COURSEID") Long courseId, fi.pyramus.rest.model.CourseComponent courseComponent) {
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

  @Path("/courses/{ID:[0-9]*}/components")
  @GET
  @RESTPermit (handle = Handle.EXCEPTION, value = CoursePermissions.LIST_COURSECOMPONENTS)
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
  @RESTPermit (handle = Handle.EXCEPTION, value = CoursePermissions.FIND_COURSECOMPONENT)
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
  @RESTPermit (handle = Handle.EXCEPTION, value = CoursePermissions.UPDATE_COURSECOMPONENT)
  public Response updateCourseComponent(@PathParam("COURSEID") Long courseId, @PathParam("COMPONENTID") Long courseComponentId, fi.pyramus.rest.model.CourseComponent courseComponentEntity) {
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
  @RESTPermit (handle = Handle.EXCEPTION, value = CoursePermissions.DELETE_COURSECOMPONENT)
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
      courseController.archiveCourseComponent(courseComponent, getLoggedUser());
    }
    
    return Response.status(Status.NO_CONTENT).build();
  }
  
  @Path("/courses/{COURSEID:[0-9]*}/students")
  @POST
  @RESTPermit (handle = Handle.EXCEPTION, value = CoursePermissions.CREATE_COURSESTUDENT)
  public Response createCourseStudent(@PathParam("COURSEID") Long courseId, fi.pyramus.rest.model.CourseStudent entity) {
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
    
    Course course = courseController.findCourseById(courseId);
    if (course == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    Student student = studentController.findStudentById(entity.getStudentId());
    if (student == null) {
      return Response.status(Status.BAD_REQUEST).entity("could not find the student #" + entity.getStudentId()).build();
    }

    BillingDetails billingDetails = null;
    if (entity.getBillingDetailsId() != null) {
      billingDetails = commonController.findBillingDetailsById(entity.getBillingDetailsId());
      if (billingDetails == null) {
        return Response.status(Status.BAD_REQUEST).entity("could not find billingDetails #" + entity.getBillingDetailsId()).build();
      }
    }
    
    fi.pyramus.domainmodel.courses.CourseEnrolmentType enrolmentType = null;
    if (entity.getEnrolmentTypeId() != null) {
      enrolmentType = courseController.findCourseEnrolmentTypeById(entity.getEnrolmentTypeId());
      if (enrolmentType == null) {
        return Response.status(Status.BAD_REQUEST).entity("could not find enrolmentType #" + entity.getEnrolmentTypeId()).build();
      }
    } else {
      enrolmentType = courseController.getDefaultCourseEnrolmentType();
    }
    
    fi.pyramus.domainmodel.base.CourseOptionality optionality = entity.getOptionality() != null ? fi.pyramus.domainmodel.base.CourseOptionality.valueOf(entity.getOptionality().name()) : null;
    fi.pyramus.domainmodel.courses.CourseParticipationType participantionType = null;
        
    if (entity.getParticipationTypeId() != null) {
      participantionType = courseController.findCourseParticipationTypeById(entity.getParticipationTypeId());
      if (participantionType == null) {
        return Response.status(Status.BAD_REQUEST).entity("could not find participantionType #" + entity.getParticipationTypeId()).build();
      }
    } else {
      participantionType = courseController.getDefaultCourseParticipationType();
    }
    
    return Response.status(Status.OK)
      .entity(objectFactory.createModel(courseController.createCourseStudent(course, student, enrolmentType, participantionType, toDate(entity.getEnrolmentTime()), entity.getLodging(), optionality, billingDetails)))
      .build();
  }

  @Path("/courses/{ID:[0-9]*}/students")
  @GET
  @RESTPermit (handle = Handle.EXCEPTION, value = CoursePermissions.LIST_COURSESTUDENTS)
  public Response listCourseStudents(@PathParam("ID") Long courseId) {
    Course course = courseController.findCourseById(courseId);
    if (course == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    List<fi.pyramus.domainmodel.courses.CourseStudent> students = courseController.listCourseStudentsByCourse(course);
    if (students.isEmpty()) {
      return Response.status(Status.NO_CONTENT).build();
    }
    
    return Response.status(Status.OK).entity(objectFactory.createModel(students)).build();
  }

  @Path("/courses/{CID:[0-9]*}/students/{ID:[0-9]*}")
  @GET
  @RESTPermit (handle = Handle.EXCEPTION, value = CoursePermissions.FIND_COURSESTUDENT)
  public Response findCourseStudentById(@PathParam("CID") Long courseId, @PathParam("ID") Long id) {
    Course course = courseController.findCourseById(courseId);
    if (course == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    fi.pyramus.domainmodel.courses.CourseStudent student = courseController.findCourseStudentById(id);
    if (student == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (!student.getCourse().getId().equals(courseId)) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if ((student.getArchived())||(student.getStudent().getArchived())||(student.getCourse().getArchived())) {
      return Response.status(Status.NOT_FOUND).build();
    }

    return Response.status(Status.OK).entity(objectFactory.createModel(student)).build();
  }
  
  @Path("/courses/{COURSEID:[0-9]*}/students/{ID:[0-9]*}")
  @PUT
  @RESTPermit (handle = Handle.EXCEPTION, value = CoursePermissions.UPDATE_COURSESTUDENT)
  public Response updateCourseStudent(@PathParam("COURSEID") Long courseId, @PathParam("ID") Long id, fi.pyramus.rest.model.CourseStudent entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build(); 
    }
    
    if (entity.getStudentId() == null) {
      return Response.status(Status.BAD_REQUEST).build(); 
    }
    
    if (entity.getEnrolmentTypeId() == null) {
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
    
    fi.pyramus.domainmodel.courses.CourseEnrolmentType courseEnrolmentType = courseController.findCourseEnrolmentTypeById(entity.getEnrolmentTypeId());
    if (courseEnrolmentType == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    fi.pyramus.domainmodel.base.CourseOptionality optionality = entity.getOptionality() != null ? fi.pyramus.domainmodel.base.CourseOptionality.valueOf(entity.getOptionality().name()) : null;
    
    fi.pyramus.domainmodel.courses.CourseParticipationType courseParticipantionType = courseController.findCourseParticipationTypeById(entity.getParticipationTypeId());
    if (courseParticipantionType == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    return Response.status(Status.OK).entity(objectFactory.createModel(
      courseController.updateCourseStudent(courseStudent, entity.getLodging(), billingDetails, courseEnrolmentType, toDate(entity.getEnrolmentTime()), optionality, courseParticipantionType))
    ).build();
  }

  @Path("/courses/{COURSEID:[0-9]*}/students/{ID:[0-9]*}")
  @DELETE
  @RESTPermit (handle = Handle.EXCEPTION, value = CoursePermissions.DELETE_COURSESTUDENT)
  public Response deleteCourseStudent(@PathParam("COURSEID") Long courseId, @PathParam("ID") Long id, @DefaultValue ("false") @QueryParam ("permanent") Boolean permanent) {
    Course course = courseController.findCourseById(courseId);
    if (course == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    fi.pyramus.domainmodel.courses.CourseStudent courseStudent = courseController.findCourseStudentById(id);
    if (courseStudent == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (!courseStudent.getCourse().getId().equals(courseId)) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (permanent) {
      courseController.deleteCourseStudent(courseStudent);
    } else {
      courseController.archiveCourseStudent(courseStudent, getLoggedUser());
    }
    
    return Response.status(Status.NO_CONTENT).build();
  }
  
  @Path("/courses/{COURSEID:[0-9]*}/staffMembers")
  @POST
  @RESTPermit (handle = Handle.EXCEPTION, value = CoursePermissions.CREATE_COURSESTAFFMEMBER)
  public Response createCourseStaffMember(@PathParam("COURSEID") Long courseId, fi.pyramus.rest.model.CourseStaffMember entity) {
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
  @RESTPermit (handle = Handle.EXCEPTION, value = CoursePermissions.LIST_COURSESTAFFMEMBERS)
  public Response listCourseStaffMembers(@PathParam("ID") Long courseId) {
    Course course = courseController.findCourseById(courseId);
    if (course == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    List<fi.pyramus.domainmodel.courses.CourseStaffMember> members = courseController.listStaffMembersByCourse(course);
    if (members.isEmpty()) {
      return Response.status(Status.NO_CONTENT).build();
    }
    
    return Response.status(Status.OK).entity(objectFactory.createModel(members)).build();
  }

  @Path("/courses/{CID:[0-9]*}/staffMembers/{ID:[0-9]*}")
  @GET
  @RESTPermit (handle = Handle.EXCEPTION, value = CoursePermissions.FIND_COURSESTAFFMEMBER)
  public Response findCourseStaffMemberById(@PathParam("CID") Long courseId, @PathParam("ID") Long id) {
    Course course = courseController.findCourseById(courseId);
    if (course == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    fi.pyramus.domainmodel.courses.CourseStaffMember staffMember = courseController.findStaffMemberById(id);
    if (staffMember == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (!staffMember.getCourse().getId().equals(courseId)) {
      return Response.status(Status.NOT_FOUND).build();
    }

    return Response.status(Status.OK).entity(objectFactory.createModel(staffMember)).build();
  }
  
  @Path("/courses/{COURSEID:[0-9]*}/staffMembers/{ID:[0-9]*}")
  @PUT
  @RESTPermit (handle = Handle.EXCEPTION, value = CoursePermissions.UPDATE_COURSESTAFFMEMBER)
  public Response updateCourseStaffMember(@PathParam("COURSEID") Long courseId, @PathParam("ID") Long id, fi.pyramus.rest.model.CourseStaffMember entity) {
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
    
    fi.pyramus.domainmodel.courses.CourseStaffMember staffMember = courseController.findStaffMemberById(id);
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
  @RESTPermit (handle = Handle.EXCEPTION, value = CoursePermissions.DELETE_COURSESTAFFMEMBER)
  public Response deleteCourseStaffMember(@PathParam("COURSEID") Long courseId, @PathParam("ID") Long id) {
    Course course = courseController.findCourseById(courseId);
    if (course == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    fi.pyramus.domainmodel.courses.CourseStaffMember staffMember = courseController.findStaffMemberById(id);
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
  @RESTPermit (handle = Handle.EXCEPTION, value = CoursePermissions.CREATE_COURSESTATE)
  public Response createCourseState(fi.pyramus.rest.model.CourseState entity) {
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
  @RESTPermit (handle = Handle.EXCEPTION, value = CoursePermissions.LIST_COURSESTATES)
  public Response listCourseStates(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<CourseState> courseStates;
    
    if (filterArchived) {
      courseStates = courseController.findUnarchivedCourseStates();
    } else {
      courseStates = courseController.findCourseStates();
    }
    
    if (courseStates.isEmpty()) {
      return Response.noContent().build();
    }
    
    return Response.ok().entity(objectFactory.createModel(courseStates)).build();
  }
  
  @Path("/courseStates/{ID:[0-9]*}")
  @GET
  @RESTPermit (handle = Handle.EXCEPTION, value = CoursePermissions.FIND_COURSESTATE)
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
  @RESTPermit (handle = Handle.EXCEPTION, value = CoursePermissions.UPDATE_COURSESTATE)
  public Response updateCourseState(@PathParam("ID") Long id, fi.pyramus.rest.model.CourseState entity) {
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
  @RESTPermit (handle = Handle.EXCEPTION, value = CoursePermissions.ARCHIVE_COURSESTATE)
  public Response archiveCourseState(@PathParam("ID") Long id, @DefaultValue ("false") @QueryParam ("permanent") Boolean permanent) {
    CourseState courseState = courseController.findCourseStateById(id);
    if (courseState == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (permanent) {
      courseController.deleteCourseState(courseState);
    } else {
      courseController.archiveCourseState(courseState, getLoggedUser());
    }
    
    return Response.noContent().build();
  }
  
  @Path("/enrolmentTypes")
  @POST
  @RESTPermit (handle = Handle.EXCEPTION, value = CoursePermissions.CREATE_COURSEENROLMENTTYPE)
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
  @RESTPermit (handle = Handle.EXCEPTION, value = CoursePermissions.LIST_COURSEENROLMENTTYPES)
  public Response listCourseEnrolmentTypes() {
    List<fi.pyramus.domainmodel.courses.CourseEnrolmentType> courseEnrolmentTypes = courseController.listCourseEnrolmentTypes();
    
    
    if (courseEnrolmentTypes.isEmpty()) {
      return Response.status(Status.NO_CONTENT).build();
    }
    
    return Response.ok().entity(objectFactory.createModel(courseEnrolmentTypes)).build();
  }
  
  @Path("/enrolmentTypes/{ID:[0-9]*}")
  @GET
  @RESTPermit (handle = Handle.EXCEPTION, value = CoursePermissions.FIND_COURSEENROLMENTTYPE)
  public Response findCourseEnrolmentType(@PathParam("ID") Long id) {
    fi.pyramus.domainmodel.courses.CourseEnrolmentType enrolmentType = courseController.findCourseEnrolmentTypeById(id);
    if (enrolmentType == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    return Response.ok()
        .entity(objectFactory.createModel(enrolmentType))
        .build();
  }
  
  @Path("/enrolmentTypes/{ID:[0-9]*}")
  @PUT
  @RESTPermit (handle = Handle.EXCEPTION, value = CoursePermissions.UPDATE_COURSEENROLMENTTYPE)
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
    
    fi.pyramus.domainmodel.courses.CourseEnrolmentType enrolmentType = courseController.findCourseEnrolmentTypeById(id);
    if (enrolmentType == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    return Response.ok()
        .entity(objectFactory.createModel(courseController.updateCourseEnrolmentType(enrolmentType, entity.getName())))
        .build();
  }

  @Path("/enrolmentTypes/{ID:[0-9]*}")
  @DELETE
  @RESTPermit (handle = Handle.EXCEPTION, value = CoursePermissions.DELETE_COURSEENROLMENTTYPE)
  public Response deleteCourseEnrolmentType(@PathParam("ID") Long id) {
    fi.pyramus.domainmodel.courses.CourseEnrolmentType enrolmentType = courseController.findCourseEnrolmentTypeById(id);
    if (enrolmentType == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    courseController.deleteCourseEnrolmentType(enrolmentType);
    
    return Response.noContent().build();
  }
    
  @Path("/participationTypes")
  @POST
  @RESTPermit (handle = Handle.EXCEPTION, value = CoursePermissions.CREATE_COURSEPARTICIPATIONTYPE)
  public Response createCourseParticipationType(fi.pyramus.rest.model.CourseParticipationType entity) {
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
  @RESTPermit (handle = Handle.EXCEPTION, value = CoursePermissions.LIST_COURSEPARTICIPATIONTYPES)
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
  @RESTPermit (handle = Handle.EXCEPTION, value = CoursePermissions.FIND_COURSEPARTICIPATIONTYPE)
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
  @RESTPermit (handle = Handle.EXCEPTION, value = CoursePermissions.UPDATE_COURSEPARTICIPATIONTYPE)
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
  @RESTPermit (handle = Handle.EXCEPTION, value = CoursePermissions.ARCHIVE_COURSEPARTICIPATIONTYPE)
  public Response archiveCourseParticipationType(@PathParam("ID") Long id, @DefaultValue ("false") @QueryParam ("permanent") Boolean permanent) {
    CourseParticipationType participationType = courseController.findCourseParticipationTypeById(id);
    if (participationType == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (permanent) {
      courseController.deleteCourseParticipationType(participationType);
    } else {
      courseController.archiveCourseParticipationType(participationType, getLoggedUser());
    }
    
    return Response.noContent().build();
  }
  
  @Path("/descriptionCategories")
  @POST
  @RESTPermit (handle = Handle.EXCEPTION, value = CoursePermissions.CREATE_COURSEDESCRIPTIONCATEGORY)
  public Response createCourseDescriptionCategory(fi.pyramus.rest.model.CourseDescriptionCategory entity) {
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
  @RESTPermit (handle = Handle.EXCEPTION, value = CoursePermissions.LIST_COURSEDESCRIPTIONCATEGORIES)
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
  @RESTPermit (handle = Handle.EXCEPTION, value = CoursePermissions.FIND_COURSEDESCRIPTIONCATEGORY)
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
  @RESTPermit (handle = Handle.EXCEPTION, value = CoursePermissions.UPDATE_COURSEDESCRIPTIONCATEGORY)
  public Response updateCourseDescriptionCategory(@PathParam("ID") Long id, fi.pyramus.rest.model.CourseDescriptionCategory entity) {
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
  @RESTPermit (handle = Handle.EXCEPTION, value = CoursePermissions.DELETE_COURSEDESCRIPTIONCATEGORY)
  public Response deleteCourseDescriptionCategory(@PathParam("ID") Long id, @DefaultValue ("false") @QueryParam ("permanent") Boolean permanent) {
    CourseDescriptionCategory category = courseController.findCourseDescriptionCategoryById(id);
    if (category == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (permanent) {
      courseController.deleteCourseDescriptionCategory(category);
    } else {
      courseController.archiveCourseDescriptionCategory(category, getLoggedUser());
    }

    return Response.noContent().build();
  }
  
  @Path("/variables")
  @POST
  @RESTPermit (handle = Handle.EXCEPTION, value = CommonPermissions.CREATE_COURSEBASEVARIABLEKEY)
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
    
    CourseBaseVariableKey courseBaseVariableKey = commonController.createCourseBaseVariableKey(entity.getKey(), entity.getName(), variableType, entity.getUserEditable());
    return Response.ok(objectFactory.createModel(courseBaseVariableKey)).build();
  }
  
  @Path("/variables")
  @GET
  @RESTPermit (handle = Handle.EXCEPTION, value = CommonPermissions.LIST_COURSEBASEVARIABLEKEYS)
  public Response listVariables() {
    List<CourseBaseVariableKey> variableKeys = commonController.listCourseBaseVariableKeys();
    if (variableKeys.isEmpty()) {
      return Response.noContent().build();
    }
    
    return Response.ok(objectFactory.createModel(variableKeys)).build();
  }
  
  @Path("/variables/{KEY}")
  @GET
  @RESTPermit (handle = Handle.EXCEPTION, value = CommonPermissions.FIND_COURSEBASEVARIABLEKEY)
  public Response findVariable(@PathParam ("KEY") String key) {
    CourseBaseVariableKey courseBaseVariableKey = commonController.findCourseBaseVariableKeyByVariableKey(key);
    if (courseBaseVariableKey == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    return Response.ok(objectFactory.createModel(courseBaseVariableKey)).build();
  }
  
  @Path("/variables/{KEY}")
  @PUT
  @RESTPermit (handle = Handle.EXCEPTION, value = CommonPermissions.UPDATE_COURSEBASEVARIABLEKEY)
  public Response updateVariable(@PathParam ("KEY") String key, fi.pyramus.rest.model.VariableKey entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    if (StringUtils.isBlank(entity.getName())||(entity.getType() == null)||(entity.getUserEditable() == null)) {
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
  @RESTPermit (handle = Handle.EXCEPTION, value = CommonPermissions.DELETE_COURSEBASEVARIABLEKEY)
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
  @RESTPermit (handle = Handle.EXCEPTION, value = CoursePermissions.CREATE_STAFFMEMBERROLE)
  public Response createStaffMemberRole(fi.pyramus.rest.model.CourseStaffMemberRole entity) {
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
  @RESTPermit (handle = Handle.EXCEPTION, value = CoursePermissions.LIST_STAFFMEMBERROLES)
  public Response listStaffMemberRoles() {
    List<CourseStaffMemberRole> staffMemberRoles = courseController.listStaffMemberRoles();
    if (staffMemberRoles.isEmpty()) {
      return Response.noContent().build();
    }
    
    return Response.ok(objectFactory.createModel(staffMemberRoles)).build();
  }
  
  @Path("/staffMemberRoles/{ID}")
  @GET
  @RESTPermit (handle = Handle.EXCEPTION, value = CoursePermissions.FIND_STAFFMEMBERROLE)
  public Response findStaffMemberRrole(@PathParam ("ID") Long id) {
    CourseStaffMemberRole staffMemberRole = courseController.findStaffMemberRoleById(id);
    if (staffMemberRole == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    return Response.ok(objectFactory.createModel(staffMemberRole)).build();
  }
  
  @Path("/staffMemberRoles/{ID}")
  @PUT
  @RESTPermit (handle = Handle.EXCEPTION, value = CoursePermissions.UPDATE_STAFFMEMBERROLE)
  public Response updateStaffMemberRrole(@PathParam ("ID") Long id, fi.pyramus.rest.model.CourseStaffMemberRole entity) {
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
  @RESTPermit (handle = Handle.EXCEPTION, value = CoursePermissions.DELETE_STAFFMEMBERROLE)
  public Response deleteStaffMemberRrole(@PathParam ("ID") Long id) {
    CourseStaffMemberRole staffMemberRole = courseController.findStaffMemberRoleById(id);
    if (staffMemberRole == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    courseController.deleteStaffMemberRole(staffMemberRole);
    
    return Response.noContent().build();
  }
  
}
