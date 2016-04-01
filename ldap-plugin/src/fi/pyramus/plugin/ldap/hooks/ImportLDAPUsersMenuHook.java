package fi.otavanopisto.pyramus.plugin.ldap.hooks;

import fi.otavanopisto.pyramus.plugin.PageHookContext;
import fi.otavanopisto.pyramus.plugin.PageHookController;

public class ImportLDAPUsersMenuHook implements PageHookController {
  
  @Override
  public void execute(PageHookContext pageHookContext) {
    pageHookContext.setIncludeFtl("/plugin/ldap/ftl/importldapusersmenu.ftl");
  }
  
}
