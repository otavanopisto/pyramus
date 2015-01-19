package fi.pyramus.util.dataimport.scripting.api;

import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.plugins.PluginDAO;
import fi.pyramus.domainmodel.plugins.Plugin;

public class PluginAPI {

  public PluginAPI(Long loggedUserId) {
    this.loggedUserId = loggedUserId;
  }
  
  public Long create(String groupId, String artifactId, String version, Boolean enabled) {
    PluginDAO pluginDAO = DAOFactory.getInstance().getPluginDAO();
    return pluginDAO.create(artifactId, groupId, version, enabled).getId();
  }
  
  public Long findIdByGroupIdAndArtifact(String groupId, String artifactId) {
    PluginDAO pluginDAO = DAOFactory.getInstance().getPluginDAO();
    Plugin plugin = pluginDAO.findByGroupIdAndArtifactId(groupId, artifactId);
    return plugin != null ? plugin.getId() : null;
  }

  @SuppressWarnings("unused")
  private Long loggedUserId;

}
