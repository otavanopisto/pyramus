package fi.otavanopisto.pyramus.framework;

import fi.internetix.smvc.AccessDeniedException;
import fi.internetix.smvc.LoginRequiredException;
import fi.internetix.smvc.controllers.RequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.users.UserDAO;
import fi.otavanopisto.pyramus.domainmodel.users.User;

public abstract class JSONRequestController2 implements fi.internetix.smvc.controllers.JSONRequestController {

  public JSONRequestController2(PyramusRequestControllerAccess access) {
    this.access = access;
  }
  
  protected abstract boolean checkAccess(RequestContext requestContext);

  @Override
  public void authorize(RequestContext requestContext) throws LoginRequiredException, AccessDeniedException {
    if (access != PyramusRequestControllerAccess.EVERYONE) {
      // PyramusRequestControllerAccess.REQUIRELOGIN is implied here
      if (!requestContext.isLoggedIn()) {
        throw new LoginRequiredException();
      }
  
      Long loggedUserId = requestContext.getLoggedUserId();
      UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
      User user = userDAO.findById(loggedUserId);
      UserUtils.checkManagementOrganizationPermission(user, requestContext.getRequest().getLocale());
  
      if (!checkAccess(requestContext)) {
        throw new AccessDeniedException(requestContext.getRequest().getLocale());
      }
    }
  }

  private PyramusRequestControllerAccess access;
}
