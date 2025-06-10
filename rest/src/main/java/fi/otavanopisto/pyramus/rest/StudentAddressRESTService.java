package fi.otavanopisto.pyramus.rest;

import java.util.List;

import jakarta.ejb.Stateful;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import fi.otavanopisto.pyramus.domainmodel.base.Address;
import fi.otavanopisto.pyramus.domainmodel.base.ContactType;
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

@Path("/students/students/{STUDENTID:[0-9]*}/addresses")
@Produces("application/json")
@Consumes("application/json")
@Stateful
@RequestScoped
public class StudentAddressRESTService extends AbstractRESTService {

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
  public Response listStudentAddresses(@PathParam("STUDENTID") Long studentId) {
    Student student = studentController.findStudentById(studentId);
    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK) {
      return Response.status(studentStatus).build();
    }

    if (!restSecurity.hasPermission(new String[] { StudentPermissions.LIST_STUDENTADDRESSS, UserPermissions.STUDENT_PARENT }, student) && !restSecurity.hasPermission(new String[] { PersonPermissions.PERSON_OWNER }, student.getPerson() )) {
      return Response.status(Status.FORBIDDEN).build();
    }

    List<Address> addresses = student.getContactInfo().getAddresses();
    if (addresses.isEmpty()) {
      return Response.noContent().build();
    }

    return Response.ok(objectFactory.createModel(addresses)).build();
  }

  @Path("/")
  @POST
  @RESTPermit(handling = Handling.INLINE)
  public Response createStudentAddress(@PathParam("STUDENTID") Long studentId, fi.otavanopisto.pyramus.rest.model.Address address) {
    if (address == null) {
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

    Long contactTypeId = address.getContactTypeId();
    Boolean defaultAddress = address.getDefaultAddress();
    String name = address.getName();
    String streetAddress = address.getStreetAddress();
    String postalCode = address.getPostalCode();
    String country = address.getCountry();
    String city = address.getCity();

    if (contactTypeId == null || defaultAddress == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    ContactType contactType = commonController.findContactTypeById(contactTypeId);
    if (contactType == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    return Response.ok(
        objectFactory.createModel(studentController.addStudentAddress(student, contactType, defaultAddress, name, streetAddress, postalCode, city, country)))
        .build();
  }

  @Path("/{ID:[0-9]*}")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response findStudentAddress(@PathParam("STUDENTID") Long studentId, @PathParam("ID") Long id) {
    Student student = studentController.findStudentById(studentId);
    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK) {
      return Response.status(studentStatus).build();
    }

    if (!restSecurity.hasPermission(new String[] { StudentPermissions.FIND_STUDENTADDRESS }, student) && !restSecurity.hasPermission(new String[] { PersonPermissions.PERSON_OWNER }, student.getPerson() )) {
      return Response.status(Status.FORBIDDEN).build();
    }

    Address address = commonController.findAddressById(id);
    if (address == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (!address.getContactInfo().getId().equals(student.getContactInfo().getId())) {
      return Response.status(Status.NOT_FOUND).build();
    }

    return Response.ok(objectFactory.createModel(address)).build();
  }

  @Path("/{ID:[0-9]*}")
  @PUT
  @RESTPermit(handling = Handling.INLINE)
  public Response updateStudentAddress(
      @PathParam("STUDENTID") Long studentId,
      @PathParam("ID") Long id,
      fi.otavanopisto.pyramus.rest.model.Address body
      ) {
    Student student = studentController.findStudentById(studentId);
    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK) {
      return Response.status(studentStatus).build();
    }

    if (!restSecurity.hasPermission(new String[] { StudentPermissions.UPDATE_STUDENTADDRESS }, student)
        && !restSecurity.hasPermission(new String[] { StudentPermissions.STUDENT_OWNER }, student)) {
      return Response.status(Status.FORBIDDEN).build();
    }

    Address address = commonController.findAddressById(id);
    if (address == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (!address.getContactInfo().getId().equals(student.getContactInfo().getId())) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    ContactType contactType = commonController.findContactTypeById(body.getContactTypeId());
    
    address = studentController.updateStudentAddress(
        address,
        contactType,
        body.getDefaultAddress(),
        body.getName(),
        body.getStreetAddress(),
        body.getPostalCode(),
        body.getCity(),
        body.getCountry());

    return Response.ok(objectFactory.createModel(address)).build();
  }

  @Path("/{ID:[0-9]*}")
  @DELETE
  @RESTPermit(StudentPermissions.DELETE_STUDENTADDRESS)
  public Response deleteStudentAddress(@PathParam("STUDENTID") Long studentId, @PathParam("ID") Long id) {
    Student student = studentController.findStudentById(studentId);
    Status studentStatus = checkStudent(student);
    if (studentStatus != Status.OK) {
      return Response.status(studentStatus).build();
    }

    Address address = commonController.findAddressById(id);
    if (address == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (!address.getContactInfo().getId().equals(student.getContactInfo().getId())) {
      return Response.status(Status.NOT_FOUND).build();
    }

    commonController.deleteAddress(address);

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
