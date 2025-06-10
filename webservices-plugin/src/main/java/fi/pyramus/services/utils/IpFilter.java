package fi.pyramus.services.utils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

public class IpFilter implements Filter {
  
  public void init(FilterConfig fc) throws ServletException {
    String ipConfStr = System.getProperty("PyramusWSAllowedIPs");
    
    if (ipConfStr != null) {
      String[] ipConfArr = ipConfStr.split(",");
      for (int i = 0; i < ipConfArr.length; i++) {
        allowedIPs.add(ipConfArr[i]);
      }
    }
  }

  public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
//    if (!allowedIPs.isEmpty() && !allowedIPs.contains(req.getRemoteAddr())) {
    if (!allowedIPs.contains(req.getRemoteAddr())) {
      System.out.println("SOAP Access denied from: " + req.getRemoteAddr());
      ((HttpServletResponse) resp).sendError(HttpServletResponse.SC_FORBIDDEN);
    }
    else {
      chain.doFilter(req, resp);
    }
  }

  public void destroy() {
  }
  
  private Set<String> allowedIPs = new HashSet<>();
}