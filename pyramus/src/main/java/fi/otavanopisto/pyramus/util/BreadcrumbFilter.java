package fi.otavanopisto.pyramus.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import fi.internetix.smvc.controllers.RequestController;
import fi.internetix.smvc.controllers.RequestControllerMapper;
import fi.otavanopisto.pyramus.breadcrumbs.BreadcrumbHandler;
import fi.otavanopisto.pyramus.breadcrumbs.Breadcrumbable;
import fi.otavanopisto.pyramus.framework.PyramusViewController;

public class BreadcrumbFilter implements Filter {

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    try {
      if (request instanceof HttpServletRequest) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        
        String uri = httpRequest.getRequestURI();
        String ctxPath = httpRequest.getContextPath();
        String controllerName = uri.substring(ctxPath.length() + 1);
        
        // TODO: Needed??
  //      if (StringUtils.isNotBlank(applicationPath)) {
  //        controllerName = controllerName.substring(applicationPath.length());
  //      }
        RequestController requestController = RequestControllerMapper.getRequestController(controllerName);
  
        if (requestController instanceof PyramusViewController && requestController instanceof Breadcrumbable) {
          BreadcrumbHandler breadcrumbHandler = getBreadcrumbHandler(httpRequest);
          if (request.getParameter("resetbreadcrumb") != null) {
            breadcrumbHandler.clear();
          }
          if (requestController instanceof Breadcrumbable && "GET".equals(httpRequest.getMethod())) {
            Breadcrumbable breadcrumbable = (Breadcrumbable) requestController;
            breadcrumbHandler.process(httpRequest, breadcrumbable);
          }        
        }
      }      
    } finally {
      chain.doFilter(request, response);
    }
  }
 
  private synchronized BreadcrumbHandler getBreadcrumbHandler(HttpServletRequest request) {
    HttpSession session = request.getSession(true);
    BreadcrumbHandler breadcrumbHandler = (BreadcrumbHandler) session.getAttribute("breadcrumbHandler");
    if (breadcrumbHandler == null) {
      breadcrumbHandler = new BreadcrumbHandler();
      session.setAttribute("breadcrumbHandler", breadcrumbHandler);
    }
    return breadcrumbHandler;
  }
  
  @Override
  public void destroy() {
  }
}