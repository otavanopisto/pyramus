package fi.otavanopisto.pyramus.json.students;

import java.util.HashMap;
import java.util.Map;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.SubjectDAO;
import fi.otavanopisto.pyramus.dao.grading.GradeDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.students.StudentSubjectGradeDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.grading.Grade;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentSubjectGrade;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class EditStudentSubjectGradeJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext jsonRequestContext) {
    StudentSubjectGradeDAO studentSubjectGradeDAO = DAOFactory.getInstance().getStudentSubjectGradeDAO();
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();
    GradeDAO gradeDAO = DAOFactory.getInstance().getGradeDAO();
    
    try {
      Long studentId = jsonRequestContext.getLong("studentId");
      Long subjectId = jsonRequestContext.getLong("subjectId");
      Long gradeId = jsonRequestContext.getLong("gradeId");
      
      Student student = studentDAO.findById(studentId);
      Subject subject = subjectDAO.findById(subjectId);
      Grade grade = gradeId != null ? gradeDAO.findById(gradeId) : null;
      boolean useComputed = true;
      
      StudentSubjectGrade studentSubjectGrade = studentSubjectGradeDAO.findBy(student, subject);
      
      if (grade != null) {
        useComputed = false;
        
        if (studentSubjectGrade == null) {
          studentSubjectGrade = studentSubjectGradeDAO.create(student, subject, grade);
        } else {
          studentSubjectGrade = studentSubjectGradeDAO.updateGrade(studentSubjectGrade, grade);
        }
      } else {
        // Grade is null, delete if it exists
        if (studentSubjectGrade != null) {
          studentSubjectGradeDAO.delete(studentSubjectGrade);
        }
      }
      
      Map<String, Object> info = new HashMap<>();
      info.put("computed", useComputed);
      info.put("id", studentSubjectGrade.getId());
      info.put("gradeId", studentSubjectGrade.getGrade().getId());
      info.put("gradeName", studentSubjectGrade.getGrade().getName());
      jsonRequestContext.addResponseParameter("results", info);
    } catch (Exception e) {
      throw new SmvcRuntimeException(e);
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
