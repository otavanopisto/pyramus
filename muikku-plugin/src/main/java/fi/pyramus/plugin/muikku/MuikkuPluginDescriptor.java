package fi.pyramus.plugin.muikku;

import java.util.HashMap;
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
    Map<String, Class<?>> result = new HashMap<>();
    
    result.put("courses.viewCourse.basic.hoverMenuLinks", ViewCourseBasicPageHookController.class);
    result.put("courses.editCourse.basic.hoverMenuLinks", EditCourseBasicPageHookController.class);
    
    return result;
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
      // TODO: We should return this only when the client is actually Muikku
      return "/plugin/muikku/muikkulogin.ftl";
    }
    
    return null;
  }
  
}
