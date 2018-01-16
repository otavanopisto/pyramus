package fi.otavanopisto.pyramus.json.applications;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationMailTemplateDAO;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationMailTemplate;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.views.applications.ApplicationUtils;

public class ListMailTemplatesJSONRequestController extends JSONRequestController {

  private static final Logger logger = Logger.getLogger(ListMailTemplatesJSONRequestController.class.getName());

  public void process(JSONRequestContext requestContext) {
    try {
      ApplicationMailTemplateDAO applicationMailTemplateDAO = DAOFactory.getInstance().getApplicationMailTemplateDAO();
      List<ApplicationMailTemplate> applicationMailTemplates = applicationMailTemplateDAO.listUnarchived();

      Collections.sort(applicationMailTemplates, new Comparator<ApplicationMailTemplate>() {
        public int compare(ApplicationMailTemplate o1, ApplicationMailTemplate o2) {
          return o1.getName().compareTo(o2.getName());
        }
      });
      
      List<Map<String, Object>> results = new ArrayList<>();
      for (ApplicationMailTemplate applicationMailTemplate : applicationMailTemplates) {
        Map<String, Object> templateInfo = new HashMap<>();
        templateInfo.put("id", applicationMailTemplate.getId());
        templateInfo.put("lineInternal", applicationMailTemplate.getLine());
        templateInfo.put("lineUi", ApplicationUtils.applicationLineUiValue(applicationMailTemplate.getLine()));
        templateInfo.put("name", applicationMailTemplate.getName());
        templateInfo.put("authorName", applicationMailTemplate.getStaffMember().getFullName());
        templateInfo.put("owner", applicationMailTemplate.getStaffMember().getId().equals(requestContext.getLoggedUserId()));
        results.add(templateInfo);
      }
      requestContext.addResponseParameter("mailTemplates", results);
    }
    catch (Exception e) {
      logger.log(Level.SEVERE, "Error loading mail templates", e);
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER };
  }

}
