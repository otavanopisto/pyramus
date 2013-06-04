package fi.pyramus.plugin.ldap;

import java.util.HashMap;
import java.util.Map;

import fi.pyramus.plugin.PluginDescriptor;
import fi.pyramus.plugin.ldap.hooks.ImportLDAPUsersMenuHook;
import fi.pyramus.plugin.ldap.views.ImportLDAPUsersViewController;

public class LDAPPluginDescriptor implements PluginDescriptor {
  
  @Override
  public Map<String, Class<?>> getBinaryRequestControllers() {
    return null;
  }
  
  @Override
  public Map<String, Class<?>> getJSONRequestControllers() {
    return null;
  }
  
  @Override
  public String getName() {
    return "LDAP";
  }
  
  @Override
  public Map<String, Class<?>> getPageHookControllers() {
    Map<String, Class<?>> viewControllers = new HashMap<String, Class<?>>();
    
    viewControllers.put("generic.navigation.systemMenu", ImportLDAPUsersMenuHook.class);

    return viewControllers;
  }
  
  @Override
  public Map<String, Class<?>> getPageRequestControllers() {
    Map<String, Class<?>> viewControllers = new HashMap<String, Class<?>>();
    
    viewControllers.put("system/importldapusers", ImportLDAPUsersViewController.class);
    
    return viewControllers;
  }
  
  public Map<String, Class<?>> getAuthenticationProviders() {
    Map<String, Class<?>> authenticationProviders = new HashMap<String, Class<?>>();
    
    authenticationProviders.put("LDAP", LDAPAuthorizationStrategy.class);
    
    return authenticationProviders;
  }
  
  @Override
  public String getMessagesBundlePath() {
    return "fi.pyramus.plugin.ldap.messages";
  }
}
