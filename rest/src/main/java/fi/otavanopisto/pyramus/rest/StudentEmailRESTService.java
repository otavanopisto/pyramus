package fi.otavanopisto.pyramus.rest;

import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.domainmodel.base.ContactType;
import fi.otavanopisto.pyramus.domainmodel.base.Email;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.framework.UserEmailInUseException;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit.Handling;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit.Style;
import fi.otavanopisto.pyramus.rest.controller.CommonController;
import fi.otavanopisto.pyramus.rest.controller.StudentController;
import fi.otavanopisto.pyramus.rest.controller.permissions.PersonPermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.StudentPermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.UserPermissions;
import fi.otavanopisto.pyramus.rest.security.RESTSecurity;

@Path("/students/students/{STUDENTID:[0-9]*}/emails")
@Produces("application/json")
@Consumes("application/json")
@Stateful
@RequestScoped
public class StudentEmailRESTService extends AbstractRESTService {

  @Inject
  private RESTSecurity restSecurity;

  @Inject
  private CommonController commonController;

  @Inject
  private StudentController studentController;
  
  @Inject
  private ObjectFactory objectFactory;
  
  @Path("/")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response listStudentEmails(@PathParam("STUDENTID") Long studentId) {
    Student student = studentController.findStudentById(studentId);
    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK) {
      return Response.status(studentStatus).build();
    }

    if (!restSecurity.hasPermission(new String[] { StudentPermissions.LIST_STUDENTEMAILS }, student) && !restSecurity.hasPermission(new String[] { PersonPermissions.PERSON_OWNER }, student.getPerson() )) {
      return Response.status(Status.FORBIDDEN).build();
    }

    List<Email> emails = student.getContactInfo().getEmails();
    if (emails.isEmpty()) {
      return Response.noContent().build();
    }

    return Response.ok(objectFactory.createModel(emails)).build();
  }

  @Path("/")
  @POST
  @RESTPermit(handling = Handling.INLINE)
  public Response createStudentEmail(@PathParam("STUDENTID") Long studentId, fi.otavanopisto.pyramus.rest.model.Email email) {
    if (email == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    Student student = studentController.findStudentById(studentId);
    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK) {
      return Response.status(studentStatus).build();
    }

    if (!restSecurity.hasPermission(new String[] { StudentPermissions.CREATE_STUDENTEMAIL, StudentPermissions.STUDENT_OWNER }, student, Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }

    Long contactTypeId = email.getContactTypeId();
    Boolean defaultAddress = email.getDefaultAddress();
    String address = email.getAddress();

    if (contactTypeId == null || defaultAddress == null || StringUtils.isBlank(address)) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    ContactType contactType = commonController.findContactTypeById(contactTypeId);
    if (contactType == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    try {
      return Response.ok(objectFactory.createModel(studentController.addStudentEmail(student, contactType, address, defaultAddress))).build();
    } catch (UserEmailInUseException ueiue) {
      return Response.status(Status.FORBIDDEN).build();
    }
  }
  
  @Path("/{ID:[0-9]*}")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response findStudentEmail(@PathParam("STUDENTID") Long studentId, @PathParam("ID") Long id) {
    Student student = studentController.findStudentById(studentId);
    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK) {
      return Response.status(studentStatus).build();
    }

    if (!restSecurity.hasPermission(new String[] { StudentPermissions.FIND_STUDENTEMAIL }, student) && !restSecurity.hasPermission(new String[] { PersonPermissions.PERSON_OWNER }, student.getPerson() )) {
      return Response.status(Status.FORBIDDEN).build();
    }

    Email email = commonController.findEmailById(id);
    if (email == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (!email.getContactInfo().getId().equals(student.getContactInfo().getId())) {
      return Response.status(Status.NOT_FOUND).build();
    }

    return Response.ok(objectFactory.createModel(email)).build();
  }

  @Path("/{ID:[0-9]*}")
  @PUT
  @RESTPermit(handling = Handling.INLINE)
  public Response updateStudentEmail(
      @PathParam("STUDENTID") Long studentId,
      @PathParam("ID") Long id,
      fi.otavanopisto.pyramus.rest.model.Email body
      ) {
    Student student = studentController.findStudentById(studentId);
    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK) {
      return Response.status(studentStatus).build();
    }

    if (!restSecurity.hasPermission(new String[] { StudentPermissions.UPDATE_STUDENTEMAIL }, student)
        && !restSecurity.hasPermission(new String[] { StudentPermissions.STUDENT_OWNER }, student)) {
      return Response.status(Status.FORBIDDEN).build();
    }

    Email email = commonController.findEmailById(id);
    if (email == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (!email.getContactInfo().getId().equals(student.getContactInfo().getId())) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    ContactType contactType = commonController.findContactTypeById(body.getContactTypeId());
    
    try {
      email = studentController.updateStudentEmail(
          student,
          email,
          contactType,
          body.getAddress(),
          body.getDefaultAddress()
      );
      
      return Response.ok(objectFactory.createModel(email)).build();
    } catch (UserEmailInUseException e) {
      return Response.status(Status.BAD_REQUEST).build();
    }
  }
  
  @Path("/{ID:[0-9]*}")
  @DELETE
  @RESTPermit(StudentPermissions.DELETE_STUDENTEMAIL)
  public Response deleteStudentEmail(@PathParam("STUDENTID") Long studentId, @PathParam("ID") Long id) {
    Student student = studentController.findStudentById(studentId);
    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK) {
      return Response.status(studentStatus).build();
    }

    Email email = commonController.findEmailById(id);
    if (email == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (!email.getContactInfo().getId().equals(student.getContactInfo().getId())) {
      return Response.status(Status.NOT_FOUND).build();
    }

    commonController.deleteEmail(email);

    return Response.noContent().build();
  }

  /**
   * Checks for student to be non-null, not archived and find_student permission.
   * 
   * @param student
   * @return
   */
  private Status checkStudent(Student student) {
    if (student == null || student.getArchived()) {
      return Status.NOT_FOUND;
    }
    
    if (!restSecurity.hasPermission(new String[] { StudentPermissions.FIND_STUDENT, UserPermissions.USER_OWNER }, student, Style.OR)) {
      return Status.FORBIDDEN;
    }

    return Status.OK;
  }
}
