package fi.pyramus.dao.plugins;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.plugins.PluginRepository;

@Stateless
public class PluginRepositoryDAO extends PyramusEntityDAO<PluginRepository> {

  public PluginRepository create(String url, String repositoryId) {
    EntityManager entityManager = getEntityManager();

    PluginRepository pluginRepository = new PluginRepository();
    pluginRepository.setUrl(url);
    pluginRepository.setRepositoryId(repositoryId);

    entityManager.persist(pluginRepository);

    return pluginRepository;
  }

  public PluginRepository update(PluginRepository repository, String url, String repositoryId) {
    repository.setUrl(url);
    repository.setRepositoryId(repositoryId);
    getEntityManager().persist(repository);
    return repository;
  }

  public PluginRepository updateUrl(PluginRepository repository, String url) {
    repository.setUrl(url);
    getEntityManager().persist(repository);
    return repository;
  }

  public PluginRepository updateId(PluginRepository repository, String id) {
    repository.setRepositoryId(id);
    getEntityManager().persist(repository);
    return repository;
  }
  
  public void delete(PluginRepository pluginRepository) {
    super.delete(pluginRepository);
  }
}
