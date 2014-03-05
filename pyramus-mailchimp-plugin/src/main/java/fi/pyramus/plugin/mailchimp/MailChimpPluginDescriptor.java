package fi.pyramus.plugin.mailchimp;

import java.util.Map;

import fi.pyramus.plugin.PluginDescriptor;

public class MailChimpPluginDescriptor implements PluginDescriptor {
  
  public Map<String, Class<?>> getBinaryRequestControllers() {
    return null;
  }

  public Map<String, Class<?>> getJSONRequestControllers() {
    return null;
  }
  
  public String getName() {
    return "MailChimp";
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
}
