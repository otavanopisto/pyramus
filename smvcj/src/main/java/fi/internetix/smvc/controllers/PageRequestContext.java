package fi.internetix.smvc.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fi.internetix.smvc.StatusCode;
import fi.internetix.smvc.dispatcher.RequestDispatchContext;

/**
 * Request context for all page requests in the application.
 */
public class PageRequestContext extends RequestContext {

  public PageRequestContext(RequestDispatchContext dispatchContext, HttpServletRequest servletRequest, HttpServletResponse servletResponse, ServletContext servletContext, String errorJspPage) {
    super(dispatchContext, servletRequest, servletResponse, servletContext);
    
    this.errorJspPage = errorJspPage;
  }

  /**
   * Returns the path of the JSP page to be included in the response.
   * 
   * @return The path of the JSP page to be included in the response
   */
  public String getIncludeJSP() {
    return includeJSP;
  }
  
  /**
   * Sets the path of the JSP page to be included in the response.
   * 
   * @param includeJSP The path of the JSP page to be included in the response
   */
  public void setIncludeJSP(String includeJSP) {
    this.includeJSP = includeJSP;
  }

  /**
   * Returns the path of the Freemarker template to be included in the response.
   * 
   * @return The path of the Freemarker template to be included in the response
   */
  public String getIncludeFtl() {
    return includeFtl;
  }

  /**
   * Sets the path of the Freemarker template to be included in the response.
   * 
   * @param includeJSP The path of theFreemarker template to be included in the response
   */
  public void setIncludeFtl(String includeFtl) {
    this.includeFtl = includeFtl;
  }

  public String getIncludeUrl() {
    return includeUrl;
  }
  
  public void setIncludeUrl(String includeUrl) {
    this.includeUrl = includeUrl;
  }
  
  /**
   * Writes the response to the page request.
   * <p/>
   * If the status code of this context is not {@link StatusCode#OK}, a redirect to the generic
   * error page is done with the following request attributes:
   * <p/>
   * <code>statusCode</code> identifying the error in question<br/>
   * <code>errorLevel</code> stating the severity of the error<br/>
   * <code>errorMessage</code> stating what went wrong<br/>
   * <code>exception</code> itself, if it was set to this context<br/>
   * <p/>
   * If the status code of this context is {@link StatusCode#OK}, the response either redirects to
   * the URL specified in this context, or includes the specified JSP page in the response.
   * 
   * @throws Exception If writing the response fails for any reason
   */
  @Override
  public void writePreCommitResponse(int statusCode) throws Exception {
    getRequest().setAttribute("messages", getMessages());
    if (statusCode == StatusCode.OK) {
      if (getRedirectURL() != null) {
        getResponse().sendRedirect(getRedirectURL());
      }
      else if (getIncludeJSP() != null) {
        getRequest().getRequestDispatcher(getIncludeJSP()).include(getRequest(), getResponse());
      }
      else if (getIncludeFtl() != null) {
        try {
          getRequest().getRequestDispatcher(getIncludeFtl()).include(getRequest(), getResponse());
        } catch (Exception e) {
          // Freemarker templates handle their own errors
          getResponse().setStatus(500);
        }
      } 
    }
    else {
      getRequest().setAttribute("statusCode", statusCode);
      getRequest().setAttribute("messages", getMessages());
      getRequest().getRequestDispatcher(errorJspPage).include(getRequest(), getResponse());
    }
  }

  @Override
  public void writePostCommitResponse(int statusCode) throws Exception {
    if (statusCode == StatusCode.OK && getIncludeUrl() != null) {
      handleIncludeUrl();
    }
  }
  
  private void handleIncludeUrl() throws IOException {
    URL includeURL = new URL(getIncludeUrl());
    HttpURLConnection connection = (HttpURLConnection) includeURL.openConnection(); 
    
    connection.setDoInput(true);
    connection.setDoOutput(true);
    connection.connect();
    
    OutputStream outputStream = getResponse().getOutputStream();
    
    InputStream inputStream = connection.getInputStream();
    byte[] buf = new byte[1024];
    int l = 0;
    while ((l = inputStream.read(buf)) > 0) {
      outputStream.write(buf, 0, l);
    }
    
    outputStream.flush();
    outputStream.close();
  }

  private String includeUrl;
  private String includeFtl;
  private String includeJSP;
  private String errorJspPage;
}
