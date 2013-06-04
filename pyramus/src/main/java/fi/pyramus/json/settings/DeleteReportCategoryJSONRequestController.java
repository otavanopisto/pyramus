package fi.pyramus.json.settings;

import java.util.Locale;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.I18N.Messages;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.reports.ReportCategoryDAO;
import fi.pyramus.domainmodel.reports.ReportCategory;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.PyramusStatusCode;
import fi.pyramus.framework.UserRole;

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
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
