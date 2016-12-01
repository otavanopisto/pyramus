package fi.otavanopisto.pyramus.views.system.setupwizard;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.students.StudentStudyEndReasonDAO;
import fi.otavanopisto.pyramus.domainmodel.students.StudentStudyEndReason;

public class StudyEndReasonsSetupWizardViewController extends SetupWizardController {

  public StudyEndReasonsSetupWizardViewController() {
    super("studyendreasons");
  }

  @Override
  public void setup(PageRequestContext requestContext) throws SetupWizardException {

  }

  @Override
  public void save(PageRequestContext requestContext) throws SetupWizardException {
    StudentStudyEndReasonDAO studentStudyEndReasonDAO = DAOFactory.getInstance().getStudentStudyEndReasonDAO();    

    int rowCount = requestContext.getInteger("studyEndReasonsTable.rowCount");
    Map<String, StudentStudyEndReason> reasons = new HashMap<>();
    Map<String, String> parents = new HashMap<>();

    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "studyEndReasonsTable." + i;
      String guid = requestContext.getString(colPrefix + ".guid");
      String parentGuid = requestContext.getString(colPrefix + ".parentGuid");
      if (!StringUtils.isBlank(parentGuid)) {
        parents.put(guid, parentGuid);
      }
    }
    
    for (String parentGuid : parents.values()) {
      if (parents.keySet().contains(parentGuid)) {
        throw new SetupWizardException("Parent end reasons can't have their own parents.");
      }
    }

    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "studyEndReasonsTable." + i;
      String name = requestContext.getString(colPrefix + ".name");
      String guid = requestContext.getString(colPrefix + ".guid");
//      String parentGuid = requestContext.getString(colPrefix + ".parentGuid");
      StudentStudyEndReason reason = studentStudyEndReasonDAO.create(null, name); 
      reasons.put(guid, reason);
    }

    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "studyEndReasonsTable." + i;
      String guid = requestContext.getString(colPrefix + ".guid");
      String parentGuid = requestContext.getString(colPrefix + ".parentGuid");
      StudentStudyEndReason reason = reasons.get(guid);
      StudentStudyEndReason parentReason = reasons.get(parentGuid);
      studentStudyEndReasonDAO.updateParentReason(reason, parentReason);
    }

  }

  @Override
  public boolean isInitialized(PageRequestContext requestContext) throws SetupWizardException {
    StudentStudyEndReasonDAO studentStudyEndReasonDAO = DAOFactory.getInstance().getStudentStudyEndReasonDAO();
    return !studentStudyEndReasonDAO.listAll().isEmpty();
  }

}
