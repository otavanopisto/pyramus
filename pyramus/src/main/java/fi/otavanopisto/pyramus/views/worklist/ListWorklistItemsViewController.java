package fi.otavanopisto.pyramus.views.worklist;

import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.breadcrumbs.Breadcrumbable;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.dao.worklist.WorklistItemTemplateDAO;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.domainmodel.worklist.WorklistItemTemplate;
import fi.otavanopisto.pyramus.domainmodel.worklist.WorklistItemTemplateType;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

/**
 * View controller to search for worklist items.
 */
public class ListWorklistItemsViewController extends PyramusViewController implements Breadcrumbable {
  
  /**
   * Returns roles that are allowed to use this resource.
   *  
   * @see fi.fi.otavanopisto.pyramus.framework.PyramusViewController#getAllowedRoles()
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

  /**
   * Processes the page request.
   * 
   * @param pageRequestContext Request context
   */
  public void process(PageRequestContext pageRequestContext) {
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    WorklistItemTemplateDAO worklistItemTemplateDAO = DAOFactory.getInstance().getWorklistItemTemplateDAO(); 
    User loggedUser = staffMemberDAO.findById(pageRequestContext.getLoggedUserId());
    List<StaffMember> staffMembers = staffMemberDAO.listByOrganizationAndArchived(loggedUser.getOrganization(), Boolean.FALSE);
    Collections.sort(staffMembers, Comparator.comparing(StaffMember::getLastName).thenComparing(StaffMember::getFirstName));
    pageRequestContext.getRequest().setAttribute("staffMembers", staffMembers);
    List<WorklistItemTemplate> worklistTemplates = worklistItemTemplateDAO.listByTemplateTypesAndArchived(EnumSet.of(WorklistItemTemplateType.DEFAULT), false);
    Collections.sort(worklistTemplates, Comparator.comparing(WorklistItemTemplate::getDescription));
    pageRequestContext.getRequest().setAttribute("worklistTemplates", worklistTemplates);
    pageRequestContext.setIncludeJSP("/templates/worklist/listworklistitems.jsp");
  }

  /**
   * Returns the localized name of this page. Used e.g. for breadcrumb navigation.
   * 
   * @param locale The locale to be used for the name
   * 
   * @return The localized name of this page
   */
  public String getName(Locale locale) {
    return Messages.getInstance().getText(locale, "worklist.listWorklistItems.pageTitle");
  }

}
