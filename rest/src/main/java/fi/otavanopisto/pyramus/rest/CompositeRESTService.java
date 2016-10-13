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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessmentRequest;
import fi.otavanopisto.pyramus.domainmodel.grading.Grade;
import fi.otavanopisto.pyramus.domainmodel.grading.GradingScale;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit.Handling;
import fi.otavanopisto.pyramus.rest.controller.AssessmentController;
import fi.otavanopisto.pyramus.rest.controller.CommonController;
import fi.otavanopisto.pyramus.rest.controller.CourseController;
import fi.otavanopisto.pyramus.rest.controller.UserController;
import fi.otavanopisto.pyramus.rest.controller.permissions.CommonPermissions;
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
      List<CourseAssessmentRequest> courseAssessmentRequests = assessmentController.listCourseAssessmentRequestsByCourse(course);
      for (CourseAssessmentRequest courseAssessmentRequest : courseAssessmentRequests) {
        CourseAssessment courseAssessment = assessmentController.findCourseAssessmentByCourseStudent(courseAssessmentRequest.getCourseStudent());
        CompositeAssessmentRequest assessmentRequest = new CompositeAssessmentRequest();
        assessmentRequest.setAssessmentRequestDate(courseAssessmentRequest.getCreated());
        assessmentRequest.setCourseEnrollmentDate(courseAssessmentRequest.getCourseStudent().getEnrolmentTime());
        assessmentRequest.setEvaluationDate(courseAssessment == null ? null : courseAssessment.getDate());
        assessmentRequest.setPassing(courseAssessment != null && courseAssessment.getGrade() != null && courseAssessment.getGrade().getPassingGrade());
        assessmentRequest.setCourseId(course.getId());
        assessmentRequest.setCourseName(course.getName());
        assessmentRequest.setCourseNameExtension(course.getNameExtension());
        assessmentRequest.setFirstName(courseAssessmentRequest.getCourseStudent().getStudent().getFirstName());
        assessmentRequest.setLastName(courseAssessmentRequest.getCourseStudent().getStudent().getLastName());
        assessmentRequest.setStudyProgramme(courseAssessmentRequest.getCourseStudent().getStudent().getStudyProgramme().getName());
        assessmentRequest.setUserId(courseAssessmentRequest.getCourseStudent().getStudent().getId());
        assessmentRequests.add(assessmentRequest);
      }
    }
    
    return Response.ok(assessmentRequests).build();
  }

}
