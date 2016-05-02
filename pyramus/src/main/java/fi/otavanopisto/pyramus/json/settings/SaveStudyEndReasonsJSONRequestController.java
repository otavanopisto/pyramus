package fi.otavanopisto.pyramus.json.settings;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.students.StudentStudyEndReasonDAO;
import fi.otavanopisto.pyramus.domainmodel.students.StudentStudyEndReason;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class SaveStudyEndReasonsJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext jsonRequestContext) {
    StudentStudyEndReasonDAO studentStudyEndReasonDAO = DAOFactory.getInstance().getStudentStudyEndReasonDAO();    

    int rowCount = NumberUtils.createInteger(jsonRequestContext.getRequest().getParameter("studyEndReasonsTable.rowCount")).intValue();
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "studyEndReasonsTable." + i;
      Long studyEndReasonId = jsonRequestContext.getLong(colPrefix + ".studyEndReasonId");
      String name = jsonRequestContext.getString(colPrefix + ".name");
      Long parentReasonId = jsonRequestContext.getLong(colPrefix + ".parentReasonId");
      StudentStudyEndReason parentReason = null;
      if (parentReasonId != null)
        parentReason = studentStudyEndReasonDAO.findById(parentReasonId);
      boolean modified = jsonRequestContext.getInteger(colPrefix + ".modified") == 1;

      if (studyEndReasonId == -1) {
        studentStudyEndReasonDAO.create(parentReason, name); 
      }
      else if (modified) {
        StudentStudyEndReason studyEndReason = studentStudyEndReasonDAO.findById(studyEndReasonId);
        studentStudyEndReasonDAO.updateName(studyEndReason, name);
        studentStudyEndReasonDAO.updateParentReason(studyEndReason, parentReason);
      }
    }
    jsonRequestContext.setRedirectURL(jsonRequestContext.getReferer(true));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
