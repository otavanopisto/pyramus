package fi.otavanopisto.pyramus.util;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

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