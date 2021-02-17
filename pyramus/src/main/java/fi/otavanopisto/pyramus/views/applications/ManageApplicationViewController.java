package fi.otavanopisto.pyramus.views.applications;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.AccessDeniedException;
import fi.internetix.smvc.Feature;
import fi.internetix.smvc.LoginRequiredException;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.internetix.smvc.controllers.RequestContext;
import fi.otavanopisto.pyramus.applications.ApplicationUtils;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationDAO;
import fi.otavanopisto.pyramus.dao.application.ApplicationSignaturesDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationSignatures;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class ManageApplicationViewController extends PyramusViewController {

  private static final Logger logger = Logger.getLogger(EditApplicationViewController.class.getName());
  
  public void process(PageRequestContext pageRequestContext) {
    try {

      StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
      StaffMember staffMember = staffMemberDAO.findById(pageRequestContext.getLoggedUserId());
      
      Long applicationId = NumberUtils.createLong(pageRequestContext.getRequest().getParameter("application"));
      if (applicationId == null) {
        pageRequestContext.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
        return;
      }
      ApplicationDAO applicationDAO = DAOFactory.getInstance().getApplicationDAO();
      Application application = applicationDAO.findById(applicationId);
      if (application == null) {
        pageRequestContext.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
        return;
      }
      ApplicationSignaturesDAO applicationSignaturesDAO = DAOFactory.getInstance().getApplicationSignaturesDAO();
      ApplicationSignatures signatures = applicationSignaturesDAO.findByApplication(application);
      
      // Hakemuksen tilatiedot

      pageRequestContext.getRequest().setAttribute("infoState", application.getState());
      pageRequestContext.getRequest().setAttribute("infoStateUi", ApplicationUtils.applicationStateUiValue(application.getState()));
      pageRequestContext.getRequest().setAttribute("infoApplicantEditable", application.getApplicantEditable());
      if (application.getHandler() != null) {
        pageRequestContext.getRequest().setAttribute("infoHandler", application.getHandler().getFullName());
        pageRequestContext.getRequest().setAttribute("infoHandlerId", application.getHandler().getId());
      }
      if (application.getStudent() != null) {
        pageRequestContext.getRequest().setAttribute("infoStudentUrl",
            String.format("/students/viewstudent.page?person=%d#at-student.%d",
                application.getStudent().getPerson().getId(),
                application.getStudent().getId()));
      }
      pageRequestContext.getRequest().setAttribute("infoCreated", application.getCreated());
      pageRequestContext.getRequest().setAttribute("infoLastModified", ApplicationUtils.getLatest(
          application.getLastModified(),
          application.getApplicantLastModified(),
          application.getCreated()));
      pageRequestContext.getRequest().setAttribute("infoSignatures", signatures);
      pageRequestContext.getRequest().setAttribute("infoSsn", staffMember == null ? null : staffMember.getPerson().getSocialSecurityNumber());
      
      pageRequestContext.getRequest().setAttribute("applicationEntityId", application.getId());      
      pageRequestContext.getRequest().setAttribute("applicationId", application.getApplicationId());
      
      // Editointinäkymä

      pageRequestContext.getRequest().setAttribute("mode", "edit");
      pageRequestContext.getRequest().setAttribute("referenceCode", application.getReferenceCode());
      pageRequestContext.getRequest().setAttribute("preload", Boolean.TRUE);
      pageRequestContext.getRequest().setAttribute("donePage", Boolean.FALSE);
      pageRequestContext.getRequest().setAttribute("saveUrl", "/applications/saveapplication.json");
      
      pageRequestContext.setIncludeJSP("/templates/applications/management-edit-application.jsp");
    }
    catch (IOException e) {
      logger.log(Level.SEVERE, "Unable to serve error response", e);
      return;
    }
  }

  @Override
  public void authorize(RequestContext requestContext) throws LoginRequiredException, AccessDeniedException {
    if (!requestContext.hasFeature(Feature.APPLICATION_MANAGEMENT)) {
      throw new AccessDeniedException(requestContext.getRequest().getLocale());
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR };
  }

}
