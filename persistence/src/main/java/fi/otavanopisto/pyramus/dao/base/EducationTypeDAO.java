package fi.otavanopisto.pyramus.dao.base;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.EducationType;
import fi.pyramus.domainmodel.base.EducationType_;

@Stateless
public class EducationTypeDAO extends PyramusEntityDAO<EducationType> {

  /**
   * Creates a new education type.
   * 
   * @param name
   *          The name of the education type
   * @param code
   *          The code of the education type
   * 
   * @return The created education type
   */
  public EducationType create(String name, String code) {
    EntityManager entityManager = getEntityManager();
    EducationType educationType = new EducationType();
    educationType.setName(name);
    educationType.setCode(code);
    entityManager.persist(educationType);
    return educationType;
  }

  /**
   * Returns the education type corresponding to the given code.
   * 
   * @param code
   *          code of the type
   * 
   * @return The education type corresponding to the given code
   */
  public EducationType findByCode(String code) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<EducationType> criteria = criteriaBuilder.createQuery(EducationType.class);
    Root<EducationType> root = criteria.from(EducationType.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(EducationType_.code), code)
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

  /**
   * Updates the given education type with the given data.
   * 
   * @param educationType
   *          The education type to be updated
   * @param name
   *          The education type name
   */
  public void update(EducationType educationType, String name, String code) {
    EntityManager entityManager = getEntityManager();
    educationType.setName(name);
    educationType.setCode(code);
    entityManager.persist(educationType);
  }

}
