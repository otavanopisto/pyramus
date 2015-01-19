package fi.pyramus.dao.plugins;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.plugins.Plugin;
import fi.pyramus.domainmodel.plugins.Plugin_;

@Stateless
public class PluginDAO extends PyramusEntityDAO<Plugin> {

  public Plugin create(String artifactId, String groupId, String version, Boolean enabled) {
    EntityManager entityManager = getEntityManager();

    Plugin plugin = new Plugin();
    plugin.setArtifactId(artifactId);
    plugin.setGroupId(groupId);
    plugin.setVersion(version);
    plugin.setEnabled(enabled);

    entityManager.persist(plugin);

    return plugin;
  }

  public Plugin findByGroupIdAndArtifactId(String groupId, String artifactId) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Plugin> criteria = criteriaBuilder.createQuery(Plugin.class);
    Root<Plugin> root = criteria.from(Plugin.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.and(
        criteriaBuilder.equal(root.get(Plugin_.groupId), groupId),
        criteriaBuilder.equal(root.get(Plugin_.artifactId), artifactId)
      )
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

  public List<Plugin> listByEnabled(Boolean enabled) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Plugin> criteria = criteriaBuilder.createQuery(Plugin.class);
    Root<Plugin> root = criteria.from(Plugin.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.equal(root.get(Plugin_.enabled), enabled)
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public Plugin updateEnabled(Plugin plugin, Boolean enabled) {
    plugin.setEnabled(enabled);
    getEntityManager().persist(plugin);
    return plugin;
  }

  public Plugin updateArtifactId(Plugin plugin, String artifactId) {
    plugin.setArtifactId(artifactId);
    getEntityManager().persist(plugin);
    return plugin;
  }

  public Plugin updateGroupId(Plugin plugin, String groupId) {
    plugin.setGroupId(groupId);
    getEntityManager().persist(plugin);
    return plugin;
  }

  public Plugin updateVersion(Plugin plugin, String version) {
    plugin.setVersion(version);
    getEntityManager().persist(plugin);
    return plugin;
  }

  public void delete(Plugin plugin) {
    super.delete(plugin);
  }
}
