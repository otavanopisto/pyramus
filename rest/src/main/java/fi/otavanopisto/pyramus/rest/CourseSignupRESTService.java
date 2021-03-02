package fi.otavanopisto.pyramus.rest;

import java.util.List;
import java.util.Objects;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import fi.otavanopisto.pyramus.dao.courses.CourseSignupStudentGroupDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseSignupStudyProgrammeDAO;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseSignupStudentGroup;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseSignupStudyProgramme;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroup;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.framework.UserUtils;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit;
import fi.otavanopisto.pyramus.rest.controller.CourseController;
import fi.otavanopisto.pyramus.rest.controller.StudentGroupController;
import fi.otavanopisto.pyramus.rest.controller.StudyProgrammeController;
import fi.otavanopisto.pyramus.security.impl.SessionController;
import fi.otavanopisto.pyramus.security.impl.permissions.CourseSignupGroupPermissions;

@Path("/courses")
@Produces("application/json")
@Consumes("application/json")
@Stateful
@RequestScoped
public class CourseSignupRESTService extends AbstractRESTService {

  @Inject
  private CourseController courseController;
  
  @Inject
  private CourseSignupStudentGroupDAO courseSignupStudentGroupDAO;

  @Inject
  private CourseSignupStudyProgrammeDAO courseSignupStudyProgrammeDAO;
  
  @Inject
  private StudyProgrammeController studyProgrammeController;
  
  @Inject
  private StudentGroupController studentGroupController;
  
  @Inject
  private SessionController sessionController;

  @Inject
  private ObjectFactory objectFactory;
  
