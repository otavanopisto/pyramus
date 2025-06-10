package fi.pyramus.services.utils;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

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