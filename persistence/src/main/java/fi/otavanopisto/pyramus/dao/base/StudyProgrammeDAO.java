package fi.otavanopisto.pyramus.dao.base;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Organization;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgrammeCategory;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme_;

@Stateless
public class StudyProgrammeDAO extends PyramusEntityDAO<StudyProgramme> {

  public StudyProgramme create(Organization organization, String name, StudyProgrammeCategory category, String code) {
    EntityManager entityManager = getEntityManager();

    StudyProgramme studyProgramme = new StudyProgramme();
    studyProgramme.setOrganization(organization);
    studyProgramme.setName(name);
    studyProgramme.setCategory(category);
    studyProgramme.setCode(code);
    entityManager.persist(studyProgramme);

    return studyProgramme;
  }

  public StudyProgramme findByName(String name) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudyProgramme> criteria = criteriaBuilder.createQuery(StudyProgramme.class);
    Root<StudyProgramme> root = criteria.from(StudyProgramme.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(StudyProgramme_.name), name)
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

  public StudyProgramme findByCode(String code) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudyProgramme> criteria = criteriaBuilder.createQuery(StudyProgramme.class);
    Root<StudyProgramme> root = criteria.from(StudyProgramme.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(StudyProgramme_.code), code)
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

  public StudyProgramme update(StudyProgramme studyProgramme, Organization organization, String name, 
      StudyProgrammeCategory category, String code) {
    studyProgramme.setOrganization(organization);
    studyProgramme.setName(name);
    studyProgramme.setCategory(category);
    studyProgramme.setCode(code);
    return persist(studyProgramme);
  }
  
}
