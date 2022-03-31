package fi.otavanopisto.pyramus.views.worklist;

import java.util.List;
import java.util.Locale;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.breadcrumbs.Breadcrumbable;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.worklist.WorklistBillingSettingsDAO;
import fi.otavanopisto.pyramus.domainmodel.worklist.WorklistBillingSettings;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class EditBillingSettingsViewController extends PyramusViewController implements Breadcrumbable {

  public void process(PageRequestContext pageRequestContext) {
    WorklistBillingSettingsDAO worklistBillingSettingsDAO = DAOFactory.getInstance().getWorklistBillingSettingsDAO();
    List<WorklistBillingSettings> worklistBillingSettings = worklistBillingSettingsDAO.listAll();
    if (!worklistBillingSettings.isEmpty()) {
      pageRequestContext.getRequest().setAttribute("billingSettings", worklistBillingSettings.get(0).getSettings());
    }
    pageRequestContext.setIncludeJSP("/templates/worklist/editbillingsettings.jsp");
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR };
  }

  public String getName(Locale locale) {
    return Messages.getInstance().getText(locale, "worklist.editBillingSettings.pageTitle");
  }

}
