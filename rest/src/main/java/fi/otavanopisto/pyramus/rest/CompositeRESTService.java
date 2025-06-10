package fi.otavanopisto.pyramus.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.ejb.Stateful;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.domainmodel.base.CourseModule;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessmentRequest;
import fi.otavanopisto.pyramus.domainmodel.grading.Grade;
import fi.otavanopisto.pyramus.domainmodel.grading.GradingScale;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.framework.DateUtils;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit.Handling;
import fi.otavanopisto.pyramus.rest.controller.AssessmentController;
import fi.otavanopisto.pyramus.rest.controller.CommonController;
import fi.otavanopisto.pyramus.rest.controller.CourseController;
import fi.otavanopisto.pyramus.rest.controller.UserController;
import fi.otavanopisto.pyramus.rest.controller.permissions.CommonPermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.CourseAssessmentPermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.StudentPermissions;
import fi.otavanopisto.pyramus.rest.model.composite.CompositeAssessmentRequest;
import fi.otavanopisto.pyramus.rest.model.composite.CompositeGrade;
import fi.otavanopisto.pyramus.rest.model.composite.CompositeGradingScale;
import fi.otavanopisto.pyramus.security.impl.SessionController;

@Path("/composite")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateful
@RequestScoped
public class CompositeRESTService {

  @Inject
  private AssessmentController assessmentController;

  @Inject
  private CommonController commonController;

  @Inject
  private CourseController courseController;

  @Inject
  private SessionController sessionController;

  @Inject
  private UserController userController;

  @Path("/gradingScales")
  @GET
  @RESTPermit (CommonPermissions.LIST_GRADINGSCALES)
  public Response listGrades() {
    List<CompositeGradingScale> compositeGradingScales = new ArrayList<CompositeGradingScale>();
    List<GradingScale> gradingScales = commonController.listUnarchivedGradingScales();
    for (GradingScale gradingScale : gradingScales) {
      List<CompositeGrade> compositeGrades = new ArrayList<CompositeGrade>();
      List<Grade> grades = gradingScale.getGrades();
      for (Grade grade : grades) {
        compositeGrades.add(new CompositeGrade(grade.getId(), grade.getName()));
      }
      compositeGradingScales.add(new CompositeGradingScale(
        gradingScale.getId(),
        gradingScale.getName(),
        compositeGrades));
    }
    return Response.ok(compositeGradingScales).build();
  }

