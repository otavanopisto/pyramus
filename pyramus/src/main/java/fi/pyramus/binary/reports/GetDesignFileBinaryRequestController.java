package fi.pyramus.binary.reports;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.AccessDeniedException;
import fi.internetix.smvc.LoginRequiredException;
import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.StatusCode;
import fi.internetix.smvc.controllers.BinaryRequestContext;
import fi.internetix.smvc.controllers.RequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.reports.ReportDAO;
import fi.pyramus.domainmodel.reports.Report;
import fi.pyramus.framework.BinaryRequestController;
import fi.pyramus.framework.UserRole;

public class GetDesignFileBinaryRequestController extends BinaryRequestController {

  public void process(BinaryRequestContext binaryRequestContext) {
    ReportDAO reportDAO = DAOFactory.getInstance().getReportDAO();
    Long reportId = binaryRequestContext.getLong("reportId");
    
    Report report = reportDAO.findById(reportId);
    String reportETag = String.valueOf(report.getLastModified().getTime());
    
    if (reportETag.equals(binaryRequestContext.getRequest().getHeader("If-None-Match"))) {
      binaryRequestContext.getResponse().setStatus(HttpServletResponse.SC_NOT_MODIFIED);
      return;
    }
    
    try {
      binaryRequestContext.getResponse().setHeader("ETag", reportETag);
      binaryRequestContext.setResponseContent(report.getData().getBytes("UTF-8"), "application/octet-stream");
    } catch (UnsupportedEncodingException e) {
      throw new SmvcRuntimeException(StatusCode.UNDEFINED, "Invalid charset UTF-8", e);
    }
  }
  
  @Override
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.EVERYONE };
  }
  
  @Override
  public void authorize(RequestContext requestContext) throws LoginRequiredException, AccessDeniedException {
    String reportskey = System.getProperty("pyramus-reports-key");
    if (StringUtils.isNotBlank(reportskey)) {
      String authorization = "PyramusReportsKey " + reportskey;
      if (authorization.equals(requestContext.getRequest().getHeader("Authorization"))) {
        return;
      }
    }
    
    throw new AccessDeniedException(requestContext.getRequest().getLocale());
  }
  
}
