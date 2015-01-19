package fi.pyramus.util.dataimport.scripting.api;

import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.plugins.PluginDAO;
import fi.pyramus.dao.plugins.PluginRepositoryDAO;
import fi.pyramus.domainmodel.plugins.Plugin;
import fi.pyramus.domainmodel.plugins.PluginRepository;

public class PluginAPI {

  public PluginAPI(Long loggedUserId) {
    this.loggedUserId = loggedUserId;
  }

  public Long createRepository(String repositoryId, String url) {
    PluginRepositoryDAO pluginRepositoryDAO = DAOFactory.getInstance().getPluginRepositoryDAO();
    return pluginRepositoryDAO.create(url, repositoryId).getId();
  }

  public Long findByRepositoryId(String repositoryId) {
    PluginRepositoryDAO pluginRepositoryDAO = DAOFactory.getInstance().getPluginRepositoryDAO();
    PluginRepository pluginRepository = pluginRepositoryDAO.findByRepositoryId(repositoryId);
    if (pluginRepository != null) {
      return pluginRepository.getId();
    }

    return null;
  }
  
  public Long findPluginByGroupIdAndArtifact(String groupId, String artifactId) {
    PluginDAO pluginDAO = DAOFactory.getInstance().getPluginDAO();
    Plugin plugin = pluginDAO.findByGroupIdAndArtifactId(groupId, artifactId);
    return plugin != null ? plugin.getId() : null;
  }
  
  public Long createPlugin(String groupId, String artifactId, String version, Boolean enabled) {
    PluginDAO pluginDAO = DAOFactory.getInstance().getPluginDAO();
    return pluginDAO.create(artifactId, groupId, version, enabled).getId();
  }

  @SuppressWarnings("unused")
  private Long loggedUserId;

}
