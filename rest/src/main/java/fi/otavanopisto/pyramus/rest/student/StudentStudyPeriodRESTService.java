package fi.otavanopisto.pyramus.rest.student;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

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

import fi.otavanopisto.pyramus.dao.students.StudentStudyPeriodDAO;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentStudyPeriod;
import fi.otavanopisto.pyramus.domainmodel.students.StudentStudyPeriodType;
import fi.otavanopisto.pyramus.framework.UserUtils;
import fi.otavanopisto.pyramus.rest.AbstractRESTService;
import fi.otavanopisto.pyramus.rest.ObjectFactory;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit.Handling;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit.Style;
import fi.otavanopisto.pyramus.rest.controller.StudentController;
import fi.otavanopisto.pyramus.rest.controller.permissions.StudentPermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.UserPermissions;
import fi.otavanopisto.pyramus.rest.security.RESTSecurity;
import fi.otavanopisto.pyramus.security.impl.SessionController;
import fi.otavanopisto.pyramus.security.impl.permissions.OrganizationPermissions;

@Path("/students")
@Produces("application/json")
@Consumes("application/json")
@Stateful
@RequestScoped
public class StudentStudyPeriodRESTService extends AbstractRESTService {

  @Inject
  private Logger logger;
  
  @Inject
  private SessionController sessionController;

  @Inject
  private ObjectFactory objectFactory;
  
  @Inject
  private RESTSecurity restSecurity;

  @Inject
  private StudentController studentController;
  
  @Inject
  private StudentStudyPeriodDAO studentStudyPeriodDAO;
  
  @Path("/students/{STUDENTID:[0-9]*}/studyPeriods")
  @POST
  @RESTPermit (StudentPermissions.CREATE_STUDENTSTUDYPERIOD)
  public Response createStudentStudyPeriod(@PathParam("STUDENTID") Long studentId, fi.otavanopisto.pyramus.rest.model.students.StudentStudyPeriod entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    Student student = studentController.findStudentById(studentId);

    if (!hasAccessToStudent(student)) {
      return Response.status(Status.FORBIDDEN).build();
    }
    
    Date begin = entity.getBegin() != null ? Date.from(entity.getBegin().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()) : null;
    Date end = entity.getEnd() != null ? Date.from(entity.getEnd().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()) : null;
    StudentStudyPeriodType periodType = entity.getType() != null ? StudentStudyPeriodType.valueOf(entity.getType().toString()) : null;
    
    StudentStudyPeriod studentStudyPeriod = studentStudyPeriodDAO.create(student, begin, end, periodType);
    
    return Response.ok(objectFactory.createModel(studentStudyPeriod)).build();
  }

  @Path("/students/{STUDENTID:[0-9]*}/studyPeriods")
  @GET
  @RESTPermit (handling = Handling.INLINE)
  public Response listStudentStudyPeriods(@PathParam("STUDENTID") Long studentId) {
    Student student = studentController.findStudentById(studentId);

    if (student == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (UserUtils.isOwnerOf(sessionController.getUser(), student.getPerson()) ||
        (sessionController.hasEnvironmentPermission(StudentPermissions.LIST_STUDENTSTUDYPERIODS) && hasAccessToStudent(student))) {
      List<StudentStudyPeriod> studyPeriods = studentStudyPeriodDAO.listByStudent(student);
      return Response.ok(objectFactory.createModel(studyPeriods)).build();
    } else {
      return Response.status(Status.FORBIDDEN).build();
    }
  }
  
  @Path("/students/{STUDENTID:[0-9]*}/studyPeriods/{PERIODID:[0-9]*}")
  @GET
  @RESTPermit (handling = Handling.INLINE)
  public Response findStudentStudyPeriod(@PathParam("STUDENTID") Long studentId, @PathParam("PERIODID") Long periodId) {
    Student student = studentController.findStudentById(studentId);

    if (student == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (UserUtils.isOwnerOf(sessionController.getUser(), student.getPerson()) ||
        (sessionController.hasEnvironmentPermission(StudentPermissions.FIND_STUDENTSTUDYPERIOD) && hasAccessToStudent(student))) {
      StudentStudyPeriod studentStudyPeriod = studentStudyPeriodDAO.findById(periodId);
      
      if (studentStudyPeriod != null && !studentStudyPeriod.getStudent().getId().equals(student.getId())) {
        logger.severe(String.format("User %d attempted access to a study period %d with non-matching student %d.", 
            (sessionController.getUser() != null ? sessionController.getUser().getId() : null), periodId, studentId));
        return Response.status(Status.FORBIDDEN).build();
      } else if (studentStudyPeriod == null) {
        return Response.status(Status.NOT_FOUND).build();
      }
      
      return Response.ok(objectFactory.createModel(studentStudyPeriod)).build();
    } else {
      return Response.status(Status.FORBIDDEN).build();
    }
  }

  @Path("/students/{STUDENTID:[0-9]*}/studyPeriods/{PERIODID:[0-9]*}")
  @PUT
  @RESTPermit (StudentPermissions.UPDATE_STUDENTSTUDYPERIOD)
  public Response updateStudentStudyPeriod(@PathParam("STUDENTID") Long studentId, @PathParam("PERIODID") Long periodId,
      fi.otavanopisto.pyramus.rest.model.students.StudentStudyPeriod entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    Student student = studentController.findStudentById(studentId);
    StudentStudyPeriod studentStudyPeriod = studentStudyPeriodDAO.findById(periodId);
    
    if (student == null || studentStudyPeriod == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (!hasAccessToStudent(student)) {
      return Response.status(Status.FORBIDDEN).build();
    }
    
    Date begin = entity.getBegin() != null ? Date.from(entity.getBegin().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()) : null;
    Date end = entity.getEnd() != null ? Date.from(entity.getEnd().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()) : null;
    StudentStudyPeriodType periodType = entity.getType() != null ? StudentStudyPeriodType.valueOf(entity.getType().toString()) : null;

    studentStudyPeriod = studentStudyPeriodDAO.update(studentStudyPeriod, begin, end, periodType);
    
    return Response.ok(objectFactory.createModel(studentStudyPeriod)).build();
  }
  
  @Path("/students/{STUDENTID:[0-9]*}/studyPeriods/{PERIODID:[0-9]*}")
  @DELETE
  @RESTPermit (StudentPermissions.DELETE_STUDENTSTUDYPERIOD)
  public Response deleteStudentStudyPeriod(@PathParam("STUDENTID") Long studentId, @PathParam("PERIODID") Long periodId) {
    StudentStudyPeriod studentStudyPeriod = studentStudyPeriodDAO.findById(periodId);
    studentStudyPeriodDAO.delete(studentStudyPeriod);
    return Response.noContent().build();
  }
  
  private boolean hasAccessToStudent(Student student) {
    if (!restSecurity.hasPermission(new String[] { StudentPermissions.FIND_STUDENT, UserPermissions.USER_OWNER }, student, Style.OR)) {
      return false;
    }

    if (!sessionController.hasEnvironmentPermission(OrganizationPermissions.ACCESS_ALL_ORGANIZATIONS)) {
      if (!UserUtils.isMemberOf(sessionController.getUser(), student.getOrganization())) {
        return false;
      }
    }
    
    return true;
  }
  
}
