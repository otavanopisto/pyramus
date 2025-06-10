package fi.otavanopisto.pyramus.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.ejb.Stateful;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.CacheControl;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.EntityTag;
import jakarta.ws.rs.core.Request;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import jakarta.ws.rs.core.Response.Status;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.domainmodel.base.Email;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessmentRequest;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.StudentParent;
import fi.otavanopisto.pyramus.domainmodel.users.StudentParentChild;
import fi.otavanopisto.pyramus.domainmodel.users.StudentParentInvitation;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit.Handling;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit.Style;
import fi.otavanopisto.pyramus.rest.controller.AssessmentController;
import fi.otavanopisto.pyramus.rest.controller.CourseController;
import fi.otavanopisto.pyramus.rest.controller.StudentController;
import fi.otavanopisto.pyramus.rest.controller.StudentParentController;
import fi.otavanopisto.pyramus.rest.controller.permissions.UserPermissions;
import fi.otavanopisto.pyramus.rest.model.students.StudentParentStudentCourseRestModel;
import fi.otavanopisto.pyramus.rest.security.RESTSecurity;

@Path("/studentparents")
@Produces("application/json")
@Consumes("application/json")
@Stateful
@RequestScoped
public class StudentParentRESTService extends AbstractRESTService {

  @Inject
  private Logger logger;
  
  @Inject
  private HttpServletRequest httpRequest;
  
  @Inject
  private AssessmentController assessmentController;

  @Inject
  private CourseController courseController;

  @Inject
  private StudentController studentController;

  @Inject
  private StudentParentController studentParentController;

  @Inject
  private ObjectFactory objectFactory;

  @Inject
  private RESTSecurity restSecurity;

