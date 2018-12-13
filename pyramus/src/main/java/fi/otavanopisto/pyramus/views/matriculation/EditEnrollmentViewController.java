package fi.otavanopisto.pyramus.views.matriculation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fi.internetix.smvc.Severity;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamAttendanceDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamEnrollmentDAO;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamAttendance;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamAttendanceStatus;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamEnrollment;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamEnrollmentState;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamGrade;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamSubject;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamTerm;
import fi.otavanopisto.pyramus.domainmodel.matriculation.SchoolType;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class EditEnrollmentViewController extends PyramusViewController {
  
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
    MatriculationExamEnrollmentDAO enrollmentDAO = DAOFactory.getInstance().getMatriculationExamEnrollmentDAO();
    MatriculationExamAttendanceDAO attendanceDAO = DAOFactory.getInstance().getMatriculationExamAttendanceDAO();
    MatriculationExamEnrollment enrollment = enrollmentDAO.findById(id);
    if (enrollment == null) {
      pageRequestContext.addMessage(Severity.ERROR, "Enrollment not found");
      return;
    }
    enrollmentDAO.update(
      enrollment,
      ObjectUtils.firstNonNull(pageRequestContext.getString("name"), ""),
      ObjectUtils.firstNonNull(pageRequestContext.getString("ssn"), ""),
      ObjectUtils.firstNonNull(pageRequestContext.getString("email"), ""),
      ObjectUtils.firstNonNull(pageRequestContext.getString("phone"), ""),
      ObjectUtils.firstNonNull(pageRequestContext.getString("address"), ""),
      ObjectUtils.firstNonNull(pageRequestContext.getString("postalCode"), ""),
      ObjectUtils.firstNonNull(pageRequestContext.getString("postalOffice"), ""),
      ObjectUtils.firstNonNull(pageRequestContext.getString("guidanceCounselor"), ""),
      SchoolType.valueOf(pageRequestContext.getString("enrollAs")),
      ObjectUtils.firstNonNull(pageRequestContext.getInteger("numMandatoryCourses"), 0),
      pageRequestContext.getBoolean("restartExam"),
      ObjectUtils.firstNonNull(pageRequestContext.getString("location"), ""),
      ObjectUtils.firstNonNull(pageRequestContext.getString("message"), ""),
      pageRequestContext.getBoolean("canPublishName"),
      enrollment.getStudent(),
      MatriculationExamEnrollmentState.valueOf(
        pageRequestContext.getString("state")
      ));
    attendanceDAO.deleteByEnrollment(enrollment);
    Integer enrolledAttendances = pageRequestContext.getInteger("enrolledAttendances.rowCount");
    if (enrolledAttendances == null) {
      enrolledAttendances = 0;
    }
    for (int i=0; i<enrolledAttendances; i++) {
      MatriculationExamSubject subject =
        MatriculationExamSubject.valueOf(pageRequestContext.getString("enrolledAttendances." + i + ".subject"));
      boolean mandatory =
        "MANDATORY".equals(pageRequestContext.getString("enrolledAttendances." + i + ".subject"));
      boolean repeat =
        "REPEAT".equals(pageRequestContext.getString("enrolledAttendances." + i + ".repeat"));
      attendanceDAO.create(enrollment, subject, mandatory, repeat,
        null, null, MatriculationExamAttendanceStatus.ENROLLED, null);
    }
    Integer finishedAttendances = pageRequestContext.getInteger("finishedAttendances.rowCount");
    if (finishedAttendances == null) {
      finishedAttendances = 0;
    }
    for (int i=0; i<finishedAttendances; i++) {
      String termString = pageRequestContext.getString("finishedAttendances." + i + ".term");
      MatriculationExamTerm term = MatriculationExamTerm.valueOf(termString.substring(0, 6));
      int year = Integer.parseInt(termString.substring(6));
      MatriculationExamSubject subject =
        MatriculationExamSubject.valueOf(pageRequestContext.getString("finishedAttendances." + i + ".subject"));
      boolean mandatory =
        "MANDATORY".equals(pageRequestContext.getString("finishedAttendances." + i + ".subject"));
      MatriculationExamGrade grade =
        MatriculationExamGrade.valueOf(pageRequestContext.getString("finishedAttendances." + i + ".grade"));
      attendanceDAO.create(enrollment, subject, mandatory, null, year, term,
        MatriculationExamAttendanceStatus.FINISHED, grade);
    }
    Integer plannedAttendances = pageRequestContext.getInteger("plannedAttendances.rowCount");
    if (plannedAttendances == null) {
      plannedAttendances = 0;
    }
    for (int i=0; i<plannedAttendances; i++) {
      String termString = pageRequestContext.getString("plannedAttendances." + i + ".term");
      MatriculationExamTerm term = MatriculationExamTerm.valueOf(termString.substring(0, 6));
      int year = Integer.parseInt(termString.substring(6));
      MatriculationExamSubject subject =
        MatriculationExamSubject.valueOf(pageRequestContext.getString("plannedAttendances." + i + ".subject"));
      boolean mandatory =
        "MANDATORY".equals(pageRequestContext.getString("plannedAttendances." + i + ".subject"));
      attendanceDAO.create(enrollment, subject, mandatory, null, year, term,
        MatriculationExamAttendanceStatus.PLANNED, null);
    }
    pageRequestContext.setRedirectURL(pageRequestContext.getRequest().getRequestURI() + "?enrollment=" + id);
  }

  private void doGet(PageRequestContext pageRequestContext) {
    pageRequestContext.setIncludeJSP("/templates/matriculation/management-edit-enrollment.jsp");
    
    Long id = pageRequestContext.getLong("enrollment");
    if (id == null) {
      pageRequestContext.addMessage(Severity.ERROR, "Enrollment not found");
      return;
    }
    MatriculationExamEnrollmentDAO enrollmentDAO = DAOFactory.getInstance().getMatriculationExamEnrollmentDAO();
    MatriculationExamAttendanceDAO attendanceDAO = DAOFactory.getInstance().getMatriculationExamAttendanceDAO();

    MatriculationExamEnrollment enrollment = enrollmentDAO.findById(id);
    if (enrollment == null) {
      pageRequestContext.addMessage(Severity.ERROR, "Enrollment not found");
      return;
    }
    
    pageRequestContext.getRequest().setAttribute("name", enrollment.getName());
    pageRequestContext.getRequest().setAttribute("ssn", enrollment.getSsn());
    pageRequestContext.getRequest().setAttribute("email", enrollment.getEmail());
    pageRequestContext.getRequest().setAttribute("phone", enrollment.getPhone());
    pageRequestContext.getRequest().setAttribute("address", enrollment.getAddress());
    pageRequestContext.getRequest().setAttribute("postalCode", enrollment.getPostalCode());
    pageRequestContext.getRequest().setAttribute("postalOffice", enrollment.getCity());
    pageRequestContext.getRequest().setAttribute("nationalStudentNumber", enrollment.getNationalStudentNumber());
    pageRequestContext.getRequest().setAttribute("guidanceCounselor", enrollment.getGuider());
    pageRequestContext.getRequest().setAttribute("enrollAs", enrollment.getEnrollAs().name());
    pageRequestContext.getRequest().setAttribute("numMandatoryCourses", enrollment.getNumMandatoryCourses());
    pageRequestContext.getRequest().setAttribute("restartExam", enrollment.isRestartExam());
    pageRequestContext.getRequest().setAttribute("location", enrollment.getLocation());
    pageRequestContext.getRequest().setAttribute("message", enrollment.getMessage());
    pageRequestContext.getRequest().setAttribute("canPublishName", enrollment.isCanPublishName());
    pageRequestContext.getRequest().setAttribute("state", enrollment.getState().name());
    
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
                attendance.getSubject().name(),
                attendance.isMandatory() ? "MANDATORY" : "OPTIONAL",
                attendance.isRetry() ? "REPEAT" : "FIRST_TIME",
                ""));
        break;
      case FINISHED:
        finishedAttendances.add(
            Arrays.asList(
                attendance.getId(),
                attendance.getTerm().name() + attendance.getYear(),
                attendance.getSubject().name(),
                attendance.isMandatory() ? "MANDATORY" : "OPTIONAL",
                attendance.getGrade().name(),
                ""));
        break;
      case PLANNED:
        plannedAttendances.add(
            Arrays.asList(
                attendance.getId(),
                attendance.getTerm().name() + attendance.getYear(),
                attendance.getSubject().name(),
                attendance.isMandatory() ? "MANDATORY" : "OPTIONAL",
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

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER };
  }

}
