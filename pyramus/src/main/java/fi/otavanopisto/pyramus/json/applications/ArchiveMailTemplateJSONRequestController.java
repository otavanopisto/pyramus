package fi.otavanopisto.pyramus.json.applications;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationMailTemplateDAO;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationMailTemplate;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class ArchiveMailTemplateJSONRequestController extends JSONRequestController {

  private static final Logger logger = Logger.getLogger(ArchiveMailTemplateJSONRequestController.class.getName());

  public void process(JSONRequestContext requestContext) {
    try {
      ApplicationMailTemplateDAO applicationMailTemplateDAO = DAOFactory.getInstance().getApplicationMailTemplateDAO();
      ApplicationMailTemplate applicationMailTemplate = null;
      Long id = requestContext.getLong("id");
      if (id != null) {
        applicationMailTemplate = applicationMailTemplateDAO.findById(id);
      }
      if (applicationMailTemplate == null) {
        logger.log(Level.WARNING, "Refusing mail template archive due to template not found");
        requestContext.getResponse().sendError(HttpServletResponse.SC_BAD_REQUEST);
        return;
      }
      applicationMailTemplateDAO.archive(applicationMailTemplate);
    }
    catch (Exception e) {
      logger.log(Level.SEVERE, "Error archiving mail template", e);
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER };
  }

}
