package fi.pyramus.dao.base;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.base.MagicKey;
import fi.pyramus.domainmodel.base.MagicKeyScope;
import fi.pyramus.domainmodel.base.MagicKey_;

@Stateless
public class MagicKeyDAO extends PyramusEntityDAO<MagicKey> {

  public MagicKey create(String name, MagicKeyScope scope) {
    EntityManager entityManager = getEntityManager();

    Date now = new Date(System.currentTimeMillis());

    MagicKey magicKey = new MagicKey();
    magicKey.setCreated(now);
    magicKey.setName(name);
    magicKey.setScope(scope);

    entityManager.persist(magicKey);

    return magicKey;
  }

  public MagicKey findByName(String name) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<MagicKey> criteria = criteriaBuilder.createQuery(MagicKey.class);
    Root<MagicKey> root = criteria.from(MagicKey.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(MagicKey_.name), name)
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }
  
  public MagicKey findByApplicationScope() {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<MagicKey> criteria = criteriaBuilder.createQuery(MagicKey.class);
    Root<MagicKey> root = criteria.from(MagicKey.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(MagicKey_.scope), MagicKeyScope.APPLICATION)
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

  public MagicKey updateName(MagicKey magicKey, String name) {
    EntityManager entityManager = getEntityManager(); 
    
    magicKey.setName(name);
    
    entityManager.persist(magicKey);
    
    return magicKey;
  }
  
  public void deleteDeprecatedMagicKeys() {
    // TODO: Not a DAO method
    Calendar c = Calendar.getInstance();
    c.setTime(new Date());
    c.roll(Calendar.DATE, -1);

    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<MagicKey> criteria = criteriaBuilder.createQuery(MagicKey.class);
    Root<MagicKey> root = criteria.from(MagicKey.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
          criteriaBuilder.lessThan(root.get(MagicKey_.created), c.getTime()),
          criteriaBuilder.equal(root.get(MagicKey_.scope), MagicKeyScope.REQUEST)
        )
    );
    
    List<MagicKey> deprecatedMagicKeys = entityManager.createQuery(criteria).getResultList();
    
    for (MagicKey deprecatedMagicKey : deprecatedMagicKeys) {
      super.delete(deprecatedMagicKey);
    }
  }

  @Override
  public void delete(MagicKey magicKey) {
    super.delete(magicKey);
  }

}
