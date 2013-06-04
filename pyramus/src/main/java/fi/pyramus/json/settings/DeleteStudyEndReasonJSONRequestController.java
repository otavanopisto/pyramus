package fi.pyramus.json.settings;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.students.StudentStudyEndReasonDAO;
import fi.pyramus.domainmodel.students.StudentStudyEndReason;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

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
