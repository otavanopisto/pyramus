package fi.otavanopisto.pyramus.dao.base;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.EducationSubtype;
import fi.otavanopisto.pyramus.domainmodel.base.EducationType;
import fi.pyramus.domainmodel.base.EducationSubtype_;

@Stateless
public class EducationSubtypeDAO extends PyramusEntityDAO<EducationSubtype> {

  /**
   * Creates a new education subtype.
   * 
   * @param educationType
   *          The education type of the subtype
   * @param name
   *          The name of the education subtype
   * @param code
   *          The code of the education subtype
   * 
   * @return The created education subtype
   */
  public EducationSubtype create(EducationType educationType, String name, String code) {
    EducationSubtype educationSubtype = new EducationSubtype();
    
    educationSubtype.setEducationType(educationType);
    educationSubtype.setName(name);
    educationSubtype.setCode(code);

    return persist(educationSubtype);
  }

  /**
   * Returns the education subtype corresponding to the given code.
   * 
   * @param code
   *          code of the subtype
   * 
   * @return The education subtype corresponding to the given code
   */
  public EducationSubtype findByCode(String code) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<EducationSubtype> criteria = criteriaBuilder.createQuery(EducationSubtype.class);
    Root<EducationSubtype> root = criteria.from(EducationSubtype.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(EducationSubtype_.code), code)
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

  /**
   * Returns a list of all education subtypes from the database, sorted by their name.
   * 
   * @param educationType
   *          The education type
   * 
   * @return A list of all education subtypes
   */
  public List<EducationSubtype> listByEducationType(EducationType educationType) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<EducationSubtype> criteria = criteriaBuilder.createQuery(EducationSubtype.class);
    Root<EducationSubtype> root = criteria.from(EducationSubtype.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(EducationSubtype_.archived), Boolean.FALSE),
            criteriaBuilder.equal(root.get(EducationSubtype_.educationType), educationType)
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }

  /**
   * Updates the given education subtype with the given data.
   * 
   * @param educationSubtype
   *          The education subtype to be updated
   * @param name
   *          The education subtype name
   * @return 
   */
  public EducationSubtype update(EducationSubtype educationSubtype, String name, String code) {
    EntityManager entityManager = getEntityManager();
    educationSubtype.setName(name);
    educationSubtype.setCode(code);
    entityManager.persist(educationSubtype);
    return educationSubtype;
  }

  public EducationSubtype updateEducationType(EducationSubtype educationSubtype, EducationType educationType) {
    educationSubtype.setEducationType(educationType);
    return persist(educationSubtype);
  }

}