package fi.otavanopisto.pyramus.binary.applications;

import java.io.ByteArrayOutputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.xhtmlrenderer.pdf.ITextRenderer;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.StatusCode;
import fi.internetix.smvc.controllers.BinaryRequestContext;
import fi.otavanopisto.pyramus.framework.BinaryRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class StaffSignatureDocumentRequestController extends BinaryRequestController {

  public void process(BinaryRequestContext binaryRequestContext) {
    try {
      HttpServletRequest httpRequest = binaryRequestContext.getRequest();
      StringBuilder baseUrl = new StringBuilder();
      baseUrl.append(httpRequest.getScheme());
      baseUrl.append("://");
      baseUrl.append(httpRequest.getServerName());
      baseUrl.append(":");
      baseUrl.append(httpRequest.getServerPort());
      String document = IOUtils.toString(binaryRequestContext.getServletContext().getResourceAsStream("/templates/applications/document-staff-signed.html"), "UTF-8");
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      ITextRenderer renderer = new ITextRenderer();
      renderer.setDocumentFromString(document, baseUrl.toString());
      renderer.layout();
      renderer.createPDF(out);
      binaryRequestContext.setResponseContent(out.toByteArray(), "application/pdf");
    } catch (Exception e) {
      throw new SmvcRuntimeException(StatusCode.FILE_HANDLING_FAILURE, "Unable to serve document", e);
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER };
  }

}
