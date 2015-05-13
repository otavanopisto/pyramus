package fi.pyramus.views.system;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.clientapplications.ClientApplicationAccessTokenDAO;
import fi.pyramus.dao.clientapplications.ClientApplicationAuthorizationCodeDAO;
import fi.pyramus.dao.clientapplications.ClientApplicationDAO;
import fi.pyramus.dao.security.EnvironmentRolePermissionDAO;
import fi.pyramus.dao.security.PermissionDAO;
import fi.pyramus.domainmodel.clientapplications.ClientApplication;
import fi.pyramus.domainmodel.clientapplications.ClientApplicationAccessToken;
import fi.pyramus.domainmodel.clientapplications.ClientApplicationAuthorizationCode;
import fi.pyramus.domainmodel.security.EnvironmentRolePermission;
import fi.pyramus.domainmodel.security.Permission;
import fi.pyramus.domainmodel.users.Role;
import fi.pyramus.framework.PyramusFormViewController;
import fi.pyramus.framework.UserRole;
import fi.pyramus.util.OauthClientSecretGenerator;

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
    
    requestContext.getRequest().setAttribute("roles", Role.values());
    requestContext.getRequest().setAttribute("permissions", permissions);
    requestContext.getRequest().setAttribute("rolePermissions", rpMap);
    requestContext.setIncludeJSP("/templates/system/managepermissions.jsp");
  }

  @Override
  public void processSend(PageRequestContext requestContext) {
    PermissionDAO permissionDAO = DAOFactory.getInstance().getPermissionDAO();
    EnvironmentRolePermissionDAO environmentRolePermissionDAO = DAOFactory.getInstance().getEnvironmentRolePermissionDAO();
    
    List<Permission> permissions = permissionDAO.listAll();

    for (Permission permission : permissions) {
      for (Role role : Role.values()) {
        String paramName = permission.getId().toString() + '.' + role.name();
        
        EnvironmentRolePermission rolePermission = environmentRolePermissionDAO.findByUserRoleAndPermission(role, permission);
        
        boolean isSet = new Integer(1).equals(requestContext.getInteger(paramName));
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
    
    processForm(requestContext);
  }

  @Override
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR };
  }
}
