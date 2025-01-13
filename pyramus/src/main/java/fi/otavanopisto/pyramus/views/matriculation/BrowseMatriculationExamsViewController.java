package fi.otavanopisto.pyramus.views.matriculation;

import java.util.EnumSet;
import java.util.List;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamAttendanceDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamEnrollmentDAO;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExam;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamAttendanceStatus;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamEnrollmentState;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class BrowseMatriculationExamsViewController extends PyramusViewController {
  
  public void process(PageRequestContext pageRequestContext) {
    MatriculationExamDAO examDAO = DAOFactory.getInstance().getMatriculationExamDAO();
    MatriculationExamEnrollmentDAO examEnrollmentDAO = DAOFactory.getInstance().getMatriculationExamEnrollmentDAO();
    MatriculationExamAttendanceDAO examAttendanceDAO = DAOFactory.getInstance().getMatriculationExamAttendanceDAO();
    
    List<MatriculationExam> exams = examDAO.listAll();
    
    JSONArray jsonMatriculationExams = new JSONArray();
    for (MatriculationExam exam : exams) {
      Long totalEnrollments = examEnrollmentDAO.countEnrollments(exam, EnumSet.allOf(MatriculationExamEnrollmentState.class));
      Long confirmedEnrollments = examEnrollmentDAO.countEnrollments(exam, EnumSet.of(MatriculationExamEnrollmentState.CONFIRMED));
      Long rejectedEnrollments = examEnrollmentDAO.countEnrollments(exam, EnumSet.of(MatriculationExamEnrollmentState.REJECTED));
      Long enrolledConfirmedAttendances = examAttendanceDAO.countEnrollments(exam, EnumSet.of(MatriculationExamEnrollmentState.CONFIRMED), EnumSet.of(MatriculationExamAttendanceStatus.ENROLLED));
      
      JSONObject obj = new JSONObject();
      obj.put("id", exam.getId());
      obj.put("examYear", exam.getExamYear());
      obj.put("examTerm", exam.getExamTerm());
      obj.put("starts", exam.getStarts() != null ? exam.getStarts().getTime() : null);
      obj.put("ends", exam.getEnds() != null ? exam.getEnds().getTime() : null);
      obj.put("enrollmentActive", exam.isEnrollmentActive());
      obj.put("totalEnrollments", totalEnrollments);
      obj.put("confirmedEnrollments", confirmedEnrollments);
      obj.put("rejectedEnrollments", rejectedEnrollments);
      obj.put("enrolledConfirmedAttendances", enrolledConfirmedAttendances);
      jsonMatriculationExams.add(obj);
    }
    setJsDataVariable(pageRequestContext, "matriculationExams", jsonMatriculationExams.toString());
    
    pageRequestContext.setIncludeJSP("/templates/matriculation/management-browse-exams.jsp");
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER };
  }

}
