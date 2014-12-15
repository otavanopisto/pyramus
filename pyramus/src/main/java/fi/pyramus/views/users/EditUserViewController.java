package fi.pyramus.views.users;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.I18N.Messages;
import fi.pyramus.breadcrumbs.Breadcrumbable;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.ContactTypeDAO;
import fi.pyramus.dao.base.ContactURLTypeDAO;
import fi.pyramus.dao.users.StaffMemberDAO;
import fi.pyramus.dao.users.UserVariableDAO;
import fi.pyramus.dao.users.UserVariableKeyDAO;
import fi.pyramus.domainmodel.base.ContactType;
import fi.pyramus.domainmodel.base.ContactURLType;
import fi.pyramus.domainmodel.base.Tag;
import fi.pyramus.domainmodel.users.Role;
import fi.pyramus.domainmodel.users.StaffMember;
import fi.pyramus.domainmodel.users.UserVariable;
import fi.pyramus.domainmodel.users.UserVariableKey;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;
import fi.pyramus.plugin.auth.AuthenticationProvider;
import fi.pyramus.plugin.auth.AuthenticationProviderVault;
import fi.pyramus.plugin.auth.InternalAuthenticationProvider;
import fi.pyramus.util.StringAttributeComparator;

/**
 * The controller responsible of the Edit User view of the application.
 * 
 * @see fi.pyramus.json.users.EditUserJSONRequestController
 */
public class EditUserViewController extends PyramusViewController implements Breadcrumbable {

  /**
   * Processes the page request by including the corresponding JSP page to the response. 
   * 
   * @param requestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    // TODO loggedUserRole vs. user role
    StaffMemberDAO staffDAO = DAOFactory.getInstance().getStaffMemberDAO();
    UserVariableDAO userVariableDAO = DAOFactory.getInstance().getUserVariableDAO();
    UserVariableKeyDAO variableKeyDAO = DAOFactory.getInstance().getUserVariableKeyDAO();
    ContactTypeDAO contactTypeDAO = DAOFactory.getInstance().getContactTypeDAO();
    ContactURLTypeDAO contactURLTypeDAO = DAOFactory.getInstance().getContactURLTypeDAO();

    StaffMember user = staffDAO.findById(pageRequestContext.getLong("userId"));
    String username = "";
    
    List<AuthenticationProviderInfoBean> authenticationProviders = new ArrayList<AuthenticationProviderInfoBean>();
    for (String authenticationProviderName : AuthenticationProviderVault.getAuthenticationProviderClasses().keySet()) {
      boolean active = AuthenticationProviderVault.getInstance().getAuthenticationProvider(authenticationProviderName) != null;
      boolean canUpdateCredentials;
      
      AuthenticationProvider authenticationProvider = AuthenticationProviderVault.getInstance().getAuthenticationProvider(authenticationProviderName);
      
      if (authenticationProvider instanceof InternalAuthenticationProvider) {
        InternalAuthenticationProvider internalAuthenticationProvider = (InternalAuthenticationProvider) authenticationProvider;
        canUpdateCredentials = internalAuthenticationProvider.canUpdateCredentials();

        //FIXME
        /*if (internalAuthenticationProvider.getName().equals(user.getAuthProvider())) {
          username = internalAuthenticationProvider.getUsername(user.getExternalId());
        }*/
      } else {
        canUpdateCredentials = false;
      }
      
      authenticationProviders.add(new AuthenticationProviderInfoBean(authenticationProviderName, active, canUpdateCredentials));
    }
    
    StringBuilder tagsBuilder = new StringBuilder();
    Iterator<Tag> tagIterator = user.getTags().iterator();
    while (tagIterator.hasNext()) {
      Tag tag = tagIterator.next();
      tagsBuilder.append(tag.getText());
      if (tagIterator.hasNext())
        tagsBuilder.append(' ');
    }

    List<ContactURLType> contactURLTypes = contactURLTypeDAO.listUnarchived();
    Collections.sort(contactURLTypes, new StringAttributeComparator("getName"));

    List<ContactType> contactTypes = contactTypeDAO.listUnarchived();
    Collections.sort(contactTypes, new StringAttributeComparator("getName"));

    List<UserVariableKey> userVariableKeys = variableKeyDAO.listUserEditableUserVariableKeys();
    Collections.sort(userVariableKeys, new StringAttributeComparator("getVariableName"));
    
    JSONArray variables = new JSONArray();
    for (UserVariableKey userVariableKey : userVariableKeys) {
      UserVariable userVariable = userVariableDAO.findByUserAndVariableKey(user, userVariableKey);
      JSONObject variable = new JSONObject();
      variable.put("type", userVariableKey.getVariableType());
      variable.put("name", userVariableKey.getVariableName());
      variable.put("key", userVariableKey.getVariableKey());
      variable.put("value", userVariable != null ? userVariable.getValue() : "");
      variables.add(variable);
    }
    
    setJsDataVariable(pageRequestContext, "variables", variables.toString());
    
    pageRequestContext.getRequest().setAttribute("tags", tagsBuilder.toString());
    pageRequestContext.getRequest().setAttribute("user", user);
    pageRequestContext.getRequest().setAttribute("username", username);
    pageRequestContext.getRequest().setAttribute("contactTypes", contactTypes);
    pageRequestContext.getRequest().setAttribute("contactURLTypes", contactURLTypes);
    pageRequestContext.getRequest().setAttribute("authenticationProviders", authenticationProviders);
    
    pageRequestContext.setIncludeJSP("/templates/users/edituser.jsp");
  }

  /**
   * Returns the roles allowed to access this page. Available for only those
   * with {@link Role#MANAGER} or {@link Role#ADMINISTRATOR} privileges.
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
    return Messages.getInstance().getText(locale, "users.editUser.pageTitle");
  }

}
