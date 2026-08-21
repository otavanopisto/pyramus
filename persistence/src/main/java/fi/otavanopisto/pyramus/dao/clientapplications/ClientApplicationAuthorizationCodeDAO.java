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
import fi.otavanopisto.pyramus.domainmodel.clientapplications.ClientApplicationAuthorizationCode;
import fi.otavanopisto.pyramus.domainmodel.clientapplications.ClientApplicationAuthorizationCode_;
import fi.otavanopisto.pyramus.domainmodel.users.User;

@Stateless
public class ClientApplicationAuthorizationCodeDAO extends PyramusEntityDAO<ClientApplicationAuthorizationCode> {

  public ClientApplicationAuthorizationCode create(User user, ClientApplication clientApplication, String authorizationCode, String redirectUrl, Set<String> selectedScopes) {
    EntityManager entityManager = getEntityManager();

    ClientApplicationAuthorizationCode clientApplicationAuthorizationCode = new ClientApplicationAuthorizationCode();
    clientApplicationAuthorizationCode.setUser(user);
    clientApplicationAuthorizationCode.setClientApplication(clientApplication);
    clientApplicationAuthorizationCode.setAuthorizationCode(authorizationCode);
    clientApplicationAuthorizationCode.setRedirectUrl(redirectUrl);
    clientApplicationAuthorizationCode.setIssuedAt(Instant.now());
    
    Set<String> scopes = new HashSet<>();
    scopes.addAll(selectedScopes);
    clientApplicationAuthorizationCode.setSelectedScopes(scopes);

    entityManager.persist(clientApplicationAuthorizationCode);
    return clientApplicationAuthorizationCode;
  }
  
  public List<ClientApplicationAuthorizationCode> listByUser(User user){
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<ClientApplicationAuthorizationCode> criteria = criteriaBuilder.createQuery(ClientApplicationAuthorizationCode.class);
    Root<ClientApplicationAuthorizationCode> root = criteria.from(ClientApplicationAuthorizationCode.class);
    criteria.select(root);
    criteria.where(
            criteriaBuilder.equal(root.get(ClientApplicationAuthorizationCode_.user), user)
        );
    
    return entityManager.createQuery(criteria).getResultList();
  }
  
  public List<ClientApplicationAuthorizationCode> listByClientApplication(ClientApplication clientApplication){
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<ClientApplicationAuthorizationCode> criteria = criteriaBuilder.createQuery(ClientApplicationAuthorizationCode.class);
    Root<ClientApplicationAuthorizationCode> root = criteria.from(ClientApplicationAuthorizationCode.class);
    criteria.select(root);
    criteria.where(
            criteriaBuilder.equal(root.get(ClientApplicationAuthorizationCode_.clientApplication), clientApplication)
        );
    
    return entityManager.createQuery(criteria).getResultList();
  }
  
  public List<ClientApplicationAuthorizationCode> listExpired(Instant threshold) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<ClientApplicationAuthorizationCode> criteria = criteriaBuilder.createQuery(ClientApplicationAuthorizationCode.class);
    Root<ClientApplicationAuthorizationCode> root = criteria.from(ClientApplicationAuthorizationCode.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.lessThan(root.get(ClientApplicationAuthorizationCode_.issuedAt), threshold)
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }
  
  public ClientApplicationAuthorizationCode findByClientApplicationAndAuthorizationCode(String authorizationCode, ClientApplication clientApplication){
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<ClientApplicationAuthorizationCode> criteria = criteriaBuilder.createQuery(ClientApplicationAuthorizationCode.class);
    Root<ClientApplicationAuthorizationCode> root = criteria.from(ClientApplicationAuthorizationCode.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.and(
        criteriaBuilder.equal(root.get(ClientApplicationAuthorizationCode_.authorizationCode), authorizationCode),
        criteriaBuilder.equal(root.get(ClientApplicationAuthorizationCode_.clientApplication), clientApplication)
      )
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }
  
  public void delete(ClientApplicationAuthorizationCode clientApplicationAuthorizationCode) {
    super.delete(clientApplicationAuthorizationCode);
  }

}
