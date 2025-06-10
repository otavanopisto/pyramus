package fi.otavanopisto.pyramus.dao.base;

import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.EducationType;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgrammeCategory;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgrammeCategory_;

@Stateless
public class StudyProgrammeCategoryDAO extends PyramusEntityDAO<StudyProgrammeCategory> {

  public StudyProgrammeCategory create(String name, EducationType educationType) {
    EntityManager entityManager = getEntityManager();

    StudyProgrammeCategory studyProgrammeCategory = new StudyProgrammeCategory();
    studyProgrammeCategory.setArchived(Boolean.FALSE);
    studyProgrammeCategory.setName(name);
    studyProgrammeCategory.setEducationType(educationType);

    entityManager.persist(studyProgrammeCategory);

    return studyProgrammeCategory;
  }

  public List<StudyProgrammeCategory> listByName(String name) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudyProgrammeCategory> criteria = criteriaBuilder.createQuery(StudyProgrammeCategory.class);
    Root<StudyProgrammeCategory> root = criteria.from(StudyProgrammeCategory.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.equal(root.get(StudyProgrammeCategory_.name), name)
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public StudyProgrammeCategory update(StudyProgrammeCategory studyProgrammeCategory, String name, EducationType educationType) {
    studyProgrammeCategory.setName(name);
    studyProgrammeCategory.setEducationType(educationType);

    return persist(studyProgrammeCategory);
  }

}