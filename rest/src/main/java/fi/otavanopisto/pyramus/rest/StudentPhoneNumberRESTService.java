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
import fi.otavanopisto.pyramus.domainmodel.base.PhoneNumber;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit.Handling;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit.Style;
import fi.otavanopisto.pyramus.rest.controller.CommonController;
import fi.otavanopisto.pyramus.rest.controller.StudentController;
import fi.otavanopisto.pyramus.rest.controller.permissions.PersonPermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.StudentPermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.UserPermissions;
import fi.otavanopisto.pyramus.rest.security.RESTSecurity;

@Path("/students/students/{STUDENTID:[0-9]*}/phoneNumbers")
@Produces("application/json")
@Consumes("application/json")
@Stateful
@RequestScoped
public class StudentPhoneNumberRESTService extends AbstractRESTService {

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
  public Response listStudentPhoneNumbers(@PathParam("STUDENTID") Long studentId) {
    Student student = studentController.findStudentById(studentId);
    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK) {
      return Response.status(studentStatus).build();
    }

    if (!restSecurity.hasPermission(new String[] { StudentPermissions.LIST_STUDENTPHONENUMBERS, UserPermissions.STUDENT_PARENT }, student) && !restSecurity.hasPermission(new String[] { PersonPermissions.PERSON_OWNER }, student.getPerson() )) {
      return Response.status(Status.FORBIDDEN).build();
    }

    List<PhoneNumber> phoneNumbers = student.getContactInfo().getPhoneNumbers();
    if (phoneNumbers.isEmpty()) {
      return Response.noContent().build();
    }

    return Response.ok(objectFactory.createModel(phoneNumbers)).build();
  }

  @Path("/")
  @POST
  @RESTPermit(handling = Handling.INLINE)
  public Response createStudentPhoneNumber(@PathParam("STUDENTID") Long studentId, fi.otavanopisto.pyramus.rest.model.PhoneNumber phoneNumber) {
    if (phoneNumber == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    Student student = studentController.findStudentById(studentId);
    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK) {
      return Response.status(studentStatus).build();
    }

    if (!restSecurity.hasPermission(new String[] { StudentPermissions.UPDATE_STUDENT, StudentPermissions.STUDENT_OWNER }, student, Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }
    Long contactTypeId = phoneNumber.getContactTypeId();
    Boolean defaultNumber = phoneNumber.getDefaultNumber();
    String number = phoneNumber.getNumber();

    if (contactTypeId == null || defaultNumber == null || StringUtils.isBlank(number)) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    ContactType contactType = commonController.findContactTypeById(contactTypeId);
    if (contactType == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    return Response.ok(objectFactory.createModel(studentController.addStudentPhoneNumber(student, contactType, number, defaultNumber))).build();
  }

  @Path("/{ID:[0-9]*}")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response findStudentPhoneNumber(@PathParam("STUDENTID") Long studentId, @PathParam("ID") Long id) {
    Student student = studentController.findStudentById(studentId);
    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK) {
      return Response.status(studentStatus).build();
    }

    if (!restSecurity.hasPermission(new String[] { StudentPermissions.FIND_STUDENTPHONENUMBER }, student) && !restSecurity.hasPermission(new String[] { PersonPermissions.PERSON_OWNER }, student.getPerson() )) {
      return Response.status(Status.FORBIDDEN).build();
    }

    PhoneNumber phoneNumber = commonController.findPhoneNumberById(id);
    if (phoneNumber == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (!phoneNumber.getContactInfo().getId().equals(student.getContactInfo().getId())) {
      return Response.status(Status.NOT_FOUND).build();
    }

    return Response.ok(objectFactory.createModel(phoneNumber)).build();
  }

  @Path("/{ID:[0-9]*}")
  @PUT
  @RESTPermit(handling = Handling.INLINE)
  public Response updateStudentPhoneNumber(
      @PathParam("STUDENTID") Long studentId,
      @PathParam("ID") Long id,
      fi.otavanopisto.pyramus.rest.model.PhoneNumber body
      ) {
    Student student = studentController.findStudentById(studentId);
    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK) {
      return Response.status(studentStatus).build();
    }

    if (!restSecurity.hasPermission(new String[] { StudentPermissions.UPDATE_STUDENTPHONENUMBER }, student)
        && !restSecurity.hasPermission(new String[] { StudentPermissions.STUDENT_OWNER }, student)) {
      return Response.status(Status.FORBIDDEN).build();
    }

    PhoneNumber phoneNumber = commonController.findPhoneNumberById(id);
    if (phoneNumber == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (!phoneNumber.getContactInfo().getId().equals(student.getContactInfo().getId())) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    ContactType contactType = commonController.findContactTypeById(body.getContactTypeId());

    phoneNumber = studentController.updateStudentPhoneNumber(
        phoneNumber,
        contactType,
        body.getNumber(),
        body.getDefaultNumber());

    return Response.ok(objectFactory.createModel(phoneNumber)).build();
  }

  @Path("/{ID:[0-9]*}")
  @DELETE
  @RESTPermit(StudentPermissions.DELETE_STUDENTPHONENUMBER)
  public Response deleteStudentPhoneNumber(@PathParam("STUDENTID") Long studentId, @PathParam("ID") Long id) {
    Student student = studentController.findStudentById(studentId);
    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK) {
      return Response.status(studentStatus).build();
    }

    PhoneNumber phoneNumber = commonController.findPhoneNumberById(id);
    if (phoneNumber == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (!phoneNumber.getContactInfo().getId().equals(student.getContactInfo().getId())) {
      return Response.status(Status.NOT_FOUND).build();
    }

    commonController.deletePhoneNumber(phoneNumber);

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
    
    if (!restSecurity.hasPermission(new String[] { StudentPermissions.FIND_STUDENT, UserPermissions.USER_OWNER, UserPermissions.STUDENT_PARENT }, student, Style.OR)) {
      return Status.FORBIDDEN;
    }

    return Status.OK;
  }
}
