package fi.otavanopisto.pyramus.views.settings;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.breadcrumbs.Breadcrumbable;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.reports.ReportCategoryDAO;
import fi.otavanopisto.pyramus.domainmodel.reports.ReportCategory;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.util.JSONArrayExtractor;

/**
 * The controller responsible of the Report Categories view of the application.
 * 
 * @see fi.otavanopisto.pyramus.json.settings.SaveReportCategoriesJSONRequestController
 */
public class ReportCategoriesViewController extends PyramusViewController implements Breadcrumbable {

  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    ReportCategoryDAO categoryDAO = DAOFactory.getInstance().getReportCategoryDAO();

    List<ReportCategory> categories = categoryDAO.listAll();

    Collections.sort(categories, new Comparator<ReportCategory>() {
      public int compare(ReportCategory o1, ReportCategory o2) {
        if (o1.getIndexColumn().equals(o2.getIndexColumn())) {
          return o1.getName() == null ? -1 : o2.getName() == null ? 1 : o1.getName().compareTo(o2.getName());
        }
        else {
          return o1.getIndexColumn() == null ? -1 : o2.getIndexColumn() == null ? 1 : o1.getIndexColumn().compareTo(o2.getIndexColumn());
        }
      }
    });
    
    
    String jsonCategories = new JSONArrayExtractor("name", "id").extractString(categories);
    
    this.setJsDataVariable(pageRequestContext, "reportCategories", jsonCategories);
    pageRequestContext.setIncludeJSP("/templates/settings/reportcategories.jsp");
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
    return Messages.getInstance().getText(locale, "settings.reportCategories.pageTitle");
  }

}
