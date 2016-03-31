package fi.otavanopisto.pyramus.views.generic;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.DefaultsDAO;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class IndexViewController extends PyramusViewController {

  public void process(PageRequestContext requestContext) {
    DefaultsDAO defaultsDAO = DAOFactory.getInstance().getDefaultsDAO();

    if (!defaultsDAO.isPyramusInitialized()) {
     requestContext.setRedirectURL(requestContext.getRequest().getContextPath() + "/system/setupwizard/index.page"); 
    } else {
      requestContext.setIncludeJSP("/templates/index.jsp");
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.EVERYONE };
  }

}
