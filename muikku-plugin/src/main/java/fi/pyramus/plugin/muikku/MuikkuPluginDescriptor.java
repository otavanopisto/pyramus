package fi.pyramus.plugin.muikku;

import java.util.Map;

import fi.pyramus.plugin.CustomLoginScreenPlugin;
import fi.pyramus.plugin.PluginDescriptor;

public class MuikkuPluginDescriptor implements PluginDescriptor, CustomLoginScreenPlugin {
  
  public Map<String, Class<?>> getBinaryRequestControllers() {
    return null;
  }

  public Map<String, Class<?>> getJSONRequestControllers() {
    return null;
  }
  
  public String getName() {
    return "Muikku";
  }
  
  public Map<String, Class<?>> getPageHookControllers() {
    return null;
  }
  
  public Map<String, Class<?>> getPageRequestControllers() {
    return null;
  }
  
  public Map<String, Class<?>> getAuthenticationProviders() {
    return null;
  }
  
  public String getMessagesBundlePath() {
    return null;
  }
  
  @Override
  public String getContextLoginFtl(String contextType, String contextId) {
    if ("OAUTHCLIENT".equals(contextType)) {
      return "/plugin/muikku/muikkulogin.ftl";
    }
    
    return null;
  }
  
}