  @Path("/courses/{COURSEID:[0-9]*}/signupStudyProgrammes")
  @POST
  @RESTPermit (CourseSignupGroupPermissions.CREATE_SIGNUP_STUDYPROGRAMME)
  public Response createSignupStudyProgramme(@PathParam("COURSEID") Long pathCourseId, fi.otavanopisto.pyramus.rest.model.course.CourseSignupStudyProgramme entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    Long courseId = entity.getCourseId();
    Long studyProgrammeId = entity.getStudyProgrammeId();
    
    if (courseId == null || studyProgrammeId == null || !courseId.equals(pathCourseId)) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    Course course = courseController.findCourseById(courseId);
    StudyProgramme studyProgramme = studyProgrammeController.findStudyProgrammeById(studyProgrammeId);

    if (course == null || studyProgramme == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    User loggedUser = sessionController.getUser();
    
    if (UserUtils.canAccessOrganization(loggedUser, course.getOrganization()) && UserUtils.canAccessOrganization(loggedUser, studyProgramme.getOrganization())) {
      return Response.ok(objectFactory.createModel(courseSignupStudyProgrammeDAO.create(course, studyProgramme))).build();
    } else {
      return Response.status(Status.FORBIDDEN).build();
    }
  }

  @Path("/courses/{COURSEID:[0-9]*}/signupStudyProgrammes")
  @GET
  @RESTPermit (CourseSignupGroupPermissions.LIST_SIGNUP_STUDYPROGRAMMES)
  public Response listSignupStudyProgrammes(@PathParam("COURSEID") Long courseId) {
    Course course = courseController.findCourseById(courseId);
    if (course == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    List<CourseSignupStudyProgramme> courseSignupStudyProgrammes = courseSignupStudyProgrammeDAO.listByCourse(course);
    
    return Response.ok(objectFactory.createModel(courseSignupStudyProgrammes)).build();
  }

  @Path("/courses/{COURSEID:[0-9]*}/signupStudyProgrammes/{ID:[0-9]*}")
  @GET
  @RESTPermit (CourseSignupGroupPermissions.FIND_SIGNUP_STUDYPROGRAMME)
  public Response findSignupStudyProgramme(@PathParam("COURSEID") Long pathCourseId, @PathParam("ID") Long id) {
    CourseSignupStudyProgramme signupStudyProgramme = courseSignupStudyProgrammeDAO.findById(id);
    if (signupStudyProgramme == null 
        || !Objects.equals(pathCourseId, signupStudyProgramme.getCourse().getId())
        || !UserUtils.canAccessOrganization(sessionController.getUser(), signupStudyProgramme.getCourse().getOrganization())
        || !UserUtils.canAccessOrganization(sessionController.getUser(), signupStudyProgramme.getStudyProgramme().getOrganization())) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    return Response.ok(objectFactory.createModel(signupStudyProgramme)).build();
  }

  @Path("/courses/{COURSEID:[0-9]*}/signupStudyProgrammes/{ID:[0-9]*}")
  @DELETE
  @RESTPermit (CourseSignupGroupPermissions.DELETE_SIGNUP_STUDYPROGRAMME)
  public Response deleteSignupStudyProgramme(@PathParam("COURSEID") Long pathCourseId, @PathParam("ID") Long id) {
    CourseSignupStudyProgramme signupStudyProgramme = courseSignupStudyProgrammeDAO.findById(id);
    if (signupStudyProgramme == null 
        || !Objects.equals(pathCourseId, signupStudyProgramme.getCourse().getId())
        || !UserUtils.canAccessOrganization(sessionController.getUser(), signupStudyProgramme.getCourse().getOrganization())
        || !UserUtils.canAccessOrganization(sessionController.getUser(), signupStudyProgramme.getStudyProgramme().getOrganization())) {
      return Response.status(Status.NOT_FOUND).build();
    }
  
    courseSignupStudyProgrammeDAO.delete(signupStudyProgramme);
    
    return Response.noContent().build();
  }

  @Path("/courses/{COURSEID:[0-9]*}/signupStudentGroups")
  @POST
  @RESTPermit (CourseSignupGroupPermissions.CREATE_SIGNUP_STUDENTGROUP)
  public Response createSignupStudentGroup(@PathParam("COURSEID") Long pathCourseId, fi.otavanopisto.pyramus.rest.model.course.CourseSignupStudentGroup entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    Long courseId = entity.getCourseId();
    Long studentGroupId = entity.getStudentGroupId();
    
    if (courseId == null || studentGroupId == null || !courseId.equals(pathCourseId)) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    Course course = courseController.findCourseById(courseId);
    StudentGroup studentGroup = studentGroupController.findStudentGroupById(studentGroupId);

    if (course == null || studentGroup == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    User loggedUser = sessionController.getUser();
    
    if (UserUtils.canAccessOrganization(loggedUser, course.getOrganization()) && UserUtils.canAccessOrganization(loggedUser, studentGroup.getOrganization())) {
      return Response.ok(objectFactory.createModel(courseSignupStudentGroupDAO.create(course, studentGroup))).build();
    } else {
      return Response.status(Status.FORBIDDEN).build();
    }
  }

  @Path("/courses/{COURSEID:[0-9]*}/signupStudentGroups")
  @GET
  @RESTPermit (CourseSignupGroupPermissions.LIST_SIGNUP_STUDENTGROUPS)
  public Response listSignupStudentGroups(@PathParam("COURSEID") Long courseId) {
    Course course = courseController.findCourseById(courseId);
    if (course == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    List<CourseSignupStudentGroup> courseSignupStudentGroups = courseSignupStudentGroupDAO.listByCourse(course);
    
    return Response.ok(objectFactory.createModel(courseSignupStudentGroups)).build();
  }
  
  @Path("/courses/{COURSEID:[0-9]*}/signupStudentGroups/{ID:[0-9]*}")
  @GET
  @RESTPermit (CourseSignupGroupPermissions.FIND_SIGNUP_STUDENTGROUP)
  public Response findSignupStudentGroup(@PathParam("COURSEID") Long pathCourseId, @PathParam("ID") Long id) {
    CourseSignupStudentGroup signupStudentGroup = courseSignupStudentGroupDAO.findById(id);
    if (signupStudentGroup == null 
        || !Objects.equals(pathCourseId, signupStudentGroup.getCourse().getId())
        || !UserUtils.canAccessOrganization(sessionController.getUser(), signupStudentGroup.getCourse().getOrganization())
        || !UserUtils.canAccessOrganization(sessionController.getUser(), signupStudentGroup.getStudentGroup().getOrganization())) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    return Response.ok(objectFactory.createModel(signupStudentGroup)).build();
  }

  @Path("/courses/{COURSEID:[0-9]*}/signupStudentGroups/{ID:[0-9]*}")
  @DELETE
  @RESTPermit (CourseSignupGroupPermissions.DELETE_SIGNUP_STUDENTGROUP)
  public Response deleteSignupStudentGroup(@PathParam("COURSEID") Long pathCourseId, @PathParam("ID") Long id) {
    CourseSignupStudentGroup signupStudentGroup = courseSignupStudentGroupDAO.findById(id);
    if (signupStudentGroup == null 
        || !Objects.equals(pathCourseId, signupStudentGroup.getCourse().getId())
        || !UserUtils.canAccessOrganization(sessionController.getUser(), signupStudentGroup.getCourse().getOrganization())
        || !UserUtils.canAccessOrganization(sessionController.getUser(), signupStudentGroup.getStudentGroup().getOrganization())) {
      return Response.status(Status.NOT_FOUND).build();
    }
  
    courseSignupStudentGroupDAO.delete(signupStudentGroup);
    
    return Response.noContent().build();
  }

}
