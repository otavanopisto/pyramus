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

import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.base.Subject;
import fi.pyramus.domainmodel.base.Tag;
import fi.pyramus.domainmodel.courses.Course;
import fi.pyramus.domainmodel.courses.CourseDescriptionCategory;
import fi.pyramus.domainmodel.courses.CourseState;
import fi.pyramus.domainmodel.modules.Module;
import fi.pyramus.rest.controller.CommonController;
import fi.pyramus.rest.controller.CourseController;
import fi.pyramus.rest.controller.ModuleController;
import fi.pyramus.rest.controller.TagController;
import fi.pyramus.rest.tranquil.base.TagEntity;
import fi.pyramus.rest.tranquil.courses.CourseDescriptionCategoryEntity;
import fi.pyramus.rest.tranquil.courses.CourseEntity;
import fi.pyramus.rest.tranquil.courses.CourseStateEntity;
import fi.tranquil.TranquilityBuilderFactory;

@Path("/courses")
@Produces("application/json")
@Consumes("application/json")
@Stateful
@RequestScoped
public class CourseRESTService extends AbstractRESTService {
  @Inject
  TranquilityBuilderFactory tranquilityBuilderFactory;
  @Inject
  CourseController courseController;
  @Inject
  ModuleController moduleController;
  @Inject
  CommonController commonController;
  @Inject
  TagController tagController;
  
  @Path("/courses")
  @POST
  public Response createCourse(CourseEntity courseEntity) {
    try{
      Module module = moduleController.findModuleById(courseEntity.getModule_id());
      String name = courseEntity.getName();
      String nameExtension = courseEntity.getNameExtension();
      CourseState state = courseController.findCourseStateById(courseEntity.getState_id());
      Subject subject = commonController.findSubjectById(courseEntity.getSubject_id());
      Integer courseNumber = courseEntity.getCourseNumber();
      Date beginDate = courseEntity.getBeginDate();
      Date endDate = courseEntity.getEndDate();
      EducationalTimeUnit courseLengthTimeUnit = commonController.findEducationalTimeUnitById(courseEntity.getCourseLength_id());
      Double courseLength = courseEntity.getCourseLength();
      Double distanceTeachingDays = courseEntity.getDistanceTeachingDays();
      Double localTeachingDays = courseEntity.getLocalTeachingDays();
      Double teachingHours = courseEntity.getTeachingHours();
      Double planningHours = courseEntity.getPlanningHours();
      Double assessingHours = courseEntity.getAssessingHours();
      String description = courseEntity.getDescription();
      Long maxParticipantCount = courseEntity.getMaxParticipantCount();
      Date enrolmentTimeEnd = courseEntity.getEnrolmentTimeEnd();
      
      return Response
          .ok()
          .entity(tranqualise(courseController.createCourse(module, name, nameExtension, state, subject, courseNumber, beginDate, endDate, courseLength,
                  courseLengthTimeUnit, distanceTeachingDays, localTeachingDays, teachingHours, planningHours, assessingHours, description, maxParticipantCount,
                  enrolmentTimeEnd, getUser())))
                  .build();
    } catch(NullPointerException e) {
      return Response.status(500).build();
    }
  }
  
  @Path("/descriptionCategories")
  @POST
  public Response createCourseDescriptionCategory(CourseDescriptionCategoryEntity categoryEntity) {
    String name = categoryEntity.getName();
    if (!StringUtils.isBlank(name)) {
      return Response.ok()
          .entity(tranqualise(courseController.createCourseDescriptionCategory(name)))
          .build();
    } else {
      return Response.status(500).build();
    }
  }
  
  @Path("/courseStates")
  @POST
  public Response createCourseState(CourseStateEntity courseStateEntity) {
    String name = courseStateEntity.getName();
    if (!StringUtils.isBlank(name)) {
      return Response.ok()
          .entity(tranqualise(courseController.createCourseState(name)))
          .build();
    } else {
      return Response.status(500).build();
    }
  }
  
