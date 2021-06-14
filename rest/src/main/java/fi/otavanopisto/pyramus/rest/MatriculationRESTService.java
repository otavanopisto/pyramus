package fi.otavanopisto.pyramus.rest;

import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamAttendanceDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamEnrollmentDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.students.StudentGroupDAO;
import fi.otavanopisto.pyramus.dao.students.StudentGroupStudentDAO;
import fi.otavanopisto.pyramus.dao.students.StudentStudyPeriodDAO;
import fi.otavanopisto.pyramus.dao.users.UserVariableDAO;
import fi.otavanopisto.pyramus.dao.users.UserVariableKeyDAO;
import fi.otavanopisto.pyramus.domainmodel.matriculation.DegreeType;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExam;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamAttendanceStatus;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamEnrollment;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamEnrollmentState;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamGrade;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamSubject;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamTerm;
import fi.otavanopisto.pyramus.domainmodel.matriculation.SchoolType;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroup;
import fi.otavanopisto.pyramus.domainmodel.students.StudentStudyPeriod;
import fi.otavanopisto.pyramus.domainmodel.students.StudentStudyPeriodType;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.domainmodel.users.UserVariableKey;
import fi.otavanopisto.pyramus.framework.DateUtils;
import fi.otavanopisto.pyramus.framework.SettingUtils;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit.Handling;
import fi.otavanopisto.pyramus.rest.controller.permissions.MatriculationPermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.UserPermissions;
import fi.otavanopisto.pyramus.rest.model.MatriculationEligibilities;
import fi.otavanopisto.pyramus.rest.model.MatriculationExamAttendance;
import fi.otavanopisto.pyramus.rest.security.RESTSecurity;
import fi.otavanopisto.pyramus.security.impl.SessionController;
import fi.otavanopisto.security.LoggedIn;

@Path("/matriculation")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateful
@RequestScoped
public class MatriculationRESTService extends AbstractRESTService {

  private static final String SETTING_ELIGIBLE_GROUPS = "matriculation.eligibleGroups";
  private static final String USERVARIABLE_PERSONAL_EXAM_ENROLLMENT_EXPIRYDATE = "matriculation.examEnrollmentExpiryDate";
  
  @Inject
  private SessionController sessionController;
  
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
  
  @Inject
  private UserVariableDAO userVariableDAO;

  @Inject
  private UserVariableKeyDAO userVariableKeyDAO;
  
  @Inject
  private StudentGroupDAO studentGroupDAO;

  @Inject
  private StudentGroupStudentDAO studentGroupStudentDAO;

  @Inject
  private StudentStudyPeriodDAO studentStudyPeriodDAO;
  
  @Path("/eligibility")
  @GET
  @LoggedIn
  @RESTPermit(handling = Handling.INLINE)
  public Response listEligibilities() {
    User loggedUser = sessionController.getUser();

    boolean compulsoryEducation = false;
    boolean upperSecondarySchoolCurriculum = false;
    
    if (loggedUser instanceof Student) {
      Student loggedStudent = (Student) loggedUser;
      
      upperSecondarySchoolCurriculum = hasGroupEligibility(loggedStudent);
          
      List<StudentStudyPeriod> studyPeriods = studentStudyPeriodDAO.listByStudent(loggedStudent);
      EnumSet<StudentStudyPeriodType> studyPeriodTypes = EnumSet.of(StudentStudyPeriodType.COMPULSORY_EDUCATION, StudentStudyPeriodType.EXTENDED_COMPULSORY_EDUCATION);
      compulsoryEducation = studyPeriods.stream().anyMatch(studyPeriod -> studyPeriodTypes.contains(studyPeriod.getPeriodType()));
    }
    
    return Response.ok(new MatriculationEligibilities(compulsoryEducation, upperSecondarySchoolCurriculum)).build();
  }
  
