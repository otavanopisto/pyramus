package fi.otavanopisto.pyramus.json.applications;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationMailTemplateDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationMailTemplate;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class EditMailTemplateJSONRequestController extends JSONRequestController {

  private static final Logger logger = Logger.getLogger(EditMailTemplateJSONRequestController.class.getName());

  public void process(JSONRequestContext requestContext) {
    try {
      StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
      StaffMember staffMember = staffMemberDAO.findById(requestContext.getLoggedUserId());
      if (staffMember == null) {
        logger.log(Level.WARNING, "Refusing mail template due to staff member not found");
        requestContext.getResponse().sendError(HttpServletResponse.SC_BAD_REQUEST);
        return;
      }
      
      Long templateId = requestContext.getLong("templateId");
      String line = requestContext.getString("line");
      String name = requestContext.getString("name");
      String subject = requestContext.getString("subject");
      String content = requestContext.getString("content");
      if (StringUtils.isEmpty(name) || StringUtils.isEmpty(subject) || StringUtils.isEmpty(content)) {
        requestContext.getResponse().sendError(HttpServletResponse.SC_BAD_REQUEST);
        return;
      }
      
      ApplicationMailTemplate applicationMailTemplate = null;
      ApplicationMailTemplateDAO applicationMailTemplateDAO = DAOFactory.getInstance().getApplicationMailTemplateDAO();
      if (templateId == null) {
        applicationMailTemplate = applicationMailTemplateDAO.create(line, name, subject, content, staffMember);
      }
      else {
        applicationMailTemplate = applicationMailTemplateDAO.findById(templateId);
        if (applicationMailTemplate == null) {
          requestContext.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
          return;
        }
        applicationMailTemplate = applicationMailTemplateDAO.update(applicationMailTemplate, line, name, subject, content);
      }
      requestContext.setRedirectURL(requestContext.getReferer(true));      
    }
    catch (Exception e) {
      logger.log(Level.SEVERE, "Error saving mail template", e);
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER };
  }

}
