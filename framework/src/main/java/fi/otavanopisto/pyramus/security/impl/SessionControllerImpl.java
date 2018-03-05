package fi.otavanopisto.pyramus.security.impl;

import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.security.PermissionDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.security.Permission;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.security.ContextReference;

@Stateful
@LocalSession
@SessionScoped
public class SessionControllerImpl implements SessionController {
  
  @Inject
  private PermissionDAO permissionDAO;
  
  @Override
  public void logout() {
    loggedUserId = null;
  }

  @Override
  public Locale getLocale() {
    if (locale != null)
      return locale;
    else
      return FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
  }

  @Override
  public void setLocale(Locale locale) {
    this.locale = locale;
  }

  public boolean isLoggedIn() {
    return loggedUserId != null;
  }

  @PostConstruct
  private void init() {
    loggedUserId = null;
  }

  @Override
  public boolean hasPermission(String permissionName, ContextReference contextReference) {
    Permission permission = permissionDAO.findByName(permissionName);
    PermissionResolver permissionResolver = getPermissionResolver(permission);
    
    if (isLoggedIn()) {
      return isSuperuser() || permissionResolver.hasPermission(permission, contextReference, getUser());
    } else {
      return permissionResolver.hasEveryonePermission(permission, contextReference);
    }
  }
  
  @Inject
  @Any
  private Instance<PermissionResolver> permissionResolvers;
  
  private PermissionResolver getPermissionResolver(Permission permission) {
    for (PermissionResolver resolver : permissionResolvers) {
      if (resolver.handlesPermission(permission))
        return resolver;
    }
    
    return null;
  }
  
  private Locale locale;

  private Long loggedUserId;

  @Override
  public User getUser() {
    if (isLoggedIn()) {
      StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffMemberDAO();
      
      return userDAO.findById(loggedUserId);
    } else
      return null;
  }

  @Override
  public boolean isSuperuser() {
    return false;
  }

  @Override
  public boolean hasEnvironmentPermission(String permission) {
    return hasPermission(permission, null);
  }
}
