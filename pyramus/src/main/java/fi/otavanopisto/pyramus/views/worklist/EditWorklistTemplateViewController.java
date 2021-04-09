package fi.otavanopisto.pyramus.views.worklist;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.breadcrumbs.Breadcrumbable;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.worklist.WorklistItemTemplateDAO;
import fi.otavanopisto.pyramus.domainmodel.worklist.WorklistItemEditableFields;
import fi.otavanopisto.pyramus.domainmodel.worklist.WorklistItemTemplate;
import fi.otavanopisto.pyramus.domainmodel.worklist.WorklistItemTemplateType;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

/**
 * The controller responsible of the Edit Worklist Template view of the application.
 * 
 * @see fi.otavanopisto.pyramus.json.worklist.EditWorklistTemplateJSONRequestController
 */
public class EditWorklistTemplateViewController extends PyramusViewController implements Breadcrumbable {

  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    WorklistItemTemplateDAO worklistItemTemplateDAO = DAOFactory.getInstance().getWorklistItemTemplateDAO();
    Long templateId = NumberUtils.createLong(pageRequestContext.getRequest().getParameter("template"));
    
    // If no template has been specified, use a dummy instance for creating a new template
    
    WorklistItemTemplate template;
    if (templateId == null) {
      template = new WorklistItemTemplate();
      template.setTemplateType(WorklistItemTemplateType.DEFAULT);
      template.setFactor(1D);
    }
    else {
      template = worklistItemTemplateDAO.findById(templateId);
    }
    Set<WorklistItemEditableFields> fields = template.getEditableFields();
    if (fields == null) {
      fields = new HashSet<>();
    }
    pageRequestContext.getRequest().setAttribute("worklistTemplate", template);
    pageRequestContext.getRequest().setAttribute("dateEditable", fields.contains(WorklistItemEditableFields.ENTRYDATE));
    pageRequestContext.getRequest().setAttribute("descriptionEditable", fields.contains(WorklistItemEditableFields.DESCRIPTION));
    pageRequestContext.getRequest().setAttribute("priceEditable", fields.contains(WorklistItemEditableFields.PRICE));
    pageRequestContext.getRequest().setAttribute("factorEditable", fields.contains(WorklistItemEditableFields.FACTOR));
    pageRequestContext.getRequest().setAttribute("billingNumberEditable", fields.contains(WorklistItemEditableFields.BILLING_NUMBER));

    pageRequestContext.setIncludeJSP("/templates/worklist/editworklisttemplate.jsp");
  }

  /**
   * Returns the roles allowed to access this page.
   * 
   * @return The roles allowed to access this page
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

  /**
   * Returns the localized name of this page. Used e.g. for breadcrumb navigation.
   * 
   * @param locale The locale to be used for the name
   * 
   * @return The localized name of this page
   */
  public String getName(Locale locale) {
    return Messages.getInstance().getText(locale, "worklist.editWorklistTemplate.pageTitle");
  }

}
