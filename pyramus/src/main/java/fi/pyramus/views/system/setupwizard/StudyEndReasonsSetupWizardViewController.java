package fi.pyramus.views.system.setupwizard;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.students.StudentDAO;
import fi.pyramus.dao.students.StudentStudyEndReasonDAO;
import fi.pyramus.domainmodel.students.StudentStudyEndReason;

public class StudyEndReasonsSetupWizardViewController extends SetupWizardController {

  public StudyEndReasonsSetupWizardViewController() {
    super("studyendreasons");
  }

  @Override
  public void setup(PageRequestContext requestContext) throws SetupWizardException {
    StudentStudyEndReasonDAO studentStudyEndReasonDAO = DAOFactory.getInstance().getStudentStudyEndReasonDAO();
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    
    List<StudentStudyEndReason> studyEndReasons = studentStudyEndReasonDAO.listAll();
    
    JSONArray jsonStudyEndReasons = new JSONArray();
    JSONArray jsonReasonsInUse = new JSONArray();
   
    for (StudentStudyEndReason reason : studyEndReasons) {
      JSONObject jsonReason = new JSONObject();
      
      jsonReason.put("id", reason.getId());
      jsonReason.put("name", reason.getName());
      if (reason.getParentReason() != null) {
        jsonReason.put("parentId", reason.getParentReason().getId());
      }
      jsonStudyEndReasons.add(jsonReason);
    }
    
    for (StudentStudyEndReason reason : studyEndReasons) {
      if (studentDAO.countByStudyEndReason(reason) > 0) {
        JSONObject jsonReason = new JSONObject();
        jsonReason.put("id", reason.getId());
        
        jsonReasonsInUse.add(jsonReason);
      }
    }
    
    this.setJsDataVariable(requestContext, "studyEndReasons", jsonStudyEndReasons.toString());
    this.setJsDataVariable(requestContext, "reasonsInUse", jsonReasonsInUse.toString());

  }

  @Override
  public void save(PageRequestContext requestContext) throws SetupWizardException {


  }

  @Override
  public boolean isInitialized(PageRequestContext requestContext) throws SetupWizardException {
    StudentStudyEndReasonDAO studentStudyEndReasonDAO = DAOFactory.getInstance().getStudentStudyEndReasonDAO();
    return !studentStudyEndReasonDAO.listUnarchived().isEmpty();
  }

}
