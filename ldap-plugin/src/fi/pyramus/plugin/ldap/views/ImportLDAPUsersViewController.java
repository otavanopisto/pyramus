package fi.pyramus.plugin.ldap.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.novell.ldap.LDAPAttribute;
import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPEntry;
import com.novell.ldap.LDAPException;
import com.novell.ldap.LDAPSearchResults;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.EmailDAO;
import fi.pyramus.dao.users.UserDAO;
import fi.pyramus.domainmodel.users.Role;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.framework.PyramusFormViewController;
import fi.pyramus.framework.UserRole;
import fi.pyramus.plugin.ldap.LDAPUtils;

public class ImportLDAPUsersViewController extends PyramusFormViewController {

  public void processForm(PageRequestContext requestContext) {
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();

    List<Map<String, String>> result = new ArrayList<Map<String, String>>();
    
    LDAPSearchResults searchResults;
    try {
      List<String> attributes = new ArrayList<String>();

      String usernameAttr = System.getProperty("authentication.ldap.usernameAttr");
      String emailAttr = System.getProperty("authentication.ldap.emailAttr");
      String firstnameAttr = System.getProperty("authentication.ldap.firstnameAttr");
      String lastnameAttr = System.getProperty("authentication.ldap.lastnameAttr");
      String defaultEmailDomain = System.getProperty("authentication.ldap.defaultEmailDomain");
      String uniqueIdAttr = System.getProperty("authentication.ldap.uniqueIdAttr");
      boolean idEncoded = "1".equals(System.getProperty("authentication.ldap.uniqueIdEncoded"));

      attributes.add(uniqueIdAttr);
      attributes.add(usernameAttr);
      attributes.add(firstnameAttr);
      attributes.add(lastnameAttr);
      if (!StringUtils.isBlank(emailAttr))
        attributes.add(emailAttr);
      
      LDAPConnection connection = LDAPUtils.getLDAPConnection();
      searchResults = connection.search(System.getProperty("authentication.ldap.authdn"), LDAPConnection.SCOPE_SUB, System.getProperty("authentication.ldap.personFilter").trim(), attributes.toArray(new String[0]), false);
      while (searchResults != null && searchResults.hasMore()) {
        LDAPEntry entry = searchResults.next();
        LDAPAttribute attr;
        String username = entry.getAttribute(usernameAttr).getStringValue();
        
        attr = entry.getAttribute(uniqueIdAttr);
        String id = idEncoded ? LDAPUtils.getAttributeBinaryValue(attr) : attr.getStringValue();
        boolean existsOnDB = userDAO.findByExternalIdAndAuthProvider(id, "LDAP") != null;
          
        if (!existsOnDB) {
          Map<String, String> info = new HashMap<String, String>();
          
          attr = entry.getAttribute(firstnameAttr);
          String firstName = attr != null ? attr.getStringValue() : "";
          attr = entry.getAttribute(lastnameAttr);
          String lastName = attr != null ? attr.getStringValue() : "";
          
          String email = ""; 
          if (!StringUtils.isBlank(emailAttr)) {
            attr = entry.getAttribute(emailAttr);
            email = attr != null ? attr.getStringValue() : "";;
          }
            
          if (StringUtils.isBlank(email) && !StringUtils.isBlank(firstName) && !StringUtils.isBlank(lastName))
            email = username.toLowerCase() + '@' + defaultEmailDomain;
          
          info.put("username", username);
          info.put("firstName", firstName);
          info.put("lastName", lastName);
          info.put("email", email);
          info.put("id", id);
          
          result.add(info);
        }
      }
    } catch (LDAPException e) {
      throw new SmvcRuntimeException(e);
    }
    
    requestContext.getRequest().setAttribute("users", result);
    requestContext.setIncludeFtl("/plugin/ldap/ftl/importldapusers.ftl");
  }
  
  public void processSend(PageRequestContext requestContext) {
    EmailDAO emailDAO = DAOFactory.getInstance().getEmailDAO();
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();

    List<User> createdUsers = new ArrayList<User>();
    
    int rowCount = requestContext.getInteger("importTable.rowCount");
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "importTable." + i;
      if ("1".equals(requestContext.getString(colPrefix + ".import"))) {
        String email = requestContext.getString(colPrefix + ".email");
        String firstName = requestContext.getString(colPrefix + ".firstName");
        String lastName = requestContext.getString(colPrefix + ".lastName");
        String roleName = requestContext.getString(colPrefix + ".role");
        String id = requestContext.getString(colPrefix + ".id");
        Role role = Enum.valueOf(Role.class, roleName);
        User user = userDAO.create(firstName, lastName, id, "LDAP", role);
        emailDAO.create(user.getContactInfo(), null, Boolean.TRUE, email);
        createdUsers.add(user);
      }
    }
 
    requestContext.getRequest().setAttribute("createdUsers", createdUsers);
    
    requestContext.setRedirectURL(requestContext.getRequest().getContextPath() + "system/importldapusers.page");
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR };
  }

}
