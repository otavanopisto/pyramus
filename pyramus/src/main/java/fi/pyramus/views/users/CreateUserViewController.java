package fi.pyramus.views.users;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.I18N.Messages;
import fi.pyramus.breadcrumbs.Breadcrumbable;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.ContactTypeDAO;
import fi.pyramus.dao.base.ContactURLTypeDAO;
import fi.pyramus.dao.students.StudentDAO;
import fi.pyramus.domainmodel.base.ContactType;
import fi.pyramus.domainmodel.base.ContactURLType;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.domainmodel.users.Role;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;
import fi.pyramus.plugin.auth.AuthenticationProvider;
import fi.pyramus.plugin.auth.AuthenticationProviderVault;
import fi.pyramus.plugin.auth.InternalAuthenticationProvider;
import fi.pyramus.util.JSONArrayExtractor;
import fi.pyramus.util.StringAttributeComparator;

/**
 * The controller responsible of the Create User view of the application.
 * 
 * @see fi.pyramus.json.users.CreateUserJSONRequestController
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
    
    Long studentId = pageRequestContext.getLong("studentId");
    
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
    
    List<AuthenticationProviderInfoBean> authenticationProviders = new ArrayList<AuthenticationProviderInfoBean>();
    for (String authenticationProviderName : AuthenticationProviderVault.getAuthenticationProviderClasses().keySet()) {
      boolean active = AuthenticationProviderVault.getInstance().getAuthenticationProvider(authenticationProviderName) != null;
      boolean canUpdateCredentials;
      
      AuthenticationProvider authenticationProvider = AuthenticationProviderVault.getInstance().getAuthenticationProvider(authenticationProviderName);
      
      if (authenticationProvider instanceof InternalAuthenticationProvider) {
        InternalAuthenticationProvider internalAuthenticationProvider = (InternalAuthenticationProvider) authenticationProvider;
        canUpdateCredentials = internalAuthenticationProvider.canUpdateCredentials();
      } else {
        canUpdateCredentials = false;
      }
      
      authenticationProviders.add(new AuthenticationProviderInfoBean(authenticationProviderName, active, canUpdateCredentials));
    }

    List<ContactURLType> contactURLTypes = contactURLTypeDAO.listUnarchived();
    Collections.sort(contactURLTypes, new StringAttributeComparator("getName"));
    
    List<ContactType> contactTypes = contactTypeDAO.listUnarchived();
    Collections.sort(contactTypes, new StringAttributeComparator("getName"));

    pageRequestContext.getRequest().setAttribute("contactTypes", contactTypes);
    pageRequestContext.getRequest().setAttribute("contactURLTypes", contactURLTypes);
    pageRequestContext.getRequest().setAttribute("authenticationProviders", authenticationProviders);
    
    pageRequestContext.setIncludeJSP("/templates/users/createuser.jsp");
  }

  /**
   * Returns the roles allowed to access this page. Creating new users requires
   * {@link Role#MANAGER} or {@link Role#ADMINISTRATOR} privileges.
   * 
   * @return The roles allowed to access this page
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
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
