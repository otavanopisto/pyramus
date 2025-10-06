package fi.otavanopisto.pyramus.views.applications;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationDAO;
import fi.otavanopisto.pyramus.dao.application.ApplicationEmailVerificationDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationEmailVerification;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class VerifyMailViewController extends PyramusViewController {

  private static final Logger logger = Logger.getLogger(VerifyMailViewController.class.getName());
  
  public void process(PageRequestContext pageRequestContext) {
    try {
      if (StringUtils.equals(pageRequestContext.getString("status"), "ok")) {
        pageRequestContext.getRequest().setAttribute("verified", Boolean.TRUE);
      }
      String token = pageRequestContext.getString("v");
      if (StringUtils.isBlank(token)) {
        pageRequestContext.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
        return;
      }
      String applicationIdStr = StringUtils.substringBefore(token, "-");
      if (!NumberUtils.isDigits(applicationIdStr)) {
        pageRequestContext.getResponse().sendError(HttpServletResponse.SC_BAD_REQUEST);
        return;
      }

      // Ensure application exists

      Long applicationId = Long.valueOf(applicationIdStr);
      ApplicationDAO applicationDAO = DAOFactory.getInstance().getApplicationDAO();
      Application application = applicationDAO.findById(applicationId);
      if (application == null) {
        pageRequestContext.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
        return;
      }
      pageRequestContext.getRequest().setAttribute("verificationToken", token);

      // Ensure verification exists

      String verificationToken = StringUtils.substringAfter(token, "-");
      ApplicationEmailVerificationDAO verificationDAO = DAOFactory.getInstance().getApplicationEmailVerificationDAO();
      ApplicationEmailVerification verification = verificationDAO.findByApplicationAndToken(application, verificationToken);
      if (verification == null) {
        pageRequestContext.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
        return;
      }
      boolean applicant = StringUtils.equals(verification.getEmail(), application.getEmail());
      if (applicant) {
        pageRequestContext.setIncludeJSP("/templates/applications/application-verify-email-applicant.jsp");
      }
      else {
        pageRequestContext.setIncludeJSP("/templates/applications/application-verify-email-guardian.jsp");
      }
    }
    catch (IOException e) {
      logger.log(Level.SEVERE, "Error serving errors", e);
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.EVERYONE };
  }

}
