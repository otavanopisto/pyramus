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

public class GetMailTemplateJSONRequestController extends JSONRequestController {

  private static final Logger logger = Logger.getLogger(GetMailTemplateJSONRequestController.class.getName());

  public void process(JSONRequestContext requestContext) {
    try {
      Long id = requestContext.getLong("id");
      if (id == null) {
        requestContext.getResponse().sendError(HttpServletResponse.SC_BAD_REQUEST);
        return;
      }
      ApplicationMailTemplateDAO applicationMailTemplateDAO = DAOFactory.getInstance().getApplicationMailTemplateDAO();
      ApplicationMailTemplate applicationMailTemplate = applicationMailTemplateDAO.findById(id);
      if (applicationMailTemplate == null) {
        requestContext.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
        return;
      }
      requestContext.addResponseParameter("id", applicationMailTemplate.getId());
      requestContext.addResponseParameter("line", applicationMailTemplate.getLine());
      requestContext.addResponseParameter("name", applicationMailTemplate.getName());
      requestContext.addResponseParameter("subject", applicationMailTemplate.getSubject());
      requestContext.addResponseParameter("content", applicationMailTemplate.getContent());
    }
    catch (Exception e) {
      logger.log(Level.SEVERE, "Error retrieving mail template", e);
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER };
  }

}
