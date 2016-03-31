package fi.otavanopisto.pyramus.plugin;

public interface CustomLoginScreenPlugin extends PluginDescriptor {

  /**
   * Returns context specific Freemarker template for login screen
   * 
   * @param contextType contextType
   * @param contextId contextId
   * @return context specific Freemarker template for login screen
   */
  public String getContextLoginFtl(String contextType, String contextId);
}
