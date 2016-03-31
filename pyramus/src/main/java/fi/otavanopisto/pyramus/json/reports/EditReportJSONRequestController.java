package fi.otavanopisto.pyramus.json.reports;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.reports.ReportCategoryDAO;
import fi.otavanopisto.pyramus.dao.reports.ReportContextDAO;
import fi.otavanopisto.pyramus.dao.reports.ReportDAO;
import fi.otavanopisto.pyramus.domainmodel.reports.Report;
import fi.otavanopisto.pyramus.domainmodel.reports.ReportCategory;
import fi.otavanopisto.pyramus.domainmodel.reports.ReportContext;
import fi.otavanopisto.pyramus.domainmodel.reports.ReportContextType;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

/**
 * The controller responsible of editing a report. 
 * 
 * @see fi.otavanopisto.pyramus.views.reports.EditReportViewController
 */
public class EditReportJSONRequestController extends JSONRequestController {

  /**
   * Processes the request to edit a report.
   * 
   * @param requestContext The JSON request context
   */
  public void process(JSONRequestContext requestContext) {
    ReportDAO reportDAO = DAOFactory.getInstance().getReportDAO();
    ReportCategoryDAO categoryDAO = DAOFactory.getInstance().getReportCategoryDAO();
    ReportContextDAO reportContextDAO = DAOFactory.getInstance().getReportContextDAO();

    Long reportId = requestContext.getLong("reportId");
    Report report = reportDAO.findById(reportId);

    Long reportCategoryId = requestContext.getLong("category");
    ReportCategory category = reportCategoryId == null ? null : categoryDAO.findById(reportCategoryId);
    
    String name = requestContext.getString("name");
    
    reportDAO.update(report, name, category);
    
    for (ReportContextType contextType : ReportContextType.values()) {
      ReportContext context = reportContextDAO.findByReportAndContextType(report, contextType);
      
      boolean selected = requestContext.getBoolean("context." + contextType.toString());
      
      if ((selected) && (context == null))
        reportContextDAO.create(report, contextType);
      else if ((!selected) && (context != null))
        reportContextDAO.delete(context);
    }

    requestContext.setRedirectURL(requestContext.getReferer(true));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
