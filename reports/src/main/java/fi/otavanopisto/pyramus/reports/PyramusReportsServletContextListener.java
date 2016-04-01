package fi.otavanopisto.pyramus.reports;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * The application context listener responsible of initialization and finalization of the
 * application.
 */
public class PyramusReportsServletContextListener implements ServletContextListener {

  /**
   * Called when the application shuts down.
   * 
   * @param ctx The servlet context event
   */
  public void contextDestroyed(ServletContextEvent ctx) {
  }

  /**
   * Called when the application starts. 
   * 
   * @param servletContextEvent The servlet context event
   */
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    ServletContext ctx = servletContextEvent.getServletContext();
    ctx.getSessionCookieConfig().setName("JSESSIONID_PYRAMUSREPORTS");
  }
}
