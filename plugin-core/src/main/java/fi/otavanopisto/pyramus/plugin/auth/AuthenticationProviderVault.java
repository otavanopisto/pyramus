package fi.otavanopisto.pyramus.plugin.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.otavanopisto.pyramus.plugin.PluginDescriptor;
import fi.otavanopisto.pyramus.plugin.PluginManager;

/**
 * The class responsible of managing the authentication providers of the application.
 */
public class AuthenticationProviderVault {
  
  /**
   * Returns a singleton instance of this class.
   * 
   * @return A singleton instance of this class
   */
  public static AuthenticationProviderVault getInstance() {
    return instance;
  }
  
 
  /**
   * Returns a collection of all authentication providers registered to this class.
   * 
   * @return A collection of all authentication providers registered to this class
   */
  public Collection<AuthenticationProvider> getAuthenticationProviders() {
    return authenticationProviders.values();
  }
  
  public List<InternalAuthenticationProvider> getInternalAuthenticationProviders() {
    List<InternalAuthenticationProvider> internalAuthenticationProviders = new ArrayList<>();
    for (AuthenticationProvider authenticationProvider : getAuthenticationProviders()) {
      if (authenticationProvider instanceof InternalAuthenticationProvider)
        internalAuthenticationProviders.add((InternalAuthenticationProvider) authenticationProvider);
    }
    return internalAuthenticationProviders;
  }
  
  public List<ExternalAuthenticationProvider> getExternalAuthenticationProviders() {
    List<ExternalAuthenticationProvider> externalAuthenticationProviders = new ArrayList<>();
    for (AuthenticationProvider authenticationProvider : getAuthenticationProviders()) {
      if (authenticationProvider instanceof ExternalAuthenticationProvider)
        externalAuthenticationProviders.add((ExternalAuthenticationProvider) authenticationProvider);
    }
    
    return externalAuthenticationProviders;
  }
  
  public static Map<String, Class<AuthenticationProvider>> getAuthenticationProviderClasses() {
    return authenticationProviderClasses;
  }
  
  public boolean hasExternalStrategies() {
    return !getExternalAuthenticationProviders().isEmpty();
  }
  
  public boolean hasInternalStrategies() {
    return !getInternalAuthenticationProviders().isEmpty();
  }
  
  /**
   * Returns the authentication provider corresponding to the given name. If it doesn't exists, returns <code>null</code>.
   * 
   * @param name The authentication provider name
   * 
   * @return The authentication provider corresponding to the given name, or <code>null</code> if not found
   */
  public AuthenticationProvider getAuthenticationProvider(String name) {
    return authenticationProviders.get(name);
  }
  
  public ExternalAuthenticationProvider getExternalAuthenticationProvider(String name) {
    AuthenticationProvider authenticationProvider = authenticationProviders.get(name);
    return authenticationProvider instanceof ExternalAuthenticationProvider ? (ExternalAuthenticationProvider) authenticationProvider : null;
  }
  
  public InternalAuthenticationProvider getInternalAuthenticationProvider(String name) {
    AuthenticationProvider authenticationProvider = authenticationProviders.get(name);
    return authenticationProvider instanceof InternalAuthenticationProvider ? (InternalAuthenticationProvider) authenticationProvider : null;
  }
  
  /**
    Registers the various authentication providers to this class.
  **/
  public void initializeStrategies() {
    String strategiesConf = System.getProperty("authentication.enabledStrategies");
    if (strategiesConf == null || "".equals(strategiesConf))
      strategiesConf = "internal";
    
    int authSourceCount = 0;
    String[] strategies = strategiesConf.split(",");
    try {
      AuthenticationProvider provider;
      for (String strategyName : strategies) {
        Class<AuthenticationProvider> authProviderClass = authenticationProviderClasses.get(strategyName.trim());
        if (authProviderClass != null) {
          provider = authProviderClass.newInstance();
          registerAuthenticationProvider(provider);
          authSourceCount++;
        }
      }
      if (authSourceCount == 0) {
        Class<AuthenticationProvider> authProviderClass = authenticationProviderClasses.get("internal");
        provider = authProviderClass.newInstance();
        registerAuthenticationProvider(provider);
      }
    } catch (InstantiationException e) {
      throw new SmvcRuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new SmvcRuntimeException(e);
    }
  }
  
  /**
   * Registers an authentication provider to this class.
   * 
   * @param authenticationProvider The authentication provider to be registered
   */
  private void registerAuthenticationProvider(AuthenticationProvider authenticationProvider) {
    authenticationProviders.put(authenticationProvider.getName(), authenticationProvider);  
  }
  
  /** Map containing authentication provider names as keys and the providers themselves as values */ 
  private Map<String, AuthenticationProvider> authenticationProviders = new HashMap<>();
  
  @SuppressWarnings("unchecked")
  public static void registerAuthenticationProviderClass(String name, Class<?> class1) {
    authenticationProviderClasses.put(name, (Class<AuthenticationProvider>) class1);
  }
  
  /** The singleton instance of this class */
  private static AuthenticationProviderVault instance = new AuthenticationProviderVault();
  /** All registered authentication provider classes **/
  private static Map<String, Class<AuthenticationProvider>> authenticationProviderClasses = new HashMap<>();
  
  static {
    List<PluginDescriptor> plugins = PluginManager.getInstance().getPlugins();
    for (PluginDescriptor plugin : plugins) {
      if (plugin.getAuthenticationProviders() != null) {
        Map<String, Class<?>> authenticationProviders = plugin.getAuthenticationProviders();
        for (Map.Entry<String, Class<?>> entry : authenticationProviders.entrySet()) {
          registerAuthenticationProviderClass(entry.getKey(), entry.getValue());
        }
      }
    }
  }
}
