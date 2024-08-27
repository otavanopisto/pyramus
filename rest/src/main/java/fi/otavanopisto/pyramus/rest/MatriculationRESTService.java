package fi.otavanopisto.pyramus.rest;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamEnrollmentChangeLogDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamEnrollmentDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamSubjectSettingsDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.domainmodel.matriculation.DegreeType;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExam;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamAttendance;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamEnrollment;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamEnrollmentChangeLog;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamEnrollmentDegreeStructure;
import fi.otavanopisto.pyramus.domainmodel.matriculation.SchoolType;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentStudyPeriodType;
import fi.otavanopisto.pyramus.domainmodel.users.StudentParent;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamAttendanceStatus;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamEnrollmentChangeLogType;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamEnrollmentState;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamGrade;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamListFilter;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamTerm;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit.Handling;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit.Style;
import fi.otavanopisto.pyramus.rest.controller.MatriculationEligibilityController;
import fi.otavanopisto.pyramus.rest.controller.StudentController;
import fi.otavanopisto.pyramus.rest.controller.StudentMatriculationEligibilityResultOPS2021;
import fi.otavanopisto.pyramus.rest.controller.permissions.StudentPermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.UserPermissions;
import fi.otavanopisto.pyramus.rest.model.MatriculationEligibilities;
import fi.otavanopisto.pyramus.rest.model.MatriculationExamStudentStatus;
import fi.otavanopisto.pyramus.rest.model.StudentMatriculationEligibilityOPS2021;
import fi.otavanopisto.pyramus.rest.security.RESTSecurity;
import fi.otavanopisto.pyramus.rest.util.PyramusRestUtils;
import fi.otavanopisto.pyramus.security.impl.SessionController;
import fi.otavanopisto.security.LoggedIn;

@Path("/matriculation")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateful
@RequestScoped
public class MatriculationRESTService extends AbstractRESTService {

  @Inject
  private Logger logger;

  @Inject
  private SessionController sessionController;

  @Inject
  private RESTSecurity restSecurity;

  @Inject
  private MatriculationExamDAO matriculationExamDao;

  @Inject
  private MatriculationExamEnrollmentDAO matriculationExamEnrollmentDao;

  @Inject
  private MatriculationExamEnrollmentChangeLogDAO matriculationExamEnrollmentChangeLogDAO;

  @Inject
  private MatriculationExamSubjectSettingsDAO matriculationExamSubjectSettingsDAO;
  
  @Inject
  private StudentDAO studentDao;

  @Inject
  private MatriculationExamAttendanceDAO matriculationExamAttendanceDao;

  @Inject
  private StudentController studentController;

  @Inject
  private MatriculationEligibilityController matriculationEligibilityController;

  @Inject
  private ObjectFactory objectFactory;

  @Path("/eligibility")
  @GET
  @LoggedIn
  @RESTPermit(handling = Handling.INLINE)
  public Response listEligibilities() {
    User loggedUser = sessionController.getUser();

    boolean upperSecondarySchoolCurriculum = false;

    if (loggedUser instanceof Student) {
      Student loggedStudent = (Student) loggedUser;

      upperSecondarySchoolCurriculum = matriculationEligibilityController.hasGroupEligibility(loggedStudent);
    }

    return Response.ok(new MatriculationEligibilities(upperSecondarySchoolCurriculum)).build();
  }

