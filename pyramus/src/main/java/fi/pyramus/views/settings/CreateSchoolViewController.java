package fi.pyramus.views.settings;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.I18N.Messages;
import fi.pyramus.breadcrumbs.Breadcrumbable;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.ContactTypeDAO;
import fi.pyramus.dao.base.ContactURLTypeDAO;
import fi.pyramus.dao.base.SchoolFieldDAO;
import fi.pyramus.dao.base.SchoolVariableKeyDAO;
import fi.pyramus.domainmodel.base.ContactType;
import fi.pyramus.domainmodel.base.ContactURLType;
import fi.pyramus.domainmodel.base.SchoolVariableKey;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;
import fi.pyramus.util.JSONArrayExtractor;
import fi.pyramus.util.StringAttributeComparator;

/**
 * The controller responsible of the Create School view of the application.
 * 
 * @see fi.pyramus.json.settings.CreateSchoolJSONRequestController
 */
public class CreateSchoolViewController extends PyramusViewController implements Breadcrumbable {
  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    SchoolFieldDAO schoolFieldDAO = DAOFactory.getInstance().getSchoolFieldDAO();
    SchoolVariableKeyDAO schoolVariableKeyDAO = DAOFactory.getInstance().getSchoolVariableKeyDAO();
    ContactTypeDAO contactTypeDAO = DAOFactory.getInstance().getContactTypeDAO();
    ContactURLTypeDAO contactURLTypeDAO = DAOFactory.getInstance().getContactURLTypeDAO();

    List<ContactURLType> contactURLTypes = contactURLTypeDAO.listUnarchived();
    Collections.sort(contactURLTypes, new StringAttributeComparator("getName"));

    List<SchoolVariableKey> schoolUserEditableVariableKeys = schoolVariableKeyDAO.listUserEditableVariableKeys();
    Collections.sort(schoolUserEditableVariableKeys, new StringAttributeComparator("getVariableName"));

    List<ContactType> contactTypes = contactTypeDAO.listUnarchived();
    Collections.sort(contactTypes, new StringAttributeComparator("getName"));
    
    String jsonContactTypes = new JSONArrayExtractor("name", "id").extractString(contactTypes);
    String jsonVariableKeys = new JSONArrayExtractor("variableKey", "variableName", "variableType").extractString(schoolUserEditableVariableKeys);

    this.setJsDataVariable(pageRequestContext, "contactTypes", jsonContactTypes);
    this.setJsDataVariable(pageRequestContext, "variableKeys", jsonVariableKeys);
    pageRequestContext.getRequest().setAttribute("schoolFields", schoolFieldDAO.listUnarchived());
    pageRequestContext.setIncludeJSP("/templates/settings/createschool.jsp");
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
    return Messages.getInstance().getText(locale, "settings.createSchool.pageTitle");
  }

}
