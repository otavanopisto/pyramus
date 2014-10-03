package fi.internetix.smvc.controllers;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import fi.internetix.smvc.logging.Logging;

public class RequestControllerMapper {

  public static RequestController getRequestController(String controllerName) {
    return requestControllers.get(controllerName);
  }

  @SuppressWarnings("unchecked")
  public final static void mapControllers(Properties properties, String urlPostfix) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    Enumeration<Object> keys = properties.keys();
    while (keys.hasMoreElements()) {
      String name = (String) keys.nextElement();
      
      String className = ((String) properties.get(name)).trim();
      Class<RequestController> controller = (Class<RequestController>) Class.forName(className);
      mapController(name, urlPostfix, controller.newInstance());
    }
  }
  
  public static void mapController(String name, String urlPostfix, RequestController controller) {
    String key = name + urlPostfix;
    requestControllers.put(key, controller);
    Logging.logDebug("Registered: " + key + " : " + controller);
  }

  public final static Map<String, RequestController> getControllers() {
    // TODO: Clone map?
    return requestControllers;
  }
  
  /**
   * Hashmap for page request controller names and their instances.
   */
  private static Map<String, RequestController> requestControllers = new HashMap<String, RequestController>();

}
