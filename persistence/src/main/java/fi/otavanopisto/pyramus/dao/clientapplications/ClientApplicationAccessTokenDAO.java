package fi.otavanopisto.pyramus.dao.clientapplications;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.clientapplications.ClientApplication;
import fi.otavanopisto.pyramus.domainmodel.clientapplications.ClientApplicationAccessToken;
import fi.otavanopisto.pyramus.domainmodel.clientapplications.ClientApplicationAccessToken_;
import fi.otavanopisto.pyramus.domainmodel.users.User;

@Stateless
public class ClientApplicationAccessTokenDAO extends PyramusEntityDAO<ClientApplicationAccessToken> {

  public ClientApplicationAccessToken create(String accessToken, String refreshToken, ClientApplication clientApplication, User user, Set<String> selectedScopes) {
    EntityManager entityManager = getEntityManager();

    ClientApplicationAccessToken clientApplicationAccessToken = new ClientApplicationAccessToken();
    clientApplicationAccessToken.setAccessToken(accessToken);
    clientApplicationAccessToken.setRefreshToken(refreshToken);
    clientApplicationAccessToken.setClientApplication(clientApplication);
    clientApplicationAccessToken.setUser(user);
    clientApplicationAccessToken.setAccessTokenIssuedAt(Instant.now());
    clientApplicationAccessToken.setRefreshTokenIssuedAt(Instant.now());

    Set<String> scopes = new HashSet<>();
    scopes.addAll(selectedScopes);
    clientApplicationAccessToken.setScopes(scopes);
    
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
  
  public ClientApplicationAccessToken findByClientApplicationAndRefreshToken(ClientApplication clientApplication, String refreshToken) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<ClientApplicationAccessToken> criteria = criteriaBuilder.createQuery(ClientApplicationAccessToken.class);
    Root<ClientApplicationAccessToken> root = criteria.from(ClientApplicationAccessToken.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(ClientApplicationAccessToken_.clientApplication), clientApplication),
            criteriaBuilder.equal(root.get(ClientApplicationAccessToken_.refreshToken), refreshToken)
        )
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }
  
  public List<ClientApplicationAccessToken> listByExpired(Instant threshold, int maxResults) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<ClientApplicationAccessToken> criteria = criteriaBuilder.createQuery(ClientApplicationAccessToken.class);
    Root<ClientApplicationAccessToken> root = criteria.from(ClientApplicationAccessToken.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.lessThan(root.get(ClientApplicationAccessToken_.refreshTokenIssuedAt), threshold)
    );
    
    return entityManager.createQuery(criteria).setMaxResults(maxResults).getResultList();
  }
  
  public ClientApplicationAccessToken refreshAccessToken(ClientApplicationAccessToken clientApplicationAccessToken, String newAccessToken, String newRefreshToken) {
    EntityManager entityManager = getEntityManager();
    clientApplicationAccessToken.setAccessTokenIssuedAt(Instant.now());
    clientApplicationAccessToken.setAccessToken(newAccessToken);
    clientApplicationAccessToken.setRefreshToken(newRefreshToken);
    entityManager.persist(clientApplicationAccessToken);
    return clientApplicationAccessToken;
  }

  public void delete(ClientApplicationAccessToken clientApplicationAccessToken){
    super.delete(clientApplicationAccessToken);
  }

}
