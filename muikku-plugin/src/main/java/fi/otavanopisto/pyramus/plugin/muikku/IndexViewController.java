package fi.otavanopisto.pyramus.plugin.muikku;

import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.controllers.PageController;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.internetix.smvc.controllers.RequestControllerMapper;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.users.UserDAO;
import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class IndexViewController extends PyramusViewController implements PageController {
  
  private Role[] REDIRECT_ROLES = {
    Role.GUEST,
    Role.STUDENT
  };
  
  @Override
  public void process(PageRequestContext requestContext) {
    if (shouldRedirect(requestContext)) {
      String muikkuHost = MuikkuPluginTools.getMuikkuHost();
      if (StringUtils.isNotBlank(muikkuHost)) {
        requestContext.setRedirectURL(String.format("https://%s", muikkuHost));
        return;
      }
    }
    
    PyramusViewController originalViewController = (PyramusViewController) RequestControllerMapper.getRequestController("index.page.masked");
    originalViewController.process(requestContext);
  }
  
  private boolean shouldRedirect(PageRequestContext requestContext) {
    if (requestContext.isLoggedIn()) {
      UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
      
      Long loggedUserId = requestContext.getLoggedUserId();
      User user = userDAO.findById(loggedUserId);
      if (user != null) {
        return user.hasAnyRole(REDIRECT_ROLES);
      }
    }
    
    return false;
  }

  @Override
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.EVERYONE };
  }
  
}
