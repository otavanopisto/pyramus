package fi.otavanopisto.pyramus.json.worklist;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.worklist.WorklistItemTemplateDAO;
import fi.otavanopisto.pyramus.domainmodel.worklist.WorklistItemEditableFields;
import fi.otavanopisto.pyramus.domainmodel.worklist.WorklistItemTemplate;
import fi.otavanopisto.pyramus.domainmodel.worklist.WorklistItemTemplateType;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

/**
 * The controller responsible of creating or modifying a worklist template. 
 * 
 * @see fi.otavanopisto.pyramus.views.worklist.EditWorklistTemplateViewController
 */
public class EditWorklistTemplateJSONRequestController extends JSONRequestController {

  /**
   * Processes the request to create or modify a worklist template.
   * 
   * @param requestContext The JSON request context
   */
  public void process(JSONRequestContext requestContext) {
    String templateIdStr = requestContext.getRequest().getParameter("templateId");
    Long templateId = NumberUtils.isNumber(templateIdStr) ? NumberUtils.createLong(templateIdStr) : null;
    WorklistItemTemplateType templateType = WorklistItemTemplateType.valueOf(requestContext.getRequest().getParameter("templateType"));
    String description = requestContext.getRequest().getParameter("description");
    Double price = NumberUtils.createDouble(requestContext.getRequest().getParameter("price"));
    Double factor = NumberUtils.createDouble(requestContext.getRequest().getParameter("factor"));
    Set<WorklistItemEditableFields> editableFields = new HashSet<>();
    if (requestContext.getRequest().getParameter("dateEditable") != null) {
      editableFields.add(WorklistItemEditableFields.ENTRYDATE);
    }
    if (requestContext.getRequest().getParameter("descriptionEditable") != null) {
      editableFields.add(WorklistItemEditableFields.DESCRIPTION);
    }
    if (requestContext.getRequest().getParameter("priceEditable") != null) {
      editableFields.add(WorklistItemEditableFields.PRICE);
    }
    if (requestContext.getRequest().getParameter("factorEditable") != null) {
      editableFields.add(WorklistItemEditableFields.FACTOR);
    }
    Boolean removable = requestContext.getRequest().getParameter("removable") != null;
    
    WorklistItemTemplateDAO worklistItemTemplateDAO = DAOFactory.getInstance().getWorklistItemTemplateDAO();
    if (templateId == null) {
      worklistItemTemplateDAO.createTemplate(templateType, description, price, factor, editableFields, removable);
    }
    else {
      WorklistItemTemplate template = worklistItemTemplateDAO.findById(templateId);
      worklistItemTemplateDAO.updateTemplate(template, templateType, description, price, factor, editableFields, removable);
    }

    String redirectURL = requestContext.getRequest().getContextPath() + "/worklist/manageworklisttemplates.page";
    String refererAnchor = requestContext.getRefererAnchor();
    if (!StringUtils.isBlank(refererAnchor)) {
      redirectURL += "#" + refererAnchor;
    }
    requestContext.setRedirectURL(redirectURL);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
