package fi.otavanopisto.pyramus.views.system;

import java.util.List;
import java.util.logging.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.security.EnvironmentRolePermissionDAO;
import fi.otavanopisto.pyramus.dao.security.PermissionDAO;
import fi.otavanopisto.pyramus.domainmodel.security.EnvironmentRolePermission;
import fi.otavanopisto.pyramus.domainmodel.security.Permission;
import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.framework.PyramusFormViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class ImportExportPermissionsViewController extends PyramusFormViewController {

  @Override
  public void processForm(PageRequestContext requestContext) {
    EnvironmentRolePermissionDAO environmentRolePermissionDAO = DAOFactory.getInstance().getEnvironmentRolePermissionDAO();
    List<EnvironmentRolePermission> rolePermissions = environmentRolePermissionDAO.listAll();
    JSONObject permissionMap = new JSONObject();
    for (EnvironmentRolePermission erp : rolePermissions) {
      int key = erp.getRole().getValue();
      if (permissionMap.containsKey(String.valueOf(key))) {
        permissionMap.getJSONArray(String.valueOf(key)).add(erp.getPermission().getName());
      } else {
        permissionMap.put(String.valueOf(key), new JSONArray());
        permissionMap.getJSONArray(String.valueOf(key)).add(erp.getPermission().getName());
      }
    }
    String permissionJson = permissionMap.toString(4);
    requestContext.getRequest().setAttribute("permissionJson", permissionJson);

    requestContext.setIncludeJSP("/templates/system/importexportpermissions.jsp");
  }

  @Override
  public void processSend(PageRequestContext requestContext) {

    String permissionJson = requestContext.getString("permissionJson");

    JSONObject permissionMap = JSONObject.fromObject(permissionJson);
    PermissionDAO permissionDAO = DAOFactory.getInstance().getPermissionDAO();
    EnvironmentRolePermissionDAO environmentRolePermissionDAO = DAOFactory.getInstance().getEnvironmentRolePermissionDAO();
    List<EnvironmentRolePermission> allPermissions = environmentRolePermissionDAO.listAll();
    for (EnvironmentRolePermission erp : allPermissions) {
      environmentRolePermissionDAO.delete(erp);
    }
    for (Object roleObject : permissionMap.keySet()) {
      String roleValue = (String) roleObject;
      Role role = Role.getRole(Integer.valueOf(roleValue));
      if(role == null){
        Logger.getLogger(getClass().getName()).severe(String.format("Role with value %s not found from system", roleValue));
        continue;
      }
      for (Object permissionNameObject : permissionMap.getJSONArray(roleValue)) {
        String permissionName = (String) permissionNameObject;
        Permission permission = permissionDAO.findByName(permissionName);
        if (permission == null) {
          Logger.getLogger(getClass().getName()).severe(String.format("Permission %s not found from system", permissionName));
          continue;
        }
        environmentRolePermissionDAO.create(role, permission);
      }
    }
    
    requestContext.setRedirectURL("/system/managepermissions.page");
  }

  @Override
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR };
  }

}
