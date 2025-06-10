package fi.otavanopisto.pyramus.dao.clientapplications;

import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.clientapplications.ClientApplication;
import fi.otavanopisto.pyramus.domainmodel.clientapplications.ClientApplicationAccessToken;
import fi.otavanopisto.pyramus.domainmodel.clientapplications.ClientApplicationAuthorizationCode;
import fi.otavanopisto.pyramus.domainmodel.clientapplications.ClientApplicationAccessToken_;

@Stateless
public class ClientApplicationAccessTokenDAO extends PyramusEntityDAO<ClientApplicationAccessToken> {

  public ClientApplicationAccessToken create(String accessToken, String refreshToken, Long expires, ClientApplication clientApplication, ClientApplicationAuthorizationCode clientApplicationAuthorizationCode) {
    EntityManager entityManager = getEntityManager();

    ClientApplicationAccessToken clientApplicationAccessToken = new ClientApplicationAccessToken();
    clientApplicationAccessToken.setAccessToken(accessToken);
    clientApplicationAccessToken.setRefreshToken(refreshToken);
    clientApplicationAccessToken.setClientApplication(clientApplication);
    clientApplicationAccessToken.setExpires(expires);
    clientApplicationAccessToken.setClientApplicationAuthorizationCode(clientApplicationAuthorizationCode);
    
    entityManager.persist(clientApplicationAccessToken);
    return clientApplicationAccessToken;
  }
  
  public ClientApplicationAccessToken findByAccessToken(String accessToken){
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<ClientApplicationAccessToken> criteria = criteriaBuilder.createQuery(ClientApplicationAccessToken.class);
    Root<ClientApplicationAccessToken> root = criteria.from(ClientApplicationAccessToken.class);
    criteria.select(root);
    criteria.where(
            criteriaBuilder.equal(root.get(ClientApplicationAccessToken_.accessToken), accessToken)
        );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }
  
  public ClientApplicationAccessToken findByRefreshToken(String refreshToken){
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<ClientApplicationAccessToken> criteria = criteriaBuilder.createQuery(ClientApplicationAccessToken.class);
    Root<ClientApplicationAccessToken> root = criteria.from(ClientApplicationAccessToken.class);
    criteria.select(root);
    criteria.where(
            criteriaBuilder.equal(root.get(ClientApplicationAccessToken_.refreshToken), refreshToken)
        );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }
  
  public List<ClientApplicationAccessToken> listByExpired(long threshold, int maxResults) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<ClientApplicationAccessToken> criteria = criteriaBuilder.createQuery(ClientApplicationAccessToken.class);
    Root<ClientApplicationAccessToken> root = criteria.from(ClientApplicationAccessToken.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.lessThan(root.get(ClientApplicationAccessToken_.expires), threshold)
    );
    
    return entityManager.createQuery(criteria).setMaxResults(maxResults).getResultList();
  }
  
  public ClientApplicationAccessToken findByAuthCode(ClientApplicationAuthorizationCode clientApplicationAuthorizationCode){
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<ClientApplicationAccessToken> criteria = criteriaBuilder.createQuery(ClientApplicationAccessToken.class);
    Root<ClientApplicationAccessToken> root = criteria.from(ClientApplicationAccessToken.class);
    criteria.select(root);
    criteria.where(
            criteriaBuilder.equal(root.get(ClientApplicationAccessToken_.clientApplicationAuthorizationCode), clientApplicationAuthorizationCode)
        );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }
  
  public ClientApplicationAccessToken updateExpires(ClientApplicationAccessToken clientApplicationAccessToken, long expires){
    EntityManager entityManager = getEntityManager();
    clientApplicationAccessToken.setExpires(expires);
    entityManager.persist(clientApplicationAccessToken);
    return clientApplicationAccessToken;
  }
  
  public ClientApplicationAccessToken updateAccessToken(ClientApplicationAccessToken clientApplicationAccessToken, String accessToken){
    EntityManager entityManager = getEntityManager();
    clientApplicationAccessToken.setAccessToken(accessToken);
    entityManager.persist(clientApplicationAccessToken);
    return clientApplicationAccessToken;
  }

  public ClientApplicationAccessToken updateRefreshToken(ClientApplicationAccessToken clientApplicationAccessToken, String refreshToken) {
    clientApplicationAccessToken.setRefreshToken(refreshToken);
    return persist(clientApplicationAccessToken);
  }
  
  public void delete(ClientApplicationAccessToken clientApplicationAccessToken){
    super.delete(clientApplicationAccessToken);
  }

}
