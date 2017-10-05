package fi.otavanopisto.pyramus.json.applications;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationState;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.views.applications.ApplicationUtils;

public class UpdateApplicationStateJSONRequestController extends JSONRequestController {

  private static final Logger logger = Logger.getLogger(UpdateApplicationStateJSONRequestController.class.getName());

  public void process(JSONRequestContext requestContext) {
    try {
      StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
      StaffMember staffMember = staffMemberDAO.findById(requestContext.getLoggedUserId());
      if (staffMember == null) {
        requestContext.getResponse().sendError(HttpServletResponse.SC_BAD_REQUEST);
        return;
      }
      Long id = requestContext.getLong("id");
      if (id == null) {
        requestContext.getResponse().sendError(HttpServletResponse.SC_BAD_REQUEST);
        return;
      }
      ApplicationState applicationState = ApplicationState.valueOf(requestContext.getString("state"));
      Boolean lockApplication = requestContext.getBoolean("lockApplication");
      Boolean setHandler = requestContext.getBoolean("setHandler");
      
      // Application update
      
      ApplicationDAO applicationDAO = DAOFactory.getInstance().getApplicationDAO();
      Application application = applicationDAO.findById(id);
      if (application == null) {
        requestContext.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
        return;
      }
      application = applicationDAO.updateApplicationState(application, applicationState, staffMember);
      if (Boolean.TRUE.equals(lockApplication) && application.getApplicantEditable()) {
        application = applicationDAO.updateApplicantEditable(application, Boolean.FALSE, staffMember);
      }
      if (Boolean.TRUE.equals(setHandler)) {
        application = applicationDAO.updateApplicationHandler(application, staffMember);
      }

      // Application log entry

      // Response parameters
      
      requestContext.addResponseParameter("id", application.getId());
      requestContext.addResponseParameter("state", ApplicationUtils.applicationStateUiValue(application.getState()));
      requestContext.addResponseParameter("applicantEditable", application.getApplicantEditable());
      requestContext.addResponseParameter("handler", application.getHandler() == null ? null : application.getHandler().getFullName());
      requestContext.addResponseParameter("lastModified", application.getLastModified().getTime());
    }
    catch (Exception e) {
      logger.log(Level.SEVERE, "Error updating application state", e);
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER };
  }

}
