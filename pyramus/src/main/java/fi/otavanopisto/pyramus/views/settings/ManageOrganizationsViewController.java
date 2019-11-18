package fi.otavanopisto.pyramus.views.settings;

import java.util.Locale;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.breadcrumbs.Breadcrumbable;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class ManageOrganizationsViewController extends PyramusViewController implements Breadcrumbable {

  public void process(PageRequestContext pageRequestContext) {
    pageRequestContext.setIncludeJSP("/templates/settings/manageorganizations.jsp");
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR };
  }

  public String getName(Locale locale) {
    return Messages.getInstance().getText(locale, "settings.manageOrganizations.pageTitle");
  }

}
