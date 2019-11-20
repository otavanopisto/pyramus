package fi.otavanopisto.pyramus.security.impl;

import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.security.ContextReference;

@Stateful
@LocalSession
@SessionScoped
public class SessionControllerImpl implements SessionController {
  
  @Inject
  private Permissions permissions;
  
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
    return permissions.hasPermission(getUser(), permissionName, contextReference);
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
  public boolean hasEnvironmentPermission(String permission) {
    return hasPermission(permission, null);
  }
}
