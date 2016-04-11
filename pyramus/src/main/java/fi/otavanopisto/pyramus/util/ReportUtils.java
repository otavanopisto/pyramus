package fi.otavanopisto.pyramus.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public class ReportUtils {

  private ReportUtils() {
  }

  public static String getReportsUrl(HttpServletRequest request) {
    String contextPath = System.getProperty("reports.contextPath");
    String host = System.getProperty("reports.host");
    String port = System.getProperty("reports.port");
    String protocol = System.getProperty("reports.protocol");
    
    if (StringUtils.isBlank(contextPath)) {
      contextPath = "_reports";
    }
    
    if (StringUtils.isBlank(host)) {
      host = request.getLocalName();
    }
    
    if (StringUtils.isBlank(port)) {
      port = String.valueOf(request.getLocalPort());
    }
    
    if (StringUtils.isBlank(protocol)) {
      protocol = "https";
    }
    
    return String.format("%s://%s:%s/%s", protocol, host, port, contextPath);
  }
  
}
