package fi.otavanopisto.pyramus.json.settings;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.students.StudentStudyEndReasonDAO;
import fi.otavanopisto.pyramus.domainmodel.students.StudentStudyEndReason;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class DeleteStudyEndReasonJSONRequestController extends JSONRequestController {
  
  public void process(JSONRequestContext requestContext) {
    StudentStudyEndReasonDAO studentStudyEndReasonDAO = DAOFactory.getInstance().getStudentStudyEndReasonDAO();
    
    Long studentStudyEndReasonId = requestContext.getLong("studyEndReason");
    StudentStudyEndReason studentStudyEndReason = studentStudyEndReasonDAO.findById(studentStudyEndReasonId);
    studentStudyEndReasonDAO.delete(studentStudyEndReason);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