  @Path("/exams")
  @GET
  @RESTPermit(MatriculationPermissions.LIST_EXAMS)
  public Response listExams(@QueryParam("onlyEligible") Boolean onlyEligible) {
    User loggedUser = sessionController.getUser();
    Student student = loggedUser instanceof Student ? (Student) loggedUser : null;
    List<MatriculationExam> exams = matriculationExamDao.listAll();
    Stream<MatriculationExam> examStream = exams.stream().filter(exam -> isVisible(exam, loggedUser));
    
    if (onlyEligible) {
      if (student != null) {
        examStream = examStream.filter(exam -> isEligible(student, exam));
      } else {
        // Caller is not student so they can't be eligible to enroll any exams
        return Response.ok(Collections.emptyList()).build();
      }
    }

    return Response.ok(
        examStream
        .map(exam -> restModel(exam, student))
        .collect(Collectors.toList())
      ).build();
  }
  
  @Path("/exams/{EXAMID}/enrollments/latest/{STUDENTID}")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response getLatestEnrollment(@PathParam("EXAMID") Long examId, @PathParam("STUDENTID") Long studentId) {
    Student student = studentDao.findById(studentId);
    if (student == null) {
      return Response.status(Status.BAD_REQUEST).entity("Student not found").build();
    }

    MatriculationExam exam = matriculationExamDao.findById(examId);
    if (exam == null) {
      return Response.status(Status.BAD_REQUEST).entity("Exam not found").build();
    }
    
    if (!restSecurity.hasPermission(new String[] { UserPermissions.USER_OWNER }, student)) {
      return Response.status(Status.FORBIDDEN).build();
    }
    
    MatriculationExamEnrollment latest = matriculationExamEnrollmentDao.findLatestByExamAndStudent(exam, student);
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
      result.setDegreeType(latest.getDegreeType().name());
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
  
  @Path("/exams/{EXAMID}/enrollments")
  @POST
  @RESTPermit(handling = Handling.INLINE)
  public Response enrollToExam(@PathParam("EXAMID") Long examId, fi.otavanopisto.pyramus.rest.model.MatriculationExamEnrollment enrollment) {
    if (!Objects.equals(examId, enrollment.getExamId()) && examId != null) {
      return Response.status(Status.BAD_REQUEST).entity("Exam ids do not match").build();
    }
    
    Student student = studentDao.findById(enrollment.getStudentId());
    if (student == null) {
      return Response.status(Status.BAD_REQUEST).entity("Student not found").build();
    }

    MatriculationExam exam = matriculationExamDao.findById(enrollment.getExamId());
    
    if (exam == null || !isEligible(student, exam)) {
      return Response.status(Status.BAD_REQUEST)
          .entity("Exam enrollment is closed")
          .build();
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
        exam,
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
        DegreeType.valueOf(enrollment.getDegreeType()),
        enrollment.getNumMandatoryCourses(),
        enrollment.isRestartExam(),
        enrollment.getLocation(),
        enrollment.getMessage(),
        enrollment.isCanPublishName(),
        student,
        MatriculationExamEnrollmentState.valueOf(enrollment.getState()),
        false,
        new Date());
        
      for (MatriculationExamAttendance attendance : enrollment.getAttendances()) {
        MatriculationExamAttendanceStatus status = attendance.getStatus() != null
            ? MatriculationExamAttendanceStatus.valueOf(attendance.getStatus()) : null;
            
        MatriculationExam matriculationExam = enrollmentEntity.getExam();
        // NOTE for ENROLLED the default values are taken from the exam properties if they don't exist in the payload
        Integer year = attendance.getYear() != null ? attendance.getYear() : 
          status == MatriculationExamAttendanceStatus.ENROLLED ? matriculationExam.getExamYear() : null;
        MatriculationExamTerm term = attendance.getTerm() != null ? MatriculationExamTerm.valueOf(attendance.getTerm()) : 
          status == MatriculationExamAttendanceStatus.ENROLLED ? matriculationExam.getExamTerm() : null;
          
        matriculationExamAttendanceDao.create(
          enrollmentEntity,
          MatriculationExamSubject.valueOf(attendance.getSubject()),
          attendance.getMandatory(),
          attendance.getRepeat(),
          year,
          term,
          status,
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

  private boolean isVisible(MatriculationExam matriculationExam, User user) {
    if (matriculationExam == null || matriculationExam.getStarts() == null || matriculationExam.getEnds() == null || !matriculationExam.isEnrollmentActive()) {
      // If dates are not set, exam enrollment is not active
      return false;
    }
    
    // TODO: custom date affects all exams...
    
    UserVariableKey userVariableKey = userVariableKeyDAO.findByVariableKey(USERVARIABLE_PERSONAL_EXAM_ENROLLMENT_EXPIRYDATE);
    String personalExamEnrollmentExpiryStr = userVariableKey != null ? userVariableDAO.findByUserAndKey(user, USERVARIABLE_PERSONAL_EXAM_ENROLLMENT_EXPIRYDATE) : null;
    Date personalExamEnrollmentExpiry = personalExamEnrollmentExpiryStr != null ? DateUtils.endOfDay(new Date(Long.parseLong(personalExamEnrollmentExpiryStr))) : null;

    Date enrollmentStarts = matriculationExam.getStarts();
    Date enrollmentEnds = personalExamEnrollmentExpiry == null ? matriculationExam.getEnds() : 
      new Date(Math.max(matriculationExam.getEnds().getTime(), personalExamEnrollmentExpiry.getTime()));
    
    Date currentDate = new Date();
    return currentDate.after(enrollmentStarts) && currentDate.before(enrollmentEnds);
  }

  private boolean isEligible(Student student, MatriculationExam matriculationExam) {
    return student == null ? false :
      isVisible(matriculationExam, student) && hasGroupEligibility(student);
  }

  /**
   * Returns true if student is in one of the groups mentioned in the setting.
   * 
   * @param student
   * @return
   */
  private boolean hasGroupEligibility(Student student) {
    if (student != null) {
      String eligibleGroupsStr = SettingUtils.getSettingValue(SETTING_ELIGIBLE_GROUPS);
      
      if (StringUtils.isNotBlank(eligibleGroupsStr)) {
        String[] split = StringUtils.split(eligibleGroupsStr, ",");
        
        for (String groupIdentifier : split) {
          if (groupIdentifier.startsWith("STUDYPROGRAMME:")) {
            Long studyProgrammeId = Long.parseLong(groupIdentifier.substring(15));
            if (student.getStudyProgramme() != null && Objects.equals(student.getStudyProgramme().getId(), studyProgrammeId)) {
              return true;
            }
          } else if (groupIdentifier.startsWith("STUDENTGROUP:")) {
            Long studentGroupId = Long.parseLong(groupIdentifier.substring(13));
            StudentGroup studentGroup = studentGroupDAO.findById(studentGroupId);
            
            if (studentGroupStudentDAO.findByStudentGroupAndStudent(studentGroup, student) != null) {
              return true;
            }
          }
        }
      }
    }
    
    return false;
  }

  private fi.otavanopisto.pyramus.rest.model.MatriculationExam restModel(MatriculationExam exam, Student student) {
    fi.otavanopisto.pyramus.rest.model.MatriculationExam result = new fi.otavanopisto.pyramus.rest.model.MatriculationExam();
    result.setId(exam.getId());
    result.setStarts(exam.getStarts().getTime());
    result.setEnds(exam.getEnds().getTime());
    if (student != null) {
      result.setEligible(isEligible(student, exam));
      
      MatriculationExamEnrollment examEnrollment = matriculationExamEnrollmentDao.findLatestByExamAndStudent(exam, student);
      result.setEnrolled(examEnrollment != null);
      result.setEnrollmentDate((examEnrollment != null && examEnrollment.getEnrollmentDate() != null) ? 
          examEnrollment.getEnrollmentDate().getTime() : null);
    } else {
      result.setEligible(false);
      result.setEnrolled(false);
      result.setEnrollmentDate(null);
    }
    return result;
  }

}