  @Path("/course/{COURSEID:[0-9]*}/assessmentRequests")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response listAssessmentRequestsByCourse(@PathParam("COURSEID") Long courseId, @QueryParam("courseStudentIds") String courseStudentIds) {
    Course course = courseController.findCourseById(courseId);
    
    if (course == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (!sessionController.hasPermission(CourseAssessmentPermissions.LIST_STUDENT_COURSEASSESSMENTS, course)) {
      return Response.status(Status.FORBIDDEN).build();
    }

    if (sessionController.hasEnvironmentPermission(StudentPermissions.FEATURE_OWNED_GROUP_STUDENTS_RESTRICTION)) {
      StaffMember staffMember = sessionController.getUser() instanceof StaffMember ? (StaffMember) sessionController.getUser() : null;
      if (!courseController.isCourseStaffMember(course, staffMember)) {
        return Response.status(Status.FORBIDDEN).build();
      }
    }
    
    List<CourseStudent> courseStudents;
    if (courseStudentIds != null) {
      courseStudents = new ArrayList<CourseStudent>();
      
      // Empty courseStudentIds can be provided but is interpreted as empty array
      if (StringUtils.isNotBlank(courseStudentIds)) {
        String[] courseStudentIdArray = courseStudentIds.split(",");
        for (int i = 0; i < courseStudentIdArray.length; i++) {
          CourseStudent courseStudent = courseController.findCourseStudentById(Long.valueOf(courseStudentIdArray[i]));
          if (courseStudent != null && !courseStudent.getArchived() && !courseStudent.getStudent().getArchived()) {
            courseStudents.add(courseStudent);
          }
        }
      }
    }
    else {
      courseStudents = courseController.listCourseStudentsByCourse(course);
    }
    
    List<CompositeAssessmentRequest> assessmentRequests = new ArrayList<CompositeAssessmentRequest>();
    
    if (CollectionUtils.isNotEmpty(courseStudents)) {
      for (CourseStudent courseStudent : courseStudents) {
        
        boolean passing = course.getCourseModules().size() > 0;
        Date evaluationDate = null;
        
        for (CourseModule courseModule : course.getCourseModules()) {
          CourseAssessment courseAssessment = assessmentController.findLatestCourseAssessmentByCourseStudentAndCourseModuleAndArchived(courseStudent, courseModule, Boolean.FALSE);
          if (courseAssessment != null) {
            passing = passing && courseAssessment.getGrade() != null && courseAssessment.getGrade().getPassingGrade();
            evaluationDate = DateUtils.max(evaluationDate, courseAssessment.getDate());
          } else {
            passing = false;
            evaluationDate = null;
            break;
          }
        }
        
        CourseAssessmentRequest courseAssessmentRequest = assessmentController.findLatestCourseAssessmentRequestByCourseStudent(courseStudent);
        CompositeAssessmentRequest assessmentRequest = new CompositeAssessmentRequest();
        assessmentRequest.setId(courseAssessmentRequest == null ? null : courseAssessmentRequest.getId());
        assessmentRequest.setCourseStudentId(courseStudent.getId());
        assessmentRequest.setAssessmentRequestDate(courseAssessmentRequest == null ? null : courseAssessmentRequest.getCreated());
        assessmentRequest.setCourseEnrollmentDate(courseStudent.getEnrolmentTime());
        assessmentRequest.setEvaluationDate(evaluationDate);
        assessmentRequest.setPassing(passing);
        assessmentRequest.setCourseId(course.getId());
        assessmentRequest.setCourseName(course.getName());
        assessmentRequest.setCourseNameExtension(course.getNameExtension());
        String firstName = courseStudent.getStudent().getFirstName();
        if (courseStudent.getStudent().getNickname() != null) {
          firstName = String.format("%s \"%s\"", firstName, courseStudent.getStudent().getNickname());
        }
        assessmentRequest.setFirstName(firstName);
        assessmentRequest.setLastName(courseStudent.getStudent().getLastName());
        assessmentRequest.setStudyProgramme(courseStudent.getStudent().getStudyProgramme().getName());
        assessmentRequest.setUserId(courseStudent.getStudent().getId());
        assessmentRequests.add(assessmentRequest);
      }
    }
    
    return Response.ok(assessmentRequests).build();
  }
  
  /**
   * Returns a list of assessment requests directed at the given staff member.
   * 
   * @param staffMemberId Staff member id
   * 
   * @return A list of assessment requests directed at the given staff member
   */
  @Path("/staffMembers/{STAFFMEMBERID:[0-9]*}/assessmentRequests")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response listAssessmentRequestsByStaffMember(@PathParam("STAFFMEMBERID") Long staffMemberId) {
    
    // Staff member must exist
    
    StaffMember staffMember = userController.findStaffMemberById(staffMemberId);
    if (staffMember == null || staffMember.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    // Staff member should be logged in
    
    User loggedUser = sessionController.getUser();
    if (loggedUser == null || !loggedUser.getId().equals(staffMember.getId())) {
      return Response.status(Status.FORBIDDEN).build();
    }
    
    // List course assessment requests of courses of the staff member
    
    List<CompositeAssessmentRequest> assessmentRequests = new ArrayList<CompositeAssessmentRequest>();
    List<Course> courses = courseController.listCoursesByStaffMember(staffMember);
    for (Course course : courses) {
      List<CourseAssessmentRequest> courseAssessmentRequests = assessmentController.listCourseAssessmentRequestsByCourseAndHandled(course, Boolean.FALSE);
      for (CourseAssessmentRequest courseAssessmentRequest : courseAssessmentRequests) {
        boolean passing = course.getCourseModules().size() > 0;
        Date evaluationDate = null;
        
        for (CourseModule courseModule : course.getCourseModules()) {
          CourseAssessment courseAssessment = assessmentController.findLatestCourseAssessmentByCourseStudentAndCourseModuleAndArchived(courseAssessmentRequest.getCourseStudent(), courseModule, Boolean.FALSE);
          if (courseAssessment != null) {
            passing = passing && courseAssessment.getGrade() != null && courseAssessment.getGrade().getPassingGrade();
            evaluationDate = DateUtils.max(evaluationDate, courseAssessment.getDate());
          } else {
            passing = false;
            evaluationDate = null;
            break;
          }
        }
        
        CompositeAssessmentRequest assessmentRequest = new CompositeAssessmentRequest();
        assessmentRequest.setId(courseAssessmentRequest.getId());
        assessmentRequest.setCourseStudentId(courseAssessmentRequest.getCourseStudent().getId());
        assessmentRequest.setAssessmentRequestDate(courseAssessmentRequest.getCreated());
        assessmentRequest.setCourseEnrollmentDate(courseAssessmentRequest.getCourseStudent().getEnrolmentTime());
        assessmentRequest.setEvaluationDate(evaluationDate);
        assessmentRequest.setPassing(passing);
        assessmentRequest.setCourseId(course.getId());
        assessmentRequest.setCourseName(course.getName());
        assessmentRequest.setCourseNameExtension(course.getNameExtension());
        String firstName = courseAssessmentRequest.getCourseStudent().getStudent().getFirstName();
        if (courseAssessmentRequest.getCourseStudent().getStudent().getNickname() != null) {
          firstName = String.format("%s \"%s\"", firstName, courseAssessmentRequest.getCourseStudent().getStudent().getNickname());
        }
        assessmentRequest.setFirstName(firstName);
        assessmentRequest.setLastName(courseAssessmentRequest.getCourseStudent().getStudent().getLastName());
        assessmentRequest.setStudyProgramme(courseAssessmentRequest.getCourseStudent().getStudent().getStudyProgramme().getName());
        assessmentRequest.setUserId(courseAssessmentRequest.getCourseStudent().getStudent().getId());
        assessmentRequests.add(assessmentRequest);
      }
    }
    
    return Response.ok(assessmentRequests).build();
  }

}
