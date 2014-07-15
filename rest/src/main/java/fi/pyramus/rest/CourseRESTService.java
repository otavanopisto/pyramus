package fi.pyramus.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

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

import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.base.Subject;
import fi.pyramus.domainmodel.base.Tag;
import fi.pyramus.domainmodel.courses.Course;
import fi.pyramus.domainmodel.courses.CourseComponent;
import fi.pyramus.domainmodel.courses.CourseDescriptionCategory;
import fi.pyramus.domainmodel.courses.CourseParticipationType;
import fi.pyramus.domainmodel.courses.CourseState;
import fi.pyramus.domainmodel.modules.Module;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.rest.controller.CommonController;
import fi.pyramus.rest.controller.CourseController;
import fi.pyramus.rest.controller.ModuleController;
import fi.pyramus.rest.controller.TagController;
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
  private ModuleController moduleController;
  
  @Inject
  private CommonController commonController;
  
  @Path("/courses")
  @POST
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
    Date beginDate = courseEntity.getBeginDate();
    Date endDate = courseEntity.getEndDate();
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
    Date enrolmentTimeEnd = courseEntity.getEnrolmentTimeEnd();
    
    User loggedUser = getLoggedUser();
    
    Course course = courseController.createCourse(module, name, nameExtension, state, subject, courseNumber, 
        beginDate, endDate, courseLength, courseLengthTimeUnit, distanceTeachingDays, localTeachingDays, teachingHours, 
        planningHours, assessingHours, description, maxParticipantCount, enrolmentTimeEnd, loggedUser);
    
    if (courseEntity.getTags() != null) {
      for (String tag : courseEntity.getTags()) {
        courseController.createCourseTag(course, tag);
      }
    }
    
    return Response.ok().entity(createCourseRestModel(course)).build();
  }
  
  @Path("/courses/{ID:[0-9]*}")
  @GET
  public Response getCourse(@PathParam("ID") Long id) {
    Course course = courseController.findCourseById(id);
    if (course != null) {
      if (course.getArchived()) {
        return Response.status(Status.NOT_FOUND).build();
      }
      
      return Response.ok().entity(createCourseRestModel(course)).build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/courses")
  @GET
  public Response listCourses(@DefaultValue("true") @QueryParam("filterArchived") boolean filterArchived) {
    List<Course> courses;
    
    if (filterArchived) {
      courses = courseController.findUnarchivedCourses();
    } else {
      courses = courseController.findCourses();
    }
    
    if (!courses.isEmpty()) {
      return Response.ok().entity(createCourseRestModel(courses)).build();
    } else {
      return Response.status(Status.NO_CONTENT).build();
    }
  }
  
  @Path("/courses/{ID:[0-9]*}")
  @PUT
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
    Date beginDate = courseEntity.getBeginDate();
    Date endDate = courseEntity.getEndDate();
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
    Date enrolmentTimeEnd = courseEntity.getEnrolmentTimeEnd();
    User loggedUser = getLoggedUser();
    
    Course updatedCourse = courseController.updateCourse(course, name, nameExtension, state, subject, courseNumber, beginDate, endDate, courseLength,
        courseLengthTimeUnit, distanceTeachingDays, localTeachingDays, teachingHours, planningHours, assessingHours, description,
        maxParticipantCount, enrolmentTimeEnd, loggedUser);
    
    courseController.updateCourseTags(updatedCourse, courseEntity.getTags() == null ? new ArrayList<String>() : courseEntity.getTags());
    
    return Response.ok().entity(createCourseRestModel(updatedCourse)).build();
  }
  
  @Path("/courses/{ID:[0-9]*}")
  @DELETE
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
      .entity(createCourseComponentRestModel(courseController.createCourseComponent(course, courseComponent.getLength(), componentLengthTimeUnit, courseComponent.getName(), courseComponent.getDescription())))
      .build();
  }

  @Path("/courses/{ID:[0-9]*}/components")
  @GET
  public Response listCourseComponents(@PathParam("ID") Long courseId) {
    Course course = courseController.findCourseById(courseId);
    if (course == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    List<CourseComponent> components = courseController.findCourseComponentsByCourse(course);
    if (components.isEmpty()) {
      return Response.status(Status.NO_CONTENT).build();
    }
    
    return Response.status(Status.OK).entity(createCourseComponentRestModel(components)).build();
  }

  @Path("/courses/{CID:[0-9]*}/components/{ID:[0-9]*}")
  @GET
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

    return Response.status(Status.OK).entity(createCourseComponentRestModel(component)).build();
  }
  
  @Path("/courses/{COURSEID:[0-9]*}/components/{COMPONENTID:[0-9]*}")
  @PUT
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
      .entity(createCourseComponentRestModel(courseController.updateCourseComponent(courseComponent, courseComponentEntity.getLength(), componentLengthTimeUnit, courseComponentEntity.getName(), courseComponentEntity.getDescription())))
      .build();
  }
  
  @Path("/courses/{COURSEID:[0-9]*}/components/{COMPONENTID:[0-9]*}")
  @DELETE
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
  
  @Path("/courseStates")
  @POST
  public Response createCourseState(fi.pyramus.rest.model.CourseState entity) {
    if (StringUtils.isBlank(entity.getName())) {
      return Response.status(Status.BAD_REQUEST).entity("name is required").build();
    }
    
    return Response
      .status(Status.OK)
      .entity(createCourseStateRestModel(courseController.createCourseState(entity.getName())))
      .build();
  }
  
  @Path("/courseStates")
  @GET
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
    
    return Response.ok().entity(createCourseStateRestModel(courseStates)).build();
  }
  
  @Path("/courseStates/{ID:[0-9]*}")
  @GET
  public Response findCourseStateById(@PathParam("ID") Long id) {
    CourseState courseState = courseController.findCourseStateById(id);
    if (courseState == null) {
      return Response.status(Status.NOT_FOUND).build(); 
    }
    
    if (courseState.getArchived()) {
      return Response.status(Status.NOT_FOUND).build(); 
    }    
    
    return Response.ok().entity(createCourseStateRestModel(courseState)).build();
  }
  
  @Path("/courseStates/{ID:[0-9]*}")
  @PUT
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
        .entity(createCourseStateRestModel(courseController.updateCourseState(courseState, entity.getName())))
        .build();
  }
  
  @Path("/courseStates/{ID:[0-9]*}") 
  @DELETE
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
  public Response createCourseEnrolmentType(CourseEnrolmentType entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    if (StringUtils.isBlank(entity.getName())) {
      return Response.status(Status.BAD_REQUEST).entity("Name is required").build();
    }
    
    return Response.ok().entity(createCourseEnrolmentTypeRestModel(courseController.createCourseEnrolmentType(entity.getName()))).build();
  }
  
  @Path("/enrolmentTypes")
  @GET
  public Response listCourseEnrolmentTypes() {
    List<fi.pyramus.domainmodel.courses.CourseEnrolmentType> courseEnrolmentTypes = courseController.listCourseEnrolmentTypes();
    
    
    if (courseEnrolmentTypes.isEmpty()) {
      return Response.status(Status.NO_CONTENT).build();
    }
    
    return Response.ok().entity(createCourseEnrolmentTypeRestModel(courseEnrolmentTypes)).build();
  }
  
  @Path("/enrolmentTypes/{ID:[0-9]*}")
  @GET
  public Response findCourseEnrolmentType(@PathParam("ID") Long id) {
    fi.pyramus.domainmodel.courses.CourseEnrolmentType enrolmentType = courseController.findCourseEnrolmentTypeById(id);
    if (enrolmentType == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    return Response.ok()
        .entity(createCourseEnrolmentTypeRestModel(enrolmentType))
        .build();
  }
  
  @Path("/enrolmentTypes/{ID:[0-9]*}")
  @PUT
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
        .entity(createCourseEnrolmentTypeRestModel(courseController.updateCourseEnrolmentType(enrolmentType, entity.getName())))
        .build();
  }

  @Path("/enrolmentTypes/{ID:[0-9]*}")
  @DELETE
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
  public Response createCourseParticipationType(fi.pyramus.rest.model.CourseParticipationType entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    String name = entity.getName();
    if (StringUtils.isBlank(name)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    return Response.ok().entity(createCourseParticipationTypeRestModel(courseController.createCourseParticipationType(name))).build();
  }

  @Path("/participationTypes")
  @GET
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
        .entity(createCourseParticipationTypeRestModel(participationTypes))
        .build();
  }
  
  @Path("/participationTypes/{ID:[0-9]*}")
  @GET
  public Response findCourseParticipationType(@PathParam("ID") Long id) {
    CourseParticipationType participationType = courseController.findCourseParticipationTypeById(id);
    if (participationType == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (participationType.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    return Response.ok()
        .entity(createCourseParticipationTypeRestModel(participationType))
        .build();
  }

  @Path("/participationTypes/{ID:[0-9]*}")
  @PUT
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
    
    return Response.ok().entity(createCourseParticipationTypeRestModel(courseController.updateCourseParticipationType(participationType, entity.getName()))).build();
  }
  
  @Path("/participationTypes/{ID:[0-9]*}")
  @DELETE
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
  public Response createCourseDescriptionCategory(fi.pyramus.rest.model.CourseDescriptionCategory entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    String name = entity.getName();
    if (StringUtils.isBlank(name)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    return Response.ok(createCourseDescriptionCategoryRestModel(courseController.createCourseDescriptionCategory(name)))
       .build();
  }
  
  @Path("/descriptionCategories")
  @GET
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
        .entity(createCourseDescriptionCategoryRestModel(categories))
        .build();
  }
  
  @Path("/descriptionCategories/{ID:[0-9]*}")
  @GET
  public Response findCourseDescriptionCategoryById(@PathParam("ID") Long id) {
    CourseDescriptionCategory category = courseController.findCourseDescriptionCategoryById(id);
    if (category == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (category.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    return Response.ok()
        .entity(createCourseDescriptionCategoryRestModel(category))
        .build();
  }

  @Path("/descriptionCategories/{ID:[0-9]*}")
  @PUT
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
    
    return Response.ok(createCourseDescriptionCategoryRestModel(courseController.updateCourseDescriptionCategory(category, entity.getName()))).build();
  }
  
  @Path("/descriptionCategories/{ID:[0-9]*}")
  @DELETE
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

  private fi.pyramus.rest.model.CourseParticipationType createCourseParticipationTypeRestModel(CourseParticipationType courseParticipationType) {
    return new fi.pyramus.rest.model.CourseParticipationType(courseParticipationType.getId(), courseParticipationType.getName(), courseParticipationType.getArchived());
  }
  
  private List<fi.pyramus.rest.model.CourseParticipationType> createCourseParticipationTypeRestModel(List<CourseParticipationType> participationTypes) {
    List<fi.pyramus.rest.model.CourseParticipationType> result = new ArrayList<>();
    
    for (CourseParticipationType participationType : participationTypes) {
      result.add(createCourseParticipationTypeRestModel(participationType));
    }
    
    return result;
  }
  
  private fi.pyramus.rest.model.CourseEnrolmentType createCourseEnrolmentTypeRestModel(fi.pyramus.domainmodel.courses.CourseEnrolmentType courseEnrolmentType) {
    return new CourseEnrolmentType(courseEnrolmentType.getId(), courseEnrolmentType.getName());
  }

  private List<fi.pyramus.rest.model.CourseEnrolmentType> createCourseEnrolmentTypeRestModel(List<fi.pyramus.domainmodel.courses.CourseEnrolmentType> courseEnrolmentTypes) {
    List<fi.pyramus.rest.model.CourseEnrolmentType> result = new ArrayList<>();
    
    for (fi.pyramus.domainmodel.courses.CourseEnrolmentType courseEnrolmentType : courseEnrolmentTypes) {
      result.add(createCourseEnrolmentTypeRestModel(courseEnrolmentType));
    }
    
    return result;
  }

  private fi.pyramus.rest.model.CourseState createCourseStateRestModel(CourseState courseState) {
    return new fi.pyramus.rest.model.CourseState(courseState.getId(), courseState.getName(), courseState.getArchived());
  }
  
  private List<fi.pyramus.rest.model.CourseState> createCourseStateRestModel(List<CourseState> courseStates) {
    List<fi.pyramus.rest.model.CourseState> result = new ArrayList<>();
    
    for (CourseState courseState : courseStates) {
      result.add(createCourseStateRestModel(courseState));
    }
    
    return result;
  }
  
  private List<fi.pyramus.rest.model.Course> createCourseRestModel(List<Course> courses) {
    List<fi.pyramus.rest.model.Course> result = new ArrayList<fi.pyramus.rest.model.Course>();
    
    for (Course course : courses) {
      result.add(createCourseRestModel(course));
    }
    
    return result;
  }

  private fi.pyramus.rest.model.Course createCourseRestModel(Course course) {
    Long subjectId = null;
    Subject courseSubject = course.getSubject();
    if (courseSubject != null) {
      subjectId = courseSubject.getId();
    }
    
    List<String> tags = new ArrayList<String>();
    Set<Tag> courseTags = course.getTags();
    if (courseTags != null) {
      for (Tag courseTag : courseTags) {
        tags.add(courseTag.getText());
      }
    }

    Double length = course.getCourseLength() != null ? course.getCourseLength().getUnits() : null;
    Long lengthUnitId = course.getCourseLength() != null ? course.getCourseLength().getUnit().getId() : null;
    
    return new fi.pyramus.rest.model.Course(course.getId(), course.getName(), course.getCreated(), 
        course.getLastModified(), course.getDescription(), course.getArchived(), course.getCourseNumber(), 
        course.getMaxParticipantCount(), course.getBeginDate(), course.getEndDate(), course.getNameExtension(), 
        course.getLocalTeachingDays(), course.getTeachingHours(), course.getDistanceTeachingDays(), 
        course.getAssessingHours(), course.getPlanningHours(), course.getEnrolmentTimeEnd(), course.getCreator().getId(), 
        course.getLastModifier().getId(), subjectId, length, lengthUnitId, course.getModule().getId(), course.getState().getId(), tags);
  }
  
  private fi.pyramus.rest.model.CourseComponent createCourseComponentRestModel(CourseComponent component) {
    return new fi.pyramus.rest.model.CourseComponent(component.getId(), component.getName(), component.getDescription(), component.getLength().getUnits(), component.getLength().getUnit().getId(), component.getArchived());
  }
  
  private List<fi.pyramus.rest.model.CourseComponent> createCourseComponentRestModel(List<CourseComponent> components) {
    List<fi.pyramus.rest.model.CourseComponent> result = new ArrayList<>();
    
    for (CourseComponent component : components) {
      result.add(createCourseComponentRestModel(component));
    }
    
    return result;
  }
  
  private fi.pyramus.rest.model.CourseDescriptionCategory createCourseDescriptionCategoryRestModel(CourseDescriptionCategory category) {
    return new fi.pyramus.rest.model.CourseDescriptionCategory(category.getId(), category.getName(), category.getArchived());
  }
  
  private List<fi.pyramus.rest.model.CourseDescriptionCategory> createCourseDescriptionCategoryRestModel(List<CourseDescriptionCategory> categories) {
    List<fi.pyramus.rest.model.CourseDescriptionCategory> result = new ArrayList<>();
    
    for (CourseDescriptionCategory category : categories) {
      result.add(createCourseDescriptionCategoryRestModel(category)); 
    }
    
    return result;
  }

}
