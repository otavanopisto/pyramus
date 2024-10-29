package fi.otavanopisto.pyramus.security.impl;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.dao.security.EnvironmentRolePermissionDAO;
import fi.otavanopisto.pyramus.dao.security.PermissionDAO;
import fi.otavanopisto.pyramus.domainmodel.security.EnvironmentRolePermission;
import fi.otavanopisto.pyramus.domainmodel.security.Permission;
import fi.otavanopisto.pyramus.domainmodel.users.Role;

@Singleton
@Startup
public class PermissionCollector {
  
  @Inject
  private Logger logger;
  
  @Inject
  @Any
  private Instance<PyramusPermissionCollection> permissionCollections;

  @Inject
  private PermissionDAO permissionDAO;
  
  @Inject
  private EnvironmentRolePermissionDAO environmentRolePermissionDAO;
  
  public void resetRoles(Role role) {
    // Remove current permissions
    List<EnvironmentRolePermission> currentPermissions = environmentRolePermissionDAO.listByUserRole(role);
    for (EnvironmentRolePermission currentPermission : currentPermissions) {
      environmentRolePermissionDAO.delete(currentPermission);
    }
    // Insert new permissions
    try {
      for (PyramusPermissionCollection collection : permissionCollections) {
        List<String> permissions = collection.listPermissions();
        for (String permissionName : permissions) {
          Permission permission = permissionDAO.findByName(permissionName);
          String permissionScope = collection.getPermissionScope(permissionName);
          if (PermissionScope.ENVIRONMENT.equals(permissionScope)) {
            String[] defaultRoles = collection.getDefaultRoles(permissionName);
            if (defaultRoles != null) {
              for (int i = 0; i < defaultRoles.length; i++) {
                String defaultRoleName = defaultRoles[i];
                Role defaultRole = Role.valueOf(defaultRoleName);
                if (defaultRole.equals(role)) {
                  environmentRolePermissionDAO.create(role, permission);
                }
              }
            }
          }
        }
      }    
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  @PostConstruct
  private void collectPermissions() {
    // Clean the permissions that are set to be reset
    List<Permission> permissionsSetToReset = permissionDAO.listPermissionsSetToReset();
    for (Permission permission : permissionsSetToReset) {
      logger.info(String.format("Reseting permission %s", permission.getName()));
      environmentRolePermissionDAO.deleteByPermission(permission);
      permissionDAO.delete(permission);
    }

    // Go through the collections and set up the permissions and environment role permissions
    for (PyramusPermissionCollection collection : permissionCollections) {
      List<String> permissions = collection.listPermissions();

      for (String permissionName : permissions) {
        Permission permission = permissionDAO.findByName(permissionName);
        
        if (permission == null) {
          try {
            String permissionScope = collection.getPermissionScope(permissionName);

            permission = permissionDAO.create(permissionName, permissionScope);
            
            String[] defaultRoles = collection.getDefaultRoles(permissionName);
            
            if (defaultRoles != null) {
              switch (permissionScope) {
                case PermissionScope.ENVIRONMENT:
                  for (int i = 0; i < defaultRoles.length; i++) {
                    String roleName = defaultRoles[i];
                    Role role = Role.valueOf(roleName);
                    
                    environmentRolePermissionDAO.create(role, permission);
                  }
                break;
                
//                case PermissionScope.WORKSPACE:
//                  List<WorkspaceEntity> workspaces = workspaceEntityDAO.listAll();
//                  WorkspaceSettingsTemplate workspaceSettingsTemplate = workspaceSettingsTemplateDAO.findById(1l); 
//                  
//                  for (int i = 0; i < defaultRoles.length; i++) {
//                    String roleName = defaultRoles[i];
//                    RoleEntity roleEntity = roleEntityDAO.findByName(roleName);
//
//                    workspaceSettingsTemplateRolePermissionDAO.create(workspaceSettingsTemplate, roleEntity, permission);
//
//                    // TODO Workspace creation & templates - is this necessary and bulletproof?
//                    for (WorkspaceEntity workspace: workspaces) {
//                      workspaceRolePermissionDAO.create(workspace, roleEntity, permission);
//                    }
//                  }
//                break;
//                
//                case PermissionScope.USERGROUP:
//                  List<UserGroup> userGroups = userGroupDAO.listAll();
//                  
//                  for (int i = 0; i < defaultRoles.length; i++) {
//                    String roleName = defaultRoles[i];
//                    RoleEntity roleEntity = roleEntityDAO.findByName(roleName);
//
//                    // TODO Workspace creation & templates - is this necessary and bulletproof?
//                    for (UserGroup userGroup: userGroups) {
//                      userGroupRolePermissionDAO.create(userGroup, roleEntity, permission);
//                    }
//                  }
//                break;
              }
            }
            
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    }
  }
  
}
