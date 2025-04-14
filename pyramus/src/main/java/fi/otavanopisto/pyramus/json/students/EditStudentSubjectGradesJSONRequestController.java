package fi.otavanopisto.pyramus.json.students;

import java.util.Date;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.SubjectDAO;
import fi.otavanopisto.pyramus.dao.grading.GradeDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.students.StudentSubjectGradeDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.grading.Grade;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentSubjectGrade;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.util.ixtable.PyramusIxTableFacade;
import fi.otavanopisto.pyramus.util.ixtable.PyramusIxTableRowFacade;

public class EditStudentSubjectGradesJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext requestContext) {
    StudentSubjectGradeDAO studentSubjectGradeDAO = DAOFactory.getInstance().getStudentSubjectGradeDAO();
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();
    GradeDAO gradeDAO = DAOFactory.getInstance().getGradeDAO();
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    
    try {
      Long studentId = requestContext.getLong("studentId");
      
      PyramusIxTableFacade gradesTable = PyramusIxTableFacade.from(requestContext, "studentSubjectGradesTable");
      
      for (PyramusIxTableRowFacade gradeRow : gradesTable.rows()) {
        if ("1".equals(gradeRow.getString("edited"))) {
          Long subjectId = gradeRow.getLong("subjectId");
          Long gradeId = gradeRow.getLong("gradeId");
          Date gradeDate = gradeRow.getDate("gradeDate");
          Long gradeApproverId = gradeRow.getLong("gradeApproverId");

          StaffMember issuer = staffMemberDAO.findById(requestContext.getLoggedUserId());
          StaffMember gradeApprover = staffMemberDAO.findById(gradeApproverId);
  
          Student student = studentDAO.findById(studentId);
          Subject subject = subjectDAO.findById(subjectId);
          Grade grade = gradeDAO.findById(gradeId);
          
          StudentSubjectGrade studentSubjectGrade = studentSubjectGradeDAO.findBy(student, subject);
          if (studentSubjectGrade == null) {
            studentSubjectGrade = studentSubjectGradeDAO.create(student, subject, issuer, grade, gradeDate, gradeApprover, "");
          } else {
            studentSubjectGrade = studentSubjectGradeDAO.updateGrade(studentSubjectGrade, issuer, grade, gradeDate, gradeApprover, studentSubjectGrade.getExplanation());
          }
        }        
      }
    } catch (Exception e) {
      throw new SmvcRuntimeException(e);
    }
    
    requestContext.setRedirectURL(requestContext.getReferer(true));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
