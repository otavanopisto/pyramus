package fi.pyramus.plugin.simple.views;

import fi.internetix.smvc.AccessDeniedException;
import fi.internetix.smvc.LoginRequiredException;
import fi.internetix.smvc.controllers.PageController;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.internetix.smvc.controllers.RequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.users.UserDAO;
import fi.pyramus.domainmodel.users.User;

/**
 * The controller responsible of the Logged User Info view of the application.
 * 
 */
public class LoggedUserInfoViewController implements PageController {

  /**
   * Processes the page request.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    User loggedUser = userDAO.findById(pageRequestContext.getLoggedUserId());
  
    pageRequestContext.getRequest().setAttribute("loggedUser", loggedUser);
    pageRequestContext.setIncludeFtl("/plugin/simple/ftl/loggeduserinfo.ftl");
  }

//  @Override
//  public UserRole[] getAllowedRoles() {
//    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
//  }

  public void authorize(RequestContext requestContext) throws LoginRequiredException, AccessDeniedException {
    // TODO Auto-generated method stub
    // throw new RuntimeException("Not implemented");
  }
}
