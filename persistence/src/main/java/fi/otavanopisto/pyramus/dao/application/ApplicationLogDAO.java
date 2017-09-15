package fi.otavanopisto.pyramus.dao.application;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationLog;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationLog_;
import fi.otavanopisto.pyramus.domainmodel.users.User;

@Stateless
public class ApplicationLogDAO extends PyramusEntityDAO<ApplicationLog> {

  public ApplicationLog create(Application application, String text, User user) {
    EntityManager entityManager = getEntityManager();

    ApplicationLog applicationLog = new ApplicationLog();
    
    applicationLog.setApplication(application);
    applicationLog.setText(text);
    applicationLog.setUser(user);
    applicationLog.setDate(new Date());
    applicationLog.setArchived(Boolean.FALSE);
   
    entityManager.persist(applicationLog);

    return applicationLog;
  }
  
  public List<ApplicationLog> listByApplication(Application application) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<ApplicationLog> criteria = criteriaBuilder.createQuery(ApplicationLog.class);
    Root<ApplicationLog> root = criteria.from(ApplicationLog.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.and(
        criteriaBuilder.equal(root.get(ApplicationLog_.application), application),
        criteriaBuilder.equal(root.get(ApplicationLog_.archived), Boolean.FALSE)
      ));
    criteria.orderBy(criteriaBuilder.desc(root.get(ApplicationLog_.date)));
    
    return entityManager.createQuery(criteria).getResultList();
  }

}