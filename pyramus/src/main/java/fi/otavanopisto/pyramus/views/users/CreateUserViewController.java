package fi.otavanopisto.pyramus.views.users;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.breadcrumbs.Breadcrumbable;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.ContactTypeDAO;
import fi.otavanopisto.pyramus.dao.base.ContactURLTypeDAO;
import fi.otavanopisto.pyramus.dao.base.OrganizationDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.domainmodel.base.ContactType;
import fi.otavanopisto.pyramus.domainmodel.base.ContactURLType;
import fi.otavanopisto.pyramus.domainmodel.base.Organization;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.plugin.auth.AuthenticationProviderVault;
import fi.otavanopisto.pyramus.util.JSONArrayExtractor;
import fi.otavanopisto.pyramus.util.StringAttributeComparator;

/**
 * The controller responsible of the Create User view of the application.
 * 
 * @see fi.otavanopisto.pyramus.json.users.CreateUserJSONRequestController
 */
public class CreateUserViewController extends PyramusViewController implements Breadcrumbable {

  /**
   * Processes the page request by including the corresponding JSP page to the response. 
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    ContactTypeDAO contactTypeDAO = DAOFactory.getInstance().getContactTypeDAO();
    ContactURLTypeDAO contactURLTypeDAO = DAOFactory.getInstance().getContactURLTypeDAO();
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    OrganizationDAO organizationDAO = DAOFactory.getInstance().getOrganizationDAO();
    
    Long studentId = pageRequestContext.getLong("studentId");
    boolean hasInternalAuthenticationStrategies = AuthenticationProviderVault.getInstance().hasInternalStrategies();
    
    if (studentId != null) {
      Student student = studentDAO.findById(studentId);
      
      pageRequestContext.getRequest().setAttribute("person", student.getPerson());
      pageRequestContext.getRequest().setAttribute("student", student);

      String emails = new JSONArrayExtractor("defaultAddress", "contactType", "address").extractString(student.getContactInfo().getEmails());
      String addresses = new JSONArrayExtractor("defaultAddress", "name", "contactType", "streetAddress", "postalCode", "city", "country").extractString(student.getContactInfo().getAddresses());
      String phones = new JSONArrayExtractor("defaultNumber", "contactType", "number").extractString(student.getContactInfo().getPhoneNumbers());

      setJsDataVariable(pageRequestContext, "createuser_emails", emails);
      setJsDataVariable(pageRequestContext, "createuser_addresses", addresses);
      setJsDataVariable(pageRequestContext, "createuser_phones", phones);
    }

    List<ContactURLType> contactURLTypes = contactURLTypeDAO.listUnarchived();
    Collections.sort(contactURLTypes, new StringAttributeComparator("getName"));
    
    List<ContactType> contactTypes = contactTypeDAO.listUnarchived();
    Collections.sort(contactTypes, new StringAttributeComparator("getName"));

    List<Organization> organizations = organizationDAO.listUnarchived();
    Collections.sort(organizations, new StringAttributeComparator("getName"));

    pageRequestContext.getRequest().setAttribute("contactTypes", contactTypes);
    pageRequestContext.getRequest().setAttribute("contactURLTypes", contactURLTypes);
    pageRequestContext.getRequest().setAttribute("hasInternalAuthenticationStrategies", hasInternalAuthenticationStrategies);
    pageRequestContext.getRequest().setAttribute("organizations", organizations);

    pageRequestContext.setIncludeJSP("/templates/users/createuser.jsp");
  }

  /**
   * Returns the roles allowed to access this page. Creating new users requires
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
    return Messages.getInstance().getText(locale, "users.createUser.pageTitle");
  }

}