  @Path("/students/{STUDENTID:[0-9]*}/invitations/{INVITATIONID:[0-9]*}/refresh")
  @POST
  @RESTPermit (UserPermissions.MANAGE_STUDENTPARENT_INVITATIONS)
  public Response refreshInvitation(@PathParam("STUDENTID") Long studentId, @PathParam("INVITATIONID") Long invitationId) {
    Student student = studentController.findStudentById(studentId);
    StudentParentInvitation invitation = studentParentController.findInvitationById(invitationId);
    
    if (student != null && invitation != null && invitation.getStudent().getId().equals(student.getId())) {
      try {
        invitation = studentParentController.refreshInvitation(invitation);
        studentParentController.sendInvitationEmail(invitation, httpRequest);
        return Response.noContent().build();
      }
      catch (Exception ex) {
        logger.log(Level.SEVERE, "Refreshing invitation failed.", ex);
        return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
    }
    else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/students/{STUDENTID:[0-9]*}/invitations/{INVITATIONID:[0-9]*}")
  @DELETE
  @RESTPermit (UserPermissions.MANAGE_STUDENTPARENT_INVITATIONS)
  public Response deleteInvitation(@PathParam("STUDENTID") Long studentId, @PathParam("INVITATIONID") Long invitationId) {
    Student student = studentController.findStudentById(studentId);
    StudentParentInvitation invitation = studentParentController.findInvitationById(invitationId);
    
    if (student != null && invitation != null && invitation.getStudent().getId().equals(student.getId())) {
      studentParentController.deleteInvitation(invitation);
      return Response.noContent().build();
    }
    else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/studentparents")
  @GET
  @RESTPermit (UserPermissions.LIST_STUDENTPARENTS)
  public Response listStaffMembers(@QueryParam ("firstResult") Integer firstResult, @QueryParam ("maxResults") Integer maxResults, @QueryParam ("email") String email) {
    List<StudentParent> studentParents;
    
    if (StringUtils.isNotBlank(email)) {
      studentParents = new ArrayList<>();
      StudentParent studentParent = studentParentController.findStudentParentByEmail(email);
      if (studentParent != null) {
        studentParents.add(studentParent);
      }
    } else {
      studentParents = studentParentController.listStudentParents(firstResult, maxResults);
    }
    
    return Response.ok(objectFactory.createModel(studentParents)).build();
  }
  
  @Path("/studentparents/{ID:[0-9]*}")
  @GET
  @RESTPermit (handling = Handling.INLINE)
  public Response findStaffMemberById(@PathParam("ID") Long id, @Context Request request) {
    StudentParent studentParent = studentParentController.findStudentParentById(id);
    
    if (studentParent == null || studentParent.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (!restSecurity.hasPermission(new String[] { UserPermissions.FIND_STUDENTPARENT, UserPermissions.USER_OWNER }, studentParent, Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }

    EntityTag tag = new EntityTag(DigestUtils.md5Hex(String.valueOf(studentParent.getVersion())));
    ResponseBuilder builder = request.evaluatePreconditions(tag);
    if (builder != null) {
      return builder.build();
    }

    CacheControl cacheControl = new CacheControl();
    cacheControl.setMustRevalidate(true);
    
    return Response
        .ok(objectFactory.createModel(studentParent))
        .cacheControl(cacheControl)
        .tag(tag)
        .build();
  }

  @Path("/studentparents/{ID:[0-9]*}/emails")
  @GET
  @RESTPermit (handling = Handling.INLINE)
  public Response listStaffMembersEmails(@PathParam("ID") Long id) {
    StudentParent studentParent = studentParentController.findStudentParentById(id);
    if (studentParent == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (!restSecurity.hasPermission(new String[] { UserPermissions.LIST_STUDENTPARENT_EMAILS, UserPermissions.USER_OWNER }, studentParent, Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }

    List<Email> emails = studentParent.getContactInfo().getEmails();
    return Response.ok(objectFactory.createModel(emails)).build();
  }

  @Path("/studentparents/{ID:[0-9]*}/students")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response listStudentParentStudents(@PathParam("ID") Long id) {
    StudentParent studentParent = studentParentController.findStudentParentById(id);
    if (studentParent == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (!restSecurity.hasPermission(new String[] { UserPermissions.LIST_STUDENTPARENT_STUDENTS, UserPermissions.USER_OWNER }, studentParent, Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }

    return Response.ok(objectFactory.createModel(studentParent.getActiveChildren())).build();
  }
  
  @Path("/studentparents/{ID:[0-9]*}/students/{STUDENTID:[0-9]*}")
  @DELETE
  @RESTPermit (UserPermissions.DETACH_STUDENTPARENT)
  public Response detachStudentParentFromStudent(
      @PathParam("ID") Long id, @PathParam("STUDENTID") Long studentId) {
    StudentParent studentParent = studentParentController.findStudentParentById(id);
    if (studentParent == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    Student student = studentController.findStudentById(studentId);
    if (student == null || !studentParent.isActiveParentOf(student)) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    StudentParentChild studentParentChild = studentParentController.findStudentParentChild(studentParent, student);
    if (studentParentChild == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    studentParentController.deleteStudentParentChild(studentParentChild);
    
    return Response.noContent().build();
  }
  
  @Path("/studentparents/{ID:[0-9]*}/students/{STUDENTID:[0-9]*}/courses")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response listStudentParentStudentsCourses(
      @PathParam("ID") Long id, @PathParam("STUDENTID") Long studentId) {
    StudentParent studentParent = studentParentController.findStudentParentById(id);
    if (studentParent == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    Student student = studentController.findStudentById(studentId);
    if (student == null || !studentParent.isActiveParentOf(student)) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (!restSecurity.hasPermission(new String[] { UserPermissions.LIST_STUDENTPARENT_STUDENTS, UserPermissions.USER_OWNER }, studentParent, Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }

    List<StudentParentStudentCourseRestModel> results = new ArrayList<>();
    List<CourseStudent> courseStudents = courseController.listCourseStudentsByStudent(student);
    for (CourseStudent courseStudent : courseStudents) {
      CourseAssessmentRequest latestAssessmentRequest = assessmentController.findLatestCourseAssessmentRequestByCourseStudent(courseStudent);
      
      results.add(new StudentParentStudentCourseRestModel(
          courseStudent.getCourse().getId(),
          courseStudent.getId(),
          courseStudent.getCourse().getName(),
          courseStudent.getCourse().getNameExtension(),
          courseStudent.getEnrolmentTime(),
          latestAssessmentRequest != null ? latestAssessmentRequest.getCreated() : null
      ));
    }
    
    return Response.ok(results).build();
  }
  
}