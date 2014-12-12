package fi.pyramus.dao.clientapplications;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.clientapplications.ClientApplication;
import fi.pyramus.domainmodel.clientapplications.ClientApplicationAccessToken;
import fi.pyramus.domainmodel.clientapplications.ClientApplicationAccessToken_;
import fi.pyramus.domainmodel.clientapplications.ClientApplicationAuthorizationCode;

@Stateless
public class ClientApplicationAccessTokenDAO extends PyramusEntityDAO<ClientApplicationAccessToken> {

  public ClientApplicationAccessToken create(String accessToken,/* String refreshToken,*/ Long expires, ClientApplication clientApplication, ClientApplicationAuthorizationCode clientApplicationAuthorizationCode) {
    EntityManager entityManager = getEntityManager();

    ClientApplicationAccessToken clientApplicationAccessToken = new ClientApplicationAccessToken();
    clientApplicationAccessToken.setAccessToken(accessToken);
    //clientApplicationAccessToken.setRefreshToken(refreshToken);
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
  
  public void delete(ClientApplicationAccessToken clientApplicationAccessToken){
    super.delete(clientApplicationAccessToken);
  }

}
