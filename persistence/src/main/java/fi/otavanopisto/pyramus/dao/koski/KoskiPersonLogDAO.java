package fi.otavanopisto.pyramus.dao.koski;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.koski.KoskiPersonLog;
import fi.otavanopisto.pyramus.domainmodel.koski.KoskiPersonLog_;
import fi.otavanopisto.pyramus.domainmodel.koski.KoskiPersonState;

@Stateless
public class KoskiPersonLogDAO extends PyramusEntityDAO<KoskiPersonLog> {

  public KoskiPersonLog create(Person person, KoskiPersonState state, Date date) {
    KoskiPersonLog koskiPersonLog = new KoskiPersonLog();
    
    koskiPersonLog.setPerson(person);
    koskiPersonLog.setState(state);
    koskiPersonLog.setDate(date);

    return persist(koskiPersonLog);
  }

  public List<KoskiPersonLog> listByPerson(Person person) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<KoskiPersonLog> criteria = criteriaBuilder.createQuery(KoskiPersonLog.class);
    Root<KoskiPersonLog> root = criteria.from(KoskiPersonLog.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(KoskiPersonLog_.person), person)
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public KoskiPersonLog findOldestByState(KoskiPersonState state) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<KoskiPersonLog> criteria = criteriaBuilder.createQuery(KoskiPersonLog.class);
    Root<KoskiPersonLog> root = criteria.from(KoskiPersonLog.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(KoskiPersonLog_.state), state)
    );
    criteria.orderBy(criteriaBuilder.asc(root.get(KoskiPersonLog_.date)));
    
    TypedQuery<KoskiPersonLog> query = entityManager.createQuery(criteria);
    
    query.setMaxResults(1);
    
    return getSingleResult(query);
  }
  
}
