package fi.pyramus.dao.clientapplications;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.clientapplications.ClientApplication;
import fi.pyramus.domainmodel.clientapplications.ClientApplicationAuthorizationCode;
import fi.pyramus.domainmodel.clientapplications.ClientApplicationAuthorizationCode_;
import fi.pyramus.domainmodel.clientapplications.ClientApplication_;
import fi.pyramus.domainmodel.users.User;

@Stateless
public class ClientApplicationAuthorizationCodeDAO extends PyramusEntityDAO<ClientApplicationAuthorizationCode> {

  public ClientApplicationAuthorizationCode create(User user, ClientApplication clientApplication, String authorizationCode, String redirectUrl) {
    EntityManager entityManager = getEntityManager();

    ClientApplicationAuthorizationCode clientApplicationAuthorizationCode = new ClientApplicationAuthorizationCode();
    clientApplicationAuthorizationCode.setUser(user);
    clientApplicationAuthorizationCode.setClientApplication(clientApplication);
    clientApplicationAuthorizationCode.setAuthorizationCode(authorizationCode);
    clientApplicationAuthorizationCode.setRedirectUrl(redirectUrl);
    
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
        ));
    
    return entityManager.createQuery(criteria).getSingleResult();
  }
  
  public void delete(ClientApplicationAuthorizationCode clientApplicationAuthorizationCode){
    super.delete(clientApplicationAuthorizationCode);
  }

}
