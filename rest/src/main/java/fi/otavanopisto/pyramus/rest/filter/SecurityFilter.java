package fi.otavanopisto.pyramus.rest.filter;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;
import org.jboss.resteasy.core.ResourceMethodInvoker;

import fi.otavanopisto.pyramus.dao.users.UserDAO;
import fi.otavanopisto.pyramus.domainmodel.clientapplications.ClientApplicationAccessToken;
import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.framework.UserUtils;
import fi.otavanopisto.pyramus.rest.annotation.Unsecure;
import fi.otavanopisto.pyramus.rest.controller.OauthController;
import fi.otavanopisto.pyramus.rest.security.RESTSecurity;
import fi.otavanopisto.pyramus.rest.session.RestSession;
import fi.otavanopisto.pyramus.security.impl.SessionController;
import fi.otavanopisto.pyramus.security.impl.SessionControllerDelegate;

@Provider
public class SecurityFilter implements javax.ws.rs.container.ContainerRequestFilter {

  @Context
  private HttpServletRequest request;

  @Context
  private HttpServletResponse response;

  @Inject
  private OauthController oauthController;
  
  @Inject
  private SessionControllerDelegate sessionControllerDelegate;
  
  @Inject
  @RestSession
  private SessionController sessionController;

  @Inject
  private RESTSecurity restSecurity;
  
  @Inject
  private UserDAO userDAO;
  
  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    sessionControllerDelegate.setImplementation(sessionController);
    
    ResourceMethodInvoker methodInvoker = (ResourceMethodInvoker) requestContext.getProperty("org.jboss.resteasy.core.ResourceMethodInvoker");
    Method method = methodInvoker.getMethod();
    if (method == null){
      requestContext.abortWith(Response.status(javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR).build());
    } else {
      if (!method.isAnnotationPresent(Unsecure.class)) {
        if (hasApiAccess()) {
          if (!restSecurity.hasPermission(method)) {
            requestContext.abortWith(Response.status(javax.ws.rs.core.Response.Status.FORBIDDEN).build());
          }
        } else {
          requestContext.abortWith(Response.status(javax.ws.rs.core.Response.Status.FORBIDDEN).build());
        }
      }
    }
  }

  private boolean hasApiAccess() {
    return hasOAuthApiAccess() || hasSessionApiAccess();
  }
  
  private boolean hasOAuthApiAccess() {
    try {
      OAuthAccessResourceRequest oauthRequest = new OAuthAccessResourceRequest(request, ParameterStyle.HEADER);
      String accessToken = oauthRequest.getAccessToken();

      ClientApplicationAccessToken clientApplicationAccessToken = oauthController.findByAccessToken(accessToken);
      if (clientApplicationAccessToken == null) {
        return false;
      } else {
        Long currentTime = System.currentTimeMillis() / 1000L;
        if (currentTime > clientApplicationAccessToken.getExpires()) {
          return false;
        } else {
          return true;
        }
      }
    } catch (OAuthProblemException e) {
      return false;
    } catch (OAuthSystemException e) {
      return false;
    }
  }
  
  private boolean hasSessionApiAccess() {
    HttpSession session = request.getSession(false);
    if (session != null) {
      Long loggedUserId = (Long) session.getAttribute("loggedUserId");
      if (loggedUserId != null) {
        User loggedUser = userDAO.findById(loggedUserId);
        if (loggedUser != null) {
          Role[] allowedRoles = new Role[] {
              Role.ADMINISTRATOR,
              Role.MANAGER,
              Role.STUDY_PROGRAMME_LEADER,
              Role.STUDY_GUIDER,
              Role.TEACHER
          };
          
          return UserUtils.hasManagementOrganizationAccess(loggedUser) && loggedUser.hasAnyRole(allowedRoles);
        } else {
          return false;
        }
      }
    }
    
    return false;
  }
}
