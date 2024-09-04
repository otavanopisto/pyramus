package fi.otavanopisto.pyramus.dao.plugins;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.plugins.PluginRepository;
import fi.otavanopisto.pyramus.domainmodel.plugins.PluginRepository_;

@Stateless
@Deprecated
public class PluginRepositoryDAO extends PyramusEntityDAO<PluginRepository> {

  public PluginRepository create(String url, String repositoryId) {
    EntityManager entityManager = getEntityManager();

    PluginRepository pluginRepository = new PluginRepository();
    pluginRepository.setUrl(url);
    pluginRepository.setRepositoryId(repositoryId);

    entityManager.persist(pluginRepository);

    return pluginRepository;
  }

  public PluginRepository findByRepositoryId(String repositoryId) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<PluginRepository> criteria = criteriaBuilder.createQuery(PluginRepository.class);
    Root<PluginRepository> root = criteria.from(PluginRepository.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.equal(root.get(PluginRepository_.repositoryId), repositoryId)
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
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
