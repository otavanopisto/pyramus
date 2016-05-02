package fi.otavanopisto.pyramus.json.settings;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.reports.ReportCategoryDAO;
import fi.otavanopisto.pyramus.domainmodel.reports.ReportCategory;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class SaveReportCategoriesJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext jsonRequestContext) {
    ReportCategoryDAO categoryDAO = DAOFactory.getInstance().getReportCategoryDAO();

    int rowCount = NumberUtils.createInteger(jsonRequestContext.getRequest().getParameter("reportCategoriesTable.rowCount")).intValue();
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "reportCategoriesTable." + i;
      Long reportCategoryId = jsonRequestContext.getLong(colPrefix + ".reportCategoryId");
      String name = jsonRequestContext.getString(colPrefix + ".name");
      
      // TODO category index column support
      boolean modified = new Integer(1).equals(jsonRequestContext.getInteger(colPrefix + ".modified"));
      if (reportCategoryId == -1) {
        categoryDAO.create(name, null);
      }
      else if (modified) {
        ReportCategory reportCategory = categoryDAO.findById(reportCategoryId);
        categoryDAO.update(reportCategory, name, null);
      }
    }
    jsonRequestContext.setRedirectURL(jsonRequestContext.getReferer(true));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
