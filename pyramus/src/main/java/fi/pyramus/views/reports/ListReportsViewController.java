package fi.pyramus.views.reports;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.I18N.Messages;
import fi.pyramus.breadcrumbs.Breadcrumbable;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.reports.ReportDAO;
import fi.pyramus.domainmodel.reports.Report;
import fi.pyramus.domainmodel.reports.ReportContextType;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;

/**
 * The controller responsible of the List Reports view.
 */
public class ListReportsViewController extends PyramusViewController implements Breadcrumbable {
  
  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    ReportDAO reportDAO = DAOFactory.getInstance().getReportDAO();

    List<Report> reports = reportDAO.listByContextType(ReportContextType.Common);
    Collections.sort(reports, new Comparator<Report>() {
      @Override
      public int compare(Report o1, Report o2) {
        String s1 = o1.getCategory() == null ? null : o1.getCategory().getName();
        String s2 = o2.getCategory() == null ? null : o2.getCategory().getName();
        int cmp = s1 == null ? 1 : s2 == null ? -1 : s1.compareToIgnoreCase(s2);
        if (cmp == 0) {
          cmp = o1.getName().compareToIgnoreCase(o2.getName());
        }
        return cmp;
      }
    });

    pageRequestContext.getRequest().setAttribute("reports", reports);
    pageRequestContext.setIncludeJSP("/templates/reports/listreports.jsp");
  }

  /**
   * Returns the roles allowed to access this page. Reports are available for users with
   * {@link Role#USER}, {@link Role#MANAGER} and {@link Role#ADMINISTRATOR} privileges.
   * 
   * @return The roles allowed to access this page
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.USER, UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

  /**
   * Returns the localized name of this page. Used e.g. for breadcrumb navigation.
   * 
   * @param locale The locale to be used for the name
   * 
   * @return The localized name of this page
   */
  public String getName(Locale locale) {
    return Messages.getInstance().getText(locale, "reports.listReports.pageTitle");
  }

}
