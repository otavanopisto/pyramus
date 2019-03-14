package fi.otavanopisto.pyramus.json.matriculation;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.grading.ProjectAssessmentDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamAttendanceDAO;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamAttendance;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class ArchiveMatriculationAttendanceJSONRequestController extends JSONRequestController {
  
  public void process(JSONRequestContext requestContext) {
    MatriculationExamAttendanceDAO examAttendanceDAO = DAOFactory.getInstance().getMatriculationExamAttendanceDAO();
    ProjectAssessmentDAO projectAssessmentDAO = DAOFactory.getInstance().getProjectAssessmentDAO();
    Long examAttendanceId = requestContext.getLong("examAttendanceId");
    
    MatriculationExamAttendance examAttendance = examAttendanceDAO.findById(examAttendanceId);
    
    if (examAttendance.getProjectAssessment() != null) {
      projectAssessmentDAO.archive(examAttendance.getProjectAssessment());
    }
    
    examAttendanceDAO.delete(examAttendance);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
