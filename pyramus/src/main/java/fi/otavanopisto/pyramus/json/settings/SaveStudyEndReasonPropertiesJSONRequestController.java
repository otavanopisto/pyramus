package fi.otavanopisto.pyramus.json.settings;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.students.StudentStudyEndReasonDAO;
import fi.otavanopisto.pyramus.domainmodel.students.StudentStudyEndReason;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.StudentStudyEndReasonProperties;
import fi.otavanopisto.pyramus.framework.UserRole;

public class SaveStudyEndReasonPropertiesJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext requestContext) {
    StudentStudyEndReasonDAO studyEndReasonDAO = DAOFactory.getInstance().getStudentStudyEndReasonDAO();
    Long studyEndReasonId = requestContext.getLong("studyEndReasonId");
    StudentStudyEndReason studyEndReason = studyEndReasonDAO.findById(studyEndReasonId);

    int rowCount = requestContext.getInteger("studyEndReasonPropertiesTable.rowCount");
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "studyEndReasonPropertiesTable." + i;
      String key = requestContext.getString(colPrefix + ".key");
      String value = requestContext.getString(colPrefix + ".value");
      
      if (StudentStudyEndReasonProperties.isProperty(key)) {
        studyEndReason.getProperties().put(key, value);
      }
    }
    
    studyEndReasonDAO.update(studyEndReason);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
