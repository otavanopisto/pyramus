package fi.otavanopisto.pyramus.views.students;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.SubjectDAO;
import fi.otavanopisto.pyramus.dao.grading.GradingScaleDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.students.StudentSubjectGradeDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.grading.GradingScale;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentSubjectGrade;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.StaffMemberProperties;
import fi.otavanopisto.pyramus.framework.UserRole;

public class EditStudentSubjectGradeDialogViewController extends PyramusViewController {

  private static final Logger logger = Logger.getLogger(EditStudentSubjectGradeDialogViewController.class.getName());

  public void process(PageRequestContext requestContext) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();
    StudentSubjectGradeDAO studentSubjectGradeDAO = DAOFactory.getInstance().getStudentSubjectGradeDAO();
    GradingScaleDAO gradingScaleDAO = DAOFactory.getInstance().getGradingScaleDAO();

    Long studentId = requestContext.getLong("studentId");
    Long subjectId = requestContext.getLong("subjectId");
    
    if (studentId == null || subjectId == null) {
      logger.log(Level.WARNING, String.format("Unable to load view due to missing studentId (%d) or subjectId (%d).", studentId, subjectId));
      try {
        requestContext.getResponse().sendError(HttpServletResponse.SC_BAD_REQUEST);
      } catch (Exception e) {
        logger.log(Level.SEVERE, "Couldn't send response", e);
      }
      return;
    }
    
    Student student = studentDAO.findById(studentId);
    Subject subject = subjectDAO.findById(subjectId);
    StudentSubjectGrade studentSubjectGrade = studentSubjectGradeDAO.findBy(student, subject);
    
    List<GradingScale> gradingScales = gradingScaleDAO.listUnarchived();

    List<StaffMember> studyApprovers = staffMemberDAO.listByProperty(StaffMemberProperties.STUDY_APPROVER.getKey(), "1");
    StaffMember selectedStudyApprover = studentSubjectGrade != null && studentSubjectGrade.getGradeApprover() != null ? studentSubjectGrade.getGradeApprover() : null;
    if (selectedStudyApprover != null) {
      Long selectedStudyApproverId = selectedStudyApprover.getId();
      
      boolean isSelectedInList = studyApprovers.stream()
        .map(StaffMember::getId)
        .anyMatch(selectedStudyApproverId::equals);
      
      if (!isSelectedInList) {
        studyApprovers.add(selectedStudyApprover);
      }
    }
    studyApprovers.sort(Comparator.comparing(StaffMember::getLastName).thenComparing(StaffMember::getFirstName));
    
    requestContext.getRequest().setAttribute("student", student);
    requestContext.getRequest().setAttribute("subject", subject);
    requestContext.getRequest().setAttribute("studentSubjectGrade", studentSubjectGrade);
    requestContext.getRequest().setAttribute("gradingScales", gradingScales);
    requestContext.getRequest().setAttribute("studyApprovers", studyApprovers);
    
    requestContext.setIncludeJSP("/templates/students/editstudentsubjectgradedialog.jsp");
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
