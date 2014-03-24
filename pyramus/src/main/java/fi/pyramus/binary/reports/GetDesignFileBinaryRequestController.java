package fi.pyramus.binary.reports;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.AccessDeniedException;
import fi.internetix.smvc.LoginRequiredException;
import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.StatusCode;
import fi.internetix.smvc.controllers.BinaryRequestContext;
import fi.internetix.smvc.controllers.RequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.MagicKeyDAO;
import fi.pyramus.dao.reports.ReportDAO;
import fi.pyramus.domainmodel.base.MagicKey;
import fi.pyramus.domainmodel.reports.Report;
import fi.pyramus.framework.BinaryRequestController;
import fi.pyramus.framework.UserRole;

public class GetDesignFileBinaryRequestController extends BinaryRequestController {

  public void process(BinaryRequestContext binaryRequestContext) {
    ReportDAO reportDAO = DAOFactory.getInstance().getReportDAO();
    Long reportId = binaryRequestContext.getLong("reportId");
    
    Report report = reportDAO.findById(reportId);
    try {
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
    String authorizationHeader = requestContext.getRequest().getHeader("Authorization");
    if (StringUtils.startsWith(authorizationHeader, "MagicKey ")) {
      String headerKey = StringUtils.substring(authorizationHeader, 9);
      if (StringUtils.isNotBlank(headerKey)) {
        MagicKeyDAO magicKeyDAO = DAOFactory.getInstance().getMagicKeyDAO();
        MagicKey magicKey = magicKeyDAO.findByName(headerKey);
        if (magicKey != null) {
          magicKeyDAO.delete(magicKey);
          return;
        }
      }
    }
    
    throw new AccessDeniedException(requestContext.getRequest().getLocale());
  }
  
}
