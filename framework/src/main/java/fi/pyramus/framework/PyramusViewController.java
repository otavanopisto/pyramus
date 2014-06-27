package fi.pyramus.framework;

import java.util.HashMap;
import java.util.Map;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import fi.internetix.smvc.AccessDeniedException;
import fi.internetix.smvc.LoginRequiredException;
import fi.internetix.smvc.controllers.PageController;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.internetix.smvc.controllers.RequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.users.UserDAO;
import fi.pyramus.domainmodel.users.Role;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.security.impl.PyramusRights;

public abstract class PyramusViewController implements PageController {

  // TODO: Remove this and UserRole
  public abstract UserRole[] getAllowedRoles();

  // TODO: Declare abstract
  public String getPermission() {
    return null;
  }
  
  public void authorize(RequestContext requestContext) throws LoginRequiredException, AccessDeniedException {
    
    // TODO: Below for permission framework
//    if (getPermission() != null) {
//      try {
//        if (!PyramusRights.hasPermission(getPermission())) {
//          throw new AccessDeniedException(requestContext.getRequest().getLocale());
//        }
//      } catch (NamingException e) {
//        e.printStackTrace();
//        throw new AccessDeniedException(requestContext.getRequest().getLocale());
//      }
//    }
 
    // TODO: Remove old rights code
    
    UserRole[] roles = getAllowedRoles();
    if (!contains(roles, UserRole.EVERYONE)) {
      if (!requestContext.isLoggedIn()) {
        HttpServletRequest request = requestContext.getRequest();
        StringBuilder currentUrl = new StringBuilder(request.getRequestURL());
        String queryString = request.getQueryString();
        if (!StringUtils.isBlank(queryString)) {
          currentUrl.append('?');
          currentUrl.append(queryString);
        }
        throw new LoginRequiredException(currentUrl.toString());
      }
      else {
        Long loggedUserId = requestContext.getLoggedUserId();
        
        UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
        User user = userDAO.findById(loggedUserId);
        
        Role role = user.getRole();
        
        // TODO Ugly hax
        UserRole userRole = UserRole.getRole(role.getValue());
        
        if (!contains(roles, userRole))
          throw new AccessDeniedException(requestContext.getRequest().getLocale());
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
      jsData = new HashMap<String, String>();
      pageRequestContext.getRequest().setAttribute("jsData", jsData);
    }
    
    jsData.put(name, value);
  }
  
}
