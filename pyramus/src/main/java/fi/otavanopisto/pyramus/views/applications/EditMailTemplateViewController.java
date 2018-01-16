package fi.otavanopisto.pyramus.views.applications;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationMailTemplateDAO;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationMailTemplate;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class EditMailTemplateViewController extends PyramusViewController {
  
  public void process(PageRequestContext pageRequestContext) {
    ApplicationMailTemplateDAO applicationMailTemplateDAO = DAOFactory.getInstance().getApplicationMailTemplateDAO();
    Long templateId = pageRequestContext.getLong("template");
    ApplicationMailTemplate applicationMailTemplate = applicationMailTemplateDAO.findById(templateId);
    pageRequestContext.getRequest().setAttribute("template", applicationMailTemplate);
    pageRequestContext.setIncludeJSP("/templates/applications/editmailtemplate.jsp");
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER };
  }

}
