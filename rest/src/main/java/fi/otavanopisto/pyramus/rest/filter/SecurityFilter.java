package fi.otavanopisto.pyramus.rest.filter;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.core.ResourceMethodInvoker;

import fi.otavanopisto.pyramus.rest.annotation.Unsecure;
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
  private SessionControllerDelegate sessionControllerDelegate;
  
  @Inject
  @RestSession
  private SessionController sessionController;

  @Inject
  private RESTSecurity restSecurity;
  
  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    sessionControllerDelegate.setImplementation(sessionController);
    
    ResourceMethodInvoker methodInvoker = (ResourceMethodInvoker) requestContext.getProperty("org.jboss.resteasy.core.ResourceMethodInvoker");
    Method method = methodInvoker.getMethod();
    if (method == null){
      requestContext.abortWith(Response.status(javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR).build());
    } else {
      if (!method.isAnnotationPresent(Unsecure.class)) {
        if (!restSecurity.hasPermission(method)) {
          requestContext.abortWith(Response.status(javax.ws.rs.core.Response.Status.FORBIDDEN).build());
        }
      }
    }
  }
  
}
