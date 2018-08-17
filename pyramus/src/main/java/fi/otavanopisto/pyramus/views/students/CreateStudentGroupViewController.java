package fi.otavanopisto.pyramus.views.students;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.breadcrumbs.Breadcrumbable;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.OrganizationDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Organization;
import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.framework.UserUtils;
import fi.otavanopisto.pyramus.util.StringAttributeComparator;

/**
 * The controller responsible of the Create StudentGroup view of the application.
 * 
 * @see fi.otavanopisto.pyramus.json.students.CreateStudentGroupJSONRequestController
 */
public class CreateStudentGroupViewController extends PyramusViewController implements Breadcrumbable {

  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    OrganizationDAO organizationDAO = DAOFactory.getInstance().getOrganizationDAO();
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    
    Long loggedUserId = pageRequestContext.getLoggedUserId();
    StaffMember user = staffMemberDAO.findById(loggedUserId);
    
    List<Organization> organizations;
    if (UserUtils.canAccessAllOrganizations(user)) {
      organizations = organizationDAO.listUnarchived();
    } else {
      organizations = Arrays.asList(user.getOrganization());
    }
    
    Collections.sort(organizations, new StringAttributeComparator("getName"));
    pageRequestContext.getRequest().setAttribute("organizations", organizations);

    pageRequestContext.setIncludeJSP("/templates/students/createstudentgroup.jsp");
  }

  /**
   * Returns the roles allowed to access this page. Creating student groups is available for users with
   * {@link Role#MANAGER} or {@link Role#ADMINISTRATOR} privileges.
   * 
   * @return The roles allowed to access this page
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

  /**
   * Returns the localized name of this page. Used e.g. for breadcrumb navigation.
   * 
   * @param locale The locale to be used for the name
   * 
   * @return The localized name of this page
   */
  public String getName(Locale locale) {
    return Messages.getInstance().getText(locale, "students.createStudentGroup.pageTitle");
  }

}
