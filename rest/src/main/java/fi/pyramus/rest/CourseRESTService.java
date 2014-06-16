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
import fi.pyramus.domainmodel.courses.CourseState;
import fi.pyramus.domainmodel.modules.Module;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.rest.controller.CommonController;
import fi.pyramus.rest.controller.CourseController;
import fi.pyramus.rest.controller.ModuleController;
import fi.pyramus.rest.controller.TagController;

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
  
  @Inject
  private TagController tagController;
  
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
    
    return Response.ok().entity(createCourseRestModel(courseController.createCourse(module, name, nameExtension, state, subject, courseNumber, 
          beginDate, endDate, courseLength, courseLengthTimeUnit, distanceTeachingDays, localTeachingDays, teachingHours, 
          planningHours, assessingHours, description, maxParticipantCount, enrolmentTimeEnd, loggedUser))).build();
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
    
    List<CourseComponent> courseComponents = courseController.findCourseComponentsByCourse(course);
    if (!courseComponents.contains(courseComponent)) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (permanent) {
      courseController.deleteCourseComponent(courseComponent);
    } else {
      courseController.archiveCourseComponent(courseComponent, getLoggedUser());
    }
    
    return Response.status(Status.NO_CONTENT).build();
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
  
  
  

//  
//  @Path("/descriptionCategories")
//  @POST
//  public Response createCourseDescriptionCategory(CourseDescriptionCategoryEntity categoryEntity) {
//    String name = categoryEntity.getName();
//    if (!StringUtils.isBlank(name)) {
//      return Response.ok()
//          .entity(tranqualise(courseController.createCourseDescriptionCategory(name)))
//          .build();
//    } else {
//      return Response.status(500).build();
//    }
//  }
//  
//  @Path("/enrolmentTypes")
//  @POST
//  public Response createCourseEnrolmentType(CourseEnrolmentTypeEntity courseEnrolmentTypeEntity) {
//    String name = courseEnrolmentTypeEntity.getName();
//    if (!StringUtils.isBlank(name)) {
//      return Response.ok()
//          .entity(tranqualise(courseController.createCourseEnrolmentType(name)))
//          .build();
//    } else {
//      return Response.status(500).build();
//    }
//  }
//  
//  @Path("/courseStates")
//  @POST
//  public Response createCourseState(CourseStateEntity courseStateEntity) {
//    String name = courseStateEntity.getName();
//    if (!StringUtils.isBlank(name)) {
//      return Response.ok()
//          .entity(tranqualise(courseController.createCourseState(name)))
//          .build();
//    } else {
//      return Response.status(500).build();
//    }
//  }
//  
//  @Path("/participationTypes")
//  @POST
//  public Response createCourseParticipationType(CourseParticipationTypeEntity participationTypeEntity) {
//    String name = participationTypeEntity.getName();
//    if (!StringUtils.isBlank(name)) {
//      return Response.ok()
//          .entity(tranqualise(courseController.createCourseParticipationType(name)))
//          .build();
//    } else {
//      return Response.status(500).build();
//    }
//  }
//  
//  @Path("/courses/{ID:[0-9]*}/tags")
//  @POST
//  public Response createCourseTag(@PathParam("ID") Long id, TagEntity tagEntity) {
//    Course course = courseController.findCourseById(id);
//    String text = tagEntity.getText();
//    if (course != null && !StringUtils.isBlank(text)) {
//      return Response.ok()
//          .entity(tranqualise(courseController.createCourseTag(course, text)))
//          .build();
//    } else {
//      return Response.status(500).build();
//    }
//  }
//  
//  @Path("/descriptionCategories")
//  @GET
//  public Response findCourseDescriptionCategories(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
//    List<CourseDescriptionCategory> categories;
//    if (filterArchived) {
//      categories = courseController.findUnarchivedCourseDescriptionCategories();
//    } else {
//      categories = courseController.findCourseDescriptionCategories();
//    }
//    if (!categories.isEmpty()) {
//      return Response.ok()
//          .entity(tranqualise(categories))
//          .build();
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//  
//  @Path("/descriptionCategories/{ID:[0-9]*}")
//  @GET
//  public Response findCourseDescriptionCategoryById(@PathParam("ID") Long id) {
//    CourseDescriptionCategory category = courseController.findCourseDescriptionCategoryById(id);
//    if (category != null) {
//      return Response.ok()
//          .entity(tranqualise(category))
//          .build();
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//  
//  @Path("/enrolmentTypes")
//  @GET
//  public Response findCourseEnrolmentTypes() {
//    List<CourseEnrolmentType> courseEnrolmentTypes = courseController.findCourseEnrolmentTypes();
//    if (!courseEnrolmentTypes.isEmpty()) {
//      return Response.ok()
//          .entity(tranqualise(courseEnrolmentTypes))
//          .build();
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//  
//  @Path("/enrolmentTypes/{ID:[0-9]*}")
//  @GET
//  public Response findCourseEnrolmentTypeById(@PathParam("ID") Long id) {
//    CourseEnrolmentType enrolmentType = courseController.findCourseEnrolmentTypeById(id);
//    if (enrolmentType != null) {
//      return Response.ok()
//          .entity(tranqualise(enrolmentType))
//          .build();
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//  
//  @Path("/courseStates")
//  @GET
//  public Response findCourseStates(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
//    List<CourseState> courseStates;
//    if (filterArchived) {
//      courseStates = courseController.findUnarchivedCourseStates();
//    } else {
//      courseStates = courseController.findCourseStates();
//    }
//    if (!courseStates.isEmpty()) {
//      return Response.ok()
//          .entity(tranqualise(courseStates))
//          .build();
//    } else {
//      return Response.status(500).build();
//    }
//  }
//  
//  @Path("/courseStates/{ID:[0-9]*}")
//  @GET
//  public Response findCourseStateById(@PathParam("ID") Long id) {
//    CourseState courseState = courseController.findCourseStateById(id);
//    if (courseState != null) {
//      return Response.ok()
//          .entity(tranqualise(courseState))
//          .build();
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//  
//  @Path("/participationTypes")
//  @GET
//  public Response findCourseParticipationTypes(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
//    List<CourseParticipationType> participationTypes;
//    if (filterArchived) {
//      participationTypes = courseController.findUnarchivedCourseParticiPationTypes();
//    } else {
//      participationTypes = courseController.findCourseParticiPationTypes();
//    }
//    if (!participationTypes.isEmpty()) {
//      return Response.ok()
//          .entity(tranqualise(participationTypes))
//          .build();
//    } else {
//      return Response.status(500).build();
//    }
//  }
//  
//  @Path("/participationTypes/{ID:[0-9]*}")
//  @GET
//  public Response findCourseParticipationTypeById(@PathParam("ID") Long id) {
//    CourseParticipationType participationType = courseController.findCourseParticipationTypeById(id);
//    if (participationType != null) {
//      return Response.ok()
//          .entity(tranqualise(participationType))
//          .build();
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//  
//  @Path("/courses/{ID:[0-9]*}/tags")
//  @GET
//  public Response findCourseTags(@PathParam("ID") Long id) {
//    Course course = courseController.findCourseById(id);
//    if (course != null) {
//      return Response.ok()
//          .entity(tranqualise(courseController.findCourseTags(course)))
//          .build();
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//  
//  @Path("/courses/{CID:[0-9]*}/components/{ID:[0-9]*}")
//  @PUT
//  public Response updateCourseComponent(@PathParam("CID") Long courseId, @PathParam("ID") Long componentId, CourseComponentEntity componentEntity) {
//    try {
//      CourseComponent component = courseController.findCourseComponentById(componentId);
//      if (component.getCourse().getId().equals(courseId)) {
//        if (componentEntity.getArchived() != null) {
//          if (!componentEntity.getArchived()) {
//            return Response.ok()
//                .entity(tranqualise(courseController.unarchiveCourseComponent(component, getUser())))
//                .build();
//          } else {
//            return Response.status(500).build();
//          }
//        } else {
//          Double length = componentEntity.getLength();
//          EducationalTimeUnit lengthTimeUnit = commonController.findEducationalTimeUnitById(componentEntity.getLength_id());
//          String name = componentEntity.getName();
//          String description = componentEntity.getDescription();
//          if (length != null && lengthTimeUnit != null && !StringUtils.isBlank(name) && !StringUtils.isBlank(description)) {
//            return Response.ok()
//                .entity(tranqualise(courseController.updateCourseComponent(component, length, lengthTimeUnit, name, description)))
//                .build();
//          } else {
//            return Response.status(500).build();
//          }
//        }
//      } else {
//        return Response.status(Status.NOT_FOUND).build();
//      }
//    } catch (NullPointerException e) {
//      return Response.status(500).build();
//    }
//  }
//  
//  @Path("/descriptionCategories/{ID:[0-9]*}")
//  @PUT
//  public Response updateCourseDescriptionCategories(@PathParam("ID") Long id, CourseDescriptionCategoryEntity categoryEntity) {
//    CourseDescriptionCategory category = courseController.findCourseDescriptionCategoryById(id);
//    if (category != null) {
//      String name = categoryEntity.getName();
//      if (!StringUtils.isBlank(name)) {
//        return Response.ok()
//            .entity(tranqualise(courseController.updateCourseDescriptionCategory(category, name)))
//            .build();
//      } else if (categoryEntity.getArchived() != null) {
//        if (!categoryEntity.getArchived()) {
//          return Response.ok()
//              .entity(tranqualise(courseController.unarchiveCourseDescriptionCategory(category, getUser())))
//              .build();
//        }
//      }
//      return Response.status(500).build();
//    }
//    return Response.status(Status.NOT_FOUND).build();
//  }
//  
//  @Path("/enrolmentTypes/{ID:[0-9]*}")
//  @PUT
//  public Response updateCourseEnrolmentType(@PathParam("ID") Long id, CourseEnrolmentTypeEntity courseEnrolmentTypeEntity) {
//    CourseEnrolmentType enrolmentType = courseController.findCourseEnrolmentTypeById(id);
//    if (enrolmentType != null) {
//      String name = courseEnrolmentTypeEntity.getName();
//      if (!StringUtils.isBlank(name)) {
//      return Response.ok()
//          .entity(tranqualise(courseController.updateCourseEnrolmentType(enrolmentType, name)))
//          .build();
//      } else {
//        return Response.status(500).build();
//      }
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//  
//  @Path("/courseStates/{ID:[0-9]*}")
//  @PUT
//  public Response updateCourseState(@PathParam("ID") Long id, CourseStateEntity courseStateEntity) {
//    CourseState courseState = courseController.findCourseStateById(id);
//    if (courseState != null) {
//      String name = courseStateEntity.getName();
//      if (!StringUtils.isBlank(name)) {
//        return Response.ok()
//            .entity(tranqualise(courseController.updateCourseState(courseState, name)))
//            .build();
//      } else if (courseStateEntity.getArchived() != null) {
//        if (!courseStateEntity.getArchived()) {
//          return Response.ok()
//              .entity(tranqualise(courseController.unarchiveCourseState(courseState, getUser())))
//              .build();
//        }
//      }
//      return Response.status(500).build();
//    }
//    return Response.status(Status.NOT_FOUND).build();
//  }
//  
//  @Path("/participationTypes/{ID:[0-9]*}")
//  @PUT
//  public Response updateCourseParticipationType(@PathParam("ID") Long id, CourseParticipationTypeEntity participationTypeEntity) {
//    CourseParticipationType participationType = courseController.findCourseParticipationTypeById(id);
//    if (participationType != null) {
//      String name = participationTypeEntity.getName();
//      if (!StringUtils.isBlank(name)) {
//        return Response.ok()
//            .entity(tranqualise(courseController.updateCourseParticipationType(participationType, name)))
//            .build();
//      } else if (participationTypeEntity.getArchived() != null) {
//        if (!participationTypeEntity.getArchived()) {
//          return Response.ok()
//              .entity(tranqualise(courseController.unarchiveCourseParticipationType(participationType, getUser())))
//              .build();
//        }
//      }
//      return Response.status(500).build();
//    }
//    return Response.status(Status.NOT_FOUND).build();
//  }
//  
//  @Path("/descriptionCategories/{ID:[0-9]*}")
//  @DELETE
//  public Response arciveCourseDescriptionCategory(@PathParam("ID") Long id) {
//    CourseDescriptionCategory category = courseController.findCourseDescriptionCategoryById(id);
//    if (category != null) {
//      return Response.ok()
//          .entity(tranqualise(courseController.archiveCourseDescriptionCategory(category, getUser())))
//          .build();
//    } else {
//      return Response.status(500).build();
//    }
//  }
//  
//  @Path("/courseStates/{ID:[0-9]*}") 
//  @DELETE
//  public Response archiveCourseState(@PathParam("ID") Long id) {
//    CourseState courseState = courseController.findCourseStateById(id);
//    if (courseState != null) {
//      return Response.ok()
//          .entity(tranqualise(courseController.archiveCourseState(courseState, getUser())))
//          .build();
//    } else {
//      return Response.status(500).build();
//    } 
//  }
//  
//  @Path("/participationTypes/{ID:[0-9]*}")
//  @DELETE
//  public Response archiveCourseParticipationType(@PathParam("ID") Long id) {
//    CourseParticipationType participationType = courseController.findCourseParticipationTypeById(id);
//    if (participationType != null) {
//      return Response.ok()
//          .entity(tranqualise(courseController.archiveCourseParticipationType(participationType, getUser())))
//          .build();
//    } else {
//      return Response.status(500).build();
//    }
//  }
//  
//  @Path("/courses/{CID:[0-9]*}/tags/{ID:[0-9]*}")
//  @DELETE
//  public Response removeTag(@PathParam("CID") Long courseId, @PathParam("ID") Long tagId) {
//    Course course = courseController.findCourseById(courseId);
//    Tag tag = tagController.findTagById(tagId);
//    if (course != null && tag != null) {
//      courseController.removeCourseTag(course, tag);
//      return Response.status(200).build();
//    } else {
//      return Response.status(500).build();
//    }
//  }

}
