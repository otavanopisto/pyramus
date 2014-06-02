package fi.pyramus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.annotation.Resource;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.transaction.UserTransaction;

import fi.internetix.smvc.controllers.RequestController;
import fi.internetix.smvc.controllers.RequestControllerMapper;
import fi.internetix.smvc.logging.Logging;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.MagicKeyDAO;
import fi.pyramus.dao.plugins.PluginDAO;
import fi.pyramus.dao.plugins.PluginRepositoryDAO;
import fi.pyramus.dao.system.SettingDAO;
import fi.pyramus.dao.system.SettingKeyDAO;
import fi.pyramus.domainmodel.base.MagicKey;
import fi.pyramus.domainmodel.base.MagicKeyScope;
import fi.pyramus.domainmodel.plugins.Plugin;
import fi.pyramus.domainmodel.plugins.PluginRepository;
import fi.pyramus.domainmodel.system.Setting;
import fi.pyramus.domainmodel.system.SettingKey;
import fi.pyramus.plugin.PluginDescriptor;
import fi.pyramus.plugin.PluginManager;
import fi.pyramus.plugin.auth.AuthenticationProviderVault;
import fi.pyramus.plugin.auth.internal.InternalAuthenticationStrategy;

/**
 * The application context listener responsible of initialization and finalization of the
 * application.
 */
public class PyramusServletContextListener implements ServletContextListener {

  /**
   * Called when the application shuts down.
   * 
   * @param ctx The servlet context event
   */
  public void contextDestroyed(ServletContextEvent ctx) {
    try {
      userTransaction.begin();
      
      MagicKeyDAO magicKeyDAO = DAOFactory.getInstance().getMagicKeyDAO();
      MagicKey magicKey = magicKeyDAO.findByApplicationScope();
      if (magicKey != null) {
        magicKeyDAO.delete(magicKey);
      }
      
      userTransaction.commit();
    } catch (Exception e) {
      try {
        userTransaction.rollback();
      } catch (Exception e1) {
        e1.printStackTrace();
      }
    }
  }

