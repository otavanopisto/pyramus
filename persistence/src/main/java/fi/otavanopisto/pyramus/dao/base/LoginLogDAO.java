package fi.otavanopisto.pyramus.dao.base;

import java.util.Date;
import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.LoginLog;
import fi.otavanopisto.pyramus.domainmodel.base.LoginLog_;
import fi.otavanopisto.pyramus.domainmodel.users.User;

@Stateless
public class LoginLogDAO extends PyramusEntityDAO<LoginLog> {
  
  public LoginLog create(User user, Date date){
    LoginLog loginLog = new LoginLog();
    loginLog.setUser(user);
    loginLog.setDate(date);
    return persist(loginLog);
  }
  
  public List<LoginLog> listByUser(User user) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<LoginLog> criteria = criteriaBuilder.createQuery(LoginLog.class);
    Root<LoginLog> root = criteria.from(LoginLog.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(LoginLog_.user), user)
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }
  
}
