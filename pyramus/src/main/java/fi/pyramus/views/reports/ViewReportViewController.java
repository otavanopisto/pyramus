package fi.pyramus.views.reports;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.I18N.Messages;
import fi.pyramus.breadcrumbs.Breadcrumbable;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.reports.ReportContextDAO;
import fi.pyramus.dao.reports.ReportDAO;
import fi.pyramus.domainmodel.reports.Report;
import fi.pyramus.domainmodel.reports.ReportContext;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;

/**
 * The controller responsible of the List Reports view.
 */
public class ViewReportViewController extends PyramusViewController implements Breadcrumbable {
  
  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    ReportDAO reportDAO = DAOFactory.getInstance().getReportDAO();
    Long reportId = pageRequestContext.getLong("reportId");
    Report report = reportDAO.findById(reportId);
    
    handleContextParameters(pageRequestContext, report);
    
    pageRequestContext.getRequest().setAttribute("report", report);
    pageRequestContext.getRequest().setAttribute("reportsContextPath", System.getProperty("reports.contextPath"));
    
    pageRequestContext.setIncludeJSP("/templates/reports/viewreport.jsp");
  }

  private void handleContextParameters(PageRequestContext pageRequestContext, Report report) {
    ReportContextDAO reportContextDAO = DAOFactory.getInstance().getReportContextDAO();
    
    List<ReportContext> contexts = reportContextDAO.listByReport(report);
    List<String> contextStrs = new ArrayList<String>();
    
    for (ReportContext ctx : contexts) {
      contextStrs.add(ctx.getContext().toString());
      
      switch (ctx.getContext()) {
        case Student:
          Long studentId = pageRequestContext.getLong("studentId");
          if (studentId != null)
            pageRequestContext.getRequest().setAttribute("studentId", studentId); 
        break;
        
        case Course:
          Long courseId = pageRequestContext.getLong("courseId");
          if (courseId != null)
            pageRequestContext.getRequest().setAttribute("courseId", courseId); 
        break;
      }
    }

    pageRequestContext.getRequest().setAttribute("reportContexts", contextStrs); 
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
    return Messages.getInstance().getText(locale, "reports.viewReport.pageTitle");
  }

}
