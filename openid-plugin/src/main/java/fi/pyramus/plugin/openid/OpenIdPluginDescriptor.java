package fi.pyramus.plugin.openid;

import java.util.HashMap;
import java.util.Map;

import fi.pyramus.plugin.PluginDescriptor;

public class OpenIdPluginDescriptor implements PluginDescriptor {
  
  public Map<String, Class<?>> getBinaryRequestControllers() {
    return null;
  }

  public Map<String, Class<?>> getJSONRequestControllers() {
    return null;
  }
  
  public String getName() {
    return "OpenID";
  }
  
  public Map<String, Class<?>> getPageHookControllers() {
    return null;
  }
  
  public Map<String, Class<?>> getPageRequestControllers() {
    return null;
  }
  
  public Map<String, Class<?>> getAuthenticationProviders() {
    Map<String, Class<?>> authenticationProviders = new HashMap<String, Class<?>>();
    
    authenticationProviders.put("OpenID", OpenIDAuthorizationStrategy.class);
    
    return authenticationProviders;
  }
  
  public String getMessagesBundlePath() {
    return null;
  }
}
