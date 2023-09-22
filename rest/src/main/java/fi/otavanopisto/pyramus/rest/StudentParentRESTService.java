package fi.otavanopisto.pyramus.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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

import fi.otavanopisto.pyramus.domainmodel.base.Email;
import fi.otavanopisto.pyramus.domainmodel.users.StudentParent;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit.Handling;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit.Style;
import fi.otavanopisto.pyramus.rest.controller.UserController;
import fi.otavanopisto.pyramus.rest.controller.permissions.UserPermissions;
import fi.otavanopisto.pyramus.rest.security.RESTSecurity;

@Path("/studentparents")
@Produces("application/json")
@Consumes("application/json")
@Stateful
@RequestScoped
public class StudentParentRESTService extends AbstractRESTService {

  @Inject
  private UserController userController;

  @Inject
  private ObjectFactory objectFactory;

  @Inject
  private RESTSecurity restSecurity;

  @Path("/studentparents")
  @GET
  @RESTPermit (UserPermissions.LIST_STUDENTPARENTS)
  public Response listStaffMembers(@QueryParam ("firstResult") Integer firstResult, @QueryParam ("maxResults") Integer maxResults, @QueryParam ("email") String email) {
    List<StudentParent> studentParents;
    
    if (StringUtils.isNotBlank(email)) {
      studentParents = new ArrayList<>();
      StudentParent studentParent = userController.findStudentParentByEmail(email);
      if (studentParent != null) {
        studentParents.add(studentParent);
      }
    } else {
      studentParents = userController.listStudentParents(firstResult, maxResults);
    }
    
    return Response.ok(objectFactory.createModel(studentParents)).build();
  }
  
  @Path("/studentparents/{ID:[0-9]*}")
  @GET
  @RESTPermit (handling = Handling.INLINE)
  public Response findStaffMemberById(@PathParam("ID") Long id, @Context Request request) {
    StudentParent studentParent = userController.findStudentParentById(id);
    
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
    StudentParent studentParent = userController.findStudentParentById(id);
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
    StudentParent studentParent = userController.findStudentParentById(id);
    if (studentParent == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (!restSecurity.hasPermission(new String[] { UserPermissions.LIST_STUDENTPARENT_STUDENTS, UserPermissions.USER_OWNER }, studentParent, Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }

    return Response.ok(objectFactory.createModel(studentParent.getChildren())).build();
  }
  
}