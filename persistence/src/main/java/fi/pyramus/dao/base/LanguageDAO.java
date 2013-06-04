package fi.pyramus.dao.base;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.base.Language;
import fi.pyramus.domainmodel.base.Language_;

@Stateless
public class LanguageDAO extends PyramusEntityDAO<Language> {

  /**
   * Returns the language corresponding to the given code.
   * 
   * @param code
   *          The language code
   * 
   * @return The language corresponding to the given code
   */
  public Language findByCode(String code) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Language> criteria = criteriaBuilder.createQuery(Language.class);
    Root<Language> root = criteria.from(Language.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(Language_.code), code)
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

}
