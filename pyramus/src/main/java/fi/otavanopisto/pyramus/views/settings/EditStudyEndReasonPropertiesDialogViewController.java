package fi.otavanopisto.pyramus.views.settings;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.students.StudentStudyEndReasonDAO;
import fi.otavanopisto.pyramus.domainmodel.students.StudentStudyEndReason;
import fi.otavanopisto.pyramus.framework.EntityProperty;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.StudentStudyEndReasonProperties;
import fi.otavanopisto.pyramus.framework.UserRole;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class EditStudyEndReasonPropertiesDialogViewController extends PyramusViewController {

  public void process(PageRequestContext requestContext) {
    StudentStudyEndReasonDAO studentStudyEndReasonDAO = DAOFactory.getInstance().getStudentStudyEndReasonDAO();
    
    Long studyEndReasonId = requestContext.getLong("studyEndReasonId");
    StudentStudyEndReason studyEndReason = studentStudyEndReasonDAO.findById(studyEndReasonId);
    
    JSONArray propertiesJSON = new JSONArray();
    for (EntityProperty prop : StudentStudyEndReasonProperties.listProperties()) {
      String value = studyEndReason.getProperties().get(prop.getKey());
      
      JSONObject propertyJSON = new JSONObject();
      propertyJSON.put("type", prop.getType());
      propertyJSON.put("name", Messages.getInstance().getText(requestContext.getRequest().getLocale(), prop.getLocaleKey()));
      propertyJSON.put("key", prop.getKey());
      propertyJSON.put("value", value != null ? value : "");
      propertiesJSON.add(propertyJSON);
    }
    setJsDataVariable(requestContext, "properties", propertiesJSON.toString());
    
    requestContext.getRequest().setAttribute("studyEndReason", studyEndReason);
    requestContext.setIncludeJSP("/templates/settings/editstudyendreasonpropertiesdialog.jsp");
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
