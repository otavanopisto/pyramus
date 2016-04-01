package fi.otavanopisto.pyramus.util;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import fi.internetix.smvc.logging.Logging;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.accesslog.AccessLogEntryDAO;
import fi.otavanopisto.pyramus.dao.accesslog.AccessLogEntryPathDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.accesslog.AccessLogEntryPath;
import fi.otavanopisto.pyramus.domainmodel.users.User;

public class AccessLoggingFilter implements Filter {

  /**
   * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
   */
  public void init(FilterConfig fc) throws ServletException {
    String disabled = System.getProperty("pyramus.accessLoggingDisabled");
    
    this.disabled = Boolean.valueOf(disabled);
  }

  /**
   * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse,
   *      javax.servlet.FilterChain)
   */
  public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException,
      ServletException {

    if (!disabled) {
      try {
        HttpServletRequest request = (HttpServletRequest) req;
        
        AccessLogEntryDAO accessLogEntryDAO = DAOFactory.getInstance().getAccessLogEntryDAO();
    
        String requestURI = request.getRequestURI();
    
        if (requestURI != null) {
          if (requestURI.endsWith(".page") || requestURI.endsWith(".json") || requestURI.endsWith(".binary")) {
            AccessLogEntryPath path = getPath(requestURI);
            
            if (path.getActive()) {
              User user = getUser(request);
              String ip = request.getRemoteAddr();
              String parameters = request.getQueryString();
              
              accessLogEntryDAO.create(user, ip, new Date(), path, parameters);
            }
          }
        }
      } catch (Exception ex) {
        Logging.logException("AccessLoggingFilter exception", ex);
      }
    }
    
    chain.doFilter(req, resp);
  }

  private AccessLogEntryPath getPath(String uri) {
    AccessLogEntryPathDAO accessLogEntryPathDAO = DAOFactory.getInstance().getAccessLogEntryPathDAO();
    AccessLogEntryPath path = accessLogEntryPathDAO.findByPath(uri);
    
    if (path != null)
      return path;
    else {
      synchronized (this) {
        path = accessLogEntryPathDAO.findByPath(uri);
        
        if (path == null)
          path = accessLogEntryPathDAO.create(uri, true);

        return path;
      }
    }
  }
  
  private User getUser(HttpServletRequest request) {
    HttpSession session = ((HttpServletRequest) request).getSession(false);
    Long userId = session == null ? null : (Long) session.getAttribute("loggedUserId");
    
    if (userId != null) {
      StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffMemberDAO();
      return userDAO.findById(userId);
    } else
      return null;
  }
  
  /**
   * @see javax.servlet.Filter#destroy()
   */
  public void destroy() {
  }

  private boolean disabled;
}