package fi.otavanopisto.pyramus.security.impl;

import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.dao.security.PermissionDAO;
import fi.otavanopisto.pyramus.domainmodel.security.Permission;
import fi.otavanopisto.security.ContextReference;

@Stateful
@SessionScoped
public abstract class AbstractSessionControllerImpl implements SessionController {
  
  @Inject
  private PermissionDAO permissionDAO;
  
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

  @PostConstruct
  private void init() {
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

  @Override
  public boolean isSuperuser() {
    return false;
  }

  @Override
  public boolean hasEnvironmentPermission(String permission) {
    return hasPermission(permission, null);
  }
}
