package fi.pyramus.views.settings;

import java.util.List;
import java.util.Locale;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.I18N.Messages;
import fi.pyramus.breadcrumbs.Breadcrumbable;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.students.StudentDAO;
import fi.pyramus.dao.students.StudentStudyEndReasonDAO;
import fi.pyramus.domainmodel.students.StudentStudyEndReason;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;

/**
 * The controller responsible of the Study End Reasons view of the application.
 * 
 * @see fi.pyramus.json.settings.SaveStudyEndReasonsJSONRequestController
 */
public class StudyEndReasonsViewController extends PyramusViewController implements Breadcrumbable {

  @Override
  public void process(PageRequestContext pageRequestContext) {
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
    
    this.setJsDataVariable(pageRequestContext, "studyEndReasons", jsonStudyEndReasons.toString());
    this.setJsDataVariable(pageRequestContext, "reasonsInUse", jsonReasonsInUse.toString());
    
    pageRequestContext.setIncludeJSP("/templates/settings/studyendreasons.jsp");
  }

  @Override
  public String getName(Locale locale) {
    return Messages.getInstance().getText(locale, "settings.studyEndReasons.pageTitle");
  }

  @Override
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
