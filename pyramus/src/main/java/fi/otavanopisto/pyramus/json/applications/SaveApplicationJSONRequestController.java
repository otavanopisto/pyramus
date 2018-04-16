package fi.otavanopisto.pyramus.json.applications;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;
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

public class SaveApplicationJSONRequestController extends JSONRequestController {

  private static final Logger logger = Logger.getLogger(SaveApplicationJSONRequestController.class.getName());

  public void process(JSONRequestContext requestContext) {
    try {
      StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
      StaffMember staffMember = staffMemberDAO.findById(requestContext.getLoggedUserId());
      if (staffMember == null) {
        logger.log(Level.WARNING, "Refusing application due to staff member not found");
        requestContext.getResponse().sendError(HttpServletResponse.SC_BAD_REQUEST);
        return;
      }
      String formDataStr = getFormData(requestContext.getRequest());
      if (formDataStr == null) {
        logger.log(Level.WARNING, "Refusing application due to missing form data");
        requestContext.getResponse().sendError(HttpServletResponse.SC_BAD_REQUEST);
        return;
      }
      
      // Form validation
      
      JSONObject formData = JSONObject.fromObject(formDataStr);
      String applicationId = formData.getString("field-application-id");
      if (applicationId == null) {
        logger.log(Level.WARNING, "Refusing application due to missing applicationId");
        requestContext.getResponse().sendError(HttpServletResponse.SC_BAD_REQUEST);
        return;
      }
      String line = formData.getString("field-line");
      if (line == null) {
        logger.log(Level.WARNING, "Refusing application due to missing line");
        requestContext.getResponse().sendError(HttpServletResponse.SC_BAD_REQUEST);
        return;
      }
      String firstName = formData.getString("field-first-names");
      if (firstName == null) {
        logger.log(Level.WARNING, "Refusing application due to missing first name");
        requestContext.getResponse().sendError(HttpServletResponse.SC_BAD_REQUEST);
        return;
      }
      String lastName = formData.getString("field-last-name");
      if (lastName == null) {
        logger.log(Level.WARNING, "Refusing application due to missing last name");
        requestContext.getResponse().sendError(HttpServletResponse.SC_BAD_REQUEST);
        return;
      }
      String email = formData.getString("field-email");
      if (email == null) {
        logger.log(Level.WARNING, "Refusing application due to missing email");
        requestContext.getResponse().sendError(HttpServletResponse.SC_BAD_REQUEST);
        return;
      }
      
      // Save application
      
      ApplicationDAO applicationDAO = DAOFactory.getInstance().getApplicationDAO();
      Application application = applicationDAO.findByApplicationId(applicationId);
      if (application == null) {
        requestContext.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
        return;
      }
      boolean referenceCodeModified = !StringUtils.equals(application.getLastName(), lastName);
      String referenceCode = referenceCodeModified ? generateReferenceCode(lastName, application.getReferenceCode()) : application.getReferenceCode(); 
      application = applicationDAO.update(
          application,
          line,
          firstName,
          lastName,
          email,
          referenceCode,
          formDataStr,
          application.getState(),
          application.getApplicantEditable(),
          staffMember);
      
      String redirecUrl = requestContext.getRequest().getContextPath() + "/applications/view.page?application=" + application.getId();
      requestContext.setRedirectURL(redirecUrl);
    }
    catch (Exception e) {
      logger.log(Level.SEVERE, "Error saving application", e);
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER };
  }
  
  private String getFormData(HttpServletRequest req) throws IOException {
    StringBuilder sb = new StringBuilder();
    BufferedReader reader = req.getReader();
    try {
      String line;
      while ((line = reader.readLine()) != null) {
        sb.append(line).append('\n');
      }
    }
    finally {
      reader.close();
    }
    return sb.toString();
  }

  private String generateReferenceCode(String lastName, String initialReferenceCode) {
    ApplicationDAO applicationDAO = DAOFactory.getInstance().getApplicationDAO();
    String referenceCode = initialReferenceCode;
    while (applicationDAO.findByLastNameAndReferenceCode(lastName, referenceCode) != null) {
      referenceCode = RandomStringUtils.randomAlphabetic(6).toUpperCase();
    }
    return referenceCode;
  }

}
