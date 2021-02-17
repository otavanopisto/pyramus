package fi.otavanopisto.pyramus.views.applications;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fi.internetix.smvc.AccessDeniedException;
import fi.internetix.smvc.Feature;
import fi.internetix.smvc.LoginRequiredException;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.internetix.smvc.controllers.RequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationMailTemplateDAO;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationMailTemplate;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.util.JSONArrayExtractor;

public class ListMailTemplatesViewController extends PyramusViewController {
  
  public void process(PageRequestContext pageRequestContext) {
    ApplicationMailTemplateDAO applicationMailTemplateDAO = DAOFactory.getInstance().getApplicationMailTemplateDAO();
    List<ApplicationMailTemplate> applicationMailTemplates = applicationMailTemplateDAO.listUnarchived();
    Collections.sort(applicationMailTemplates, new Comparator<ApplicationMailTemplate>() {
      public int compare(ApplicationMailTemplate o1, ApplicationMailTemplate o2) {
        return o1.getName().compareTo(o2.getName());
      }
    });
    String jsonMailTemplates = new JSONArrayExtractor("name", "id").extractString(applicationMailTemplates);
    setJsDataVariable(pageRequestContext, "mailTemplates", jsonMailTemplates);
    pageRequestContext.setIncludeJSP("/templates/applications/listmailtemplates.jsp");
  }

  @Override
  public void authorize(RequestContext requestContext) throws LoginRequiredException, AccessDeniedException {
    if (!requestContext.hasFeature(Feature.APPLICATION_MANAGEMENT)) {
      throw new AccessDeniedException(requestContext.getRequest().getLocale());
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR };
  }

}
