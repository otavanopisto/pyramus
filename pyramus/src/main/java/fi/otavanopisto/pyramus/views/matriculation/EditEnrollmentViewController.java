package fi.otavanopisto.pyramus.views.matriculation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamEnrollmentChangeLogDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamEnrollmentDAO;
import fi.otavanopisto.pyramus.dao.students.StudentGroupDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.matriculation.DegreeType;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExam;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamAttendance;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamEnrollment;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamEnrollmentChangeLog;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamEnrollmentDegreeStructure;
import fi.otavanopisto.pyramus.domainmodel.matriculation.SchoolType;
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
    Long id = pageRequestContext.getLong("enrollment");
    if (id == null) {
      pageRequestContext.addMessage(Severity.ERROR, "Enrollment not found");
      return;
    }
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    MatriculationExamEnrollmentDAO enrollmentDAO = DAOFactory.getInstance().getMatriculationExamEnrollmentDAO();
    MatriculationExamAttendanceDAO attendanceDAO = DAOFactory.getInstance().getMatriculationExamAttendanceDAO();
    MatriculationExamEnrollmentChangeLogDAO matriculationExamEnrollmentChangeLogDAO = DAOFactory.getInstance().getMatriculationExamEnrollmentChangeLogDAO();
    MatriculationExamEnrollment enrollment = enrollmentDAO.findById(id);
    if (enrollment == null) {
      pageRequestContext.addMessage(Severity.ERROR, "Enrollment not found");
      return;
    }
    
    StaffMember loggedUser = staffMemberDAO.findById(pageRequestContext.getLoggedUserId());
    EnumSet<MatriculationExamEnrollmentState> allowedStates = getAllowedStates(loggedUser, enrollment.getState());

    MatriculationExamEnrollmentState enrollmentState = MatriculationExamEnrollmentState.valueOf(
        pageRequestContext.getString("state"));
    
    if (!allowedStates.contains(enrollmentState)) {
      pageRequestContext.addMessage(Severity.ERROR, "Cannot change state to the requested one");
      return;
    }

    MatriculationExamEnrollmentChangeLogType changeLogChangeType = MatriculationExamEnrollmentChangeLogType.ENROLLMENT_UPDATED;
    MatriculationExamEnrollmentState changeLogNewState = null;

    enrollment = enrollmentDAO.update(
      enrollment,
      ObjectUtils.firstNonNull(pageRequestContext.getString("guidanceCounselor"), ""),
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
    
    pageRequestContext.setRedirectURL(pageRequestContext.getRequest().getRequestURI() + "?enrollment=" + id);
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

    Long id = pageRequestContext.getLong("enrollment");
    if (id == null) {
      pageRequestContext.addMessage(Severity.ERROR, "Enrollment not found");
      return;
    }
    MatriculationExamAttendanceDAO attendanceDAO = DAOFactory.getInstance().getMatriculationExamAttendanceDAO();
    MatriculationExamEnrollmentDAO enrollmentDAO = DAOFactory.getInstance().getMatriculationExamEnrollmentDAO();
    MatriculationExamEnrollmentChangeLogDAO enrollmentChangeLogDAO = DAOFactory.getInstance().getMatriculationExamEnrollmentChangeLogDAO();
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    StudentGroupDAO studentGroupDAO = DAOFactory.getInstance().getStudentGroupDAO();

    MatriculationExamEnrollment enrollment = enrollmentDAO.findById(id);
    if (enrollment == null) {
      pageRequestContext.addMessage(Severity.ERROR, "Enrollment not found");
      return;
    }

    EnumSet<Role> activeStaffMemberRoles = EnumSet.of(Role.ADMINISTRATOR, Role.STUDY_PROGRAMME_LEADER, Role.MANAGER, Role.STUDY_GUIDER, Role.TEACHER);

    List<StaffMember> enrollmentHandlers = staffMemberDAO.listActiveStaffMembersByRole(activeStaffMemberRoles);
    StaffMember selectedHandler = enrollment.getHandler() != null ? enrollment.getHandler() : null;
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

    StaffMember loggedUser = staffMemberDAO.findById(pageRequestContext.getLoggedUserId());
    
    EnumSet<MatriculationExamEnrollmentState> allowedStates = getAllowedStates(loggedUser, enrollment.getState());
    Set<String> allowedStatesStrSet = allowedStates.stream().map(MatriculationExamEnrollmentState::name).collect(Collectors.toSet());
    
    List<MatriculationExamEnrollmentChangeLog> changeLog = enrollmentChangeLogDAO.listByEnrollment(enrollment);

    String fullName = enrollment.getStudent().getFullName();
    String email = enrollment.getStudent().getPrimaryEmail() != null ? enrollment.getStudent().getPrimaryEmail().getAddress() : null;
    String phoneNumber = enrollment.getStudent().getPrimaryPhoneNumber() != null ? enrollment.getStudent().getPrimaryPhoneNumber().getNumber() : null;

    List<StudentGroup> studentGroups = studentGroupDAO.listByStudent(enrollment.getStudent());
    List<StudentGroupUser> studyAdvisors = studentGroups.stream()
      .filter(studentGroup -> Boolean.TRUE.equals(studentGroup.getGuidanceGroup()))
      .flatMap(studentGroup -> studentGroup.getUsers().stream())
      .filter(studentGroupUser -> studentGroupUser.isStudyAdvisor())
      .collect(Collectors.toList());
    
    pageRequestContext.getRequest().setAttribute("enrollment", enrollment);
    pageRequestContext.getRequest().setAttribute("changeLog", changeLog);
    pageRequestContext.getRequest().setAttribute("name", fullName);
    pageRequestContext.getRequest().setAttribute("email", email);
    pageRequestContext.getRequest().setAttribute("phone", phoneNumber);
    pageRequestContext.getRequest().setAttribute("nationalStudentNumber", enrollment.getNationalStudentNumber());
    pageRequestContext.getRequest().setAttribute("guidanceCounselor", enrollment.getGuider());
    pageRequestContext.getRequest().setAttribute("studyAdvisors", studyAdvisors);
    pageRequestContext.getRequest().setAttribute("enrollAs", enrollment.getEnrollAs().name());
    pageRequestContext.getRequest().setAttribute("numMandatoryCourses", enrollment.getNumMandatoryCourses());
    pageRequestContext.getRequest().setAttribute("restartExam", enrollment.isRestartExam());
    pageRequestContext.getRequest().setAttribute("location", enrollment.getLocation());
    pageRequestContext.getRequest().setAttribute("message", enrollment.getMessage());
    pageRequestContext.getRequest().setAttribute("canPublishName", enrollment.isCanPublishName());
    pageRequestContext.getRequest().setAttribute("state", enrollment.getState().name());
    pageRequestContext.getRequest().setAttribute("degreeStructure", enrollment.getDegreeStructure().name());
    pageRequestContext.getRequest().setAttribute("enrollmentDate", enrollment.getEnrollmentDate());
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

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER };
  }

}
