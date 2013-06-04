package fi.pyramus.plugin.ldap.hooks;

import fi.pyramus.plugin.PageHookContext;
import fi.pyramus.plugin.PageHookController;

public class ImportLDAPUsersMenuHook implements PageHookController {
  
  @Override
  public void execute(PageHookContext pageHookContext) {
    pageHookContext.setIncludeFtl("/plugin/ldap/ftl/importldapusersmenu.ftl");
  }
  
}
