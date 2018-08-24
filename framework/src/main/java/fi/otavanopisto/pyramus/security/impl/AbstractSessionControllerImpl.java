package fi.otavanopisto.pyramus.security.impl;

import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import fi.otavanopisto.security.ContextReference;

@Stateful
@SessionScoped
public abstract class AbstractSessionControllerImpl implements SessionController {
  
  @Inject
  private Permissions permissions;
  
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
    return permissions.hasPermission(getUser(), permissionName, contextReference);
  }
  
  private Locale locale;

  @Override
  public boolean hasEnvironmentPermission(String permission) {
    return hasPermission(permission, null);
  }
}
