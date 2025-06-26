package fi.otavanopisto.pyramus.views.matriculation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fi.internetix.smvc.Severity;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamAttendanceDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamEnrollmentChangeLogDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamEnrollmentDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.students.StudentGroupDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.matriculation.DegreeType;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExam;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamAttendance;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamEnrollment;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamEnrollmentChangeLog;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamEnrollmentDegreeStructure;
import fi.otavanopisto.pyramus.domainmodel.matriculation.SchoolType;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroup;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroupUser;
import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamAttendanceFunding;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamAttendanceStatus;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamEnrollmentChangeLogType;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamEnrollmentState;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamGrade;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamSubject;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamTerm;

public class EditEnrollmentViewController extends PyramusViewController {
  
  private static final Long NEW_ROW_ID = Long.valueOf(-1); 
  
  public void process(PageRequestContext pageRequestContext) {
    switch (pageRequestContext.getRequest().getMethod()) {
    case "GET":
      doGet(pageRequestContext);
      break;
    case "POST":
      doPost(pageRequestContext);
      break;
    }
  }
  
  private void doPost(PageRequestContext pageRequestContext) {
    pageRequestContext.setIncludeJSP("/templates/matriculation/management-edit-enrollment.jsp");

    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    MatriculationExamDAO examDAO = DAOFactory.getInstance().getMatriculationExamDAO();
    MatriculationExamEnrollmentDAO enrollmentDAO = DAOFactory.getInstance().getMatriculationExamEnrollmentDAO();
    MatriculationExamAttendanceDAO attendanceDAO = DAOFactory.getInstance().getMatriculationExamAttendanceDAO();
    MatriculationExamEnrollmentChangeLogDAO matriculationExamEnrollmentChangeLogDAO = DAOFactory.getInstance().getMatriculationExamEnrollmentChangeLogDAO();

    StaffMember loggedUser = staffMemberDAO.findById(pageRequestContext.getLoggedUserId());

    MatriculationExamEnrollmentState enrollmentState = MatriculationExamEnrollmentState.valueOf(
        pageRequestContext.getString("state"));
    
    Long enrollmentId = pageRequestContext.getLong("enrollment");
    MatriculationExamEnrollment enrollment = enrollmentId != null ? enrollmentDAO.findById(enrollmentId) : null;
    if (enrollment != null) {
      // Edit existing enrollment

      EnumSet<MatriculationExamEnrollmentState> allowedStates = getAllowedStates(loggedUser, enrollment.getState());

      if (!allowedStates.contains(enrollmentState)) {
        pageRequestContext.addMessage(Severity.ERROR, "Cannot change state to the requested one");
        return;
      }
      
      enrollment = enrollmentDAO.update(
          enrollment,
          SchoolType.valueOf(pageRequestContext.getString("enrollAs")),
          DegreeType.valueOf(pageRequestContext.getString("degreeType")),
          ObjectUtils.firstNonNull(pageRequestContext.getInteger("numMandatoryCourses"), 0),
          pageRequestContext.getBoolean("restartExam"),
          ObjectUtils.firstNonNull(pageRequestContext.getString("location"), ""),
          ObjectUtils.firstNonNull(pageRequestContext.getString("contactInfoChange"), ""),
          ObjectUtils.firstNonNull(pageRequestContext.getString("message"), ""),
          pageRequestContext.getBoolean("canPublishName"),
          enrollment.getStudent(),
          MatriculationExamEnrollmentDegreeStructure.valueOf(pageRequestContext.getString("degreeStructure"))
      );
    }
    else {
      // Create new enrollment
      
      Long studentId = pageRequestContext.getLong("student");
      Student student = studentId != null ? studentDAO.findById(studentId) : null;
      if (student == null) {
        pageRequestContext.addMessage(Severity.ERROR, "Student not found");
        return;
      }
      
      Long examId = pageRequestContext.getLong("examId");
      MatriculationExam exam = examId != null ? examDAO.findById(examId) : null;
      if (exam == null) {
        pageRequestContext.addMessage(Severity.ERROR, "Exam not found");
        return;
      }
      
      enrollment = enrollmentDAO.create(
          exam,
          pageRequestContext.getLong("nationalStudentNumber"),
          SchoolType.valueOf(pageRequestContext.getString("enrollAs")),
          DegreeType.valueOf(pageRequestContext.getString("degreeType")),
          ObjectUtils.firstNonNull(pageRequestContext.getInteger("numMandatoryCourses"), 0),
          pageRequestContext.getBoolean("restartExam"),
          ObjectUtils.firstNonNull(pageRequestContext.getString("location"), ""),
          ObjectUtils.firstNonNull(pageRequestContext.getString("contactInfoChange"), ""),
          ObjectUtils.firstNonNull(pageRequestContext.getString("message"), ""),
          pageRequestContext.getBoolean("canPublishName"),
          student,
          enrollmentState,
          MatriculationExamEnrollmentDegreeStructure.valueOf(pageRequestContext.getString("degreeStructure")),
          new Date()
      );
    }
    

    MatriculationExamEnrollmentChangeLogType changeLogChangeType = MatriculationExamEnrollmentChangeLogType.ENROLLMENT_UPDATED;
    MatriculationExamEnrollmentState changeLogNewState = null;

    if (enrollmentState != enrollment.getState()) {
      changeLogNewState = enrollmentState;
      enrollment = enrollmentDAO.updateState(enrollment, enrollmentState);
    }

    StaffMember handler = pageRequestContext.getLong("handler") != null ? staffMemberDAO.findById(pageRequestContext.getLong("handler")) : null;
    enrollment = enrollmentDAO.updateHandler(enrollment, handler);

    String handlerNotes = StringUtils.isNotBlank(pageRequestContext.getString("handlerNotes")) ? pageRequestContext.getString("handlerNotes") : null;
    enrollment = enrollmentDAO.updateHandlerNotes(enrollment, handlerNotes);
    
    String changeLogMessage = StringUtils.isNotBlank(pageRequestContext.getString("changeLogMessage")) ? pageRequestContext.getString("changeLogMessage") : null;
    matriculationExamEnrollmentChangeLogDAO.create(enrollment, loggedUser, changeLogChangeType, changeLogNewState, changeLogMessage);

    Integer enrolledAttendances = pageRequestContext.getInteger("enrolledAttendances.rowCount");
    if (enrolledAttendances == null) {
      enrolledAttendances = 0;
    }
    for (int i=0; i<enrolledAttendances; i++) {
      Long attendanceId = pageRequestContext.getLong("enrolledAttendances." + i + ".attendanceId");
      attendanceId = attendanceId != null ? attendanceId : -1;
      
      MatriculationExamSubject subject =
        MatriculationExamSubject.valueOf(pageRequestContext.getString("enrolledAttendances." + i + ".subject"));
      Boolean mandatory = parseMandatory(pageRequestContext.getString("enrolledAttendances." + i + ".mandatority"));
      boolean repeat =
        "REPEAT".equals(pageRequestContext.getString("enrolledAttendances." + i + ".repeat"));
      MatriculationExamAttendanceFunding funding = parseFunding(pageRequestContext.getString("enrolledAttendances." + i + ".funding"));      

      MatriculationExamAttendance examAttendance;
      if (NEW_ROW_ID.equals(attendanceId)) {
        // NOTE: new ENROLLED attendances are tied to the year and term defined in the exam properties
        MatriculationExam matriculationExam = enrollment.getExam();
        Integer year = matriculationExam.getExamYear();
        MatriculationExamTerm term = matriculationExam.getExamTerm();
        
        examAttendance = attendanceDAO.create(enrollment, subject, mandatory, repeat,
            year, term, MatriculationExamAttendanceStatus.ENROLLED, funding, null);
      } else {
        examAttendance = attendanceDAO.findById(attendanceId);
        examAttendance = attendanceDAO.update(examAttendance, enrollment, subject, mandatory, repeat,
            examAttendance.getYear(), examAttendance.getTerm(), examAttendance.getStatus(), funding, examAttendance.getGrade());
      }
    }
    
    Integer finishedAttendances = pageRequestContext.getInteger("finishedAttendances.rowCount");
    if (finishedAttendances == null) {
      finishedAttendances = 0;
    }
    for (int i=0; i<finishedAttendances; i++) {
      Long attendanceId = pageRequestContext.getLong("finishedAttendances." + i + ".attendanceId");
      attendanceId = attendanceId != null ? attendanceId : -1;

      String termString = pageRequestContext.getString("finishedAttendances." + i + ".term");
      MatriculationExamTerm term = MatriculationExamTerm.valueOf(termString.substring(0, 6));
      int year = Integer.parseInt(termString.substring(6));
      MatriculationExamSubject subject =
        MatriculationExamSubject.valueOf(pageRequestContext.getString("finishedAttendances." + i + ".subject"));
      Boolean mandatory = parseMandatory(pageRequestContext.getString("finishedAttendances." + i + ".mandatority"));
      MatriculationExamGrade grade =
        MatriculationExamGrade.valueOf(pageRequestContext.getString("finishedAttendances." + i + ".grade"));
      MatriculationExamAttendanceFunding funding = parseFunding(pageRequestContext.getString("finishedAttendances." + i + ".funding"));      
      
      if (NEW_ROW_ID.equals(attendanceId)) {
        attendanceDAO.create(enrollment, subject, mandatory, null, year, term,
          MatriculationExamAttendanceStatus.FINISHED, funding, grade);
      } else {
        MatriculationExamAttendance examAttendance = attendanceDAO.findById(attendanceId);
        attendanceDAO.update(examAttendance, enrollment, subject, mandatory, null, year, term,
            MatriculationExamAttendanceStatus.FINISHED, funding, grade);
      }
    }
    
    Integer plannedAttendances = pageRequestContext.getInteger("plannedAttendances.rowCount");
    if (plannedAttendances == null) {
      plannedAttendances = 0;
    }
    for (int i=0; i<plannedAttendances; i++) {
      Long attendanceId = pageRequestContext.getLong("plannedAttendances." + i + ".attendanceId");
      attendanceId = attendanceId != null ? attendanceId : -1;

      String termString = pageRequestContext.getString("plannedAttendances." + i + ".term");
      MatriculationExamTerm term = MatriculationExamTerm.valueOf(termString.substring(0, 6));
      int year = Integer.parseInt(termString.substring(6));
      MatriculationExamSubject subject =
        MatriculationExamSubject.valueOf(pageRequestContext.getString("plannedAttendances." + i + ".subject"));
      Boolean mandatory = parseMandatory(pageRequestContext.getString("plannedAttendances." + i + ".mandatority"));
      MatriculationExamAttendanceFunding funding = parseFunding(pageRequestContext.getString("plannedAttendances." + i + ".funding"));      

      if (NEW_ROW_ID.equals(attendanceId)) {
        attendanceDAO.create(enrollment, subject, mandatory, null, year, term,
          MatriculationExamAttendanceStatus.PLANNED, funding, null);
      } else {
        MatriculationExamAttendance examAttendance = attendanceDAO.findById(attendanceId);
        attendanceDAO.update(examAttendance, enrollment, subject, mandatory, null, year, term,
            MatriculationExamAttendanceStatus.PLANNED, funding, null);
      }
    }
    
    pageRequestContext.setRedirectURL(pageRequestContext.getRequest().getRequestURI() + "?enrollment=" + enrollment.getId());
  }

