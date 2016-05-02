package fi.pyramus.services.utils;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class IpLoggingFilter implements Filter {
  
  public void init(FilterConfig fc) throws ServletException {
    
  }

  public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
    System.out.println("SOAP Accessed from: " + req.getRemoteAddr());
    chain.doFilter(req, resp);
  }

  public void destroy() {
  }
  

}