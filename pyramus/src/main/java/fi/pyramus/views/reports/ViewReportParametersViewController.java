package fi.pyramus.views.reports;

import java.util.List;
import java.util.Locale;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.internetix.smvc.controllers.RequestContext;
import fi.pyramus.I18N.Messages;
import fi.pyramus.breadcrumbs.Breadcrumbable;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.MagicKeyDAO;
import fi.pyramus.dao.reports.ReportContextDAO;
import fi.pyramus.dao.reports.ReportDAO;
import fi.pyramus.domainmodel.base.MagicKey;
import fi.pyramus.domainmodel.base.MagicKeyScope;
import fi.pyramus.domainmodel.reports.Report;
import fi.pyramus.domainmodel.reports.ReportContext;
import fi.pyramus.domainmodel.users.Role;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;

/**
 * The controller responsible of the List Reports view.
 */
public class ViewReportParametersViewController extends PyramusViewController implements Breadcrumbable {
  
  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    MagicKeyDAO magicKeyDAO = DAOFactory.getInstance().getMagicKeyDAO();
    ReportDAO reportDAO = DAOFactory.getInstance().getReportDAO();

    Long reportId = pageRequestContext.getLong("reportId");
    Report report = reportDAO.findById(reportId);
    String reportsContextPath = System.getProperty("reports.contextPath");
    
    StringBuilder magicKeyBuilder = new StringBuilder()
      .append(Long.toHexString(reportId))
      .append('-')
      .append(Long.toHexString(System.currentTimeMillis()))
      .append('-')
      .append(Long.toHexString(Thread.currentThread().getId()));
    
    MagicKey magicKey = magicKeyDAO.create(magicKeyBuilder.toString(), MagicKeyScope.REQUEST); 
    
    StringBuilder urlBuilder = new StringBuilder()
      .append(reportsContextPath)
      .append("/parameter?magicKey=")
      .append(magicKey.getName())
      .append("&__report=reports/").append(reportId).append(".rptdesign")
      .append("&__masterpage=true&__nocache");
    
    handleContextParameters(pageRequestContext, report, urlBuilder);
    
    pageRequestContext.setIncludeUrl(urlBuilder.toString());
  }

  private void handleContextParameters(PageRequestContext pageRequestContext, Report report, StringBuilder urlBuilder) {
    ReportContextDAO reportContextDAO = DAOFactory.getInstance().getReportContextDAO();
    
    List<ReportContext> contexts = reportContextDAO.listByReport(report);
    
    for (ReportContext ctx : contexts) {
      switch (ctx.getContext()) {
        case Student:
          Long studentId = pageRequestContext.getLong("studentId");
          if (studentId != null)
            urlBuilder.append("&studentId=").append(studentId);
        break;
        
        case Course:
          Long courseId = pageRequestContext.getLong("courseId");
          if (courseId != null)
            urlBuilder.append("&courseId=").append(courseId);
        break;
      }
    }
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