  @Path("/courses/{ID:[0-9]*}/tags")
  @POST
  public Response createCourseTag(@PathParam("ID") Long id, TagEntity tagEntity) {
    Course course = courseController.findCourseById(id);
    String text = tagEntity.getText();
    if (course != null && !StringUtils.isBlank(text)) {
      return Response.ok()
          .entity(tranqualise(courseController.createCourseTag(course, text)))
          .build();
    } else {
      return Response.status(500).build();
    }
  }
  
  @Path("/courses")
  @GET
  public Response findCourses(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<Course> courses;
    if (filterArchived) {
      courses = courseController.findUnarchivedCourses();
    } else {
      courses = courseController.findCourses();
    }
    if (!courses.isEmpty()) {
      return Response.ok()
          .entity(tranqualise(courses))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/courses/{ID:[0-9]*}")
  @GET
  public Response findCourseById(@PathParam("ID") Long id) {
    Course course = courseController.findCourseById(id);
    if (course != null) {
      return Response.ok()
          .entity(tranqualise(course))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/descriptionCategories")
  @GET
  public Response findCourseDescriptionCategories(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<CourseDescriptionCategory> categories;
    if (filterArchived) {
      categories = courseController.findUnarchivedCourseDescriptionCategories();
    } else {
      categories = courseController.findCourseDescriptionCategories();
    }
    if (!categories.isEmpty()) {
      return Response.ok()
          .entity(tranqualise(categories))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/descriptionCategories/{ID:[0-9]*}")
  @GET
  public Response findCourseDescriptionCategoryById(@PathParam("ID") Long id) {
    CourseDescriptionCategory category = courseController.findCourseDescriptionCategoryById(id);
    if (category != null) {
      return Response.ok()
          .entity(tranqualise(category))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/courseStates")
  @GET
  public Response findCourseStates(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<CourseState> courseStates;
    if (filterArchived) {
      courseStates = courseController.findUnarchivedCourseStates();
    } else {
      courseStates = courseController.findCourseStates();
    }
    if (!courseStates.isEmpty()) {
      return Response.ok()
          .entity(tranqualise(courseStates))
          .build();
    } else {
      return Response.status(500).build();
    }
  }
  
  @Path("/courseStates/{ID:[0-9]*}")
  @GET
  public Response findCourseStateById(@PathParam("ID") Long id) {
    CourseState courseState = courseController.findCourseStateById(id);
    if (courseState != null) {
      return Response.ok()
          .entity(tranqualise(courseState))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/courses/{ID:[0-9]*}/tags")
  @GET
  public Response findCourseTags(@PathParam("ID") Long id) {
    Course course = courseController.findCourseById(id);
    if (course != null) {
      return Response.ok()
          .entity(tranqualise(courseController.findCourseTags(course)))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/courses/{ID:[0-9]*}")
  @PUT
  public Response updateCourse(@PathParam("ID") Long id, CourseEntity courseEntity) {
    Course course = courseController.findCourseById(id);
    if (course != null) {
      try {
        if (courseEntity.getArchived() != null) {
          if(!courseEntity.getArchived()) {
            return Response.ok()
                .entity(tranqualise(courseController.unarchiveCourse(course, getUser())))
                .build();
          } else {
            return Response.status(500).build();
          } 
        } else {
          String name = courseEntity.getName();
          String nameExtension = courseEntity.getNameExtension();
          CourseState courseState = courseController.findCourseStateById(courseEntity.getState_id());
          Subject subject = commonController.findSubjectById(courseEntity.getSubject_id());
          Integer courseNumber = courseEntity.getCourseNumber();
          Date beginDate = courseEntity.getBeginDate();
          Date endDate = courseEntity.getEndDate();
          EducationalTimeUnit courseLengthTimeUnit = commonController.findEducationalTimeUnitById(courseEntity.getCourseLength_id());
          Double courseLength = courseEntity.getCourseLength();
          Double distanceTeachingDays = courseEntity.getDistanceTeachingDays();
          Double localTeachingDays = courseEntity.getLocalTeachingDays();
          Double teachingHours = courseEntity.getTeachingHours();
          Double planningHours = courseEntity.getPlanningHours();
          Double assessingHours = courseEntity.getAssessingHours();
          String description = courseEntity.getDescription();
          Long maxParticipantCount = courseEntity.getMaxParticipantCount();
          Date enrolmentTimeEnd = courseEntity.getEnrolmentTimeEnd();
          return Response.ok()
              .entity(
                  tranqualise(courseController.updateCourse(course, name, nameExtension, courseState, subject, courseNumber, beginDate, endDate, courseLength,
                  courseLengthTimeUnit, distanceTeachingDays, localTeachingDays, teachingHours, planningHours, assessingHours, description,
                  maxParticipantCount, enrolmentTimeEnd, getUser())))
              .build();
        }
      } catch (Exception e) {
        return Response.status(500).build();
      }
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/descriptionCategories/{ID:[0-9]*}")
  @PUT
  public Response updateCourseDescriptionCategories(@PathParam("ID") Long id, CourseDescriptionCategoryEntity categoryEntity) {
    CourseDescriptionCategory category = courseController.findCourseDescriptionCategoryById(id);
    if (category != null) {
      String name = categoryEntity.getName();
      if (!StringUtils.isBlank(name)) {
        return Response.ok()
            .entity(tranqualise(courseController.updateCourseDescriptionCategory(category, name)))
            .build();
      } else if (categoryEntity.getArchived() != null) {
        if (!categoryEntity.getArchived()) {
          return Response.ok()
              .entity(tranqualise(courseController.unarchiveCourseDescriptionCategory(category, getUser())))
              .build();
        }
      }
      return Response.status(500).build();
    }
    return Response.status(Status.NOT_FOUND).build();
  }
  
  @Path("/courseStates/{ID:[0-9]*}")
  @PUT
  public Response updateCourseState(@PathParam("ID") Long id, CourseStateEntity courseStateEntity) {
    CourseState courseState = courseController.findCourseStateById(id);
    if (courseState != null) {
      String name = courseStateEntity.getName();
      if (!StringUtils.isBlank(name)) {
        return Response.ok()
            .entity(tranqualise(courseController.updateCourseState(courseState, name)))
            .build();
      } else if (courseStateEntity.getArchived() != null) {
        if (!courseStateEntity.getArchived()) {
          return Response.ok()
              .entity(tranqualise(courseController.unarchiveCourseState(courseState, getUser())))
              .build();
        }
      }
      return Response.status(500).build();
    }
    return Response.status(Status.NOT_FOUND).build();
  }
  
  @Path("/courses/{ID:[0-9]*}")
  @DELETE
  public Response archiveCourse(@PathParam("ID") Long id) {
    Course course = courseController.findCourseById(id);
    if (course != null) {
      return Response.ok()
          .entity(tranqualise(courseController.archiveCourse(course, getUser())))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/descriptionCategories/{ID:[0-9]*}")
  @DELETE
  public Response arciveCourseDescriptionCategory(@PathParam("ID") Long id) {
    CourseDescriptionCategory category = courseController.findCourseDescriptionCategoryById(id);
    if (category != null) {
      return Response.ok()
          .entity(tranqualise(courseController.archiveCourseDescriptionCategory(category, getUser())))
          .build();
    } else {
      return Response.status(500).build();
    }
  }
  
  @Path("/courseStates/{ID:[0-9]*}") 
  @DELETE
  public Response archiveCourseState(@PathParam("ID") Long id) {
    CourseState courseState = courseController.findCourseStateById(id);
    if (courseState != null) {
      return Response.ok()
          .entity(tranqualise(courseController.archiveCourseState(courseState, getUser())))
          .build();
    } else {
      return Response.status(500).build();
    } 
  }
  
  @Path("/courses/{CID:[0-9]*}/tags/{ID:[0-9]*}")
  @DELETE
  public Response removeTag(@PathParam("CID") Long courseId, @PathParam("ID") Long tagId) {
    Course course = courseController.findCourseById(courseId);
    Tag tag = tagController.findTagById(tagId);
    if (course != null && tag != null) {
      courseController.removeCourseTag(course, tag);
      return Response.status(200).build();
    } else {
      return Response.status(500).build();
    }
  }
  
  @Override
  protected TranquilityBuilderFactory getTranquilityBuilderFactory() {
    return tranquilityBuilderFactory;
  }

}
