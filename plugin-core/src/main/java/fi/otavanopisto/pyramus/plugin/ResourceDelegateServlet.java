package fi.otavanopisto.pyramus.plugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

/**
 * Delegates plugin resources. 
 */
public class ResourceDelegateServlet extends HttpServlet {
  
  private static final long serialVersionUID = 6948865332192958933L;

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    
    this.classPath = config.getInitParameter("ClassPath");
  }
  
  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String resourcePath = request.getPathInfo();
    String contextPath = request.getContextPath();
    if (StringUtils.isNotBlank(contextPath)) {
      resourcePath = resourcePath.substring(contextPath.length());
    }
    
    URL resource = PluginManager.getInstance().getPluginsClassLoader().getResource(classPath + resourcePath);
    if (resource != null) {
      ServletOutputStream outputStream = response.getOutputStream();
      
      URLConnection connection = resource.openConnection();
      connection.setDoInput(true);
      connection.setDoOutput(true);
//      String contentType = connection.getContentType();
      InputStream resourceStream = connection.getInputStream();
      int bytesRead = 0;
      byte[] buff = new byte[1024];
      while ((bytesRead = resourceStream.read(buff, 0, 1024)) > 0) {
        outputStream.write(buff, 0, bytesRead);
      }
      resourceStream.close();
      
//      response.setContentType(contentType);
      outputStream.flush();
      outputStream.close();
    } else {
      response.sendError(HttpURLConnection.HTTP_NOT_FOUND);
    }
  }

  
  private String classPath;
}
