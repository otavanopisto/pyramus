package fi.otavanopisto.pyramus.json.worklist;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.worklist.WorklistItemTemplateDAO;
import fi.otavanopisto.pyramus.domainmodel.worklist.WorklistItemTemplate;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class ArchiveWorklistTemplateJSONRequestController extends JSONRequestController {
  
  public void process(JSONRequestContext requestContext) {
    WorklistItemTemplateDAO worklistItemTemplateDAO = DAOFactory.getInstance().getWorklistItemTemplateDAO();
    Long templateId = NumberUtils.createLong(requestContext.getRequest().getParameter("templateId"));
    WorklistItemTemplate worklistItemTemplate = worklistItemTemplateDAO.findById(templateId);
    worklistItemTemplateDAO.archive(worklistItemTemplate);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }
  
}
