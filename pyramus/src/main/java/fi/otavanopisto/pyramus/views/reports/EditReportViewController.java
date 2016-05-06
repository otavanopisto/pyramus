package fi.otavanopisto.pyramus.views.reports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.breadcrumbs.Breadcrumbable;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.reports.ReportCategoryDAO;
import fi.otavanopisto.pyramus.dao.reports.ReportContextDAO;
import fi.otavanopisto.pyramus.dao.reports.ReportDAO;
import fi.otavanopisto.pyramus.domainmodel.reports.Report;
import fi.otavanopisto.pyramus.domainmodel.reports.ReportCategory;
import fi.otavanopisto.pyramus.domainmodel.reports.ReportContext;
import fi.otavanopisto.pyramus.domainmodel.reports.ReportContextType;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

/**
 * The controller responsible of the Edit Report view.
 */
public class EditReportViewController extends PyramusViewController implements Breadcrumbable {
  
  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    ReportDAO reportDAO = DAOFactory.getInstance().getReportDAO();
    ReportCategoryDAO categoryDAO = DAOFactory.getInstance().getReportCategoryDAO();
    ReportContextDAO reportContextDAO = DAOFactory.getInstance().getReportContextDAO();
    Long reportId = pageRequestContext.getLong("reportId");
    Report report = reportDAO.findById(reportId);
    
    List<ReportCategory> categories = categoryDAO.listAll();
    List<ReportContext> reportContexts = reportContextDAO.listByReport(report);
    
    Collections.sort(categories, new Comparator<ReportCategory>() {
      public int compare(ReportCategory o1, ReportCategory o2) {
        if (o1.getIndexColumn() == o2.getIndexColumn() || o1.getIndexColumn().equals(o2.getIndexColumn())) {
          return o1.getName() == null ? -1 : o2.getName() == null ? 1 : o1.getName().compareTo(o2.getName());
        }
        else {
          return o1.getIndexColumn() == null ? -1 : o2.getIndexColumn() == null ? 1 : o1.getIndexColumn().compareTo(o2.getIndexColumn());
        }
      }
    });
    
    Map<String, Boolean> selectedContexts = new HashMap<>();
    for (ReportContext context : reportContexts)
      selectedContexts.put(context.getContext().toString(), Boolean.TRUE);

    List<String> contextTypes = new ArrayList<>();
    for (ReportContextType contextType : ReportContextType.values())
      contextTypes.add(contextType.toString());
    
    pageRequestContext.getRequest().setAttribute("report", report);
    pageRequestContext.getRequest().setAttribute("reportCategories", categories);
    pageRequestContext.getRequest().setAttribute("reportContexts", selectedContexts);
    pageRequestContext.getRequest().setAttribute("contextTypes", contextTypes);
    
    pageRequestContext.setIncludeJSP("/templates/reports/editreport.jsp");
  }

  /**
   * Returns the roles allowed to access this page. Reports are available for users with
   * {@link Role#USER}, {@link Role#MANAGER} and {@link Role#ADMINISTRATOR} privileges.
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
    return Messages.getInstance().getText(locale, "reports.editReport.pageTitle");
  }

}
