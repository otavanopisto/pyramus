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

import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.base.Subject;
import fi.pyramus.domainmodel.courses.Course;
import fi.pyramus.domainmodel.courses.CourseState;
import fi.pyramus.domainmodel.modules.Module;
import fi.pyramus.rest.controller.CommonController;
import fi.pyramus.rest.controller.CourseController;
import fi.pyramus.rest.controller.ModuleController;
import fi.pyramus.rest.tranquil.courses.CourseEntity;
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
  
  @Path("/courses")
  @GET
  public Response findCourses(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<Course> courses;
    if (filterArchived) {
      courses = courseController.findUnarchivedCourses();
    } else {
      courses = courseController.findCourses();
    }
    if(!courses.isEmpty()) {
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
  
  @Override
  protected TranquilityBuilderFactory getTranquilityBuilderFactory() {
    return tranquilityBuilderFactory;
  }

}
