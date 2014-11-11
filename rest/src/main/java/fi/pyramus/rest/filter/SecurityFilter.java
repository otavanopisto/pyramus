package fi.pyramus.rest.filter;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;
import org.jboss.resteasy.core.ResourceMethodInvoker;

import fi.pyramus.domainmodel.clientapplications.ClientApplicationAccessToken;
import fi.pyramus.rest.annotation.Unsecure;
import fi.pyramus.rest.controller.OauthController;

@Provider
public class SecurityFilter implements javax.ws.rs.container.ContainerRequestFilter {

  @Context
  private HttpServletRequest request;

  @Context
  private HttpServletResponse response;

  @Inject
  private OauthController oauthController;

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    ResourceMethodInvoker methodInvoker = (ResourceMethodInvoker) requestContext.getProperty("org.jboss.resteasy.core.ResourceMethodInvoker");
    Method method = methodInvoker.getMethod();
    if(method == null){
      requestContext.abortWith(Response.status(javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR).build());
    }
    if (!method.isAnnotationPresent(Unsecure.class)) {
      try {
        OAuthAccessResourceRequest oauthRequest = new OAuthAccessResourceRequest(request, ParameterStyle.HEADER);
        String accessToken = oauthRequest.getAccessToken();

        ClientApplicationAccessToken clientApplicationAccessToken = oauthController.findByAccessToken(accessToken);
        if (clientApplicationAccessToken == null) {
          requestContext.abortWith(Response.status(javax.ws.rs.core.Response.Status.FORBIDDEN).build());
        } else {
          Long currentTime = System.currentTimeMillis() / 1000L;
          if (currentTime > clientApplicationAccessToken.getExpires()) {
            requestContext.abortWith(Response.status(javax.ws.rs.core.Response.Status.FORBIDDEN).build());
          }
//          else {
//            
//            clientApplicationAccessToken.getClientApplicationAuthorizationCode().getUser()
//
//          }
        }

      } catch (OAuthProblemException e) {
        requestContext.abortWith(Response.status(javax.ws.rs.core.Response.Status.BAD_REQUEST).entity(e.getMessage()).build());
      } catch (OAuthSystemException e) {
        requestContext.abortWith(Response.status(javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build());
      }
    }
  }

}
