package fi.otavanopisto.pyramus.rest;

import java.util.Date;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamAttendanceDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamEnrollmentDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExam;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamAttendanceStatus;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamEnrollment;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamEnrollmentState;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamGrade;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamSubject;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamTerm;
import fi.otavanopisto.pyramus.domainmodel.matriculation.SchoolType;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit.Handling;
import fi.otavanopisto.pyramus.rest.controller.permissions.MatriculationPermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.UserPermissions;
import fi.otavanopisto.pyramus.rest.model.MatriculationExamAttendance;
import fi.otavanopisto.pyramus.rest.security.RESTSecurity;

@Path("/matriculation")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateful
@RequestScoped
public class MatriculationRESTService extends AbstractRESTService {

  @Inject
  private RESTSecurity restSecurity;

  @Inject
  private MatriculationExamDAO matriculationExamDao;

  @Inject
  private MatriculationExamEnrollmentDAO matriculationExamEnrollmentDao;

  @Inject
  private StudentDAO studentDao;

  @Inject
  private MatriculationExamAttendanceDAO matriculationExamAttendanceDao;
  
  @Path("/currentExam")
  @GET
  @RESTPermit(MatriculationPermissions.GET_CURRENT_EXAM)
  public Response getCurrentExam() {
    MatriculationExam exam = matriculationExamDao.get();
    if (exam == null) {
      return Response.status(Status.NOT_FOUND).entity("No current exam").build();
    } else {
      fi.otavanopisto.pyramus.rest.model.MatriculationExam result = new fi.otavanopisto.pyramus.rest.model.MatriculationExam();
      result.setId(exam.getId());
      result.setStarts(exam.getStarts().getTime());
      result.setEnds(exam.getEnds().getTime());
      return Response.ok(result).build();
    }
  }
  
  @Path("/enrollments/latest/{STUDENTID}")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response getLatestEnrollment(@PathParam("STUDENTID") Long studentId) {
    Student student = studentDao.findById(studentId);
    if (student == null) {
      return Response.status(Status.BAD_REQUEST).entity("Student not found").build();
    }

    if (!restSecurity.hasPermission(new String[] { UserPermissions.USER_OWNER }, student)) {
      return Response.status(Status.FORBIDDEN).build();
    }
    
    MatriculationExamEnrollment latest = matriculationExamEnrollmentDao.findLatestByStudent(student);
    if (latest == null) {
      return Response.status(Status.NOT_FOUND).entity("No enrollments for student").build();
    } else {
      fi.otavanopisto.pyramus.rest.model.MatriculationExamEnrollment result =
          new fi.otavanopisto.pyramus.rest.model.MatriculationExamEnrollment();
      String enrollmentDate = null;
      if (latest.getEnrollmentDate() != null) {
        enrollmentDate = latest.getEnrollmentDate().toString();
      }
      result.setName(latest.getName());
      result.setSsn(latest.getSsn());
      result.setEmail(latest.getEmail());
      result.setPhone(latest.getPhone());
      result.setAddress(latest.getAddress());
      result.setPostalCode(latest.getPostalCode());
      result.setCity(latest.getCity());
      result.setNationalStudentNumber(latest.getNationalStudentNumber());
      result.setGuider(latest.getGuider());
      result.setEnrollAs(latest.getEnrollAs().name());
      result.setNumMandatoryCourses(latest.getNumMandatoryCourses());
      result.setRestartExam(latest.isRestartExam());
      result.setLocation(latest.getLocation());
      result.setMessage(latest.getMessage());
      result.setCanPublishName(latest.isCanPublishName());
      result.setStudentId(latest.getStudent().getId());
      result.setState(latest.getState().name());
      result.setEnrollmentDate(enrollmentDate);
      return Response.ok(result).build();
    }
    
  }
  
  @Path("/enrollments")
  @POST
  @RESTPermit(handling = Handling.INLINE)
  public Response enrollToExam(fi.otavanopisto.pyramus.rest.model.MatriculationExamEnrollment enrollment) {
  
    Student student = studentDao.findById(enrollment.getStudentId());
    if (student == null) {
      return Response.status(Status.BAD_REQUEST).entity("Student not found").build();
    }

    if (!restSecurity.hasPermission(new String[] { UserPermissions.USER_OWNER }, student)) {
      return Response.status(Status.FORBIDDEN).build();
    }
    
    if (!"PENDING".equals(enrollment.getState())) {
      return Response.status(Status.BAD_REQUEST)
                     .entity("Can only send pending enrollments via REST")
                     .build();
    }
  
    try {
      MatriculationExamEnrollment enrollmentEntity = matriculationExamEnrollmentDao.create(
        enrollment.getName(),
        enrollment.getSsn(),
        enrollment.getEmail(),
        enrollment.getPhone(),
        enrollment.getAddress(),
        enrollment.getPostalCode(),
        enrollment.getCity(),
        enrollment.getNationalStudentNumber(),
        enrollment.getGuider(),
        SchoolType.valueOf(enrollment.getEnrollAs()),
        enrollment.getNumMandatoryCourses(),
        enrollment.isRestartExam(),
        enrollment.getLocation(),
        enrollment.getMessage(),
        enrollment.isCanPublishName(),
        student,
        MatriculationExamEnrollmentState.valueOf(enrollment.getState()),
        new Date());
        
      for (MatriculationExamAttendance attendance : enrollment.getAttendances()) {
        matriculationExamAttendanceDao.create(
          enrollmentEntity,
          MatriculationExamSubject.valueOf(attendance.getSubject()),
          attendance.getMandatory(),
          attendance.getRepeat(),
          attendance.getYear(),
          attendance.getTerm() != null
            ? MatriculationExamTerm.valueOf(attendance.getTerm()) : null,
          attendance.getStatus() != null
            ? MatriculationExamAttendanceStatus.valueOf(attendance.getStatus()) : null,
          attendance.getGrade() != null
            ? MatriculationExamGrade.valueOf(attendance.getGrade()) : null);
      }
    } catch (IllegalArgumentException ex) {
      return Response.status(Status.BAD_REQUEST)
                     .entity(ex.getMessage())
                     .build();
    }
    
    return Response.ok(enrollment).build();
  }

}
