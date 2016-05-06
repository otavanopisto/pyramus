package fi.otavanopisto.pyramus.views.settings;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.breadcrumbs.Breadcrumbable;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.MunicipalityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Municipality;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.util.JSONArrayExtractor;
import fi.otavanopisto.pyramus.util.StringAttributeComparator;

/**
 * The controller responsible of the Manage Municipalities view of the application.
 * 
 * @see fi.otavanopisto.pyramus.json.settings.SaveEducationTypesJSONRequestController
 */
public class MunicipalitiesViewController extends PyramusViewController implements Breadcrumbable {

  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    MunicipalityDAO municipalityDAO = DAOFactory.getInstance().getMunicipalityDAO();
    
    List<Municipality> municipalities = municipalityDAO.listUnarchived();
    Collections.sort(municipalities, new StringAttributeComparator("getName"));
    String jsonMunicipalities = new JSONArrayExtractor("name", "code", "id").extractString(municipalities);
    this.setJsDataVariable(pageRequestContext, "municipalities", jsonMunicipalities);
    pageRequestContext.setIncludeJSP("/templates/settings/municipalities.jsp");
  }

  /**
   * Returns the roles allowed to access this page.
   * 
   * @return The roles allowed to access this page
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

  /**
   * Returns the localized name of this page. Used e.g. for breadcrumb navigation.
   * 
   * @param locale The locale to be used for the name
   * 
   * @return The localized name of this page
   */
  public String getName(Locale locale) {
    return Messages.getInstance().getText(locale, "settings.municipalities.pageTitle");
  }

}
