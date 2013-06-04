package fi.pyramus.views.settings;

import java.util.List;
import java.util.Locale;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import fi.pyramus.util.JSONArrayExtractor;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.I18N.Messages;
import fi.pyramus.breadcrumbs.Breadcrumbable;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.courses.CourseDescriptionCategoryDAO;
import fi.pyramus.domainmodel.courses.CourseDescriptionCategory;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;

/**
 * The controller responsible of the Manage CourseDescrptionCategories view of the application.
 * 
 * @see fi.pyramus.json.settings.SaveCourseDescrptionCategoriesJSONRequestController
 */
public class CourseDescriptionCategoriesViewController extends PyramusViewController implements Breadcrumbable {

  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    CourseDescriptionCategoryDAO descCatDAO = DAOFactory.getInstance().getCourseDescriptionCategoryDAO();
    List<CourseDescriptionCategory> descCats = descCatDAO.listUnarchived();
    
    String jsonCategories = new JSONArrayExtractor("name", "id").extractString(descCats);
    
    this.setJsDataVariable(pageRequestContext, "descriptionCategories", jsonCategories);
    
    pageRequestContext.setIncludeJSP("/templates/settings/coursedescriptioncategories.jsp");
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
    return Messages.getInstance().getText(locale, "settings.courseDescriptionCategories.pageTitle");
  }

}
