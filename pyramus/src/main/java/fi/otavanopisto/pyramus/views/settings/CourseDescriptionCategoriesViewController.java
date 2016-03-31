package fi.otavanopisto.pyramus.views.settings;

import java.util.List;
import java.util.Locale;

import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.breadcrumbs.Breadcrumbable;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.courses.CourseDescriptionCategoryDAO;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseDescriptionCategory;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.util.JSONArrayExtractor;
import fi.internetix.smvc.controllers.PageRequestContext;

/**
 * The controller responsible of the Manage CourseDescrptionCategories view of the application.
 * 
 * @see fi.otavanopisto.pyramus.json.settings.SaveCourseDescrptionCategoriesJSONRequestController
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
