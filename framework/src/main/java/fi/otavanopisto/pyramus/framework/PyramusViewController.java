package fi.otavanopisto.pyramus.framework;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import fi.internetix.smvc.AccessDeniedException;
import fi.internetix.smvc.LoginRequiredException;
import fi.internetix.smvc.controllers.PageController;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.internetix.smvc.controllers.RequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.users.UserDAO;
import fi.otavanopisto.pyramus.domainmodel.users.User;

public abstract class PyramusViewController implements PageController {

  public abstract UserRole[] getAllowedRoles();

  protected void throwLoginRequiredException(RequestContext requestContext) {
    HttpServletRequest request = requestContext.getRequest();
    StringBuilder currentUrl = new StringBuilder(request.getRequestURL());
    String queryString = request.getQueryString();
    if (!StringUtils.isBlank(queryString)) {
      currentUrl.append('?');
      currentUrl.append(queryString);
    }
    throw new LoginRequiredException(currentUrl.toString());
  }
  
  public void authorize(RequestContext requestContext) throws LoginRequiredException, AccessDeniedException {
    UserRole[] roles = getAllowedRoles();
    if (!contains(roles, UserRole.EVERYONE)) {
      if (!requestContext.isLoggedIn()) {
        throwLoginRequiredException(requestContext);
      }
      else {
        Long loggedUserId = requestContext.getLoggedUserId();
        UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
        User user = userDAO.findById(loggedUserId);

        UserUtils.checkManagementOrganizationPermission(user, requestContext.getRequest().getLocale());

        UserRole userRole = UserUtils.roleToUserRole(user.getRole());
        
        if (!contains(roles, userRole)) {
          throw new AccessDeniedException(requestContext.getRequest().getLocale());
        }
      }
    }
  }

  /**
   * Returns whether the given role is included in the given role array.
   * 
   * @param roles The roles
   * @param role The role
   * 
   * @return <code>true</code> if the roles array contains the given role, otherwise
   * <code>false</code>
   */
  private boolean contains(UserRole[] roles, UserRole role) {
    for (int i = 0; i < roles.length; i++) {
      if (roles[i] == role) {
        return true;
      }
    }
    return false;
  }
  
  protected void setJsDataVariable(PageRequestContext pageRequestContext, String name, String value) {
    @SuppressWarnings("unchecked")
    Map<String, String> jsData = (Map<String, String>) pageRequestContext.getRequest().getAttribute("jsData");
    if (jsData == null) {
      jsData = new HashMap<>();
      pageRequestContext.getRequest().setAttribute("jsData", jsData);
    }
    
    jsData.put(name, value);
  }
  
  protected void sendError(RequestContext requestContext, int response) {
    try {
      requestContext.getResponse().sendError(response);
    }
    catch (IOException e) {
    }
  }
  
}
