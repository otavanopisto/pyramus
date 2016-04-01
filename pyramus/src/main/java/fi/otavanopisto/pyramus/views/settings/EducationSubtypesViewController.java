package fi.otavanopisto.pyramus.views.settings;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import net.sf.json.*;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.breadcrumbs.Breadcrumbable;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.EducationSubtypeDAO;
import fi.otavanopisto.pyramus.dao.base.EducationTypeDAO;
import fi.otavanopisto.pyramus.domainmodel.base.EducationSubtype;
import fi.otavanopisto.pyramus.domainmodel.base.EducationType;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.util.JSONArrayExtractor;
import fi.otavanopisto.pyramus.util.StringAttributeComparator;

/**
 * The controller responsible of the Manage Subtypes of Fields of Education view of the application.
 * 
 * @see fi.otavanopisto.pyramus.json.settings.SaveEducationSubtypesJSONRequestController
 */
public class EducationSubtypesViewController extends PyramusViewController implements Breadcrumbable {

  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();
    EducationSubtypeDAO educationSubtypeDAO = DAOFactory.getInstance().getEducationSubtypeDAO();
    
    List<EducationType> educationTypes = educationTypeDAO.listUnarchived();
    Collections.sort(educationTypes, new StringAttributeComparator("getName"));
    
    JSONArray jsonEducationTypes = new JSONArrayExtractor("name", "id").extract(educationTypes);
    for (int i=0; i<educationTypes.size(); i++) {
      List<EducationSubtype> subtypes = educationSubtypeDAO.listByEducationType(educationTypes.get(i));
      JSONArray jsonSubtypes = new JSONArrayExtractor("id", "name", "code").extract(subtypes);
      jsonEducationTypes.getJSONObject(i).put("subtypes", jsonSubtypes);
    }

    this.setJsDataVariable(pageRequestContext, "educationTypes", jsonEducationTypes.toString());
    pageRequestContext.setIncludeJSP("/templates/settings/educationsubtypes.jsp");
  }

  /**
   * Returns the roles allowed to access this page.
   * 
   * @return The roles allowed to access this page
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

  /**
   * Returns the localized name of this page. Used e.g. for breadcrumb navigation.
   * 
   * @param locale The locale to be used for the name
   * 
   * @return The localized name of this page
   */
  public String getName(Locale locale) {
    return Messages.getInstance().getText(locale, "settings.educationSubtypes.pageTitle");
  }

}
