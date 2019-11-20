package fi.otavanopisto.pyramus.views.settings;

import java.util.Locale;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.breadcrumbs.Breadcrumbable;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.OrganizationDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Organization;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class EditOrganizationViewController extends PyramusViewController implements Breadcrumbable {

  public void process(PageRequestContext requestContext) {
    OrganizationDAO organizationDAO = DAOFactory.getInstance().getOrganizationDAO();
    Long organizationId = requestContext.getLong("organizationId");
    Organization organization = organizationDAO.findById(organizationId);
    
    requestContext.getRequest().setAttribute("organization", organization);
    requestContext.setIncludeJSP("/templates/settings/editorganization.jsp");
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR };
  }

  public String getName(Locale locale) {
    return Messages.getInstance().getText(locale, "settings.editOrganization.pageTitle");
  }

}