  /**
   * Called when the application starts. Sets up both Hibernate and the page and JSON mappers needed
   * to serve the client requests.
   * 
   * @param servletContextEvent The servlet context event
   */
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    try {
      userTransaction.begin();
      
      MagicKeyDAO magicKeyDAO = DAOFactory.getInstance().getMagicKeyDAO();
      String applicationMagicKey = UUID.randomUUID().toString();
      
      MagicKey magicKey = magicKeyDAO.findByApplicationScope();
      if (magicKey != null) {
        magicKeyDAO.updateName(magicKey, applicationMagicKey);
      } else {
        magicKeyDAO.create(applicationMagicKey, MagicKeyScope.APPLICATION);
      }
      
      Properties pageControllers = new Properties();
      Properties jsonControllers = new Properties();
      Properties binaryControllers = new Properties();

      ServletContext ctx = servletContextEvent.getServletContext();
      String webappPath = ctx.getRealPath("/");
      
      // Load the system settings into the system properties
      loadSystemSettings(System.getProperties());
      
      // Load default page mappings from properties file
      loadPropertiesFile(ctx, pageControllers, "pagemapping.properties");
      // Load default JSON mappings from properties file
      loadPropertiesFile(ctx, jsonControllers, "jsonmapping.properties");
      // Load default binary mappings from properties file
      loadPropertiesFile(ctx, binaryControllers, "binarymapping.properties");
      
      // Initialize the page mapper in order to serve page requests 
      RequestControllerMapper.mapControllers(pageControllers, ".page");

      // Initialize the JSON mapper in order to serve JSON requests 
      RequestControllerMapper.mapControllers(jsonControllers, ".json");

      // Initialize the binary mapper in order to serve binary requests 
      RequestControllerMapper.mapControllers(binaryControllers, ".binary");
      
      // Load plugins here so that plugins can override existing controllers
      loadPlugins();
      
      // Sets the application directory of the application, used primarily for initial data creation

      System.getProperties().setProperty("appdirectory", webappPath);
      
      // Register internal authentication provider 
      AuthenticationProviderVault.registerAuthenticationProviderClass("internal", InternalAuthenticationStrategy.class);
      
      // Initializes all configured authentication strategies
      AuthenticationProviderVault.getInstance().initializeStrategies();
      
      if ("development".equals(System.getProperties().getProperty("system.environment"))) {
        trustSelfSignedCerts();
      }
      
      userTransaction.commit();
    } catch (Exception e) {
      try {
        userTransaction.rollback();
      } catch (Exception e1) {
        e1.printStackTrace();
      }
      
      e.printStackTrace();
      throw new ExceptionInInitializerError(e);
    }
  }

  private void loadPropertiesFile(ServletContext servletContext, Properties properties, String filename)
      throws FileNotFoundException, IOException {
    String webappPath = servletContext.getRealPath("/");
    File settingsFile = new File(webappPath + "WEB-INF/" + filename);
    if (settingsFile.canRead()) {
      properties.load(new FileReader(settingsFile));
    }
  }
  
  private void loadSystemSettings(Properties properties) {
    SettingDAO settingDAO = DAOFactory.getInstance().getSettingDAO();
    SettingKeyDAO settingKeyDAO = DAOFactory.getInstance().getSettingKeyDAO();
    
    List<SettingKey> settingKeys = settingKeyDAO.listAll();
    for (SettingKey settingKey : settingKeys) {
      Setting setting = settingDAO.findByKey(settingKey);
      if (setting != null)
        properties.put(settingKey.getName(), setting.getValue());
    } 
  }
  
  @SuppressWarnings("unchecked")
  private void loadPlugins() {
    try {
      PluginRepositoryDAO pluginRepositoryDAO = DAOFactory.getInstance().getPluginRepositoryDAO();
      List<PluginRepository> pluginRepositories = pluginRepositoryDAO.listAll();
      
      PluginManager pluginManager = PluginManager.initialize(getClass().getClassLoader(), pluginRepositories);
      
      PluginDAO pluginDAO = DAOFactory.getInstance().getPluginDAO();
      List<Plugin> enabledPlugins = pluginDAO.listByEnabled(Boolean.TRUE);
      for (Plugin plugin : enabledPlugins) {
        try {
          pluginManager.loadPlugin(plugin.getGroupId(), plugin.getArtifactId(), plugin.getVersion());
        } catch (Exception e) {
          Logging.logException("Failed to load plugin: " + plugin.getGroupId() + "." + plugin.getArtifactId() + ":" + plugin.getVersion(), e);
        }
      }
      
      pluginManager.registerPlugins();
            
      // Load additional request mappings from plugins
      List<PluginDescriptor> plugins = pluginManager.getPlugins();
      for (PluginDescriptor plugin : plugins) {
        Map<String, Class<?>> pageRequestControllers = plugin.getPageRequestControllers();
        if (pageRequestControllers != null) {
          for (String key : pageRequestControllers.keySet()) {
            String className = pageRequestControllers.get(key).getName();
            Class<? extends RequestController> pageController = (Class<? extends RequestController>) Class.forName(className, false, pluginManager.getPluginsClassLoader());
            RequestController oldController = RequestControllerMapper.getRequestController(key + ".page");
            if (oldController != null) {
              // Save masked controllers for extending existing functionality by calling
              // the masked controller's .process()
              RequestControllerMapper.mapController(key, ".page.masked", oldController);
            }
            RequestControllerMapper.mapController(key, ".page", pageController.newInstance());
          }
        }
        
        Map<String, Class<?>> jsonRequestControllers = plugin.getJSONRequestControllers();
        if (jsonRequestControllers != null) {
          for (String key : jsonRequestControllers.keySet()) {
            String className = jsonRequestControllers.get(key).getName();
            Class<? extends RequestController> pageController = (Class<? extends RequestController>) Class.forName(className, false, pluginManager.getPluginsClassLoader());
            RequestController oldController = RequestControllerMapper.getRequestController(key + ".json");
            if (oldController != null) {
              // Save masked controllers for extending existing functionality by calling
              // the masked controller's .process()
              RequestControllerMapper.mapController(key, ".json.masked", oldController);
            }
            RequestControllerMapper.mapController(key, ".json", pageController.newInstance());
          }
        }
        
        Map<String, Class<?>> binaryRequestControllers = plugin.getBinaryRequestControllers();
        if (binaryRequestControllers != null) {
          for (String key : binaryRequestControllers.keySet()) {
            String className = binaryRequestControllers.get(key).getName();
            Class<? extends RequestController> pageController = (Class<? extends RequestController>) Class.forName(className, false, pluginManager.getPluginsClassLoader());
            RequestController oldController = RequestControllerMapper.getRequestController(key + ".binary");
            if (oldController != null) {
              // Save masked controllers for extending existing functionality by calling
              // the masked controller's .process()
              RequestControllerMapper.mapController(key, ".binary.masked", oldController);
            }
            RequestControllerMapper.mapController(key, ".binary", pageController.newInstance());
          }
        }
      }
    } catch (Exception e) {
      Logging.logException("Plugins loading failed", e);
    }
  }
  
  private static void trustSelfSignedCerts() {
    try {
      TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
          return null;
        }
  
        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
        }
  
        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
        }
      } };
  
      SSLContext sc = SSLContext.getInstance("SSL");
      sc.init(null, trustAllCerts, new java.security.SecureRandom());
      HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    } catch (Exception e) {
    }
  }
  
  @Resource
  private UserTransaction userTransaction;
}
