package fi.otavanopisto.pyramus.json.settings;

import java.util.Locale;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.reports.ReportCategoryDAO;
import fi.otavanopisto.pyramus.domainmodel.reports.ReportCategory;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.PyramusStatusCode;
import fi.otavanopisto.pyramus.framework.UserRole;

public class DeleteReportCategoryJSONRequestController extends JSONRequestController {
  
  public void process(JSONRequestContext requestContext) {
    ReportCategoryDAO categoryDAO = DAOFactory.getInstance().getReportCategoryDAO();
    Long reportCategoryId = requestContext.getLong("reportCategory");
    ReportCategory reportCategory = categoryDAO.findById(reportCategoryId);
    if (categoryDAO.isReportCategoryInUse(reportCategory)) {
      Locale locale = requestContext.getRequest().getLocale();
      String msg = Messages.getInstance().getText(locale, "settings.deleteReportCategory.categoryInUse");
      throw new SmvcRuntimeException(PyramusStatusCode.VALIDATION_FAILURE, msg);
    }
    else {
      categoryDAO.delete(reportCategory);
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
