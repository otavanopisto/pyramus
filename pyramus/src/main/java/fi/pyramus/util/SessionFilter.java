package fi.pyramus.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionFilter implements Filter {

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    if (request instanceof HttpServletRequest) {
      HttpSession session = ((HttpServletRequest) request).getSession(false);
      
      ThreadSessionContainer.setSession(session);
      try {
        chain.doFilter(request, response);
      } finally {
        ThreadSessionContainer.setSession(null);  
      }
      
    } else {
      chain.doFilter(request, response);
    }
  }
  
  @Override
  public void destroy() {
  }
}