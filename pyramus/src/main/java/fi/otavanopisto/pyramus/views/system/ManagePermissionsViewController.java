package fi.otavanopisto.pyramus.views.system;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.PersistenceException;

import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.security.EnvironmentRolePermissionDAO;
import fi.otavanopisto.pyramus.dao.security.PermissionDAO;
import fi.otavanopisto.pyramus.domainmodel.security.EnvironmentRolePermission;
import fi.otavanopisto.pyramus.domainmodel.security.Permission;
import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.framework.PyramusFormViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.security.impl.PermissionCollector;

public class ManagePermissionsViewController extends PyramusFormViewController {

  @Override
  public void processForm(PageRequestContext requestContext) {
    PermissionDAO permissionDAO = DAOFactory.getInstance().getPermissionDAO();
    EnvironmentRolePermissionDAO environmentRolePermissionDAO = DAOFactory.getInstance().getEnvironmentRolePermissionDAO();
    
    List<Permission> permissions = permissionDAO.listAll();
    
    Collections.sort(permissions, new Comparator<Permission>() {
      @Override
      public int compare(Permission o1, Permission o2) {
        return o1.getName().compareTo(o2.getName());
      }
    });

    List<EnvironmentRolePermission> rolePermissions = environmentRolePermissionDAO.listAll();
    Map<String, Boolean> rpMap = new HashMap<>();
    
    for (EnvironmentRolePermission erp : rolePermissions) {
      String key = erp.getPermission().getId().toString() + '.' + erp.getRole().name();
      rpMap.put(key, Boolean.TRUE);
    }
    
    requestContext.getRequest().setAttribute("roles", manageableRoles());
    requestContext.getRequest().setAttribute("permissions", permissions);
    requestContext.getRequest().setAttribute("rolePermissions", rpMap);
    requestContext.setIncludeJSP("/templates/system/managepermissions.jsp");
  }

  @Override
  public void processSend(PageRequestContext requestContext) {
    String resetRole = requestContext.getString("roleReset");
    if (StringUtils.isBlank(resetRole)) {
      PermissionDAO permissionDAO = DAOFactory.getInstance().getPermissionDAO();
      EnvironmentRolePermissionDAO environmentRolePermissionDAO = DAOFactory.getInstance().getEnvironmentRolePermissionDAO();
      
      List<Permission> permissions = permissionDAO.listAll();
  
      for (Permission permission : permissions) {
        for (Role role : manageableRoles()) {
          
          String paramName = permission.getId().toString() + '.' + role.name();
          
          EnvironmentRolePermission rolePermission = environmentRolePermissionDAO.findByUserRoleAndPermission(role, permission);
          
          boolean isSet = Integer.valueOf(1).equals(requestContext.getInteger(paramName));
          boolean exists = rolePermission != null;
          
          if (isSet != exists) {
            if (isSet) {
              environmentRolePermissionDAO.create(role, permission);
            } else {
              environmentRolePermissionDAO.delete(rolePermission);
            }
          }
        }
      }
    }
    else {
      resetRoles(Role.valueOf(resetRole));
    }
    
    processForm(requestContext);
  }

  private Role[] manageableRoles() {
    return Role.values();
  }
  
  private void resetRoles(Role role) {
    try {
      PermissionCollector permissionCollector = (PermissionCollector) findByClass(PermissionCollector.class);
      if (permissionCollector != null) {
        permissionCollector.resetRoles(role);
      }
    }
    catch (Exception e) {
      Logger.getLogger(getClass().getName()).log(Level.SEVERE, String.format("Reset permissions of role %s failed", role.name()), e);
    }
  }

  private String getAppName() throws NamingException {
    String appName = "";
    try {
      String jndiName = "java:app/AppName";
      appName = (String) new InitialContext().lookup(jndiName);
    } catch (Throwable t) {
    }
    
    if (StringUtils.isBlank(appName))
      appName = "Pyramus";
    
    return appName;
  }
  
  private Object findByClass(Class<?> cls) {
    try {
      String jndiName = "java:app/" + getAppName() + "/" + cls.getSimpleName();
      return new InitialContext().lookup(jndiName);
    } catch (NamingException e) {
      throw new PersistenceException(e);
    }
  }

  @Override
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR };
  }
}
