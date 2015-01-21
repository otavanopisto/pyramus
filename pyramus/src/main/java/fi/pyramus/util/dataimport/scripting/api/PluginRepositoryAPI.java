package fi.pyramus.util.dataimport.scripting.api;

import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.plugins.PluginRepositoryDAO;
import fi.pyramus.domainmodel.plugins.PluginRepository;

public class PluginRepositoryAPI {

  public PluginRepositoryAPI(Long loggedUserId) {
    this.loggedUserId = loggedUserId;
  }

  public Long create(String repositoryId, String url) {
    PluginRepositoryDAO pluginRepositoryDAO = DAOFactory.getInstance().getPluginRepositoryDAO();
    return pluginRepositoryDAO.create(url, repositoryId).getId();
  }

  public Long findIdByRepositoryId(String repositoryId) {
    PluginRepositoryDAO pluginRepositoryDAO = DAOFactory.getInstance().getPluginRepositoryDAO();
    PluginRepository pluginRepository = pluginRepositoryDAO.findByRepositoryId(repositoryId);
    if (pluginRepository != null) {
      return pluginRepository.getId();
    }

    return null;
  }

  @SuppressWarnings("unused")
  private Long loggedUserId;

}