  @Path("/students/{STUDENTID}/eligibility")
  @GET
  @LoggedIn
  @RESTPermit(handling = Handling.INLINE)
  public Response listEligibilities(@PathParam("STUDENTID") Long studentId) {
    Student student = studentController.findStudentById(studentId);
    if (student == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    User loggedUser = sessionController.getUser();
    if (!loggedUser.getId().equals(studentId)) {
      if (!((loggedUser instanceof StudentParent) && (((StudentParent) loggedUser).isActiveParentOf(student)))) {
        return Response.status(Status.NOT_FOUND).build();
      }
    }
    
    boolean upperSecondarySchoolCurriculum = matriculationEligibilityController.hasGroupEligibility(student);
    return Response.ok(new MatriculationEligibilities(upperSecondarySchoolCurriculum)).build();
  }

  @Path("/students/{STUDENTID}/exams")
  @GET
  @LoggedIn
  @RESTPermit(handling = Handling.INLINE)
  public Response listStudentsExams(@PathParam("STUDENTID") Long studentId, @QueryParam("filter") @DefaultValue("ALL") MatriculationExamListFilter filter) {
    Student student = studentController.findStudentById(studentId);
    if (student == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    User loggedUser = sessionController.getUser();
    if (!loggedUser.getId().equals(studentId)) {
      if (!((loggedUser instanceof StudentParent) && (((StudentParent) loggedUser).isActiveParentOf(student)))) {
        return Response.status(Status.NOT_FOUND).build();
      }
    }
    
    List<MatriculationExam> studentExams = matriculationEligibilityController.listExamsByStudent(student, filter);
    
    return Response.ok(
        studentExams.stream()
          .map(exam -> restModel(exam, student))
          .collect(Collectors.toList())
      ).build();
  }

  @Path("/students/{STUDENTID}/exams/{EXAMID}/enrollment")
  @GET
  @LoggedIn
  @RESTPermit(handling = Handling.INLINE)
  public Response getStudentsExamEnrollment(@PathParam("STUDENTID") Long studentId, @PathParam("EXAMID") Long examId) {
    Student student = studentController.findStudentById(studentId);
    if (student == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    MatriculationExam exam = matriculationExamDao.findById(examId);
    if (exam == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    MatriculationExamEnrollment examEnrollment = matriculationExamEnrollmentDao.findByExamAndStudent(exam, student);
    if (examEnrollment == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    User loggedUser = sessionController.getUser();
    if (!loggedUser.getId().equals(studentId)) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    return Response.ok(restModel(examEnrollment)).build();
  }

  @Path("/students/{STUDENTID}/exams/{EXAMID}/enrollment/changelog")
  @GET
  @LoggedIn
  @RESTPermit(handling = Handling.INLINE)
  public Response listStudentExamEnrollmentChangeLog(@PathParam("STUDENTID") Long studentId, @PathParam("EXAMID") Long examId) {
    Student student = studentController.findStudentById(studentId);
    if (student == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    MatriculationExam exam = matriculationExamDao.findById(examId);
    if (exam == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    MatriculationExamEnrollment examEnrollment = matriculationExamEnrollmentDao.findByExamAndStudent(exam, student);
    if (examEnrollment == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    User loggedUser = sessionController.getUser();
    if (!loggedUser.getId().equals(studentId)) {
      return Response.status(Status.NOT_FOUND).build();
    }

    List<MatriculationExamEnrollmentChangeLog> changeLog = matriculationEligibilityController.listEnrollmentChangeLog(examEnrollment);
    return Response.ok(objectFactory.createModel(changeLog)).build();
  }

  @Path("/students/{STUDENTID}/exams/{EXAMID}/enrollment/state")
  @PUT
  @LoggedIn
  @RESTPermit(handling = Handling.INLINE)
  public Response setStudentsExamEnrollmentState(@PathParam("STUDENTID") Long studentId, @PathParam("EXAMID") Long examId, MatriculationExamEnrollmentState newState) {
    Student student = studentController.findStudentById(studentId);
    if (student == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    MatriculationExam exam = matriculationExamDao.findById(examId);
    if (exam == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    MatriculationExamEnrollment examEnrollment = matriculationExamEnrollmentDao.findByExamAndStudent(exam, student);
    if (examEnrollment == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    User loggedUser = sessionController.getUser();
    if (!loggedUser.getId().equals(studentId)) {
      return Response.status(Status.NOT_FOUND).build();
    }

    // Student is only allowed to change status from APPROVED to CONFIRMED
    if (examEnrollment.getState() != MatriculationExamEnrollmentState.APPROVED) {
      return Response.status(Status.FORBIDDEN).entity("Matriculation exam enrollment not in applicable state.").build();
    }
    
    if (newState != MatriculationExamEnrollmentState.CONFIRMED) {
      return Response.status(Status.FORBIDDEN).entity("Matriculation exam enrollment can only be confirmed via this operation.").build();
    }
    else {
      // TODO ????
//      // Update enrollment state
//      examEnrollment = matriculationEligibilityController.setEnrollmentState(examEnrollment, newState, loggedUser);
     
      // Update enrollment state
      examEnrollment = matriculationExamEnrollmentDao.updateState(examEnrollment, newState);
      
      // Make a log entry for state change with new state
      matriculationExamEnrollmentChangeLogDAO.create(examEnrollment, loggedUser, MatriculationExamEnrollmentChangeLogType.STATE_CHANGED, newState, null);

      return Response.ok(restModel(examEnrollment)).build();
    }
  }

//  @Path("/exams")
//  @GET
//  @RESTPermit(MatriculationPermissions.LIST_EXAMS)
//  public Response listExams(@QueryParam("onlyEligible") Boolean onlyEligible) {
//    User loggedUser = sessionController.getUser();
//    Student student = loggedUser instanceof Student ? (Student) loggedUser : null;
//    List<MatriculationExam> exams = matriculationExamDao.listAll();
//    Stream<MatriculationExam> examStream = exams.stream().filter(exam -> matriculationEligibilityController.isVisible(exam, loggedUser));
//
//    if (onlyEligible) {
//      if (student != null) {
//        examStream = examStream.filter(exam -> matriculationEligibilityController.isEligible(student, exam));
//      } else {
//        // Caller is not student so they can't be eligible to enroll any exams
//        return Response.ok(Collections.emptyList()).build();
//      }
//    }
//
//    return Response.ok(
//        examStream
//        .map(exam -> restModel(exam, student))
//        .collect(Collectors.toList())
//      ).build();
//  }

//  @Path("/exams/{EXAMID}/enrollments/latest/{STUDENTID}")
//  @GET
//  @RESTPermit(handling = Handling.INLINE)
//  public Response getLatestEnrollment(@PathParam("EXAMID") Long examId, @PathParam("STUDENTID") Long studentId) {
//    Student student = studentDao.findById(studentId);
//    if (student == null) {
//      return Response.status(Status.BAD_REQUEST).entity("Student not found").build();
//    }
//
//    MatriculationExam exam = matriculationExamDao.findById(examId);
//    if (exam == null) {
//      return Response.status(Status.BAD_REQUEST).entity("Exam not found").build();
//    }
//
//    if (!restSecurity.hasPermission(new String[] { UserPermissions.USER_OWNER }, student)) {
//      return Response.status(Status.FORBIDDEN).build();
//    }
//
//    MatriculationExamEnrollment latest = matriculationExamEnrollmentDao.findLatestByExamAndStudent(exam, student);
//    if (latest == null) {
//      return Response.status(Status.NOT_FOUND).entity("No enrollments for student").build();
//    } else {
//      fi.otavanopisto.pyramus.rest.model.MatriculationExamEnrollment result =
//          new fi.otavanopisto.pyramus.rest.model.MatriculationExamEnrollment();
//      String enrollmentDate = null;
//      if (latest.getEnrollmentDate() != null) {
//        enrollmentDate = latest.getEnrollmentDate().toString();
//      }
//      result.setName(latest.getName());
//      result.setSsn(latest.getSsn());
//      result.setEmail(latest.getEmail());
//      result.setPhone(latest.getPhone());
//      result.setAddress(latest.getAddress());
//      result.setPostalCode(latest.getPostalCode());
//      result.setCity(latest.getCity());
//      result.setNationalStudentNumber(latest.getNationalStudentNumber());
//      result.setGuider(latest.getGuider());
//      result.setEnrollAs(latest.getEnrollAs().name());
//      result.setDegreeType(latest.getDegreeType().name());
//      result.setNumMandatoryCourses(latest.getNumMandatoryCourses());
//      result.setRestartExam(latest.isRestartExam());
//      result.setLocation(latest.getLocation());
//      result.setMessage(latest.getMessage());
//      result.setCanPublishName(latest.isCanPublishName());
//      result.setStudentId(latest.getStudent().getId());
//      result.setState(matriculationEligibilityController.translateState(latest.getState()));
//      result.setEnrollmentDate(enrollmentDate);
//      return Response.ok(result).build();
//    }
//  }

  @Path("/students/{STUDENTID}/exams/{EXAMID}/enrollment")
  @POST
  @RESTPermit(handling = Handling.INLINE)
  public Response saveOrUpdateExamEnrollment(@PathParam("STUDENTID") Long studentId, @PathParam("EXAMID") Long examId, fi.otavanopisto.pyramus.rest.model.MatriculationExamEnrollment enrollment) {
    if (examId == null || !Objects.equals(examId, enrollment.getExamId())) {
      return Response.status(Status.BAD_REQUEST).entity("Exam ids do not match").build();
    }

    if (studentId == null || !Objects.equals(studentId, enrollment.getStudentId())) {
      return Response.status(Status.BAD_REQUEST).entity("Student ids do not match").build();
    }

    Student student = studentDao.findById(studentId);
    if (student == null) {
      return Response.status(Status.BAD_REQUEST).entity("Student not found").build();
    }

    MatriculationExam exam = matriculationExamDao.findById(examId);

    if (exam == null || !matriculationEligibilityController.isEligible(student, exam)) {
      return Response.status(Status.BAD_REQUEST)
          .entity("Exam enrollment is closed")
          .build();
    }

    if (!restSecurity.hasPermission(new String[] { UserPermissions.USER_OWNER }, student)) {
      return Response.status(Status.FORBIDDEN).build();
    }
    
    // Find the enrollment by exam and student, if it exists, we are updating
    MatriculationExamEnrollment existingEnrollment = matriculationExamEnrollmentDao.findByExamAndStudent(exam, student);

    /*
     * Validation
     */
    
    // If we're modifying, validate the payload's enrollmentId
    if (existingEnrollment != null && !Objects.equals(existingEnrollment.getId(), enrollment.getId())) {
      return Response.status(Status.BAD_REQUEST).entity("Enrollment ids do not match").build();
    }
    
    /*
     * If we're creating a new enrollment, only allowed status is PENDING
     * If we're modifying an existing enrollment, allowed status' are x y and z
     * 
     * TODOOOOOO
     */
    
    MatriculationExamEnrollmentState enrollmentState = matriculationEligibilityController.translateState(enrollment.getState());

    EnumSet<MatriculationExamEnrollmentState> allowedStates = existingEnrollment == null 
        ? EnumSet.of(MatriculationExamEnrollmentState.PENDING)
        : EnumSet.of(MatriculationExamEnrollmentState.SUPPLEMENTED);
    
    if (!allowedStates.contains(enrollmentState)) {
      return Response.status(Status.BAD_REQUEST)
          .entity("Can only send pending enrollments via REST")
          .build();
    }

    if (enrollment.getAttendances() == null) {
      return Response.status(Status.BAD_REQUEST).entity("Attendances list is not present").build();
    }

    for (fi.otavanopisto.pyramus.rest.model.MatriculationExamAttendance attendance : enrollment.getAttendances()) {
      if (attendance.getStatus() == null) {
        return Response.status(Status.BAD_REQUEST).entity("Attendance missing status").build();
      }
      
      /*
       * Validate term and year. For finished and planned attendances they just need to exist,
       * while for enrolled they either need to match the exam's term and year or need to be null,
       * in which case they will be set to the exam's values automatically.
       */
      switch (attendance.getStatus()) {
        case FINISHED:
        case PLANNED:
          if (attendance.getTerm() == null) {
            return Response.status(Status.BAD_REQUEST).entity("Attendance missing term").build();
          }
          if (attendance.getYear() == null) {
            return Response.status(Status.BAD_REQUEST).entity("Attendance missing year").build();
          }
        break;
        
        case ENROLLED:
          if (attendance.getTerm() != null && !Objects.equals(attendance.getTerm(), exam.getExamTerm())) {
            return Response.status(Status.BAD_REQUEST).entity("Enrolled attendance term doesn't match the exam's term").build();
          }
          if (attendance.getYear() != null && !Objects.equals(attendance.getYear(), exam.getExamYear())) {
            return Response.status(Status.BAD_REQUEST).entity("Enrolled attendance year doesn't match the exam's year").build();
          }
        break;
      }
    }

    /*
     * Create or modify the entities
     */
    
    MatriculationExamEnrollment enrollmentEntity;
    
    try {
      if (existingEnrollment == null) {
        /*
         * Create new enrollment
         */
        
        enrollmentEntity = matriculationExamEnrollmentDao.create(
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
          enrollmentState,
          MatriculationExamEnrollmentDegreeStructure.valueOf(enrollment.getDegreeStructure()),
          new Date());
  
        matriculationExamEnrollmentChangeLogDAO.create(enrollmentEntity, student, MatriculationExamEnrollmentChangeLogType.ENROLLMENT_CREATED, null, null);
        
        for (fi.otavanopisto.pyramus.rest.model.MatriculationExamAttendance attendance : enrollment.getAttendances()) {
          MatriculationExam matriculationExam = enrollmentEntity.getExam();
          // NOTE for ENROLLED the default values are taken from the exam properties if they don't exist in the payload
          Integer year = attendance.getYear() != null ? attendance.getYear() : 
            attendance.getStatus() == MatriculationExamAttendanceStatus.ENROLLED ? matriculationExam.getExamYear() : null;
          MatriculationExamTerm term = attendance.getTerm() != null ? attendance.getTerm() : 
            attendance.getStatus() == MatriculationExamAttendanceStatus.ENROLLED ? matriculationExam.getExamTerm() : null;
  
          matriculationExamAttendanceDao.create(
            enrollmentEntity,
            attendance.getSubject(),
            attendance.getMandatory(),
            attendance.getRepeat(),
            year,
            term,
            attendance.getStatus(),
            attendance.getFunding(),
            attendance.getGrade());
        }
      }
      else {
        /*
         * Modify existing enrollment
         */
        
        MatriculationExamEnrollmentChangeLogType changeLogChangeType = MatriculationExamEnrollmentChangeLogType.ENROLLMENT_UPDATED;
        MatriculationExamEnrollmentState changeLogNewState = null;
        
        enrollmentEntity = matriculationExamEnrollmentDao.update(
          existingEnrollment,
          enrollment.getName(),
          enrollment.getSsn(),
          enrollment.getEmail(),
          enrollment.getPhone(),
          enrollment.getAddress(),
          enrollment.getPostalCode(),
          enrollment.getCity(),
          enrollment.getGuider(),
          SchoolType.valueOf(enrollment.getEnrollAs()),
          DegreeType.valueOf(enrollment.getDegreeType()),
          enrollment.getNumMandatoryCourses(),
          enrollment.isRestartExam(),
          enrollment.getLocation(),
          enrollment.getMessage(),
          enrollment.isCanPublishName(),
          student,
          MatriculationExamEnrollmentDegreeStructure.valueOf(enrollment.getDegreeStructure()));
  
        if (enrollmentState != enrollmentEntity.getState()) {
          changeLogNewState = enrollmentState;
          enrollmentEntity = matriculationExamEnrollmentDao.updateState(enrollmentEntity, enrollmentState);
        }
        
        matriculationExamEnrollmentChangeLogDAO.create(enrollmentEntity, student, changeLogChangeType, changeLogNewState, null);
        
        for (fi.otavanopisto.pyramus.rest.model.MatriculationExamAttendance attendance : enrollment.getAttendances()) {
          Long attendanceId = attendance.getId();
          
          MatriculationExamAttendance existingAttendance = attendanceId != null ? matriculationExamAttendanceDao.findById(attendanceId) : null;
          
          MatriculationExam matriculationExam = enrollmentEntity.getExam();
          // NOTE for ENROLLED the default values are taken from the exam properties if they don't exist in the payload
          Integer year = attendance.getYear() != null ? attendance.getYear() : 
            attendance.getStatus() == MatriculationExamAttendanceStatus.ENROLLED ? matriculationExam.getExamYear() : null;
          MatriculationExamTerm term = attendance.getTerm() != null ? attendance.getTerm() : 
            attendance.getStatus() == MatriculationExamAttendanceStatus.ENROLLED ? matriculationExam.getExamTerm() : null;
  
          if (existingAttendance == null) {
            /*
             * No existing attendance, create new
             */
            matriculationExamAttendanceDao.create(
              enrollmentEntity,
              attendance.getSubject(),
              attendance.getMandatory(),
              attendance.getRepeat(),
              year,
              term,
              attendance.getStatus(),
              attendance.getFunding(),
              attendance.getGrade());
          }
          else {
            /*
             * Found existing attendance, modify it
             */

            matriculationExamAttendanceDao.update(
              existingAttendance,
              enrollmentEntity,
              attendance.getSubject(),
              attendance.getMandatory(),
              attendance.getRepeat(),
              year,
              term,
              attendance.getStatus(),
              attendance.getFunding(),
              attendance.getGrade());
          }
        }
      }
    } catch (IllegalArgumentException ex) {
      return Response.status(Status.BAD_REQUEST)
          .entity(ex.getMessage())
          .build();
    }

    return Response.ok(restModel(enrollmentEntity)).build();
  }

  @Path("/students/{ID:[0-9]*}/matriculationEligibility")
  @GET
  @RESTPermit(handling = Handling.INLINE)
  public Response getStudentMatriculationEligibility(@PathParam("ID") Long studentId, @QueryParam ("subjectCode") String subjectCode) {
    if (StringUtils.isBlank(subjectCode)) {
      return Response.status(Status.BAD_REQUEST).entity("Subject is required").build();
    }
    
    Student student = studentController.findStudentById(studentId);
    if (student == null) {
      return Response.status(Status.NOT_FOUND).entity("Not found").build();
    }
    
    if (!restSecurity.hasPermission(new String[] { StudentPermissions.FIND_STUDENT, UserPermissions.USER_OWNER, UserPermissions.STUDENT_PARENT }, student, Style.OR)) {
      return Response.status(Status.FORBIDDEN).build();
    }

    try {
      StudentMatriculationEligibilityResultOPS2021 result = matriculationEligibilityController.getStudentMatriculationEligibleOPS2021(student, subjectCode);
      if (result == null) {
        return Response.status(Status.BAD_REQUEST).entity("Could not resolve matriculation eligibility").build();
      } else {
        return Response.ok(new StudentMatriculationEligibilityOPS2021(result.isEligible(), result.getRequiredPassingGradeCourseCreditPoints(), result.getPassingGradeCourseCreditPoints())).build();
      }
    } catch (Exception e) {
      logger.log(Level.SEVERE, "Could not resolve matriculation eligibility", e);
      return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Could not resolve matriculation eligibility").build();
    }
  }

  private fi.otavanopisto.pyramus.rest.model.MatriculationExam restModel(MatriculationExam exam, Student student) {
    fi.otavanopisto.pyramus.rest.model.MatriculationExam result = new fi.otavanopisto.pyramus.rest.model.MatriculationExam();
    result.setId(exam.getId());
    result.setTerm(exam.getExamTerm());
    result.setYear(exam.getExamYear());
    result.setStarts(exam.getStarts().getTime());
    result.setEnds(exam.getEnds().getTime());
    
    if (student != null) {
      boolean compulsoryEducationEligible = false;
      Date maxDate = matriculationExamSubjectSettingsDAO.findMaxExamDate(exam);
      if (maxDate != null) {
        // Check if the student is within compulsory education system for the date of the last exam of an matriculation exam period 
        EnumSet<StudentStudyPeriodType> activeStudyPeriods = studentController.getActiveStudyPeriods(student, maxDate);
        compulsoryEducationEligible = 
            activeStudyPeriods.contains(StudentStudyPeriodType.COMPULSORY_EDUCATION) ||
            activeStudyPeriods.contains(StudentStudyPeriodType.EXTENDED_COMPULSORY_EDUCATION);
      } else {
        logger.severe(String.format("Maximum exam date could'nt be resolved for exam %d", exam.getId()));
      }
      
      result.setCompulsoryEducationEligible(compulsoryEducationEligible);

      // Add information about enrollment if already done so
      
      MatriculationExamEnrollment examEnrollment = matriculationExamEnrollmentDao.findByExamAndStudent(exam, student);
      
      MatriculationExamStudentStatus studentStatus = examEnrollment == null 
          ? (matriculationEligibilityController.isEligible(student, exam) ? MatriculationExamStudentStatus.ELIGIBLE : MatriculationExamStudentStatus.NOT_ELIGIBLE)
          : matriculationEligibilityController.translateState(examEnrollment.getState());
      result.setStudentStatus(studentStatus);
      
      if (examEnrollment != null) {
        result.setEnrollment(restModel(examEnrollment));
      }
    } else {
      result.setStudentStatus(MatriculationExamStudentStatus.NOT_ELIGIBLE);
    }
    
    return result;
  }

  private fi.otavanopisto.pyramus.rest.model.MatriculationExamEnrollment restModel(MatriculationExamEnrollment examEnrollment) {
    fi.otavanopisto.pyramus.rest.model.MatriculationExamEnrollment result = new fi.otavanopisto.pyramus.rest.model.MatriculationExamEnrollment();
    OffsetDateTime enrollmentDate = null;
    
    if (examEnrollment.getEnrollmentDate() != null) {
      enrollmentDate = PyramusRestUtils.toOffsetDateTime(examEnrollment.getEnrollmentDate());
    }
    result.setId(examEnrollment.getId());
    result.setExamId(examEnrollment.getExam().getId());
    result.setName(examEnrollment.getName());
    result.setSsn(examEnrollment.getSsn());
    result.setEmail(examEnrollment.getEmail());
    result.setPhone(examEnrollment.getPhone());
    result.setAddress(examEnrollment.getAddress());
    result.setPostalCode(examEnrollment.getPostalCode());
    result.setCity(examEnrollment.getCity());
    result.setNationalStudentNumber(examEnrollment.getNationalStudentNumber());
    result.setGuider(examEnrollment.getGuider());
    result.setEnrollAs(examEnrollment.getEnrollAs().name());
    result.setDegreeType(examEnrollment.getDegreeType().name());
    result.setNumMandatoryCourses(examEnrollment.getNumMandatoryCourses());
    result.setRestartExam(examEnrollment.isRestartExam());
    result.setLocation(examEnrollment.getLocation());
    result.setMessage(examEnrollment.getMessage());
    result.setCanPublishName(examEnrollment.isCanPublishName());
    result.setStudentId(examEnrollment.getStudent().getId());
    result.setState(matriculationEligibilityController.translateState(examEnrollment.getState()));
    result.setEnrollmentDate(enrollmentDate);
    result.setDegreeStructure(examEnrollment.getDegreeStructure() != null ? examEnrollment.getDegreeStructure().name() : null);
    
    List<MatriculationExamAttendance> attendances = matriculationExamAttendanceDao.listByEnrollment(examEnrollment);
    
    List<fi.otavanopisto.pyramus.rest.model.MatriculationExamAttendance> attendanceRestModels = attendances.stream()
        .map(attendance -> restModel(attendance))
        .collect(Collectors.toList());
    
    result.setAttendances(attendanceRestModels);
    
    return result;
  }

  private fi.otavanopisto.pyramus.rest.model.MatriculationExamAttendance restModel(MatriculationExamAttendance attendance) {
    MatriculationExamGrade grade = null;
    OffsetDateTime gradeDate = null;
    
    // Grade comes from either the student project (for ENROLLED) or the grade field
    if (attendance.getStatus() == MatriculationExamAttendanceStatus.ENROLLED) {
      if (attendance.getProjectAssessment() != null && attendance.getProjectAssessment().getGrade() != null && StringUtils.isNotBlank(attendance.getProjectAssessment().getGrade().getName())) {
        /*
         * Grades don't follow the enum naming so we have to translate them - maybe the values 
         * for enrolled should also be refactored one day to the MatriculationExamAttendance table.
         */
        String gradeName = attendance.getProjectAssessment().getGrade().getName();

        switch (gradeName) {
          case "A":
            grade = MatriculationExamGrade.APPROBATUR;
          break;
          case "C":
            grade = MatriculationExamGrade.CUM_LAUDE_APPROBATUR;
          break;
          case "E":
            grade = MatriculationExamGrade.EXIMIA_CUM_LAUDE_APPROBATUR;
          break;
          case "I":
            grade = MatriculationExamGrade.IMPROBATUR;
          break;
          case "K":
            grade = MatriculationExamGrade.K;
          break;
          case "L":
            grade = MatriculationExamGrade.LAUDATUR;
          break;
          case "B":
            grade = MatriculationExamGrade.LUBENTER_APPROBATUR;
          break;
          case "M":
            grade = MatriculationExamGrade.MAGNA_CUM_LAUDE_APPROBATUR;
          break;
          default:
            // Null should suffice if the grade is unknown - UNKNOWN seems to only make things more confusing
            // grade = MatriculationExamGrade.UNKNOWN;
          break;
        }

        // Include gradeDate only when proper grade exists
        if (grade != null && attendance.getProjectAssessment().getDate() != null) {
          gradeDate = PyramusRestUtils.toOffsetDateTime(attendance.getProjectAssessment().getDate());
        }
      }
    }
    else {
      grade = attendance.getGrade();
    }
    
    fi.otavanopisto.pyramus.rest.model.MatriculationExamAttendance result = new fi.otavanopisto.pyramus.rest.model.MatriculationExamAttendance();
    result.setId(attendance.getId());
    result.setEnrollmentId(attendance.getEnrollment().getId());
    result.setFunding(attendance.getFunding());
    result.setGrade(grade);
    result.setGradeDate(gradeDate);
    result.setMandatory(attendance.isMandatory());
    result.setRepeat(attendance.isRetry());
    result.setStatus(attendance.getStatus());
    result.setSubject(attendance.getSubject());
    result.setTerm(attendance.getTerm());
    result.setYear(attendance.getYear());
    return result;
  }

}
