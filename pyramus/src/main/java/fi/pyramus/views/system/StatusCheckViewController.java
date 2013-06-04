package fi.pyramus.views.system;

import java.io.IOException;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.DefaultsDAO;
import fi.pyramus.domainmodel.base.Defaults;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;

public class StatusCheckViewController extends PyramusViewController {

  public void process(PageRequestContext requestContext) {
    DefaultsDAO defaultsDAO = DAOFactory.getInstance().getDefaultsDAO();
    Defaults defaults = defaultsDAO.getDefaults();
    if (defaults != null) {
      try {
        requestContext.getResponse().getWriter().print("OK");
      }
      catch (IOException ioe) {
        throw new SmvcRuntimeException(ioe);
      }
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.EVERYONE };
  }

}
