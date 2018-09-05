package fi.otavanopisto.pyramus.views.matriculation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

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
  
  private static final Logger logger = Logger.getLogger(EditEnrollmentViewController.class.getName());
  
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
      pageRequestContext.getString("name"),
      pageRequestContext.getString("ssn"),
      pageRequestContext.getString("email"),
      pageRequestContext.getString("phone"),
      pageRequestContext.getString("address"),
      pageRequestContext.getString("postalCode"),
      pageRequestContext.getString("postalOffice"),
      pageRequestContext.getLong("nationalStudentNumber"),
      pageRequestContext.getString("guidanceCounselor"),
      SchoolType.valueOf(pageRequestContext.getString("enrollAs")),
      pageRequestContext.getInteger("numMandatoryCourses"),
      pageRequestContext.getBoolean("restartExam"),
      pageRequestContext.getString("location"),
      pageRequestContext.getString("message"),
      pageRequestContext.getBoolean("canPulishName"),
      null,
      MatriculationExamEnrollmentState.PENDING);
    attendanceDAO.deleteByEnrollment(enrollment);
    int enrolledAttendances = pageRequestContext.getInteger("enrolledAttendances.rowCount");
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
    int finishedAttendances = pageRequestContext.getInteger("finishedAttendances.rowCount");
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
    int plannedAttendances = pageRequestContext.getInteger("plannedAttendances.rowCount");
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
                attendance.isRepeat() ? "REPEAT" : "FIRST_TIME",
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
