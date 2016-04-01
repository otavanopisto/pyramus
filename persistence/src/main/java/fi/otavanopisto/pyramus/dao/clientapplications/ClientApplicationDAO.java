package fi.otavanopisto.pyramus.dao.clientapplications;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.clientapplications.ClientApplication;
import fi.otavanopisto.pyramus.domainmodel.clientapplications.ClientApplication_;

@Stateless
public class ClientApplicationDAO extends PyramusEntityDAO<ClientApplication> {

  public ClientApplication create(String clientName, String clientId, String clientSecret, Boolean skipPrompt) {

    ClientApplication clientApplication = new ClientApplication();
    clientApplication.setClientId(clientId);
    clientApplication.setClientName(clientName);
    clientApplication.setClientSecret(clientSecret);
    clientApplication.setSkipPrompt(skipPrompt);

    return persist(clientApplication);
  }

  public ClientApplication findByClientId(String clientId) {
    EntityManager entityManager = getEntityManager();

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<ClientApplication> criteria = criteriaBuilder.createQuery(ClientApplication.class);
    Root<ClientApplication> root = criteria.from(ClientApplication.class);
    criteria.select(root);
    criteria.where(criteriaBuilder.equal(root.get(ClientApplication_.clientId), clientId));

    return getSingleResult(entityManager.createQuery(criteria));
  }

  public ClientApplication findByClientIdAndClientSecret(String clientId, String clientSecret) {
    EntityManager entityManager = getEntityManager();

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<ClientApplication> criteria = criteriaBuilder.createQuery(ClientApplication.class);
    Root<ClientApplication> root = criteria.from(ClientApplication.class);
    criteria.select(root);
    criteria.where(criteriaBuilder.and(criteriaBuilder.equal(root.get(ClientApplication_.clientId), clientId),
        criteriaBuilder.equal(root.get(ClientApplication_.clientSecret), clientSecret)));

    return getSingleResult(entityManager.createQuery(criteria));
  }

  public ClientApplication updateName(ClientApplication clientApplication, String clientName) {
    clientApplication.setClientName(clientName);
    return persist(clientApplication);
  }

  public ClientApplication updateClientId(ClientApplication clientApplication, String clientId) {
    clientApplication.setClientId(clientId);
    return persist(clientApplication);
  }

  public ClientApplication updateClientSecret(ClientApplication clientApplication, String clientSecret) {
    clientApplication.setClientSecret(clientSecret);
    return persist(clientApplication);
  }

  public ClientApplication updateSkipPrompt(ClientApplication clientApplication, boolean skipPrompt) {
    clientApplication.setSkipPrompt(skipPrompt);
    return persist(clientApplication);
  }

  public void delete(ClientApplication clientApplication) {
    super.delete(clientApplication);
  }

}
