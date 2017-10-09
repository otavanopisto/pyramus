package fi.otavanopisto.pyramus.json.applications;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;
import net.sf.json.JSONObject;

public class ListMailRecipientsJSONRequestController extends JSONRequestController {

  private static final Logger logger = Logger.getLogger(ListMailRecipientsJSONRequestController.class.getName());

  public void process(JSONRequestContext requestContext) {
    try {
      Long applicationEntityId = Long.valueOf(requestContext.getRequest().getParameter("applicationEntityId"));
      ApplicationDAO applicationDAO = DAOFactory.getInstance().getApplicationDAO();
      Application application = applicationDAO.findById(applicationEntityId);
      if (application == null) {
        requestContext.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
        return;
      }
      StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
      StaffMember staffMember = staffMemberDAO.findById(requestContext.getLoggedUserId());
      
      JSONObject applicationData = JSONObject.fromObject(application.getFormData());

      List<Map<String, Object>> results = new ArrayList<>();
      
      // Applicant
      
      Map<String, Object> recipientInfo = new HashMap<>();
      recipientInfo.put("type", "to");
      recipientInfo.put("name", String.format("%s %s", application.getFirstName(), application.getLastName()));
      recipientInfo.put("mail", application.getEmail());
      results.add(recipientInfo);
      
      // Guardian
      
      if (StringUtils.isNotBlank(applicationData.getString("field-underage-email"))) {
        recipientInfo = new HashMap<>();
        recipientInfo.put("type", "to");
        recipientInfo.put("name", String.format("%s %s", applicationData.getString("field-underage-first-name"), applicationData.getString("field-underage-last-name")));
        recipientInfo.put("mail", applicationData.getString("field-underage-email"));
        results.add(recipientInfo);
      }
      
      // Sender

      recipientInfo = new HashMap<>();
      recipientInfo.put("type", "cc");
      recipientInfo.put("name", staffMember.getFullName());
      recipientInfo.put("mail", staffMember.getPrimaryEmail().getAddress());
      results.add(recipientInfo);
      
      requestContext.addResponseParameter("recipients", results);
    }
    catch (Exception e) {
      logger.log(Level.SEVERE, "Error loading mail recipients", e);
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER };
  }

}