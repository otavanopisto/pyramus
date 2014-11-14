package fi.pyramus.rest.filter;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import fi.muikku.security.AuthorizationException;
import fi.pyramus.rest.session.RestSession;
import fi.pyramus.security.impl.SessionController;
import fi.pyramus.security.impl.SessionControllerDelegate;

@WebFilter(
    urlPatterns = "/rest"
)
public class SessionFilter implements Filter {

  @Inject
  private SessionControllerDelegate sessionControllerDelegate;
  
  @Inject
  @RestSession
  private SessionController sessionController;
  
  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
      ServletException {
    sessionControllerDelegate.setImplementation(sessionController);
    
    System.out.println("REST session initialized");
    
//    try {
      chain.doFilter(request, response);
//    } catch (AuthorizationException ae) {
//      response.
//    }
  }

  @Override
  public void destroy() {
  }
  
}
