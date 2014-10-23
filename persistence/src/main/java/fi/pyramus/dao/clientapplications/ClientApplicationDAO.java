package fi.pyramus.dao.clientapplications;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.clientapplications.ClientApplication;
import fi.pyramus.domainmodel.clientapplications.ClientApplication_;

@Stateless
public class ClientApplicationDAO extends PyramusEntityDAO<ClientApplication> {
  
  public ClientApplication create(String clientName, String clientId, String clientSecret, Boolean skipPrompt ){
    EntityManager entityManager = getEntityManager();
    
    ClientApplication clientApplication = new ClientApplication();
    clientApplication.setClientId(clientId);
    clientApplication.setClientName(clientName);
    clientApplication.setClientSecret(clientSecret);
    clientApplication.setSkipPrompt(skipPrompt);
    
    entityManager.persist(clientApplication);
    return clientApplication;
  }
  
  public ClientApplication findByClientId(String clientId){
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<ClientApplication> criteria = criteriaBuilder.createQuery(ClientApplication.class);
    Root<ClientApplication> root = criteria.from(ClientApplication.class);
    criteria.select(root);
    criteria.where(
            criteriaBuilder.equal(root.get(ClientApplication_.clientId), clientId)
        );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }
  
  public ClientApplication findByClientIdAndClientSecret(String clientId, String clientSecret){
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<ClientApplication> criteria = criteriaBuilder.createQuery(ClientApplication.class);
    Root<ClientApplication> root = criteria.from(ClientApplication.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(ClientApplication_.clientId), clientId),
            criteriaBuilder.equal(root.get(ClientApplication_.clientSecret), clientSecret)
        ));
    
    return getSingleResult(entityManager.createQuery(criteria));
  }
  
  public ClientApplication updateName(ClientApplication clientApplication, String clientName){
    EntityManager entityManager = getEntityManager(); 
    clientApplication.setClientName(clientName);
    entityManager.persist(clientApplication);
    return clientApplication;
  }
  
  public ClientApplication updateClientId(ClientApplication clientApplication, String clientId){
    EntityManager entityManager = getEntityManager();
    clientApplication.setClientId(clientId);
    entityManager.persist(clientApplication);
    return clientApplication;
  }
  
  public ClientApplication updateClientSecret(ClientApplication clientApplication, String clientSecret){
    EntityManager entityManager = getEntityManager();
    clientApplication.setClientSecret(clientSecret);
    entityManager.persist(clientApplication);
    return clientApplication;
  }
  
  public ClientApplication updateSkipPrompt(ClientApplication clientApplication, boolean skipPrompt){
    EntityManager entityManager = getEntityManager();
    clientApplication.setSkipPrompt(skipPrompt);
    entityManager.persist(clientApplication);
    return clientApplication;
  }
  
  public void delete(ClientApplication clientApplication) {
    super.delete(clientApplication);
  }

}
