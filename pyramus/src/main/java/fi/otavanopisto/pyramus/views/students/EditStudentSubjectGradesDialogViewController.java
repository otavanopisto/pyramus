package fi.otavanopisto.pyramus.views.students;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.grading.GradingScaleDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.students.StudentSubjectGradeDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.grading.Grade;
import fi.otavanopisto.pyramus.domainmodel.grading.GradingScale;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentSubjectGrade;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.framework.PyramusStatusCode;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.StaffMemberProperties;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.framework.UserUtils;
import fi.otavanopisto.pyramus.tor.StudentTOR;
import fi.otavanopisto.pyramus.tor.StudentTORController;
import fi.otavanopisto.pyramus.tor.StudentTORController.StudentTORHandling;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class EditStudentSubjectGradesDialogViewController extends PyramusViewController {

  private static final Logger logger = Logger.getLogger(EditStudentSubjectGradesDialogViewController.class.getName());
      
  /**
   * Processes the page request by including the corresponding JSP page to the response. 
   * 
   * @param requestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    StudentSubjectGradeDAO studentSubjectGradeDAO = DAOFactory.getInstance().getStudentSubjectGradeDAO();

    StaffMember loggedUser = staffMemberDAO.findById(pageRequestContext.getLoggedUserId());

    Student student = studentDAO.findById(pageRequestContext.getLong("studentId"));
    
    // TODO permission ?

    if (!UserUtils.canAccessOrganization(loggedUser, student.getOrganization())) {
      throw new SmvcRuntimeException(PyramusStatusCode.UNAUTHORIZED, "Cannot access users' organization.");
    }

    try {
      StudentTOR tor = StudentTORController.constructStudentTOR(student, StudentTORHandling.CURRICULUM_MOVE_INCLUDED);
      
      ObjectMapper mapper = new ObjectMapper();
      StringWriter writer = new StringWriter();
      mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
      
      mapper.writeValue(writer, tor);

      String requestStr = writer.toString();
      setJsDataVariable(pageRequestContext, "subjectCredits", requestStr);
      
    } catch (Exception ex) {
      logger.log(Level.SEVERE, String.format("Failed to construct TOR for student %d", student.getId()), ex);
    } 

    // StudentSubjectGrades
    
    List<StudentSubjectGrade> studentSubjectGrades = studentSubjectGradeDAO.listByStudent(student);
    JSONObject studentSubjectGradesJSON = new JSONObject();
    for (StudentSubjectGrade studentSubjectGrade : studentSubjectGrades) {
      JSONObject studentSubjectGradeJSON = new JSONObject();
      studentSubjectGradeJSON.put("subjectId", studentSubjectGrade.getSubject() != null ? studentSubjectGrade.getSubject().getId() : null);
      studentSubjectGradeJSON.put("gradeId", studentSubjectGrade.getGrade() != null ? studentSubjectGrade.getGrade().getId() : null);
      studentSubjectGradeJSON.put("gradeApproverId", studentSubjectGrade.getGradeApprover() != null ? studentSubjectGrade.getGradeApprover().getId() : null);
      studentSubjectGradeJSON.put("gradeDate", studentSubjectGrade.getGradeDate() != null ? studentSubjectGrade.getGradeDate().getTime() : null);
      studentSubjectGradesJSON.put(studentSubjectGrade.getSubject().getId(), studentSubjectGradeJSON);
    }
    setJsDataVariable(pageRequestContext, "studentSubjectGrades", studentSubjectGradesJSON.toString());
    
    // Study approvers list in ixTable compatible json
    
    // StaffMembers currently set as study approvers
    List<StaffMember> studyApprovers = staffMemberDAO.listByProperty(StaffMemberProperties.STUDY_APPROVER.getKey(), "1");

    // List of StaffMembers that have previously been selected for the StudentSubjectGrade
    // These are added to the list so that they are available to be selected again from the form
    List<StaffMember> selectedStudyApprovers = studentSubjectGrades.stream()
        .map(StudentSubjectGrade::getGradeApprover)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
    for (StaffMember selectedStudyApprover : selectedStudyApprovers) {
      Long selectedStudyApproverId = selectedStudyApprover.getId();
      
      boolean isSelectedInList = studyApprovers.stream()
        .map(StaffMember::getId)
        .anyMatch(selectedStudyApproverId::equals);
      
      if (!isSelectedInList) {
        studyApprovers.add(selectedStudyApprover);
      }
    }
    studyApprovers.sort(Comparator.comparing(StaffMember::getLastName).thenComparing(StaffMember::getFirstName));

    JSONArray studyApproversJSON = new JSONArray();
    for (StaffMember studyApprover : studyApprovers) {
      StringBuilder approverName = new StringBuilder(studyApprover.getFullName());
      if (StringUtils.isNotBlank(studyApprover.getTitle())) {
        approverName
          .append(" (")
          .append(studyApprover.getTitle())
          .append(")");
      }
      
      JSONObject studyApproverJSON = new JSONObject();
      studyApproverJSON.put("text", approverName.toString());
      studyApproverJSON.put("value", studyApprover.getId());
      studyApproversJSON.add(studyApproverJSON);
    }
    setJsDataVariable(pageRequestContext, "studyApprovers", studyApproversJSON.toString());
    
    // Grades in ixTable compatible json
    
    GradingScaleDAO gradingScaleDAO = DAOFactory.getInstance().getGradingScaleDAO();

    List<GradingScale> gradingScales = gradingScaleDAO.listUnarchived();
    JSONArray gradesJSON = new JSONArray();
    for (GradingScale gradingScale : gradingScales) {
      JSONObject gradingScaleJSON = new JSONObject();
      
      JSONArray gradeArrayJSON = new JSONArray();
      for (Grade grade : gradingScale.getGrades()) {
        JSONObject gradeJSON = new JSONObject();
        gradeJSON.put("text", grade.getName());
        gradeJSON.put("value", grade.getId());
        gradeArrayJSON.add(gradeJSON);
      }
      
      gradingScaleJSON.put("text", gradingScale.getName());
      gradingScaleJSON.put("optionGroup", true);
      gradingScaleJSON.put("options", gradeArrayJSON);
      gradesJSON.add(gradingScaleJSON);
    }
    setJsDataVariable(pageRequestContext, "grades", gradesJSON.toString());
    
    pageRequestContext.getRequest().setAttribute("student", student);
    pageRequestContext.getRequest().setAttribute("studyApprovers", studyApprovers);
    
    pageRequestContext.setIncludeJSP("/templates/students/editstudentsubjectgradesdialog.jsp");
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
