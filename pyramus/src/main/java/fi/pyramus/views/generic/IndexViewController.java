package fi.pyramus.views.generic;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.DefaultsDAO;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;

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
