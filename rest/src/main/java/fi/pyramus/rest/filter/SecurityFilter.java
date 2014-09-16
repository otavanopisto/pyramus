package fi.pyramus.rest.filter;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
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
  
  @Inject
  private Logger logger;

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    ResourceMethodInvoker methodInvoker = (ResourceMethodInvoker) requestContext.getProperty("org.jboss.resteasy.core.ResourceMethodInvoker");
    Method method = methodInvoker.getMethod();

    if (!method.isAnnotationPresent(Unsecure.class)) {

      try {
        OAuthAccessResourceRequest oauthRequest = new OAuthAccessResourceRequest(request, ParameterStyle.HEADER);
        String accessToken = oauthRequest.getAccessToken();

        ClientApplicationAccessToken clientApplicationAccessToken = oauthController.findByAccessToken(accessToken);
        if (clientApplicationAccessToken == null) {
          requestContext.abortWith(Response.status(javax.ws.rs.core.Response.Status.UNAUTHORIZED).build());
        } else {
          Long currentTime = System.currentTimeMillis() / 1000L;
          if (currentTime > clientApplicationAccessToken.getExpires()) {
            requestContext.abortWith(Response.status(javax.ws.rs.core.Response.Status.UNAUTHORIZED).build());
          }
        }

      } catch (OAuthProblemException | OAuthSystemException ex) {
        logger.severe("############################################");
        logger.severe(ex.getStackTrace().toString());
        logger.severe("############################################");
        requestContext.abortWith(Response.status(javax.ws.rs.core.Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity(ex.getMessage()).build());
      }

    }

  }

}
