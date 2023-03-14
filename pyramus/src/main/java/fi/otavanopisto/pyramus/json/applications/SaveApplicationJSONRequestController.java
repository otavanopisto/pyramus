package fi.otavanopisto.pyramus.json.applications;

import static fi.otavanopisto.pyramus.applications.ApplicationUtils.getFormValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.applications.ApplicationUtils;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationAttachmentDAO;
import fi.otavanopisto.pyramus.dao.application.ApplicationDAO;
import fi.otavanopisto.pyramus.dao.application.ApplicationLogDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationAttachment;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationLogType;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

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
      String firstName = getFormValue(formData, "field-first-names");
      if (firstName == null) {
        logger.log(Level.WARNING, "Refusing application due to missing first name");
        requestContext.getResponse().sendError(HttpServletResponse.SC_BAD_REQUEST);
        return;
      }
      String lastName = getFormValue(formData, "field-last-name");
      if (lastName == null) {
        logger.log(Level.WARNING, "Refusing application due to missing last name");
        requestContext.getResponse().sendError(HttpServletResponse.SC_BAD_REQUEST);
        return;
      }
      String email = StringUtils.lowerCase(StringUtils.trim(getFormValue(formData, "field-email")));
      if (StringUtils.isBlank(email)) {
        logger.log(Level.WARNING, "Refusing application due to missing email");
        requestContext.getResponse().sendError(HttpServletResponse.SC_BAD_REQUEST);
        return;
      }
      
      // Attachments
      
      if (formData.has("attachment-name") && formData.has("attachment-description")) {
        ApplicationAttachmentDAO applicationAttachmentDAO = DAOFactory.getInstance().getApplicationAttachmentDAO();
        if (JSONUtils.isArray(formData.get("attachment-name"))) {
          JSONArray attachmentNames = formData.getJSONArray("attachment-name");
          JSONArray attachmentDescriptions = formData.getJSONArray("attachment-description");
          for (int i = 0; i < attachmentNames.size(); i++) {
            String name = attachmentNames.getString(i);
            String description = attachmentDescriptions.getString(i);
            ApplicationAttachment applicationAttachment = applicationAttachmentDAO.findByApplicationIdAndName(applicationId, name);
            if (applicationAttachment == null) {
              logger.warning(String.format("Attachment %s for application %s not found", name, applicationId));
            }
            else {
              applicationAttachmentDAO.updateDescription(applicationAttachment, description);
            }
          }
        }
        else {
          String name = getFormValue(formData, "attachment-name");
          String description = getFormValue(formData, "attachment-description");
          ApplicationAttachment applicationAttachment = applicationAttachmentDAO.findByApplicationIdAndName(applicationId, name);
          if (applicationAttachment == null) {
            logger.warning(String.format("Attachment %s for application %s not found", name, applicationId));
          }
          else {
            applicationAttachmentDAO.updateDescription(applicationAttachment, description);
          }
        }
      }
      
      // Save application
      
      ApplicationDAO applicationDAO = DAOFactory.getInstance().getApplicationDAO();
      Application application = applicationDAO.findByApplicationId(applicationId);
      if (application == null) {
        requestContext.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
        return;
      }
      if (!ApplicationUtils.hasLineAccess(staffMember, application.getLine())) {
        logger.log(Level.WARNING, "Refusing application due to missing line access");
        requestContext.getResponse().sendError(HttpServletResponse.SC_FORBIDDEN);
        return;
      }
      boolean referenceCodeModified = !StringUtils.equalsIgnoreCase(application.getLastName(), lastName);
      String oldSurname = referenceCodeModified ? application.getLastName() : lastName;
      String referenceCode = referenceCodeModified
          ? ApplicationUtils.generateReferenceCode(lastName, application.getReferenceCode())
          : application.getReferenceCode(); 
      boolean lineChanged = !StringUtils.equals(line, application.getLine());
      String oldLine = application.getLine();
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
      
      // If line has changed, send notification of a new application 
      
      if (lineChanged) {
        String notification = String.format("Hakemus vaihdettu linjalta <b>%s</b> linjalle <b>%s</b>",
            ApplicationUtils.applicationLineUiValue(oldLine), ApplicationUtils.applicationLineUiValue(line));
        ApplicationLogDAO applicationLogDAO = DAOFactory.getInstance().getApplicationLogDAO();
        applicationLogDAO.create(
            application,
            ApplicationLogType.HTML,
            notification,
            staffMember);
        ApplicationUtils.sendNotifications(application, requestContext.getRequest(), staffMember, true, null, false);
      }
      if (referenceCodeModified) {
        ApplicationUtils.sendApplicationModifiedMail(application, requestContext.getRequest(), oldSurname);
      }
      
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

}
