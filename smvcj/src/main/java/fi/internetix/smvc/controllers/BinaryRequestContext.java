package fi.internetix.smvc.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import fi.internetix.smvc.StatusCode;
import fi.internetix.smvc.dispatcher.RequestDispatchContext;

/**
 * Request context for all binary requests in the application.
 */
public class BinaryRequestContext extends RequestContext {

  /** Create a new binary request context.
   * 
   * @param dispatchContext The dispatch context, passed to RequestContext constructor
   * @param servletRequest The servlet request, passed to RequestContext constructor
   * @param servletResponse The servlet response, passed to RequestContext constructor
   * @param servletContext The servlet context, passed to RequestContext constructor
   */
  public BinaryRequestContext(RequestDispatchContext dispatchContext, HttpServletRequest servletRequest, HttpServletResponse servletResponse, ServletContext servletContext) {
    super(dispatchContext, servletRequest, servletResponse, servletContext);
  }

  /** Sets the content of the binary response, with the given MIME content type.
   *  
   * @param content The content to send to the client.
   * @param contentType The MIME type of the content to send.
   */
  public void setResponseContent(byte[] content, String contentType) {
    this.content = content;
    this.contentType = contentType;
  }
  
  /** Sets the URL of the content.
   * 
   * @param contentUrl The new content of the URL.
   */
  public void setContentUrl(String contentUrl) {
    this.contentUrl = contentUrl;
  }
  
  /** Returns the URL of the content.
   * 
   * @return The URL of the content.
   */
  public String getContentUrl() {
    return contentUrl;
  }
  
  /** Sets the filename for the content.
   * 
   * @param fileName The new filename for the content.
   */
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  @Override
  public void writePreCommitResponse(int statusCode) throws Exception {
    if (getRedirectURL() != null) {
      getResponse().sendRedirect(getRedirectURL());
    }
  }

  /**
   * Writes the response to the binary request.
   * 
   * @throws Exception If writing the response fails for any reason
   */
  @Override
  public void writePostCommitResponse(int statusCode) throws Exception {
    if (statusCode == StatusCode.OK) {
      if (fileName != null) {
        getResponse().setHeader("Content-Disposition", "attachment; filename=" + fileName);
      }
      
      if (contentType != null && content != null) {
        getResponse().setContentType(contentType);
        getResponse().getOutputStream().write(content);
      } else if (!StringUtils.isBlank(contentUrl)) {
        handleContentUrl(); 
      }
    }
    else {
      // TODO Error handing
      switch (statusCode) {
        case StatusCode.UNAUTHORIZED:
        case StatusCode.NOT_LOGGED_IN:
          getResponse().setStatus(403);
          break;
        default:
          getResponse().setStatus(500);
          break;
      }
    }
  }
  
  private void handleContentUrl() throws IOException {
    String contentUrl = getContentUrl();
    URL includeURL;
    
    boolean isExternal = contentUrl.startsWith("http://")||contentUrl.startsWith("https://");
    if (isExternal) {
      includeURL = new URL(contentUrl);
    } else {
      boolean isRelative = !contentUrl.startsWith("/");
      
      // TODO: This is not quite correct because if this request is secure it doesn't mean that include request would be 
      String protocol = "http";
      if (getRequest().isSecure()) {
        protocol = "https";
      }
      
      String serverName = getRequest().getServerName();
  
      StringBuilder urlBuilder = new StringBuilder();
      if (isRelative) {
        urlBuilder
          .append(getRequest().getContextPath())
          .append(getRequest().getPathInfo())
          .append('/')
          .append(contentUrl);
      } else {
        urlBuilder.append(contentUrl);
      }
        
      includeURL = new URL(protocol, serverName, getRequest().getLocalPort(), urlBuilder.toString());
    }
    
    HttpURLConnection connection = (HttpURLConnection) includeURL.openConnection(); 
    
    connection.setDoInput(true);
    connection.setDoOutput(true);
    connection.connect();
    
    getResponse().setContentType(connection.getContentType());
    OutputStream outputStream = getResponse().getOutputStream();
    
    InputStream inputStream = connection.getInputStream();
    byte[] buf = new byte[1024];
    int l;
    while ((l = inputStream.read(buf)) > 0) {
      outputStream.write(buf, 0, l);
    }
    
    outputStream.flush();
    outputStream.close();
  }

  private String fileName;
  private byte[] content;
  private String contentType;
  private String contentUrl;
}
