package fi.otavanopisto.pyramus.dao.base;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Nationality;
import fi.otavanopisto.pyramus.domainmodel.base.Nationality_;

@Stateless
public class NationalityDAO extends PyramusEntityDAO<Nationality> {

  public Nationality create(String name, String code) {
    EntityManager entityManager = getEntityManager();
    
    Nationality nationality = new Nationality();
    nationality.setName(name);
    nationality.setCode(code);
    
    entityManager.persist(nationality);
    return nationality;
  }
  /**
   * Returns the nationality corresponding to the given code.
   * 
   * @param code
   *          The nationality code
   * 
   * @return The nationality corresponding to the given code
   */
  public Nationality findByCode(String code) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Nationality> criteria = criteriaBuilder.createQuery(Nationality.class);
    Root<Nationality> root = criteria.from(Nationality.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(Nationality_.code), code)
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

  public Nationality updateCode(Nationality nationality, String code) {
    EntityManager entityManager = getEntityManager();
    
    nationality.setCode(code);
    
    entityManager.persist(nationality);
    return nationality;
  }

  public Nationality updateName(Nationality nationality, String name) {
    EntityManager entityManager = getEntityManager();
    
    nationality.setName(name);
    
    entityManager.persist(nationality);
    return nationality;
  }
}
