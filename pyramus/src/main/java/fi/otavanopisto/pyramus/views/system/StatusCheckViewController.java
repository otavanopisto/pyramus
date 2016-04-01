package fi.otavanopisto.pyramus.views.system;

import java.io.IOException;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.DefaultsDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Defaults;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

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