  private MatriculationExamAttendanceFunding parseFunding(String value) {
    return StringUtils.isNotBlank(value) ? MatriculationExamAttendanceFunding.valueOf(value) : null;
  }
  
  private Boolean parseMandatory(String value) {
    return "MANDATORY".equals(value) ? Boolean.TRUE : 
      "OPTIONAL".equals(value) ? Boolean.FALSE : null;
  }
  
  private String mandatoryToString(Boolean mandatory) {
    return Boolean.TRUE.equals(mandatory) ? "MANDATORY" :
      Boolean.FALSE.equals(mandatory) ? "OPTIONAL" : null;
  }
  
  private void doGet(PageRequestContext pageRequestContext) {
    pageRequestContext.setIncludeJSP("/templates/matriculation/management-edit-enrollment.jsp");

    MatriculationExamDAO examDAO = DAOFactory.getInstance().getMatriculationExamDAO();
    MatriculationExamAttendanceDAO attendanceDAO = DAOFactory.getInstance().getMatriculationExamAttendanceDAO();
    MatriculationExamEnrollmentDAO enrollmentDAO = DAOFactory.getInstance().getMatriculationExamEnrollmentDAO();
    MatriculationExamEnrollmentChangeLogDAO enrollmentChangeLogDAO = DAOFactory.getInstance().getMatriculationExamEnrollmentChangeLogDAO();
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    StudentGroupDAO studentGroupDAO = DAOFactory.getInstance().getStudentGroupDAO();

    StaffMember loggedUser = staffMemberDAO.findById(pageRequestContext.getLoggedUserId());

    String formMode;
    Student student;
    // The staff member which should be included as an option for the handler of the enrollment
    StaffMember selectedHandler;
    MatriculationExamEnrollmentState currentEnrollmentState;
    List<MatriculationExamWithParticipationInfo> exams = null;

    Long enrollmentId = pageRequestContext.getLong("enrollment");
    MatriculationExamEnrollment enrollment = enrollmentId != null ? enrollmentDAO.findById(enrollmentId) : null;
    if (enrollment != null) {
      formMode = "EDIT";
      student = enrollment.getStudent();
      selectedHandler = enrollment.getHandler() != null ? enrollment.getHandler() : null;
      currentEnrollmentState = enrollment.getState();
    }
    else {
      // Creating new enrollment on behalf of the student
      formMode = "NEW";

      Long studentId = pageRequestContext.getLong("student");
      student = studentId != null ? studentDAO.findById(studentId) : null;
      if (student == null) {
        pageRequestContext.addMessage(Severity.ERROR, "Enrollment not found");
        return;
      }

      // Make sure the logged in user is available as the handler for enrollment
      selectedHandler = loggedUser;
      // Default state for new enrollment on behalf of the student
      currentEnrollmentState = MatriculationExamEnrollmentState.FILLED_ON_BEHALF;

      // Collect the exams and their enrollment info
      exams = new ArrayList<MatriculationExamWithParticipationInfo>();
      List<MatriculationExam> allExams = examDAO.listSorted();
      for (MatriculationExam matriculationExam : allExams) {
        MatriculationExamEnrollment examEnrollment = enrollmentDAO.findByExamAndStudent(matriculationExam, student);
        exams.add(new MatriculationExamWithParticipationInfo(matriculationExam, examEnrollment));
      }
    }

    EnumSet<Role> activeStaffMemberRoles = EnumSet.of(Role.ADMINISTRATOR, Role.STUDY_PROGRAMME_LEADER, Role.MANAGER, Role.STUDY_GUIDER, Role.TEACHER);

    List<StaffMember> enrollmentHandlers = staffMemberDAO.listActiveStaffMembersByRole(activeStaffMemberRoles);
    if (selectedHandler != null) {
      Long selectedStudyApproverId = selectedHandler.getId();
      
      boolean isSelectedInList = enrollmentHandlers.stream()
        .map(StaffMember::getId)
        .anyMatch(selectedStudyApproverId::equals);
      
      if (!isSelectedInList) {
        enrollmentHandlers.add(selectedHandler);
      }
    }
    enrollmentHandlers.sort(Comparator.comparing(StaffMember::getLastName).thenComparing(StaffMember::getFirstName));

    EnumSet<MatriculationExamEnrollmentState> allowedStates = getAllowedStates(loggedUser, currentEnrollmentState);
    Set<String> allowedStatesStrSet = allowedStates.stream().map(MatriculationExamEnrollmentState::name).collect(Collectors.toSet());
    
    List<MatriculationExamEnrollmentChangeLog> changeLog = enrollment != null ? enrollmentChangeLogDAO.listByEnrollment(enrollment) : Collections.emptyList();

    // Group and study advisors are listed from the StudentGroups
    
    List<StudentGroup> studentGroups = studentGroupDAO.listByStudent(student);
    List<StudentGroupUser> groupAdvisors = studentGroups.stream()
        .filter(studentGroup -> Boolean.TRUE.equals(studentGroup.getGuidanceGroup()))
        .flatMap(studentGroup -> studentGroup.getUsers().stream())
        .filter(studentGroupUser -> studentGroupUser.isGroupAdvisor())
        .collect(Collectors.toList());
    List<StudentGroupUser> studyAdvisors = studentGroups.stream()
      .filter(studentGroup -> Boolean.TRUE.equals(studentGroup.getGuidanceGroup()))
      .flatMap(studentGroup -> studentGroup.getUsers().stream())
      .filter(studentGroupUser -> studentGroupUser.isStudyAdvisor())
      .collect(Collectors.toList());
    
    pageRequestContext.getRequest().setAttribute("formMode", formMode);
    pageRequestContext.getRequest().setAttribute("exams", exams);
    pageRequestContext.getRequest().setAttribute("enrollment", enrollment);
    pageRequestContext.getRequest().setAttribute("student", student);
    pageRequestContext.getRequest().setAttribute("changeLog", changeLog);
    pageRequestContext.getRequest().setAttribute("groupAdvisors", groupAdvisors);
    pageRequestContext.getRequest().setAttribute("studyAdvisors", studyAdvisors);
    pageRequestContext.getRequest().setAttribute("numMandatoryCourses", enrollment != null ? enrollment.getNumMandatoryCourses() : 0);
    pageRequestContext.getRequest().setAttribute("restartExam", enrollment != null ? enrollment.isRestartExam() : false);
    pageRequestContext.getRequest().setAttribute("state", currentEnrollmentState.name());
    pageRequestContext.getRequest().setAttribute("enrollmentDate", enrollment != null ? enrollment.getEnrollmentDate() : new Date());
    pageRequestContext.getRequest().setAttribute("allowedStates", allowedStatesStrSet);
    pageRequestContext.getRequest().setAttribute("handlers", enrollmentHandlers);
    
    List<MatriculationExamAttendance> attendances = attendanceDAO.listByEnrollment(enrollment);
    
    List<List<Object>> enrolledAttendances = new ArrayList<>();
    List<List<Object>> finishedAttendances = new ArrayList<>();
    List<List<Object>> plannedAttendances = new ArrayList<>();
    
    for (MatriculationExamAttendance attendance : attendances) {
      switch (attendance.getStatus()) {
      case ENROLLED:
        enrolledAttendances.add(
            Arrays.asList(
                attendance.getId(),
                attendance.getTerm().name() + attendance.getYear(),
                attendance.getSubject().name(),
                mandatoryToString(attendance.isMandatory()),
                attendance.isRetry() ? "REPEAT" : "FIRST_TIME",
                attendance.getFunding() != null ? attendance.getFunding().toString() : "",
                (attendance.getProjectAssessment() != null && attendance.getProjectAssessment().getDate() != null) ? 
                    attendance.getProjectAssessment().getDate().getTime() : null,
                ""));
        break;
      case FINISHED:
        finishedAttendances.add(
            Arrays.asList(
                attendance.getId(),
                attendance.getTerm().name() + attendance.getYear(),
                attendance.getSubject().name(),
                mandatoryToString(attendance.isMandatory()),
                attendance.getFunding() != null ? attendance.getFunding().toString() : "",
                attendance.getGrade().name(),
                ""));
        break;
      case PLANNED:
        plannedAttendances.add(
            Arrays.asList(
                attendance.getId(),
                attendance.getTerm().name() + attendance.getYear(),
                attendance.getSubject().name(),
                mandatoryToString(attendance.isMandatory()),
                ""));
        break;
      default:
        break;
      }      
    }

    ObjectMapper om = new ObjectMapper();
    try {
      setJsDataVariable(
          pageRequestContext,
          "enrolledAttendances",
          om.writeValueAsString(enrolledAttendances));
      setJsDataVariable(
          pageRequestContext,
          "finishedAttendances",
          om.writeValueAsString(finishedAttendances));
      setJsDataVariable(
          pageRequestContext,
          "plannedAttendances",
          om.writeValueAsString(plannedAttendances));
    } catch (JsonProcessingException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Returns the allowed states the student can be set to by the given user
   * and their privileges.
   * 
   * @param loggedUser
   * @param currentState
   * @return
   */
  private EnumSet<MatriculationExamEnrollmentState> getAllowedStates(StaffMember loggedUser,
      MatriculationExamEnrollmentState currentState) {
    EnumSet<MatriculationExamEnrollmentState> allowedStates;
    if (loggedUser.hasAnyRole(Role.ADMINISTRATOR, Role.STUDY_PROGRAMME_LEADER)) {
      allowedStates = EnumSet.allOf(MatriculationExamEnrollmentState.class);
    }
    else {
      // Limit the states the study guiders etc with less permissions can change the enrollment to
      // By default, add the current state so the form can be saved without changing state
      allowedStates = EnumSet.of(currentState);

      switch (currentState) {
        case PENDING:
        case SUPPLEMENTATION_REQUEST:
        case SUPPLEMENTED:
          allowedStates.add(MatriculationExamEnrollmentState.SUPPLEMENTATION_REQUEST);
          allowedStates.add(MatriculationExamEnrollmentState.APPROVED);
          allowedStates.add(MatriculationExamEnrollmentState.REJECTED);
        break;
        
        default:
          // Any other state does not allow modification if the user doesn't have sufficient privileges.
        break;
      }
    }
    
    return allowedStates;
  }

  public class MatriculationExamWithParticipationInfo {
    public MatriculationExamWithParticipationInfo(MatriculationExam exam, MatriculationExamEnrollment enrollment) {
      this.exam = exam;
      this.enrollment = enrollment;
    }
    
    public MatriculationExam getExam() {
      return exam;
    }
    
    public MatriculationExamEnrollment getEnrollment() {
      return enrollment;
    }
    
    private final MatriculationExam exam;
    private final MatriculationExamEnrollment enrollment;
  }
  
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER };
  }

}
