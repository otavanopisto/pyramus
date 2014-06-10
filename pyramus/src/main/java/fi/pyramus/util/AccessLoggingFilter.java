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

import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.accesslog.AccessLogEntryDAO;
import fi.pyramus.dao.accesslog.AccessLogEntryPathDAO;
import fi.pyramus.dao.users.UserDAO;
import fi.pyramus.domainmodel.accesslog.AccessLogEntryPath;
import fi.pyramus.domainmodel.users.User;

public class AccessLoggingFilter implements Filter {

  /**
   * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
   */
  public void init(FilterConfig fc) throws ServletException {
    this.filterConfig = fc;
//    this.encoding = filterConfig.getInitParameter("encoding");
  }

  /**
   * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse,
   *      javax.servlet.FilterChain)
   */
  public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException,
      ServletException {

    HttpServletRequest request = (HttpServletRequest) req;
    
    AccessLogEntryDAO accessLogEntryDAO = DAOFactory.getInstance().getAccessLogEntryDAO();

    String requestURI = request.getRequestURI();

    User user = getUser(request);
    String ip = request.getRemoteAddr();
    AccessLogEntryPath path = getPath(requestURI);
    String parameters = request.getQueryString();
    
    accessLogEntryDAO.create(user, ip, path, parameters);
    
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
          path = accessLogEntryPathDAO.create(uri);

        return path;
      }
    }
  }
  
  private User getUser(HttpServletRequest request) {
    HttpSession session = ((HttpServletRequest) request).getSession(false);
    Long userId = session == null ? null : (Long) session.getAttribute("loggedUserId");
    
    if (userId != null) {
      UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
      return userDAO.findById(userId);
    } else
      return null;
  }
  
  /**
   * @see javax.servlet.Filter#destroy()
   */
  public void destroy() {
  }

  private FilterConfig filterConfig;

}