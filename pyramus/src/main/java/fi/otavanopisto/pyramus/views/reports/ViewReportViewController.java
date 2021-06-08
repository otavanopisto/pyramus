package fi.otavanopisto.pyramus.views.reports;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.breadcrumbs.Breadcrumbable;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.reports.ReportContextDAO;
import fi.otavanopisto.pyramus.dao.reports.ReportDAO;
import fi.otavanopisto.pyramus.domainmodel.reports.Report;
import fi.otavanopisto.pyramus.domainmodel.reports.ReportContext;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

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
    
    pageRequestContext.setIncludeJSP("/templates/reports/viewreport.jsp");
  }

  private void handleContextParameters(PageRequestContext pageRequestContext, Report report) {
    ReportContextDAO reportContextDAO = DAOFactory.getInstance().getReportContextDAO();
    
    List<ReportContext> contexts = reportContextDAO.listByReport(report);
    List<String> contextStrs = new ArrayList<>();
    
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
        
        case Common:
        break;
      }
    }

    
    Long staffMemberId = pageRequestContext.getLong("staffMemberId");
    if (staffMemberId != null) {
      pageRequestContext.getRequest().setAttribute("staffMemberId", staffMemberId);
    }
    
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    
    Date startDate = pageRequestContext.getDate("startDate");
    if (startDate != null)
      pageRequestContext.getRequest().setAttribute("startDate", df.format(startDate));
    
    Date endDate = pageRequestContext.getDate("endDate");
    if (endDate != null)
      pageRequestContext.getRequest().setAttribute("endDate", df.format(endDate));
    
    pageRequestContext.getRequest().setAttribute("reportContexts", contextStrs); 
  }
  
  /**
   * Returns the roles allowed to access this page. Reports are available for users with
   * {@link Role#USER}, {@link Role#MANAGER} and {@link Role#ADMINISTRATOR} privileges.
   * 
   * @return The roles allowed to access this page
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.USER, UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
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
